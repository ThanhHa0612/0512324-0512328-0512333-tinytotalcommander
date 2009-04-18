package DuyetFile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;


/**
 *
 *Tham khao http://www.java2s.com/Code/Java/Swing-JFC/FileTreewithPopupMenu.htm
 * @author Dang Thi Phuong Thao
 */
public class CayDuyetFile {

    /**
     * ICON_CONPUTER la icon hinh compute
     */
    public final ImageIcon ICON_COMPUTER =
            new ImageIcon(this.getClass().getResource(".\\resources\\Tree_computer.png"));
    /**
     * ICON_DISK la hinh icon disk
     */
    public final ImageIcon ICON_DISK =
            new ImageIcon(this.getClass().getResource(".\\resources\\Tree_disk.png"));
    /**
     * m_tree la 1 tree, duoc su dung de duyet va dua file vao
     */
    protected JTree m_tree;
    /**
     *  m_model giu kieu cua node goc, la kieu  ma m_tree su dung
     */
    protected DefaultTreeModel m_model;
    /**
     *  m_display
     */
//  protected JTextField m_display;
    /**
     *  m_popup luu lai nhung action de sao nay dua vao memu
     */
    protected JPopupMenu m_popup;
    /**
     *  m_action luu tru action dua vao m_popup
     */
    protected Action m_action;
    /**
     *  m_clickedPath luu nhung treepath da dc kick qua
     */
    private TreePath m_clickedPath;

    private DirSelectionListener dirSelectionListener;
    /**
     * ham khoi tao CayDuyetFile voi thong so vao la 1 jcsrollPane s
     */
    public CayDuyetFile(JScrollPane s) {

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                new IconData(ICON_COMPUTER, null, "Computer"));

        DefaultMutableTreeNode node;
        File[] roots = File.listRoots();
        for (int k = 0; k < roots.length; k++) {
            node = new DefaultMutableTreeNode(new IconData(ICON_DISK,
                    null, new FileNode(roots[k])));
            top.add(node);
            node.add(new DefaultMutableTreeNode(new Boolean(true)));
        }
        m_model = new DefaultTreeModel(top);
        m_tree = new JTree(m_model);

        m_tree.putClientProperty("JTree.lineStyle", "Angled");
        TreeCellRenderer renderer = new IconCellRenderer();
        m_tree.setCellRenderer(renderer);

        m_tree.addTreeExpansionListener(new DirExpansionListener());

        dirSelectionListener = new DirSelectionListener();
        m_tree.addTreeSelectionListener(dirSelectionListener);

        
        m_tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_tree.setShowsRootHandles(true);
        m_tree.setEditable(false);
        m_tree.setScrollsOnExpand(true);
        s.getViewport().add(m_tree);

        // m_display = new JTextField();
        // m_display.setEditable(false);

