package raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.TextUtils;

public class RewindButton extends VideoPlayerButton {

    private Rectangle bounds;
    private final Rectangle symbolsRect;
    
    private boolean focused = false;
    private int rwState = 0;
    
    private final VideoPlayerButton playBtn;
    
    public RewindButton (int x, int y, int width, int height, VideoPlayerButton playBtn)
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
        if (state)
        {
            rwState++;
            
            if (rwState > 5)
            {
                rwState = 1;
            }
        }
        else
        {
            rwState = 0;
        }
    }
    
    @Override
    public void performAction ()
    {
        playBtn.setState(false);
        setState(true);
        SceneManager.getCurrentScene().getPlayer().toggleSeeking(1); // 1 = rewind
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
        
        
        //TRIANGLE
        GeneralPath ff1 = new GeneralPath();
            ff1.moveTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height/3);
            ff1.lineTo(symbolsRect.x+symbolsRect.width/4, symbolsRect.y+symbolsRect.height/2);
            ff1.lineTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height-symbolsRect.height/3);
        ff1.closePath();



        //TRIANGLE
        GeneralPath ff2 = new GeneralPath();
            ff2.moveTo(symbolsRect.x+symbolsRect.width-symbolsRect.width/4, symbolsRect.y+symbolsRect.height/3);
            ff2.lineTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height/2);
            ff2.lineTo(symbolsRect.x+symbolsRect.width-symbolsRect.width/4, symbolsRect.y+symbolsRect.height-symbolsRect.height/3);
        ff2.closePath();

        g2d.fill(ff1);
        g2d.fill(ff2);
        
        if (!playBtn.getState())
        {
            if (rwState == 1)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x2", symbolsRect.x+(int)Math.floor(symbolsRect.width/2.5), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (rwState == 2)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x4", symbolsRect.x+(int)Math.floor(symbolsRect.width/2.5), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (rwState == 3)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x8", symbolsRect.x+(int)Math.floor(symbolsRect.width/2.5), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (rwState == 4)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x16", symbolsRect.x+(int)Math.floor(symbolsRect.width/3), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (rwState == 5)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x32", symbolsRect.x+(int)Math.floor(symbolsRect.width/3), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
        }
        else
        {
            if (rwState != 0)
            {
                rwState  = 0;
            }
        }
    }
}