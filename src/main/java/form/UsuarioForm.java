package form;

import controller.UsuarioController;
import dao.impl.UsuarioDaoNativeImpl;
import java.awt.Color;
import java.awt.Frame;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import model.Usuario;
import swing.icon.GoogleMaterialDesignIcons;
import swing.icon.IconFontSwing;
import swing.table.EventAction;
import swing.table.ModelAction;
import swing.table.ModelProfile;

/**
 * Form that lists usuarios fetched from the database with action buttons for
 * edit and delete and a button to add new usuarios.
 */
public class UsuarioForm extends javax.swing.JPanel {

    private final UsuarioController controller;

    public UsuarioForm() {
        controller = new UsuarioController(new UsuarioDaoNativeImpl());
        initComponents();
        table1.fixTable(jScrollPane1);
        setOpaque(false);
        loadUsuarios();
    }

    private void loadUsuarios() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        List<Usuario> usuarios = controller.listar();
        EventAction<Usuario> eventAction = new EventAction<Usuario>() {
            @Override
            public void delete(Usuario u) {
                controller.remover(u.getIdUsuario());
                loadUsuarios();
            }

            @Override
            public void update(Usuario u) {
                Frame frame = (Frame) SwingUtilities.getWindowAncestor(UsuarioForm.this);
                UsuarioDialog dialog = new UsuarioDialog(frame, u);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    controller.atualizar(dialog.getUsuario());
                    loadUsuarios();
                }
            }
        };

        for (Usuario u : usuarios) {
            Icon icon = u.getFoto() != null ? new ImageIcon(u.getFoto())
                    : new ImageIcon(getClass().getResource("/icon/profile.jpg"));
            model.addRow(new Object[]{new ModelProfile(icon, u.getNome()), u.getEmail(), u.getCpf(),
                new ModelAction<>(u, eventAction)});
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new swing.table.Table();
        btnAdd = new swing.Button();

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(76, 76, 76));
        jLabel1.setText("Lista de Usuarios");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Usuário", "Email", "CPF", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setPreferredWidth(150);
        }

        btnAdd.setBackground(new java.awt.Color(75, 134, 253));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ADD, 18, Color.WHITE));
        btnAdd.setText("Adicionar");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        Frame frame = (Frame) SwingUtilities.getWindowAncestor(this);
        UsuarioDialog dialog = new UsuarioDialog(frame, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            controller.criar(dialog.getUsuario());
            loadUsuarios();
        }
    }

    // Variables declaration - do not modify
    private swing.Button btnAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private swing.table.Table table1;
    // End of variables declaration
}

