package form;

import controller.EventoController;
import dao.impl.EventoDaoNativeImpl;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Evento;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import component.Card;
import util.ImageUtils;

/**
 * Formulário para gerenciamento de Eventos.
 */
public class EventoForm extends JPanel {

    private final EventoController controller;
    private Card cardEventos;
    private Card cardVantagens;
    private Card cardSemVantagem;
    private Table table;
    private JTextField txtBusca;
    private Button btnVantagem;
    private Button btnSemVantagem;
    private Button btnNovo;
    private Boolean filtroVantagem;
    private List<Evento> eventos;
    private List<Evento> filtrados;
    private javax.swing.Icon iconEvento;
    private javax.swing.Icon iconVantagem;
    private javax.swing.Icon iconSemVantagem;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public EventoForm() {
        controller = new EventoController(new EventoDaoNativeImpl());
        initComponents();
        carregarEventos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconEvento = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EVENT, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardEventos = new Card();
        cardEventos.setBackground(new Color(33,150,243));
        cardEventos.setColorGradient(new Color(33,203,243));
        cardEventos.setData(new ModelCard("Eventos",0,0,iconEvento));
        cards.add(cardEventos);

        iconVantagem = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.THUMB_UP, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardVantagens = new Card();
        cardVantagens.setBackground(new Color(76,175,80));
        cardVantagens.setColorGradient(new Color(129,199,132));
        cardVantagens.setData(new ModelCard("Vantagens",0,0,iconVantagem));
        cards.add(cardVantagens);

        iconSemVantagem = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.THUMB_DOWN, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardSemVantagem = new Card();
        cardSemVantagem.setBackground(new Color(255,152,0));
        cardSemVantagem.setColorGradient(new Color(255,203,107));
        cardSemVantagem.setData(new ModelCard("Sem Vantagem",0,0,iconSemVantagem));
        cards.add(cardSemVantagem);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnVantagem = new Button();
        btnVantagem.setText("Vantagem");
        btnVantagem.setBackground(new Color(76,175,80));
        btnVantagem.setForeground(Color.WHITE);
        btnVantagem.addActionListener(e -> {filtroVantagem = Boolean.TRUE; aplicarFiltros();});
        filtro.add(btnVantagem);

        btnSemVantagem = new Button();
        btnSemVantagem.setText("Sem Vantagem");
        btnSemVantagem.setBackground(new Color(255,152,0));
        btnSemVantagem.setForeground(Color.BLACK);
        btnSemVantagem.addActionListener(e -> {filtroVantagem = Boolean.FALSE; aplicarFiltros();});
        filtro.add(btnSemVantagem);

        txtBusca = new JTextField(15);
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });
        filtro.add(new JLabel("Pesquisar:"));
        filtro.add(txtBusca);

        btnNovo = new Button();
        btnNovo.setBackground(new Color(33,150,243));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD,20,Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30,30));
        btnNovo.addActionListener(e -> adicionarEvento());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Nome", "Descrição", "Vantagem", "Categoria", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
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

    private void carregarEventos() {
        eventos = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (eventos == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = eventos.stream()
                .filter(e -> filtroVantagem == null || (e.getVantagem() != null && filtroVantagem.equals(e.getVantagem())))
                .filter(e -> filtro == null || (e.getNome() != null && e.getNome().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Evento> lista) {
        int total = lista.size();
        int vantagens = 0;
        int sem = 0;
        for (Evento e : lista) {
            if (Boolean.TRUE.equals(e.getVantagem())) {
                vantagens++;
            } else {
                sem++;
            }
        }
        cardEventos.setData(new ModelCard("Eventos", total, 0, iconEvento));
        cardVantagens.setData(new ModelCard("Vantagens", vantagens, 0, iconVantagem));
        cardSemVantagem.setData(new ModelCard("Sem Vantagem", sem, 0, iconSemVantagem));
    }

    private void atualizarTabela(List<Evento> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Evento> eventAction = new EventAction<Evento>() {
            @Override
            public void delete(Evento e) {
                excluirEvento(e);
            }
            @Override
            public void update(Evento e) {
                editarEvento(e);
            }
        };
        for (Evento e : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(e.getFoto());
            if (icon == null) {
                icon = new ImageIcon(getClass().getResource("/icon/profile.jpg"));
            }
            model.addRow(new Object[]{
                icon,
                e.getNome(),
                e.getDescricao(),
                vantagemTexto(e.getVantagem()),
                e.getCategoria() != null ? e.getCategoria().getNome() : "",
                new ModelAction<>(e, eventAction)
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
        int vantCol = table.getColumnModel().getColumnCount() - 3;
        table.getColumnModel().getColumn(vantCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                if ("Sim".equals(value)) {
                    lbl.setBackground(new Color(76,175,80));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(new Color(255,152,0));
                    lbl.setForeground(Color.BLACK);
                }
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Evento> page = getCurrentPageEventos();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Evento> getCurrentPageEventos() {
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

    private void adicionarEvento() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        EventoDialog dialog = new EventoDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getEvento());
                carregarEventos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarEvento(Evento e) {
        try {
            Evento completo = controller.buscarComFotoPorId(e.getIdEvento());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            EventoDialog dialog = new EventoDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getEvento());
                    carregarEventos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirEvento(Evento e) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir evento?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(e.getIdEvento());
                carregarEventos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String vantagemTexto(Boolean v) {
        return Boolean.TRUE.equals(v) ? "Sim" : "Não";
    }
}
