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

import controller.UsuarioController;
import dao.impl.UsuarioDaoNativeImpl;
import model.Caixa;
import model.Usuario;
import swing.Button;

/**
 * Dialog used for creating or editing a {@link Caixa}.
 */
public class CaixaDialog extends JDialog {

    private Caixa caixa;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtValor;
    private JTextField txtSalario;
    private JTextField txtReserva;
    private JComboBox<String> cbUsuario;
    private List<Usuario> usuarios;
    private final UsuarioController usuarioController = new UsuarioController(new UsuarioDaoNativeImpl());

    public CaixaDialog(Frame parent, Caixa caixa) {
        super(parent, true);
        this.caixa = caixa != null ? caixa : new Caixa();
        initComponents();
        loadUsuarios();
        setLocationRelativeTo(parent);
        loadData();
    }

    private void initComponents() {
        setTitle("Caixa");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        JLabel lblId = new JLabel("ID");
        txtId = new JTextField(5);
        txtId.setEditable(false);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblId, gbc);
        gbc.gridx = 1; panel.add(txtId, gbc); y++;

        JLabel lblNome = new JLabel("Nome");
        txtNome = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblNome, gbc);
        gbc.gridx = 1; panel.add(txtNome, gbc); y++;

        JLabel lblValor = new JLabel("Valor Caixa");
        txtValor = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblValor, gbc);
        gbc.gridx = 1; panel.add(txtValor, gbc); y++;

        JLabel lblSalario = new JLabel("Salario");
        txtSalario = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblSalario, gbc);
        gbc.gridx = 1; panel.add(txtSalario, gbc); y++;

        JLabel lblReserva = new JLabel("Reserva");
        txtReserva = new JTextField(10);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblReserva, gbc);
        gbc.gridx = 1; panel.add(txtReserva, gbc); y++;

        JLabel lblUsuario = new JLabel("Usuario");
        cbUsuario = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblUsuario, gbc);
        gbc.gridx = 1; panel.add(cbUsuario, gbc); y++;

        Button btnSalvar = new Button();
        btnSalvar.setText("Salvar");
        btnSalvar.setBackground(new Color(75,134,253));
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
        if (caixa.getIdCaixa() != null) {
            txtId.setText(String.valueOf(caixa.getIdCaixa()));
        }
        if (caixa.getNome() != null) {
            txtNome.setText(caixa.getNome());
        }
        if (caixa.getValorTotal() != null) {
            txtValor.setText(caixa.getValorTotal().toString());
        }
        if (caixa.getSalarioMedio() != null) {
            txtSalario.setText(caixa.getSalarioMedio().toString());
        }
        if (caixa.getReservaEmergencia() != null) {
            txtReserva.setText(caixa.getReservaEmergencia().toString());
        }
        if (caixa.getUsuario() != null && usuarios != null) {
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).getIdUsuario().equals(caixa.getUsuario().getIdUsuario())) {
                    cbUsuario.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void loadUsuarios() {
        usuarios = usuarioController.listar();
        cbUsuario.removeAllItems();
        for (Usuario u : usuarios) {
            cbUsuario.addItem(u.getNome());
        }
    }

    private void salvar() {
        caixa.setNome(txtNome.getText());
        caixa.setValorTotal(parseBigDecimal(txtValor.getText()));
        caixa.setSalarioMedio(parseBigDecimal(txtSalario.getText()));
        caixa.setReservaEmergencia(parseBigDecimal(txtReserva.getText()));
        if (cbUsuario.getSelectedIndex() >= 0) {
            caixa.setUsuario(usuarios.get(cbUsuario.getSelectedIndex()));
        }
        confirmed = true;
        dispose();
    }

    private BigDecimal parseBigDecimal(String text) {
        try {
            return text == null || text.isEmpty() ? null : new BigDecimal(text);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valor inválido: " + text, "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Caixa getCaixa() {
        return caixa;
    }
}
