package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Ingrediente;
import swing.Button;
import swing.ImageAvatar;
import util.ImageUtils;

/** Dialog for creating or editing an {@link Ingrediente}. */
public class IngredienteDialog extends JDialog {

    private Ingrediente ingrediente;
    private boolean confirmed;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private ImageAvatar avatar;
    private byte[] fotoSelecionada;

    public IngredienteDialog(Frame parent, Ingrediente ingrediente) {
        super(parent, true);
        this.ingrediente = ingrediente != null ? ingrediente : new Ingrediente();
        this.fotoSelecionada = this.ingrediente.getFoto();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (ingrediente.getIdIngrediente() != null) {
            txtId.setText(String.valueOf(ingrediente.getIdIngrediente()));
        }
        if (ingrediente.getNome() != null) {
            txtNome.setText(ingrediente.getNome());
        }
        if (ingrediente.getDescricao() != null) {
            txtDescricao.setText(ingrediente.getDescricao());
        }
        atualizarAvatar();
    }

    private void initComponents() {
        setTitle("Ingrediente");
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

        JLabel lblDescricao = new JLabel("Descrição");
        txtDescricao = new JTextArea(4, 20);
        JScrollPane scroll = new JScrollPane(txtDescricao);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblDescricao, gbc);
        gbc.gridx = 1;
        panel.add(scroll, gbc);
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
        ingrediente.setNome(nome);
        ingrediente.setDescricao(txtDescricao.getText());
        ingrediente.setFoto(fotoSelecionada);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }
}
