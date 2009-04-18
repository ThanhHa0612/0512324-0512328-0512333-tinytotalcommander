/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Dialog_Copy.java
 *
 * Created on Apr 16, 2009, 7:41:29 PM
 */

package QuanLyFile;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author Administrator
 */
public class Dialog_ReMove extends javax.swing.JDialog {

    BoQuanLyFile boQuanLyFile;
    Task task;
    /** Creates new form Dialog_Copy */
    public Dialog_ReMove(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jButton_Thoat.setText("Thoát");
        jButton_Delete.setText("Delete");
        jLabel_Delete.setText("Delete:");
        jLabel3.setText("Đang Delete:");
        boQuanLyFile = new BoQuanLyFile();
        boQuanLyFile.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equalsIgnoreCase("DangReMove")){
                    jLabel_DangMove.setText(evt.getNewValue().toString());
                }
            }
        });
    }


    /**
     * @param jTextField_Nguon the jTextField_Nguon to set
     */
    public void setJTextField_Nguon(javax.swing.JTextField jTextField_Nguon) {
        this.jTextField_Nguon = jTextField_Nguon;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_Delete = new javax.swing.JButton();
        jButton_Thoat = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel_Delete = new javax.swing.JLabel();
        jTextField_Nguon = new javax.swing.JTextField();
        jLabel_DangMove = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(doan_totalcommander_002.DoAn_TatalCommande_002App.class).getContext().getResourceMap(Dialog_ReMove.class);
        jButton_Delete.setText(resourceMap.getString("jButton_Move.text")); // NOI18N
        jButton_Delete.setName("jButton_Move"); // NOI18N
        jButton_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MoveActionPerformed(evt);
            }
        });

        jButton_Thoat.setText(resourceMap.getString("jButton_Thoat.text")); // NOI18N
        jButton_Thoat.setName("jButton_Thoat"); // NOI18N
        jButton_Thoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThoatActionPerformed(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel_Delete.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel_Delete.setName("jLabel1"); // NOI18N

        jTextField_Nguon.setName("jTextField_Nguon"); // NOI18N

        jLabel_DangMove.setText(resourceMap.getString("jLabel_DangMove.text")); // NOI18N
        jLabel_DangMove.setName("jLabel_DangMove"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel_Delete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jTextField_Nguon, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_DangMove, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton_Delete)
                        .addGap(4, 4, 4)
                        .addComponent(jButton_Thoat)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Nguon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_DangMove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Delete)
                    .addComponent(jButton_Thoat))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_MoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MoveActionPerformed
        // TODO add your handling code here:
        jButton_Delete.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        task.addPropertyChangeListener(boQuanLyFile.getPropertyChangeListeners()[0]);
        task.execute();
}//GEN-LAST:event_jButton_MoveActionPerformed

    private void jButton_ThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThoatActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "Bạn muốn thoát?"
                , "Xác nhận thoát", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            dispose();
}//GEN-LAST:event_jButton_ThoatActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog_Move dialog = new Dialog_Move(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton_Delete;
    private javax.swing.JButton jButton_Thoat;
    private javax.swing.JLabel jLabel_Delete;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel_DangMove;
    private javax.swing.JTextField jTextField_Nguon;
    // End of variables declaration//GEN-END:variables


    /**
     * @return the jTextField_Nguon
     */
    public javax.swing.JTextField getJTextField_Nguon() {
        return jTextField_Nguon;
    }    // End of variables declaration

    class Task extends SwingWorker<Void, Void> {
        public void deleteFile(){
            try {
                // TODO add your handling code here:
                String str_FileNguon = getJTextField_Nguon().getText();
                boQuanLyFile.removeDirectory(new File(str_FileNguon), false);
            } catch (IOException ex) {
                Logger.getLogger(Dialog_ReMove.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Delete file thất bại! Lỗi: " + ex.getMessage());
            }
        }
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            deleteFile();
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            jButton_Delete.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            //jLabel_DuongDanDangTim.setText("Số file tìm được: " + jTable_TimDuoc.getRowCount());
            //Thông báo thành công và xác nhận thoát
            jLabel_DangMove.setText("Đã hoàn thành!");
            /*if (JOptionPane.showConfirmDialog(null, "Delete file thành công! Bạn muốn thoát?"
                , "Xác nhận thoát", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)*/
                dispose();

        }
    }
}
