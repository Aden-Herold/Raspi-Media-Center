package raspimediacenter.Logic.Utilities;

import raspimediacenter.GUI.GUI;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class TextUtils {

    public static final int LEFT_ALIGN = 0;
    public static final int RIGHT_ALIGN = 1;
    
    public static final int TINY_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.007);
    public static final int SMALL_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.009);
    public static final int STANDARD_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.011);
    public static final int LARGE_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.022);
    
    public static final int MENU_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.022);
    public static final int STAR_RATING_FONT_SIZE = (int)Math.floor(GUI.getScreenWidth()*0.015);
    
    public static Font TINY_FONT;
    public static Font SMALL_FONT;
    public static Font STANDARD_FONT;
    public static Font LARGE_FONT;
    
    private static Graphics graphics;
    
    private TextUtils(){}
    
    public static void setFont(Font font)
    {
        STANDARD_FONT = font;
        TINY_FONT = font.deriveFont(Font.PLAIN, TINY_FONT_SIZE);
        SMALL_FONT = font.deriveFont(Font.PLAIN, SMALL_FONT_SIZE);
        LARGE_FONT = font.deriveFont(Font.BOLD, LARGE_FONT_SIZE);
    }
    
    public static void setGraphics (Graphics g)
    {
        graphics = g;
    }
    
    public static FontMetrics getMetrics ( Font f)
    {
        return graphics.getFontMetrics(f);
    }
}
