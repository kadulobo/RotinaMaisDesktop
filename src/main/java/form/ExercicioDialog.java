package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JOptionPane;
import model.Exercicio;
import swing.Button;

/** Dialog for creating or editing an {@link Exercicio}. */
public class ExercicioDialog extends JDialog {

    private Exercicio exercicio;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtNome;
    private JSpinner spCargaLeve;
    private JSpinner spCargaMedia;
    private JSpinner spCargaMaxima;

    public ExercicioDialog(Frame parent, Exercicio exercicio) {
        super(parent, true);
        this.exercicio = exercicio != null ? exercicio : new Exercicio();
        initComponents();
        setLocationRelativeTo(parent);
        if (this.exercicio.getIdExercicio() != null) {
            txtId.setText(String.valueOf(this.exercicio.getIdExercicio()));
        }
        if (this.exercicio.getNome() != null) {
            txtNome.setText(this.exercicio.getNome());
        }
        if (this.exercicio.getCargaLeve() != null) {
            spCargaLeve.setValue(this.exercicio.getCargaLeve());
        }
        if (this.exercicio.getCargaMedia() != null) {
            spCargaMedia.setValue(this.exercicio.getCargaMedia());
        }
        if (this.exercicio.getCargaMaxima() != null) {
            spCargaMaxima.setValue(this.exercicio.getCargaMaxima());
        }
    }

    private void initComponents() {
        setTitle("Exercício");
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

        JLabel lblCargaLeve = new JLabel("Carga leve");
        spCargaLeve = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblCargaLeve, gbc);
        gbc.gridx = 1;
        panel.add(spCargaLeve, gbc);
        y++;

        JLabel lblCargaMedia = new JLabel("Carga média");
        spCargaMedia = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblCargaMedia, gbc);
        gbc.gridx = 1;
        panel.add(spCargaMedia, gbc);
        y++;

        JLabel lblCargaMaxima = new JLabel("Carga máxima");
        spCargaMaxima = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblCargaMaxima, gbc);
        gbc.gridx = 1;
        panel.add(spCargaMaxima, gbc);
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

    private void salvar() {
        String nome = txtNome.getText();
        if (nome == null || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        exercicio.setNome(nome);
        exercicio.setCargaLeve(((Number) spCargaLeve.getValue()).intValue());
        exercicio.setCargaMedia(((Number) spCargaMedia.getValue()).intValue());
        exercicio.setCargaMaxima(((Number) spCargaMaxima.getValue()).intValue());
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }
}
