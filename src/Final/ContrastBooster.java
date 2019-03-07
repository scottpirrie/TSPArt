package Final;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ContrastBooster {

    private BufferedImage image;

    public ContrastBooster(BufferedImage image){
        this.image=image;
    }

    public BufferedImage getBoostedImage(int factor){
        BufferedImage tempInput = image;
        BufferedImage output=image;
        RescaleOp rescale = new RescaleOp(factor, 0, null);
        rescale.filter(tempInput,output);
        return output;
    }
}
