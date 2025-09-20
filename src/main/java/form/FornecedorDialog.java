package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Fornecedor;
import swing.Button;
import swing.ImageAvatar;
import util.ImageUtils;

/** Dialog for creating or editing a {@link Fornecedor}. */
public class FornecedorDialog extends JDialog {

    private Fornecedor fornecedor;
    private boolean confirmed;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEndereco;
    private JCheckBox chkOnline;
    private ImageAvatar avatar;
    private byte[] fotoSelecionada;

    public FornecedorDialog(Frame parent, Fornecedor fornecedor) {
        super(parent, true);
        this.fornecedor = fornecedor != null ? fornecedor : new Fornecedor();
        this.fotoSelecionada = this.fornecedor.getFoto();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (fornecedor.getIdFornecedor() != null) {
            txtId.setText(String.valueOf(fornecedor.getIdFornecedor()));
        }
        if (fornecedor.getNome() != null) {
            txtNome.setText(fornecedor.getNome());
        }
        if (fornecedor.getEndereco() != null) {
            txtEndereco.setText(fornecedor.getEndereco());
        }
        if (fornecedor.getOnline() != null) {
            chkOnline.setSelected(fornecedor.getOnline());
        }
        atualizarAvatar();
    }

    private void initComponents() {
        setTitle("Fornecedor");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        JLabel lblId = new JLabel("ID");
        txtId = new JTextField(10);
        txtId.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblId, gbc);
        gbc.gridx = 1;
        panel.add(txtId, gbc);
        y++;

        JLabel lblNome = new JLabel("Nome");
        txtNome = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblNome, gbc);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);
        y++;

        JLabel lblEndereco = new JLabel("Endereço");
        txtEndereco = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblEndereco, gbc);
        gbc.gridx = 1;
        panel.add(txtEndereco, gbc);
        y++;

        JLabel lblOnline = new JLabel("Online");
        chkOnline = new JCheckBox();
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblOnline, gbc);
        gbc.gridx = 1;
        panel.add(chkOnline, gbc);
        y++;

        JLabel lblFoto = new JLabel("Foto");
        avatar = new ImageAvatar();
        avatar.setPreferredSize(new java.awt.Dimension(80, 80));
        Button btnSelecionar = new Button();
        btnSelecionar.setText("Selecionar foto");
        btnSelecionar.addActionListener(e -> selecionarFoto());
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblFoto, gbc);
        gbc.gridx = 1;
        panel.add(avatar, gbc);
        y++;
        gbc.gridx = 1;
        panel.add(btnSelecionar, gbc);
        y++;

        Button btnSalvar = new Button();
        btnSalvar.setText("Salvar");
        btnSalvar.setBackground(new Color(75, 134, 253));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvar());

        Button btnCancelar = new Button();
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(btnSalvar, gbc);
        gbc.gridx = 1;
        panel.add(btnCancelar, gbc);

        getContentPane().add(panel);
        pack();
        getRootPane().setDefaultButton(btnSalvar);
    }

    private void selecionarFoto() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                fotoSelecionada = Files.readAllBytes(file.toPath());
                atualizarAvatar();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Não foi possível carregar a imagem", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarAvatar() {
        if (fotoSelecionada != null) {
            avatar.setIcon(ImageUtils.bytesToImageIcon(fotoSelecionada));
        } else {
            avatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.jpg")));
        }
    }

    private void salvar() {
        String nome = txtNome.getText();
        if (nome == null || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        fornecedor.setNome(nome);
        fornecedor.setEndereco(txtEndereco.getText());
        fornecedor.setOnline(chkOnline.isSelected());
        fornecedor.setFoto(fotoSelecionada);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }
}
