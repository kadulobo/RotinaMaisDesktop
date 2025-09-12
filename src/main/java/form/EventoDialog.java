package form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.DefaultListCellRenderer;

import model.Categoria;
import model.Evento;
import controller.CategoriaController;
import dao.impl.CategoriaDaoNativeImpl;
import swing.Button;
import swing.ImageAvatar;
import util.ImageUtils;

/**
 * Dialog used for creating or editing an {@link Evento}.
 */
public class EventoDialog extends JDialog {

    private Evento evento;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JComboBox<String> cbStatus;
    private JComboBox<Categoria> cbCategoria;
    private ImageAvatar avatarPreview;
    private JButton btnEscolherImagem;

    private final CategoriaController categoriaController;

    public EventoDialog(Frame parent, Evento evento) {
        super(parent, true);
        this.evento = evento != null ? evento : new Evento();
        this.categoriaController = new CategoriaController(new CategoriaDaoNativeImpl());
        initComponents();
        setLocationRelativeTo(parent);
        if (this.evento.getIdEvento() != null) {
            txtId.setText(String.valueOf(this.evento.getIdEvento()));
            txtNome.setText(this.evento.getNome());
            txtDescricao.setText(this.evento.getDescricao());
            if (this.evento.getStatus() != null) {
                cbStatus.setSelectedIndex(this.evento.getStatus() == 1 ? 0 : 1);
            }
            if (this.evento.getCategoria() != null) {
                for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                    Categoria c = cbCategoria.getItemAt(i);
                    if (c.getIdCategoria().equals(this.evento.getCategoria().getIdCategoria())) {
                        cbCategoria.setSelectedIndex(i);
                        break;
                    }
                }
            }
            if (this.evento.getFoto() != null) {
                avatarPreview.setIcon(ImageUtils.bytesToImageIcon(this.evento.getFoto()));
            }
        }
    }

    private void initComponents() {
        setTitle("Evento");
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
        cbStatus = new JComboBox<>(new String[]{"Ativo","Desativado"});
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblStatus, gbc);
        gbc.gridx = 1; panel.add(cbStatus, gbc); y++;

        JLabel lblCategoria = new JLabel("Categoria");
        cbCategoria = new JComboBox<>();
        carregarCategorias();
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblCategoria, gbc);
        gbc.gridx = 1; panel.add(cbCategoria, gbc); y++;

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

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaController.listar();
            for (Categoria c : categorias) {
                if (c.getStatus() != null && c.getStatus() == 1) {
                    cbCategoria.addItem(c);
                }
            }
        } catch (Exception ex) {
            // ignore errors loading categories
        }
        cbCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Categoria) {
                    setText(((Categoria) value).getNome());
                }
                return this;
            }
        });
    }

    private void salvar() {
        evento.setNome(txtNome.getText());
        evento.setDescricao(txtDescricao.getText());
        evento.setStatus(cbStatus.getSelectedIndex() == 0 ? 1 : 2);
        Categoria cat = (Categoria) cbCategoria.getSelectedItem();
        if (cat != null) {
            evento.setCategoria(cat);
        }
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
                evento.setFoto(bytes);
                avatarPreview.setIcon(new ImageIcon(ImageIO.read(file)));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Evento getEvento() {
        return evento;
    }
}
