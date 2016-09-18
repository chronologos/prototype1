import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SubimageTest {
  public static void main(String[] args) {
    try {
      File f = new File("hs-2006-10-a-full_jpg.jpg"); 
      boolean exists = f.exists(); 
      BufferedImage bufferedImage = ImageIO.read(f);
      BufferedImage subimage = bufferedImage.getSubimage(1, 1, 100, 100);
      DataBuffer buff = bufferedImage.getRaster().getDataBuffer();
      DataBuffer buff2 = subimage.getRaster().getDataBuffer();
      int bytes = buff.getSize() * DataBuffer.getDataTypeSize(buff.getDataType()) / 8;
      int bytes2 = buff2.getSize() * DataBuffer.getDataTypeSize(buff.getDataType()) / 8;
      System.out.println(buff == buff2);
      System.out.println(bytes);
      System.out.println(bytes2);
    }
    catch (IOException exc) {
      exc.printStackTrace();
      System.exit(1);
    }
  }
}
