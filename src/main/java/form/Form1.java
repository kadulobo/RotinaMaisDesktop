package form;

import javax.swing.ImageIcon;
import model.ModelStudent;
import swing.table.EventAction;

/**
 * Form shown when clicking the "Buttons" menu. Displays a table using the same
 * layout as the home form but fills the entire panel.
 */
public class Form1 extends javax.swing.JPanel {

    public Form1() {
        initComponents();
        table1.fixTable(jScrollPane1);
        setOpaque(false);
        initData();
    }

    private void initData() {
        EventAction eventAction = new EventAction() {
            @Override
            public void delete(ModelStudent student) {
                System.out.println("Delete Student : " + student.getName());
            }

            @Override
            public void update(ModelStudent student) {
                System.out.println("Update Student : " + student.getName());
            }
        };

        table1.addRow(new ModelStudent(new ImageIcon(getClass().getResource("/icon/profile.jpg")),
                "Jonh", "Male", "Java", 300).toRowTable(eventAction));
        table1.addRow(new ModelStudent(new ImageIcon(getClass().getResource("/icon/profile1.jpg")),
                "Dara", "Male", "C++", 300).toRowTable(eventAction));
        table1.addRow(new ModelStudent(new ImageIcon(getClass().getResource("/icon/profile2.jpg")),
                "Bora", "Male", "C#", 300).toRowTable(eventAction));
        table1.addRow(new ModelStudent(new ImageIcon(getClass().getResource("/icon/profile2.jpg")),
                "Bora", "Male", "C#", 300).toRowTable(eventAction));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new swing.table.Table();

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(76, 76, 76));
        jLabel1.setText("Data Student");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Gender", "Course", "Fees", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setPreferredWidth(150);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private swing.table.Table table1;
    // End of variables declaration//GEN-END:variables
}

