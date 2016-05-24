package raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerButton;
import raspimediacenter.GUI.SceneManager;

public class StopButton extends VideoPlayerButton {

    private Rectangle bounds;
    
    private boolean focused = false;
    
    public StopButton (int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
    }
    
    // GETTERS
    @Override
    public Rectangle getRect()
    {
        return bounds;
    }
    
    public boolean getFocused ()
    {
        return focused;
    }
    
    @Override 
    public boolean getState()
    {
        return false;
    }
    
    // SETTERS
    public void setBounds(int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
    }

    @Override
    public void setFocused (boolean isFocused)
    {
        focused = isFocused;
    }
    
    @Override
    public void setState(boolean state) {
    }
    
    @Override
    public void performAction ()
    {
        try {
            Robot robot = new Robot();
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
            /*
            SceneManager.getCurrentScene().getPlayer().stop();
            SceneManager.loadPreviousScene();
            */
        } catch (AWTException ex) {
            Logger.getLogger(StopButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void drawButton(Graphics2D g2d) {
        
        if (focused)
        {
            g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
        }
        else
        {
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        }
        
        g2d.setPaint(Color.WHITE);
        g2d.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
        
        g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
        g2d.setPaint(SceneManager.getMenuColor());

        int size = (int)Math.floor(bounds.width/3);
        
         g2d.fillRect(bounds.x+size,
                         bounds.y+size, 
                         bounds.width/3,
                         bounds.height/3);
    } 
 }
