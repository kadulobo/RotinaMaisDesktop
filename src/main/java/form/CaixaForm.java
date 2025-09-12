package form;

import controller.CaixaController;
import dao.impl.CaixaDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import javax.swing.border.EmptyBorder;

import model.Caixa;
import model.ModelCard;
import swing.Button;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import component.Card;

/**
 * Formulário para gerenciamento de Caixas.
 */
public class CaixaForm extends JPanel {

    private final CaixaController controller;
    private Card cardValor;
    private Card cardSalario;
    private Card cardReserva;
    private Table table;
    private JTextField txtBusca;
    private Button btnNovo;
    private List<Caixa> caixas;
    private List<Caixa> filtradas;
    private javax.swing.Icon iconValor;
    private javax.swing.Icon iconSalario;
    private javax.swing.Icon iconReserva;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public CaixaForm() {
        controller = new CaixaController(new CaixaDaoNativeImpl());
        initComponents();
        carregarCaixas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconValor = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_BALANCE,60,Color.WHITE,new Color(255,255,255,15));
        cardValor = new Card();
        cardValor.setBackground(new Color(33,150,243));
        cardValor.setColorGradient(new Color(33,203,243));
        cardValor.setData(new ModelCard("Valor Caixa",0,0,iconValor));
        cards.add(cardValor);

        iconSalario = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ATTACH_MONEY,60,Color.WHITE,new Color(255,255,255,15));
        cardSalario = new Card();
        cardSalario.setBackground(new Color(76,175,80));
        cardSalario.setColorGradient(new Color(129,199,132));
        cardSalario.setData(new ModelCard("Salario",0,0,iconSalario));
        cards.add(cardSalario);

        iconReserva = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_BALANCE_WALLET,60,Color.WHITE,new Color(255,255,255,15));
        cardReserva = new Card();
        cardReserva.setBackground(new Color(255,152,0));
        cardReserva.setColorGradient(new Color(255,203,107));
        cardReserva.setData(new ModelCard("Reserva",0,0,iconReserva));
        cards.add(cardReserva);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

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
        btnNovo.addActionListener(e -> adicionarCaixa());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Nome", "Valor Caixa", "Salario", "Reserva", "Usuario", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
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

    private void carregarCaixas() {
        caixas = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (caixas == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtradas = caixas.stream()
                .filter(c -> filtro == null || (c.getNome() != null && c.getNome().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards();
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards() {
        BigDecimal totalValor = BigDecimal.ZERO;
        BigDecimal totalSalario = BigDecimal.ZERO;
        BigDecimal totalReserva = BigDecimal.ZERO;
        if (caixas != null) {
            for (Caixa c : caixas) {
                if (c.getValorTotal() != null) {
                    totalValor = totalValor.add(c.getValorTotal());
                }
                if (c.getSalarioMedio() != null) {
                    totalSalario = totalSalario.add(c.getSalarioMedio());
                }
                if (c.getReservaEmergencia() != null) {
                    totalReserva = totalReserva.add(c.getReservaEmergencia());
                }
            }
        }
        cardValor.setData(new ModelCard("Valor Caixa", totalValor.doubleValue(), 0, iconValor));
        cardSalario.setData(new ModelCard("Salario", totalSalario.doubleValue(), 0, iconSalario));
        cardReserva.setData(new ModelCard("Reserva", totalReserva.doubleValue(), 0, iconReserva));
    }

    private void atualizarTabela(List<Caixa> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Caixa> eventAction = new EventAction<Caixa>() {
            @Override
            public void delete(Caixa c) {
                excluirCaixa(c);
            }
            @Override
            public void update(Caixa c) {
                editarCaixa(c);
            }
        };
        for (Caixa c : lista) {
            model.addRow(new Object[]{
                c.getNome(),
                c.getValorTotal(),
                c.getSalarioMedio(),
                c.getReservaEmergencia(),
                c.getUsuario() != null ? c.getUsuario().getNome() : "",
                new ModelAction<>(c, eventAction)
            });
        }
        table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = new JLabel(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Caixa> page = getCurrentPageCaixas();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Caixa> getCurrentPageCaixas() {
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

    private void adicionarCaixa() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        CaixaDialog dialog = new CaixaDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getCaixa());
                carregarCaixas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCaixa(Caixa c) {
        try {
            Caixa completo = controller.buscarPorId(c.getIdCaixa());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            CaixaDialog dialog = new CaixaDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getCaixa());
                    carregarCaixas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCaixa(Caixa c) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir caixa?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(c.getIdCaixa());
                carregarCaixas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