        m_popup = new JPopupMenu();
        m_action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getM_clickedPath() == null) {
                    return;
                }
                if (m_tree.isExpanded(getM_clickedPath())) {
                    m_tree.collapsePath(getM_clickedPath());
                } else {
                    m_tree.expandPath(getM_clickedPath());
                }
            }
        };
        m_popup.add(m_action);
        m_popup.addSeparator();

        Action a1 = new AbstractAction("Delete") {

            @Override
            public void actionPerformed(ActionEvent e) {
                m_tree.repaint();
                JOptionPane.showMessageDialog(null,
                        "Delete option is not implemented",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        m_popup.add(a1);

        Action a2 = new AbstractAction("Rename") {

            public void actionPerformed(ActionEvent e) {
                m_tree.repaint();
                JOptionPane.showMessageDialog(null,
                        "Rename option is not implemented",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        m_popup.add(a2);
        m_tree.add(m_popup);
        m_tree.addMouseListener(new PopupTrigger());

        WindowListener wndCloser = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
    //addWindowListener(wndCloser);

    //setVisible(true);
    }

    public int capNhatCay(String path) {
        String[] strCacDuongDan = path.split("\\\\");

        String strDuongDanHienTai = "";
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) m_model.getRoot();
        File file = null;
        //Đầu tiên tìm node của phân vùng (hơi khác biệt vì dùng hàm listRoors)
        for (int i = 0; i < File.listRoots().length; i++){
            file = File.listRoots()[i];
            if (file.getAbsolutePath().contains(strCacDuongDan[0])){//tìm đúng phân vùng
                node = (DefaultMutableTreeNode) m_model.getChild(node, i);
                strDuongDanHienTai += strCacDuongDan[0];
                break;
            }
        }

        int length = strCacDuongDan.length;
        //Nếu là quay về thư mục cha thì loại bỏ 2 cấp (.. và thư mục con gần nhất)
        if (strCacDuongDan[length - 1] == "..")
            length -= 2;

        //Lần lượt tiến sâu theo từng node (các đường dẫn tiếp theo)
        for (int i = 1; i < length; i++){
            //Lấy đường dẫn tiếp theo
            String strDuongDanTiepTheo = strCacDuongDan[i];

            //Duyệt xem stt của đường dẫn tiếp theo trong thư mục cha
            file = new File(strDuongDanHienTai).getAbsoluteFile();
            int j = 0;
            for (File filecon : file.listFiles())
            {
                if (!filecon.isDirectory())
                    continue;
                //Tìm thư mục con thì cập nhật node và đường dẫn hiện tại
                if (filecon.getName().equalsIgnoreCase(strDuongDanTiepTheo)){
                    node = (DefaultMutableTreeNode) m_model.getChild(node, j);
                    strDuongDanHienTai += "\\" + strDuongDanTiepTheo;
                    break;
                }
                else //tăng số thứ tự lên
                    j++;
            }
        }


        final FileNode filenode = new FileNode(file);
        final DefaultMutableTreeNode finalNode = node;
        //cập nhật cây
        TreePath tf = new TreePath(m_model.getPathToRoot(finalNode));
        if (m_clickedPath != tf)
            m_tree.fireTreeExpanded(tf);

        return m_tree.getSelectionRows()[0] * m_tree.getRowHeight();
    }

    /**
     * lay ra 1 node tren tree
     * @param path la duong dan treepath
     * @return 1 node
     */
    DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) (path.getLastPathComponent());
    }

    /**
     *
     * @param node
     * @return
     */
    FileNode getFileNode(DefaultMutableTreeNode node) {
        if (node == null) {
            return null;
        }
        Object obj = node.getUserObject();
        if (obj instanceof IconData) {
            obj = ((IconData) obj).getObject();
        }
        if (obj instanceof FileNode) {
            return (FileNode) obj;
        } else {
            return null;
        }
    }
//Các hàm sau phục vụ cho việc gởi sự kiện click chuột vào bảng ra ngoài (tham khảo từ nhiều nguồn trên mạng)
    //http://www.exampledepot.com/egs/java.util/CustEvent.html
    // Tạo một listener list
    protected javax.swing.event.EventListenerList listenerList =
            new javax.swing.event.EventListenerList();

    /**
     * Phát sinh sử kiện click chuột vào tree
     * @param evt tham số cho sự kiện click chuột vào tree (ở đây là tên của file đang được chọn)
     */
    // This private class is used to fire MyEvents
    void initEvent_ClickChuotVaoCayDuyetFile(String evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == EventListener_ClickChuotVaoCayDuyetFile.class) {
                ((EventListener_ClickChuotVaoCayDuyetFile) listeners[i + 1]).Event_ClickChuotVaoCayDuyetFile_Occurred(evt);
            }
        }
    }

    /**
     * Đăng ký sự kiện cho classes
     * @param listener  Sự kiện cần đăng ký
     */
    public void addEventListener_ClickChuotVaoCayDuyetFile(EventListener_ClickChuotVaoCayDuyetFile listener) {
        listenerList.add(EventListener_ClickChuotVaoCayDuyetFile.class, listener);
    }

    /**
     * Gỡ bỏ sự kiện khỏi classes
     * @param listener  Sự kiện cần gỡ bỏ
     */
    public void delEventListener_ClickChuotVaoCayDuyetFile(EventListener_ClickChuotVaoCayDuyetFile listener) {
        listenerList.remove(EventListener_ClickChuotVaoCayDuyetFile.class, listener);
    }

    /**
     * cap nhat lai cay duyet file sao khi click chuot vao bang
     * @param tree la cay duyet file hien tai
     * @param st_file file dang dc chon
     */
    public void capNhatCay(CayDuyetFile tree, String st_file) {
        tree.initEvent_ClickChuotVaoCayDuyetFile(st_file);
        tree.addEventListener_ClickChuotVaoCayDuyetFile(new EventListener_ClickChuotVaoCayDuyetFile() {

            public void Event_ClickChuotVaoCayDuyetFile_Occurred(String str_fileduocchon) {
            }
        });

    }

    /**
     * @return the dirSelectionListener
     */
    public DirSelectionListener getDirSelectionListener() {
        return dirSelectionListener;
    }

    /**
     * @param dirSelectionListener the dirSelectionListener to set
     */
    public void setDirSelectionListener(DirSelectionListener dirSelectionListener) {
        this.dirSelectionListener = dirSelectionListener;
    }

    /**
     * @return the m_clickedPath
     */
    public TreePath getM_clickedPath() {
        return m_clickedPath;
    }

    /**
     * Lop PopupTrigger
     */
