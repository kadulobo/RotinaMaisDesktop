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
import javax.swing.JTextField;
import model.Meta;
import swing.Button;

public class MetaDialog extends JDialog {

    private final Meta meta;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtPontoMinimo;
    private JTextField txtPontoMedio;
    private JTextField txtPontoMaximo;
    private JTextField txtStatus;
    private JTextField txtIdPeriodo;
    private byte[] foto;

    public MetaDialog(Frame parent, Meta meta) {
        super(parent, true);
        this.meta = meta != null ? meta : new Meta();
        this.foto = this.meta.getFoto();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (meta.getIdMeta() != null) {
            txtId.setText(String.valueOf(meta.getIdMeta()));
        }
        if (meta.getPontoMinimo() != null) {
            txtPontoMinimo.setText(String.valueOf(meta.getPontoMinimo()));
        }
        if (meta.getPontoMedio() != null) {
            txtPontoMedio.setText(String.valueOf(meta.getPontoMedio()));
        }
        if (meta.getPontoMaximo() != null) {
            txtPontoMaximo.setText(String.valueOf(meta.getPontoMaximo()));
        }
        if (meta.getStatus() != null) {
            txtStatus.setText(String.valueOf(meta.getStatus()));
        }
        if (meta.getIdPeriodo() != null) {
            txtIdPeriodo.setText(String.valueOf(meta.getIdPeriodo()));
        }
    }

    private void initComponents() {
        setTitle("Meta");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        txtId = new JTextField(10);
        txtId.setEditable(false);
        addRow(panel, gbc, y++, "ID", txtId);

        txtPontoMinimo = new JTextField(20);
        addRow(panel, gbc, y++, "Ponto Mínimo", txtPontoMinimo);

        txtPontoMedio = new JTextField(20);
        addRow(panel, gbc, y++, "Ponto Médio", txtPontoMedio);

        txtPontoMaximo = new JTextField(20);
        addRow(panel, gbc, y++, "Ponto Máximo", txtPontoMaximo);

        txtStatus = new JTextField(20);
        addRow(panel, gbc, y++, "Status", txtStatus);

        txtIdPeriodo = new JTextField(20);
        addRow(panel, gbc, y++, "ID Período", txtIdPeriodo);

        Button btnFoto = new Button();
        btnFoto.setText("Selecionar Foto");
        btnFoto.setBackground(new Color(255,152,0));
        btnFoto.setForeground(Color.BLACK);
        btnFoto.addActionListener(e -> selecionarFoto());
        addRow(panel, gbc, y++, "Foto", btnFoto);

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

    private void selecionarFoto() {
        JFileChooser chooser = new JFileChooser();
        int opt = chooser.showOpenDialog(this);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                foto = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao ler arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvar() {
        try {
            meta.setPontoMinimo(parseInt(txtPontoMinimo.getText()));
            meta.setPontoMedio(parseInt(txtPontoMedio.getText()));
            meta.setPontoMaximo(parseInt(txtPontoMaximo.getText()));
            meta.setStatus(parseInt(txtStatus.getText()));
            meta.setIdPeriodo(parseInt(txtIdPeriodo.getText()));
            meta.setFoto(foto);
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preencha os campos numéricos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Meta getMeta() {
        return meta;
    }
}
