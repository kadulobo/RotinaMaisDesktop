package form;

import controller.CaixaController;
import controller.MovimentacaoController;
import controller.PeriodoController;
import dao.impl.CaixaDaoNativeImpl;
import dao.impl.MovimentacaoDaoNativeImpl;
import dao.impl.PeriodoDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Caixa;
import model.Movimentacao;
import model.ModelCard;
import model.Periodo;
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
    private final CaixaController caixaController;

    private Card cardDesconto;
    private Card cardVantagem;
    private Card cardLiquido;
    private Card cardCaixa;
    private Table table;
    private JComboBox<Integer> cbAno;

    private Icon iconDesc;
    private Icon iconVant;
    private Icon iconLiq;
    private Icon iconCaixa;

    public MovimentacaoForm() {
        movController = new MovimentacaoController(new MovimentacaoDaoNativeImpl());
        periodoController = new PeriodoController(new PeriodoDaoNativeImpl());
        caixaController = new CaixaController(new CaixaDaoNativeImpl());
        initComponents();
        carregarAnos();
        carregarMovimentacoes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel de cards
        JPanel cards = new JPanel(new java.awt.GridLayout(1, 4, 18, 0));
        cards.setBackground(Color.WHITE);

        iconDesc = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.MONEY_OFF, 60, Color.WHITE, new Color(255, 255, 255, 15));
        cardDesconto = new Card();
        cardDesconto.setBackground(new Color(33, 150, 243));
        cardDesconto.setColorGradient(new Color(33, 203, 243));
        cardDesconto.setData(new ModelCard("Total Desconto", 0, 0, iconDesc));
        cards.add(cardDesconto);

        iconVant = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.TRENDING_UP, 60, Color.WHITE, new Color(255, 255, 255, 15));
        cardVantagem = new Card();
        cardVantagem.setBackground(new Color(76, 175, 80));
        cardVantagem.setColorGradient(new Color(129, 199, 132));
        cardVantagem.setData(new ModelCard("Total Vantagem", 0, 0, iconVant));
        cards.add(cardVantagem);

        iconLiq = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_BALANCE, 60, Color.WHITE, new Color(255, 255, 255, 15));
        cardLiquido = new Card();
        cardLiquido.setBackground(new Color(255, 152, 0));
        cardLiquido.setColorGradient(new Color(255, 203, 107));
        cardLiquido.setData(new ModelCard("Total Líquido", 0, 0, iconLiq));
        cards.add(cardLiquido);

        iconCaixa = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_BALANCE_WALLET, 60, Color.WHITE, new Color(255, 255, 255, 15));
        cardCaixa = new Card();
        cardCaixa.setBackground(new Color(63, 81, 181));
        cardCaixa.setColorGradient(new Color(121, 134, 203));
        cardCaixa.setData(new ModelCard("Saldo Caixa", 0, 0, iconCaixa));
        cards.add(cardCaixa);

        add(cards, BorderLayout.NORTH);

        // Filtro de ano
        JPanel filtro = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);
        filtro.add(new JLabel("Ano:"));
        cbAno = new JComboBox<>();
        cbAno.addActionListener(e -> carregarMovimentacoes());
        filtro.add(cbAno);
        add(filtro, BorderLayout.CENTER);

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
        add(scroll, BorderLayout.SOUTH);
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
    }

    private void carregarMovimentacoes() {
        Integer ano = (Integer) cbAno.getSelectedItem();
        if (ano == null) {
            return;
        }
        List<Periodo> periodosAno = periodoController.buscarPorAno(ano);
        List<Movimentacao> movs = new ArrayList<>();
        for (Periodo p : periodosAno) {
            movs.addAll(movController.buscarPorIdPeriodo(p.getIdPeriodo()));
        }
        atualizarTabela(movs);
        atualizarCards(movs);
    }

    private void atualizarCards(List<Movimentacao> movs) {
        BigDecimal totalDesc = BigDecimal.ZERO;
        BigDecimal totalVant = BigDecimal.ZERO;
        BigDecimal totalLiq = BigDecimal.ZERO;
        BigDecimal totalCx = BigDecimal.ZERO;
        Set<Integer> caixasProcessadas = new HashSet<>();

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
            if (m.getCaixa() != null && m.getCaixa().getIdCaixa() != null) {
                int id = m.getCaixa().getIdCaixa();
                if (caixasProcessadas.add(id)) {
                    Caixa c = caixaController.buscarPorId(id);
                    if (c != null && c.getValorTotal() != null) {
                        totalCx = totalCx.add(c.getValorTotal());
                    }
                }
            }
        }

        cardDesconto.setData(new ModelCard("Total Desconto", totalDesc.doubleValue(), 0, iconDesc));
        cardVantagem.setData(new ModelCard("Total Vantagem", totalVant.doubleValue(), 0, iconVant));
        cardLiquido.setData(new ModelCard("Total Líquido", totalLiq.doubleValue(), 0, iconLiq));
        cardCaixa.setData(new ModelCard("Saldo Caixa", totalCx.doubleValue(), 0, iconCaixa));
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
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ("Concluído".equals(value)) {
                    lbl.setForeground(new Color(0, 128, 0));
                } else if ("Pendente".equals(value)) {
                    lbl.setForeground(new Color(255, 165, 0));
                } else {
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