// NEW
    class PopupTrigger extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int x = e.getX();
                int y = e.getY();
                TreePath path = m_tree.getPathForLocation(x, y);
                if (path != null) {
                    if (m_tree.isExpanded(path)) {
                        m_action.putValue(Action.NAME, "Collapse");
                    } else {
                        m_action.putValue(Action.NAME, "Expand");
                    }
                    m_popup.show(m_tree, x, y);
                    m_clickedPath = path;
                }
            }
        }
    }

    // Make sure expansion is threaded and updating the tree model
    // only occurs within the event dispatching thread.
    /**
     * Lop DirExpansionListener
     */
    class DirExpansionListener implements TreeExpansionListener {

        public void treeExpanded(TreeExpansionEvent event) {
            final DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            final FileNode fnode = getFileNode(node);

            Thread runner = new Thread() {

                public void run() {
                    if (fnode != null && fnode.expand(node)) {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                m_model.reload(node);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                }
            };
            runner.start();
            m_tree.setSelectionPath(event.getPath());
        }

        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    /**
     * Lop Dir SelectionListerner
     */
    class DirSelectionListener
            implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent event) {
            DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            FileNode fnode = getFileNode(node);
            if (fnode != null) {
                //  m_display.setText(fnode.getFile().
                ///  getAbsolutePath());
                initEvent_ClickChuotVaoCayDuyetFile(fnode.getFile().getAbsolutePath());
            }
        //  else
        //  m_display.setText("");
        }
    }
}
/**
 * Lop IconCellRenderer
 * @author Dang Thi Phuong Thao
 */
class IconCellRenderer
        extends JLabel
        implements TreeCellRenderer {

    protected Color m_textSelectionColor;
    protected Color m_textNonSelectionColor;
    protected Color m_bkSelectionColor;
    protected Color m_bkNonSelectionColor;
    protected Color m_borderSelectionColor;
    protected boolean m_selected;

    public IconCellRenderer() {
        super();
        m_textSelectionColor = UIManager.getColor(
                "Tree.selectionForeground");
        m_textNonSelectionColor = UIManager.getColor(
                "Tree.textForeground");
        m_bkSelectionColor = UIManager.getColor(
                "Tree.selectionBackground");
        m_bkNonSelectionColor = UIManager.getColor(
                "Tree.textBackground");
        m_borderSelectionColor = UIManager.getColor(
                "Tree.selectionBorderColor");
        setOpaque(false);
    }

    public Component getTreeCellRendererComponent(JTree tree,
            Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        setText(obj.toString());

        if (obj instanceof Boolean) {
            setText("Retrieving data...");
        }

        if (obj instanceof IconData) {
            IconData idata = (IconData) obj;
            if (expanded) {
                setIcon(idata.getExpandedIcon());
            } else {
                setIcon(idata.getIcon());
            }
        } else {
            setIcon(null);
        }

        setFont(tree.getFont());
        setForeground(sel ? m_textSelectionColor : m_textNonSelectionColor);
        setBackground(sel ? m_bkSelectionColor : m_bkNonSelectionColor);
        m_selected = sel;
        return this;
    }

    public void paintComponent(Graphics g) {
        Color bColor = getBackground();
        Icon icon = getIcon();

        g.setColor(bColor);
        int offset = 0;
        if (icon != null && getText() != null) {
            offset = (icon.getIconWidth() + getIconTextGap());
        }
        g.fillRect(offset, 0, getWidth() - 1 - offset,
                getHeight() - 1);

        if (m_selected) {
            g.setColor(m_borderSelectionColor);
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }

        super.paintComponent(g);
    }
}

