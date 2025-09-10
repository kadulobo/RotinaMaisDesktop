package form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Usuario;
import swing.Button;
import swing.ImageAvatar;

/**
 * Dialog used for creating or editing an {@link Usuario}.
 */
public class UsuarioDialog extends JDialog {

    private Usuario usuario;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JPasswordField txtSenha;
    private ImageAvatar avatarPreview;
    private JButton btnEscolherImagem;
    private JTextField txtEmail;
    private JTextField txtCpf;

    public UsuarioDialog(Frame parent, Usuario usuario) {
        super(parent, true);
        this.usuario = usuario != null ? usuario : new Usuario();
        initComponents();
        setLocationRelativeTo(parent);
        if (usuario != null && usuario.getIdUsuario() != null) {
            txtId.setText(String.valueOf(usuario.getIdUsuario()));
            txtNome.setText(usuario.getNome());
            txtSenha.setText(usuario.getSenha());
            txtEmail.setText(usuario.getEmail());
            txtCpf.setText(usuario.getCpf());
            if (usuario.getFoto() != null) {
                avatarPreview.setIcon(new ImageIcon(usuario.getFoto()));
            }
        }
    }

    private void initComponents() {
        setTitle("Usuário");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
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

        JLabel lblSenha = new JLabel("Senha");
        txtSenha = new JPasswordField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblSenha, gbc);
        gbc.gridx = 1; panel.add(txtSenha, gbc); y++;

        JLabel lblFoto = new JLabel("Foto");
        avatarPreview = new ImageAvatar();
        avatarPreview.setPreferredSize(new Dimension(64, 64));
        btnEscolherImagem = new JButton("Escolher imagem...");
        btnEscolherImagem.addActionListener(e -> escolherImagem());
        JPanel fotoPanel = new JPanel();
        fotoPanel.add(avatarPreview);
        fotoPanel.add(btnEscolherImagem);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblFoto, gbc);
        gbc.gridx = 1; panel.add(fotoPanel, gbc); y++;

        JLabel lblEmail = new JLabel("Email");
        txtEmail = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblEmail, gbc);
        gbc.gridx = 1; panel.add(txtEmail, gbc); y++;

        JLabel lblCpf = new JLabel("CPF");
        txtCpf = new JTextField(15);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblCpf, gbc);
        gbc.gridx = 1; panel.add(txtCpf, gbc); y++;

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

    private void salvar() {
        usuario.setNome(txtNome.getText());
        usuario.setSenha(new String(txtSenha.getPassword()));
        usuario.setEmail(txtEmail.getText());
        usuario.setCpf(txtCpf.getText());
        confirmed = true;
        dispose();
    }

    private void escolherImagem() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
                usuario.setFoto(bytes);
                avatarPreview.setIcon(new ImageIcon(ImageIO.read(file)));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

