/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MyComp_OpenSaveFile.java
 *
 * Created on Apr 15, 2009, 12:05:37 AM
 */

package QuanLyFile;

import com.sun.org.apache.bcel.internal.generic.Select;
import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;

/**
 *
 * @author Administrator
 */
public class MyComp_OpenSaveFile extends javax.swing.JPanel {

    private Boolean bTimFileLuu = false;
    private JFileChooser fileChooser = new JFileChooser();
    /** Creates new form MyComp_OpenSaveFile */
    public MyComp_OpenSaveFile() {
        initComponents();
    }

    public MyComp_OpenSaveFile(boolean bMoTimFileLuu) {
        initComponents();
        bTimFileLuu = bMoTimFileLuu;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox_DuongDanFile = new javax.swing.JComboBox();
        jButton_MoDialog = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jComboBox_DuongDanFile.setEditable(true);
        jComboBox_DuongDanFile.setName("jComboBox_DuongDanFile"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(doan_totalcommander_002.DoAn_TatalCommande_002App.class).getContext().getResourceMap(MyComp_OpenSaveFile.class);
        jButton_MoDialog.setText(resourceMap.getString("jButton_MoDialog.text")); // NOI18N
        jButton_MoDialog.setName("jButton_MoDialog"); // NOI18N
        jButton_MoDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MoDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jComboBox_DuongDanFile, 0, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_MoDialog))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jComboBox_DuongDanFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton_MoDialog))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_MoDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MoDialogActionPerformed
        // TODO add your handling code here:
        int iLuaChonCuaNguoiDung;
        iLuaChonCuaNguoiDung = getBTimFileLuu() ? getFileChooser().showSaveDialog(null) : getFileChooser().showOpenDialog(null);
        if (iLuaChonCuaNguoiDung == JFileChooser.APPROVE_OPTION){
            String strDuongDanFile = getFileChooser().getSelectedFile().getAbsolutePath();
            getJComboBox_DuongDanFile().setSelectedItem(strDuongDanFile);
            if(getJComboBox_DuongDanFile().getSelectedIndex() == -1)
                getJComboBox_DuongDanFile().addItem(strDuongDanFile);
        }
        phatSinhSuKien_DongFileChooser(getFileChooser());
    }//GEN-LAST:event_jButton_MoDialogActionPerformed

//Các hàm sau phục vụ cho việc gởi sự kiện click chuột vào bảng ra ngoài (tham khảo từ nhiều nguồn trên mạng)
    //http://www.exampledepot.com/egs/java.util/CustEvent.html
    // Tạo một listener list
    protected javax.swing.event.EventListenerList listenerList =
            new javax.swing.event.EventListenerList();

    /**
     * Phát sinh sử kiện đóng file chooser
     * @param evt tham số cho sự kiện 
     */
    // This private class is used to fire MyEvents
    void phatSinhSuKien_DongFileChooser(JFileChooser evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == EventListener_HoanThanhCongViec.class) {
                ((EventListener_HoanThanhCongViec) listeners[i + 1]).Event_DongFileChooser_Occurred(evt);
            }
        }
    }

    /**
     * Đăng ký sự kiện cho classes
     * @param listener  Sự kiện cần đăng ký
     */
    public void themEventListener_HoanThanhCongViec(EventListener_HoanThanhCongViec listener) {
        listenerList.add(EventListener_HoanThanhCongViec.class, listener);
    }

    /**
     * Gỡ bỏ sự kiện khỏi classes
     * @param listener  Sự kiện cần gỡ bỏ
     */
    public void boEventListener_HoanThanhCongViec(EventListener_HoanThanhCongViec listener) {
        listenerList.remove(EventListener_HoanThanhCongViec.class, listener);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_MoDialog;
    private javax.swing.JComboBox jComboBox_DuongDanFile;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the bTimFileLuu
     */
    public Boolean getBTimFileLuu() {
        return bTimFileLuu;
    }

    /**
     * @param bTimFileLuu the bTimFileLuu to set
     */
    public void setBTimFileLuu(Boolean bTimFileLuu) {
        this.bTimFileLuu = bTimFileLuu;
    }

    /**
     * @return the jComboBox_DuongDanFile
     */
    public javax.swing.JComboBox getJComboBox_DuongDanFile() {
        return jComboBox_DuongDanFile;
    }

    /**
     * @param jComboBox_DuongDanFile the jComboBox_DuongDanFile to set
     */
    public void setJComboBox_DuongDanFile(javax.swing.JComboBox jComboBox_DuongDanFile) {
        this.jComboBox_DuongDanFile = jComboBox_DuongDanFile;
    }

    /**
     * @return the fileChooser
     */
    public JFileChooser getFileChooser() {
        return fileChooser;
    }

}
