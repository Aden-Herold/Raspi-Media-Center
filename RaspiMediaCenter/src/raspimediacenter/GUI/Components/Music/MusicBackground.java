package raspimediacenter.GUI.Components.Music;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtils;

public class MusicBackground extends SceneComponent{

    private final String backgroundImagePath = "Resources/musicBackground1.jpg";
    private BufferedImage background;
    
    private Equalizer eq;
    private Thread rendererThread;
    private boolean rendering = false;
    
    public MusicBackground ()
    {
        eq = new Equalizer();
        
        background = ImageUtils.getImageFromPath(backgroundImagePath);
        
        rendererThread = new Thread(){
            @Override
            public void run()
                {
                    while (rendering)
                    {
                        updateBackground();
                    }
                }
        };
        
        rendering = true;
        rendererThread.start();
    }
    
    private void updateBackground()
    {
        SceneManager.getCurrentScene().paintScene();
        
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            Logger.getLogger(MusicBackground.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void unload ()
    {
        rendering = false;
        rendererThread = null;
        eq = null;
        background = null;
    }
    
    @Override
    public void paintSceneComponent(Graphics2D g2d) 
    {
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, GUI.getScreenWidth(), GUI.getScreenHeight());
        
        g2d.drawImage(background, 0, 0, GUI.getScreenWidth(), GUI.getScreenHeight(), null);
        
        eq.paintSceneComponent(g2d);
    }
}
