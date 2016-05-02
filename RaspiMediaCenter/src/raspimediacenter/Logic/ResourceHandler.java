package raspimediacenter.Logic;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.TV.TVEpisodesScene;
import raspimediacenter.GUI.Scenes.TV.TVSeasonsScene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;

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
    
    public static void unloadResources (Scene scene)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            ((MoviesScene)scene).unloadResources();
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            if (Scene.getSubScene().toLowerCase().matches("seasons"))
            {
                ((TVSeasonsScene)scene).unloadResources();
            }
            else if (Scene.getSubScene().toLowerCase().matches("episodes"))
            {
                ((TVEpisodesScene)scene).unloadResources();
            }
            else
            {
                ((TVSeriesScene)scene).unloadResources();
            }  
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("images"))
        {
            
        }
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
