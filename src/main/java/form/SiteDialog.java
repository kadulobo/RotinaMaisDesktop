package form;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Site;
import swing.Button;

public class SiteDialog extends JDialog {

    private final Site site;
    private boolean confirmed = false;

    private JTextField txtId;
    private JTextField txtUrl;
    private JCheckBox chkAtivo;
    private byte[] logo;

    public SiteDialog(Frame parent, Site site) {
        super(parent, true);
        this.site = site != null ? site : new Site();
        this.logo = this.site.getLogo();
        initComponents();
        setLocationRelativeTo(parent);
        preencherCampos();
    }

    private void preencherCampos() {
        if (site.getIdSite() != null) {
            txtId.setText(String.valueOf(site.getIdSite()));
        }
        txtUrl.setText(site.getUrl());
        chkAtivo.setSelected(Boolean.TRUE.equals(site.getAtivo()));
    }

    private void initComponents() {
        setTitle("Site");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        txtId = new JTextField(10);
        txtId.setEditable(false);
        addRow(panel, gbc, y++, "ID", txtId);

        txtUrl = new JTextField(20);
        addRow(panel, gbc, y++, "URL", txtUrl);

        chkAtivo = new JCheckBox("Ativo");
        addRow(panel, gbc, y++, "Status", chkAtivo);

        Button btnLogo = new Button();
        btnLogo.setText("Selecionar Logo");
        btnLogo.setBackground(new Color(0,150,136));
        btnLogo.setForeground(Color.WHITE);
        btnLogo.addActionListener(e -> selecionarLogo());
        addRow(panel, gbc, y++, "Logo", btnLogo);

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

    private void selecionarLogo() {
        JFileChooser chooser = new JFileChooser();
        int opt = chooser.showOpenDialog(this);
        if (opt == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                logo = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao ler arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvar() {
        site.setUrl(txtUrl.getText());
        site.setAtivo(chkAtivo.isSelected());
        site.setLogo(logo);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Site getSite() {
        return site;
    }
}
