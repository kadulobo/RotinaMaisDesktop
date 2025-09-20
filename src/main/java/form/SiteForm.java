package form;

import component.Card;
import controller.SiteController;
import dao.impl.SiteDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import model.ModelCard;
import model.Site;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

public class SiteForm extends JPanel {

    private final SiteController controller;
    private Card cardSites;
    private Card cardAtivos;
    private Card cardInativos;
    private Table table;
    private JTextField txtBusca;
    private Button btnTodos;
    private Button btnAtivos;
    private Button btnInativos;
    private Button btnNovo;
    private Boolean filtroAtivo;
    private List<Site> sites;
    private List<Site> filtrados;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private javax.swing.Icon iconSites;
    private javax.swing.Icon iconAtivos;
    private javax.swing.Icon iconInativos;

    public SiteForm() {
        controller = new SiteController(new SiteDaoNativeImpl());
        initComponents();
        carregarSites();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconSites = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LANGUAGE, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardSites = new Card();
        cardSites.setBackground(new Color(63,81,181));
        cardSites.setColorGradient(new Color(92,107,192));
        cardSites.setData(new ModelCard("Sites",0,0,iconSites));
        cards.add(cardSites);

        iconAtivos = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK_CIRCLE, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardAtivos = new Card();
        cardAtivos.setBackground(new Color(76,175,80));
        cardAtivos.setColorGradient(new Color(129,199,132));
        cardAtivos.setData(new ModelCard("Ativos",0,0,iconAtivos));
        cards.add(cardAtivos);

        iconInativos = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.REPORT, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardInativos = new Card();
        cardInativos.setBackground(new Color(244,67,54));
        cardInativos.setColorGradient(new Color(239,83,80));
        cardInativos.setData(new ModelCard("Inativos",0,0,iconInativos));
        cards.add(cardInativos);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnTodos = new Button();
        btnTodos.setText("Todos");
        btnTodos.setBackground(new Color(63,81,181));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.addActionListener(e -> { filtroAtivo = null; aplicarFiltros(); });
        filtro.add(btnTodos);

        btnAtivos = new Button();
        btnAtivos.setText("Ativos");
        btnAtivos.setBackground(new Color(76,175,80));
        btnAtivos.setForeground(Color.WHITE);
        btnAtivos.addActionListener(e -> { filtroAtivo = Boolean.TRUE; aplicarFiltros(); });
        filtro.add(btnAtivos);

        btnInativos = new Button();
        btnInativos.setText("Inativos");
        btnInativos.setBackground(new Color(244,67,54));
        btnInativos.setForeground(Color.WHITE);
        btnInativos.addActionListener(e -> { filtroAtivo = Boolean.FALSE; aplicarFiltros(); });
        filtro.add(btnInativos);

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
        btnNovo.addActionListener(e -> adicionarSite());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Logo", "URL", "Ativo", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
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

    private void carregarSites() {
        sites = controller.listar();
        filtroAtivo = null;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (sites == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = sites.stream()
                .filter(s -> filtroAtivo == null || filtroAtivo.equals(s.getAtivo()))
                .filter(s -> filtro == null || (s.getUrl() != null && s.getUrl().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Site> lista) {
        int total = lista != null ? lista.size() : 0;
        long ativos = lista.stream().filter(s -> Boolean.TRUE.equals(s.getAtivo())).count();
        long inativos = total - ativos;
        cardSites.setData(new ModelCard("Sites", total, 0, iconSites));
        cardAtivos.setData(new ModelCard("Ativos", (int) ativos, 0, iconAtivos));
        cardInativos.setData(new ModelCard("Inativos", (int) inativos, 0, iconInativos));
    }

    private void atualizarTabela(List<Site> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Site> eventAction = new EventAction<Site>() {
            @Override
            public void delete(Site site) { excluirSite(site); }
            @Override
            public void update(Site site) { editarSite(site); }
        };
        for (Site s : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(s.getLogo());
            model.addRow(new Object[]{
                icon,
                s.getUrl(),
                Boolean.TRUE.equals(s.getAtivo()) ? "Sim" : "Não",
                new ModelAction<>(s, eventAction)
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
        table.getColumnModel().getColumn(2).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                if ("Sim".equals(value)) {
                    lbl.setBackground(new Color(76,175,80));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(new Color(244,67,54));
                    lbl.setForeground(Color.WHITE);
                }
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Site> page = getCurrentPageSites();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Site> getCurrentPageSites() {
        if (filtrados == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtrados.size());
        if (start > end) {
            return new ArrayList<>();
        }
        return filtrados.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtrados != null ? filtrados.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarSite() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        SiteDialog dialog = new SiteDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getSite());
                carregarSites();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarSite(Site site) {
        try {
            Site completo = controller.buscarPorId(site.getIdSite());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            SiteDialog dialog = new SiteDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                controller.atualizar(dialog.getSite());
                carregarSites();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirSite(Site site) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir site?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(site.getIdSite());
                carregarSites();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
