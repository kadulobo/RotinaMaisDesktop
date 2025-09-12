package form;

import controller.CategoriaController;
import dao.impl.CategoriaDaoNativeImpl;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Categoria;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import component.Card;
import util.ImageUtils;

/**
 * Formulário para gerenciamento de Categorias.
 */
public class CategoriaForm extends JPanel {

    private final CategoriaController controller;
    private Card cardTotal;
    private Card cardAtivas;
    private Card cardInativas;
    private Table table;
    private JTextField txtBusca;
    private Button btnAtiva;
    private Button btnInativa;
    private Button btnNovo;
    private Integer filtroStatus;
    private List<Categoria> categorias;
    private List<Categoria> filtradas;
    private javax.swing.Icon iconTotal;
    private javax.swing.Icon iconAtiva;
    private javax.swing.Icon iconInativa;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public CategoriaForm() {
        controller = new CategoriaController(new CategoriaDaoNativeImpl());
        initComponents();
        carregarCategorias();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconTotal = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LABEL, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(33,150,243));
        cardTotal.setColorGradient(new Color(33,203,243));
        cardTotal.setData(new ModelCard("Categorias",0,0,iconTotal));
        cards.add(cardTotal);

        iconAtiva = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK_CIRCLE, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardAtivas = new Card();
        cardAtivas.setBackground(new Color(76,175,80));
        cardAtivas.setColorGradient(new Color(129,199,132));
        cardAtivas.setData(new ModelCard("Ativas",0,0,iconAtiva));
        cards.add(cardAtivas);

        iconInativa = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardInativas = new Card();
        cardInativas.setBackground(new Color(255,152,0));
        cardInativas.setColorGradient(new Color(255,203,107));
        cardInativas.setData(new ModelCard("Inativas",0,0,iconInativa));
        cards.add(cardInativas);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnAtiva = new Button();
        btnAtiva.setText("Ativa");
        btnAtiva.setBackground(new Color(76,175,80));
        btnAtiva.setForeground(Color.WHITE);
        btnAtiva.addActionListener(e -> {filtroStatus = 1; aplicarFiltros();});
        filtro.add(btnAtiva);

        btnInativa = new Button();
        btnInativa.setText("Inativa");
        btnInativa.setBackground(new Color(255,152,0));
        btnInativa.setForeground(Color.BLACK);
        btnInativa.addActionListener(e -> {filtroStatus = 0; aplicarFiltros();});
        filtro.add(btnInativa);

        txtBusca = new JTextField(15);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });
        filtro.add(new JLabel("Pesquisar:"));
        filtro.add(txtBusca);

        btnNovo = new Button();
        btnNovo.setBackground(new Color(33,150,243));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD,20,Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30,30));
        btnNovo.addActionListener(e -> adicionarCategoria());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Nome", "Descrição", "Status", "Ações"
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
            if ((currentPage + 1) * PAGE_SIZE < (filtradas != null ? filtradas.size() : 0)) {
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

    private void carregarCategorias() {
        categorias = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (categorias == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtradas = categorias.stream()
                .filter(c -> filtroStatus == null || (c.getStatus() != null && filtroStatus.equals(c.getStatus())))
                .filter(c -> filtro == null || (c.getNome() != null && c.getNome().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtradas);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Categoria> lista) {
        int total = lista.size();
        int ativas = 0;
        int inativas = 0;
        for (Categoria c : lista) {
            if (c.getStatus() != null && c.getStatus() == 1) {
                ativas++;
            } else {
                inativas++;
            }
        }
        cardTotal.setData(new ModelCard("Categorias", total, 0, iconTotal));
        cardAtivas.setData(new ModelCard("Ativas", ativas, 0, iconAtiva));
        cardInativas.setData(new ModelCard("Inativas", inativas, 0, iconInativa));
    }

    private void atualizarTabela(List<Categoria> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Categoria> eventAction = new EventAction<Categoria>() {
            @Override
            public void delete(Categoria c) {
                excluirCategoria(c);
            }
            @Override
            public void update(Categoria c) {
                editarCategoria(c);
            }
        };
        for (Categoria c : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(c.getFoto());
            if (icon == null) {
                icon = new ImageIcon(getClass().getResource("/icon/profile.jpg"));
            }
            model.addRow(new Object[]{
                icon,
                c.getNome(),
                c.getDescricao(),
                statusTexto(c.getStatus()),
                new ModelAction<>(c, eventAction)
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
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                if ("Ativa".equals(value)) {
                    lbl.setBackground(new Color(76,175,80));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(new Color(255,152,0));
                    lbl.setForeground(Color.BLACK);
                }
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Categoria> page = getCurrentPageCategorias();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Categoria> getCurrentPageCategorias() {
        if (filtradas == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtradas.size());
        return filtradas.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtradas != null ? filtradas.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarCategoria() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        CategoriaDialog dialog = new CategoriaDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getCategoria());
                carregarCategorias();
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
                    carregarCategorias();
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
                controller.remover(c.getIdCategoria());
                carregarCategorias();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String statusTexto(Integer status) {
        return status != null && status == 1 ? "Ativa" : "Inativa";
    }
}
