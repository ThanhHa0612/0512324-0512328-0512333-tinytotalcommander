package doan_totalcommander_002;


import java.awt.Image;

import javax.swing.ImageIcon;
/**
 * Dùng để co giản các ImageIcon tham khao tại: http://www.java2s.com/Code/Java/2D-Graphics-GUI/Imagescale.htm
 * @author Administrator
 */
public class BoCoGianImage{
  public BoCoGianImage() {
  }
/**
 * Co giản ảnh kiểu ImageIcon
 * @param icon          Ảnh cần co giản (do chủ yếu cần co giản Icon nên để kiểu dữ liệu đưa vào là ImageIcon
 * @param i_Rong        Chiều rộng sau khi co giản
 * @param i_Cao         Chiều cao sau khi co giản
 * @param i_LoaiCoGian  loại co giản (đẹp hay nhanh) ví dụ Image.SCALE_SMOOTH
 * @return              ImageIcon sau khi co giản
 */
  public static ImageIcon coGianImageIcon(ImageIcon icon, int i_Rong, int i_Cao, int i_LoaiCoGian) {
    //ImageIcon icon = new ImageIcon("D:\\08-09 HK2\\Java\\Bai Tap\\DoAn_TatalCommander_004\\src\\doan_tatalcommande_002\\resources\\Back.png");
    Image image = icon.getImage();
    
    Image original = image;
    
    image = original.getScaledInstance(i_Rong, i_Cao, i_LoaiCoGian);
    return new ImageIcon(image);
  }
}