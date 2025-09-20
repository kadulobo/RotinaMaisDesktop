package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane;
import model.Rotina;
import swing.Button;

/** Dialog for creating or editing a {@link Rotina}. */
public class RotinaDialog extends JDialog {

    private Rotina rotina;
    private boolean confirmed;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtInicio;
    private JTextField txtFim;
    private JTextArea txtDescricao;
    private JComboBox<StatusOption> cbStatus;
    private JSpinner spPonto;
    private JTextField txtIdUsuario;

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public RotinaDialog(Frame parent, Rotina rotina) {
        super(parent, true);
        this.rotina = rotina != null ? rotina : new Rotina();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (rotina.getIdRotina() != null) {
            txtId.setText(String.valueOf(rotina.getIdRotina()));
        }
        if (rotina.getNome() != null) {
            txtNome.setText(rotina.getNome());
        }
        if (rotina.getInicio() != null) {
            txtInicio.setText(rotina.getInicio().format(formatter));
        }
        if (rotina.getFim() != null) {
            txtFim.setText(rotina.getFim().format(formatter));
        }
        if (rotina.getDescricao() != null) {
            txtDescricao.setText(rotina.getDescricao());
        }
        if (rotina.getStatus() != null) {
            for (int i = 0; i < cbStatus.getItemCount(); i++) {
                if (cbStatus.getItemAt(i).codigo.equals(rotina.getStatus())) {
                    cbStatus.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (rotina.getPonto() != null) {
            spPonto.setValue(rotina.getPonto());
        }
        if (rotina.getIdUsuario() != null) {
            txtIdUsuario.setText(String.valueOf(rotina.getIdUsuario()));
        }
    }

    private void initComponents() {
        setTitle("Rotina");
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

        JLabel lblInicio = new JLabel("Início (AAAA-MM-DD)");
        txtInicio = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblInicio, gbc);
        gbc.gridx = 1;
        panel.add(txtInicio, gbc);
        y++;

        JLabel lblFim = new JLabel("Fim (AAAA-MM-DD)");
        txtFim = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblFim, gbc);
        gbc.gridx = 1;
        panel.add(txtFim, gbc);
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

        JLabel lblStatus = new JLabel("Status");
        cbStatus = new JComboBox<>(new StatusOption[]{
            new StatusOption("Ativa", 1),
            new StatusOption("Pausada", 2),
            new StatusOption("Concluída", 3)
        });
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblStatus, gbc);
        gbc.gridx = 1;
        panel.add(cbStatus, gbc);
        y++;

        JLabel lblPonto = new JLabel("Pontos");
        spPonto = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblPonto, gbc);
        gbc.gridx = 1;
        panel.add(spPonto, gbc);
        y++;

        JLabel lblUsuario = new JLabel("Id Usuário");
        txtIdUsuario = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panel.add(txtIdUsuario, gbc);
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
        rotina.setNome(nome);
        rotina.setDescricao(txtDescricao.getText());
        rotina.setStatus(((StatusOption) cbStatus.getSelectedItem()).codigo);
        rotina.setPonto(((Number) spPonto.getValue()).intValue());
        try {
            rotina.setIdUsuario(parseInteger(txtIdUsuario.getText()));
            rotina.setInicio(parseDate(txtInicio.getText()));
            rotina.setFim(parseDate(txtFim.getText()));
        } catch (IllegalArgumentException ex) {
            return;
        }
        confirmed = true;
        dispose();
    }

    private Integer parseInteger(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Id do usuário inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Id inválido", ex);
        }
    }

    private LocalDate parseDate(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Utilize o formato AAAA-MM-DD", "Erro", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Data inválida", ex);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Rotina getRotina() {
        return rotina;
    }

    private static class StatusOption {
        private final String descricao;
        private final Integer codigo;

        private StatusOption(String descricao, Integer codigo) {
            this.descricao = descricao;
            this.codigo = codigo;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }
}
