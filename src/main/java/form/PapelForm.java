package form;

import component.Card;
import controller.PapelController;
import dao.impl.PapelDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.ModelCard;
import model.Papel;
import swing.Button;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;

/**
 * Formulário para gerenciamento de {@link Papel}.
 */
public class PapelForm extends JPanel {

    private static final int PAGE_SIZE = 6;

    private final PapelController controller;
    private Card cardTotal;
    private Card cardTipos;
    private Card cardVencimentos;
    private Table table;
    private JTextField txtBusca;
    private JComboBox<String> cbTipo;
    private Button btnNovo;
    private List<Papel> papeis;
    private List<Papel> filtrados;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private String filtroTipo;
    private javax.swing.Icon iconPapel;
    private javax.swing.Icon iconTipo;
    private javax.swing.Icon iconVencimento;

    public PapelForm() {
        controller = new PapelController(new PapelDaoNativeImpl());
        initComponents();
        carregarPapeis();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 18, 0));
        cards.setBackground(Color.WHITE);

        iconPapel = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.INSERT_DRIVE_FILE, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(63, 81, 181));
        cardTotal.setColorGradient(new Color(121, 134, 203));
        cardTotal.setData(new ModelCard("Papéis", 0, 0, iconPapel));
        cards.add(cardTotal);

        iconTipo = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LABEL, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTipos = new Card();
        cardTipos.setBackground(new Color(0, 150, 136));
        cardTipos.setColorGradient(new Color(77, 182, 172));
        cardTipos.setData(new ModelCard("Tipos", 0, 0, iconTipo));
        cards.add(cardTipos);

        iconVencimento = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EVENT, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardVencimentos = new Card();
        cardVencimentos.setBackground(new Color(255, 87, 34));
        cardVencimentos.setColorGradient(new Color(255, 167, 38));
        cardVencimentos.setData(new ModelCard("Venc. Próx. (30d)", 0, 0, iconVencimento));
        cards.add(cardVencimentos);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        filtro.add(new JLabel("Tipo:"));
        cbTipo = new JComboBox<>(new String[]{"Todos"});
        cbTipo.addActionListener(e -> {
            Object selected = cbTipo.getSelectedItem();
            if (selected != null && !"Todos".equals(selected.toString())) {
                filtroTipo = selected.toString();
            } else {
                filtroTipo = null;
            }
            aplicarFiltros();
        });
        filtro.add(cbTipo);

        filtro.add(new JLabel("Código:"));
        txtBusca = new JTextField(15);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });
        filtro.add(txtBusca);

        btnNovo = new Button();
        btnNovo.setBackground(new Color(63, 81, 181));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 20, Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30, 30));
        btnNovo.addActionListener(e -> adicionarPapel());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Código", "Tipo", "Vencimento", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
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

    private void carregarPapeis() {
        papeis = controller.listar();
        atualizarFiltroTipos();
        aplicarFiltros();
    }

    private void atualizarFiltroTipos() {
        if (papeis == null) {
            cbTipo.setModel(new DefaultComboBoxModel<>(new String[]{"Todos"}));
            filtroTipo = null;
            return;
        }
        Map<String, String> unique = papeis.stream()
                .map(Papel::getTipo)
                .filter(t -> t != null && !t.isEmpty())
                .collect(Collectors.toMap(t -> t.toLowerCase(), t -> t, (existing, replacement) -> existing, LinkedHashMap::new));
        List<String> values = new ArrayList<>();
        values.add("Todos");
        values.addAll(unique.values());
        cbTipo.setModel(new DefaultComboBoxModel<>(values.toArray(new String[0])));
        if (filtroTipo != null) {
            String match = unique.entrySet().stream()
                    .filter(e -> e.getKey().equals(filtroTipo.toLowerCase()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
            if (match != null) {
                cbTipo.setSelectedItem(match);
                filtroTipo = match;
                return;
            }
        }
        filtroTipo = null;
        cbTipo.setSelectedIndex(0);
    }

    private void aplicarFiltros() {
        if (papeis == null) return;
        String busca = txtBusca.getText();
        String filtroCodigo = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = papeis.stream()
                .filter(p -> filtroTipo == null || (p.getTipo() != null && p.getTipo().equalsIgnoreCase(filtroTipo)))
                .filter(p -> filtroCodigo == null || (p.getCodigo() != null && p.getCodigo().toLowerCase().contains(filtroCodigo)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Papel> lista) {
        int total = lista != null ? lista.size() : 0;
        int tipos = 0;
        long proximos = 0;
        if (lista != null) {
            Map<String, String> unique = lista.stream()
                    .map(Papel::getTipo)
                    .filter(t -> t != null && !t.isEmpty())
                    .collect(Collectors.toMap(t -> t.toLowerCase(), t -> t, (existing, replacement) -> existing));
            tipos = unique.size();
            LocalDate hoje = LocalDate.now();
            LocalDate limite = hoje.plusDays(30);
            proximos = lista.stream()
                    .map(Papel::getVencimento)
                    .filter(d -> d != null && !d.isBefore(hoje) && !d.isAfter(limite))
                    .count();
        }
        cardTotal.setData(new ModelCard("Papéis", total, 0, iconPapel));
        cardTipos.setData(new ModelCard("Tipos", tipos, 0, iconTipo));
        cardVencimentos.setData(new ModelCard("Venc. Próx. (30d)", (int) proximos, 0, iconVencimento));
    }

    private void atualizarTabela(List<Papel> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Papel> eventAction = new EventAction<Papel>() {
            @Override
            public void delete(Papel p) { excluirPapel(p); }
            @Override
            public void update(Papel p) { editarPapel(p); }
        };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Papel p : lista) {
            model.addRow(new Object[]{
                p.getCodigo(),
                p.getTipo(),
                p.getVencimento() != null ? p.getVencimento().format(formatter) : "",
                new ModelAction<>(p, eventAction)
            });
        }
        table.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5, 5, 5, 5));
                lbl.setBackground(new Color(0, 150, 136));
                lbl.setForeground(Color.WHITE);
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Papel> page = getCurrentPagePapeis();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Papel> getCurrentPagePapeis() {
        if (filtrados == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtrados.size());
        if (start > end) {
            return Collections.emptyList();
        }
        return filtrados.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtrados != null ? filtrados.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarPapel() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        PapelDialog dialog = new PapelDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getPapel());
                carregarPapeis();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarPapel(Papel p) {
        try {
            Papel completo = controller.buscarPorId(p.getIdPapel());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            PapelDialog dialog = new PapelDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getPapel());
                    carregarPapeis();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPapel(Papel p) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir papel?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(p.getIdPapel());
                carregarPapeis();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
