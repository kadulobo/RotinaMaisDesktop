package form;

import component.Card;
import controller.ExercicioController;
import dao.impl.ExercicioDaoNativeImpl;
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
import model.Exercicio;
import model.ModelCard;
import swing.Button;
import swing.ImageAvatar;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;

/**
 * Formulário para gerenciamento de Exercícios.
 */
public class ExercicioForm extends JPanel {

    private final ExercicioController controller;
    private Card cardTotal;
    private Card cardCargaMedia;
    private Card cardCargaMaxima;
    private Table table;
    private JTextField txtBusca;
    private Button btnLeve;
    private Button btnMedia;
    private Button btnMaxima;
    private Button btnNovo;
    private Integer filtroCarga;
    private List<Exercicio> exercicios;
    private List<Exercicio> filtrados;
    private javax.swing.Icon iconTotal;
    private javax.swing.Icon iconMedia;
    private javax.swing.Icon iconMax;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public ExercicioForm() {
        controller = new ExercicioController(new ExercicioDaoNativeImpl());
        initComponents();
        carregarExercicios();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 3, 18, 0));
        cards.setBackground(Color.WHITE);

        iconTotal = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FITNESS_CENTER, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardTotal = new Card();
        cardTotal.setBackground(new Color(33, 150, 243));
        cardTotal.setColorGradient(new Color(30, 136, 229));
        cardTotal.setData(new ModelCard("Exercícios", 0, 0, iconTotal));
        cards.add(cardTotal);

        iconMedia = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHOW_CHART, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardCargaMedia = new Card();
        cardCargaMedia.setBackground(new Color(102, 187, 106));
        cardCargaMedia.setColorGradient(new Color(129, 199, 132));
        cardCargaMedia.setData(new ModelCard("Carga média", 0, 0, iconMedia));
        cards.add(cardCargaMedia);

        iconMax = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FLASH_ON, 60, Color.WHITE,
                new Color(255, 255, 255, 15));
        cardCargaMaxima = new Card();
        cardCargaMaxima.setBackground(new Color(255, 152, 0));
        cardCargaMaxima.setColorGradient(new Color(255, 203, 107));
        cardCargaMaxima.setData(new ModelCard("Carga máxima", 0, 0, iconMax));
        cards.add(cardCargaMaxima);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnLeve = new Button();
        btnLeve.setText("Leve");
        btnLeve.setBackground(new Color(33, 150, 243));
        btnLeve.setForeground(Color.WHITE);
        btnLeve.addActionListener(e -> {
            filtroCarga = 1;
            aplicarFiltros();
        });
        filtro.add(btnLeve);

        btnMedia = new Button();
        btnMedia.setText("Média");
        btnMedia.setBackground(new Color(102, 187, 106));
        btnMedia.setForeground(Color.WHITE);
        btnMedia.addActionListener(e -> {
            filtroCarga = 2;
            aplicarFiltros();
        });
        filtro.add(btnMedia);

        btnMaxima = new Button();
        btnMaxima.setText("Máxima");
        btnMaxima.setBackground(new Color(255, 152, 0));
        btnMaxima.setForeground(Color.BLACK);
        btnMaxima.addActionListener(e -> {
            filtroCarga = 3;
            aplicarFiltros();
        });
        filtro.add(btnMaxima);

        Button btnLimpar = new Button();
        btnLimpar.setText("Todos");
        btnLimpar.addActionListener(e -> {
            filtroCarga = null;
            aplicarFiltros();
        });
        filtro.add(btnLimpar);

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
        btnNovo.setBackground(new Color(33, 150, 243));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 20, Color.WHITE));
        btnNovo.setPreferredSize(new Dimension(30, 30));
        btnNovo.addActionListener(e -> adicionarExercicio());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Ícone", "Nome", "Carga leve", "Carga média", "Carga máxima", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return ImageIcon.class;
                }
                return Object.class;
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

    private void carregarExercicios() {
        exercicios = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (exercicios == null) {
            return;
        }
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = exercicios.stream()
                .filter(e -> filtroCarga == null || filtraPorCarga(e))
                .filter(e -> filtro == null || (e.getNome() != null && e.getNome().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtrados);
        currentPage = 0;
        updatePage();
    }

    private boolean filtraPorCarga(Exercicio e) {
        if (filtroCarga == null) {
            return true;
        }
        switch (filtroCarga) {
            case 1:
                return e.getCargaLeve() != null && e.getCargaLeve() > 0;
            case 2:
                return e.getCargaMedia() != null && e.getCargaMedia() > 0;
            case 3:
                return e.getCargaMaxima() != null && e.getCargaMaxima() > 0;
            default:
                return true;
        }
    }

    private void atualizarCards(List<Exercicio> lista) {
        int total = lista.size();
        int media = lista.stream().mapToInt(e -> e.getCargaMedia() != null ? e.getCargaMedia() : 0).sum();
        int maxima = lista.stream().mapToInt(e -> e.getCargaMaxima() != null ? e.getCargaMaxima() : 0).sum();
        cardTotal.setData(new ModelCard("Exercícios", total, 0, iconTotal));
        cardCargaMedia.setData(new ModelCard("Carga média", media, 0, iconMedia));
        cardCargaMaxima.setData(new ModelCard("Carga máxima", maxima, 0, iconMax));
    }

    private void atualizarTabela(List<Exercicio> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Exercicio> eventAction = new EventAction<Exercicio>() {
            @Override
            public void delete(Exercicio e) {
                excluirExercicio(e);
            }

            @Override
            public void update(Exercicio e) {
                editarExercicio(e);
            }
        };
        ImageIcon avatarIcon = (ImageIcon) IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FITNESS_CENTER, 35, Color.WHITE,
                new Color(33, 150, 243, 80));
        for (Exercicio e : lista) {
            model.addRow(new Object[]{
                avatarIcon,
                e.getNome(),
                valorOuVazio(e.getCargaLeve()),
                valorOuVazio(e.getCargaMedia()),
                valorOuVazio(e.getCargaMaxima()),
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
    }

    private void updatePage() {
        List<Exercicio> page = getCurrentPage();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Exercicio> getCurrentPage() {
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

    private void adicionarExercicio() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        ExercicioDialog dialog = new ExercicioDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getExercicio());
                carregarExercicios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarExercicio(Exercicio e) {
        try {
            Exercicio completo = controller.buscarPorId(e.getIdExercicio());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            ExercicioDialog dialog = new ExercicioDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getExercicio());
                    carregarExercicios();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirExercicio(Exercicio e) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir exercício?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(e.getIdExercicio());
                carregarExercicios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Object valorOuVazio(Integer valor) {
        return valor == null ? "" : valor;
    }
}
