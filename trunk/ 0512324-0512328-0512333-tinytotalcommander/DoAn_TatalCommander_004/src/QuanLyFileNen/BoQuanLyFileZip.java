/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package QuanLyFileNen;

import java.io.*;
import java.util.zip.*;
import javax.swing.JOptionPane;
/**
 *Chua cac ham de quan ly file zip: nen va giai nen file
 * @author Dang Thi Phuong Thao
 */
public class BoQuanLyFileZip {
/**
 * Ham nen file
 * Tham khao tai http://www.java-tips.org/java-se-tips/java.util.zip/how-to-create-a-zip-file-with-java.util.zip-pa-2.html
 * @param filename la file can nen
 * @param zipfilename la file sau khi nen
 */
     public void doZip(String filename,String zipfilename) {
        try {
            byte[] buf = new byte[1024];
            FileInputStream fis = new FileInputStream(filename);
            fis.read(buf,0,buf.length);

            CRC32 crc = new CRC32();
            ZipOutputStream s = new ZipOutputStream(
                    (OutputStream)new FileOutputStream(zipfilename));

            s.setLevel(6);

            ZipEntry entry = new ZipEntry(filename);
            entry.setSize((long)buf.length);
            crc.reset();
            crc.update(buf);
            entry.setCrc( crc.getValue());
            s.putNextEntry(entry);
            s.write(buf, 0, buf.length);
            s.finish();
            s.close();
        } catch (Exception e) {
          
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
