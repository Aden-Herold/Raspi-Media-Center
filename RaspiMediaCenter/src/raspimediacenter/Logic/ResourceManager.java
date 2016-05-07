package raspimediacenter.Logic;

import raspimediacenter.Logic.Utilities.TextUtils;
import static raspimediacenter.Logic.Utilities.TextUtils.STANDARD_FONT_SIZE;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class ResourceManager {

    private final String menuFontPath = "Resources/Fonts/AeroMatics.ttf";
    
    public ResourceManager()
    {
        loadFonts();
    }
    
    private void loadFonts ()
    {
        try 
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(menuFontPath));
            font = font.deriveFont(Font.PLAIN, STANDARD_FONT_SIZE);
            ge.registerFont(font);
            
            TextUtils.setFont(font);
        } 
        catch (IOException | FontFormatException e) 
        {
            System.out.println(e.getMessage());
        }
    }
}
