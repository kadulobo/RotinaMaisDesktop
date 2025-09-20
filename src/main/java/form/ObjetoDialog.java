package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Objeto;
import swing.Button;

public class ObjetoDialog extends JDialog {

    private final Objeto objeto;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtTipo;
    private JTextField txtValor;
    private JTextArea txtDescricao;
    private byte[] foto;

    public ObjetoDialog(Frame parent, Objeto objeto) {
        super(parent, true);
        this.objeto = objeto != null ? objeto : new Objeto();
        this.foto = this.objeto.getFoto();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (objeto.getIdObjeto() != null) {
            txtId.setText(String.valueOf(objeto.getIdObjeto()));
        }
        txtNome.setText(objeto.getNome());
        if (objeto.getTipo() != null) {
            txtTipo.setText(String.valueOf(objeto.getTipo()));
        }
        if (objeto.getValor() != null) {
            txtValor.setText(objeto.getValor().toPlainString());
        }
        txtDescricao.setText(objeto.getDescricao());
    }

    private void initComponents() {
        setTitle("Objeto");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        txtId = new JTextField(10);
        txtId.setEditable(false);
        addRow(panel, gbc, y++, "ID", txtId);

        txtNome = new JTextField(20);
        addRow(panel, gbc, y++, "Nome", txtNome);

        txtTipo = new JTextField(20);
        addRow(panel, gbc, y++, "Tipo", txtTipo);

        txtValor = new JTextField(20);
        addRow(panel, gbc, y++, "Valor", txtValor);

        txtDescricao = new JTextArea(3, 20);
        addRow(panel, gbc, y++, "Descrição", new JScrollPane(txtDescricao));

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
            objeto.setNome(txtNome.getText());
            objeto.setTipo(parseInteger(txtTipo.getText()));
            objeto.setValor(parseDecimal(txtValor.getText()));
            objeto.setDescricao(txtDescricao.getText());
            objeto.setFoto(foto);
            confirmed = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preencha os campos numéricos corretamente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new BigDecimal(value);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Objeto getObjeto() {
        return objeto;
    }
}
