package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Caderno;
import swing.Button;

public class CadernoDialog extends JDialog {

    private final Caderno caderno;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNomeIa;
    private JTextField txtTitulo;
    private JTextArea txtObjetivo;
    private JTextArea txtComando;
    private JTextArea txtResultado;
    private JTextField txtData;
    private JTextField txtIdUsuario;
    private JTextField txtIdCategoria;
    private byte[] imagem;
    private byte[] video;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CadernoDialog(Frame parent, Caderno caderno) {
        super(parent, true);
        this.caderno = caderno != null ? caderno : new Caderno();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (caderno.getIdCaderno() != null) {
            txtId.setText(String.valueOf(caderno.getIdCaderno()));
        }
        txtNomeIa.setText(caderno.getNomeIa());
        txtTitulo.setText(caderno.getTitulo());
        txtObjetivo.setText(caderno.getObjetivo());
        txtComando.setText(caderno.getComando());
        txtResultado.setText(caderno.getResultado());
        if (caderno.getData() != null) {
            txtData.setText(formatter.format(caderno.getData()));
        }
        if (caderno.getIdUsuario() != null) {
            txtIdUsuario.setText(String.valueOf(caderno.getIdUsuario()));
        }
        if (caderno.getIdCategoria() != null) {
            txtIdCategoria.setText(String.valueOf(caderno.getIdCategoria()));
        }
        imagem = caderno.getResultadoImagem();
        video = caderno.getResultadoVideo();
    }

    private void initComponents() {
        setTitle("Caderno");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        txtId = new JTextField(10);
        txtId.setEditable(false);
        addRow(panel, gbc, y++, "ID", txtId);

        txtNomeIa = new JTextField(20);
        addRow(panel, gbc, y++, "Nome IA", txtNomeIa);

        txtTitulo = new JTextField(20);
        addRow(panel, gbc, y++, "Título", txtTitulo);

        txtObjetivo = new JTextArea(3, 20);
        addRow(panel, gbc, y++, "Objetivo", new JScrollPane(txtObjetivo));

        txtComando = new JTextArea(3, 20);
        addRow(panel, gbc, y++, "Comando", new JScrollPane(txtComando));

        txtResultado = new JTextArea(3, 20);
        addRow(panel, gbc, y++, "Resultado", new JScrollPane(txtResultado));

        txtData = new JTextField(20);
        addRow(panel, gbc, y++, "Data (yyyy-MM-dd)", txtData);

        txtIdUsuario = new JTextField(20);
        addRow(panel, gbc, y++, "ID Usuário", txtIdUsuario);

        txtIdCategoria = new JTextField(20);
        addRow(panel, gbc, y++, "ID Categoria", txtIdCategoria);

        Button btnImagem = new Button();
        btnImagem.setText("Selecionar Imagem");
        btnImagem.setBackground(new Color(63,81,181));
        btnImagem.setForeground(Color.WHITE);
        btnImagem.addActionListener(e -> selecionarArquivo(true));
        addRow(panel, gbc, y++, "Imagem", btnImagem);

        Button btnVideo = new Button();
        btnVideo.setText("Selecionar Vídeo");
        btnVideo.setBackground(new Color(0,150,136));
        btnVideo.setForeground(Color.WHITE);
        btnVideo.addActionListener(e -> selecionarArquivo(false));
        addRow(panel, gbc, y++, "Vídeo", btnVideo);

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

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, java.awt.Component component) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void selecionarArquivo(boolean imagem) {
        JFileChooser chooser = new JFileChooser();
        int opt = chooser.showOpenDialog(this);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                byte[] data = Files.readAllBytes(file.toPath());
                if (imagem) {
                    this.imagem = data;
                } else {
                    this.video = data;
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao ler arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvar() {
        caderno.setNomeIa(txtNomeIa.getText());
        caderno.setTitulo(txtTitulo.getText());
        caderno.setObjetivo(txtObjetivo.getText());
        caderno.setComando(txtComando.getText());
        caderno.setResultado(txtResultado.getText());
        String dataTxt = txtData.getText();
        if (dataTxt != null && !dataTxt.isEmpty()) {
            try {
                caderno.setData(LocalDate.parse(dataTxt, formatter));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato yyyy-MM-dd.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            caderno.setData(null);
        }
        caderno.setResultadoImagem(imagem);
        caderno.setResultadoVideo(video);
        String usuario = txtIdUsuario.getText();
        if (usuario != null && !usuario.isEmpty()) {
            try {
                caderno.setIdUsuario(Integer.parseInt(usuario));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de usuário inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            caderno.setIdUsuario(null);
        }
        String categoria = txtIdCategoria.getText();
        if (categoria != null && !categoria.isEmpty()) {
            try {
                caderno.setIdCategoria(Integer.parseInt(categoria));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de categoria inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            caderno.setIdCategoria(null);
        }
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Caderno getCaderno() {
        return caderno;
    }
}
