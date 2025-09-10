package form;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import swing.Button;

/**
 * Diálogo simples para cadastro de movimentação.
 */
public class MovimentacaoDialog extends JDialog {

    public MovimentacaoDialog(Frame parent) {
        super(parent, true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setTitle("Movimentação");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Tela de cadastro de movimentação", SwingConstants.CENTER), BorderLayout.CENTER);
        Button btnFechar = new Button();
        btnFechar.setText("Fechar");
        btnFechar.addActionListener(e -> dispose());
        panel.add(btnFechar, BorderLayout.SOUTH);
        getContentPane().add(panel);
        setSize(350, 150);
    }
}
