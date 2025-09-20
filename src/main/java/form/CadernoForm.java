package form;

import component.Card;
import controller.CadernoController;
import dao.impl.CadernoDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Caderno;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

public class CadernoForm extends JPanel {

    private final CadernoController controller;
    private Card cardNotas;
    private Card cardIas;
    private Card cardCategorias;
    private Table table;
    private JTextField txtBusca;
    private Button btnIa1;
    private Button btnIa2;
    private Button btnIa3;
    private Button btnNovo;
    private String filtroIa;
    private List<Caderno> cadernos;
    private List<Caderno> filtrados;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final String[] iaBotoes = new String[3];
    private javax.swing.Icon iconNotas;
    private javax.swing.Icon iconIas;
    private javax.swing.Icon iconCategorias;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CadernoForm() {
        controller = new CadernoController(new CadernoDaoNativeImpl());
        initComponents();
        carregarCadernos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconNotas = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BOOK, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardNotas = new Card();
        cardNotas.setBackground(new Color(33,150,243));
        cardNotas.setColorGradient(new Color(30,136,229));
        cardNotas.setData(new ModelCard("Anotações",0,0,iconNotas));
        cards.add(cardNotas);

        iconIas = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ASSIGNMENT, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardIas = new Card();
        cardIas.setBackground(new Color(121,134,203));
        cardIas.setColorGradient(new Color(92,107,192));
        cardIas.setData(new ModelCard("Assistentes",0,0,iconIas));
        cards.add(cardIas);

        iconCategorias = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CATEGORY, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardCategorias = new Card();
        cardCategorias.setBackground(new Color(0,150,136));
        cardCategorias.setColorGradient(new Color(38,166,154));
        cardCategorias.setData(new ModelCard("Categorias",0,0,iconCategorias));
        cards.add(cardCategorias);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnIa1 = criarBotaoFiltro(0);
        filtro.add(btnIa1);

        btnIa2 = criarBotaoFiltro(1);
        filtro.add(btnIa2);

        btnIa3 = criarBotaoFiltro(2);
        filtro.add(btnIa3);

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
        btnNovo.addActionListener(e -> adicionarCaderno());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Imagem", "Título", "Assistente", "Data", "Resultado", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        });
        table.setRowHeight(60);
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

    private Button criarBotaoFiltro(int index) {
        Button button = new Button();
        button.setPreferredSize(new Dimension(120,30));
        button.setBackground(new Color(121,134,203));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> aplicarFiltroIa(index));
        button.setEnabled(false);
        button.setText("Assistente " + (index + 1));
        return button;
    }

    private void aplicarFiltroIa(int index) {
        filtroIa = iaBotoes[index];
        aplicarFiltros();
    }

    private void carregarCadernos() {
        cadernos = controller.listar();
        atualizarIaBotoes();
        filtroIa = null;
        aplicarFiltros();
    }

    private void atualizarIaBotoes() {
        for (int i = 0; i < iaBotoes.length; i++) {
            iaBotoes[i] = null;
        }
        if (cadernos != null) {
            Set<String> ias = cadernos.stream()
                    .map(Caderno::getNomeIa)
                    .filter(s -> s != null && !s.isEmpty())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            int idx = 0;
            for (String ia : ias) {
                if (idx >= iaBotoes.length) break;
                iaBotoes[idx++] = ia;
            }
        }
        atualizarTextoBotoes();
    }

    private void atualizarTextoBotoes() {
        Button[] botoes = {btnIa1, btnIa2, btnIa3};
        for (int i = 0; i < botoes.length; i++) {
            Button b = botoes[i];
            String texto = iaBotoes[i];
            if (texto != null) {
                b.setText(texto);
                b.setEnabled(true);
            } else {
                b.setText("Todos");
                b.setEnabled(true);
            }
        }
    }

    private void aplicarFiltros() {
        if (cadernos == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = cadernos.stream()
                .filter(c -> filtroIa == null || filtroIa.equals(c.getNomeIa()) || "Todos".equals(filtroIa))
                .filter(c -> filtro == null ||
                        (c.getTitulo() != null && c.getTitulo().toLowerCase().contains(filtro)) ||
                        (c.getNomeIa() != null && c.getNomeIa().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Caderno> lista) {
        int total = lista != null ? lista.size() : 0;
        Set<String> ias = new LinkedHashSet<>();
        Set<Integer> categorias = new LinkedHashSet<>();
        for (Caderno c : lista) {
            if (c.getNomeIa() != null && !c.getNomeIa().isEmpty()) ias.add(c.getNomeIa());
            if (c.getIdCategoria() != null) categorias.add(c.getIdCategoria());
        }
        cardNotas.setData(new ModelCard("Anotações", total, 0, iconNotas));
        cardIas.setData(new ModelCard("Assistentes", ias.size(), 0, iconIas));
        cardCategorias.setData(new ModelCard("Categorias", categorias.size(), 0, iconCategorias));
    }

    private void atualizarTabela(List<Caderno> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Caderno> eventAction = new EventAction<Caderno>() {
            @Override
            public void delete(Caderno c) { excluirCaderno(c); }
            @Override
            public void update(Caderno c) { editarCaderno(c); }
        };
        for (Caderno c : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(c.getResultadoImagem());
            model.addRow(new Object[]{
                icon,
                c.getTitulo(),
                c.getNomeIa(),
                c.getData() != null ? formatter.format(c.getData()) : "",
                resumo(c.getResultado()),
                new ModelAction<>(c, eventAction)
            });
        }
        table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                ImageAvatar avatar = new ImageAvatar();
                avatar.setPreferredSize(new Dimension(50, 50));
                if (value instanceof ImageIcon) {
                    avatar.setIcon((ImageIcon) value);
                }
                return avatar;
            }
        });
        table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea area = new JTextArea(value == null ? "" : value.toString());
                area.setWrapStyleWord(true);
                area.setLineWrap(true);
                area.setOpaque(false);
                area.setBorder(new EmptyBorder(5,5,5,5));
                return area;
            }
        });
    }

    private String resumo(String texto) {
        if (texto == null) return "";
        if (texto.length() <= 60) return texto;
        return texto.substring(0, 57) + "...";
    }

    private void updatePage() {
        List<Caderno> page = getCurrentPageCadernos();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Caderno> getCurrentPageCadernos() {
        if (filtrados == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtrados.size());
        if (start > end) {
            return new ArrayList<>();
        }
        return filtrados.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtrados != null ? filtrados.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarCaderno() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        CadernoDialog dialog = new CadernoDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getCaderno());
                carregarCadernos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCaderno(Caderno caderno) {
        try {
            Caderno completo = controller.buscarPorId(caderno.getIdCaderno());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            CadernoDialog dialog = new CadernoDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                controller.atualizar(dialog.getCaderno());
                carregarCadernos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCaderno(Caderno caderno) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir caderno?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(caderno.getIdCaderno());
                carregarCadernos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
