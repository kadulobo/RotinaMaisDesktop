package form;

import component.Card;
import controller.ObjetoController;
import dao.impl.ObjetoDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
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
import model.ModelCard;
import model.Objeto;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

public class ObjetoForm extends JPanel {

    private final ObjetoController controller;
    private Card cardObjetos;
    private Card cardTipos;
    private Card cardValor;
    private Table table;
    private JTextField txtBusca;
    private Button btnTipo1;
    private Button btnTipo2;
    private Button btnTipo3;
    private Button btnNovo;
    private Integer filtroTipo;
    private List<Objeto> objetos;
    private List<Objeto> filtrados;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final Integer[] tiposBotoes = new Integer[3];
    private javax.swing.Icon iconObjetos;
    private javax.swing.Icon iconTipos;
    private javax.swing.Icon iconValor;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public ObjetoForm() {
        controller = new ObjetoController(new ObjetoDaoNativeImpl());
        initComponents();
        carregarObjetos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconObjetos = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CARD_GIFTCARD, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardObjetos = new Card();
        cardObjetos.setBackground(new Color(33,150,243));
        cardObjetos.setColorGradient(new Color(30,136,229));
        cardObjetos.setData(new ModelCard("Objetos",0,0,iconObjetos));
        cards.add(cardObjetos);

        iconTipos = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CATEGORY, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardTipos = new Card();
        cardTipos.setBackground(new Color(156,39,176));
        cardTipos.setColorGradient(new Color(186,104,200));
        cardTipos.setData(new ModelCard("Tipos",0,0,iconTipos));
        cards.add(cardTipos);

        iconValor = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ATTACH_MONEY, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardValor = new Card();
        cardValor.setBackground(new Color(76,175,80));
        cardValor.setColorGradient(new Color(129,199,132));
        cardValor.setData(new ModelCard("Valor Total",0,0,iconValor));
        cards.add(cardValor);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnTipo1 = criarBotaoFiltro(0);
        filtro.add(btnTipo1);

        btnTipo2 = criarBotaoFiltro(1);
        filtro.add(btnTipo2);

        btnTipo3 = criarBotaoFiltro(2);
        filtro.add(btnTipo3);

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
        btnNovo.addActionListener(e -> adicionarObjeto());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Nome", "Tipo", "Valor", "Descrição", "Ações"
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
        button.setPreferredSize(new Dimension(110,30));
        button.setBackground(new Color(156,39,176));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> aplicarFiltroTipo(index));
        button.setEnabled(false);
        button.setText("Tipo " + (index + 1));
        return button;
    }

    private void aplicarFiltroTipo(int index) {
        filtroTipo = tiposBotoes[index];
        aplicarFiltros();
    }

    private void carregarObjetos() {
        objetos = controller.listar();
        atualizarTiposBotoes();
        filtroTipo = null;
        aplicarFiltros();
    }

    private void atualizarTiposBotoes() {
        for (int i = 0; i < tiposBotoes.length; i++) {
            tiposBotoes[i] = null;
        }
        if (objetos != null) {
            Set<Integer> tipos = objetos.stream()
                    .map(Objeto::getTipo)
                    .filter(t -> t != null)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            int idx = 0;
            for (Integer t : tipos) {
                if (idx >= tiposBotoes.length) break;
                tiposBotoes[idx++] = t;
            }
        }
        atualizarTextoBotoes();
    }

    private void atualizarTextoBotoes() {
        Button[] botoes = {btnTipo1, btnTipo2, btnTipo3};
        for (int i = 0; i < botoes.length; i++) {
            Button b = botoes[i];
            Integer valor = tiposBotoes[i];
            if (valor != null) {
                b.setText("Tipo " + valor);
                b.setEnabled(true);
            } else {
                b.setText("Todos");
                b.setEnabled(true);
            }
        }
    }

    private void aplicarFiltros() {
        if (objetos == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = objetos.stream()
                .filter(o -> filtroTipo == null || filtroTipo.equals(o.getTipo()))
                .filter(o -> filtro == null ||
                        (o.getNome() != null && o.getNome().toLowerCase().contains(filtro)) ||
                        (o.getDescricao() != null && o.getDescricao().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Objeto> lista) {
        int total = lista != null ? lista.size() : 0;
        Set<Integer> tipos = new LinkedHashSet<>();
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (Objeto o : lista) {
            if (o.getTipo() != null) tipos.add(o.getTipo());
            if (o.getValor() != null) valorTotal = valorTotal.add(o.getValor());
        }
        cardObjetos.setData(new ModelCard("Objetos", total, 0, iconObjetos));
        cardTipos.setData(new ModelCard("Tipos", tipos.size(), 0, iconTipos));
        cardValor.setData(new ModelCard("Valor Total", valorTotal.doubleValue(), 0, iconValor));
    }

    private void atualizarTabela(List<Objeto> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Objeto> eventAction = new EventAction<Objeto>() {
            @Override
            public void delete(Objeto objeto) { excluirObjeto(objeto); }
            @Override
            public void update(Objeto objeto) { editarObjeto(objeto); }
        };
        for (Objeto o : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(o.getFoto());
            model.addRow(new Object[]{
                icon,
                o.getNome(),
                o.getTipo(),
                o.getValor() != null ? currency.format(o.getValor()) : "",
                resumo(o.getDescricao()),
                new ModelAction<>(o, eventAction)
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
        List<Objeto> page = getCurrentPageObjetos();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Objeto> getCurrentPageObjetos() {
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

    private void adicionarObjeto() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        ObjetoDialog dialog = new ObjetoDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getObjeto());
                carregarObjetos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarObjeto(Objeto objeto) {
        try {
            Objeto completo = controller.buscarPorId(objeto.getIdObjeto());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            ObjetoDialog dialog = new ObjetoDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                controller.atualizar(dialog.getObjeto());
                carregarObjetos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirObjeto(Objeto objeto) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir objeto?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(objeto.getIdObjeto());
                carregarObjetos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
