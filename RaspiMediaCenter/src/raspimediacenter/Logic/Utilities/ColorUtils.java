package raspimediacenter.Logic.Utilities;

import raspimediacenter.GUI.SceneManager;
import java.awt.Color;

public class ColorUtils {
    
    public static Color getInvertedColor (Color color)
    {
        int rgb = SceneManager.getMenuColor().getRGB();
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
    
    public static Color darken(Color color, int intensity) {

        while(intensity > 0)
        {
            color = color.darker();
            intensity = intensity-1;
        }
        
        return color;
    }
}