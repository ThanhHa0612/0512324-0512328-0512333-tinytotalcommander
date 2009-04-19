/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package QuanLyFileNen;

import QuanLyFile.BoQuanLyFile;
import java.io.*;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;
import javax.swing.JOptionPane;

import java.util.*;
import java.util.ArrayList;

/**
 * Chua cac ham de quan ly file zip: nen va giai nen file
 * @author Dang Thi Phuong Thao
 */
public class BoQuanLyFileZip {

    /**
     * Ham nen file
     * Tham khao tai http://www.exampledepot.com/egs/java.util.zip/pkg.html#ZIP
     * @param filename la file can nen
     * @param zipfilename la file sau khi nen
     */
    public static void ZipFile(String[] filenames, String outFilename) {
        byte[] buf = new byte[1024];

        try {
            // Create the ZIP file

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

            // Compress the files
            for (int i = 0; i < filenames.length; i++) {
                FileInputStream in = new FileInputStream(filenames[i]);

                // Add ZIP entry to output stream.
                File file = new File(filenames[i]);
                out.putNextEntry(new ZipEntry(file.getName()));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
/**
 * //http://www.java2s.com/Code/Java/File-Input-Output/unzip.htm
 * giai nen file, folder
 * @param filezip file zip can giai ne
 * @param directory thu muc chua file giai nen
 * @throws java.io.IOException
 */
    public static  void UnZip(String filezip, String directory) throws IOException {
        ZipFile zf = new ZipFile(filezip);
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
           // System.out.println("Unzipping " + ze.getName());
            File file = new File (directory + ze.getName());
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            FileOutputStream fout = new FileOutputStream(directory + ze.getName());
            InputStream in = zf.getInputStream(ze);
            byte[] buf = new byte[1024];
            int c = 0;
            while((c = in.read(buf, 0, 1024)) != -1){
                fout.write(buf, 0, c);
                in.skip(c);
            }

            //for (int c = in.read(); c != -1; c = in.read())
              //  fout.write(c);
        }
    }

/**
 * //http://www.java2s.com/Code/Java/File-Input-Output/UseJavacodetozipafolder.htm
 * nen folder
 * @param srcFolder: folder can nen
 * @param destZipFile: duong dan va ten  file zip
 * @throws java.lang.Exception
 */
     
     public static void zipFolder(String srcFolder, String destZipFile) throws Exception {
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;

        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);

        if (new File(srcFolder).isFile())
            addFileToZip("", srcFolder, zip);
        else
            addFolderToZip("", srcFolder, zip);
        
        zip.flush();
        zip.close();
  }
/**
 * dua file vao file zip,dc su dung boi ham zipFolder
 * @param path: duong dan cua file can them
 * @param srcFile: file dua vao
 * @param zip: file zip
 * @throws java.lang.Exception
 */

  static private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
      throws Exception {

    File folder = new File(srcFile);
    if (folder.isDirectory()) {
      addFolderToZip(path, srcFile, zip);
    } else {
      byte[] buf = new byte[1024];
      int len;
      FileInputStream in = new FileInputStream(srcFile);
      zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
      while ((len = in.read(buf)) > 0) {
        zip.write(buf, 0, len);
      }
      zip.closeEntry();
    }
  }
/**
 * dua folder vao file zip, dc su dung boi ham zipFolder
 * @param path: duong dan cua folder can them
 * @param srcFile: folder dua vao
 * @param zip: file zip
 * @throws java.lang.Exception
 */
  static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
      throws Exception {
    File folder = new File(srcFolder);

    for (String fileName : folder.list()) {
      if (path.equals("")) {
        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
      } else {
        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
      }
    }
  }
  /**
   * Them file vao file zip
   * @param appendFile: file can them vao
   * @param fileZip: file zip
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   * @throws java.lang.Exception
   */
   public static void appendFileToFileZip(String appendFile, String fileZip) throws FileNotFoundException, IOException, Exception{
        String tempFolder = outPutTemp(fileZip);
        BoQuanLyFile boQuanLyFile = new BoQuanLyFile();
        File folder = new File(tempFolder).getCanonicalFile();
        File zip = new File(fileZip);
        boQuanLyFile.copyDirectory(new File(appendFile), new File(tempFolder + "/" + new File(appendFile).getName()), true);
        
        zipFolder(folder.getPath(), fileZip);
  }
   /**
    * Giải nén ra một thư mục tạm. nhớ xóa khi kết thúc chương trình
    * @param zipfile    file zip cần xem
    * @return           đường dẫn của thư mục tạm
    * @throws java.io.IOException
    */
  public static String outPutTemp (String zipfile) throws IOException
  {
      String tempfolder = System.getProperty("java.io.tmpdir");
      String timenow = String.valueOf(new File (zipfile).getName());
      tempfolder += timenow + "/";
      new File(tempfolder).deleteOnExit();
      UnZip(zipfile, tempfolder);
      return tempfolder;
  }
}


