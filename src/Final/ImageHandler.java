package Final;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {

    BufferedImage image;

    public ImageHandler(File input, int gridSize) throws IOException {
        this.image = ImageIO.read(input);
        this.image = image.getSubimage(0,0,image.getWidth()-(image.getWidth()%gridSize),image.getHeight()-(image.getHeight()%gridSize));
    }

    public BufferedImage getImage(){
        return image;
    }

    public BufferedImage getBlackAndWhite(){
        BufferedImage tempImage = image;
        for(int x=0; x<image.getWidth(); x++){
            for(int y=0; y<image.getHeight(); y++){
                Color tempColor = new Color(image.getRGB(x,y));
                int avg = (tempColor.getRed()+tempColor.getGreen()+tempColor.getBlue())/3;
                tempColor = new Color(avg,avg,avg);
                tempImage.setRGB(x,y,tempColor.getRGB());
            }
        }
        return tempImage;
    }

}
