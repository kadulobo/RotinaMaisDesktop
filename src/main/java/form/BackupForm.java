package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Simple form to execute database backup and restore commands.
 */
public class BackupForm extends JPanel {

    private final JTextField txtPath;
    private final Properties dbProps;

    public BackupForm() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        JLabel lblPath = new JLabel("Caminho do arquivo:");
        txtPath = new JTextField(25);
        txtPath.setEditable(false);
        JButton btnBackup = new JButton("Gerar Backup");
        JButton btnRestore = new JButton("Restaurar Backup");

        inputPanel.add(lblPath);
        inputPanel.add(txtPath);
        inputPanel.add(btnBackup);
        inputPanel.add(btnRestore);

        add(inputPanel, BorderLayout.NORTH);

        dbProps = loadProperties();

        btnBackup.addActionListener((ActionEvent e) -> choosePathAndExecute(true));
        btnRestore.addActionListener((ActionEvent e) -> choosePathAndExecute(false));
    }

    private void choosePathAndExecute(boolean backup) {
        JFileChooser chooser = new JFileChooser();
        if (backup) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File dir = chooser.getSelectedFile();
                String schema = dbProps.getProperty("db.schema", "backup");
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File file = new File(dir, schema + "_" + timestamp + ".sql");
                txtPath.setText(file.getAbsolutePath());
                executeCommand(true, file.getAbsolutePath());
            }
        } else {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                txtPath.setText(file.getAbsolutePath());
                executeCommand(false, file.getAbsolutePath());
            }
        }
    }

    private void executeCommand(boolean backup, String path) {
        new Thread(() -> {
            try {
                String user = dbProps.getProperty("db.user", "postgres");
                String db = dbProps.getProperty("db.name", "");
                String password = dbProps.getProperty("db.password", "");

                ProcessBuilder pb;
                if (backup) {
                    pb = new ProcessBuilder("pg_dump", "-U", user, "-h", "localhost", "-d", db, "-f", path);
                } else {
                    pb = new ProcessBuilder("psql", "-U", user, "-h", "localhost", "-d", db, "-f", path);
                }
                if (!password.isEmpty()) {
                    pb.environment().put("PGPASSWORD", password);
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

    private Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("database.properties")) {
            props.load(in);
        } catch (IOException e) {
            // use defaults when file is not available
        }
        return props;
    }
}
