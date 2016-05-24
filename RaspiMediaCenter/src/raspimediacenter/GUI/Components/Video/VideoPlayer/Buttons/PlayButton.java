package raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerButton;
import raspimediacenter.GUI.SceneManager;

public class PlayButton extends VideoPlayerButton {

    private Rectangle bounds;
    
    private boolean focused = false;
    private boolean playing = true;
    
    public PlayButton (int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
    }
    
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
    public boolean getState ()
    {
        return playing;
    }
    
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
    public void setState (boolean state)
    {
        playing = state;
    }
    
    @Override
    public void performAction ()
    {
        playing = !playing;
        SceneManager.getCurrentScene().getPlayer().togglePause();
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
        
        //If paused display play button
        if (!playing)
        {
            //TRIANGLE
            GeneralPath playBtn = new GeneralPath();
                playBtn.moveTo(bounds.x+bounds.width/3, bounds.y+bounds.height/4);
                playBtn.lineTo(bounds.x+bounds.width-bounds.width/4, bounds.y+bounds.height/2);
                playBtn.lineTo(bounds.x+bounds.width/3, bounds.y+bounds.height-bounds.height/4);
            playBtn.closePath();
            
            g2d.fill(playBtn);
        }
        else //display pause button
        {
            g2d.fillRect(bounds.x+bounds.width/3, 
                         bounds.y+bounds.height/4, 
                         bounds.width/8, 
                         bounds.height/2);
            
            g2d.fillRect(bounds.x+bounds.width-(bounds.width/3)-(bounds.width/8),
                         bounds.y+bounds.height/4, 
                         bounds.width/8, 
                         bounds.height/2);
        } 
    }
}
