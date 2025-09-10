package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.Cofre;
import swing.Button;

/** Dialog for creating or editing a {@link Cofre}. */
public class CofreDialog extends JDialog {

    private Cofre cofre;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtPlataforma;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbTipo;

    public CofreDialog(Frame parent, Cofre c) {
        super(parent, true);
        this.cofre = c != null ? c : new Cofre();
        initComponents();
        setLocationRelativeTo(parent);
        if (this.cofre.getIdCofre() != null) {
            txtId.setText(String.valueOf(this.cofre.getIdCofre()));
            txtPlataforma.setText(this.cofre.getPlataforma());
            txtLogin.setText(this.cofre.getLogin());
            txtSenha.setText(this.cofre.getSenha());
            if (this.cofre.getTipo() != null && this.cofre.getTipo() >= 1 && this.cofre.getTipo() <= 3) {
                cbTipo.setSelectedIndex(this.cofre.getTipo() - 1);
            }
        }
    }

    private byte[] defaultFoto(int tipo) {
        String path;
        switch (tipo) {
            case 1:
                path = "/icon/profile.jpg";
                break;
            case 2:
                path = "/icon/profile1.jpg";
                break;
            case 3:
                path = "/icon/profile2.jpg";
                break;
            default:
                return null;
        }
        try (InputStream is = getClass().getResourceAsStream(path)) {
            return is != null ? is.readAllBytes() : null;
        } catch (IOException ex) {
            return null;
        }
    }

    private void initComponents() {
        setTitle("Cofre");
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

        JLabel lblPlataforma = new JLabel("Plataforma");
        txtPlataforma = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblPlataforma, gbc);
        gbc.gridx = 1; panel.add(txtPlataforma, gbc); y++;

        JLabel lblLogin = new JLabel("Login");
        txtLogin = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblLogin, gbc);
        gbc.gridx = 1; panel.add(txtLogin, gbc); y++;

        JLabel lblSenha = new JLabel("Senha");
        txtSenha = new JPasswordField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblSenha, gbc);
        gbc.gridx = 1; panel.add(txtSenha, gbc); y++;

        JLabel lblTipo = new JLabel("Tipo");
        cbTipo = new JComboBox<>(new String[]{"Pessoa", "Trabalho", "Financeiro"});
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblTipo, gbc);
        gbc.gridx = 1; panel.add(cbTipo, gbc); y++;

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
        cofre.setPlataforma(txtPlataforma.getText());
        cofre.setLogin(txtLogin.getText());
        cofre.setSenha(new String(txtSenha.getPassword()));
        cofre.setTipo(cbTipo.getSelectedIndex() + 1);
        cofre.setFoto(defaultFoto(cofre.getTipo()));
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Cofre getCofre() {
        return cofre;
    }
}

