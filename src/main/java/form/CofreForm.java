package form;

import controller.CofreController;
import dao.impl.CofreDaoNativeImpl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.HashSet;
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
import model.Cofre;
import model.ModelCard;
import swing.Button;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.Table;
import swing.table.EventAction;
import swing.table.ModelAction;
import component.Card;

/**
 * Formulário para gerenciamento do Cofre de senhas.
 */
public class CofreForm extends JPanel {

    private final CofreController controller;
    private Card cardLogins;
    private Card cardPlataformas;
    private Card cardSenhas;
    private Table table;
    private JTextField txtBusca;
    private Button btnPessoa;
    private Button btnTrabalho;
    private Button btnFinanceiro;
    private Button btnNovo;
    private Integer filtroTipo;
    private List<Cofre> cofres;
    private List<Cofre> filtrados;
    private javax.swing.Icon iconLogin;
    private javax.swing.Icon iconPlat;
    private javax.swing.Icon iconSenha;

    public CofreForm() {
        controller = new CofreController(new CofreDaoNativeImpl());
        initComponents();
        carregarCofres();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new java.awt.GridLayout(1,3,18,0));
        cards.setBackground(Color.WHITE);

        iconLogin = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_CIRCLE, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardLogins = new Card();
        cardLogins.setBackground(new Color(33,150,243));
        cardLogins.setColorGradient(new Color(33,203,243));
        cardLogins.setData(new ModelCard("Logins",0,0,iconLogin));
        cards.add(cardLogins);

        iconPlat = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LAPTOP, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardPlataformas = new Card();
        cardPlataformas.setBackground(new Color(76,175,80));
        cardPlataformas.setColorGradient(new Color(129,199,132));
        cardPlataformas.setData(new ModelCard("Plataformas",0,0,iconPlat));
        cards.add(cardPlataformas);

        iconSenha = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.LOCK, 60, Color.WHITE,
                new Color(255,255,255,15));
        cardSenhas = new Card();
        cardSenhas.setBackground(new Color(255,152,0));
        cardSenhas.setColorGradient(new Color(255,203,107));
        cardSenhas.setData(new ModelCard("Senhas",0,0,iconSenha));
        cards.add(cardSenhas);

        top.add(cards, BorderLayout.NORTH);

        JPanel filtroWrapper = new JPanel(new BorderLayout());
        filtroWrapper.setBackground(Color.WHITE);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.setBackground(Color.WHITE);

        btnPessoa = new Button();
        btnPessoa.setText("Pessoa");
        btnPessoa.setBackground(new Color(33,150,243));
        btnPessoa.setForeground(Color.WHITE);
        btnPessoa.addActionListener(e -> {filtroTipo = 1; aplicarFiltros();});
        filtro.add(btnPessoa);

        btnTrabalho = new Button();
        btnTrabalho.setText("Trabalho");
        btnTrabalho.setBackground(new Color(76,175,80));
        btnTrabalho.setForeground(Color.WHITE);
        btnTrabalho.addActionListener(e -> {filtroTipo = 2; aplicarFiltros();});
        filtro.add(btnTrabalho);

        btnFinanceiro = new Button();
        btnFinanceiro.setText("Financeiro");
        btnFinanceiro.setBackground(new Color(255,152,0));
        btnFinanceiro.setForeground(Color.BLACK);
        btnFinanceiro.addActionListener(e -> {filtroTipo = 3; aplicarFiltros();});
        filtro.add(btnFinanceiro);

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
        btnNovo.addActionListener(e -> adicionarCofre());
        filtro.add(btnNovo);

        filtroWrapper.add(filtro, BorderLayout.CENTER);
        top.add(filtroWrapper, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table = new Table();
        table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{
            "ID", "Plataforma", "Login", "Senha", "Tipo", "Ações"
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        });
        JScrollPane scroll = new JScrollPane(table);
        table.fixTable(scroll);
        table.setRowSelectionAllowed(false);
        add(scroll, BorderLayout.CENTER);
    }

    private void carregarCofres() {
        cofres = controller.listar();
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (cofres == null) return;
        String busca = txtBusca.getText();
        String filtro = (busca != null && !busca.isEmpty()) ? busca.toLowerCase() : null;
        filtrados = cofres.stream()
                .filter(c -> filtroTipo == null || (c.getTipo() != null && filtroTipo.equals(c.getTipo())))
                .filter(c -> filtro == null || (c.getPlataforma() != null && c.getPlataforma().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());
        atualizarTabela(filtrados);
        atualizarCards(filtrados);
    }

    private void atualizarCards(List<Cofre> lista) {
        Set<String> logins = new HashSet<>();
        Set<String> plataformas = new HashSet<>();
        Set<String> senhas = new HashSet<>();
        for (Cofre c : lista) {
            if (c.getLogin() != null) logins.add(c.getLogin());
            if (c.getPlataforma() != null) plataformas.add(c.getPlataforma());
            if (c.getSenha() != null) senhas.add(c.getSenha());
        }
        cardLogins.setData(new ModelCard("Logins", logins.size(), 0, iconLogin));
        cardPlataformas.setData(new ModelCard("Plataformas", plataformas.size(), 0, iconPlat));
        cardSenhas.setData(new ModelCard("Senhas", senhas.size(), 0, iconSenha));
    }

    private void atualizarTabela(List<Cofre> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        EventAction<Cofre> eventAction = new EventAction<Cofre>() {
            @Override
            public void delete(Cofre c) {
                excluirCofre(c);
            }
            @Override
            public void update(Cofre c) {
                editarCofre(c);
            }
        };
        for (Cofre c : lista) {
            model.addRow(new Object[]{
                c.getIdCofre(),
                c.getPlataforma(),
                c.getLogin(),
                mask(c.getSenha()),
                tipoTexto(c.getTipo()),
                new ModelAction<>(c, eventAction)
            });
        }
        int tipoCol = table.getColumnModel().getColumnCount() - 2;
        table.getColumnModel().getColumn(tipoCol).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Button lbl = new Button();
                lbl.setText(value == null ? "" : value.toString());
                lbl.setBorder(new EmptyBorder(5,5,5,5));
                if ("Pessoa".equals(value)) {
                    lbl.setBackground(new Color(33,150,243));
                    lbl.setForeground(Color.WHITE);
                } else if ("Trabalho".equals(value)) {
                    lbl.setBackground(new Color(76,175,80));
                    lbl.setForeground(Color.WHITE);
                } else if ("Financeiro".equals(value)) {
                    lbl.setBackground(new Color(255,152,0));
                    lbl.setForeground(Color.BLACK);
                }
                return lbl;
            }
        });
    }

    private void adicionarCofre() {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        CofreDialog dialog = new CofreDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                controller.criar(dialog.getCofre());
                carregarCofres();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCofre(Cofre c) {
        try {
            Cofre completo = controller.buscarPorId(c.getIdCofre());
            Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
            CofreDialog dialog = new CofreDialog(frame, completo);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                try {
                    controller.atualizar(dialog.getCofre());
                    carregarCofres();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCofre(Cofre c) {
        int opt = JOptionPane.showConfirmDialog(this, "Excluir cofre?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                controller.remover(c.getIdCofre());
                carregarCofres();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String mask(String s) {
        if (s == null) return "";
        int len = Math.min(6, s.length());
        return "*".repeat(len);
    }

    private String tipoTexto(Integer tipo) {
        if (tipo == null) return "";
        switch (tipo) {
            case 1: return "Pessoa";
            case 2: return "Trabalho";
            case 3: return "Financeiro";
            default: return tipo.toString();
        }
    }
}

