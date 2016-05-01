package raspimediacenter.GUI;

import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.MainMenuScene;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.BlackBasePanel;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.TV.TVEpisodesScene;
import raspimediacenter.GUI.Scenes.TV.TVSeasonsScene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;

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
        /*
        screenWidth = dim.width;
        screenHeight = dim.height;
        */
        //Create a JFrame
        frame = new JFrame();
        frame.setSize(screenWidth, screenHeight);

        //Define Panes - for overlapping elements
        contentPane = new JLayeredPane();
        contentPane.setBounds(0, 0, screenWidth, screenHeight);
        contentPane.setOpaque(false);
        
        JPanel blackBasePanel = new BlackBasePanel();
        blackBasePanel.setBounds(0, 0, screenWidth, screenHeight);
        contentPane.add(blackBasePanel, 0, 0);
        
        loadScene("main menu");
        
        //frame.addKeyListener(new KeyboardListener());
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new GlobalDispatcher());
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
    
    public static Scene getCurrentScene()
    {
        return currentScene;
    }
    
    public static void loadScene (String scene)
    {
        unloadScene(currentScene);
        
        if (scene.toLowerCase().matches("main menu"))
        {
            currentScene = new MainMenuScene();
        }
        else if (scene.toLowerCase().matches("movies"))
        {
            currentScene = new MoviesScene();
        }
        else if (scene.toLowerCase().matches("tv shows"))
        {
            currentScene = new TVSeriesScene();
        }
        else if (scene.toLowerCase().matches("music"))
        {
            
        }
        else if (scene.toLowerCase().matches("images"))
        {
            
        }
    }
    
    public static void loadScene (String scene, Object obj)
    {
        unloadScene(currentScene);
        
        if (scene.toLowerCase().matches("main menu"))
        {
            currentScene = new MainMenuScene();
        }
        else if (scene.toLowerCase().matches("seasons"))
        {
            currentScene = new TVSeasonsScene((TVSeries)obj);
        }
        
    }
    
    public static void loadScene (String scene, Object obj, int num)
    {
        unloadScene(currentScene);
        
        if (scene.toLowerCase().matches("episodes"))
        {
            currentScene = new TVEpisodesScene((TVSeries)obj, num);
        }
    }
    
    public static void unloadScene (Scene scene)
    {
        if (scene != null)
        {
            scene.unloadBackgrounds();
            scene.unloadScene();
        }
        contentPane.removeAll();
    }
    
    //EVENT LISTENERS  
    private class GlobalDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) 
        {
            if (e.getID() == KeyEvent.KEY_PRESSED) 
            {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_BACK_SPACE)
                {
                    System.out.println("Current Scene: " + Scene.getCurrentScene().toLowerCase());
                    if (!Scene.getCurrentScene().toLowerCase().matches("main menu"))
                    {
                        String currentScene = Scene.getCurrentScene().toLowerCase();
                        String subScene = Scene.getSubScene().toLowerCase();
                        
                        if (currentScene.matches("movies"))
                        {
                            if (subScene.matches("episodes"))
                            {
                                
                            }
                            else if (subScene.matches("seasons"))
                            {
                                
                            }
                            else 
                            {
                                loadScene("Main Menu");
                            }
                        }
                        else if (currentScene.matches("tv shows"))
                        {
                            if (subScene.matches("episodes"))
                            {
                                loadScene("seasons", TVEpisodesScene.getShow());
                            }
                            else if (subScene.matches("seasons"))
                            {
                                loadScene("TV Shows");
                            }
                            else 
                            {
                                loadScene("Main Menu");
                            }
                        }
                        else if (currentScene.matches("music"))
                        {
                            
                        }
                        else if (currentScene.matches("images"))
                        {
                            
                        }

                        
                    }
                }
            } 
            else if (e.getID() == KeyEvent.KEY_RELEASED) 
            {
                //System.out.println("2test2");
            } 
            else if (e.getID() == KeyEvent.KEY_TYPED) 
            {
                //System.out.println("3test3");
            }
            return false;
        }
    }
}
