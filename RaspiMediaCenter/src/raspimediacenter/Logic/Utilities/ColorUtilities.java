package raspimediacenter.Logic.Utilities;

import java.awt.Color;
import raspimediacenter.GUI.Scenes.Scene;

public class ColorUtilities {
    
    public static Color getInvertedColor (Color color)
    {
        int rgb = Scene.getMenuColor().getRGB();
        int inverted = (0x00FFFFFF - (rgb | 0xFF000000)) | (rgb & 0xFF000000);
        Color color_inverted = new Color(inverted);
        
        return color_inverted;
    }
    
    public static Color brighten(Color color, int intensity) {

        while(intensity > 0)
        {
            color = color.brighter();
            intensity = intensity-1;
        }
        
        return color;
    }
}
