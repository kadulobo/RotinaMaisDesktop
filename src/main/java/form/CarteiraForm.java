package form;

import controller.CarteiraController;
import dao.impl.CarteiraDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import model.Carteira;
import model.ModelCard;
import swing.Button;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.Table;
import component.Card;

public class CarteiraForm extends JPanel {

    private final CarteiraController controller;
    private Card cardCarteiras;
    private Card cardTipos;
    private Card cardUsuarios;
    private Table table;
    private JTextField txtBusca;
    private Button btnTipo1;
    private Button btnTipo2;
    private Button btnTipo3;
    private Button btnNovo;
    private String filtroTipo;
    private List<Carteira> carteiras;
    private List<Carteira> filtradas;
    private JButton btnPrevPage;
    private JButton btnNextPage;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private final String[] tiposBotoes = new String[3];
    private javax.swing.Icon iconCarteiras;
    private javax.swing.Icon iconTipos;
    private javax.swing.Icon iconUsuarios;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CarteiraForm() {
        controller = new CarteiraController(new CarteiraDaoNativeImpl());
        initComponents();
        carregarCarteiras();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconCarteiras = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_BALANCE_WALLET, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardCarteiras = new Card();
        cardCarteiras.setBackground(new Color(63,81,181));
        cardCarteiras.setColorGradient(new Color(92,107,192));
        cardCarteiras.setData(new ModelCard("Carteiras",0,0,iconCarteiras));
        cards.add(cardCarteiras);

        iconTipos = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LABEL, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardTipos = new Card();
        cardTipos.setBackground(new Color(0,150,136));
        cardTipos.setColorGradient(new Color(38,166,154));
        cardTipos.setData(new ModelCard("Tipos",0,0,iconTipos));
        cards.add(cardTipos);

        iconUsuarios = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.GROUP, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardUsuarios = new Card();
        cardUsuarios.setBackground(new Color(255,87,34));
        cardUsuarios.setColorGradient(new Color(255,112,67));
        cardUsuarios.setData(new ModelCard("Usuários",0,0,iconUsuarios));
        cards.add(cardUsuarios);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnTipo1 = criarBotaoFiltro(0);
        filtro.add(btnTipo1);

        btnTipo2 = criarBotaoFiltro(1);
        filtro.add(btnTipo2);

        btnTipo3 = criarBotaoFiltro(2);
        filtro.add(btnTipo3);

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
        btnNovo.addActionListener(e -> adicionarCarteira());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "Nome", "Tipo", "Data Início", "Usuário", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
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

    private Button criarBotaoFiltro(int index) {
        Button button = new Button();
        button.setPreferredSize(new Dimension(120,30));
        button.setBackground(new Color(0,150,136));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> aplicarFiltroTipo(index));
        button.setEnabled(false);
        button.setText("Tipo " + (index + 1));
        return button;
    }

    private void aplicarFiltroTipo(int index) {
        if (tiposBotoes[index] != null) {
            filtroTipo = tiposBotoes[index];
        } else {
            filtroTipo = null;
        }
        aplicarFiltros();
    }

    private void carregarCarteiras() {
        carteiras = controller.listar();
        atualizarTiposBotoes();
        filtroTipo = null;
        aplicarFiltros();
    }

    private void atualizarTiposBotoes() {
        for (int i = 0; i < tiposBotoes.length; i++) {
            tiposBotoes[i] = null;
        }
        if (carteiras == null || carteiras.isEmpty()) {
            atualizarTextoBotoes();
            return;
        }
        Set<String> tipos = carteiras.stream()
                .map(Carteira::getTipo)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        int idx = 0;
        for (String t : tipos) {
            if (idx >= tiposBotoes.length) break;
            tiposBotoes[idx++] = t;
        }
        atualizarTextoBotoes();
    }

    private void atualizarTextoBotoes() {
        Button[] botoes = {btnTipo1, btnTipo2, btnTipo3};
        for (int i = 0; i < botoes.length; i++) {
            String texto = tiposBotoes[i];
            Button b = botoes[i];
            if (texto != null) {
                b.setText(texto);
                b.setEnabled(true);
            } else {
                b.setText("Todos");
                b.setEnabled(true);
            }
        }
    }

    private void aplicarFiltros() {
        if (carteiras == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtradas = carteiras.stream()
                .filter(c -> filtroTipo == null || (c.getTipo() != null && c.getTipo().equalsIgnoreCase(filtroTipo))
                        || (filtroTipo != null && "Todos".equalsIgnoreCase(filtroTipo)))
                .filter(c -> filtro == null ||
                        (c.getNome() != null && c.getNome().toLowerCase().contains(filtro)) ||
                        (c.getTipo() != null && c.getTipo().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarCards(filtradas);
        currentPage = 0;
        updatePage();
    }

    private void atualizarCards(List<Carteira> lista) {
        int total = lista != null ? lista.size() : 0;
        Set<String> tipos = new HashSet<>();
        Set<Integer> usuarios = new HashSet<>();
        for (Carteira c : lista) {
            if (c.getTipo() != null && !c.getTipo().isEmpty()) tipos.add(c.getTipo());
            if (c.getIdUsuario() != null) usuarios.add(c.getIdUsuario());
        }
        cardCarteiras.setData(new ModelCard("Carteiras", total, 0, iconCarteiras));
        cardTipos.setData(new ModelCard("Tipos", tipos.size(), 0, iconTipos));
        cardUsuarios.setData(new ModelCard("Usuários", usuarios.size(), 0, iconUsuarios));
    }

    private void atualizarTabela(List<Carteira> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Carteira> eventAction = new EventAction<Carteira>() {
            @Override
            public void delete(Carteira c) { excluirCarteira(c); }
            @Override
            public void update(Carteira c) { editarCarteira(c); }
        };
        for (Carteira c : lista) {
            model.addRow(new Object[]{
                c.getNome(),
                c.getTipo(),
                c.getDataInicio() != null ? formatter.format(c.getDataInicio()) : "",
                c.getIdUsuario(),
                new ModelAction<>(c, eventAction)
            });
        }
        int tipoCol = 1;
        table.getColumnModel().getColumn(tipoCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                lbl.setBackground(new Color(0,150,136));
                lbl.setForeground(Color.WHITE);
                return lbl;
            }
        });
    }

    private void updatePage() {
        List<Carteira> page = getCurrentPageCarteiras();
        atualizarTabela(page);
        updatePaginationButtons();
    }

    private List<Carteira> getCurrentPageCarteiras() {
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

    private void adicionarCarteira() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        CarteiraDialog dialog = new CarteiraDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getCarteira());
                carregarCarteiras();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCarteira(Carteira carteira) {
        try {
            Carteira completa = controller.buscarPorId(carteira.getIdCarteira());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            CarteiraDialog dialog = new CarteiraDialog(frame, completa);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                controller.atualizar(dialog.getCarteira());
                carregarCarteiras();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCarteira(Carteira carteira) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir carteira?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(carteira.getIdCarteira());
                carregarCarteiras();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