/**
 * Lop IconData mo ta mot doi tuong chung cho cac node
 * @author Dang Thi Phuong Thao
 */
class IconData {

    protected Icon m_icon;
    protected Icon m_expandedIcon;
    protected Object m_data;

    public IconData(Icon icon, Object data) {
        m_icon = icon;
        m_expandedIcon = null;
        m_data = data;
    }

    public IconData(Icon icon, Icon expandedIcon, Object data) {
        m_icon = icon;
        m_expandedIcon = expandedIcon;
        m_data = data;
    }

    public Icon getIcon() {
        return m_icon;
    }

    public Icon getExpandedIcon() {
        return m_expandedIcon != null ? m_expandedIcon : m_icon;
    }

    public Object getObject() {
        return m_data;
    }

    public String toString() {
        return m_data.toString();
    }
}

/**
 * FileNode la 1 doi tuong file, la mot node tren tree
 * @author Dang Thi Phuong Thao
 */
class FileNode {

    public final ImageIcon ICON_FOLDER =
            new ImageIcon(this.getClass().getResource(".\\resources\\Tree_folder.png"));
    public final ImageIcon ICON_EXPANDEDFOLDER =
            new ImageIcon(".\\resources\\Tree_expandedfolder.png");
    protected File m_file;

    public FileNode(File file) {
        m_file = file;
    }

    public File getFile() {
        return m_file;
    }

    /**
     * Lay ten file va chuyen sang dang chuoi.
     * @return
     */
    public String toString() {
        return m_file.getName().length() > 0 ? m_file.getName() : m_file.getPath();
    }

    /**
     * Kiem tra xem 1 node co the co expand ra node con ko
     * @param parent : node cha
     * @return: true neu expand dc, false neu expand ko dc
     */
    public boolean expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag =
                (DefaultMutableTreeNode) parent.getFirstChild();
        if (flag == null) // No flag
        {
            return false;
        }
        Object obj = flag.getUserObject();
        if (!(obj instanceof Boolean)) {
            return false;      // Already expanded
        }
        parent.removeAllChildren();  // Remove Flag

        File[] files = listFiles();
        if (files == null) {
            return true;
        }

        Vector v = new Vector();

        for (int k = 0; k < files.length; k++) {
            File f = files[k];
            if (!(f.isDirectory())) {
                continue;
            }

            FileNode newNode = new FileNode(f);

            boolean isAdded = false;
            for (int i = 0; i < v.size(); i++) {
                FileNode nd = (FileNode) v.elementAt(i);
                if (newNode.compareTo(nd) < 0) {
                    v.insertElementAt(newNode, i);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                v.addElement(newNode);
            }
        }

        for (int i = 0; i < v.size(); i++) {
            FileNode nd = (FileNode) v.elementAt(i);
            IconData idata = new IconData(ICON_FOLDER,
                    ICON_EXPANDEDFOLDER, nd);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(idata);
            parent.add(node);

            if (nd.hasSubDirs()) {
                node.add(new DefaultMutableTreeNode(
                        new Boolean(true)));
            }
        }

        return true;
    }

    /**
     * Kiem tra xem trong cac file co thu muc con ko
     * @return true neu co thu muc con, false neu ko co
     */
    public boolean hasSubDirs() {
        File[] files = listFiles();
        if (files == null) {
            return false;
        }
        for (int k = 0; k < files.length; k++) {
            if (files[k].isDirectory()) {
                return true;
            }
        }
        return false;
    }

    public int compareTo(FileNode toCompare) {
        return m_file.getName().compareToIgnoreCase(
                toCompare.m_file.getName());
    }

    /**
     * lay ra danh sach cac file neu co
     * neu ko co thi thong bao loi
     * @return danh sach cac file co trong m_file
     */
    protected File[] listFiles() {
        if (!m_file.isDirectory()) {
            return null;
        }
        try {
            return m_file.listFiles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error reading directory " + m_file.getAbsolutePath(),
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}