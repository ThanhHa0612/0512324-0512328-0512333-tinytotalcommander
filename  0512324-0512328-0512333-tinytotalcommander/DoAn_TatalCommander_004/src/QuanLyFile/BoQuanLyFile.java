/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QuanLyFile;

import doan_totalcommander_002.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**
 * gồm các hàm hộ trở xử lý file như hiện thị file, doc file...
 * @author Administrator
 */
public class BoQuanLyFile extends JComponent{
    // <editor-fold defaultstate="collapsed" desc="Đã hoàn thành">
    /**
     *
     * Hiện thị binary code của file vào jScrollPane
     * @param str_DuongDanFile      đường dẫn file cần hiện thị
     * @param jScrollPane           ScrollPane chứa textpane hiện thị
     * @param enumLoaiTruyXuat      Loại hiện thị ví dụ: jEnum_CacEnumTrongBai.SuaFile
     */
    public static void hienThiFile (String str_DuongDanFile, JTextPane jTextPane, jEnum_CacEnumTrongBai enumLoaiTruyXuat)
            throws IOException    {
        File file = new File(str_DuongDanFile);
        if (!file.exists()){
            JOptionPane.showMessageDialog(null, "Không tìm thấy file " + str_DuongDanFile);
            return;
        }
        jTextPane.setText(docFile(str_DuongDanFile));
        //Set edit cho textpane phù hợp
        jTextPane.setEditable(enumLoaiTruyXuat == jEnum_CacEnumTrongBai.SuaFile);
}
    /**
     * Đọc 1 file và trả về một String dữ liệu trong file
     * @param str_DuongDan      đường dẫn đến file cần đọc
     * @return              dữ liệu trả về kiển string
     * @throws java.io.IOException
     */
    private static String docFile(String str_DuongDan)throws java.io.IOException{
        StringBuffer strbuff_DuLieu = new StringBuffer(1000);
        BufferedReader buffReader = new BufferedReader(new FileReader(str_DuongDan));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=buffReader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            strbuff_DuLieu.append(readData);
            buf = new char[1024];
        }
        buffReader.close();
        return strbuff_DuLieu.toString();
    }
    /**
     * Ghi một string vào file
     * @param str_DuongDan      đường dẫn file cần ghi
     * @param str_DuLieu        dữ liệu cần ghi
     * @throws java.io.IOException
     */
    public static void  ghiFile(String str_DuongDan, String str_DuLieu)throws java.io.IOException{

            BufferedWriter buffWriter = new BufferedWriter(new FileWriter(str_DuongDan));

            buffWriter.write(str_DuLieu);

            buffWriter.close();
        }
    /**
     *
     * copy nội dung của file nguồn sng9 file đích
     * @param srFile    đường dẫn file nguồn
     * @param dtFile    đường dẫn file đích
     * 
     */
     public static void copyfile(String srFile, String dtFile) throws FileNotFoundException, IOException{
        try{
          File f1 = new File(srFile);
          File f2 = new File(dtFile);
          InputStream in = new FileInputStream(f1);

          //For Append the file.
    //      OutputStream out = new FileOutputStream(f2,true);

          //For Overwrite the file.
          OutputStream out = new FileOutputStream(f2);

          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0){
            out.write(buf, 0, len);
          }
          in.close();
          out.close();
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
          System.exit(0);
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }
     }

     /**
     *
     * copy folder,file sang đường dẫn khác
     * @param srcPath    đường dẫn file,folder nguồn
     * @param dstpath    đường dẫn file,folder đích
     *
     */
     public void copyDirectory(File srcPath, File dstPath,boolean xoatatca)
                               throws IOException{
        JOptionPane overwritePrompt = new JOptionPane();
            Object[] options = {"Yes","Yes to all","No"};
        if (srcPath.isDirectory()){
            if (!dstPath.exists()){
                dstPath.mkdir();
            }
            //tao thu muc nguon trong thu muc dich
            String namedir = srcPath.getName();
            dstPath = new File (dstPath + "/" + namedir);

            
            if (dstPath.exists() && xoatatca == false){
               int n = JOptionPane.showOptionDialog(overwritePrompt,
                                "Already exists. Overwrite?",
                                "Overwrite All File?",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                options,
                                options[2]);
               if(n ==0 )
                    dstPath.delete();
               if(n ==1){
                    dstPath.delete();
                    xoatatca = true;
               }
               if(n==2){
                   return;
               }

            }
            dstPath.mkdir();
            String files[] = srcPath.list();
            //de quy
            for(int i = 0; i < files.length; i++){
                copyDirectory(new File(srcPath, files[i]),
                         new File(dstPath, files[i]),xoatatca);

            }

        }
        //neu la file
        else{
          if(!srcPath.exists()){//ko tồn tại
                return;
          }
          //da co file o thu muc dich
          
            else{ //chua co
              

                if (dstPath.exists() && xoatatca == false){
               int n = JOptionPane.showOptionDialog(overwritePrompt,
                                "Already exists. Overwrite?",
                                "Overwrite All File?",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE,
                                null,
                                options,
                                options[2]);
               if(n ==0 )
                    dstPath.delete();
               if(n ==1){
                    dstPath.delete();
                    xoatatca = true;
               }
               if(n==2){
                   return;
               }

            }

                this.firePropertyChange("DangCopy", "", srcPath.getCanonicalPath());
                InputStream in = new FileInputStream(srcPath.getAbsoluteFile());
                OutputStream out = new FileOutputStream(dstPath.getAbsoluteFile());
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }
    

     public static void removeDirectory(File srcPath,boolean xoatatca)
                               throws IOException{
      JOptionPane overwritePrompt = new JOptionPane();
        Object[] options = {"Có","Xoá tất cả","Không"};


        if(xoatatca==false)
        {
            int n = JOptionPane.showOptionDialog(overwritePrompt,
                                    "Bạn Thật sự muốn xóa?",
                                    "Xóa tất cả các File?",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    options,
                                    options[2]);

           if(n ==1){
                xoatatca = true;
           }
           if(n==2){
               return;
           }
        }



        if (srcPath.isDirectory()){

            String files[] = srcPath.list();
            //de quy
            for(int i = 0; i < files.length; i++){
                removeDirectory(new File(srcPath, files[i]),xoatatca);

            }
            srcPath.delete();

        }
        //neu la file
        else{
          if(!srcPath.exists()){//ko tồn tại
                return;
          }

            else{

            srcPath.delete();
            }
        }
    }


    /**
     * di chuyển file đến thư vị trí khác
     * @param oldFile       đường dẫn file nguồn
     * @param newFile       đường dẫn file địch
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public boolean movefile(String oldFile, String newFile) throws FileNotFoundException, IOException{

      File f1 = new File(oldFile);
      File f2 = new File(newFile);
      boolean result = f1.renameTo(f2);
      if(result == false && JOptionPane.showConfirmDialog(null, "File bị trùng bạn có muốn chép đè lên ko") == 0)
      {
          copyDirectory(f1,f2,false);
          f1.delete();
      }
      return result;
    }
    /**
 * đổi tên file
 * @param oldFile       tên file nguồn
 * @param newFile       tên file đích
 * @return
 * @throws java.io.FileNotFoundException
 * @throws java.io.IOException
 */
    public static boolean renamefile(String oldFile, String newFile) throws FileNotFoundException, IOException{

      File f1 = new File(oldFile);
      File f2 = new File(newFile);
      boolean result = f1.renameTo(f2);
      if(result == false)
      {
           JOptionPane.showMessageDialog(null, "File bị trùng tên");
      }
      return result;
    }
    /**
     * Cắt nhỏ 1 file thành nhiều file  
     * @param str_DuongDanFileNguon     file cần cắt
     * @param str_DuongDanThuMucDich    thư mục chứa các file sau khi cắt   
     * @param l_KichThuoc               kích thước mỗi file sau khi cắt
     * @return
     */
    public boolean catFile (String str_DuongDanFileNguon, String str_DuongDanThuMucDich, long l_KichThuoc) throws IOException{
        File fileNguon = new File(str_DuongDanFileNguon);
        String str_DuongDanChungCuaFileDich = str_DuongDanThuMucDich + "\\" + fileNguon.getName() + ".";
        int i_SttFileHienTai = 1001;

        long l_KichThuocDaCopy = 0;
        int oldValue = (int)(fileNguon.length() / jEnum_CacEnumTrongBai.MB.value());

        while (l_KichThuocDaCopy < fileNguon.length()){
            int newValue = (int)(l_KichThuocDaCopy / jEnum_CacEnumTrongBai.MB.value());
            if (newValue % 5 == 0)
                this.firePropertyChange("KichThuocDaCat", oldValue, newValue);
            //Tạo file có dạng filenguon.xxx
            //do stt hiện tại có 1001 để khi chuyển thành chuổi có thể giữ được những số 0 đầu
            //nên cần cắt chuổi từ vị trí 1 - 4
            File fileDichHienTai = new File(str_DuongDanChungCuaFileDich +
                    String.valueOf(i_SttFileHienTai).substring(1, 4));

            fileDichHienTai.deleteOnExit();//xóa trước khi tạo file mới
            copy1phanfile(str_DuongDanFileNguon, fileDichHienTai.getPath()
                    , l_KichThuocDaCopy, false, l_KichThuoc);
            //Copy phần file vào file đích hiện tại
            //Tăng số stt file hiện tại và cập nhật kích thước đã copy
            i_SttFileHienTai++;
            l_KichThuocDaCopy += l_KichThuoc;
        }
        return true;
    }
    /**
     * Copy 1 phần của file
     * @param srFile                file nguồn
     * @param dtFile                file đích
     * @param l_ViTriBatDauCopy     bắt đầu từ vị trí này của file
     * @param l_KichThuocCanCopy    kích thước phần cần copy
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void copy1phanfile(String srFile, String dtFile, long l_ViTriBatDauCopy
            , boolean b_NoiThemVao, long l_KichThuocCanCopy) throws FileNotFoundException, IOException{
        try{
          File f1 = new File(srFile);
          File f2 = new File(dtFile);
          InputStream in = new FileInputStream(f1);
          in.skip(l_ViTriBatDauCopy);

          //For Append the file.
    //      OutputStream out = new FileOutputStream(f2,true);

          //For Overwrite the file.
          OutputStream out = new FileOutputStream(f2, b_NoiThemVao);
          
          byte[] buf = new byte[1024];
          int len;
          long l_KichThuocDaCopy = 0;
          while ((len = in.read(buf)) > 0 && l_KichThuocDaCopy < l_KichThuocCanCopy){
            out.write(buf, 0, len);
            l_KichThuocDaCopy += len;
          }
          in.close();
          out.close();
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
          System.exit(0);
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }
     }
    //// </editor-fold>
    public static int ghepFile (String str_FileDauTien, String str_FileDich) throws FileNotFoundException, IOException{
        File fileHienTai = new File (str_FileDauTien);
        String str_FileHienTai = str_FileDauTien.substring(0, str_FileDauTien.lastIndexOf(".") + 1);
        int i_SttFileHienTai = 1001;
        
        //Xóa nếu file hiện tại đã có
        new File(str_FileDich).deleteOnExit();
        while (fileHienTai.isFile()){
            //copy thêm file hiện tại vào file đích
            copy1phanfile(fileHienTai.getPath(), str_FileDich, 0, true, fileHienTai.length());
            //Tăng số thứ tự file hiện tại
            i_SttFileHienTai++;
            //Cập nhât file hiện tại
            fileHienTai = new File(str_FileHienTai + String.valueOf(i_SttFileHienTai).substring(1, 4));
        }
        return i_SttFileHienTai - 1000;
    }
}

