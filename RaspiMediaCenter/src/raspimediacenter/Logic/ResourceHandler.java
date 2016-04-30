package raspimediacenter.Logic;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public final class ResourceHandler {
    
    private final String menuFontPath = "src/raspimediacenter/GUI/Fonts/Bombard.ttf";
    
    public ResourceHandler ()
    {
        loadResources();
    }
    
    public void loadResources ()
    {
        loadFonts();
    }
    
    public void unloadResources ()
    {
        
    }
    
    private void loadFonts ()
    {
        try 
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(menuFontPath)));
        } 
        catch (IOException | FontFormatException e) 
        {
            System.out.println(e.getMessage());
        }
    }
}
