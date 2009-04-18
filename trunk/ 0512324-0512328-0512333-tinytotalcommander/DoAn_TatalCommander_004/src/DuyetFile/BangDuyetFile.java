package DuyetFile;

/*
Definitive Guide to Swing for Java 2, Second Edition
By John Zukowski
ISBN: 1-893115-78-X
Publisher: APress
 */
import QuanLyFile.BoQuanLyFile;
import doan_totalcommander_002.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import sun.awt.shell.ShellFolder;

/**
 * Lớp đối tượng dùng để hiện thị các file trong một thư mục
 * @author Administrator
 */
public class BangDuyetFile {

    private String tenThuMucHienHanh;
    private Object[][] cacDongDuLieu;
    private JScrollPane scrollPane_HienThiBang;
    private DroppableTable bangHienThiThuMucHienHanh;
    private TableModel modelBangHienThi;
    private int kichThuocIcon = 24;

    public BangDuyetFile(String myTenFile, JScrollPane myScrollPane) {
        tenThuMucHienHanh = myTenFile;
        scrollPane_HienThiBang = myScrollPane;

//Phần này tham khảo source từ nhiều nguồn trên mạng!!!
        //http://www.java2s.com/Code/Java/Swing-JFC/ColumnSampleTableModel.htm 08
        modelBangHienThi = new AbstractTableModel() {

            Object Data[][] = cacDongDuLieu;
            String columnNames[] = {"Icon", "Tên", "Cỡ", "Đường dẫn tuyệt đối"};

            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            public int getRowCount() {
                return cacDongDuLieu.length;
            }

            public Object getValueAt(int row, int column) {
                return cacDongDuLieu[row][column];
            }

            @Override
            public Class getColumnClass(int column) {
                return (getValueAt(0, column).getClass());
            }
        };
        bangHienThiThuMucHienHanh = new DroppableTable(getModelBangHienThi(),this) {

            @Override
            /**
             * Them tooltip cho tung cell
             */
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                JComponent jcomp = (JComponent) comp;
                if (comp == jcomp) {
                    File file = new File(getTenFile());
                    jcomp.setToolTipText(file.getPath() + "\\" + (String) getValueAt(row, 1));
                }
                return comp;
            }
        };
        capNhatBangDuyetThuMuc(myTenFile, myScrollPane);
        bangHienThiThuMucHienHanh.addMouseListener(new MouseAdapter() {
            //Xu ly su kien click vao _bangHienThiThuMucHienHanh
            @Override
            // Xử lý xử kiện người dùng click chuột vào bảng
            public void mousePressed(MouseEvent evt) {
                xuLyClickChuot(evt);
            }
        });


//<Chỉnh các thuộc tính cho giao diên của table>
        int Heigth = kichThuocIcon;//Chieu cao cua icon
        bangHienThiThuMucHienHanh.setRowHeight(Heigth);
        bangHienThiThuMucHienHanh.setShowHorizontalLines(false);
        bangHienThiThuMucHienHanh.setShowVerticalLines(false);
        bangHienThiThuMucHienHanh.getColumnModel().getColumn(0).setMinWidth(kichThuocIcon + 3);
        bangHienThiThuMucHienHanh.getColumnModel().getColumn(0).setMaxWidth(kichThuocIcon + 3);

        bangHienThiThuMucHienHanh.getColumnModel().getColumn(2).setMinWidth(70);
        bangHienThiThuMucHienHanh.getColumnModel().getColumn(2).setMaxWidth(70);

        bangHienThiThuMucHienHanh.getColumnModel().getColumn(3).setMinWidth(0);
        bangHienThiThuMucHienHanh.getColumnModel().getColumn(3).setMaxWidth(0);
        bangHienThiThuMucHienHanh.getColumnModel().getColumn(3).setPreferredWidth(0);
//</Chỉnh các thuộc tính cho giao diên của table>

