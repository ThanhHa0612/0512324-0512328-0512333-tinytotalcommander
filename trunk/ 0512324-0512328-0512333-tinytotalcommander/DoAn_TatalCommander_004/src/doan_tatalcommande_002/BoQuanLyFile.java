/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package doan_tatalcommande_002;

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
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**
 * gồm các hàm hộ trở xử lý file như hiện thị file, doc file...
 * @author Administrator
 */
public class BoQuanLyFile {
    // <editor-fold defaultstate="collapsed" desc="Đã hoàn thành(doc, ghi, hienthi file)">
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
    //// </editor-fold>

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


    public static boolean movefile(String oldFile, String newFile) throws FileNotFoundException, IOException{

      File f1 = new File(oldFile);
      File f2 = new File(newFile);
      boolean result = f1.renameTo(f2);
      if(result == false && JOptionPane.showConfirmDialog(null, "File bị trùng bạn có muốn chép đè lên ko") == 0)
      {
          copyfile(oldFile,newFile);
          f1.delete();
      }
      return result;
    }

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
}

