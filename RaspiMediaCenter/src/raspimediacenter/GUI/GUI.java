package raspimediacenter.GUI;

import java.awt.BufferCapabilities;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.ResourceManager;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JFrame;

public class GUI {

    private static int screenWidth;
    private static int screenHeight;
    private static JFrame window;
    private static Canvas screen;
    private SceneManager sceneManager;
    
    public GUI ()
    {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        if (device.isFullScreenSupported())
        {
            window = new JFrame();
            window.setIgnoreRepaint(true);
            window.setUndecorated(true);
            window.setResizable(false);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            device.setFullScreenWindow(window);
            screenWidth = device.getFullScreenWindow().getSize().width;
            screenHeight = device.getFullScreenWindow().getSize().height;
            ResourceManager resources = new ResourceManager();
            
            screen = new Canvas();
            screen.setIgnoreRepaint(true);
            screen.setSize(new Dimension(screenWidth, screenHeight));
            screen.addMouseListener(new mouseListener());
            screen.addMouseWheelListener(new mouseListener());
            screen.addMouseMotionListener(new mouseListener());
            screen.addKeyListener(new keyboardListener());
            
            window.getContentPane().add(screen);

            screen.createBufferStrategy(2);   
            
            BufferCapabilities cap = screen.getBufferStrategy().getCapabilities();
            System.out.println("Page Flipping: "+cap.isPageFlipping());
            System.out.println("MultiBuffer: "+cap.isMultiBufferAvailable());
            System.out.println("Fullscreen: "+cap.isFullScreenRequired());
            System.out.println("Backbuffer Accel: "+cap.getBackBufferCapabilities().isAccelerated());
            
            sceneManager = new SceneManager();
            SceneManager.loadScene("main menu");
        }
        else
        {
            System.out.println("Full-Screen Mode not supported");
            System.exit(0);
        }
    }
    
    public static int getScreenWidth()
    {
        return screenWidth;
    }
    
    public static int getScreenHeight()
    {
        return screenHeight;
    }

    public static Canvas getScreen()
    {
        return screen;
    }
    
    // EVENT LISTENERS
    private class keyboardListener implements KeyListener
    {
        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            
            if (sceneManager != null)
            {
                Scene currentScene = SceneManager.getCurrentScene();
                
                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_D)
                {
                    currentScene.getMenu().focusNextButton();
                } 
                else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_A)
                {
                    currentScene.getMenu().focusPreviousButton();
                }
                else if (key == KeyEvent.VK_ENTER)
                {
                    currentScene.buttonClicked();
                }
                else if (key == KeyEvent.VK_BACK_SPACE)
                {
                    if (SceneManager.getLastPreviousScene() != null)
                    {
                        SceneManager.loadScene(SceneManager.getLastPreviousScene());
                        SceneManager.removeLastScene();
                    }
                }
                else if (key == KeyEvent.VK_ESCAPE)
                {
                    System.exit(0);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }
    }
    
    private class mouseListener implements MouseListener, MouseWheelListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) 
        {
            if (sceneManager != null)
            {
                if (SceneManager.getCurrentScene() != null)
                {
                    if (SceneManager.getCurrentScene().getMenu() != null)
                    {
                        SceneMenu menu = SceneManager.getCurrentScene().getMenu();
                        if (menu != null)
                        {
                            menu.clickedButton(e);
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            
            if (sceneManager != null)
            {
                SceneMenu menu = SceneManager.getCurrentScene().getMenu();
                if (menu != null)
                {
                   if (menu.isScrollable())
                    {
                        int notches = e.getWheelRotation()*-1;
                        menu.scrollList(notches, 40);
                    } 
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
            if (sceneManager != null)
            {
                if (SceneManager.getCurrentScene() != null)
                {
                    if (SceneManager.getCurrentScene().getMenu() != null)
                    {
                        SceneMenu menu = SceneManager.getCurrentScene().getMenu();
                        if (menu != null)
                        {
                            menu.focusHoveredButton(e);
                        }
                    }
                }
            }
            
        }
    }
}
