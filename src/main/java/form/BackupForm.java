package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Simple form to execute database backup and restore commands.
 */
public class BackupForm extends JPanel {

    private final JTextField txtPath;

    public BackupForm() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        JLabel lblPath = new JLabel("Caminho do arquivo:");
        txtPath = new JTextField(25);
        JButton btnBackup = new JButton("Gerar Backup");
        JButton btnRestore = new JButton("Restaurar Backup");

        inputPanel.add(lblPath);
        inputPanel.add(txtPath);
        inputPanel.add(btnBackup);
        inputPanel.add(btnRestore);

        add(inputPanel, BorderLayout.NORTH);

        btnBackup.addActionListener((ActionEvent e) -> executeCommand(true));
        btnRestore.addActionListener((ActionEvent e) -> executeCommand(false));
    }

    private void executeCommand(boolean backup) {
        String path = txtPath.getText().trim();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o caminho do arquivo.");
            return;
        }
        new Thread(() -> {
            try {
                ProcessBuilder pb;
                if (backup) {
                    pb = new ProcessBuilder("pg_dump", "-U", "kadu", "-h", "localhost", "-d", "rotinamais", "-f", path);
                } else {
                    pb = new ProcessBuilder("psql", "-U", "kadu", "-h", "localhost", "-d", "rotinamais", "-f", path);
                }
                pb.redirectErrorStream(true);
                Process p = pb.start();
                int exit = p.waitFor();
                SwingUtilities.invokeLater(() -> {
                    if (exit == 0) {
                        JOptionPane.showMessageDialog(this, "Comando executado com sucesso.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Falha ao executar comando.");
                    }
                });
            } catch (IOException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage()));
            }
        }).start();
    }
}
