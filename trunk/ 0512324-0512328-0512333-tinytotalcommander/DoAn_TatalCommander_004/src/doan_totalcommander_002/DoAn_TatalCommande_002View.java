/*
 * DoAn_TatalCommande_002View.java
 */

package doan_totalcommander_002;

import QuanLyFile.BoQuanLyFile;
import QuanLyFile.Dialog_Xem_ChinhSuaFile;
import QuanLyFile.Dialog_SoSanhFile;
import DuyetFile.EventListener_ClickChuotVaoBangDuyetFile;
import DuyetFile.BangDuyetFile;
import DuyetFile.CayDuyetFile;
import DuyetFile.DroppableTable;
import DuyetFile.EventListener_ClickChuotVaoCayDuyetFile;
import QuanLyFile.Dialog_CatNho;
import QuanLyFile.Dialog_Copy;
import QuanLyFile.Dialog_GhepNoi;
import QuanLyFile.Dialog_Move;
import QuanLyFile.Dialog_ReMove;
import QuanLyFile.Dialog_TimFile;
import QuanLyFileNen.BoQuanLyFileZip;
import QuanLyFileNen.Dialog_AppendZip;
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
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ch.randelshofer.quaqua.QuaquaManager;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import net.infonode.gui.laf.InfoNodeLookAndFeel;


/**
 * The application's main frame.
 */

public class DoAn_TatalCommande_002View extends FrameView {
    private BangDuyetFile bangTrai;
    private BangDuyetFile bangPhai;
//    private jEnum_CacBang _enum_BangHienTai;
    private BangDuyetFile bangHienTai;
    private CayDuyetFile cayDuyetFile;
    private boolean bKhoiTaoXong = false;

    public DoAn_TatalCommande_002View(SingleFrameApplication app) {
        super(app);

        initComponents();
 /*---------------------Khởi tạo menu item cho LAF-------------------------------------------*/
        UIManager.LookAndFeelInfo[] infos= UIManager.getInstalledLookAndFeels();
        JMenuItem []items=new JMenuItem[infos.length];
        for (int i=0; i<infos.length; i++){
            items[i]=new JMenuItem(infos[i].getName());
            items[i].setActionCommand(infos[i].getClassName());
            items[i].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    clickMenu(e);
                }
            });
            this.LaF.add(items[i]);

        }




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
        bangTrai = new BangDuyetFile("C:", jScrollPane_PhanChinh_BangTrai);
        bangPhai = new BangDuyetFile("D:", jScrollPane_PhanChinh_BangPhai);
//        _enum_BangHienTai = jEnum_CacBang.BangTrai;
        bangHienTai = bangTrai;
        bangPhai.getTable().clearSelection();
        //System.setProperty("user.dir", _bangHienTai.getTenFile());

        //Khi nguoi dung thay doi thu muc cua bang ben phai
        bangTrai.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String str_TenFileDuocChon, int iSoLanClick) {
                //jLabel1.setText("Ban chon cua so trai");
                //bangPhai.getTable().clearSelection();
//                _enum_BangHienTai = jEnum_CacBang.BangTrai;
                if (!bKhoiTaoXong)//nếu chưa khởi tạo xong thì return
                    return;
                bangHienTai = bangTrai;
                
                if (iSoLanClick == 1)
                    return;
                String str_PhanVung;
                if (bangTrai.getTenFile().contains("\\"))
                    str_PhanVung = bangTrai.getTenFile().substring(0, "C:\\".length());
                else
                    str_PhanVung = bangTrai.getTenFile() + "\\";

                if (!bangTrai.getTenFile().substring(0,1).equalsIgnoreCase(str_PhanVung.substring(0, 1)))
                    jComboBox_PhanChinh_BangTrai.setSelectedItem(str_PhanVung);
                //System.setProperty("user.dir", _bangHienTai.getTenFile());
                //JOptionPane.showMessageDialog(null, System.getProperty("user.dir"));
                //Goi ham cap nhat cay
                int viTriThanhCuon = cayDuyetFile.capNhatCay(str_TenFileDuocChon);
                jScrollPane_PhanChinh_Tree.getVerticalScrollBar().setValue(viTriThanhCuon);
            }
        });
        
        //Khi nguoi dung thay doi thu muc cua bang ben phai
        bangPhai.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String str_TenFileDuocChon, int iSoLanClick) {
                //jLabel1.setText("Ban chon cua so phai");
                //bangTrai.getTable().clearSelection();
                //_enum_BangHienTai = jEnum_CacBang.BangPhai;
                bangHienTai = bangPhai;

                if (iSoLanClick == 1)
                    return;
                String str_PhanVung;
                if (bangPhai.getTenFile().contains("\\"))
                    str_PhanVung = bangPhai.getTenFile().substring(0, "C:\\".length());
                else
                    str_PhanVung = bangPhai.getTenFile() + "\\";
                
                if (!bangPhai.getTenFile().substring(0,1).equalsIgnoreCase(str_PhanVung.substring(0, 1)))
                    jComboBox_PhanChinh_BangPhai.setSelectedItem(str_PhanVung);

                //Goi ham cap nhat cay

                int viTriThanhCuon = cayDuyetFile.capNhatCay(str_TenFileDuocChon);
                jScrollPane_PhanChinh_Tree.getVerticalScrollBar().setValue(viTriThanhCuon);
            }
        });
/*---------------------------------------------------------------------------------------*/

