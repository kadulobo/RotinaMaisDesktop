package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Carteira;
import swing.Button;

public class CarteiraDialog extends JDialog {

    private final Carteira carteira;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtTipo;
    private JTextField txtDataInicio;
    private JTextField txtIdUsuario;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CarteiraDialog(Frame parent, Carteira carteira) {
        super(parent, true);
        this.carteira = carteira != null ? carteira : new Carteira();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (carteira.getIdCarteira() != null) {
            txtId.setText(String.valueOf(carteira.getIdCarteira()));
        }
        if (carteira.getNome() != null) {
            txtNome.setText(carteira.getNome());
        }
        if (carteira.getTipo() != null) {
            txtTipo.setText(carteira.getTipo());
        }
        if (carteira.getDataInicio() != null) {
            txtDataInicio.setText(formatter.format(carteira.getDataInicio()));
        }
        if (carteira.getIdUsuario() != null) {
            txtIdUsuario.setText(String.valueOf(carteira.getIdUsuario()));
        }
    }

    private void initComponents() {
        setTitle("Carteira");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        JLabel lblId = new JLabel("ID");
        txtId = new JTextField(10);
        txtId.setEditable(false);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblId, gbc);
        gbc.gridx = 1; panel.add(txtId, gbc); y++;

        JLabel lblNome = new JLabel("Nome");
        txtNome = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblNome, gbc);
        gbc.gridx = 1; panel.add(txtNome, gbc); y++;

        JLabel lblTipo = new JLabel("Tipo");
        txtTipo = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblTipo, gbc);
        gbc.gridx = 1; panel.add(txtTipo, gbc); y++;

        JLabel lblData = new JLabel("Data Início (yyyy-MM-dd)");
        txtDataInicio = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblData, gbc);
        gbc.gridx = 1; panel.add(txtDataInicio, gbc); y++;

        JLabel lblUsuario = new JLabel("ID Usuário");
        txtIdUsuario = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblUsuario, gbc);
        gbc.gridx = 1; panel.add(txtIdUsuario, gbc); y++;

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

    private void salvar() {
        carteira.setNome(txtNome.getText());
        carteira.setTipo(txtTipo.getText());
        String data = txtDataInicio.getText();
        if (data != null && !data.isEmpty()) {
            try {
                carteira.setDataInicio(LocalDate.parse(data, formatter));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato yyyy-MM-dd.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            carteira.setDataInicio(null);
        }
        String usuario = txtIdUsuario.getText();
        if (usuario != null && !usuario.isEmpty()) {
            try {
                carteira.setIdUsuario(Integer.parseInt(usuario));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de usuário inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            carteira.setIdUsuario(null);
        }
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Carteira getCarteira() {
        return carteira;
    }
}
