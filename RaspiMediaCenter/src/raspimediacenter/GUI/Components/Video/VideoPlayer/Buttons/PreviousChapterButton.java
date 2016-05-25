package raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerButton;
import raspimediacenter.GUI.SceneManager;

public class PreviousChapterButton extends VideoPlayerButton {

    private Rectangle bounds;
    private final Rectangle symbolsRect;
    
    private boolean focused = false;
    private final VideoPlayerButton playBtn;
    
    public PreviousChapterButton (int x, int y, int width, int height, VideoPlayerButton playBtn)
    {
        bounds = new Rectangle(x, y, width, height);
        symbolsRect = new Rectangle (x, y, width, height);
        symbolsRect.x -= 4;
        
        this.playBtn = playBtn;
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
    public void setState (boolean state)
    {

    }
    
    @Override
    public void performAction ()
    {
        SceneManager.getCurrentScene().getPlayer().previousChapter();
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
        
        g2d.fillRect(symbolsRect.x+symbolsRect.width/3, symbolsRect.y+symbolsRect.height/3, symbolsRect.width/8, symbolsRect.height/3);
        
       //TRIANGLE
        GeneralPath ff2 = new GeneralPath();
            ff2.moveTo(symbolsRect.x+symbolsRect.width-symbolsRect.width/4, symbolsRect.y+symbolsRect.height/3);
            ff2.lineTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height/2);
            ff2.lineTo(symbolsRect.x+symbolsRect.width-symbolsRect.width/4, symbolsRect.y+symbolsRect.height-symbolsRect.height/3);
        ff2.closePath();

        g2d.fill(ff2);
    }
}
