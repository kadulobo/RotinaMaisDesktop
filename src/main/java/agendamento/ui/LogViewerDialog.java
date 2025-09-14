package agendamento.ui;

import javax.swing.*;
import java.awt.*;

/** Simple dialog to show logs of a step run. */
public class LogViewerDialog {
    public static void show(Component parent, String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(parent, sp, "Log", JOptionPane.PLAIN_MESSAGE);
    }
}
