package raspimediacenter.GUI;

import java.awt.AWTEvent;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.ResourceManager;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import raspimediacenter.GUI.Components.System.SystemMenuPopup;
import raspimediacenter.Logic.Utilities.TextUtils;

public class GUI {

    private static final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int screenWidth = dim.width;
    private static final int screenHeight = dim.height;
    private static JFrame window;
    private static Canvas screen;
    private static BufferStrategy buffer;
    private SceneManager sceneManager;
    private static SystemMenuPopup menuPopup = null;
    
    public GUI ()
    {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        if (device.isFullScreenSupported())
        {
            window = new JFrame();
            window.setBackground(Color.BLACK);
            window.setSize(screenWidth, screenHeight);
            window.setIgnoreRepaint(true);
            window.setUndecorated(true);
            window.setResizable(false);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            new ResourceManager();

            screen = new Canvas();
            screen.setIgnoreRepaint(true);
            screen.setBounds(0, 0, screenWidth, screenHeight);
            
            window.getLayeredPane().add(screen, 0, 0);
            window.setVisible(true);
            screen.setVisible(true);
            
            screen.createBufferStrategy(2);   
            buffer = screen.getBufferStrategy();
            
            TextUtils.setGraphics(buffer.getDrawGraphics());
            
            addGlobalKeyListener();
            addGlobalMouseListener();
            addGlobalMouseScrollListener();
            addGlobalMouseMotionListener();

            sceneManager = new SceneManager();
            SceneManager.loadScene("main menu");
        }
        else
        {
            System.out.println("Full-Screen Mode not supported");
            System.exit(0);
        }
    }
    
    // GETTERS
    public static int getScreenWidth()
    {
        return screenWidth;
    }
    
    public static int getScreenHeight()
    {
        return screenHeight;
    }
    
    public static JFrame getWindow()
    {
        return window;
    }
    
    public static Canvas getScreen()
    {
        return screen;
    }
    
    public static BufferStrategy getBuffer()
    {
        return buffer;
    }
    
    public static SystemMenuPopup getPopup ()
    {
        return menuPopup;
    }
    
    // EVENT LISTENERS
    private void addGlobalKeyListener()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event) -> {
            if(event instanceof KeyEvent) 
            {
                KeyEvent evt = (KeyEvent)event;
                if(evt.getID() == KeyEvent.KEY_RELEASED)
                {
                    if (sceneManager != null)
                    {
                        Scene currentScene = SceneManager.getCurrentScene();
                        
                        int key = evt.getKeyCode();
                        
                        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_D)
                        {
                            if (menuPopup != null)
                            {
                                menuPopup.focusNextButton();
                            }
                            else
                            {
                                currentScene.getMenu().focusNextButton();
                            }
                        }
                        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_A)
                        {
                            if (menuPopup != null)
                            {
                                menuPopup.focusPreviousButton();
                            }
                            else
                            {
                                currentScene.getMenu().focusPreviousButton();
                            }
                        }
                        else if (key == KeyEvent.VK_TAB)
                        {
                            if (currentScene.getSceneName().toLowerCase().matches("main menu"))
                            {
                                ArrayList<String> options = new ArrayList<>(Arrays.asList("Scrape All", "Scrape All + Images"));
                                menuPopup = new SystemMenuPopup(getScreenWidth()/2, getScreenHeight()/2, options);
                                menuPopup.setupLibraryList(options);
                            }
                            if (currentScene.getSceneName().toLowerCase().matches("image collections"))
                            {
                                ArrayList<String> options = new ArrayList<>(Arrays.asList("Set as Main Menu Backgrounds", "Reset Main Menu Backgrounds"));
                                menuPopup = new SystemMenuPopup(getScreenWidth()/2, getScreenHeight()/2, options);
                                menuPopup.setupLibraryList(options);
                            }
                        }
                        else if (key == KeyEvent.VK_ENTER)
                        {
                            if (menuPopup != null)
                            {
                                menuPopup.clickedButton();
                                menuPopup.unloadMenu();
                                menuPopup = null;
                            }
                            else
                            {
                                currentScene.getMenu().clickedButton();
                            }
                        }
                        else if (key == KeyEvent.VK_BACK_SPACE)
                        {
                            if (menuPopup != null)
                            {
                                menuPopup.unloadMenu();
                                menuPopup = null;
                            }
                            else
                            {
                                if (SceneManager.getLastPreviousScene() != null)
                                {
                                    SceneManager.loadPreviousScene();
                                }
                            }
                        }
                        else if (key == KeyEvent.VK_ESCAPE)
                        {
                            if (menuPopup != null)
                            {
                                menuPopup.unloadMenu();
                                menuPopup = null;
                            }
                            else
                            {
                                System.exit(0);
                            }
                        }
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }
    
    private void addGlobalMouseListener()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event) -> {
            if(event instanceof MouseEvent) 
            {
                MouseEvent evt = (MouseEvent)event;
                if(evt.getID() == MouseEvent.MOUSE_CLICKED)
                {
                    if (menuPopup != null)
                    {
                        menuPopup.clickedButton(evt); 
                    }
                    else
                    {
                        if (sceneManager != null)
                        {
                            if (SceneManager.getCurrentScene() != null)
                            {
                                SceneMenu menu = SceneManager.getCurrentScene().getMenu();
                                if (menu != null)
                                {
                                    menu.clickedButton(evt);
                                }
                            }
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }
    
    private void addGlobalMouseScrollListener()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event) -> {
            if(event instanceof MouseEvent) 
            {
                MouseWheelEvent evt = (MouseWheelEvent)event;
                if(evt.getID() == MouseEvent.MOUSE_WHEEL)
                {
                    if (menuPopup != null)
                    {
                        int notches = evt.getWheelRotation()*-1;
                        menuPopup.scrollList(notches); 
                    }
                    else
                    {
                        if (sceneManager != null)
                        {
                            SceneMenu menu = SceneManager.getCurrentScene().getMenu();
                            if (menu != null)
                            {
                                if (menu.isScrollable())
                                {
                                    int notches = evt.getWheelRotation()*-1;
                                    menu.scrollList(notches); 
                                }
                            }
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_WHEEL_EVENT_MASK);
    }
    
    private void addGlobalMouseMotionListener()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event) -> {
            if(event instanceof MouseEvent) 
            {
                MouseEvent evt = (MouseEvent)event;
                if (evt.getID() == MouseEvent.MOUSE_MOVED)
                {
                    if (menuPopup != null)
                    {
                        menuPopup.focusHoveredButton(evt);
                    }
                    else
                    {
                        if (sceneManager != null)
                        {
                            if (SceneManager.getCurrentScene() != null)
                            {
                                SceneMenu menu = SceneManager.getCurrentScene().getMenu();
                                if (menu != null)
                                {
                                    menu.focusHoveredButton(evt);
                                }
                            }
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
}
