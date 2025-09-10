package form;

import controller.UsuarioController;
import dao.impl.UsuarioDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Usuario;
import swing.Button;
import swing.ImageAvatar;
import swing.scrollbar.ScrollBarCustom;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import util.ImageUtils;

/**
 * Form that lists usuarios fetched from the database with card and list views.
 */
public class UsuarioForm extends JPanel {

    private final UsuarioController controller;
    private final String PLACEHOLDER = "Search users...";

    private JTextField txtSearch;
    private JButton btnToggleView;
    private Button btnAdd;
    private JPanel viewContainer;
    private java.awt.CardLayout viewLayout;
    private JPanel cardsPanel;
    private JPanel listPanel;
    private JPanel emptyPanel;
    private JPanel paginationPanel;
    private JButton btnPrev;
    private JButton btnNext;

    private List<Usuario> allUsuarios = new ArrayList<>();
    private List<Usuario> filteredUsuarios = new ArrayList<>();
    private boolean showingCards = true;
    private final int PAGE_SIZE = 6;
    private int currentPage = 0;

    public UsuarioForm() {
        controller = new UsuarioController(new UsuarioDaoNativeImpl());
        initComponents();
        loadUsuarios();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        btnAdd.setIcon(new ImageIcon(getClass().getResource("/icon/11.png")));
        btnAdd.setText("");
        btnAdd.addActionListener(e -> adicionarUsuario());

        topBar.add(txtSearch);
        topBar.add(btnToggleView);
        topBar.add(btnAdd);

        add(topBar, BorderLayout.NORTH);

        // View container with CardLayout
        viewLayout = new java.awt.CardLayout();
        viewContainer = new JPanel(viewLayout);

        // Cards view
        cardsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        cardsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane cardScroll = new JScrollPane(cardsPanel);
        cardScroll.setVerticalScrollBar(new ScrollBarCustom());
        cardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cardScroll.getVerticalScrollBar().setUnitIncrement(16);
        viewContainer.add(cardScroll, "cards");

        // List view (vertical cards)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane listScroll = new JScrollPane(listPanel);
        listScroll.setVerticalScrollBar(new ScrollBarCustom());
        listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        listScroll.getVerticalScrollBar().setUnitIncrement(16);
        viewContainer.add(listScroll, "list");

        // Empty view
        emptyPanel = new JPanel(new BorderLayout());
        JLabel lblEmpty = new JLabel("No users found", JLabel.CENTER);
        emptyPanel.add(lblEmpty, BorderLayout.CENTER);
        viewContainer.add(emptyPanel, "empty");

        add(viewContainer, BorderLayout.CENTER);

        // Pagination panel
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrev = new JButton("Previous");
        btnPrev.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                refreshPage();
            }
        });
        btnNext = new JButton("Next");
        btnNext.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < filteredUsuarios.size()) {
                currentPage++;
                refreshPage();
            }
        });
        add(paginationPanel, BorderLayout.SOUTH);
    }

    private void loadUsuarios() {
        allUsuarios = controller.listar();
        applyFilter();
    }

    private void applyFilter() {
        String text = getFilterText().toLowerCase();
        filteredUsuarios = allUsuarios.stream()
                .filter(u -> u.getNome().toLowerCase().contains(text)
                        || u.getEmail().toLowerCase().contains(text)
                        || u.getCpf().toLowerCase().contains(text))
                .collect(Collectors.toList());

        if (filteredUsuarios.isEmpty()) {
            viewLayout.show(viewContainer, "empty");
            paginationPanel.setVisible(false);
        } else {
            currentPage = 0;
            refreshPage();
            paginationPanel.setVisible(true);
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

    private void populateList() {
        listPanel.removeAll();
        for (Usuario u : getPageUsuarios()) {
            JComponent item = createListItem(u);
            listPanel.add(item);
            listPanel.add(Box.createVerticalStrut(10));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void populateCards() {
        cardsPanel.removeAll();
        for (Usuario u : getPageUsuarios()) {
            JComponent card = createCard(u);
            card.setPreferredSize(new Dimension(200, 200));
            cardsPanel.add(card);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private List<Usuario> getPageUsuarios() {
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filteredUsuarios.size());
        return filteredUsuarios.subList(start, end);
    }

    private void refreshPage() {
        populateCards();
        populateList();
        updateView();
        updatePagination();
    }

    private void updatePagination() {
        paginationPanel.removeAll();
        int totalPages = (int) Math.ceil(filteredUsuarios.size() / (double) PAGE_SIZE);
        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled(currentPage < totalPages - 1);
        paginationPanel.add(btnPrev);
        for (int i = 0; i < totalPages; i++) {
            JButton btn = new JButton(String.valueOf(i + 1));
            final int page = i;
            btn.setEnabled(page != currentPage);
            btn.addActionListener(e -> {
                currentPage = page;
                refreshPage();
            });
            paginationPanel.add(btn);
        }
        paginationPanel.add(btnNext);
        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JComponent createCard(Usuario u) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new javax.swing.border.LineBorder(new Color(230, 230, 230), 1, true));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x15, 0x65, 0xC0));
        JLabel lblName = new JLabel(u.getNome());
        lblName.setForeground(Color.WHITE);
        lblName.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel lblMenu = new JLabel(new ImageIcon(getClass().getResource("/icon/menu.png")));
        lblMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblMenu.setBorder(new EmptyBorder(0, 0, 0, 5));

        JPopupMenu popup = new JPopupMenu();
        JMenuItem miEdit = new JMenuItem("Editar");
        miEdit.addActionListener(e -> editarUsuario(u));
        JMenuItem miDelete = new JMenuItem("Excluir");
        miDelete.addActionListener(e -> excluirUsuario(u));
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
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(10, 10, 10, 10));

        ImageAvatar avatar = new ImageAvatar();
        avatar.setPreferredSize(new Dimension(64, 64));
        ImageIcon icon = ImageUtils.bytesToImageIcon(u.getFoto());
        if (icon != null) {
            avatar.setIcon(icon);
        } else {
            avatar.setIcon(new ImageIcon(getClass().getResource("/icon/profile.jpg")));
        }
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(avatar);
        body.add(javax.swing.Box.createVerticalStrut(5));

        JLabel lblEmail = new JLabel(u.getEmail());
        lblEmail.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EMAIL, 16, Color.DARK_GRAY));
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(lblEmail);

        JLabel lblCpf = new JLabel(u.getCpf());
        lblCpf.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PERM_IDENTITY, 16, Color.DARK_GRAY));
        lblCpf.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(lblCpf);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private JComponent createListItem(Usuario u) {
        JComponent card = createCard(u);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        return card;
    }

    private void adicionarUsuario() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        UsuarioDialog dialog = new UsuarioDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getUsuario());
                loadUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarUsuario(Usuario u) {
        try {
            Usuario completo = controller.buscarComFotoPorId(u.getIdUsuario());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            UsuarioDialog dialog = new UsuarioDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getUsuario());
                    loadUsuarios();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirUsuario(Usuario u) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(u.getIdUsuario());
                loadUsuarios();
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

