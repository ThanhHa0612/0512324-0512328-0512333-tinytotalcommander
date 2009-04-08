/*
 * DoAn_TatalCommande_002View.java
 */

package doan_tatalcommande_002;

import com.sun.org.apache.xml.internal.utils.ObjectPool;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The application's main frame.
 */

public class DoAn_TatalCommande_002View extends FrameView {
    private BangDuyetThuMuc _bangTrai;
    private BangDuyetThuMuc _bangPhai;
    private jEnum_CacBang _enum_BangHienTai;
    private BangDuyetThuMuc _bangHienTai;
    int dem = 1;

    public DoAn_TatalCommande_002View(SingleFrameApplication app) {
        super(app);

        initComponents();

/*---------------------Khởi tạo các ScrollPane-------------------------------------------*/
        jScrollPane_PhanChinh_BangTrai.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("ToolTipText"))
                    jTabbedPane_PhanChinh_BangTrai.setTitleAt(0, jScrollPane_PhanChinh_BangTrai.getToolTipText());
            }
        });
        jScrollPane_PhanChinh_BangPhai.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("ToolTipText"))
                    jTabbedPane_PhanChinh_BangPhai.setTitleAt(0, jScrollPane_PhanChinh_BangPhai.getToolTipText());
            }
        });
/*---------------------------------------------------------------------------------------*/
        
/*---------------------Khởi tạo các bảng duyệt thư mục-----------------------------------*/
        _bangTrai = new BangDuyetThuMuc("C:", jScrollPane_PhanChinh_BangTrai);
        _bangPhai = new BangDuyetThuMuc("D:", jScrollPane_PhanChinh_BangPhai);
        _enum_BangHienTai = jEnum_CacBang.BangTrai;
        _bangHienTai = _bangTrai;
        _bangPhai.getTable().clearSelection();
        //System.setProperty("user.dir", _bangHienTai.getTenFile());

        _bangTrai.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String str_TenFileDuocChon) {
                jLabel1.setText("Ban chon cua so trai");
                _bangPhai.getTable().clearSelection();
                _enum_BangHienTai = jEnum_CacBang.BangTrai;
                _bangHienTai = _bangTrai;
                //System.setProperty("user.dir", _bangHienTai.getTenFile());
                //JOptionPane.showMessageDialog(null, System.getProperty("user.dir"));
            }
        });
        _bangPhai.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String str_TenFileDuocChon) {
                jLabel1.setText("Ban chon cua so phai");
                _bangTrai.getTable().clearSelection();
                _enum_BangHienTai = jEnum_CacBang.BangPhai;
                _bangHienTai = _bangPhai;
                //System.setProperty("user.dir", _bangHienTai.getTenFile());
            }
        });
/*---------------------------------------------------------------------------------------*/

/*---------Khởi tạo treeview và 2 combo box hiện thị các ổ đĩa----------------------------*/
        jComboBox_PhanChinh_BangPhai.removeAllItems();
        jComboBox_PhanChinh_BangTrai.removeAllItems();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("My Computer");
        for (File file : File.listRoots()){
            DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(file.getPath());
            jComboBox_PhanChinh_BangPhai.addItem(file.getPath());
            jComboBox_PhanChinh_BangTrai.addItem(file.getPath());
            root.add(child1);
        }
        jComboBox_PhanChinh_BangPhai.setSelectedItem(_bangTrai.getTenFile());
        jComboBox_PhanChinh_BangTrai.setSelectedItem(_bangPhai.getTenFile());
        jTree1 = new JTree(root);
        jScrollPane_PhanChinh_Tree.setViewportView(jTree1);
