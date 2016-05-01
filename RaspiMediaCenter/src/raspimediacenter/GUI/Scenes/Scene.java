package raspimediacenter.GUI.Scenes;

import java.awt.Color;
import javax.swing.JPanel;
import raspimediacenter.GUI.Components.BackgroundCanvas;
import raspimediacenter.Logic.Utilities.ColorUtilities;

public class Scene extends JPanel {
    
    //MENU COLOR CHOICES
    private static final Color purple = new Color(138, 77, 179);
    private static final Color blue = new Color(0, 153, 204);
    private static final Color darkblue = new Color(38, 64, 115);
    private static final Color aqua = new Color(4, 149, 149);
    private static final Color darkGreen = new Color(50, 103, 103);
    private static final Color red = new Color (179, 77, 77);
    private static final Color black = new Color(20, 20, 20);
    private static final Color darkgrey = new Color(70, 70, 70);
    
    //BACKGROUND
    protected BackgroundCanvas bgCanvas;
    protected final String backgroundImagesPath = "src/raspimediacenter/GUI/Resources/";
    protected final String userImagesPath = "src/raspimediacenter/GUI/Resources/UserBackgrounds/";
    
    //MENU SETTINGS
    public static float MENU_TRANSPARENCY = 0.9f; //Percentage value of menu transparency
    public static Color INVERTED_COLOR;
    public static Color MENU_COLOR = darkblue; //Color of menu elements
    
    public static String currentScene;
    public static String subScene = "";
    
    //BASE CONSTRUCTOR
    public Scene ()
    {
        this.setLayout(null);
        setOpaque(false);
        bgCanvas = new BackgroundCanvas();
        INVERTED_COLOR = ColorUtilities.getInvertedColor(MENU_COLOR);
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
    
    public static Color getInvertedMenuColor ()
    {
        return INVERTED_COLOR;
    }
    
    public static Color getLighterMenuColor (int intensity)
    {
        return ColorUtilities.brighten(MENU_COLOR, intensity);
    }
    
    public static Color getDarkerMenuColor (int intensity)
    {
        return ColorUtilities.darken(MENU_COLOR, intensity);
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

    public void unloadBackgrounds ()    
    {
        bgCanvas.unloadBackgrounds();
    }
    
    public void unloadScene ()
    {
        Scene.setCurrentScene("");
        Scene.setSubScene("");
    }
}
