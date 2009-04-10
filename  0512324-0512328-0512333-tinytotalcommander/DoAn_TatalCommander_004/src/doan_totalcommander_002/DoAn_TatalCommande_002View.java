/*
 * DoAn_TatalCommande_002View.java
 */

package doan_totalcommander_002;

import QuanLyFile.BoQuanLyFile;
import QuanLyFile.Dialog_Xem_ChinhSuaFile;
import QuanLyFile.Dialog_SoSanhFile;
import DuyetFile.EventListener_ClickChuotVaoBangDuyetFile;
import DuyetFile.BangDuyetFile;
import com.sun.org.apache.xml.internal.utils.ObjectPool;
import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
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
import java.awt.event.KeyEvent;
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
    private BangDuyetFile _bangTrai;
    private BangDuyetFile _bangPhai;
//    private jEnum_CacBang _enum_BangHienTai;
    private BangDuyetFile _bangHienTai;
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
        _bangTrai = new BangDuyetFile("C:", jScrollPane_PhanChinh_BangTrai);
        _bangPhai = new BangDuyetFile("D:", jScrollPane_PhanChinh_BangPhai);
//        _enum_BangHienTai = jEnum_CacBang.BangTrai;
        _bangHienTai = _bangTrai;
        _bangPhai.getTable().clearSelection();
        //System.setProperty("user.dir", _bangHienTai.getTenFile());

        _bangTrai.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String str_TenFileDuocChon) {
                jLabel1.setText("Ban chon cua so trai");
                _bangPhai.getTable().clearSelection();
//                _enum_BangHienTai = jEnum_CacBang.BangTrai;
                _bangHienTai = _bangTrai;
                String str_PhanVung;
                if (_bangTrai.getTenFile().contains("\\"))
                    str_PhanVung = _bangTrai.getTenFile().substring(0, "C:\\".length());
                else
                    str_PhanVung = _bangTrai.getTenFile() + "\\";
                jComboBox_PhanChinh_BangTrai.setSelectedItem(str_PhanVung);
                //System.setProperty("user.dir", _bangHienTai.getTenFile());
                //JOptionPane.showMessageDialog(null, System.getProperty("user.dir"));
            }
        });
        _bangPhai.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String str_TenFileDuocChon) {
                jLabel1.setText("Ban chon cua so phai");
                _bangTrai.getTable().clearSelection();
                //_enum_BangHienTai = jEnum_CacBang.BangPhai;
                _bangHienTai = _bangPhai;

                String str_PhanVung;
                if (_bangPhai.getTenFile().contains("\\"))
                    str_PhanVung = _bangPhai.getTenFile().substring(0, "C:\\".length());
                else
                    str_PhanVung = _bangPhai.getTenFile() + "\\";
                jComboBox_PhanChinh_BangPhai.setSelectedItem(str_PhanVung);
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
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
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
        jComboBox_ThucThiCommandLine = new javax.swing.JComboBox();
        jButton_ThucThiCommandLine = new javax.swing.JButton();
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
        jMenuItem_File_XemFile = new javax.swing.JMenuItem();
        jMenuItem_File_ChinhSuaTapTin = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem_File_NewFolder = new javax.swing.JMenuItem();
        jMenuItem_File_Xoa = new javax.swing.JMenuItem();
        jMenuItem_File_SoSanh = new javax.swing.JMenuItem();
        jMenuItem_File_DoiTen = new javax.swing.JMenuItem();
        jMenuItem_File_DiChuyen = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jMenuItem_File_TimKiem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuItem_Zip = new javax.swing.JMenuItem();
        jMenuItem_unZip = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu_Edit = new javax.swing.JMenu();
        jMenuItem_Edit_Back = new javax.swing.JMenuItem();
        jMenu_Expect = new javax.swing.JMenu();
        jMenuItem_CatTapTin = new javax.swing.JMenuItem();
        jMenuItem_NoiTapTin = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        jMenuItem_KetNoiFTP = new javax.swing.JMenuItem();
        jMenuItem_KetNoiLan = new javax.swing.JMenuItem();
        jMenu_View_Tree = new javax.swing.JMenu();
        jCheckBoxMenuItem_TreeView = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem_fullView = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem_Brief = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem_Thumbnail = new javax.swing.JCheckBoxMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        jMenuItem_HienThiJavaDoc = new javax.swing.JMenuItem();
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

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(doan_totalcommander_002.DoAn_TatalCommande_002App.class).getContext().getResourceMap(DoAn_TatalCommande_002View.class);
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

        jButton10.setText(resourceMap.getString("jButton10.text")); // NOI18N
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setName("jButton10"); // NOI18N
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });
        jToolBar1.add(jButton10);

        jButton11.setText(resourceMap.getString("jButtonMove.text")); // NOI18N
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setName("jButtonMove"); // NOI18N
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton11MouseClicked(evt);
            }
        });
        jToolBar1.add(jButton11);

        jButton12.setText(resourceMap.getString("jButton_ReName.text")); // NOI18N
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setName("jButton_ReName"); // NOI18N
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_renameMouseClicked(evt);
            }
        });
        jToolBar1.add(jButton12);

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

        jComboBox_ThucThiCommandLine.setEditable(true);
        jComboBox_ThucThiCommandLine.setName("jComboBox_ThucThiCommandLine"); // NOI18N
        jComboBox_ThucThiCommandLine.setNextFocusableComponent(jButton_ThucThiCommandLine);
        jComboBox_ThucThiCommandLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_ThucThiCommandLineActionPerformed(evt);
            }
        });

        jButton_ThucThiCommandLine.setText(resourceMap.getString("jButton_ThucThiCommandLine.text")); // NOI18N
        jButton_ThucThiCommandLine.setName("jButton_ThucThiCommandLine"); // NOI18N
        jButton_ThucThiCommandLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ThucThiCommandLineActionPerformed(evt);
            }
        });

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
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(201, 201, 201)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_ThucThiCommandLine)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox_ThucThiCommandLine, 0, 500, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))))
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
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox_ThucThiCommandLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_ThucThiCommandLine))))
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
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jMenuItem_File_XemFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem_File_XemFile.setText(resourceMap.getString("jMenuItem_File_XemFile.text")); // NOI18N
        jMenuItem_File_XemFile.setName("jMenuItem_File_XemFile"); // NOI18N
        jMenuItem_File_XemFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_XemFileActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_XemFile);

        jMenuItem_File_ChinhSuaTapTin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        jMenuItem_File_ChinhSuaTapTin.setText(resourceMap.getString("jMenuItem_File_ChinhSuaTapTin.text")); // NOI18N
        jMenuItem_File_ChinhSuaTapTin.setName("jMenuItem_File_ChinhSuaTapTin"); // NOI18N
        jMenuItem_File_ChinhSuaTapTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_ChinhSuaTapTinActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_ChinhSuaTapTin);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem1);

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

        jMenuItem_File_SoSanh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem_File_SoSanh.setText(resourceMap.getString("jMenuItem_File_SoSanh.text")); // NOI18N
        jMenuItem_File_SoSanh.setName("jMenuItem_File_SoSanh"); // NOI18N
        jMenuItem_File_SoSanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_SoSanhActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_SoSanh);

        jMenuItem_File_DoiTen.setText(resourceMap.getString("jMenuItem_File_DoiTen.text")); // NOI18N
        jMenuItem_File_DoiTen.setName("jMenuItem_File_DoiTen"); // NOI18N
        jMenu_File.add(jMenuItem_File_DoiTen);

        jMenuItem_File_DiChuyen.setText(resourceMap.getString("jMenuItem_File_DiChuyen.text")); // NOI18N
        jMenuItem_File_DiChuyen.setName("jMenuItem_File_DiChuyen"); // NOI18N
        jMenu_File.add(jMenuItem_File_DiChuyen);

        jSeparator9.setName("jSeparator9"); // NOI18N
        jMenu_File.add(jSeparator9);

        jMenuItem_File_TimKiem.setText(resourceMap.getString("jMenuItem_File_TimKiem.text")); // NOI18N
        jMenuItem_File_TimKiem.setName("jMenuItem_File_TimKiem"); // NOI18N
        jMenu_File.add(jMenuItem_File_TimKiem);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jMenu_File.add(jSeparator4);

        jMenuItem_Zip.setText(resourceMap.getString("jMenuItem_Zip.text")); // NOI18N
        jMenuItem_Zip.setName("jMenuItem_Zip"); // NOI18N
        jMenu_File.add(jMenuItem_Zip);

        jMenuItem_unZip.setText(resourceMap.getString("jMenuItem_unZip.text")); // NOI18N
        jMenuItem_unZip.setName("jMenuItem_unZip"); // NOI18N
        jMenu_File.add(jMenuItem_unZip);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jMenu_File.add(jSeparator5);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(doan_totalcommander_002.DoAn_TatalCommande_002App.class).getContext().getActionMap(DoAn_TatalCommande_002View.class, this);
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

        jMenu_Expect.setText(resourceMap.getString("jMenu_Expect.text")); // NOI18N
        jMenu_Expect.setName("jMenu_Expect"); // NOI18N

        jMenuItem_CatTapTin.setText(resourceMap.getString("jMenuItem_CatTapTin.text")); // NOI18N
        jMenuItem_CatTapTin.setName("jMenuItem_CatTapTin"); // NOI18N
        jMenu_Expect.add(jMenuItem_CatTapTin);

        jMenuItem_NoiTapTin.setText(resourceMap.getString("jMenuItem_NoiTapTin.text")); // NOI18N
        jMenuItem_NoiTapTin.setName("jMenuItem_NoiTapTin"); // NOI18N
        jMenu_Expect.add(jMenuItem_NoiTapTin);

        jSeparator8.setName("jSeparator8"); // NOI18N
        jMenu_Expect.add(jSeparator8);

        jMenuItem_KetNoiFTP.setText(resourceMap.getString("jMenuItem_KetNoiFTP.text")); // NOI18N
        jMenuItem_KetNoiFTP.setName("jMenuItem_KetNoiFTP"); // NOI18N
        jMenu_Expect.add(jMenuItem_KetNoiFTP);

        jMenuItem_KetNoiLan.setText(resourceMap.getString("jMenuItem_KetNoiLan.text")); // NOI18N
        jMenuItem_KetNoiLan.setName("jMenuItem_KetNoiLan"); // NOI18N
        jMenu_Expect.add(jMenuItem_KetNoiLan);

        menuBar.add(jMenu_Expect);

        jMenu_View_Tree.setText(resourceMap.getString("jMenu_View_Tree.text")); // NOI18N
        jMenu_View_Tree.setName("jMenu_View_Tree"); // NOI18N

        jCheckBoxMenuItem_TreeView.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.ALT_MASK));
        jCheckBoxMenuItem_TreeView.setSelected(true);
        jCheckBoxMenuItem_TreeView.setText(resourceMap.getString("jCheckBoxMenuItem_TreeView.text")); // NOI18N
        jCheckBoxMenuItem_TreeView.setName("jCheckBoxMenuItem_TreeView"); // NOI18N
        jCheckBoxMenuItem_TreeView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem_TreeViewActionPerformed(evt);
            }
        });
        jMenu_View_Tree.add(jCheckBoxMenuItem_TreeView);

        jCheckBoxMenuItem_fullView.setSelected(true);
        jCheckBoxMenuItem_fullView.setText(resourceMap.getString("jCheckBoxMenuItem_fullView.text")); // NOI18N
        jCheckBoxMenuItem_fullView.setName("jCheckBoxMenuItem_fullView"); // NOI18N
        jMenu_View_Tree.add(jCheckBoxMenuItem_fullView);

        jCheckBoxMenuItem_Brief.setSelected(true);
        jCheckBoxMenuItem_Brief.setText(resourceMap.getString("jCheckBoxMenuItem_Brief.text")); // NOI18N
        jCheckBoxMenuItem_Brief.setName("jCheckBoxMenuItem_Brief"); // NOI18N
        jMenu_View_Tree.add(jCheckBoxMenuItem_Brief);

        jCheckBoxMenuItem_Thumbnail.setSelected(true);
        jCheckBoxMenuItem_Thumbnail.setText(resourceMap.getString("jCheckBoxMenuItem_Thumbnail.text")); // NOI18N
        jCheckBoxMenuItem_Thumbnail.setName("jCheckBoxMenuItem_Thumbnail"); // NOI18N
        jMenu_View_Tree.add(jCheckBoxMenuItem_Thumbnail);

        menuBar.add(jMenu_View_Tree);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        jMenuItem_HienThiJavaDoc.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem_HienThiJavaDoc.setText(resourceMap.getString("jMenuItem_HienThiJavaDoc.text")); // NOI18N
        jMenuItem_HienThiJavaDoc.setName("jMenuItem_HienThiJavaDoc"); // NOI18N
        jMenuItem_HienThiJavaDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_HienThiJavaDocActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem_HienThiJavaDoc);

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
    //hiện thị tree view
    private void jCheckBoxMenuItem_TreeViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem_TreeViewActionPerformed
        // TODO add your handling code here:
        datTrangTraiHienThiTreeView(jCheckBoxMenuItem_TreeView.getState());
}//GEN-LAST:event_jCheckBoxMenuItem_TreeViewActionPerformed

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
        if (jComboBox_PhanChinh_BangPhai.getSelectedItem() != null && _bangPhai.getTenFile() != null
                && jComboBox_PhanChinh_BangPhai.getSelectedItem().toString().charAt(0) != _bangPhai.getTenFile().charAt(0)){
            //Nếu jCombox đã được khởi tạo và ổ đĩa được chọn khác ổ đỉa hiện tại
            _bangPhai.capNhatBangDuyetThuMuc(jComboBox_PhanChinh_BangPhai.getSelectedItem().toString(), jScrollPane_PhanChinh_BangPhai);
            jTabbedPane_PhanChinh_BangPhai.setTitleAt(0, _bangPhai.getTenFile());
        }
}//GEN-LAST:event_jComboBox_PhanChinh_BangPhaiActionPerformed

    private void jComboBox_PhanChinh_BangTraiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_PhanChinh_BangTraiActionPerformed
        // TODO add your handling code here:
        if (jComboBox_PhanChinh_BangTrai.getSelectedItem() != null && _bangTrai.getTenFile() != null
                && jComboBox_PhanChinh_BangTrai.getSelectedItem().toString().charAt(0) != _bangTrai.getTenFile().charAt(0)){
            //Nếu jCombox đã được khởi tạo và ổ đĩa được chọn khác ổ đỉa hiện tại
            _bangTrai.capNhatBangDuyetThuMuc(jComboBox_PhanChinh_BangTrai.getSelectedItem().toString(), jScrollPane_PhanChinh_BangTrai);
            jTabbedPane_PhanChinh_BangTrai.setTitleAt(0, _bangTrai.getTenFile());
        }
}//GEN-LAST:event_jComboBox_PhanChinh_BangTraiActionPerformed

    private void jMenuItem_HienThiJavaDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_HienThiJavaDocActionPerformed
        // TODO add your handling code here:
        File file_JavaDoc = new File("javadoc\\index.html");
        if (file_JavaDoc.exists())
            try {
            Desktop.getDesktop().open(file_JavaDoc);
            } catch (IOException ex) {
                Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
            }
        else
            JOptionPane.showMessageDialog(null, "Không tìm thấy file \".\\javadoc\\index.html\" trong thư mục cài đặt!");
}//GEN-LAST:event_jMenuItem_HienThiJavaDocActionPerformed

    /**
 *
 * Khởi tạo vào hiện thị dialog cho phép xem hoặc chỉnh sửa tập tin
 * @param str_DuongDan          đường dẫn file cần xem
 * @param enumLoaiTruyXuat      xem hoặc chỉnh sửa ví dụ:jEnum_CacEnumTrongBai.XemFile
 * @throws java.io.IOException
 */
    private void hienThiDialogXemFile(String str_DuongDan, jEnum_CacEnumTrongBai enumLoaiTruyXuat) throws IOException {
        // TODO add your handling code here:
        if (new File(str_DuongDan).isFile()) {
            //Nếu là file thì khởi tạo dialog
            Dialog_Xem_ChinhSuaFile dialog_Xem_ChinhSuaFile = new Dialog_Xem_ChinhSuaFile();
            BoQuanLyFile.hienThiFile(str_DuongDan, dialog_Xem_ChinhSuaFile.getJTextPane_HienThiFile()
                    , enumLoaiTruyXuat);
            //Đặt title là đường dẫn file đang xem
            dialog_Xem_ChinhSuaFile.setTitle(str_DuongDan);
            //setEnable phù hợp cho button Save
            dialog_Xem_ChinhSuaFile.getJButton_Luu().setEnabled(enumLoaiTruyXuat == jEnum_CacEnumTrongBai.SuaFile);
            //hiện thị dialog
            dialog_Xem_ChinhSuaFile.setVisible(true);
        }
    }
    private void jMenuItem_File_XemFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_XemFileActionPerformed
        try {
            hienThiDialogXemFile(_bangHienTai.layDuongDanDayDuFileDangDuocChon().get(0),
                    jEnum_CacEnumTrongBai.XemFile);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem_File_XemFileActionPerformed

    private void jMenuItem_File_ChinhSuaTapTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_ChinhSuaTapTinActionPerformed
        // TODO add your handling code here:
        /*try {
            hienThiDialogXemFile(_bangHienTai.layDuongDanDayDuFileDangDuocChon().get(0),
                    jEnum_CacEnumTrongBai.SuaFile);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        File file = new File(_bangHienTai.layDuongDanDayDuFileDangDuocChon().get(0));
        if (file.isFile())
            thucThiCommandLine("notepad " + file.getPath());
    }//GEN-LAST:event_jMenuItem_File_ChinhSuaTapTinActionPerformed

    private void jComboBox_ThucThiCommandLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_ThucThiCommandLineActionPerformed
        // TODO add your handling code here:
        jButton_ThucThiCommandLineActionPerformed(evt);

    }//GEN-LAST:event_jComboBox_ThucThiCommandLineActionPerformed

    private void jButton_ThucThiCommandLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ThucThiCommandLineActionPerformed
        // TODO add your handling code here:
        String str_Command = jComboBox_ThucThiCommandLine.getSelectedItem().toString();
        if (jComboBox_ThucThiCommandLine.getSelectedIndex() == -1)
            jComboBox_ThucThiCommandLine.addItem(str_Command);
        if(thucThiCommandLine(str_Command) == 1)
            //Nếu không gọi được commandline
            JOptionPane.showMessageDialog(null, "Không thể thực thi commandline");
}//GEN-LAST:event_jButton_ThucThiCommandLineActionPerformed

    /**
     * Thực thi command line
     * @param str_Command   command cần thực thi
     */
    private int thucThiCommandLine(String str_Command){
        File file = new File(str_Command + "\\");//trường hợp người dùng đánh thiếu ký tự \
        if (file.isDirectory()){//Nếu là thư mục khác
            if(_bangHienTai == _bangPhai)
                _bangPhai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangPhai);
            else
                _bangTrai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangTrai);
            return 0;
        }

        file = new File(_bangHienTai.getTenFile() + "\\" + str_Command + "\\");
        if (file.isDirectory()){//Nếu là thư mục con của bảng hiện tại
            if(_bangHienTai == _bangPhai)
                _bangPhai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangPhai);
            else
                _bangTrai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangTrai);
            return 0;
        }
        file = new File(_bangHienTai.getTenFile() + "\\" + str_Command + "\\");
        if(file.exists())
            try {//Thử mở file trong thư mục hiện tại
                Desktop.getDesktop().open(file);
                return 0;
            } catch (IOException ex) {
                Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }

        //tìm thư mục windows/system32.
        String str_FileTrongThuMucSystem32DuDoan = System.getProperty("user.home").
                substring(0, "C:\\".length()) + "Windows\\System32\\" + str_Command;
        file = new File(str_FileTrongThuMucSystem32DuDoan);
        if (!file.exists())
            file = new File(str_FileTrongThuMucSystem32DuDoan + ".exe");//Đề phòng trường hợp đánh thiếu .exe
        if (file.exists())
            try {//thử tìm trong thư mục windows/system32.
                Desktop.getDesktop().open(file);
                return 0;
            } catch (IOException ex1) {
                Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex1);
            }

        //Chạy commandline
        try {
            Runtime.getRuntime().exec(str_Command);
            return 0;
        } catch (IOException ex2) {
            //JOptionPane.showMessageDialog(null, "Có lỗi khi thực thi command: " + str_Command, "Lỗi!", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return 1;
    }
    private void jMenuItem_File_SoSanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_SoSanhActionPerformed
        // TODO add your handling code here:
        try {
            Dialog_SoSanhFile dialog_SoSanhFile = new Dialog_SoSanhFile("C:\\temp.txt","C:\\temp.txt");
            dialog_SoSanhFile.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem_File_SoSanhActionPerformed
//copy click
    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseClicked
        try {
            // TODO add your handling code here:
            BoQuanLyFile.copyfile("F:\\1.txt.txt","F:\\a\\2.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton10MouseClicked
//rename click
    private void jButton11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseClicked
        try {
            // TODO add your handling code here:
            BoQuanLyFile.movefile("F:\\2.txt", "F:\\a\\2.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton11MouseClicked

    private void jButton_renameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_renameMouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            BoQuanLyFile.renamefile("F:\\a\\2.txt", "F:\\a\\5.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton_renameMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton_ThucThiCommandLine;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_Brief;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_Thumbnail;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_TreeView;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_fullView;
    private javax.swing.JComboBox jComboBox_PhanChinh_BangPhai;
    private javax.swing.JComboBox jComboBox_PhanChinh_BangTrai;
    private javax.swing.JComboBox jComboBox_ThucThiCommandLine;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem_CatTapTin;
    private javax.swing.JMenuItem jMenuItem_Edit_Back;
    private javax.swing.JMenuItem jMenuItem_File_ChinhSuaTapTin;
    private javax.swing.JMenuItem jMenuItem_File_DiChuyen;
    private javax.swing.JMenuItem jMenuItem_File_DoiTen;
    private javax.swing.JMenuItem jMenuItem_File_NewFolder;
    private javax.swing.JMenuItem jMenuItem_File_SoSanh;
    private javax.swing.JMenuItem jMenuItem_File_TimKiem;
    private javax.swing.JMenuItem jMenuItem_File_XemFile;
    private javax.swing.JMenuItem jMenuItem_File_Xoa;
    private javax.swing.JMenuItem jMenuItem_HienThiJavaDoc;
    private javax.swing.JMenuItem jMenuItem_KetNoiFTP;
    private javax.swing.JMenuItem jMenuItem_KetNoiLan;
    private javax.swing.JMenuItem jMenuItem_NoiTapTin;
    private javax.swing.JMenuItem jMenuItem_Zip;
    private javax.swing.JMenuItem jMenuItem_unZip;
    private javax.swing.JMenu jMenu_Edit;
    private javax.swing.JMenu jMenu_Expect;
    private javax.swing.JMenu jMenu_View_Tree;
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
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
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