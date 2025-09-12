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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import model.Categoria;
import swing.Button;
import swing.ImageAvatar;
import util.ImageUtils;

/**
 * Dialog used for creating or editing a {@link Categoria}.
 */
public class CategoriaDialog extends JDialog {

    private Categoria categoria;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private ImageAvatar avatarPreview;
    private JButton btnEscolherImagem;
    private JComboBox<String> comboStatus;

    public CategoriaDialog(Frame parent, Categoria categoria) {
        super(parent, true);
        this.categoria = categoria != null ? categoria : new Categoria();
        initComponents();
        setLocationRelativeTo(parent);
        comboStatus.setSelectedIndex(this.categoria.getStatus() != null && this.categoria.getStatus() == 0 ? 1 : 0);
        if (this.categoria.getIdCategoria() != null) {
            txtId.setText(String.valueOf(this.categoria.getIdCategoria()));
            txtNome.setText(this.categoria.getNome());
            txtDescricao.setText(this.categoria.getDescricao());
            if (this.categoria.getFoto() != null) {
                avatarPreview.setIcon(ImageUtils.bytesToImageIcon(this.categoria.getFoto()));
            }
        }
    }

    private void initComponents() {
        setTitle("Categoria");
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

        JLabel lblDescricao = new JLabel("Descrição");
        txtDescricao = new JTextArea(3,20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblDescricao, gbc);
        gbc.gridx = 1; panel.add(txtDescricao, gbc); y++;

        JLabel lblStatus = new JLabel("Status");
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Pendente"});
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblStatus, gbc);
        gbc.gridx = 1; panel.add(comboStatus, gbc); y++;

        JLabel lblFoto = new JLabel("Foto");
        avatarPreview = new ImageAvatar();
        avatarPreview.setPreferredSize(new Dimension(64,64));
        btnEscolherImagem = new JButton("Escolher imagem...");
        btnEscolherImagem.addActionListener(e -> escolherImagem());
        JPanel fotoPanel = new JPanel();
        fotoPanel.add(avatarPreview);
        fotoPanel.add(btnEscolherImagem);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblFoto, gbc);
        gbc.gridx = 1; panel.add(fotoPanel, gbc); y++;

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
        categoria.setNome(txtNome.getText());
        categoria.setDescricao(txtDescricao.getText());
        categoria.setStatus(comboStatus.getSelectedIndex() == 0 ? 1 : 0);
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
                categoria.setFoto(bytes);
                avatarPreview.setIcon(new ImageIcon(ImageIO.read(file)));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
