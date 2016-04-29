package raspimediacenter.GUI;

import java.awt.Color;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.MainMenuScene;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import raspimediacenter.GUI.Scenes.MoviesScene;
import raspimediacenter.GUI.Scenes.MusicScene;
import raspimediacenter.GUI.Scenes.TVSeriesScene;

public final class SceneManager {
    
    //retrieve the local screen resolution
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenWidth;
    private static int screenHeight;
    private static Scene currentScene;
    private final JFrame frame;
    private static JLayeredPane contentPane;
    
    public SceneManager () {
       
        screenWidth = 1920;//dim.width;
        screenHeight = 1080;//dim.height;
        
        screenWidth = dim.width;
        screenHeight = dim.height;
        
        //Create a JFrame
        frame = new JFrame();
        frame.setBackground(Color.black);
        //Start media center in full screen
        frame.setSize(screenWidth, screenHeight);

        //Define Panes - for overlapping elements
        contentPane = new JLayeredPane();
        contentPane.setBounds(0, 0, screenWidth, screenHeight);
        
        loadScene("main menu");
        
        frame.getContentPane().add(contentPane);
        
        //set properties for the JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setVisible(true);
    }
    
    public static JLayeredPane getContentPane()
    {
        return contentPane;
    }
    
    public static int getScreenWidth()
    {
        return screenWidth;
    }
    
    public static int getScreenHeight()
    {
        return screenHeight;
    }
    
    public void loadScene (String scene)
    {
        unloadScene(currentScene);
        
        if (scene.matches("main menu"))
        {
            currentScene = new MainMenuScene(this);
        }
        else if (scene.matches("movies"))
        {
            currentScene = new MoviesScene(this);
        }
        else if (scene.matches("tv shows"))
        {
            currentScene = new TVSeriesScene(this);
        }
        else if (scene.matches("music"))
        {
            currentScene = new MusicScene(this);
        }
        else if (scene.matches("images"))
        {
            
        }
    }
    
    public void unloadScene (Scene scene)
    {
        contentPane.removeAll();
    }
}
