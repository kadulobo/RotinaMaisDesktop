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
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.AbstractCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.Icon;
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
    private JButton btnCardView;
    private JButton btnListView;
    private Button btnAdd;
    private JPanel viewContainer;
    private java.awt.CardLayout viewLayout;
    private JPanel cardsPanel;
    private javax.swing.JTable table;
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

        btnCardView = new JButton();
        btnCardView.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VIEW_MODULE, 18, Color.BLACK));
        btnCardView.addActionListener(e -> {
            showingCards = true;
            updateView();
        });

        btnListView = new JButton();
        btnListView.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VIEW_LIST, 18, Color.BLACK));
        btnListView.addActionListener(e -> {
            showingCards = false;
            updateView();
        });

        btnAdd = new Button();
        btnAdd.setBackground(new Color(75, 134, 253));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setIcon(new ImageIcon(getClass().getResource("/icon/11.png")));
        btnAdd.setText("");
        btnAdd.addActionListener(e -> adicionarUsuario());

        topBar.add(txtSearch);
        topBar.add(btnCardView);
        topBar.add(btnListView);
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

        // List view (table)
        table = new javax.swing.JTable();
        table.setRowHeight(60);
        table.setAutoCreateRowSorter(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(0xE0, 0xE0, 0xE0));
        table.setIntercellSpacing(new Dimension(4, 8));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getModel() instanceof UsuarioTableModel) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        int modelRow = table.convertRowIndexToModel(row);
                        Usuario u = ((UsuarioTableModel) table.getModel()).getUsuarioAt(modelRow);
                        editarUsuario(u);
                    }
                }
            }
        });

        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                swing.table.TableHeader h = new swing.table.TableHeader(String.valueOf(value));
                if (column == table.getColumnCount() - 1) {
                    h.setHorizontalAlignment(javax.swing.JLabel.CENTER);
                }
                return h;
            }
        });

        JScrollPane listScroll = new JScrollPane(table);
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
        btnPrev = new JButton();
        btnPrev.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHEVRON_LEFT, 18, Color.BLACK));
        btnPrev.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                refreshPage();
            }
        });
        btnNext = new JButton();
        btnNext.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHEVRON_RIGHT, 18, Color.BLACK));
        btnNext.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < filteredUsuarios.size()) {
                currentPage++;
                refreshPage();
            }
        });
        add(paginationPanel, BorderLayout.SOUTH);

        updateView();
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
            btnCardView.setEnabled(false);
            btnListView.setEnabled(true);
        } else {
            viewLayout.show(viewContainer, "list");
            btnCardView.setEnabled(true);
            btnListView.setEnabled(false);
        }
    }

    private void populateTable() {
        if (table.getModel() instanceof UsuarioTableModel) {
            ((UsuarioTableModel) table.getModel()).setUsuarios(getPageUsuarios());
        } else {
            table.setModel(new UsuarioTableModel(getPageUsuarios()));
            table.getColumnModel().getColumn(0).setMinWidth(72);
            table.getColumnModel().getColumn(0).setMaxWidth(72);
            table.getColumnModel().getColumn(0).setPreferredWidth(72);
            table.getColumnModel().getColumn(1).setPreferredWidth(220);
            table.getColumnModel().getColumn(2).setPreferredWidth(240);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setMinWidth(110);
            table.getColumnModel().getColumn(4).setMaxWidth(130);
            table.getColumnModel().getColumn(4).setPreferredWidth(120);

            table.getColumnModel().getColumn(0).setCellRenderer(new AvatarRenderer());
            table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
            ActionCell actionCell = new ActionCell();
            table.getColumnModel().getColumn(4).setCellRenderer(actionCell);
            table.getColumnModel().getColumn(4).setCellEditor(actionCell);
        }
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
        populateTable();
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

    private Icon createInitialsIcon(String name) {
        int size = 36;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(200, 200, 200));
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
        String initials = getInitials(name);
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initials)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(initials, x, y);
        g2.dispose();
        return new ImageIcon(img);
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++) {
            sb.append(Character.toUpperCase(parts[i].charAt(0)));
        }
        return sb.toString();
    }

    private class AvatarRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Usuario u = (Usuario) value;
            ImageAvatar avatar = new ImageAvatar();
            avatar.setPreferredSize(new Dimension(36, 36));
            ImageIcon icon = ImageUtils.bytesToImageIcon(u.getFoto());
            if (icon != null) {
                avatar.setIcon(icon);
            } else {
                avatar.setIcon(createInitialsIcon(u.getNome()));
            }
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panel.add(avatar);
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return panel;
        }
    }

    private class StatusRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setHorizontalAlignment(JLabel.LEFT);
            lbl.setOpaque(true);
            String status = String.valueOf(value);
            if ("Blocked".equalsIgnoreCase(status)) {
                lbl.setBackground(new Color(0xEEEEEE));
                lbl.setForeground(Color.DARK_GRAY);
            } else {
                lbl.setBackground(new Color(0xE8F5E9));
                lbl.setForeground(new Color(0x2E7D32));
            }
            return lbl;
        }
    }

    private class ActionCell extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        private final JButton btnEdit = new JButton("\u270E");
        private final JButton btnDelete = new JButton("\uD83D\uDDD1\uFE0F");
        private Usuario current;

        public ActionCell() {
            btnEdit.setBorder(null);
            btnEdit.setContentAreaFilled(false);
            btnDelete.setBorder(null);
            btnDelete.setContentAreaFilled(false);
            btnEdit.addActionListener(e -> {
                if (current != null) {
                    editarUsuario(current);
                }
                fireEditingStopped();
            });
            btnDelete.addActionListener(e -> {
                if (current != null) {
                    excluirUsuario(current);
                }
                fireEditingStopped();
            });
            panel.add(btnEdit);
            panel.add(btnDelete);
            panel.setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return panel;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            current = (Usuario) value;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return current;
        }
    }

    private static class UsuarioTableModel extends AbstractTableModel {
        private final String[] cols = {"Avatar", "Nome", "Email", "Status", "Ações"};
        private List<Usuario> usuarios;

        public UsuarioTableModel(List<Usuario> usuarios) {
            this.usuarios = new ArrayList<>(usuarios);
        }

        public void setUsuarios(List<Usuario> usuarios) {
            this.usuarios = new ArrayList<>(usuarios);
            fireTableDataChanged();
        }

        public Usuario getUsuarioAt(int row) {
            return usuarios.get(row);
        }

        @Override
        public int getRowCount() {
            return usuarios.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int column) {
            return cols[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Usuario u = usuarios.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return u;
                case 1:
                    return u.getNome();
                case 2:
                    return u.getEmail();
                case 3:
                    return "Active";
                case 4:
                    return u;
                default:
                    return null;
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0 || columnIndex == 4) {
                return Usuario.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 4;
        }
    }
}

