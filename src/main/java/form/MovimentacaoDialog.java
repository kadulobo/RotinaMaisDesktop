package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.CaixaController;
import controller.PeriodoController;
import dao.impl.CaixaDaoNativeImpl;
import dao.impl.PeriodoDaoNativeImpl;
import model.Caixa;
import model.Movimentacao;
import model.Periodo;
import swing.Button;

/**
 * Dialog used for creating or editing a {@link Movimentacao}.
 */
public class MovimentacaoDialog extends JDialog {

    private Movimentacao movimentacao;
    private boolean confirmed = false;

    private JTextField txtId;
    private JComboBox<String> cbTipo;
    private JTextField txtPonto;
    private JTextField txtDesconto;
    private JTextField txtVantagem;
    private JTextField txtLiquido;
    private JComboBox<String> cbStatus;
    private JComboBox<String> cbPeriodo;
    private JComboBox<String> cbCaixa;
    private List<Periodo> periodos;
    private List<Caixa> caixas;
    private final PeriodoController periodoController = new PeriodoController(new PeriodoDaoNativeImpl());
    private final CaixaController caixaController = new CaixaController(new CaixaDaoNativeImpl());

    public MovimentacaoDialog(Frame parent, Movimentacao mov, Periodo periodo) {
        super(parent, true);
        this.movimentacao = mov != null ? mov : new Movimentacao();
        if (periodo != null) {
            this.movimentacao.setPeriodo(periodo);
        }
        initComponents();
        loadLists();
        setLocationRelativeTo(parent);
        loadData();
    }

    private void initComponents() {
        setTitle("Movimentação");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        JLabel lblId = new JLabel("ID");
        txtId = new JTextField(5);
        txtId.setEditable(false);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblId, gbc);
        gbc.gridx = 1; panel.add(txtId, gbc); y++;

        JLabel lblTipo = new JLabel("Tipo");
        cbTipo = new JComboBox<>(new String[]{"Entrada", "Saída"});
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblTipo, gbc);
        gbc.gridx = 1; panel.add(cbTipo, gbc); y++;

        JLabel lblPeriodo = new JLabel("Período");
        cbPeriodo = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblPeriodo, gbc);
        gbc.gridx = 1; panel.add(cbPeriodo, gbc); y++;

        JLabel lblCaixa = new JLabel("Caixa");
        cbCaixa = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblCaixa, gbc);
        gbc.gridx = 1; panel.add(cbCaixa, gbc); y++;

        JLabel lblPonto = new JLabel("Ponto");
        txtPonto = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblPonto, gbc);
        gbc.gridx = 1; panel.add(txtPonto, gbc); y++;

        JLabel lblDesconto = new JLabel("Desconto");
        txtDesconto = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblDesconto, gbc);
        gbc.gridx = 1; panel.add(txtDesconto, gbc); y++;

        JLabel lblVantagem = new JLabel("Vantagem");
        txtVantagem = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblVantagem, gbc);
        gbc.gridx = 1; panel.add(txtVantagem, gbc); y++;

        JLabel lblLiquido = new JLabel("Líquido");
        txtLiquido = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblLiquido, gbc);
        gbc.gridx = 1; panel.add(txtLiquido, gbc); y++;

        JLabel lblStatus = new JLabel("Status");
        cbStatus = new JComboBox<>(new String[]{"Concluído", "Pendente"});
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblStatus, gbc);
        gbc.gridx = 1; panel.add(cbStatus, gbc); y++;

        Button btnSalvar = new Button();
        btnSalvar.setText("Salvar");
        btnSalvar.setBackground(new Color(75, 134, 253));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvar());

        Button btnCancelar = new Button();
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        gbc.gridx = 0; gbc.gridy = y; panel.add(btnSalvar, gbc);
        gbc.gridx = 1; panel.add(btnCancelar, gbc);

        getContentPane().add(panel);
        pack();
        getRootPane().setDefaultButton(btnSalvar);
    }

    private void loadData() {
        if (movimentacao.getIdMovimentacao() != null) {
            txtId.setText(String.valueOf(movimentacao.getIdMovimentacao()));
        }
        if (movimentacao.getTipo() != null) {
            cbTipo.setSelectedIndex(movimentacao.getTipo() == 1 ? 0 : 1);
        }
        if (movimentacao.getPonto() != null) {
            txtPonto.setText(movimentacao.getPonto().toString());
        }
        if (movimentacao.getDesconto() != null) {
            txtDesconto.setText(movimentacao.getDesconto().toString());
        }
        if (movimentacao.getVantagem() != null) {
            txtVantagem.setText(movimentacao.getVantagem().toString());
        }
        if (movimentacao.getLiquido() != null) {
            txtLiquido.setText(movimentacao.getLiquido().toString());
        }
        if (movimentacao.getStatus() != null) {
            cbStatus.setSelectedIndex(movimentacao.getStatus() == 1 ? 0 : 1);
        }
        if (movimentacao.getPeriodo() != null && periodos != null) {
            for (int i = 0; i < periodos.size(); i++) {
                if (periodos.get(i).getIdPeriodo().equals(movimentacao.getPeriodo().getIdPeriodo())) {
                    cbPeriodo.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (movimentacao.getCaixa() != null && caixas != null) {
            for (int i = 0; i < caixas.size(); i++) {
                if (caixas.get(i).getIdCaixa().equals(movimentacao.getCaixa().getIdCaixa())) {
                    cbCaixa.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void loadLists() {
        periodos = periodoController.listar();
        cbPeriodo.removeAllItems();
        for (Periodo p : periodos) {
            cbPeriodo.addItem(p.getAno() + "/" + String.format("%02d", p.getMes()));
        }
        caixas = caixaController.listar();
        cbCaixa.removeAllItems();
        for (Caixa c : caixas) {
            cbCaixa.addItem(c.getNome());
        }
    }

    private void salvar() {
        try {
            movimentacao.setTipo(cbTipo.getSelectedIndex() == 0 ? 1 : 2);
            movimentacao.setPonto(parseInteger(txtPonto.getText()));
            movimentacao.setDesconto(parseBigDecimal(txtDesconto.getText()));
            movimentacao.setVantagem(parseBigDecimal(txtVantagem.getText()));
            movimentacao.setLiquido(parseBigDecimal(txtLiquido.getText()));
            movimentacao.setStatus(cbStatus.getSelectedIndex() == 0 ? 1 : 2);
            if (cbPeriodo.getSelectedIndex() >= 0) {
                movimentacao.setPeriodo(periodos.get(cbPeriodo.getSelectedIndex()));
            }
            if (cbCaixa.getSelectedIndex() >= 0) {
                movimentacao.setCaixa(caixas.get(cbCaixa.getSelectedIndex()));
            }
            if (movimentacao.getPeriodo() == null || movimentacao.getCaixa() == null) {
                JOptionPane.showMessageDialog(this, "Período e Caixa são obrigatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores inválidos", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer parseInteger(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return Integer.valueOf(text);
    }

    private BigDecimal parseBigDecimal(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return new BigDecimal(text);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }
}

