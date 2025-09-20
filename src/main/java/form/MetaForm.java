package form;

import component.Card;
import controller.MetaController;
import dao.impl.MetaDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Meta;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

public class MetaForm extends JPanel {

    private final MetaController controller;
    private Card cardMetas;
    private Card cardStatus;
    private Card cardPeriodos;
    private Table table;
    private JTextField txtBusca;
    private Button btnPlanejada;
    private Button btnEmAndamento;
    private Button btnConcluida;
    private Button btnNovo;
    private Integer filtroStatus;
    private List<Meta> metas;
    private List<Meta> filtradas;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private javax.swing.Icon iconMetas;
    private javax.swing.Icon iconStatus;
    private javax.swing.Icon iconPeriodos;

    public MetaForm() {
        controller = new MetaController(new MetaDaoNativeImpl());
        initComponents();
        carregarMetas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconMetas = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FLAG, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardMetas = new Card();
        cardMetas.setBackground(new Color(63,81,181));
        cardMetas.setColorGradient(new Color(92,107,192));
        cardMetas.setData(new ModelCard("Metas",0,0,iconMetas));
        cards.add(cardMetas);

        iconStatus = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.TRENDING_UP, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardStatus = new Card();
        cardStatus.setBackground(new Color(33,150,243));
        cardStatus.setColorGradient(new Color(30,136,229));
        cardStatus.setData(new ModelCard("Status",0,0,iconStatus));
        cards.add(cardStatus);

        iconPeriodos = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.DATE_RANGE, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardPeriodos = new Card();
        cardPeriodos.setBackground(new Color(0,150,136));
        cardPeriodos.setColorGradient(new Color(38,166,154));
        cardPeriodos.setData(new ModelCard("Períodos",0,0,iconPeriodos));
        cards.add(cardPeriodos);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnPlanejada = new Button();
        btnPlanejada.setText("Planejada");
        btnPlanejada.setBackground(new Color(63,81,181));
        btnPlanejada.setForeground(Color.WHITE);
        btnPlanejada.addActionListener(e -> { filtroStatus = 1; aplicarFiltros(); });
        filtro.add(btnPlanejada);

        btnEmAndamento = new Button();
        btnEmAndamento.setText("Em andamento");
        btnEmAndamento.setBackground(new Color(255,193,7));
        btnEmAndamento.setForeground(Color.BLACK);
        btnEmAndamento.addActionListener(e -> { filtroStatus = 2; aplicarFiltros(); });
        filtro.add(btnEmAndamento);

        btnConcluida = new Button();
        btnConcluida.setText("Concluída");
        btnConcluida.setBackground(new Color(76,175,80));
        btnConcluida.setForeground(Color.WHITE);
        btnConcluida.addActionListener(e -> { filtroStatus = 3; aplicarFiltros(); });
        filtro.add(btnConcluida);

        txtBusca = new JTextField(15);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });
        filtro.add(new JLabel("Pesquisar:"));
        filtro.add(txtBusca);

        btnNovo = new Button();
        btnNovo.setBackground(new Color(63,81,181));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD,20,Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30,30));
        btnNovo.addActionListener(e -> adicionarMeta());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Ponto Mínimo", "Ponto Médio", "Ponto Máximo", "Status", "Período", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : Object.class;
            }
        });
        table.setRowHeight(50);
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
            if ((currentPage + 1) * PAGE_SIZE < (filtradas != null ? filtradas.size() : 0)) {
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

    private void carregarMetas() {
        metas = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (metas == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtradas = metas.stream()
                .filter(m -> filtroStatus == null || filtroStatus.equals(m.getStatus()))
                .filter(m -> filtro == null ||
                        (m.getPontoMinimo() != null && String.valueOf(m.getPontoMinimo()).contains(filtro)) ||
                        (m.getPontoMedio() != null && String.valueOf(m.getPontoMedio()).contains(filtro)) ||
                        (m.getPontoMaximo() != null && String.valueOf(m.getPontoMaximo()).contains(filtro)) ||
                        (m.getIdPeriodo() != null && String.valueOf(m.getIdPeriodo()).contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtradas);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Meta> lista) {
        int total = lista != null ? lista.size() : 0;
        Set<Integer> status = new HashSet<>();
        Set<Integer> periodos = new HashSet<>();
        for (Meta m : lista) {
            if (m.getStatus() != null) status.add(m.getStatus());
            if (m.getIdPeriodo() != null) periodos.add(m.getIdPeriodo());
        }
        cardMetas.setData(new ModelCard("Metas", total, 0, iconMetas));
        cardStatus.setData(new ModelCard("Status", status.size(), 0, iconStatus));
        cardPeriodos.setData(new ModelCard("Períodos", periodos.size(), 0, iconPeriodos));
    }

    private void atualizarTabela(List<Meta> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Meta> eventAction = new EventAction<Meta>() {
            @Override
            public void delete(Meta meta) { excluirMeta(meta); }
            @Override
            public void update(Meta meta) { editarMeta(meta); }
        };
        for (Meta m : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(m.getFoto());
            model.addRow(new Object[]{
                icon,
                m.getPontoMinimo(),
                m.getPontoMedio(),
                m.getPontoMaximo(),
                statusTexto(m.getStatus()),
                m.getIdPeriodo(),
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
        table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                if ("Planejada".equals(value)) {
                    lbl.setBackground(new Color(63,81,181));
                    lbl.setForeground(Color.WHITE);
                } else if ("Em andamento".equals(value)) {
                    lbl.setBackground(new Color(255,193,7));
                    lbl.setForeground(Color.BLACK);
                } else if ("Concluída".equals(value)) {
                    lbl.setBackground(new Color(76,175,80));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(new Color(158,158,158));
                    lbl.setForeground(Color.WHITE);
                }
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Meta> page = getCurrentPageMetas();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Meta> getCurrentPageMetas() {
        if (filtradas == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtradas.size());
        if (start > end) {
            return new ArrayList<>();
        }
        return filtradas.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtradas != null ? filtradas.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarMeta() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        MetaDialog dialog = new MetaDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getMeta());
                carregarMetas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarMeta(Meta meta) {
        try {
            Meta completa = controller.buscarComFotoPorId(meta.getIdMeta());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            MetaDialog dialog = new MetaDialog(frame, completa);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                controller.atualizar(dialog.getMeta());
                carregarMetas();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirMeta(Meta meta) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir meta?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(meta.getIdMeta());
                carregarMetas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String statusTexto(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "Planejada";
            case 2: return "Em andamento";
            case 3: return "Concluída";
            default: return String.valueOf(status);
        }
    }
}
