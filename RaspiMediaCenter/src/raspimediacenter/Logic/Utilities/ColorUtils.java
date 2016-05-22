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
    
    //Returns a colour along the HUE scale between 0.0 and 1.0 
    // Hue pattern :: red - orange - yellow - green - blue - purple - red
    public static Color getColor(double power)
    {
        double H = power * 1; // Hue
        double S = 0.9; // Saturation
        double B = 0.9; // Brightness

        return Color.getHSBColor((float)H, (float)S, (float)B);
    }
}