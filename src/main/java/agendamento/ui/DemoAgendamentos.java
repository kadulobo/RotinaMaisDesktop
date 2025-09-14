package agendamento.ui;

import agendamento.controller.AgendamentosController;
import agendamento.dao.JobDao;
import agendamento.service.JobRunnerService;

import javax.swing.*;
import java.awt.*;

/**
 * Small demo application that opens the AgendamentosView in a frame.
 */
public class DemoAgendamentos {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JobDao dao = new JobDao();
            JobRunnerService service = new JobRunnerService(dao);
            AgendamentosController controller = new AgendamentosController(dao, service);
            JFrame f = new JFrame("Agendamentos Demo");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());
            f.add(new AgendamentosView(controller), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
