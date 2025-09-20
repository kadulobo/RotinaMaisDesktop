package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Monitoramento;
import swing.Button;
import swing.ImageAvatar;
import util.ImageUtils;

/** Dialog for creating or editing a {@link Monitoramento}. */
public class MonitoramentoDialog extends JDialog {

    private Monitoramento monitoramento;
    private boolean confirmed;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JComboBox<StatusOption> cbStatus;
    private JTextField txtIdPeriodo;
    private ImageAvatar avatar;
    private byte[] fotoSelecionada;

    public MonitoramentoDialog(Frame parent, Monitoramento monitoramento) {
        super(parent, true);
        this.monitoramento = monitoramento != null ? monitoramento : new Monitoramento();
        this.fotoSelecionada = this.monitoramento.getFoto();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (monitoramento.getIdMonitoramento() != null) {
            txtId.setText(String.valueOf(monitoramento.getIdMonitoramento()));
        }
        if (monitoramento.getNome() != null) {
            txtNome.setText(monitoramento.getNome());
        }
        if (monitoramento.getDescricao() != null) {
            txtDescricao.setText(monitoramento.getDescricao());
        }
        if (monitoramento.getStatus() != null) {
            for (int i = 0; i < cbStatus.getItemCount(); i++) {
                if (cbStatus.getItemAt(i).codigo.equals(monitoramento.getStatus())) {
                    cbStatus.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (monitoramento.getIdPeriodo() != null) {
            txtIdPeriodo.setText(String.valueOf(monitoramento.getIdPeriodo()));
        }
        atualizarAvatar();
    }

    private void initComponents() {
        setTitle("Monitoramento");
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

        JLabel lblStatus = new JLabel("Status");
        cbStatus = new JComboBox<>(new StatusOption[]{
            new StatusOption("Ativo", 1),
            new StatusOption("Pausado", 2),
            new StatusOption("Alerta", 3)
        });
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblStatus, gbc);
        gbc.gridx = 1;
        panel.add(cbStatus, gbc);
        y++;

        JLabel lblPeriodo = new JLabel("Id Período");
        txtIdPeriodo = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(lblPeriodo, gbc);
        gbc.gridx = 1;
        panel.add(txtIdPeriodo, gbc);
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
        monitoramento.setNome(nome);
        monitoramento.setDescricao(txtDescricao.getText());
        monitoramento.setStatus(((StatusOption) cbStatus.getSelectedItem()).codigo);
        monitoramento.setFoto(fotoSelecionada);
        try {
            monitoramento.setIdPeriodo(parseInteger(txtIdPeriodo.getText()));
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
            JOptionPane.showMessageDialog(this, "Id do período inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("Id período inválido", ex);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Monitoramento getMonitoramento() {
        return monitoramento;
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
