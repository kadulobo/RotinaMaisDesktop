package form;

import component.Card;
import controller.MonitoramentoController;
import dao.impl.MonitoramentoDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.ModelCard;
import model.Monitoramento;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

/**
 * Formulário para gerenciamento de Monitoramentos.
 */
public class MonitoramentoForm extends JPanel {

    private final MonitoramentoController controller;
    private Card cardTotal;
    private Card cardAtivos;
    private Card cardAlertas;
    private Table table;
    private JTextField txtBusca;
    private Button btnAtivo;
    private Button btnPausado;
    private Button btnAlerta;
    private Button btnNovo;
    private Integer filtroStatus;
    private List<Monitoramento> monitoramentos;
    private List<Monitoramento> filtrados;
    private javax.swing.Icon iconTotal;
    private javax.swing.Icon iconAtivo;
    private javax.swing.Icon iconAlerta;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public MonitoramentoForm() {
        controller = new MonitoramentoController(new MonitoramentoDaoNativeImpl());
        initComponents();
        carregarMonitoramentos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 18, 0));
        cards.setBackground(Color.WHITE);

        iconTotal = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ASSESSMENT, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(63, 81, 181));
        cardTotal.setColorGradient(new Color(92, 107, 192));
        cardTotal.setData(new ModelCard("Monitoramentos", 0, 0, iconTotal));
        cards.add(cardTotal);

        iconAtivo = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VISIBILITY, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardAtivos = new Card();
        cardAtivos.setBackground(new Color(76, 175, 80));
        cardAtivos.setColorGradient(new Color(129, 199, 132));
        cardAtivos.setData(new ModelCard("Ativos", 0, 0, iconAtivo));
        cards.add(cardAtivos);

        iconAlerta = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.WARNING, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardAlertas = new Card();
        cardAlertas.setBackground(new Color(255, 152, 0));
        cardAlertas.setColorGradient(new Color(255, 203, 107));
        cardAlertas.setData(new ModelCard("Alertas", 0, 0, iconAlerta));
        cards.add(cardAlertas);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnAtivo = new Button();
        btnAtivo.setText("Ativos");
        btnAtivo.setBackground(new Color(76, 175, 80));
        btnAtivo.setForeground(Color.WHITE);
        btnAtivo.addActionListener(e -> {
            filtroStatus = 1;
            aplicarFiltros();
        });
        filtro.add(btnAtivo);

        btnPausado = new Button();
        btnPausado.setText("Pausados");
        btnPausado.setBackground(new Color(33, 150, 243));
        btnPausado.setForeground(Color.WHITE);
        btnPausado.addActionListener(e -> {
            filtroStatus = 2;
            aplicarFiltros();
        });
        filtro.add(btnPausado);

        btnAlerta = new Button();
        btnAlerta.setText("Alertas");
        btnAlerta.setBackground(new Color(255, 152, 0));
        btnAlerta.setForeground(Color.BLACK);
        btnAlerta.addActionListener(e -> {
            filtroStatus = 3;
            aplicarFiltros();
        });
        filtro.add(btnAlerta);

        Button btnTodos = new Button();
        btnTodos.setText("Todos");
        btnTodos.addActionListener(e -> {
            filtroStatus = null;
            aplicarFiltros();
        });
        filtro.add(btnTodos);

        txtBusca = new JTextField(15);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltros();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltros();
            }
        });
        filtro.add(new JLabel("Pesquisar:"));
        filtro.add(txtBusca);

        btnNovo = new Button();
        btnNovo.setBackground(new Color(63, 81, 181));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 20, Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30, 30));
        btnNovo.addActionListener(e -> adicionarMonitoramento());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Nome", "Status", "Período", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        });
        table.setRowHeight(40);
        JScrollPane scroll = new JScrollPane(table);
        table.fixTable(scroll);
        table.setRowSelectionAllowed(false);
        add(scroll, BorderLayout.CENTER);

        btnPrevPage = new JButton("Anterior");
        btnNextPage = new JButton("Próximo");
        btnPrevPage.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });
        btnNextPage.addActionListener(e -> {
            if ((currentPage + 1) * PAGE_SIZE < (filtrados != null ? filtrados.size() : 0)) {
                currentPage++;
                updatePage();
            }
        });
        JPanel pagination = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pagination.setBackground(Color.WHITE);
        pagination.add(btnPrevPage);
        pagination.add(btnNextPage);
        add(pagination, BorderLayout.SOUTH);
    }

    private void carregarMonitoramentos() {
        monitoramentos = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (monitoramentos == null) {
            return;
        }
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = monitoramentos.stream()
                .filter(m -> filtroStatus == null || (m.getStatus() != null && filtroStatus.equals(m.getStatus())))
                .filter(m -> filtro == null || matchesBusca(m, filtro))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private boolean matchesBusca(Monitoramento m, String filtro) {
        return (m.getNome() != null && m.getNome().toLowerCase().contains(filtro))
                || (m.getDescricao() != null && m.getDescricao().toLowerCase().contains(filtro));
    }

    private void atualizarCards(List<Monitoramento> lista) {
        long ativos = lista.stream().filter(m -> m.getStatus() != null && m.getStatus() == 1).count();
        long alertas = lista.stream().filter(m -> m.getStatus() != null && m.getStatus() == 3).count();
        cardTotal.setData(new ModelCard("Monitoramentos", lista.size(), 0, iconTotal));
        cardAtivos.setData(new ModelCard("Ativos", (int) ativos, 0, iconAtivo));
        cardAlertas.setData(new ModelCard("Alertas", (int) alertas, 0, iconAlerta));
    }

    private void atualizarTabela(List<Monitoramento> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Monitoramento> eventAction = new EventAction<Monitoramento>() {
            @Override
            public void delete(Monitoramento m) {
                excluirMonitoramento(m);
            }

            @Override
            public void update(Monitoramento m) {
                editarMonitoramento(m);
            }
        };
        for (Monitoramento m : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(m.getFoto());
            if (icon == null) {
                icon = new ImageIcon(getClass().getResource("/icon/profile.jpg"));
            }
            model.addRow(new Object[]{
                icon,
                m.getNome(),
                statusTexto(m.getStatus()),
                periodoTexto(m.getIdPeriodo()),
                new ModelAction<>(m, eventAction)
            });
        }
        table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                ImageAvatar avatar = new ImageAvatar();
                avatar.setPreferredSize(new Dimension(40, 40));
                if (value instanceof ImageIcon) {
                    avatar.setIcon((ImageIcon) value);
                }
                return avatar;
            }
        });
        int statusCol = table.getColumnModel().getColumnCount() - 2;
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorderPainted(false);
                lbl.setFocusPainted(false);
                lbl.setContentAreaFilled(true);
                lbl.setPreferredSize(new Dimension(90, 28));
                if ("Ativo".equals(value)) {
                    lbl.setBackground(new Color(76, 175, 80));
                    lbl.setForeground(Color.WHITE);
                } else if ("Pausado".equals(value)) {
                    lbl.setBackground(new Color(33, 150, 243));
                    lbl.setForeground(Color.WHITE);
                } else if ("Alerta".equals(value)) {
                    lbl.setBackground(new Color(255, 152, 0));
                    lbl.setForeground(Color.BLACK);
                } else {
                    lbl.setBackground(new Color(189, 189, 189));
                    lbl.setForeground(Color.BLACK);
                }
                return lbl;
            }
        });
    }

    private String periodoTexto(Integer idPeriodo) {
        if (idPeriodo == null) {
            return "";
        }
        return "Período #" + idPeriodo;
    }

    private void updatePage() {
        List<Monitoramento> page = getCurrentPage();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Monitoramento> getCurrentPage() {
        if (filtrados == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtrados.size());
        return filtrados.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtrados != null ? filtrados.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarMonitoramento() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        MonitoramentoDialog dialog = new MonitoramentoDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getMonitoramento());
                carregarMonitoramentos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarMonitoramento(Monitoramento m) {
        try {
            Monitoramento completo = controller.buscarComFotoPorId(m.getIdMonitoramento());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            MonitoramentoDialog dialog = new MonitoramentoDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getMonitoramento());
                    carregarMonitoramentos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirMonitoramento(Monitoramento m) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir monitoramento?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(m.getIdMonitoramento());
                carregarMonitoramentos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String statusTexto(Integer status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case 1:
                return "Ativo";
            case 2:
                return "Pausado";
            case 3:
                return "Alerta";
            default:
                return status.toString();
        }
    }
}
