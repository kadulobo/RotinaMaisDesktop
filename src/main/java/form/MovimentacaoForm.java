package form;

import controller.MovimentacaoController;
import controller.PeriodoController;
import dao.impl.MovimentacaoDaoNativeImpl;
import dao.impl.PeriodoDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Movimentacao;
import model.ModelCard;
import model.Periodo;
import swing.Button;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.Table;
import component.Card;

/**
 * Tela de listagem de movimentações financeiras com totalizadores e filtro por ano.
 */
public class MovimentacaoForm extends JPanel {

    private final MovimentacaoController movController;
    private final PeriodoController periodoController;

    private Card cardDesconto;
    private Card cardVantagem;
    private Card cardLiquido;
    private Card cardPontos;
    private Table table;
    private JComboBox<Integer> cbAno;
    private JComboBox<String> cbMes;
    private Button btnConcluido;
    private Button btnPendente;
    private Button btnTodas;
    private Button btnNova;
    private Integer filtroStatus;
    private List<Movimentacao> movsAno;
    private List<Movimentacao> movsFiltrados;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 10;

    private Icon iconDesc;
    private Icon iconVant;
    private Icon iconLiq;
    private Icon iconPonto;

    public MovimentacaoForm() {
        movController = new MovimentacaoController(new MovimentacaoDaoNativeImpl());
        periodoController = new PeriodoController(new PeriodoDaoNativeImpl());
        initComponents();
        carregarAnos();
        carregarMovimentacoes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel superior com cards e filtros
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        // Painel de cards
        JPanel cards = new JPanel(new java.awt.GridLayout(1, 4, 18, 0));
        cards.setBackground(Color.WHITE);

        iconDesc = IconFontSwing.buildIcon(
                GoogleMaterialDesignIcons.MONEY_OFF, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardDesconto = new Card();
        cardDesconto.setBackground(new Color(33, 150, 243));
        cardDesconto.setColorGradient(new Color(33, 203, 243));
        cardDesconto.setData(new ModelCard("Total Desconto", 0, 0, iconDesc));
        cards.add(cardDesconto);

        iconVant = IconFontSwing.buildIcon(
                GoogleMaterialDesignIcons.TRENDING_UP, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardVantagem = new Card();
        cardVantagem.setBackground(new Color(76, 175, 80));
        cardVantagem.setColorGradient(new Color(129, 199, 132));
        cardVantagem.setData(new ModelCard("Total Vantagem", 0, 0, iconVant));
        cards.add(cardVantagem);

        iconLiq = IconFontSwing.buildIcon(
                GoogleMaterialDesignIcons.ACCOUNT_BALANCE, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardLiquido = new Card();
        cardLiquido.setBackground(new Color(255, 152, 0));
        cardLiquido.setColorGradient(new Color(255, 203, 107));
        cardLiquido.setData(new ModelCard("Total Líquido", 0, 0, iconLiq));
        cards.add(cardLiquido);

        iconPonto = IconFontSwing.buildIcon(
                GoogleMaterialDesignIcons.PIN_DROP, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardPontos = new Card();
        cardPontos.setBackground(new Color(156, 39, 176));
        cardPontos.setColorGradient(new Color(206, 147, 216));
        cardPontos.setData(new ModelCard("Total Pontos", 0, 0, iconPonto));
        cards.add(cardPontos);

        top.add(cards, BorderLayout.NORTH);

        // Painel de filtros e novo registro
        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);
        filtro.add(new JLabel("Ano:"));
        cbAno = new JComboBox<>();
        cbAno.addActionListener(e -> carregarMovimentacoes());
        filtro.add(cbAno);

        filtro.add(new JLabel("Mês:"));
        cbMes = new JComboBox<>();
        cbMes.addActionListener(e -> aplicarFiltros());
        filtro.add(cbMes);

        btnTodas = new Button();
        btnTodas.setText("Todas");
        btnTodas.setBackground(new Color(33,150,243));
        btnTodas.setForeground(Color.WHITE);
        btnTodas.addActionListener(e -> {filtroStatus = null; aplicarFiltros();});
        filtro.add(btnTodas);

        btnConcluido = new Button();
        btnConcluido.setText("Concluído");
        btnConcluido.setBackground(new Color(76,175,80));
        btnConcluido.setForeground(Color.WHITE);
        btnConcluido.addActionListener(e -> {filtroStatus = 1; aplicarFiltros();});
        filtro.add(btnConcluido);

        btnPendente = new Button();
        btnPendente.setText("Pendente");
        btnPendente.setBackground(new Color(255,193,7));
        btnPendente.setForeground(Color.BLACK);
        btnPendente.addActionListener(e -> {filtroStatus = 2; aplicarFiltros();});
        filtro.add(btnPendente);

        btnNova = new Button();
        btnNova.setBackground(new Color(33,150,243));
        btnNova.setForeground(Color.WHITE);
        btnNova.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 20, Color.WHITE));
        btnNova.setPreferredSize(new Dimension(30,30));
        btnNova.addActionListener(e -> new MovimentacaoDialog(null).setVisible(true));
        filtro.add(btnNova);

        filtroWrapper.add(filtro, BorderLayout.CENTER);

        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // Tabela de movimentações
        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Tipo", "Ponto", "Desconto", "Vantagem", "Líquido", "Status"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        table.fixTable(scroll);
        table.setRowSelectionAllowed(false);
        add(scroll, BorderLayout.CENTER);

        // Rodapé com paginação
        btnPrevPage = new JButton("Anterior");
        btnNextPage = new JButton("Próximo");
        btnPrevPage.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                atualizarPagina();
            }
        });
        btnNextPage.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < (movsFiltrados != null ? movsFiltrados.size() : 0)) {
                currentPage++;
                atualizarPagina();
            }
        });
        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pagination.setBackground(Color.WHITE);
        pagination.add(btnPrevPage);
        pagination.add(btnNextPage);
        add(pagination, BorderLayout.SOUTH);
    }

    private void carregarAnos() {
        List<Periodo> periodos = periodoController.listar();
        Set<Integer> anos = periodos.stream()
                .map(Periodo::getAno)
                .collect(Collectors.toCollection(HashSet::new));
        List<Integer> ordenados = new ArrayList<>(anos);
        ordenados.sort(Integer::compareTo);
        DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
        for (Integer a : ordenados) {
            model.addElement(a);
        }
        cbAno.setModel(model);
        if (model.getSize() > 0) {
            cbAno.setSelectedItem(Year.now().getValue());
        }
        carregarMeses();
    }

    private void carregarMovimentacoes() {
        Integer ano = (Integer) cbAno.getSelectedItem();
        if (ano == null) {
            return;
        }
        List<Periodo> periodosAno = periodoController.buscarPorAno(ano);
        movsAno = new ArrayList<>();
        for (Periodo p : periodosAno) {
            movsAno.addAll(movController.buscarPorIdPeriodo(p.getIdPeriodo()));
        }
        carregarMeses(periodosAno);
        aplicarFiltros();
    }

    private void carregarMeses() {
        Integer ano = (Integer) cbAno.getSelectedItem();
        if (ano == null) {
            cbMes.setModel(new DefaultComboBoxModel<>());
            return;
        }
        List<Periodo> periodosAno = periodoController.buscarPorAno(ano);
        carregarMeses(periodosAno);
    }

    private void carregarMeses(List<Periodo> periodosAno) {
        Set<Integer> meses = periodosAno.stream()
                .map(Periodo::getMes)
                .collect(Collectors.toCollection(java.util.TreeSet::new));
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Todos");
        for (Integer m : meses) {
            model.addElement(m.toString());
        }
        cbMes.setModel(model);
        cbMes.setSelectedIndex(0);
    }

    private void aplicarFiltros() {
        if (movsAno == null) {
            return;
        }
        String mesSel = (String) cbMes.getSelectedItem();
        final Integer mes = (mesSel != null && !"Todos".equals(mesSel))
                ? Integer.valueOf(mesSel)
                : null;
        movsFiltrados = movsAno.stream()
                .filter(m -> mes == null
                        || (m.getPeriodo() != null && mes.equals(m.getPeriodo().getMes())))
                .filter(m -> filtroStatus == null
                        || (m.getStatus() != null && filtroStatus.equals(m.getStatus())))
                .collect(Collectors.toList());
        currentPage = 0;
        atualizarPagina();
        atualizarCards(movsFiltrados);
    }

    private void atualizarPagina() {
        if (movsFiltrados == null) {
            return;
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, movsFiltrados.size());
        List<Movimentacao> page = movsFiltrados.subList(start, end);
        atualizarTabela(page);
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled(end < movsFiltrados.size());
    }

    private void atualizarCards(List<Movimentacao> movs) {
        BigDecimal totalDesc = BigDecimal.ZERO;
        BigDecimal totalVant = BigDecimal.ZERO;
        BigDecimal totalLiq = BigDecimal.ZERO;
        int totalPontos = 0;

        for (Movimentacao m : movs) {
            if (m.getDesconto() != null) {
                totalDesc = totalDesc.add(m.getDesconto());
            }
            if (m.getVantagem() != null) {
                totalVant = totalVant.add(m.getVantagem());
            }
            if (m.getLiquido() != null) {
                totalLiq = totalLiq.add(m.getLiquido());
            }
            if (m.getPonto() != null) {
                totalPontos += m.getPonto();
            }
        }

        cardDesconto.setData(new ModelCard("Total Desconto", totalDesc.doubleValue(), 0, iconDesc));
        cardVantagem.setData(new ModelCard("Total Vantagem", totalVant.doubleValue(), 0, iconVant));
        cardLiquido.setData(new ModelCard("Total Líquido", totalLiq.doubleValue(), 0, iconLiq));
        cardPontos.setData(new ModelCard("Total Pontos", totalPontos, 0, iconPonto));
    }

    private void atualizarTabela(List<Movimentacao> movs) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Movimentacao m : movs) {
            String tipo = tipoTexto(m.getTipo());
            String status = statusTexto(m.getStatus());
            model.addRow(new Object[]{
                tipo,
                m.getPonto(),
                m.getDesconto(),
                m.getVantagem(),
                m.getLiquido(),
                status
            });
        }
        int statusCol = table.getColumnModel().getColumnCount() - 1;
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5, 5, 5, 5));
                if ("Concluído".equals(value)) {
                    lbl.setBackground(new Color(76,175,80));
                    lbl.setForeground(Color.WHITE);
                } else if ("Pendente".equals(value)) {
                    lbl.setBackground(new Color(255,193,7));
                    lbl.setForeground(Color.BLACK);
                } else {
                    lbl.setBackground(Color.WHITE);
                    lbl.setForeground(Color.BLACK);
                }
                return lbl;
            }
        });
    }

    private String tipoTexto(Integer tipo) {
        if (tipo == null) {
            return "";
        }
        switch (tipo) {
            case 1:
                return "Entrada";
            case 2:
                return "Saída";
            default:
                return tipo.toString();
        }
    }

    private String statusTexto(Integer status) {
        if (status == null) {
            return "";
        }
        if (status == 1) {
            return "Concluído";
        } else if (status == 2) {
            return "Pendente";
        } else {
            return status.toString();
        }
    }
}
