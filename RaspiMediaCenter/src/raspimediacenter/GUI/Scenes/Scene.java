package raspimediacenter.GUI.Scenes;

import java.awt.Color;
import javax.swing.JPanel;
import raspimediacenter.GUI.Components.BackgroundCanvas;

public class Scene extends JPanel {
    
    //BACKGROUND
    protected BackgroundCanvas bgCanvas;
    protected final String backgroundImagesPath = "src/raspimediacenter/GUI/Resources/";
    protected final String userImagesPath = "src/raspimediacenter/GUI/Resources/UserBackgrounds/";
    
    //MENU SETTINGS
    public static float MENU_TRANSPARENCY = 0.9f; //Percentage value of menu transparency
    public static Color MENU_COLOR = new Color(0, 153, 204); //Color of menu elements
    
    public static String currentScene;
    public static String subScene = "";
    
    //BASE CONSTRUCTOR
    public Scene ()
    {
        this.setLayout(null);
        bgCanvas = new BackgroundCanvas();
    }
    
    //GETTERS
    public static float getMenuTransparency()
    {
        return MENU_TRANSPARENCY;
    }
    
    public static Color getMenuColor()
    {
        return MENU_COLOR;
    }
    
    public static String getCurrentScene()
    {
        return currentScene;
    }
    
    public static String getSubScene()
    {
        return subScene;
    }
    
    //SETTERS
    public static void setMenuTransparency(float transparency)
    {
        MENU_TRANSPARENCY = transparency;
    }
    
    public static void setMenuColor (Color color)
    {
        MENU_COLOR = color;
    }
    
    public static void setCurrentScene(String scene)
    {
        currentScene = scene;
    }
    
    public static void setSubScene(String scene)
    {
        subScene = scene;
    }
    
    //FUNCTIONS
    public void loadBackgrounds()
    {
        if (!bgCanvas.loadImagesFromDir(userImagesPath))
        {
            bgCanvas.loadImagesFromDir(backgroundImagesPath);
        }
    }
    
    public void loadBackground(String path)
    {
        bgCanvas.loadImageFromPath(path);
    }  
    
    public void unloadBackgrounds ()    
    {
        bgCanvas.unloadFanartFromMemory();
    }
    
    public void unloadScene ()
    {
        Scene.setCurrentScene("");
        Scene.setSubScene("");
    }
}
