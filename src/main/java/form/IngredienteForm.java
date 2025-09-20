package form;

import component.Card;
import controller.IngredienteController;
import dao.impl.IngredienteDaoNativeImpl;
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
import model.Ingrediente;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import util.ImageUtils;

/**
 * Formulário para gerenciamento de Ingredientes.
 */
public class IngredienteForm extends JPanel {

    private final IngredienteController controller;
    private Card cardTotal;
    private Card cardComFoto;
    private Card cardSemFoto;
    private Table table;
    private JTextField txtBusca;
    private Button btnTodos;
    private Button btnComFoto;
    private Button btnSemFoto;
    private Button btnNovo;
    private Integer filtroFoto; // 1 = com foto, 2 = sem foto
    private List<Ingrediente> ingredientes;
    private List<Ingrediente> filtrados;
    private javax.swing.Icon iconTotal;
    private javax.swing.Icon iconComFoto;
    private javax.swing.Icon iconSemFoto;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public IngredienteForm() {
        controller = new IngredienteController(new IngredienteDaoNativeImpl());
        initComponents();
        carregarIngredientes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 18, 0));
        cards.setBackground(Color.WHITE);

        iconTotal = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LOCAL_DINING, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(63, 81, 181));
        cardTotal.setColorGradient(new Color(92, 107, 192));
        cardTotal.setData(new ModelCard("Ingredientes", 0, 0, iconTotal));
        cards.add(cardTotal);

        iconComFoto = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PHOTO_CAMERA, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardComFoto = new Card();
        cardComFoto.setBackground(new Color(76, 175, 80));
        cardComFoto.setColorGradient(new Color(129, 199, 132));
        cardComFoto.setData(new ModelCard("Com foto", 0, 0, iconComFoto));
        cards.add(cardComFoto);

        iconSemFoto = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.IMAGE_NOT_SUPPORTED, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardSemFoto = new Card();
        cardSemFoto.setBackground(new Color(255, 152, 0));
        cardSemFoto.setColorGradient(new Color(255, 203, 107));
        cardSemFoto.setData(new ModelCard("Sem foto", 0, 0, iconSemFoto));
        cards.add(cardSemFoto);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnTodos = new Button();
        btnTodos.setText("Todos");
        btnTodos.setBackground(new Color(63, 81, 181));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.addActionListener(e -> {
            filtroFoto = null;
            aplicarFiltros();
        });
        filtro.add(btnTodos);

        btnComFoto = new Button();
        btnComFoto.setText("Com foto");
        btnComFoto.setBackground(new Color(76, 175, 80));
        btnComFoto.setForeground(Color.WHITE);
        btnComFoto.addActionListener(e -> {
            filtroFoto = 1;
            aplicarFiltros();
        });
        filtro.add(btnComFoto);

        btnSemFoto = new Button();
        btnSemFoto.setText("Sem foto");
        btnSemFoto.setBackground(new Color(255, 152, 0));
        btnSemFoto.setForeground(Color.BLACK);
        btnSemFoto.addActionListener(e -> {
            filtroFoto = 2;
            aplicarFiltros();
        });
        filtro.add(btnSemFoto);

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
        btnNovo.addActionListener(e -> adicionarIngrediente());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Foto", "Nome", "Descrição", "Ações"
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

    private void carregarIngredientes() {
        ingredientes = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (ingredientes == null) {
            return;
        }
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = ingredientes.stream()
                .filter(i -> filtroFoto == null || filtraPorFoto(i))
                .filter(i -> filtro == null || matchesBusca(i, filtro))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private boolean filtraPorFoto(Ingrediente i) {
        if (filtroFoto == null) {
            return true;
        }
        boolean hasFoto = i.getFoto() != null && i.getFoto().length > 0;
        if (filtroFoto == 1) {
            return hasFoto;
        }
        if (filtroFoto == 2) {
            return !hasFoto;
        }
        return true;
    }

    private boolean matchesBusca(Ingrediente i, String filtro) {
        return (i.getNome() != null && i.getNome().toLowerCase().contains(filtro))
                || (i.getDescricao() != null && i.getDescricao().toLowerCase().contains(filtro));
    }

    private void atualizarCards(List<Ingrediente> lista) {
        long comFoto = lista.stream().filter(i -> i.getFoto() != null && i.getFoto().length > 0).count();
        long semFoto = lista.stream().filter(i -> i.getFoto() == null || i.getFoto().length == 0).count();
        cardTotal.setData(new ModelCard("Ingredientes", lista.size(), 0, iconTotal));
        cardComFoto.setData(new ModelCard("Com foto", (int) comFoto, 0, iconComFoto));
        cardSemFoto.setData(new ModelCard("Sem foto", (int) semFoto, 0, iconSemFoto));
    }

    private void atualizarTabela(List<Ingrediente> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Ingrediente> eventAction = new EventAction<Ingrediente>() {
            @Override
            public void delete(Ingrediente i) {
                excluirIngrediente(i);
            }

            @Override
            public void update(Ingrediente i) {
                editarIngrediente(i);
            }
        };
        for (Ingrediente i : lista) {
            ImageIcon icon = ImageUtils.bytesToImageIcon(i.getFoto());
            if (icon == null) {
                icon = new ImageIcon(getClass().getResource("/icon/profile.jpg"));
            }
            model.addRow(new Object[]{
                icon,
                i.getNome(),
                i.getDescricao(),
                new ModelAction<>(i, eventAction)
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

    private void updatePage() {
        List<Ingrediente> page = getCurrentPage();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Ingrediente> getCurrentPage() {
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

    private void adicionarIngrediente() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        IngredienteDialog dialog = new IngredienteDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getIngrediente());
                carregarIngredientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarIngrediente(Ingrediente i) {
        try {
            Ingrediente completo = controller.buscarComFotoPorId(i.getIdIngrediente());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            IngredienteDialog dialog = new IngredienteDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getIngrediente());
                    carregarIngredientes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirIngrediente(Ingrediente i) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir ingrediente?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(i.getIdIngrediente());
                carregarIngredientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
