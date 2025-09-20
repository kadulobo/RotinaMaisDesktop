package form;

import component.Card;
import controller.RotinaController;
import dao.impl.RotinaDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
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
import model.Rotina;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;

/**
 * Formulário para gerenciamento de Rotinas.
 */
public class RotinaForm extends JPanel {

    private final RotinaController controller;
    private Card cardTotal;
    private Card cardAtivas;
    private Card cardConcluidas;
    private Table table;
    private JTextField txtBusca;
    private Button btnAtiva;
    private Button btnPausada;
    private Button btnConcluida;
    private Button btnNovo;
    private Integer filtroStatus;
    private List<Rotina> rotinas;
    private List<Rotina> filtradas;
    private javax.swing.Icon iconTotal;
    private javax.swing.Icon iconAtiva;
    private javax.swing.Icon iconConcluida;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public RotinaForm() {
        controller = new RotinaController(new RotinaDaoNativeImpl());
        initComponents();
        carregarRotinas();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 18, 0));
        cards.setBackground(Color.WHITE);

        iconTotal = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.VIEW_LIST, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(63, 81, 181));
        cardTotal.setColorGradient(new Color(92, 107, 192));
        cardTotal.setData(new ModelCard("Rotinas", 0, 0, iconTotal));
        cards.add(cardTotal);

        iconAtiva = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PLAY_ARROW, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardAtivas = new Card();
        cardAtivas.setBackground(new Color(76, 175, 80));
        cardAtivas.setColorGradient(new Color(129, 199, 132));
        cardAtivas.setData(new ModelCard("Ativas", 0, 0, iconAtiva));
        cards.add(cardAtivas);

        iconConcluida = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.CHECK_CIRCLE, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardConcluidas = new Card();
        cardConcluidas.setBackground(new Color(255, 152, 0));
        cardConcluidas.setColorGradient(new Color(255, 203, 107));
        cardConcluidas.setData(new ModelCard("Concluídas", 0, 0, iconConcluida));
        cards.add(cardConcluidas);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnAtiva = new Button();
        btnAtiva.setText("Ativas");
        btnAtiva.setBackground(new Color(76, 175, 80));
        btnAtiva.setForeground(Color.WHITE);
        btnAtiva.addActionListener(e -> {
            filtroStatus = 1;
            aplicarFiltros();
        });
        filtro.add(btnAtiva);

        btnPausada = new Button();
        btnPausada.setText("Pausadas");
        btnPausada.setBackground(new Color(33, 150, 243));
        btnPausada.setForeground(Color.WHITE);
        btnPausada.addActionListener(e -> {
            filtroStatus = 2;
            aplicarFiltros();
        });
        filtro.add(btnPausada);

        btnConcluida = new Button();
        btnConcluida.setText("Concluídas");
        btnConcluida.setBackground(new Color(255, 152, 0));
        btnConcluida.setForeground(Color.BLACK);
        btnConcluida.addActionListener(e -> {
            filtroStatus = 3;
            aplicarFiltros();
        });
        filtro.add(btnConcluida);

        Button btnTodos = new Button();
        btnTodos.setText("Todas");
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
        btnNovo.addActionListener(e -> adicionarRotina());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Ícone", "Nome", "Período", "Status", "Pontos", "Usuário", "Ações"
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

    private void carregarRotinas() {
        rotinas = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (rotinas == null) {
            return;
        }
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtradas = rotinas.stream()
                .filter(r -> filtroStatus == null || (r.getStatus() != null && filtroStatus.equals(r.getStatus())))
                .filter(r -> filtro == null || matchesBusca(r, filtro))
                .collect(Collectors.toList());
        atualizarCards(filtradas);
        currentPage = 0;
        updatePage();
    }

    private boolean matchesBusca(Rotina r, String filtro) {
        return (r.getNome() != null && r.getNome().toLowerCase().contains(filtro))
                || (r.getDescricao() != null && r.getDescricao().toLowerCase().contains(filtro));
    }

    private void atualizarCards(List<Rotina> lista) {
        long ativas = lista.stream().filter(r -> r.getStatus() != null && r.getStatus() == 1).count();
        long concluidas = lista.stream().filter(r -> r.getStatus() != null && r.getStatus() == 3).count();
        cardTotal.setData(new ModelCard("Rotinas", lista.size(), 0, iconTotal));
        cardAtivas.setData(new ModelCard("Ativas", (int) ativas, 0, iconAtiva));
        cardConcluidas.setData(new ModelCard("Concluídas", (int) concluidas, 0, iconConcluida));
    }

    private void atualizarTabela(List<Rotina> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Rotina> eventAction = new EventAction<Rotina>() {
            @Override
            public void delete(Rotina r) {
                excluirRotina(r);
            }

            @Override
            public void update(Rotina r) {
                editarRotina(r);
            }
        };
        ImageIcon avatarIcon = (ImageIcon) IconFontSwing.buildIcon(GoogleMaterialDesignIcons.EVENT_NOTE, 35, Color.WHITE,
                new Color(63, 81, 181, 80));
        for (Rotina r : lista) {
            model.addRow(new Object[]{
                avatarIcon,
                r.getNome(),
                periodoTexto(r),
                statusTexto(r.getStatus()),
                r.getPonto(),
                r.getIdUsuario(),
                new ModelAction<>(r, eventAction)
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
    }

    private String periodoTexto(Rotina r) {
        String inicio = r.getInicio() != null ? r.getInicio().format(formatter) : "";
        String fim = r.getFim() != null ? r.getFim().format(formatter) : "";
        if (inicio.isEmpty() && fim.isEmpty()) {
            return "";
        }
        return inicio + " - " + fim;
    }

    private void updatePage() {
        List<Rotina> page = getCurrentPage();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Rotina> getCurrentPage() {
        if (filtradas == null) {
            return Collections.emptyList();
        }
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtradas.size());
        return filtradas.subList(start, end);
    }

    private void updatePaginationButtons() {
        int size = filtradas != null ? filtradas.size() : 0;
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled((currentPage + 1) * PAGE_SIZE < size);
    }

    private void adicionarRotina() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        RotinaDialog dialog = new RotinaDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getRotina());
                carregarRotinas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarRotina(Rotina r) {
        try {
            Rotina completa = controller.buscarPorId(r.getIdRotina());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            RotinaDialog dialog = new RotinaDialog(frame, completa);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getRotina());
                    carregarRotinas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirRotina(Rotina r) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir rotina?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(r.getIdRotina());
                carregarRotinas();
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
                return "Ativa";
            case 2:
                return "Pausada";
            case 3:
                return "Concluída";
            default:
                return status.toString();
        }
    }
}
