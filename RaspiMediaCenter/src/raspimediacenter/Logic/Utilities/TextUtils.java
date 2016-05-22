package raspimediacenter.Logic.Utilities;

import raspimediacenter.GUI.GUI;
import java.awt.Font;

public class TextUtils {

    public static final int LEFT_ALIGN = 0;
    public static final int RIGHT_ALIGN = 1;
    
    public static final int SMALL_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.009);
    public static final int STANDARD_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.011);
    public static final int MENU_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.022);
    public static final int STAR_RATING_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.015);
    
    public static Font SMALL_FONT;
    public static Font STANDARD_FONT;
    
    private TextUtils(){}
    
    public static void setFont(Font font)
    {
        STANDARD_FONT = font;
        SMALL_FONT = font.deriveFont(Font.PLAIN, SMALL_FONT_SIZE);
    }
}