/*---------Khởi tạo treeview và 2 combo box hiện thị các ổ đĩa----------------------------*/
        /*---------Khởi tạo treeview và 2 combo box hiện thị các ổ đĩa----------------------------*/
        cayDuyetFile = new CayDuyetFile(jScrollPane_PhanChinh_Tree);
        cayDuyetFile.addEventListener_ClickChuotVaoCayDuyetFile(new EventListener_ClickChuotVaoCayDuyetFile() {

            public void Event_ClickChuotVaoCayDuyetFile_Occurred(String str_fileduocchon) {
                if (!bKhoiTaoXong)//nếu chưa khởi tạo xong thì return
                    return;
               if ((bangHienTai.getTenFile().replace("\\\\", "\\")).equalsIgnoreCase(str_fileduocchon))
                   return;
               if (bangHienTai == bangPhai){
                   bangPhai.capNhatBangDuyetThuMuc(str_fileduocchon, jScrollPane_PhanChinh_BangPhai);
               }
               else
                   bangTrai.capNhatBangDuyetThuMuc(str_fileduocchon, jScrollPane_PhanChinh_BangTrai);
            }
        }
        )  ;
        jComboBox_PhanChinh_BangPhai.removeAllItems();
        jComboBox_PhanChinh_BangTrai.removeAllItems();
        for (File file : File.listRoots()){
            jComboBox_PhanChinh_BangPhai.addItem(file.getPath());
            jComboBox_PhanChinh_BangTrai.addItem(file.getPath());
        }
        jComboBox_PhanChinh_BangPhai.setSelectedItem(bangTrai.getTenFile());
        jComboBox_PhanChinh_BangTrai.setSelectedItem(bangPhai.getTenFile());
        //jTree1 = new JTree(root);
        //jScrollPane_PhanChinh_Tree.setViewportView(jTree1);
/*---------------------------------------------------------------------------------------*/
        //bangTrai.capNhatBangDuyetThuMuc("C:", jScrollPane_PhanChinh_BangTrai);
        //String strThuMucHienHanh = System.getProperty("user.dir");
        bangPhai.capNhatBangDuyetThuMuc("C:", jScrollPane_PhanChinh_BangPhai);

        Timer timer = new Timer(10 * 1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (bangHienTai == bangTrai){
                    bangPhai.capNhatBangDuyetThuMuc(jScrollPane_PhanChinh_BangPhai.getToolTipText()
                            , jScrollPane_PhanChinh_BangPhai);
                    bangTrai.capNhatBangDuyetThuMuc(jScrollPane_PhanChinh_BangTrai.getToolTipText()
                            , jScrollPane_PhanChinh_BangTrai);
                    bangTrai.getTable().requestFocusInWindow();
                }
                if (bangHienTai == bangPhai){
                    bangTrai.capNhatBangDuyetThuMuc(jScrollPane_PhanChinh_BangTrai.getToolTipText()
                            , jScrollPane_PhanChinh_BangTrai);
                    bangPhai.capNhatBangDuyetThuMuc(jScrollPane_PhanChinh_BangPhai.getToolTipText()
                            , jScrollPane_PhanChinh_BangPhai);
                    bangPhai.getTable().requestFocusInWindow();
                }
            }
        });
        timer.start();
        bKhoiTaoXong = true;