/*---------------------------------------------------------------------------------------*/

        //jTabbedPane1.addTab("C:", jTabbedPane1.getTabComponentAt(0));
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DoAn_TatalCommande_002App.getApplication().getMainFrame();
            aboutBox = new DoAn_TatalCommande_002AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DoAn_TatalCommande_002App.getApplication().show(aboutBox);
    }
    /**
 * Cập nhật lại các bảng trái, phải sau khi có hành động như tạo, xóa thư mục/tập tin
 */
    private void capNhatCacBang(File thuMucVuaCapNhat) {
        if (_bangHienTai.getTenFile().equals(_bangPhai.getTenFile())) {
            _bangPhai.capNhatBangDuyetThuMuc(_bangPhai.getTenFile(), jScrollPane_PhanChinh_BangPhai);
        } 
        if(_bangHienTai.getTenFile().equals(_bangTrai.getTenFile())) {
            _bangTrai.capNhatBangDuyetThuMuc(_bangTrai.getTenFile(), jScrollPane_PhanChinh_BangTrai);
        }
        if(_bangTrai.getTenFile().equalsIgnoreCase(thuMucVuaCapNhat.getPath()))
            _bangTrai.capNhatBangDuyetThuMuc(_bangTrai.getTenFile(), jScrollPane_PhanChinh_BangTrai);
        if(_bangPhai.getTenFile().equalsIgnoreCase(thuMucVuaCapNhat.getPath()))
            _bangPhai.capNhatBangDuyetThuMuc(_bangPhai.getTenFile(), jScrollPane_PhanChinh_BangPhai);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel_PhanDau = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel_PhanChan = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane_PhanChinh_Tree = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox_PhanChinh_BangTrai = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane_PhanChinh_BangTrai = new javax.swing.JTabbedPane();
        jScrollPane_PhanChinh_BangTrai = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane_PhanChinh_BangPhai = new javax.swing.JTabbedPane();
        jScrollPane_PhanChinh_BangPhai = new javax.swing.JScrollPane();
        jTextField3 = new javax.swing.JTextField();
        jComboBox_PhanChinh_BangPhai = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu jMenu_File = new javax.swing.JMenu();
        jMenuItem_File_NewFolder = new javax.swing.JMenuItem();
        jMenuItem_File_Xoa = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu_Edit = new javax.swing.JMenu();
        jMenuItem_Edit_Back = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel_PhanDau.setName("jPanel_PhanDau"); // NOI18N
        jPanel_PhanDau.setPreferredSize(new java.awt.Dimension(932, 42));

        jToolBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 38));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(doan_tatalcommande_002.DoAn_TatalCommande_002App.class).getContext().getResourceMap(DoAn_TatalCommande_002View.class);
        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setAlignmentY(0.0F);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setPreferredSize(new java.awt.Dimension(35, 35));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setAlignmentY(0.0F);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton5);

        javax.swing.GroupLayout jPanel_PhanDauLayout = new javax.swing.GroupLayout(jPanel_PhanDau);
        jPanel_PhanDau.setLayout(jPanel_PhanDauLayout);
        jPanel_PhanDauLayout.setHorizontalGroup(
            jPanel_PhanDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
        );
        jPanel_PhanDauLayout.setVerticalGroup(
            jPanel_PhanDauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_PhanDauLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel_PhanDau, java.awt.BorderLayout.PAGE_START);

        jPanel_PhanChan.setName("jPanel_PhanChan"); // NOI18N
        jPanel_PhanChan.setPreferredSize(new java.awt.Dimension(900, 70));

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel4.setName("jPanel4"); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setName("jSeparator2"); // NOI18N

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setName("jSeparator3"); // NOI18N

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator6.setName("jSeparator6"); // NOI18N

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator7.setName("jSeparator7"); // NOI18N

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton2.setName("jButton2"); // NOI18N

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton3.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton3.setName("jButton3"); // NOI18N

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton4.setName("jButton4"); // NOI18N

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton6.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton6.setName("jButton6"); // NOI18N

        jButton7.setText(resourceMap.getString("jButton7.text")); // NOI18N
        jButton7.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton7.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton7.setName("jButton7"); // NOI18N

        jButton8.setText(resourceMap.getString("jButton8.text")); // NOI18N
        jButton8.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton8.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton8.setName("jButton8"); // NOI18N

        jButton9.setText(resourceMap.getString("jButton9.text")); // NOI18N
        jButton9.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton9.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton9.setName("jButton9"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(404, 404, 404)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(97, 97, 97)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(201, 201, 201)))
                        .addGap(2, 2, 2)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, 528, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel_PhanChanLayout = new javax.swing.GroupLayout(jPanel_PhanChan);
        jPanel_PhanChan.setLayout(jPanel_PhanChanLayout);
        jPanel_PhanChanLayout.setHorizontalGroup(
            jPanel_PhanChanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_PhanChanLayout.setVerticalGroup(
            jPanel_PhanChanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel_PhanChan, java.awt.BorderLayout.PAGE_END);

        jSplitPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jSplitPane1.setDividerLocation(600);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jSplitPane2.setDividerLocation(150);
        jSplitPane2.setLastDividerLocation(150);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jScrollPane_PhanChinh_Tree.setName("jScrollPane_PhanChinh_Tree"); // NOI18N

        jTree1.setName("jTree1"); // NOI18N
        jScrollPane_PhanChinh_Tree.setViewportView(jTree1);

        jSplitPane2.setLeftComponent(jScrollPane_PhanChinh_Tree);

        jPanel1.setName("jPanel1"); // NOI18N

        jTextField2.setEditable(false);
        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setEnabled(false);
        jTextField2.setName("jTextField2"); // NOI18N

        jComboBox_PhanChinh_BangTrai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_PhanChinh_BangTrai.setKeySelectionManager(null);
        jComboBox_PhanChinh_BangTrai.setName("jComboBox_PhanChinh_BangTrai"); // NOI18N
        jComboBox_PhanChinh_BangTrai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_PhanChinh_BangTraiActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setName("jLabel1"); // NOI18N

        jTabbedPane_PhanChinh_BangTrai.setName("jTabbedPane_PhanChinh_BangTrai"); // NOI18N

        jScrollPane_PhanChinh_BangTrai.setName("jScrollPane_PhanChinh_BangTrai"); // NOI18N
        jTabbedPane_PhanChinh_BangTrai.addTab(resourceMap.getString("jScrollPane_PhanChinh_BangTrai.TabConstraints.tabTitle"), jScrollPane_PhanChinh_BangTrai); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jComboBox_PhanChinh_BangTrai, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane_PhanChinh_BangTrai, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_PhanChinh_BangTrai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 298, Short.MAX_VALUE)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jTabbedPane_PhanChinh_BangTrai, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .addGap(25, 25, 25)))
        );

        jSplitPane2.setRightComponent(jPanel1);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jPanel2.setName("jPanel2"); // NOI18N

        jTabbedPane_PhanChinh_BangPhai.setName("jTabbedPane_PhanChinh_BangPhai"); // NOI18N

        jScrollPane_PhanChinh_BangPhai.setName("jScrollPane_PhanChinh_BangPhai"); // NOI18N
        jTabbedPane_PhanChinh_BangPhai.addTab(resourceMap.getString("jScrollPane_PhanChinh_BangPhai.TabConstraints.tabTitle"), jScrollPane_PhanChinh_BangPhai); // NOI18N

        jTextField3.setEditable(false);
        jTextField3.setText(resourceMap.getString("jTextField3.text")); // NOI18N
        jTextField3.setEnabled(false);
        jTextField3.setName("jTextField3"); // NOI18N

        jComboBox_PhanChinh_BangPhai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_PhanChinh_BangPhai.setName("jComboBox_PhanChinh_BangPhai"); // NOI18N
        jComboBox_PhanChinh_BangPhai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_PhanChinh_BangPhaiActionPerformed(evt);
            }
        });

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
            .addComponent(jTabbedPane_PhanChinh_BangPhai, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jComboBox_PhanChinh_BangPhai, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox_PhanChinh_BangPhai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane_PhanChinh_BangPhai, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        mainPanel.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        menuBar.setName("menuBar"); // NOI18N

        jMenu_File.setText(resourceMap.getString("jMenu_File.text")); // NOI18N
        jMenu_File.setName("jMenu_File"); // NOI18N

        jMenuItem_File_NewFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        jMenuItem_File_NewFolder.setText(resourceMap.getString("jMenuItem_File_NewFolder.text")); // NOI18N
        jMenuItem_File_NewFolder.setName("jMenuItem_File_NewFolder"); // NOI18N
        jMenuItem_File_NewFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_NewFolderActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_NewFolder);

        jMenuItem_File_Xoa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        jMenuItem_File_Xoa.setText(resourceMap.getString("jMenuItem_File_Xoa.text")); // NOI18N
        jMenuItem_File_Xoa.setName("jMenuItem_File_Xoa"); // NOI18N
        jMenuItem_File_Xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_XoaActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_Xoa);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(doan_tatalcommande_002.DoAn_TatalCommande_002App.class).getContext().getActionMap(DoAn_TatalCommande_002View.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        jMenu_File.add(exitMenuItem);

        menuBar.add(jMenu_File);

        jMenu_Edit.setText(resourceMap.getString("jMenu_Edit.text")); // NOI18N
        jMenu_Edit.setName("jMenu_Edit"); // NOI18N

        jMenuItem_Edit_Back.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_BACK_SPACE, 0));
        jMenuItem_Edit_Back.setText(resourceMap.getString("jMenuItem_Edit_Back.text")); // NOI18N
        jMenuItem_Edit_Back.setName("jMenuItem_Edit_Back"); // NOI18N
        jMenuItem_Edit_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_Edit_BackActionPerformed(evt);
            }
        });
        jMenu_Edit.add(jMenuItem_Edit_Back);

        menuBar.add(jMenu_Edit);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jCheckBoxMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.ALT_MASK));
        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText(resourceMap.getString("jCheckBoxMenuItem1.text")); // NOI18N
        jCheckBoxMenuItem1.setName("jCheckBoxMenuItem1"); // NOI18N
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem1);

        menuBar.add(jMenu1);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 762, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N
        jMenuBar1.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N
        jMenuBar1.add(jMenu3);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem_Edit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_Edit_BackActionPerformed
        // TODO add your handling code here:
        if (_bangHienTai == _bangTrai)
            _bangTrai.quayVeThuMucCha();
        else
            _bangPhai.quayVeThuMucCha();
    }//GEN-LAST:event_jMenuItem_Edit_BackActionPerformed
    /**
     * Tạo tên mặc định cho thư mục mới có dạng NewFolderx với x từ 0 đến 2^20
     * @param str_DuongDanThuMucCha     Đường dẫn đến thư mục cha của thư mục cần tạo để xác định số
     *                                  folder có tên dạng NewFolderx đang có trong thư mục cha
     * @return                          tên được tạo
     */
    private String taoTenThuMucMoiMacDinh (String str_DuongDanThuMucCha){
        String str_TenThuMucMoi = "NewFolder";
        String str_DuongDanDayDuThuMucCanTao = str_DuongDanThuMucCha + "\\" + str_TenThuMucMoi;
        File file = new File(str_DuongDanDayDuThuMucCanTao);
        if (!file.exists() && !file.isDirectory()){
            return str_DuongDanDayDuThuMucCanTao;
        }
        int i = 1;
        for (; i < Math.pow(2, 20); i++){
            file = new File(str_DuongDanDayDuThuMucCanTao + String.valueOf(i));
            if (!file.exists() && !file.isDirectory()){
                str_DuongDanDayDuThuMucCanTao += String.valueOf(i);
                break;
            }
        }
        return str_DuongDanDayDuThuMucCanTao;
    }
    /**
     * Tạo một thư mục mới
     * @param str_DuongDan  Đường dẫn đầy đủ (bao gồm tên) của thư mục cần tạo
     */
    private static void taoThuMucMoi (String str_DuongDan){
        File file = new File(str_DuongDan);
        if(!file.mkdirs())
            JOptionPane.showMessageDialog(null, "Không thể tạo thư mục!");
    }
    private void jMenuItem_File_NewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_NewFolderActionPerformed
        // TODO add your handling code here:
        /*
        String thuMucHienTai = _bangPhai.getTenFile();
        String tenThuMucCanTao = "NewFolder";
        String tenDayDuThuMucCanTao = thuMucHienTai + "\\" + tenThuMucCanTao;
        for (int i = 1; i < 1025; i++){
            File file = new File(tenDayDuThuMucCanTao + String.valueOf(i));
            if (!file.exists() && !file.isDirectory()){
                tenDayDuThuMucCanTao += String.valueOf(i);
                break;
            }
        }
        File file = new File(tenDayDuThuMucCanTao);
        file.mkdir();
        */
        String str_TenThuMucMoi = 
                JOptionPane.showInputDialog("Nhập tên thư mục mới:", taoTenThuMucMoiMacDinh(_bangHienTai.getTenFile()));
        //File file = new File(str_TenThuMucMoi).
        //String str_DuongDanDayDuThuMucMoi = _bangHienTai.getTenFile() + "\\";
        //System.setProperty(str_TenThuMucMoi, str_TenThuMucMoi)
        taoThuMucMoi(str_TenThuMucMoi);
        capNhatCacBang(new File(str_TenThuMucMoi).getParentFile());
}//GEN-LAST:event_jMenuItem_File_NewFolderActionPerformed
    /**
     * đặt trạng thái của tree view thành hiện thị hoặc ẩn
     * @param hienThi   true_hiện thị tree view, fasle không hiện thị
     */
    private void datTrangTraiHienThiTreeView(boolean b_HienThi){
        if (!b_HienThi){
                jScrollPane_PhanChinh_Tree.setVisible(false);
                jSplitPane2.setDividerLocation(0);
            }
            else{
                jScrollPane_PhanChinh_Tree.setVisible(true);
                jSplitPane2.setDividerLocation(150);
            }
}
    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        // TODO add your handling code here:
        datTrangTraiHienThiTreeView(jCheckBoxMenuItem1.getState());
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenuItem_File_XoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_XoaActionPerformed
        // TODO add your handling code here:
        ArrayList<String> str_CacDuongDan = _bangHienTai.layDuongDanDayDuFileDangDuocChon();
        //Nếu không đang chọn thư mục nào hoặc không xác nhận xóa
        if (str_CacDuongDan.size() == 0 || JOptionPane.showConfirmDialog(null, "Bạn muốn xóa " +
                    str_CacDuongDan.size() + " tập tin/ thư mục?") != 0)
            return;
        for (String str_DuongDan : str_CacDuongDan){
            File file = new File(str_DuongDan);
            if(!file.delete()){
                JOptionPane.showMessageDialog(null, "Không thể xóa " +
                        str_DuongDan, "Không thể xóa tập tin/thư mục", 2);
            }
        }
        capNhatCacBang(new File(str_CacDuongDan.get(0)).getParentFile());
    }//GEN-LAST:event_jMenuItem_File_XoaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        _bangTrai.duyetCacThuMucDatBiet("My Documents", jTabbedPane_PhanChinh_BangTrai);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        //File file = new File("E:\\Temp E\\Test VS2k8\\");
        //file.
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jComboBox_PhanChinh_BangPhaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_PhanChinh_BangPhaiActionPerformed
        // TODO add your handling code here:
        if (jComboBox_PhanChinh_BangPhai.getSelectedItem() != null){
            _bangPhai.capNhatBangDuyetThuMuc(jComboBox_PhanChinh_BangPhai.getSelectedItem().toString(), jScrollPane_PhanChinh_BangPhai);
            jTabbedPane_PhanChinh_BangPhai.setTitleAt(0, _bangPhai.getTenFile());
        }
}//GEN-LAST:event_jComboBox_PhanChinh_BangPhaiActionPerformed

    private void jComboBox_PhanChinh_BangTraiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_PhanChinh_BangTraiActionPerformed
        // TODO add your handling code here:
        if (jComboBox_PhanChinh_BangTrai.getSelectedItem() != null){
            _bangTrai.capNhatBangDuyetThuMuc(jComboBox_PhanChinh_BangTrai.getSelectedItem().toString(), jScrollPane_PhanChinh_BangTrai);
            jTabbedPane_PhanChinh_BangTrai.setTitleAt(0, _bangTrai.getTenFile());
        }
}//GEN-LAST:event_jComboBox_PhanChinh_BangTraiActionPerformed

    /**
     * Thực thi command line
     * @param str_Command   command cần thực thi
     */
    public static int thucThiCommandLine(String str_Command){
        try {
            Runtime.getRuntime().exec(str_Command);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Có lỗi khi thực thi command: " + str_Command, "Lỗi!", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 1;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox_PhanChinh_BangPhai;
    private javax.swing.JComboBox jComboBox_PhanChinh_BangTrai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem_Edit_Back;
    private javax.swing.JMenuItem jMenuItem_File_NewFolder;
    private javax.swing.JMenuItem jMenuItem_File_Xoa;
    private javax.swing.JMenu jMenu_Edit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel_PhanChan;
    private javax.swing.JPanel jPanel_PhanDau;
    private javax.swing.JScrollPane jScrollPane_PhanChinh_BangPhai;
    private javax.swing.JScrollPane jScrollPane_PhanChinh_BangTrai;
    private javax.swing.JScrollPane jScrollPane_PhanChinh_Tree;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane_PhanChinh_BangPhai;
    private javax.swing.JTabbedPane jTabbedPane_PhanChinh_BangTrai;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