        bangHienThiThuMucHienHanh.addRowSelectionInterval(0, 0);
        //bangHienThiThuMucHienHanh.setAutoCreateRowSorter(true);
        scrollPane_HienThiBang.setViewportView(bangHienThiThuMucHienHanh);

    }
    public void xuLyClickChuot(MouseEvent evt) {
        bangHienThiThuMucHienHanh = (DroppableTable) evt.getComponent();
        int selectedRow = bangHienThiThuMucHienHanh.getSelectedRow();
        String str_TenFileDuocChon = getTenFile();
        phatSinhSuKien_ClickChuotVaoBangDuyetFile(str_TenFileDuocChon, evt.getClickCount());
        if (!getTenFile().endsWith("\\")) {
            str_TenFileDuocChon += "\\";
        }
        if (evt.getButton() != 1 || evt.getClickCount() != 2) {
            return;
        }
            str_TenFileDuocChon += bangHienThiThuMucHienHanh.getValueAt(selectedRow, 1).toString();
        if (bangHienThiThuMucHienHanh.getValueAt(selectedRow, 1).toString().equals("..")) {
            //Nếu là click đơn thì bỏ qua
            quayVeThuMucCha();
            return;
        }
        //Xác định file đang được chọn
        File file = new File(str_TenFileDuocChon);
        //Xử lý mở file hoặc duyệt vào thư mục con.
        getScrollPane_HienThiBang().setToolTipText(tenThuMucHienHanh);
        if (file.isFile()) {
            String str_PhanMoRong = str_TenFileDuocChon.substring(str_TenFileDuocChon.lastIndexOf("."), str_TenFileDuocChon.length());
            if (str_PhanMoRong.compareToIgnoreCase(".lnk") == 0) {
                //nếu là file shortcut
                try {
                    ShellFolder sh = ShellFolder.getShellFolder(file.getAbsoluteFile());
                    //Lấy link và duyệt thư mục trong link
                    file = new File(sh.getLinkLocation().getPath());
                    if (file.isFile()) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            Logger.getLogger(BangDuyetFile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        //nếu dẫn tới thư mục
                        setTenFile(file.getPath());
                        capNhatBangDuyetThuMuc(tenThuMucHienHanh, getScrollPane_HienThiBang());
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BangDuyetFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    Logger.getLogger(BangDuyetFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            setTenFile(str_TenFileDuocChon);
            capNhatBangDuyetThuMuc(tenThuMucHienHanh, getScrollPane_HienThiBang()); //new BangDuyetFile(getTenFile(),_scrollPane_HienThiBang);
        }
        return;
    }

// <editor-fold defaultstate="collapsed" desc="Đã hoàn thành">

    /**
     * Rút gọn phần thập phân của một số
     * @param i_SoCanLamTron              số cần làm rút gọn
     * @param i_SoLuongChuSoSauDauCham    số lượng số sau dấu "." cần lầy
     * @return                            s   ố đã được làm rút gọn
     * Ví dụ: lamTronPhanThapPhan(3.146,2) return 3.14
     * Cần cải tiến thêm thành hàm làm tròn
     */
    public static double rutGonPhanThapPhan(double i_SoCanLamTron, int i_SoLuongChuSoSauDauCham) {
        double Kq = 0;
        String Temp = String.valueOf(i_SoCanLamTron);
        int viTriCuoiCanLay = Math.min(Temp.lastIndexOf(".") + i_SoLuongChuSoSauDauCham - 1, String.valueOf(i_SoCanLamTron).length());
        Temp = Temp.substring(0, viTriCuoiCanLay);
        Kq = Double.parseDouble(Temp);
        return Kq;
    }

    /**
     * Đếm xem trong thư mục có bao nhiêu file (không tính thư mục)
     * @param file_ThuMucCanDem  Thư mục cần đếm
     * @return
     */
    public static int demSoLuongFileTrongThuMuc(File file_ThuMucCanDem) {
        int Kq = 0;
        if (file_ThuMucCanDem.isDirectory()) {
            for (File file : file_ThuMucCanDem.listFiles()) {
                if (file.isFile()) {
                    Kq++;
                }
            }
        }
        return Kq;
    }

    /**
     * Quay trở về thư mục cha của thư mục hiện hành (nếu có)
     */
    public void quayVeThuMucCha() {
        File file = new File(getScrollPane_HienThiBang().getToolTipText());
        if (file.getParentFile() != null) {
            setTenFile(file.getParent());
            getScrollPane_HienThiBang().setToolTipText(tenThuMucHienHanh);
            capNhatBangDuyetThuMuc(tenThuMucHienHanh,getScrollPane_HienThiBang());
        }
    }

    /**
     * @return the _tenThuMucHienHanh
     */
    public String getTenFile() {
        return tenThuMucHienHanh;
    }

    /**
     * @param _tenThuMucHienHanh the _tenThuMucHienHanh to set
     */
    public void setTenFile(String tenFile) {
        this.tenThuMucHienHanh = tenFile;
    }

//Các hàm sau phục vụ cho việc gởi sự kiện click chuột vào bảng ra ngoài (tham khảo từ nhiều nguồn trên mạng)
    //http://www.exampledepot.com/egs/java.util/CustEvent.html
    // Tạo một listener list
    protected javax.swing.event.EventListenerList listenerList =
            new javax.swing.event.EventListenerList();

    /**
     * Phát sinh sử kiện click chuột vào bảng
     * @param evt tham số cho sự kiện click chuột vào bảng (ở đây là tên của file đang được chọn)
     */
    // This private class is used to fire MyEvents
    void phatSinhSuKien_ClickChuotVaoBangDuyetFile(String evt, int iSoLanClick) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == EventListener_ClickChuotVaoBangDuyetFile.class) {
                ((EventListener_ClickChuotVaoBangDuyetFile) listeners[i + 1]).Event_ClickChuotVaoBangDuyetFile_Occurred(evt, iSoLanClick);
            }
        }
    }

    /**
     * Đăng ký sự kiện cho classes
     * @param listener  Sự kiện cần đăng ký
     */
    public void themEventListener_ClickChuotVaoBangDuyetFile(EventListener_ClickChuotVaoBangDuyetFile listener) {
        listenerList.add(EventListener_ClickChuotVaoBangDuyetFile.class, listener);
    }

    /**
     * Gỡ bỏ sự kiện khỏi classes
     * @param listener  Sự kiện cần gỡ bỏ
     */
    public void boEventListener_ClickChuotVaoBangDuyetFile(EventListener_ClickChuotVaoBangDuyetFile listener) {
        listenerList.remove(EventListener_ClickChuotVaoBangDuyetFile.class, listener);
    }

    /**
     * @return the _bangHienThiThuMucHienHanh
     */
    public DroppableTable getTable() {
        return bangHienThiThuMucHienHanh;
    }

    /**
     * @param _bangHienThiThuMucHienHanh the _bangHienThiThuMucHienHanh to set
     */
    public void setTable(DroppableTable table) {
        this.bangHienThiThuMucHienHanh = table;
    }

    /**
     * duyệt các thư mục và file con bên trong một thư mục và hiện thị vào _bangHienThiThuMucHienHanh
     * @param str_FileName  đường dẫn thư mục cần duyệt
     * @param myScrollPane  JScrollPane hiện thị bảng
     */
    public void capNhatBangDuyetThuMuc(String str_FileName, JScrollPane myScrollPane) {
        tenThuMucHienHanh = str_FileName;
        scrollPane_HienThiBang = myScrollPane;
//Phần này tham khảo source từ nhiều nguồn trên mạng!!!
        //http://www.java2s.com/Code/Java/Swing-JFC/ColumnSampleTableModel.htm

        int SoThuocTinh = 4;
        File file = new File(getTenFile()).getAbsoluteFile();
        setTenFile(file.getPath());
        myScrollPane.setToolTipText(file.getPath());
        File files[] = (new File(getTenFile())).listFiles();
        if (files == null) {
            //JOptionPane.showMessageDialog(null, "Không duyệt được thư mục " + _tenThuMucHienHanh);
            cacDongDuLieu = new Object[1][SoThuocTinh];
            cacDongDuLieu[0][0] = new ImageIcon();
            cacDongDuLieu[0][1] = "";
            cacDongDuLieu[0][2] = "";
            cacDongDuLieu[0][3] = "";
        } else {
            cacDongDuLieu = new Object[files.length + 1][SoThuocTinh];//thêm một dòng thư mục cha
            int SoLuongFileCon = demSoLuongFileTrongThuMuc(new File(getTenFile()));

            //Các thư mục tạm để phân loại file hay thư mục
            Object rowFiles[][] = new Object[SoLuongFileCon][SoThuocTinh];
            int indexFiles = 0;

            Object rowFolders[][] = new Object[files.length - SoLuongFileCon][SoThuocTinh];
            int indexFolders = 0;

            //Các giá trị của KB, MB, GB để xác định size phù hợp của file
            double KB = jEnum_CacEnumTrongBai.KB.value();
            double MB = jEnum_CacEnumTrongBai.MB.value();
            double GB = jEnum_CacEnumTrongBai.GB.value();

            Icon icon = null;

            //Duyệt danh sách các file trong thư mục
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                ShellFolder sf1;
                try {
                    //Lấy icon tên của file và đường dẫn của thư mục hiện hành
                    cacDongDuLieu[i][1] = file.getName();
                    cacDongDuLieu[i][3] = file.getAbsolutePath();
                    sf1 = ShellFolder.getShellFolder(file);
                    icon = new ImageIcon(sf1.getIcon(true));

                    icon = BoCoGianImage.coGianImageIcon(new ImageIcon(sf1.getIcon(true)),
                            kichThuocIcon, kichThuocIcon, Image.SCALE_SMOOTH);

                    cacDongDuLieu[i][0] = icon;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BangDuyetFile.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Tạo chuổi kích thước file thích hợp và đưa vào mảng tạm file nếu là file
                if (file.isFile()) {
                    //_cacDongDuLieu[i][2] = file.getName().substring(file.getName().lastIndexOf("."));
                    double filesize = file.length();
                    if (filesize / GB > 0.9) {//GB
                        cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / GB, 3)) + " GB";
                    } else if (filesize / MB > 0.9) {//MB
                        cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / MB, 3)) + " MB";
                    } else if (filesize / KB > 0.9) {//KB
                        cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / KB, 3)) + " KB";
                    } else {
                        cacDongDuLieu[i][2] = String.valueOf(filesize) + " bytes";
                    }
                    rowFiles[indexFiles++] = cacDongDuLieu[i];
                } else {//Nếu là thư mục thì đưa vào mảng tạm thông tin các thư mục
                    cacDongDuLieu[i][2] = "Folder";
                    rowFolders[indexFolders++] = cacDongDuLieu[i];
                }

            }

            int index = 0;
            file = new File(tenThuMucHienHanh);
            if (file.getParentFile() != null && !file.getPath().startsWith("\\")) {
                //Nếu có thư mục cha và không phải đang truy cập mạng
                index++;
                cacDongDuLieu = new Object[files.length + 1][SoThuocTinh];//thêm một dòng thư mục cha
                //Tạo dòng thư mục cha

                icon = BoCoGianImage.coGianImageIcon(new ImageIcon(this.getClass().getResource("../doan_totalcommander_002/resources/Up.png")),
                        kichThuocIcon, kichThuocIcon, Image.SCALE_SMOOTH);

                cacDongDuLieu[0][0] = icon;
                cacDongDuLieu[0][1] = "..";
                cacDongDuLieu[0][2] = "Folder";
                cacDongDuLieu[0][3] = tenThuMucHienHanh;
            }

            //Thêm các thư mục trước các file

            for (int i = 0; i < rowFolders.length; i++) {
                cacDongDuLieu[index++] = rowFolders[i];
            }

            for (int i = 0; i < rowFiles.length; i++) {
                cacDongDuLieu[index++] = rowFiles[i];
            }
        }


        bangHienThiThuMucHienHanh.setModel(getModelBangHienThi());
        //JOptionPane.showMessageDialog(null, bangHienThiThuMucHienHanh.getRowCount());
        bangHienThiThuMucHienHanh.paintImmediately(bangHienThiThuMucHienHanh.getBounds());
        myScrollPane.setViewportView(bangHienThiThuMucHienHanh);
        //System.setProperty("user.dir", getTenFile());
        //phatSinhSuKien_ClickChuotVaoBangDuyetFile(getTenFile(), 2);
    }

    /**
     * Trả về đường dẫn đầy đủ của file đang được chọn trong bảng hiện thị.
     * @return  đường dẫn
     */
    public ArrayList<String> layDuongDanDayDuFileDangDuocChon() {
        String str_ThuMucHienHanh = getTenFile();
        if (!getTenFile().endsWith("\\")) {
            str_ThuMucHienHanh += "\\";
        }
        int[] i_DongDuocChon = bangHienThiThuMucHienHanh.getSelectedRows();
        ArrayList<String> als_CacFileDuocChon = new ArrayList<String>();
        for (int i : i_DongDuocChon) {
            if (cacDongDuLieu[i][1] != "..") {
                als_CacFileDuocChon.add(str_ThuMucHienHanh +
                        bangHienThiThuMucHienHanh.getValueAt(i, 1).toString());
            }
        }
        return als_CacFileDuocChon;
    }

    /**
     * tham khảo tại: http://lwjgl.org/forum/index.php?topic=1956.0
     * duyệt một số thư mục đặc biệt như My Documents, Desktop của user hiện tại (đối với win XP)
     * @param str_TenThuMucDatBiet  tên thư mục muốn duyệt
     * @param jTabbedPane           TabbedPane chứa ScrollPane chứa bảng duyệt thư mục
     */
    public void duyetCacThuMucDatBiet(String str_TenThuMucDatBiet, JTabbedPane jTabbedPane) {
        // TODO add your handling code here:
        String str_DuongDan = System.getProperty("user.home") + "\\" + str_TenThuMucDatBiet;
        capNhatBangDuyetThuMuc(str_DuongDan,getScrollPane_HienThiBang());
        jTabbedPane.setTitleAt(0, str_DuongDan);
    }

    /**
     * @return the scrollPane_HienThiBang
     */
    public JScrollPane getScrollPane_HienThiBang() {
        return scrollPane_HienThiBang;
    }

    /**
     * @return the modelBangHienThi
     */
    public TableModel getModelBangHienThi() {
        return modelBangHienThi;
    }

    /**
     * @param modelBangHienThi the modelBangHienThi to set
     */
    public void setModelBangHienThi(TableModel modelBangHienThi) {
        this.modelBangHienThi = modelBangHienThi;
    }
}//</editor-fold>