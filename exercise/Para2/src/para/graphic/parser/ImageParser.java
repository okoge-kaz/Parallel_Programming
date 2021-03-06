package para.graphic.parser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import para.graphic.shape.Attribute;
import para.graphic.shape.Image;

class ImageParser implements ShapeParser {
  private static final Pattern pattern = Pattern.compile("[\\w\\+/=&]+");
  private static final int codeand = "&".hashCode();
  private byte[] bisdata;
  private ByteArrayInputStream bis;
  private Base64.Decoder decoder;

  ImageParser() {
    bisdata = new byte[Image.MAXWIDTH * Image.MAXHEIGHT * 4 * 78 * 4 / (76 * 3) + 128];
    bis = new ByteArrayInputStream(bisdata);
    decoder = Base64.getDecoder();
  }

  @Override
  public Image parse(Scanner s, int id) {
    int x = s.nextInt();
    int y = s.nextInt();
    BufferedImage img;
    try {
      img = scanImage(s);
    } catch (IOException e) {
      img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    }

    Attribute attr = null;
    if (s.hasNext("Attribute")) {
      attr = AttributeParser.parse(s);
    }
    Image ret = new Image(id, x, y, img, attr);
    return ret;
  }

  private BufferedImage scanImage(Scanner s) throws IOException {
    String str = s.next(pattern);
    int pos = 0;
    while (!(str.length() == 1 && str.hashCode() == codeand)) {
      System.arraycopy(str.getBytes(), 0, bisdata, pos, str.length());
      pos += str.length();
      str = s.next(pattern);
    }
    bis.reset();
    return ImageIO.read(decoder.wrap(bis));
  }
}
