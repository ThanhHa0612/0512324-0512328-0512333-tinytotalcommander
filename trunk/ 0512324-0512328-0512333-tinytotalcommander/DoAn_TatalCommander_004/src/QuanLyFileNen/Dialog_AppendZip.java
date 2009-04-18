/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Dialog_AppendZip.java
 *
 * Created on Apr 19, 2009, 5:48:34 AM
 */

package QuanLyFileNen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class Dialog_AppendZip extends javax.swing.JDialog {

    /** Creates new form Dialog_AppendZip */
    public Dialog_AppendZip(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myComp_OpenSaveFile_To = new QuanLyFile.MyComp_OpenSaveFile();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField_Append = new javax.swing.JTextField();
        jButton_Thoat = new javax.swing.JButton();
        jButton_Append = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        myComp_OpenSaveFile_To.setName("myComp_OpenSaveFile_To"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(doan_totalcommander_002.DoAn_TatalCommande_002App.class).getContext().getResourceMap(Dialog_AppendZip.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextField_Append.setText(resourceMap.getString("jTextField_Append.text")); // NOI18N
        jTextField_Append.setName("jTextField_Append"); // NOI18N

        jButton_Thoat.setText(resourceMap.getString("jButton_Thoat.text")); // NOI18N
        jButton_Thoat.setName("jButton_Thoat"); // NOI18N
        jButton_Thoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThoatActionPerformed(evt);
            }
        });

        jButton_Append.setText(resourceMap.getString("jButton_Append.text")); // NOI18N
        jButton_Append.setName("jButton_Append"); // NOI18N
        jButton_Append.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AppendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_Append, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(myComp_OpenSaveFile_To, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton_Append)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Thoat)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_Append, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(myComp_OpenSaveFile_To, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Append)
                    .addComponent(jButton_Thoat))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThoatActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "Bạn muốn thoát?"
                , "Xác nhận thoát", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        dispose();
    }//GEN-LAST:event_jButton_ThoatActionPerformed

    private void jButton_AppendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AppendActionPerformed
        String filezip = myComp_OpenSaveFile_To.getJComboBox_DuongDanFile().getSelectedItem().toString();
            try {
                BoQuanLyFileZip.appendFileToFileZip(getJTextField_Append().getText(), filezip);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Dialog_AppendZip.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lổi: " + ex.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(Dialog_AppendZip.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lổi: " + ex.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(Dialog_AppendZip.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Lổi: " + ex.getMessage());
            }

    }//GEN-LAST:event_jButton_AppendActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog_AppendZip dialog = new Dialog_AppendZip(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Append;
    private javax.swing.JButton jButton_Thoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField_Append;
    private QuanLyFile.MyComp_OpenSaveFile myComp_OpenSaveFile_To;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the jTextField_Append
     */
    public javax.swing.JTextField getJTextField_Append() {
        return jTextField_Append;
    }

}
