package form;

import controller.AgendamentosController;
import model.*;
import agendamento.util.DurationUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

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
        table.setRowHeight(40);
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
                return DurationUtil.format(run.getDurationMs());
            }
            StepRun sr = mapStepRuns.get(rowIndex).get(steps.get(columnIndex - 1).getId());
            return sr;
        }
    }

    /** Cell renderer coloring background and showing duration. */
    private static class StageCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            if (column == 0) {
                label.setBackground(Color.WHITE);
                label.setText(value != null ? value.toString() : "");
                return label;
            }
            StepRun sr = (StepRun) value;
            RunStatus status = sr != null ? sr.getStatus() : RunStatus.QUEUED;
            long ms = 0;
            if (sr != null) {
                ms = sr.getDurationMs();
                if (status == RunStatus.RUNNING && sr.getIniciouEm() != null) {
                    ms = Duration.between(sr.getIniciouEm(), Instant.now()).toMillis();
                }
            }
            String text = sr != null ? DurationUtil.format(ms) : "";
            Color bg;
            switch (status) {
                case FAILED:
                case ABORTED:
                    bg = Color.RED; break;
                case RUNNING:
                case SUCCESS:
                    bg = Color.GREEN; break;
                default:
                    bg = Color.LIGHT_GRAY; break;
            }
            label.setBackground(bg);
            label.setText(text);
            return label;
        }
    }
}
