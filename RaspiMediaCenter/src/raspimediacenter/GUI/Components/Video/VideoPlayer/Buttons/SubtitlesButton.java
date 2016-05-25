package raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.TextUtils;

public class SubtitlesButton extends VideoPlayerButton {

    private Rectangle bounds;
    private final Rectangle symbolsRect;
    
    private boolean focused = false;
    private boolean subsState = false;
    
    private int subtitlesCount;
    private int subsStyles = -1;
    
    public SubtitlesButton (int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
        symbolsRect = new Rectangle (x, y, width, height);
        
        subtitlesCount = SceneManager.getCurrentScene().getPlayer().getSubtitlesCount();
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
        if (state)
        {
            subsStyles++;
            if (subsStyles > subtitlesCount) 
            {
                subsStyles = -1;
            }
        }
        else
        {
            subsStyles = -1;
        }
    }
    
    @Override
    public void performAction ()
    {
        setState(true);
        SceneManager.getCurrentScene().getPlayer().toggleSubs();
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
        
        int lineHeight = g2d.getFontMetrics(TextUtils.LARGE_FONT).getHeight()+5;
        int lineWidth = g2d.getFontMetrics(TextUtils.LARGE_FONT).stringWidth("S");
        g2d.setFont(TextUtils.LARGE_FONT);
        g2d.drawString("S", symbolsRect.x+symbolsRect.width/2-lineWidth/2, symbolsRect.y+symbolsRect.height-(int)Math.floor(symbolsRect.height/2.5));
        
        lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
        g2d.setFont(TextUtils.SMALL_FONT);
        
        if (subsStyles != -1) // subs on
        {
            String style = "SB"+String.valueOf(subsStyles+1);
            lineWidth = g2d.getFontMetrics(TextUtils.SMALL_FONT).stringWidth(style);
            g2d.drawString(style, symbolsRect.x+symbolsRect.width/2-lineWidth/2, symbolsRect.y+symbolsRect.height-lineHeight/2);
        }
        else // subs off
        {
            lineWidth = g2d.getFontMetrics(TextUtils.SMALL_FONT).stringWidth("OFF");
            g2d.drawString("OFF", symbolsRect.x+symbolsRect.width/2-lineWidth/2, symbolsRect.y+symbolsRect.height-lineHeight/2);
        }
    }
}