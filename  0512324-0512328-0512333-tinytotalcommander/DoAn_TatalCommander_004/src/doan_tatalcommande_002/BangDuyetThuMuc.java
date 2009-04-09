package doan_tatalcommande_002;

/*
Definitive Guide to Swing for Java 2, Second Edition
By John Zukowski
ISBN: 1-893115-78-X
Publisher: APress
*/

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import sun.awt.shell.ShellFolder;
/**
 * Lớp đối tượng dùng để hiện thị các file trong một thư mục
 * @author Administrator
 */
public class BangDuyetThuMuc {
  private String _tenThuMucHienHanh;
  private Object[][] _cacDongDuLieu;
  private JScrollPane _scrollPane_HienThiBang;
  private JTable _bangHienThiThuMucHienHanh;
  private TableModel _modelBangHienThi;
  private int _kichThuocIcon = 24;

  public  BangDuyetThuMuc(String myTenFile, JScrollPane myScrollPane ) {
    _tenThuMucHienHanh = myTenFile;
    _scrollPane_HienThiBang = myScrollPane;

//Phần này tham khảo source từ nhiều nguồn trên mạng!!!
    //http://www.java2s.com/Code/Java/Swing-JFC/ColumnSampleTableModel.htm
    _modelBangHienThi = new AbstractTableModel() {
      Object Data[][] = _cacDongDuLieu;

      String columnNames[] = {"Icon", "Name", "Size"};
      public int getColumnCount() {
        return columnNames.length;
      }

            @Override
      public String getColumnName(int column) {
        return columnNames[column];
      }

      public int getRowCount() {
        return _cacDongDuLieu.length;
      }

      public Object getValueAt(int row, int column) {
        return _cacDongDuLieu[row][column];
      }

            @Override
      public Class getColumnClass(int column) {
        return (getValueAt(0, column).getClass());
      }
    };
    _bangHienThiThuMucHienHanh = new JTable(_modelBangHienThi){
          @Override
          /**
           * Them tooltip cho tung cell
           */
          public Component prepareRenderer(TableCellRenderer renderer,int row, int col) {
              Component comp = super.prepareRenderer(renderer, row, col);
              JComponent jcomp = (JComponent)comp;
              if (comp == jcomp) {
                File file = new File(getTenFile());
                jcomp.setToolTipText(file.getPath() + "\\" + (String)getValueAt(row, 1));
              }
              return comp;
           }
        };
    capNhatBangDuyetThuMuc(myTenFile, myScrollPane);
    _bangHienThiThuMucHienHanh.addMouseListener(new MouseAdapter() {
        //Xu ly su kien click vao _bangHienThiThuMucHienHanh
        @Override

         // Xử lý xử kiện người dùng click chuột vào bảng

        public void mousePressed (MouseEvent evt){

            _bangHienThiThuMucHienHanh = (JTable)evt.getComponent();
            int selectedRow = _bangHienThiThuMucHienHanh.getSelectedRow();
            String str_TenFileDuocChon = getTenFile();
            if (!getTenFile().endsWith("\\"))
                str_TenFileDuocChon += "\\";
            str_TenFileDuocChon += _bangHienThiThuMucHienHanh.getValueAt(selectedRow, 1).toString();
            phatSinhSuKien_ClickChuotVaoBangDuyetFile(str_TenFileDuocChon);
            //Nếu là click đơn thì bỏ qua
            if (evt.getButton() != 1 || evt.getClickCount() != 2){
                return;
            }
            //Nếu chọn quay về thư mục cha
            if (_bangHienThiThuMucHienHanh.getValueAt(selectedRow, 1).toString().equals("..")){
                quayVeThuMucCha();
                return;
            }
            //Xác định file đang được chọn
            File file = new File(str_TenFileDuocChon);

            //Xử lý mở file hoặc duyệt vào thư mục con.
            _scrollPane_HienThiBang.setToolTipText(_tenThuMucHienHanh);
            if (file.isFile()){
                String str_PhanMoRong = str_TenFileDuocChon.substring(str_TenFileDuocChon.lastIndexOf(".")
                        , str_TenFileDuocChon.length());
                if (str_PhanMoRong.compareToIgnoreCase(".lnk") == 0){//nếu là file shortcut
                        try {
                            ShellFolder sh = ShellFolder.getShellFolder(file.getAbsoluteFile());
                            //Lấy link và duyệt thư mục trong link
                            file = new File(sh.getLinkLocation().getPath());
                            if (file.isFile())//nếu shortcut dẫn tới 1 file
                                try {
                                    Desktop.getDesktop().open(file);
                                } catch (IOException ex) {
                                    Logger.getLogger(BangDuyetThuMuc.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            else{//nếu dẫn tới thư mục
                                setTenFile(file.getPath());
                                capNhatBangDuyetThuMuc(_tenThuMucHienHanh, _scrollPane_HienThiBang);
                            }
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(BangDuyetThuMuc.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                else //nếu là file
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        Logger.getLogger(BangDuyetThuMuc.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else{
                setTenFile(str_TenFileDuocChon);
                capNhatBangDuyetThuMuc(_tenThuMucHienHanh, _scrollPane_HienThiBang);//new BangDuyetThuMuc(getTenFile(),_scrollPane_HienThiBang);
            }
            //_bangHienThiThuMucHienHanh.requestFocus();
        }
    });
/*
    int SoThuocTinh = 3;
    //Phải lấy giá trị đường dẫn tuyệt đối của file trước phòng trường hợp ổ đĩa hiện tại đang được truy cập sâu
    File fileHienTai = new File(getTenFile()).getAbsoluteFile();
    setTenFile(fileHienTai.getPath());
    myScrollPane.setToolTipText(_tenThuMucHienHanh);
    File files[] = fileHienTai.listFiles();
    if (files == null){
        JOptionPane.showMessageDialog(null, "Không duyệt được thư mục " + _tenThuMucHienHanh);
        return;
    }

    _cacDongDuLieu = new Object[files.length + 1][SoThuocTinh];//thêm một dòng thư mục cha
    int SoLuongFileCon = demSoLuongFileTrongThuMuc(new File(getTenFile()));
    
    //Các thư mục tạm để phân loại file hay thư mục
    Object rowFiles[][] = new Object[SoLuongFileCon][SoThuocTinh];
    int indexFiles = 0;

    Object rowFolders[][] = new Object[files.length - SoLuongFileCon][SoThuocTinh];
    int indexFolders = 0;

    //Các giá trị của KB, MB, GB để xác định size phù hợp của file
    double KB = Math.pow(2, 10);
    double MB = Math.pow(2, 20);
    double GB = Math.pow(2, 30);

    Icon icon = null;

    //Duyệt danh sách các file trong thư mục
    for (int i = 0; i < files.length; i++){
        File file = files[i];
        ShellFolder sf1;
            try {
                //Lấy icon và tên của file
                sf1 = ShellFolder.getShellFolder(file);
                icon = BoCoGianImage.coGianImageIcon(new ImageIcon(sf1.getIcon(true))
                        , _kichThuocIcon, _kichThuocIcon, Image.SCALE_SMOOTH);
                _cacDongDuLieu[i][0] = icon;
                _cacDongDuLieu[i][1] = file.getName();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BangDuyetThuMuc.class.getName()).log(Level.SEVERE, null, ex);
            }

        //Tạo chuổi kích thước file thích hợp và đưa vào mảng tạm file nếu là file
        if (file.isFile()){
            //_cacDongDuLieu[i][2] = file.getName().substring(file.getName().lastIndexOf("."));
            double filesize = file.length();
            if (filesize / GB > 0.9){//GB
                _cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / GB, 3)) + " GB";
            }
            else if (filesize / MB > 0.9){//MB
                _cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / MB, 3)) + " MB";
            }
            else if (filesize / KB > 0.9){//KB
                _cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / KB, 3)) + " KB";
            }
            else
                _cacDongDuLieu[i][2] = String.valueOf(filesize) + " bytes";
            rowFiles[indexFiles++] = _cacDongDuLieu[i];
        }
        else{//Nếu là thư mục thì đưa vào mảng tạm thông tin các thư mục
            _cacDongDuLieu[i][2] = "Folder";
            rowFolders[indexFolders++] = _cacDongDuLieu[i];
        }

    }

    int index = 0;
    File file = new File(_tenThuMucHienHanh);
    if (file.getParentFile() != null){
        index++;
        _cacDongDuLieu = new Object[files.length + 1][SoThuocTinh];//thêm một dòng thư mục cha
        //Tạo dòng thư mục cha

        icon = new ImageIcon(this.getClass().getResource("./resources/Up.png"));

        _cacDongDuLieu[0][0] = icon;
        _cacDongDuLieu[0][1] = "..";
        _cacDongDuLieu[0][2] = "Folder";
    }
    
    //Thêm các thư mục trước các file
    
    for (int i = 0; i < rowFolders.length; i++)
            _cacDongDuLieu[index++] = rowFolders[i];

    for (int i = 0; i < rowFiles.length; i++)
            _cacDongDuLieu[index++] = rowFiles[i];

    

    int Heigth = _kichThuocIcon;//Chieu cao cua icon
    _bangHienThiThuMucHienHanh.setRowHeight(Heigth);
    _bangHienThiThuMucHienHanh.setShowHorizontalLines(false);
    _bangHienThiThuMucHienHanh.setShowVerticalLines(false);
    
    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(0).setMinWidth(35);
    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(0).setMaxWidth(35);

    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(2).setMinWidth(70);
    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(2).setMaxWidth(70);

    _bangHienThiThuMucHienHanh.addRowSelectionInterval(0, 0);
    _scrollPane_HienThiBang.setViewportView(_bangHienThiThuMucHienHanh);
 */
  }
  /**
   * Rút gọn phần thập phân của một số
   * @param i_SoCanLamTron              số cần làm rút gọn
   * @param i_SoLuongChuSoSauDauCham    số lượng số sau dấu "." cần lầy
   * @return                            s   ố đã được làm rút gọn
   * Ví dụ: lamTronPhanThapPhan(3.146,2) return 3.14
   * Cần cải tiến thêm thành hàm làm tròn
   */
  public static double rutGonPhanThapPhan (double i_SoCanLamTron, int i_SoLuongChuSoSauDauCham){
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
  public static int demSoLuongFileTrongThuMuc (File file_ThuMucCanDem){
      int Kq = 0;
      if (file_ThuMucCanDem.isDirectory())
          for (File file : file_ThuMucCanDem.listFiles())
              if (file.isFile())
                Kq++;
      return Kq;
  }
  /**
   * Quay trở về thư mục cha của thư mục hiện hành (nếu có)
   */
  public void quayVeThuMucCha (){
      File file = new File(_scrollPane_HienThiBang.getToolTipText());
      if (file.getParentFile() != null){
            setTenFile(file.getParent());
            _scrollPane_HienThiBang.setToolTipText(_tenThuMucHienHanh);
          capNhatBangDuyetThuMuc(_tenThuMucHienHanh, _scrollPane_HienThiBang);
      }
  }

    /**
     * @return the _tenThuMucHienHanh
     */
    public String getTenFile() {
        return _tenThuMucHienHanh;
    }

    /**
     * @param _tenThuMucHienHanh the _tenThuMucHienHanh to set
     */
    public void setTenFile(String tenFile) {
        this._tenThuMucHienHanh = tenFile;
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
    void phatSinhSuKien_ClickChuotVaoBangDuyetFile(String evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i=0; i<listeners.length; i+=2) {
            if (listeners[i]==EventListener_ClickChuotVaoBangDuyetFile.class) {
                ((EventListener_ClickChuotVaoBangDuyetFile)listeners[i+1]).Event_ClickChuotVaoBangDuyetFile_Occurred(evt);
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
    public JTable getTable() {
        return _bangHienThiThuMucHienHanh;
    }

    /**
     * @param _bangHienThiThuMucHienHanh the _bangHienThiThuMucHienHanh to set
     */
    public void setTable(JTable table) {
        this._bangHienThiThuMucHienHanh = table;
    }
    /**
 * duyệt các thư mục và file con bên trong một thư mục và hiện thị vào _bangHienThiThuMucHienHanh
 * @param str_FileName  đường dẫn thư mục cần duyệt
 * @param myScrollPane  JScrollPane hiện thị bảng
 */
    public void capNhatBangDuyetThuMuc(String str_FileName, JScrollPane myScrollPane){
        _tenThuMucHienHanh = str_FileName;
      _scrollPane_HienThiBang = myScrollPane;
//Phần này tham khảo source từ nhiều nguồn trên mạng!!!
      //http://www.java2s.com/Code/Java/Swing-JFC/ColumnSampleTableModel.htm
   
    int SoThuocTinh = 3;
    myScrollPane.setToolTipText(_tenThuMucHienHanh);
    File files[] = (new File(getTenFile())).listFiles();
    if (files == null){
        JOptionPane.showMessageDialog(null, "Không duyệt được thư mục " + _tenThuMucHienHanh);
        _cacDongDuLieu = new Object[1][SoThuocTinh];
        _cacDongDuLieu[0][0] = new ImageIcon();
        _cacDongDuLieu[0][1] = "";
        _cacDongDuLieu[0][2] = "";
    }
    else{
        _cacDongDuLieu = new Object[files.length + 1][SoThuocTinh];//thêm một dòng thư mục cha
        int SoLuongFileCon = demSoLuongFileTrongThuMuc(new File(getTenFile()));

        //Các thư mục tạm để phân loại file hay thư mục
        Object rowFiles[][] = new Object[SoLuongFileCon][SoThuocTinh];
        int indexFiles = 0;

        Object rowFolders[][] = new Object[files.length - SoLuongFileCon][SoThuocTinh];
        int indexFolders = 0;

        //Các giá trị của KB, MB, GB để xác định size phù hợp của file
        double KB = Math.pow(2, 10);
        double MB = Math.pow(2, 20);
        double GB = Math.pow(2, 30);

        Icon icon = null;

        //Duyệt danh sách các file trong thư mục
        for (int i = 0; i < files.length; i++){
            File file = files[i];
            ShellFolder sf1;
                try {
                    //Lấy icon và tên của file
                    _cacDongDuLieu[i][1] = file.getName();
                    sf1 = ShellFolder.getShellFolder(file);
                    icon = new ImageIcon(sf1.getIcon(true));

                    icon = BoCoGianImage.coGianImageIcon(new ImageIcon(sf1.getIcon(true)),
                    _kichThuocIcon, _kichThuocIcon, Image.SCALE_SMOOTH);

                    _cacDongDuLieu[i][0] = icon;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BangDuyetThuMuc.class.getName()).log(Level.SEVERE, null, ex);
                }

            //Tạo chuổi kích thước file thích hợp và đưa vào mảng tạm file nếu là file
            if (file.isFile()){
                //_cacDongDuLieu[i][2] = file.getName().substring(file.getName().lastIndexOf("."));
                double filesize = file.length();
                if (filesize / GB > 0.9){//GB
                    _cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / GB, 3)) + " GB";
                }
                else if (filesize / MB > 0.9){//MB
                    _cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / MB, 3)) + " MB";
                }
                else if (filesize / KB > 0.9){//KB
                    _cacDongDuLieu[i][2] = String.valueOf(rutGonPhanThapPhan(filesize / KB, 3)) + " KB";
                }
                else
                    _cacDongDuLieu[i][2] = String.valueOf(filesize) + " bytes";
                rowFiles[indexFiles++] = _cacDongDuLieu[i];
            }
            else{//Nếu là thư mục thì đưa vào mảng tạm thông tin các thư mục
                _cacDongDuLieu[i][2] = "Folder";
                rowFolders[indexFolders++] = _cacDongDuLieu[i];
            }

        }

        int index = 0;
        File file = new File(_tenThuMucHienHanh);
        if (file.getParentFile() != null && !file.getPath().startsWith("\\")){
            //Nếu có thư mục cha và không phải đang truy cập mạng
            index++;
            _cacDongDuLieu = new Object[files.length + 1][SoThuocTinh];//thêm một dòng thư mục cha
            //Tạo dòng thư mục cha

            icon = BoCoGianImage.coGianImageIcon(new ImageIcon(this.getClass().getResource("./resources/Back.png")),
                    _kichThuocIcon, _kichThuocIcon, Image.SCALE_SMOOTH);

            _cacDongDuLieu[0][0] = icon;
            _cacDongDuLieu[0][1] = "..";
            _cacDongDuLieu[0][2] = "Folder";
        }

        //Thêm các thư mục trước các file

        for (int i = 0; i < rowFolders.length; i++)
                _cacDongDuLieu[index++] = rowFolders[i];

        for (int i = 0; i < rowFiles.length; i++)
                _cacDongDuLieu[index++] = rowFiles[i];
    }

    _modelBangHienThi = new AbstractTableModel() {
      Object Data[][] = _cacDongDuLieu;

      String columnNames[] = {"Icon", "Name", "Size"};
      public int getColumnCount() {
        return columnNames.length;
      }

            @Override
      public String getColumnName(int column) {
        return columnNames[column];
      }

      public int getRowCount() {
        return _cacDongDuLieu.length;
      }

      public Object getValueAt(int row, int column) {
        return _cacDongDuLieu[row][column];
      }

            @Override
      public Class getColumnClass(int column) {
        return (getValueAt(0, column).getClass());
      }
    };
    if (_bangHienThiThuMucHienHanh == null)
        return;
    _bangHienThiThuMucHienHanh.setModel(_modelBangHienThi);

    int Heigth = _kichThuocIcon;//Chieu cao cua icon
    _bangHienThiThuMucHienHanh.setRowHeight(Heigth);
    _bangHienThiThuMucHienHanh.setShowHorizontalLines(false);
    _bangHienThiThuMucHienHanh.setShowVerticalLines(false);

    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(0).setMinWidth(_kichThuocIcon + 3);
    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(0).setMaxWidth(_kichThuocIcon + 3);

    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(2).setMinWidth(70);
    _bangHienThiThuMucHienHanh.getColumnModel().getColumn(2).setMaxWidth(70);

    _bangHienThiThuMucHienHanh.addRowSelectionInterval(0, 0);
    _scrollPane_HienThiBang.setViewportView(_bangHienThiThuMucHienHanh);
    phatSinhSuKien_ClickChuotVaoBangDuyetFile(str_FileName);
    }

    /**
     * Trả về đường dẫn đầy đủ của file đang được chọn trong bảng hiện thị.
     * @return  đường dẫn
     */
    public ArrayList<String> layDuongDanDayDuFileDangDuocChon (){
        String str_ThuMucHienHanh = getTenFile();
        if (!getTenFile().endsWith("\\"))
            str_ThuMucHienHanh += "\\";
        int[] i_DongDuocChon = _bangHienThiThuMucHienHanh.getSelectedRows();
        ArrayList<String> als_CacFileDuocChon = new ArrayList<String>();
        for(int i : i_DongDuocChon)
            if (i != 0)
                als_CacFileDuocChon.add(str_ThuMucHienHanh +
                        _bangHienThiThuMucHienHanh.getValueAt(i, 1).toString());
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
        capNhatBangDuyetThuMuc(str_DuongDan, _scrollPane_HienThiBang);
        jTabbedPane.setTitleAt(0, str_DuongDan);
    }
}
