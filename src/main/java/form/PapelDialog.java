package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DateFormatter;
import model.Papel;
import swing.Button;

/** Dialog for creating or editing a {@link Papel}. */
public class PapelDialog extends JDialog {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Papel papel;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtCodigo;
    private JTextField txtTipo;
    private JFormattedTextField txtVencimento;

    public PapelDialog(Frame parent, Papel p) {
        super(parent, true);
        this.papel = p != null ? p : new Papel();
        initComponents();
        setLocationRelativeTo(parent);
        if (this.papel.getIdPapel() != null) {
            txtId.setText(String.valueOf(this.papel.getIdPapel()));
        }
        if (this.papel.getCodigo() != null) {
            txtCodigo.setText(this.papel.getCodigo());
        }
        if (this.papel.getTipo() != null) {
            txtTipo.setText(this.papel.getTipo());
        }
        if (this.papel.getVencimento() != null) {
            txtVencimento.setText(this.papel.getVencimento().format(FORMATTER));
        }
    }

    private void initComponents() {
        setTitle("Papel");
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

        JLabel lblCodigo = new JLabel("Código");
        txtCodigo = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblCodigo, gbc);
        gbc.gridx = 1; panel.add(txtCodigo, gbc); y++;

        JLabel lblTipo = new JLabel("Tipo");
        txtTipo = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblTipo, gbc);
        gbc.gridx = 1; panel.add(txtTipo, gbc); y++;

        JLabel lblVencimento = new JLabel("Vencimento (yyyy-MM-dd)");
        DateFormatter formatter = new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"));
        formatter.setAllowsInvalid(false);
        txtVencimento = new JFormattedTextField(formatter);
        txtVencimento.setColumns(20);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lblVencimento, gbc);
        gbc.gridx = 1; panel.add(txtVencimento, gbc); y++;

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
        papel.setCodigo(txtCodigo.getText());
        papel.setTipo(txtTipo.getText());
        String vencStr = txtVencimento.getText();
        if (vencStr != null && !vencStr.trim().isEmpty()) {
            try {
                LocalDate data = LocalDate.parse(vencStr, FORMATTER);
                papel.setVencimento(data);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data inválida. Utilize o formato yyyy-MM-dd.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            papel.setVencimento(null);
        }
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Papel getPapel() {
        return papel;
    }
}
