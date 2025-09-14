package agendamento.ui;

import agendamento.controller.AgendamentosController;
import agendamento.model.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Swing panel showing jobs and their executions in a Jenkins-like stage view.
 */
public class AgendamentosView extends JPanel {
    private final AgendamentosController controller;
    private final JComboBox<Job> comboJobs = new JComboBox<>();
    private final JButton btnExecutar = new JButton("Executar agora");
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JTextField txtQtd = new JTextField("10", 3);
    private final StageTableModel tableModel = new StageTableModel();
    private final JTable table = new JTable(tableModel);

    public AgendamentosView(AgendamentosController controller) {
        super(new BorderLayout());
        this.controller = controller;
        initTop();
        initCenter();
        loadJobs();
    }

    private void initTop() {
        JPanel top = new JPanel();
        top.add(new JLabel("Job:"));
        top.add(comboJobs);
        top.add(btnExecutar);
        top.add(btnAtualizar);
        top.add(new JLabel("Qtd runs:"));
        top.add(txtQtd);
        add(top, BorderLayout.NORTH);

        btnAtualizar.addActionListener(e -> refresh());
        btnExecutar.addActionListener(e -> executarAgora());
    }

    private void initCenter() {
        table.setDefaultRenderer(Object.class, new StageCellRenderer());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    tableModel.openLog(row, col, controller, AgendamentosView.this);
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadJobs() {
        try {
            List<Job> jobs = controller.listJobs();
            for (Job j : jobs) {
                comboJobs.addItem(j);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar jobs: " + e.getMessage());
        }
    }

    private void executarAgora() {
        Job job = (Job) comboJobs.getSelectedItem();
        if (job == null) return;
        try {
            controller.executarAgora(job.getId());
            refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao executar: " + e.getMessage());
        }
    }

    private void refresh() {
        Job job = (Job) comboJobs.getSelectedItem();
        if (job == null) return;
        try {
            int qtd = Integer.parseInt(txtQtd.getText());
            List<JobRun> runs = controller.listRuns(job.getId(), qtd, 0);
            List<JobStep> steps = controller.listSteps(job.getId());
            List<Map<Long, StepRun>> maps = new ArrayList<>();
            for (JobRun r : runs) {
                maps.add(controller.mapStepRuns(r.getId()));
            }
            tableModel.setData(runs, steps, maps);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage());
        }
    }

    /** Table model for stage view. */
    private static class StageTableModel extends AbstractTableModel {
        private final List<JobRun> runs = new ArrayList<>();
        private final List<JobStep> steps = new ArrayList<>();
        private final List<Map<Long, StepRun>> mapStepRuns = new ArrayList<>();

        public void setData(List<JobRun> runs, List<JobStep> steps, List<Map<Long, StepRun>> maps) {
            this.runs.clear();
            this.steps.clear();
            this.mapStepRuns.clear();
            this.runs.addAll(runs);
            this.steps.addAll(steps);
            this.mapStepRuns.addAll(maps);
            fireTableStructureChanged();
        }

        public void openLog(int row, int col, AgendamentosController controller, Component parent) {
            if (row < 0 || col <= 0) return;
            StepRun sr = mapStepRuns.get(row).get(steps.get(col - 1).getId());
            if (sr == null) return;
            try {
                String text = controller.abrirLog(sr);
                LogViewerDialog.show(parent, text);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, e.getMessage());
            }
        }

        @Override
        public int getRowCount() { return runs.size(); }

        @Override
        public int getColumnCount() { return steps.size() + 1; }

        @Override
        public String getColumnName(int column) {
            return column == 0 ? "Run" : steps.get(column - 1).getNome();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            JobRun run = runs.get(rowIndex);
            if (columnIndex == 0) {
                return "#" + run.getId() + " " + DateTimeFormatter.ISO_LOCAL_TIME
                        .format(run.getIniciouEm() != null ? run.getIniciouEm() : run.getFilaEm());
            }
            StepRun sr = mapStepRuns.get(rowIndex).get(steps.get(columnIndex - 1).getId());
            return sr != null ? sr.getStatus() : RunStatus.QUEUED;
        }
    }

    /** Cell renderer coloring background based on status. */
    private static class StageCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0) {
                c.setBackground(Color.WHITE);
                return c;
            }
            RunStatus status = (RunStatus) value;
            Color bg;
            switch (status) {
                case SUCCESS: bg = Color.GREEN; break;
                case FAILED:
                case ABORTED: bg = Color.RED; break;
                case RUNNING: bg = Color.YELLOW; break;
                default: bg = Color.LIGHT_GRAY; break;
            }
            c.setBackground(bg);
            return c;
        }
    }
}