//
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
    }// dong view

    @Action

   public void clickMenu(ActionEvent e){
        String laf=e.getActionCommand();
        try{
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
        }catch(Exception ex){

        }
    }


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
        if (bangHienTai.getTenFile().equals(bangPhai.getTenFile())) {
            bangPhai.capNhatBangDuyetThuMuc(bangPhai.getTenFile(), jScrollPane_PhanChinh_BangPhai);
        } 
        if(bangHienTai.getTenFile().equals(bangTrai.getTenFile())) {
            bangTrai.capNhatBangDuyetThuMuc(bangTrai.getTenFile(), jScrollPane_PhanChinh_BangTrai);
        }
        if(bangTrai.getTenFile().equalsIgnoreCase(thuMucVuaCapNhat.getPath()))
            bangTrai.capNhatBangDuyetThuMuc(bangTrai.getTenFile(), jScrollPane_PhanChinh_BangTrai);
        if(bangPhai.getTenFile().equalsIgnoreCase(thuMucVuaCapNhat.getPath()))
            bangPhai.capNhatBangDuyetThuMuc(bangPhai.getTenFile(), jScrollPane_PhanChinh_BangPhai);
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
        jButton_destop = new javax.swing.JButton();
        jButton_myDocuments = new javax.swing.JButton();
        jButton_find = new javax.swing.JButton();
        jButton_newFile = new javax.swing.JButton();
        jButton_zip = new javax.swing.JButton();
        jButton_unzip = new javax.swing.JButton();
        jButton_sosanhfile = new javax.swing.JButton();
        jPanel_PhanChan = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jButton_View = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton_Edit = new javax.swing.JButton();
        jButton_NewFolder = new javax.swing.JButton();
        jButton_Move = new javax.swing.JButton();
        jButton_Copy = new javax.swing.JButton();
        jButton_Delete = new javax.swing.JButton();
        jComboBox_ThucThiCommandLine = new javax.swing.JComboBox();
        jButton_ThucThiCommandLine = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane_PhanChinh_Tree = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jComboBox_PhanChinh_BangTrai = new javax.swing.JComboBox();
        jTabbedPane_PhanChinh_BangTrai = new javax.swing.JTabbedPane();
        jScrollPane_PhanChinh_BangTrai = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane_PhanChinh_BangPhai = new javax.swing.JTabbedPane();
        jScrollPane_PhanChinh_BangPhai = new javax.swing.JScrollPane();
        jComboBox_PhanChinh_BangPhai = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu jMenu_File = new javax.swing.JMenu();
        jMenuItem_File_XemFile = new javax.swing.JMenuItem();
        jMenuItem_File_ChinhSuaTapTin = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem_File_NewFolder = new javax.swing.JMenuItem();
        jMenuItem_TaoFileMoi = new javax.swing.JMenuItem();
        jMenuItem_File_Xoa = new javax.swing.JMenuItem();
        jMenuItem_File_SoSanh = new javax.swing.JMenuItem();
        jMenuItem_File_DoiTen = new javax.swing.JMenuItem();
        jMenuItem_File_DiChuyen = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jMenuItem_File_TimKiem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuItem_Zip = new javax.swing.JMenuItem();
        jMenuItem_File_AppendZip = new javax.swing.JMenuItem();
        jMenuItem_File_ViewZip = new javax.swing.JMenuItem();
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
        LaF = new javax.swing.JMenu();
        MacLAF = new javax.swing.JMenuItem();
        InfoNodeLAF = new javax.swing.JMenuItem();
        TinyLAF = new javax.swing.JMenuItem();
        SquarenessLAF = new javax.swing.JMenuItem();
        smoothLAF = new javax.swing.JMenuItem();
        EaSynthLAF = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
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
        jButton13 = new javax.swing.JButton();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel_PhanDau.setName("jPanel_PhanDau"); // NOI18N
        jPanel_PhanDau.setPreferredSize(new java.awt.Dimension(932, 42));

        jToolBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setPreferredSize(new java.awt.Dimension(100, 38));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(doan_totalcommander_002.DoAn_TatalCommande_002App.class).getContext().getResourceMap(DoAn_TatalCommande_002View.class);
        jButton_destop.setIcon(resourceMap.getIcon("jButton_destop.icon")); // NOI18N
        jButton_destop.setText(resourceMap.getString("jButton_destop.text")); // NOI18N
        jButton_destop.setToolTipText(resourceMap.getString("jButton_destop.toolTipText")); // NOI18N
        jButton_destop.setFocusable(false);
        jButton_destop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_destop.setName("jButton_destop"); // NOI18N
        jButton_destop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_destop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_destopActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_destop);

        jButton_myDocuments.setIcon(resourceMap.getIcon("jButton_myDocuments.icon")); // NOI18N
        jButton_myDocuments.setText(resourceMap.getString("jButton_myDocuments.text")); // NOI18N
        jButton_myDocuments.setToolTipText(resourceMap.getString("jButton_myDocuments.toolTipText")); // NOI18N
        jButton_myDocuments.setFocusable(false);
        jButton_myDocuments.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_myDocuments.setName("jButton_myDocuments"); // NOI18N
        jButton_myDocuments.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_myDocuments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_myDocumentsActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_myDocuments);

        jButton_find.setIcon(resourceMap.getIcon("jbutton_find.icon")); // NOI18N
        jButton_find.setText(resourceMap.getString("jbutton_find.text")); // NOI18N
        jButton_find.setToolTipText(resourceMap.getString("jbutton_find.toolTipText")); // NOI18N
        jButton_find.setFocusable(false);
        jButton_find.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_find.setName("jbutton_find"); // NOI18N
        jButton_find.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_find);

        jButton_newFile.setIcon(resourceMap.getIcon("jButton_newFlie.icon")); // NOI18N
        jButton_newFile.setText(resourceMap.getString("jButton_newFlie.text")); // NOI18N
        jButton_newFile.setToolTipText(resourceMap.getString("jButton_newFlie.toolTipText")); // NOI18N
        jButton_newFile.setFocusable(false);
        jButton_newFile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_newFile.setName("jButton_newFlie"); // NOI18N
        jButton_newFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_newFile);

        jButton_zip.setIcon(resourceMap.getIcon("jButton_zipFile.icon")); // NOI18N
        jButton_zip.setText(resourceMap.getString("jButton_zipFile.text")); // NOI18N
        jButton_zip.setToolTipText(resourceMap.getString("jButton_zipFile.toolTipText")); // NOI18N
        jButton_zip.setFocusable(false);
        jButton_zip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_zip.setName("jButton_zipFile"); // NOI18N
        jButton_zip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_zip);

        jButton_unzip.setIcon(resourceMap.getIcon("jButton_unZip.icon")); // NOI18N
        jButton_unzip.setText(resourceMap.getString("jButton_unZip.text")); // NOI18N
        jButton_unzip.setToolTipText(resourceMap.getString("jButton_unZip.toolTipText")); // NOI18N
        jButton_unzip.setFocusable(false);
        jButton_unzip.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_unzip.setName("jButton_unZip"); // NOI18N
        jButton_unzip.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton_unzip);

        jButton_sosanhfile.setIcon(resourceMap.getIcon("jButton_sosanhfile.icon")); // NOI18N
        jButton_sosanhfile.setText(resourceMap.getString("jButton_sosanhfile.text")); // NOI18N
        jButton_sosanhfile.setToolTipText(resourceMap.getString("jButton_sosanhfile.toolTipText")); // NOI18N
        jButton_sosanhfile.setFocusable(false);
        jButton_sosanhfile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_sosanhfile.setName("jButton_sosanhfile"); // NOI18N
        jButton_sosanhfile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_sosanhfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_sosanhfileActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton_sosanhfile);

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

        jButton_View.setText(resourceMap.getString("jButton_f3view.text")); // NOI18N
        jButton_View.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton_View.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton_View.setName("jButton_f3view"); // NOI18N
        jButton_View.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ViewActionPerformed(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton_alf4Exit.text")); // NOI18N
        jButton3.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton3.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton3.setName("jButton_alf4Exit"); // NOI18N

        jButton_Edit.setText(resourceMap.getString("jButton_f4Edit.text")); // NOI18N
        jButton_Edit.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton_Edit.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton_Edit.setName("jButton_f4Edit"); // NOI18N
        jButton_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EditActionPerformed(evt);
            }
        });

        jButton_NewFolder.setText(resourceMap.getString("jButton_f7NewFolder.text")); // NOI18N
        jButton_NewFolder.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton_NewFolder.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton_NewFolder.setName("jButton_f7NewFolder"); // NOI18N
        jButton_NewFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NewFolderActionPerformed(evt);
            }
        });

        jButton_Move.setText(resourceMap.getString("jButton_f6Move.text")); // NOI18N
        jButton_Move.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton_Move.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton_Move.setName("jButton_f6Move"); // NOI18N
        jButton_Move.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MoveActionPerformed(evt);
            }
        });

        jButton_Copy.setText(resourceMap.getString("jButton_f5Copy.text")); // NOI18N
        jButton_Copy.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton_Copy.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton_Copy.setName("jButton_f5Copy"); // NOI18N
        jButton_Copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CopyActionPerformed(evt);
            }
        });

        jButton_Delete.setText(resourceMap.getString("jButton_f8Delete.text")); // NOI18N
        jButton_Delete.setMaximumSize(new java.awt.Dimension(1024, 23));
        jButton_Delete.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton_Delete.setName("jButton_f8Delete"); // NOI18N
        jButton_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DeleteActionPerformed(evt);
            }
        });

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
                        .addComponent(jButton_View, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Edit, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Copy, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jButton_ThucThiCommandLine)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox_ThucThiCommandLine, 0, 498, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton_Move, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_NewFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Delete, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
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
                    .addComponent(jButton_View, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_NewFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Move, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Copy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jComboBox_PhanChinh_BangTrai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_PhanChinh_BangTrai.setName("jComboBox_PhanChinh_BangTrai"); // NOI18N
        jComboBox_PhanChinh_BangTrai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_PhanChinh_BangTraiActionPerformed(evt);
            }
        });

        jTabbedPane_PhanChinh_BangTrai.setName("jTabbedPane_PhanChinh_BangTrai"); // NOI18N

        jScrollPane_PhanChinh_BangTrai.setName("jScrollPane_PhanChinh_BangTrai"); // NOI18N
        jTabbedPane_PhanChinh_BangTrai.addTab(resourceMap.getString("jScrollPane_PhanChinh_BangTrai.TabConstraints.tabTitle"), jScrollPane_PhanChinh_BangTrai); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane_PhanChinh_BangTrai, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jComboBox_PhanChinh_BangTrai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(387, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jComboBox_PhanChinh_BangTrai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane_PhanChinh_BangTrai, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel1);

        jSplitPane1.setLeftComponent(jSplitPane2);

        jPanel2.setName("jPanel2"); // NOI18N

        jTabbedPane_PhanChinh_BangPhai.setName("jTabbedPane_PhanChinh_BangPhai"); // NOI18N

        jScrollPane_PhanChinh_BangPhai.setName("jScrollPane_PhanChinh_BangPhai"); // NOI18N
        jTabbedPane_PhanChinh_BangPhai.addTab(resourceMap.getString("jScrollPane_PhanChinh_BangPhai.TabConstraints.tabTitle"), jScrollPane_PhanChinh_BangPhai); // NOI18N

        jComboBox_PhanChinh_BangPhai.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_PhanChinh_BangPhai.setName("jComboBox_PhanChinh_BangPhai"); // NOI18N
        jComboBox_PhanChinh_BangPhai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_PhanChinh_BangPhaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane_PhanChinh_BangPhai, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jComboBox_PhanChinh_BangPhai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jComboBox_PhanChinh_BangPhai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane_PhanChinh_BangPhai, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
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

        jMenuItem_TaoFileMoi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItem_TaoFileMoi.setText(resourceMap.getString("jMenuItem_TaoFileMoi.text")); // NOI18N
        jMenuItem_TaoFileMoi.setName("jMenuItem_TaoFileMoi"); // NOI18N
        jMenuItem_TaoFileMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_TaoFileMoiActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_TaoFileMoi);

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
        jMenuItem_File_DoiTen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_DoiTenActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_DoiTen);

        jMenuItem_File_DiChuyen.setText(resourceMap.getString("jMenuItem_File_DiChuyen.text")); // NOI18N
        jMenuItem_File_DiChuyen.setName("jMenuItem_File_DiChuyen"); // NOI18N
        jMenuItem_File_DiChuyen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_DiChuyenActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_DiChuyen);

        jSeparator9.setName("jSeparator9"); // NOI18N
        jMenu_File.add(jSeparator9);

        jMenuItem_File_TimKiem.setText(resourceMap.getString("jMenuItem_File_TimKiem.text")); // NOI18N
        jMenuItem_File_TimKiem.setName("jMenuItem_File_TimKiem"); // NOI18N
        jMenuItem_File_TimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_TimKiemActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_TimKiem);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jMenu_File.add(jSeparator4);

        jMenuItem_Zip.setText(resourceMap.getString("jMenuItem_Zip.text")); // NOI18N
        jMenuItem_Zip.setName("jMenuItem_Zip"); // NOI18N
        jMenuItem_Zip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_ZipActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_Zip);

        jMenuItem_File_AppendZip.setText(resourceMap.getString("jMenuItem_File_AppendZip.text")); // NOI18N
        jMenuItem_File_AppendZip.setName("jMenuItem_File_AppendZip"); // NOI18N
        jMenuItem_File_AppendZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_AppendZipActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_AppendZip);

        jMenuItem_File_ViewZip.setText(resourceMap.getString("jMenuItem_File_ViewZip.text")); // NOI18N
        jMenuItem_File_ViewZip.setName("jMenuItem_File_ViewZip"); // NOI18N
        jMenuItem_File_ViewZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_File_ViewZipActionPerformed(evt);
            }
        });
        jMenu_File.add(jMenuItem_File_ViewZip);

        jMenuItem_unZip.setText(resourceMap.getString("jMenuItem_unZip.text")); // NOI18N
        jMenuItem_unZip.setName("jMenuItem_unZip"); // NOI18N
        jMenuItem_unZip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_unZipActionPerformed(evt);
            }
        });
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
        jMenuItem_CatTapTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_CatTapTinActionPerformed(evt);
            }
        });
        jMenu_Expect.add(jMenuItem_CatTapTin);

        jMenuItem_NoiTapTin.setText(resourceMap.getString("jMenuItem_NoiTapTin.text")); // NOI18N
        jMenuItem_NoiTapTin.setName("jMenuItem_NoiTapTin"); // NOI18N
        jMenuItem_NoiTapTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_NoiTapTinActionPerformed(evt);
            }
        });
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

        LaF.setText(resourceMap.getString("LaF.text")); // NOI18N
        LaF.setName("LaF"); // NOI18N

        MacLAF.setText(resourceMap.getString("MacLAF.text")); // NOI18N
        MacLAF.setName("MacLAF"); // NOI18N
        MacLAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MacLAFActionPerformed(evt);
            }
        });
        LaF.add(MacLAF);

        InfoNodeLAF.setText(resourceMap.getString("InfoNodeLAF.text")); // NOI18N
        InfoNodeLAF.setName("InfoNodeLAF"); // NOI18N
        InfoNodeLAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InfoNodeLAFActionPerformed(evt);
            }
        });
        LaF.add(InfoNodeLAF);

        TinyLAF.setText(resourceMap.getString("TinyLAF.text")); // NOI18N
        TinyLAF.setName("TinyLAF"); // NOI18N
        TinyLAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TinyLAFActionPerformed(evt);
            }
        });
        LaF.add(TinyLAF);

        SquarenessLAF.setText(resourceMap.getString("SquarenessLAF.text")); // NOI18N
        SquarenessLAF.setName("SquarenessLAF"); // NOI18N
        SquarenessLAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SquarenessLAFActionPerformed(evt);
            }
        });
        LaF.add(SquarenessLAF);

        smoothLAF.setText(resourceMap.getString("smoothLAF.text")); // NOI18N
        smoothLAF.setName("smoothLAF"); // NOI18N
        smoothLAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smoothLAFActionPerformed(evt);
            }
        });
        LaF.add(smoothLAF);

        EaSynthLAF.setText(resourceMap.getString("EaSynthLAF.text")); // NOI18N
        EaSynthLAF.setName("EaSynthLAF"); // NOI18N
        EaSynthLAF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EaSynthLAFActionPerformed(evt);
            }
        });
        LaF.add(EaSynthLAF);

        jSeparator10.setName("jSeparator10"); // NOI18N
        LaF.add(jSeparator10);

        menuBar.add(LaF);

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
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                .addGap(207, 207, 207)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 762, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addGap(160, 160, 160))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel))
                .addGap(3, 3, 3))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N
        jMenuBar1.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N
        jMenuBar1.add(jMenu3);

        jButton13.setText(resourceMap.getString("jButton13.text")); // NOI18N
        jButton13.setName("jButton13"); // NOI18N

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem_Edit_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_Edit_BackActionPerformed
        // TODO add your handling code here:
        if (bangHienTai == bangTrai)
            bangTrai.quayVeThuMucCha();
        else
            bangPhai.quayVeThuMucCha();
    }//GEN-LAST:event_jMenuItem_Edit_BackActionPerformed
    
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
                JOptionPane.showInputDialog("Nhập tên thư mục mới:", BoQuanLyFile.taoTenThuMucMoiMacDinh(bangHienTai.getTenFile()));
        if (!str_TenThuMucMoi.contains("\\"))
            str_TenThuMucMoi = bangHienTai.getTenFile() + str_TenThuMucMoi;
        //File file = new File(str_TenThuMucMoi).
        //String str_DuongDanDayDuThuMucMoi = _bangHienTai.getTenFile() + "\\";
        //System.setProperty(str_TenThuMucMoi, str_TenThuMucMoi)
        BoQuanLyFile.taoThuMucMoi(str_TenThuMucMoi);
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
        ArrayList<String> str_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        //Nếu không đang chọn thư mục nào hoặc không xác nhận xóa
        if (str_CacDuongDan.size() == 0)
        {
            JOptionPane.showMessageDialog(null, "Chọn file để xóa");
            return;
        }
        Dialog_ReMove dialog = new Dialog_ReMove(null, false);
        dialog.getJTextField_Nguon().setText(str_CacDuongDan.get(0));
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_File_XoaActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        //Kiểm tra xem người dùng có chọn file nào không?
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file để copy!");
            return;
        }
        String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);

        //tạo file các file nguồn và đích
        File f1 = new File(str_DuongDanFileDangChon);
        File f2 = bangHienTai != bangPhai ? new File(bangPhai.getTenFile()) : new File(bangPhai.getTenFile());

        Dialog_Copy dialog = new Dialog_Copy(null, false);
        
        dialog.getJTextField_Dich().setText(f2.getAbsolutePath());
        dialog.getJTextField_Nguon().setText(f1.getAbsolutePath());

        dialog.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jComboBox_PhanChinh_BangPhaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_PhanChinh_BangPhaiActionPerformed
        // TODO add your handling code here:
        if (!bKhoiTaoXong)//nếu chưa khởi tạo xong thì return
            return;
        if (jComboBox_PhanChinh_BangPhai.getSelectedItem() != null && bangPhai.getTenFile() != null
                && jComboBox_PhanChinh_BangPhai.getSelectedItem().toString().charAt(0) != bangPhai.getTenFile().charAt(0)){
            //Nếu jCombox đã được khởi tạo và ổ đĩa được chọn khác ổ đỉa hiện tại
            bangPhai.capNhatBangDuyetThuMuc(jComboBox_PhanChinh_BangPhai.getSelectedItem().toString(), jScrollPane_PhanChinh_BangPhai);
            jTabbedPane_PhanChinh_BangPhai.setTitleAt(0, bangPhai.getTenFile());
        }
}//GEN-LAST:event_jComboBox_PhanChinh_BangPhaiActionPerformed

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
            hienThiDialogXemFile(bangHienTai.layDuongDanDayDuFileDangDuocChon().get(0),
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
        File file = new File(bangHienTai.layDuongDanDayDuFileDangDuocChon().get(0));
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
            if(bangHienTai == bangPhai)
                bangPhai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangPhai);
            else
                bangTrai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangTrai);
            return 0;
        }

        file = new File(bangHienTai.getTenFile() + "\\" + str_Command + "\\");
        if (file.isDirectory()){//Nếu là thư mục con của bảng hiện tại
            if(bangHienTai == bangPhai)
                bangPhai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangPhai);
            else
                bangTrai.capNhatBangDuyetThuMuc(file.getPath(), jScrollPane_PhanChinh_BangTrai);
            return 0;
        }
        file = new File(bangHienTai.getTenFile() + "\\" + str_Command + "\\");
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
//copy click//rename click
    private void jMenuItem_CatTapTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_CatTapTinActionPerformed
        // TODO add your handling code here:
        //Lấy file đầu tiên được chọn
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file để cắt!");
            return;
        }
        String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);
        
        if (new File(str_DuongDanFileDangChon).isFile()){//nếu là file
            //Lấy đường dẫn thư mục đích (là đường dẫn hiện tại của bảng ko được chọn)
            String str_DuongDanThuMucDich = (bangHienTai.getTenFile().equalsIgnoreCase(bangPhai.getTenFile())) ?
                bangTrai.getTenFile() : bangPhai.getTenFile();

            //tạo và cập nhật thông tin cho dialog
            Dialog_CatNho dialog = new Dialog_CatNho(null, false);
            dialog.getMyComp_OpenSaveFile_FileNguon().getJComboBox_DuongDanFile().setSelectedItem(str_DuongDanFileDangChon);
            dialog.setTextOfJTextField_ThuMucDich(str_DuongDanThuMucDich);
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem_CatTapTinActionPerformed

    private void jMenuItem_NoiTapTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_NoiTapTinActionPerformed
        // TODO add your handling code here:
        //tạo dialog
        Dialog_GhepNoi dialog = new Dialog_GhepNoi(null, true);

        //Cập nhật thông tin mặc định
        //Lấy file đầu tiên được chọn
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() > 0){
            String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);
            File fileDauTien = new File(str_DuongDanFileDangChon);
            if (fileDauTien.isFile()//Nếu file đầu tiên trong danh sách được chọn là file
                    && str_DuongDanFileDangChon.endsWith(".001")){//Nếu là file và có phần mở rộng là 001

                //tạo đường dẫn file đích (là đường dẫn hiện tại của bảng ko được chọn và tên của file nguồn)
                String str_DuongDanFileDich = (bangHienTai.getTenFile().equalsIgnoreCase(bangPhai.getTenFile())) ?
                    bangTrai.getTenFile() : bangPhai.getTenFile();
                str_DuongDanFileDich += fileDauTien.getName().substring(0,
                        fileDauTien.getName().lastIndexOf(".001"));

                dialog.setTextOfJTextField_FileNguon(str_DuongDanFileDangChon);
                dialog.setTextOfJTextField_ThuMucDich(str_DuongDanFileDich);
            }
        }
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_NoiTapTinActionPerformed

    private void jMenuItem_File_TimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_TimKiemActionPerformed
        // TODO add your handling code here:

        Dialog_TimFile dialog = new Dialog_TimFile(null, true);
        dialog.themEventListener_ClickChuotVaoBangDuyetFile(new EventListener_ClickChuotVaoBangDuyetFile() {
            public void Event_ClickChuotVaoBangDuyetFile_Occurred(String strTenFileDuocChon, int iSoLanClick) {
                bangTrai.capNhatBangDuyetThuMuc(strTenFileDuocChon, jScrollPane_PhanChinh_BangTrai);
                //JOptionPane.showMessageDialog(null, js);
            }
        });
            dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_File_TimKiemActionPerformed

    private void MacLAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MacLAFActionPerformed
        // TODO add your handling code here:
        try{
         UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
         SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
    }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
    }
}//GEN-LAST:event_MacLAFActionPerformed

    private void InfoNodeLAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InfoNodeLAFActionPerformed
        // TODO add your handling code here:
        try{
         UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
         SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
    }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
    }
    }//GEN-LAST:event_InfoNodeLAFActionPerformed

    private void TinyLAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TinyLAFActionPerformed
        // TODO add your handling code here:
        try{
         UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");
         SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
    }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
    }
    }//GEN-LAST:event_TinyLAFActionPerformed

    private void SquarenessLAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SquarenessLAFActionPerformed
        // TODO add your handling code here:
        try{
             UIManager.setLookAndFeel("net.beeger.squareness.SquarenessLookAndFeel");
             SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_SquarenessLAFActionPerformed

    private void smoothLAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smoothLAFActionPerformed
        // TODO add your handling code here:
        try{
             UIManager.setLookAndFeel("smooth.metal.SmoothLookAndFeel");
             SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_smoothLAFActionPerformed

    private void EaSynthLAFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EaSynthLAFActionPerformed
        // TODO add your handling code here:
        try{
             UIManager.setLookAndFeel("com.easynth.lookandfeel.EaSynthLookAndFeel");
             SwingUtilities.updateComponentTreeUI(DoAn_TatalCommande_002App.getApplication().getMainFrame());
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_EaSynthLAFActionPerformed

    private void jComboBox_PhanChinh_BangTraiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_PhanChinh_BangTraiActionPerformed
        // TODO add your handling code here:
        if (!bKhoiTaoXong)//nếu chưa khởi tạo xong thì return
            return;
        if (jComboBox_PhanChinh_BangTrai.getSelectedItem() != null && bangTrai.getTenFile() != null
                && jComboBox_PhanChinh_BangTrai.getSelectedItem().toString().charAt(0) != bangTrai.getTenFile().charAt(0)){
            //Nếu jCombox đã được khởi tạo và ổ đĩa được chọn khác ổ đỉa hiện tại
            bangTrai.capNhatBangDuyetThuMuc(jComboBox_PhanChinh_BangTrai.getSelectedItem().toString(), jScrollPane_PhanChinh_BangTrai);
            jTabbedPane_PhanChinh_BangTrai.setTitleAt(0, bangTrai.getTenFile());
        }
}//GEN-LAST:event_jComboBox_PhanChinh_BangTraiActionPerformed

    private void jMenuItem_File_DiChuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_DiChuyenActionPerformed
        // TODO add your handling code here:
        //Kiểm tra xem người dùng có chọn file nào không?
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file để move!");
            return;
        }
        String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);

        //tạo file các file nguồn và đích
        File f1 = new File(str_DuongDanFileDangChon);
        File f2 = bangHienTai != bangPhai ? new File(bangPhai.getTenFile()) : new File(bangPhai.getTenFile());

        Dialog_Move dialog = new Dialog_Move(null, false);

        dialog.getJTextField_Dich().setText(f2.getAbsolutePath());
        dialog.getJTextField_Nguon().setText(f1.getAbsolutePath());

        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_File_DiChuyenActionPerformed

    private void jMenuItem_TaoFileMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_TaoFileMoiActionPerformed
        // TODO add your handling code here:
        String str_TenFileMoi =
                JOptionPane.showInputDialog("Nhập tên file cần tạo:");
        if (!str_TenFileMoi.contains("\\"))
            str_TenFileMoi = bangHienTai.getTenFile() + str_TenFileMoi;
        try {
            BoQuanLyFile.taoFileMoi(str_TenFileMoi);
            thucThiCommandLine("notepad " + str_TenFileMoi);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jMenuItem_TaoFileMoiActionPerformed
// nen file va nen folder
    private void jMenuItem_ZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_ZipActionPerformed
        // TODO add your handling code here:
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file để zip!");
            return;
        }
        String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);

        String Str_TenMoi = JOptionPane.showInputDialog("Zip file " + str_DuongDanFileDangChon + " thành: ", str_DuongDanFileDangChon + ".zip");
        if (!Str_TenMoi.contains("\\"))
                    Str_TenMoi = bangHienTai.getTenFile() + Str_TenMoi;
        try {
            BoQuanLyFileZip.zipFolder(str_DuongDanFileDangChon, Str_TenMoi);
        } catch (Exception ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem_ZipActionPerformed
// unzip file va folder
    private void jMenuItem_unZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_unZipActionPerformed
        // TODO add your handling code here:
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file *.zip để unzip!");
            return;
        }
        String str_DuongDanFileDangChon = null;
        for (String temp : astr_CacDuongDan)
            if (temp.endsWith(".zip"))
                str_DuongDanFileDangChon = temp;
        if (str_DuongDanFileDangChon == null){
            JOptionPane.showMessageDialog(null, "Xin chọn file *.zip để unzip!");
            return;
        }
        String Str_TenMoi = JOptionPane.showInputDialog("Unzip file " + str_DuongDanFileDangChon + " vào thư mục: ", str_DuongDanFileDangChon.substring(0, str_DuongDanFileDangChon.indexOf(".")) + "\\");
        if (!Str_TenMoi.contains("\\"))
                    Str_TenMoi = bangHienTai.getTenFile() + Str_TenMoi;
        if (!Str_TenMoi.endsWith("\\"))
            Str_TenMoi += "\\";
        try {
            BoQuanLyFileZip.UnZip(str_DuongDanFileDangChon, Str_TenMoi);
        } catch (Exception ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem_unZipActionPerformed

    private void jMenuItem_File_AppendZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_AppendZipActionPerformed
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file để đổi append vào file zip!");
            return;
        }
        String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);

        Dialog_AppendZip dialog = new Dialog_AppendZip(null, false);
        dialog.getJTextField_Append().setText(str_DuongDanFileDangChon);
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem_File_AppendZipActionPerformed

    private void jMenuItem_File_ViewZipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_ViewZipActionPerformed
        // TODO add your handling code here:
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file *.zip để unzip!");
            return;
        }
        String str_DuongDanFileDangChon = null;
        for (String temp : astr_CacDuongDan)
            if (temp.endsWith(".zip"))
                str_DuongDanFileDangChon = temp;
        if (str_DuongDanFileDangChon == null){
            JOptionPane.showMessageDialog(null, "Xin chọn file *.zip để unzip!");
            return;
        }
        String tempfolder = "";
        try {
            tempfolder = BoQuanLyFileZip.outPutTemp(str_DuongDanFileDangChon);
        } catch (IOException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "loi: " + ex.getMessage());
            return;
        }
        if (bangHienTai != bangTrai)
            bangTrai.capNhatBangDuyetThuMuc(tempfolder, jScrollPane_PhanChinh_BangTrai);
        else
            bangPhai.capNhatBangDuyetThuMuc(tempfolder, jScrollPane_PhanChinh_BangPhai);
    }//GEN-LAST:event_jMenuItem_File_ViewZipActionPerformed

    private void jMenuItem_File_DoiTenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_File_DoiTenActionPerformed
        // TODO add your handling code here:
        ArrayList<String> astr_CacDuongDan = bangHienTai.layDuongDanDayDuFileDangDuocChon();
        if (astr_CacDuongDan.size() == 0){
            JOptionPane.showMessageDialog(null, "Xin chọn file để đổi tên!");
            return;
        }
        String str_DuongDanFileDangChon = astr_CacDuongDan.get(0);

        String Str_TenMoi = JOptionPane.showInputDialog("Đổi tên " + str_DuongDanFileDangChon + " thành: ", str_DuongDanFileDangChon);
        if (!Str_TenMoi.contains("\\"))
                    Str_TenMoi = bangHienTai.getTenFile() + Str_TenMoi;
        try {
            BoQuanLyFile.renamefile(str_DuongDanFileDangChon, Str_TenMoi);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DoAn_TatalCommande_002View.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem_File_DoiTenActionPerformed

    private void jButton_ViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ViewActionPerformed
        // TODO add your handling code here:
        jMenuItem_File_ViewZipActionPerformed(evt);
}//GEN-LAST:event_jButton_ViewActionPerformed

    private void jButton_EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EditActionPerformed
        // TODO add your handling code here:
        jMenuItem_Edit_BackActionPerformed(evt);
}//GEN-LAST:event_jButton_EditActionPerformed

    private void jButton_CopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CopyActionPerformed
        // TODO add your handling code here:
        jMenuItem1ActionPerformed(evt);
}//GEN-LAST:event_jButton_CopyActionPerformed

    private void jButton_MoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_MoveActionPerformed
        // TODO add your handling code here:
        jMenuItem_File_DiChuyenActionPerformed(evt);
    }//GEN-LAST:event_jButton_MoveActionPerformed

    private void jButton_NewFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NewFolderActionPerformed
        // TODO add your handling code here:
        jMenuItem_File_NewFolderActionPerformed(evt);
    }//GEN-LAST:event_jButton_NewFolderActionPerformed

    private void jButton_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DeleteActionPerformed
        // TODO add your handling code here:
        jMenuItem_File_XoaActionPerformed(evt);
    }//GEN-LAST:event_jButton_DeleteActionPerformed

    private void jButton_destopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_destopActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jButton_destopActionPerformed

    private void jButton_myDocumentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_myDocumentsActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jButton_myDocumentsActionPerformed

    private void jButton_sosanhfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_sosanhfileActionPerformed
        // TODO add your handling code here:
        jMenuItem_File_SoSanhActionPerformed(evt);
    }//GEN-LAST:event_jButton_sosanhfileActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem EaSynthLAF;
    private javax.swing.JMenuItem InfoNodeLAF;
    private javax.swing.JMenu LaF;
    private javax.swing.JMenuItem MacLAF;
    private javax.swing.JMenuItem SquarenessLAF;
    private javax.swing.JMenuItem TinyLAF;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton_Copy;
    private javax.swing.JButton jButton_Delete;
    private javax.swing.JButton jButton_Edit;
    private javax.swing.JButton jButton_Move;
    private javax.swing.JButton jButton_NewFolder;
    private javax.swing.JButton jButton_ThucThiCommandLine;
    private javax.swing.JButton jButton_View;
    private javax.swing.JButton jButton_destop;
    private javax.swing.JButton jButton_find;
    private javax.swing.JButton jButton_myDocuments;
    private javax.swing.JButton jButton_newFile;
    private javax.swing.JButton jButton_sosanhfile;
    private javax.swing.JButton jButton_unzip;
    private javax.swing.JButton jButton_zip;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_Brief;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_Thumbnail;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_TreeView;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_fullView;
    private javax.swing.JComboBox jComboBox_PhanChinh_BangPhai;
    private javax.swing.JComboBox jComboBox_PhanChinh_BangTrai;
    private javax.swing.JComboBox jComboBox_ThucThiCommandLine;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem_CatTapTin;
    private javax.swing.JMenuItem jMenuItem_Edit_Back;
    private javax.swing.JMenuItem jMenuItem_File_AppendZip;
    private javax.swing.JMenuItem jMenuItem_File_ChinhSuaTapTin;
    private javax.swing.JMenuItem jMenuItem_File_DiChuyen;
    private javax.swing.JMenuItem jMenuItem_File_DoiTen;
    private javax.swing.JMenuItem jMenuItem_File_NewFolder;
    private javax.swing.JMenuItem jMenuItem_File_SoSanh;
    private javax.swing.JMenuItem jMenuItem_File_TimKiem;
    private javax.swing.JMenuItem jMenuItem_File_ViewZip;
    private javax.swing.JMenuItem jMenuItem_File_XemFile;
    private javax.swing.JMenuItem jMenuItem_File_Xoa;
    private javax.swing.JMenuItem jMenuItem_HienThiJavaDoc;
    private javax.swing.JMenuItem jMenuItem_KetNoiFTP;
    private javax.swing.JMenuItem jMenuItem_KetNoiLan;
    private javax.swing.JMenuItem jMenuItem_NoiTapTin;
    private javax.swing.JMenuItem jMenuItem_TaoFileMoi;
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
    private javax.swing.JSeparator jSeparator10;
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
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem smoothLAF;
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
