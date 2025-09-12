package form;

import controller.EventoController;
import controller.CategoriaController;
import dao.impl.EventoDaoNativeImpl;
import dao.impl.CategoriaDaoNativeImpl;
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
    private Card cardAtivos;
    private Card cardDesativados;
    private Table table;
    private JTextField txtBusca;
    private Button btnAtivo;
    private Button btnDesativado;
    private Button btnNovo;
    private Integer filtroStatus;
    private List<Evento> eventos;
    private List<Evento> filtrados;
    private javax.swing.Icon iconEvento;
    private javax.swing.Icon iconAtivo;
    private javax.swing.Icon iconDesativado;
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
        cardEventos.setData(new ModelCard("Qtd. Eventos",0,0,iconEvento));
        cards.add(cardEventos);

        iconAtivo = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK_CIRCLE, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardAtivos = new Card();
        cardAtivos.setBackground(new Color(76,175,80));
        cardAtivos.setColorGradient(new Color(129,199,132));
        cardAtivos.setData(new ModelCard("Ativos",0,0,iconAtivo));
        cards.add(cardAtivos);

        iconDesativado = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CANCEL, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardDesativados = new Card();
        cardDesativados.setBackground(new Color(244,67,54));
        cardDesativados.setColorGradient(new Color(229,115,115));
        cardDesativados.setData(new ModelCard("Desativados",0,0,iconDesativado));
        cards.add(cardDesativados);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnAtivo = new Button();
        btnAtivo.setText("Ativo");
        btnAtivo.setBackground(new Color(76,175,80));
        btnAtivo.setForeground(Color.WHITE);
        btnAtivo.addActionListener(e -> {filtroStatus = 1; aplicarFiltros();});
        filtro.add(btnAtivo);

        btnDesativado = new Button();
        btnDesativado.setText("Desativado");
        btnDesativado.setBackground(new Color(244,67,54));
        btnDesativado.setForeground(Color.WHITE);
        btnDesativado.addActionListener(e -> {filtroStatus = 2; aplicarFiltros();});
        filtro.add(btnDesativado);

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
            "Foto", "Nome", "Descrição", "Status", "Categoria", "Ações"
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
        CategoriaController catController = new CategoriaController(new CategoriaDaoNativeImpl());
        for (Evento e : eventos) {
            if (e.getCategoria() != null && e.getCategoria().getIdCategoria() != null) {
                try {
                    e.setCategoria(catController.buscarPorId(e.getCategoria().getIdCategoria()));
                } catch (Exception ex) {
                    // ignore loading errors
                }
            }
        }
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (eventos == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = eventos.stream()
                .filter(e -> filtroStatus == null || (e.getStatus() != null && filtroStatus.equals(e.getStatus())))
                .filter(e -> filtro == null || (e.getNome() != null && e.getNome().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Evento> lista) {
        int total = lista.size();
        int ativos = 0;
        int desativados = 0;
        for (Evento e : lista) {
            if (e.getStatus() != null && e.getStatus() == 1) {
                ativos++;
            } else {
                desativados++;
            }
        }
        cardEventos.setData(new ModelCard("Qtd. Eventos", total, 0, iconEvento));
        cardAtivos.setData(new ModelCard("Ativos", ativos, 0, iconAtivo));
        cardDesativados.setData(new ModelCard("Desativados", desativados, 0, iconDesativado));
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
                statusTexto(e.getStatus()),
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
        int statusCol = table.getColumnModel().getColumnCount() - 3;
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                if ("Ativo".equals(value)) {
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

    private String statusTexto(Integer s) {
        return s != null && s == 1 ? "Ativo" : "Desativado";
    }
}
