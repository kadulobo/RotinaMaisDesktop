package form;

import java.awt.Color;
import java.awt.Frame;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.Usuario;
import swing.Button;

/**
 * Dialog used for creating or editing an {@link Usuario}.
 */
public class UsuarioDialog extends JDialog {

    private Usuario usuario;
    private boolean confirmed = false;

    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtCpf;
    private JPasswordField txtSenha;

    public UsuarioDialog(Frame parent, Usuario usuario) {
        super(parent, true);
        this.usuario = usuario != null ? usuario : new Usuario();
        initComponents();
        setLocationRelativeTo(parent);
        if (usuario != null) {
            txtNome.setText(usuario.getNome());
            txtEmail.setText(usuario.getEmail());
            txtCpf.setText(usuario.getCpf());
            txtSenha.setText(usuario.getSenha());
        }
    }

    private void initComponents() {
        setTitle("Usuário");
        JPanel panel = new JPanel();

        JLabel lblNome = new JLabel("Nome");
        txtNome = new JTextField(20);
        JLabel lblEmail = new JLabel("Email");
        txtEmail = new JTextField(20);
        JLabel lblCpf = new JLabel("CPF");
        txtCpf = new JTextField(15);
        JLabel lblSenha = new JLabel("Senha");
        txtSenha = new JPasswordField(20);

        Button btnSalvar = new Button();
        btnSalvar.setText("Salvar");
        btnSalvar.setBackground(new Color(75, 134, 253));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvar());

        Button btnCancelar = new Button();
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblNome)
                .addComponent(txtNome)
                .addComponent(lblEmail)
                .addComponent(txtEmail)
                .addComponent(lblCpf)
                .addComponent(txtCpf)
                .addComponent(lblSenha)
                .addComponent(txtSenha)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnSalvar, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(lblNome)
                .addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lblEmail)
                .addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lblCpf)
                .addComponent(txtCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(lblSenha)
                .addComponent(txtSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(panel);
        pack();
    }

    private void salvar() {
        usuario.setNome(txtNome.getText());
        usuario.setEmail(txtEmail.getText());
        usuario.setCpf(txtCpf.getText());
        usuario.setSenha(new String(txtSenha.getPassword()));
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

