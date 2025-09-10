package form;

import controller.CategoriaController;
import dao.impl.CategoriaDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import model.Categoria;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

/**
 * Form that lists categorias fetched from the database with card and list views.
 */
public class CategoriaForm extends JPanel {

    private final CategoriaController controller;
    private final String PLACEHOLDER = "Search categories...";

    private JTextField txtSearch;
    private JButton btnToggleView;
    private Button btnAdd;
    private JPanel viewContainer;
    private java.awt.CardLayout viewLayout;
    private JPanel cardsPanel;
    private JPanel cardsTopPanel;
    private JPanel cardsBottomPanel;
    private Table table;
    private JPanel emptyPanel;
    private JButton btnPrevPage;
    private JButton btnNextPage;

    private List<Categoria> allCategorias = new ArrayList<>();
    private List<Categoria> filteredCategorias = new ArrayList<>();
    private boolean showingCards = true;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public CategoriaForm() {
        controller = new CategoriaController(new CategoriaDaoNativeImpl());
        initComponents();
        loadCategorias();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(Color.WHITE);
        txtSearch = new JTextField(20);
        txtSearch.setText(PLACEHOLDER);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals(PLACEHOLDER)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText(PLACEHOLDER);
                }
            }
        });
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilter(); }

            @Override
            public void removeUpdate(DocumentEvent e) { applyFilter(); }

            @Override
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });

        btnToggleView = new JButton();
        btnToggleView.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VIEW_LIST, 18, Color.BLACK));
        btnToggleView.addActionListener(e -> {
            showingCards = !showingCards;
            updateView();
        });

        btnAdd = new Button();
        btnAdd.setBackground(new Color(75, 134, 253));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 18, Color.WHITE));
        btnAdd.setText("Adicionar");
        btnAdd.addActionListener(e -> adicionarCategoria());

        topBar.add(txtSearch);
        topBar.add(btnToggleView);
        topBar.add(btnAdd);

        add(topBar, BorderLayout.NORTH);

        // View container with CardLayout
        viewLayout = new java.awt.CardLayout();
        viewContainer = new JPanel(viewLayout);
        viewContainer.setBackground(Color.WHITE);

        // Cards view split into two rows of three cards
        int buttonHeight = btnAdd.getPreferredSize().height;
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBorder(new EmptyBorder(buttonHeight, 10, 10, 10));
        cardsPanel.setBackground(Color.WHITE);

        cardsTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsTopPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsBottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardsTopPanel.setBackground(Color.WHITE);
        cardsBottomPanel.setBackground(Color.WHITE);
        cardsPanel.add(cardsTopPanel);
        cardsPanel.add(Box.createVerticalStrut(buttonHeight));
        cardsPanel.add(cardsBottomPanel);

        JScrollPane cardScroll = new JScrollPane(cardsPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        cardScroll.getViewport().setBackground(Color.WHITE);
        viewContainer.add(cardScroll, "cards");

        // List view
        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "Descrição", ""}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0 && row < filteredCategorias.size()) {
                        editarCategoria(filteredCategorias.get(row));
                    }
                }
            }
        });
        JScrollPane listScroll = new JScrollPane(table);
        table.fixTable(listScroll);
        listScroll.getViewport().setBackground(Color.WHITE);
        viewContainer.add(listScroll, "list");

        // Empty view
        emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(Color.WHITE);
        JLabel lblEmpty = new JLabel("No categories found", JLabel.CENTER);
        emptyPanel.add(lblEmpty, BorderLayout.CENTER);
        viewContainer.add(emptyPanel, "empty");

        add(viewContainer, BorderLayout.CENTER);

        // Pagination controls
        btnPrevPage = new JButton("Anterior");
        btnNextPage = new JButton("Próximo");
        btnPrevPage.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });
        btnNextPage.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < filteredCategorias.size()) {
                currentPage++;
                updatePage();
            }
        });
        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pagination.add(btnPrevPage);
        pagination.add(btnNextPage);
        add(pagination, BorderLayout.SOUTH);
    }

    private void loadCategorias() {
        allCategorias = controller.listar();
        applyFilter();
    }

    private void applyFilter() {
        String text = getFilterText().toLowerCase();
        filteredCategorias = allCategorias.stream()
                .filter(c -> c.getNome().toLowerCase().contains(text)
                        || (c.getDescricao() != null && c.getDescricao().toLowerCase().contains(text)))
                .collect(Collectors.toList());

        if (filteredCategorias.isEmpty()) {
            viewLayout.show(viewContainer, "empty");
            btnPrevPage.setEnabled(false);
            btnNextPage.setEnabled(false);
        } else {
            currentPage = 0;
            updatePage();
        }
    }

    private void updateView() {
        if (showingCards) {
            viewLayout.show(viewContainer, "cards");
            btnToggleView.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VIEW_LIST, 18, Color.BLACK));
        } else {
            viewLayout.show(viewContainer, "list");
            btnToggleView.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VIEW_MODULE, 18, Color.BLACK));
        }
    }

    private void updatePage() {
        populateCards();
        populateTable();
        updatePaginationButtons();
        updateView();
    }

    private List<Categoria> getCurrentPageCategorias() {
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filteredCategorias.size());
        return filteredCategorias.subList(start, end);
    }

    private void populateTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Categoria> eventAction = new EventAction<Categoria>() {
            @Override
            public void delete(Categoria u) {
                excluirCategoria(c);
            }

            @Override
            public void update(Categoria u) {
                editarCategoria(c);
            }
        };
        for (Categoria c : getCurrentPageCategorias()) {
            model.addRow(new Object[]{c.getIdCategoria(), c.getNome(), c.getDescricao(), new ModelAction<>(c, eventAction)});
        }
    }

    private void populateCards() {
        cardsTopPanel.removeAll();
        cardsBottomPanel.removeAll();
        List<Categoria> categorias = getCurrentPageCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            JComponent card = createCard(categorias.get(i));
            if (i < 3) {
                cardsTopPanel.add(card);
            } else {
                cardsBottomPanel.add(card);
            }
        }
        cardsTopPanel.revalidate();
        cardsTopPanel.repaint();
        cardsBottomPanel.revalidate();
        cardsBottomPanel.repaint();
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void updatePaginationButtons() {
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < filteredCategorias.size());
    }

    private JComponent createCard(Categoria c) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(260, 180));
        card.setBackground(Color.WHITE);
        card.setBorder(new javax.swing.border.LineBorder(new Color(230, 230, 230), 1, true));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x15, 0x65, 0xC0));
        JLabel lblName = new JLabel(c.getNome());
        lblName.setForeground(Color.WHITE);
        lblName.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel lblMenu = new JLabel(new ImageIcon(getClass().getResource("/icon/menu.png")));
        lblMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblMenu.setBorder(new EmptyBorder(0, 0, 0, 5));

        JPopupMenu popup = new JPopupMenu();
        JMenuItem miEdit = new JMenuItem("Editar");
        miEdit.addActionListener(e -> editarCategoria(c));
        JMenuItem miDelete = new JMenuItem("Excluir");
        miDelete.addActionListener(e -> excluirCategoria(c));
        popup.add(miEdit);
        popup.add(miDelete);
        lblMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                popup.show(lblMenu, e.getX(), e.getY());
            }
        });

        header.add(lblName, BorderLayout.WEST);
        header.add(lblMenu, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        // Body
        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 10, 10, 10));

        ImageAvatar avatar = new ImageAvatar();
        avatar.setPreferredSize(new Dimension(90, 90));
        ImageIcon icon = ImageUtils.bytesToImageIcon(c.getFoto());
        if (icon != null) {
            avatar.setIcon(icon);
        } else {
            avatar.setIcon(new ImageIcon(getClass().getResource("/icon/profile.jpg")));
        }
        body.add(avatar, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblDescricao = new JLabel(c.getDescricao() != null ? c.getDescricao() : "");
        infoPanel.add(lblDescricao);

        body.add(infoPanel, BorderLayout.CENTER);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private void adicionarCategoria() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        CategoriaDialog dialog = new CategoriaDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getCategoria());
                loadCategorias();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCategoria(Categoria c) {
        try {
            Categoria completo = controller.buscarPorId(c.getIdCategoria());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            CategoriaDialog dialog = new CategoriaDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getCategoria());
                    loadCategorias();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCategoria(Categoria c) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir categoria?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                // Obter sempre o ID da linha atualmente editada/selecionada
                int row = table.getEditingRow();
                if (row < 0) {
                    row = table.getSelectedRow();
                }
                int id = c.getIdCategoria();
                if (row >= 0) {
                    Object val = table.getValueAt(row, 0);
                    if (val instanceof Integer) {
                        id = (Integer) val;
                    }
                }
                controller.remover(id);
                loadCategorias();
                table.clearSelection();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFilterText() {
        String text = txtSearch.getText();
        return PLACEHOLDER.equals(text) ? "" : text;
    }
}

