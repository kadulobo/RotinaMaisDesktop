package form;

import component.Card;
import controller.FornecedorController;
import dao.impl.FornecedorDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Fornecedor;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

/**
 * Formulário para gerenciamento de Fornecedores.
 */
public class FornecedorForm extends JPanel {

    private final FornecedorController controller;
    private Card cardTotal;
    private Card cardOnline;
    private Card cardOffline;
    private Table table;
    private JTextField txtBusca;
    private Button btnTodos;
    private Button btnOnline;
    private Button btnOffline;
    private Button btnNovo;
    private Boolean filtroOnline;
    private List<Fornecedor> fornecedores;
    private List<Fornecedor> filtrados;
    private javax.swing.Icon iconTotal;
    private javax.swing.Icon iconOnline;
    private javax.swing.Icon iconOffline;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public FornecedorForm() {
        controller = new FornecedorController(new FornecedorDaoNativeImpl());
        initComponents();
        carregarFornecedores();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 18, 0));
        cards.setBackground(Color.WHITE);

        iconTotal = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHOPPING_CART, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(63, 81, 181));
        cardTotal.setColorGradient(new Color(92, 107, 192));
        cardTotal.setData(new ModelCard("Fornecedores", 0, 0, iconTotal));
        cards.add(cardTotal);

        iconOnline = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.WIFI, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardOnline = new Card();
        cardOnline.setBackground(new Color(76, 175, 80));
        cardOnline.setColorGradient(new Color(129, 199, 132));
        cardOnline.setData(new ModelCard("Online", 0, 0, iconOnline));
        cards.add(cardOnline);

        iconOffline = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PORTABLE_WIFI_OFF, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardOffline = new Card();
        cardOffline.setBackground(new Color(244, 67, 54));
        cardOffline.setColorGradient(new Color(239, 83, 80));
        cardOffline.setData(new ModelCard("Offline", 0, 0, iconOffline));
        cards.add(cardOffline);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnTodos = new Button();
        btnTodos.setText("Todos");
        btnTodos.setBackground(new Color(63, 81, 181));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.addActionListener(e -> {
            filtroOnline = null;
            aplicarFiltros();
        });
        filtro.add(btnTodos);

        btnOnline = new Button();
        btnOnline.setText("Online");
        btnOnline.setBackground(new Color(76, 175, 80));
        btnOnline.setForeground(Color.WHITE);
        btnOnline.addActionListener(e -> {
            filtroOnline = Boolean.TRUE;
            aplicarFiltros();
        });
        filtro.add(btnOnline);

        btnOffline = new Button();
        btnOffline.setText("Offline");
        btnOffline.setBackground(new Color(158, 158, 158));
        btnOffline.setForeground(Color.BLACK);
        btnOffline.addActionListener(e -> {
            filtroOnline = Boolean.FALSE;
            aplicarFiltros();
        });
        filtro.add(btnOffline);

        txtBusca = new JTextField(15);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltros();
            }
        });
        filtro.add(new JLabel("Pesquisar:"));
        filtro.add(txtBusca);

        btnNovo = new Button();
        btnNovo.setBackground(new Color(63, 81, 181));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 20, Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30, 30));
        btnNovo.addActionListener(e -> adicionarFornecedor());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Nome", "Endereço", "Status", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        });
        table.setRowHeight(40);
        JScrollPane scroll = new JScrollPane(table);
        table.fixTable(scroll);
        table.setRowSelectionAllowed(false);
        add(scroll, BorderLayout.CENTER);

        btnPrevPage = new JButton("Anterior");
        btnNextPage = new JButton("Próximo");
        btnPrevPage.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });
        btnNextPage.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < (filtrados != null ? filtrados.size() : 0)) {
                currentPage++;
                updatePage();
            }
        });
        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pagination.setBackground(Color.WHITE);
        pagination.add(btnPrevPage);
        pagination.add(btnNextPage);
        add(pagination, BorderLayout.SOUTH);
    }

    private void carregarFornecedores() {
        fornecedores = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (fornecedores == null) {
            return;
        }
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = fornecedores.stream()
                .filter(f -> filtroOnline == null || (f.getOnline() != null && filtroOnline.equals(f.getOnline())))
                .filter(f -> filtro == null || matchesBusca(f, filtro))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private boolean matchesBusca(Fornecedor f, String filtro) {
        return (f.getNome() != null && f.getNome().toLowerCase().contains(filtro))
                || (f.getEndereco() != null && f.getEndereco().toLowerCase().contains(filtro));
    }

    private void atualizarCards(List<Fornecedor> lista) {
        long online = lista.stream().filter(f -> Boolean.TRUE.equals(f.getOnline())).count();
        long offline = lista.stream().filter(f -> Boolean.FALSE.equals(f.getOnline())).count();
        cardTotal.setData(new ModelCard("Fornecedores", lista.size(), 0, iconTotal));
        cardOnline.setData(new ModelCard("Online", (int) online, 0, iconOnline));
        cardOffline.setData(new ModelCard("Offline", (int) offline, 0, iconOffline));
    }

    private void atualizarTabela(List<Fornecedor> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Fornecedor> eventAction = new EventAction<Fornecedor>() {
            @Override
            public void delete(Fornecedor f) {
                excluirFornecedor(f);
            }

            @Override
            public void update(Fornecedor f) {
                editarFornecedor(f);
            }
        };
        for (Fornecedor f : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(f.getFoto());
            if (icon == null) {
                icon = new ImageIcon(getClass().getResource("/icon/profile.jpg"));
            }
            model.addRow(new Object[]{
                icon,
                f.getNome(),
                f.getEndereco(),
                statusTexto(f.getOnline()),
                new ModelAction<>(f, eventAction)
            });
        }
        table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                ImageAvatar avatar = new ImageAvatar();
                avatar.setPreferredSize(new Dimension(40, 40));
                if (value instanceof ImageIcon) {
                    avatar.setIcon((ImageIcon) value);
                }
                return avatar;
            }
        });
        int statusCol = table.getColumnModel().getColumnCount() - 2;
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorderPainted(false);
                lbl.setFocusPainted(false);
                lbl.setContentAreaFilled(true);
                lbl.setPreferredSize(new Dimension(80, 28));
                if ("Online".equals(value)) {
                    lbl.setBackground(new Color(76, 175, 80));
                    lbl.setForeground(Color.WHITE);
                } else if ("Offline".equals(value)) {
                    lbl.setBackground(new Color(158, 158, 158));
                    lbl.setForeground(Color.BLACK);
                } else {
                    lbl.setBackground(new Color(189, 189, 189));
                    lbl.setForeground(Color.BLACK);
                }
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Fornecedor> page = getCurrentPage();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Fornecedor> getCurrentPage() {
        if (filtrados == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtrados.size());
        return filtrados.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtrados != null ? filtrados.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarFornecedor() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        FornecedorDialog dialog = new FornecedorDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getFornecedor());
                carregarFornecedores();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarFornecedor(Fornecedor f) {
        try {
            Fornecedor completo = controller.buscarComFotoPorId(f.getIdFornecedor());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            FornecedorDialog dialog = new FornecedorDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getFornecedor());
                    carregarFornecedores();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirFornecedor(Fornecedor f) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir fornecedor?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(f.getIdFornecedor());
                carregarFornecedores();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String statusTexto(Boolean online) {
        if (online == null) {
            return "Indefinido";
        }
        return online ? "Online" : "Offline";
    }
}
