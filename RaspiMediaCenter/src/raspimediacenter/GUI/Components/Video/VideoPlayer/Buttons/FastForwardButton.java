package raspimediacenter.GUI.Components.Video.VideoPlayer.Buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.TextUtils;

public class FastForwardButton extends VideoPlayerButton {

    private Rectangle bounds;
    private Rectangle symbolsRect;
    
    private boolean focused = false;
    private int ffState = 0;
    
    private VideoPlayerButton playBtn;
    
    public FastForwardButton (int x, int y, int width, int height, VideoPlayerButton playBtn)
    {
        bounds = new Rectangle(x, y, width, height);
        symbolsRect = new Rectangle (x, y, width, height);
        symbolsRect.x += 4;
        
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
            ffState++;
            
            if (ffState > 5)
            {
                ffState = 1;
            }
        }
        else
        {
            ffState = 0;
        }
    }
    
    @Override
    public void performAction ()
    {
        playBtn.setState(false);
        setState(true);
        SceneManager.getCurrentScene().getPlayer().toggleSeeking(0); // 0 = fast forward
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
            ff1.moveTo(symbolsRect.x+symbolsRect.width/4, symbolsRect.y+symbolsRect.height/3);
            ff1.lineTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height/2);
            ff1.lineTo(symbolsRect.x+symbolsRect.width/4, symbolsRect.y+symbolsRect.height-symbolsRect.height/3);
        ff1.closePath();



        //TRIANGLE
        GeneralPath ff2 = new GeneralPath();
            ff2.moveTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height/3);
            ff2.lineTo(symbolsRect.x+symbolsRect.width-symbolsRect.width/4, symbolsRect.y+symbolsRect.height/2);
            ff2.lineTo(symbolsRect.x+symbolsRect.width/2, symbolsRect.y+symbolsRect.height-symbolsRect.height/3);
        ff2.closePath();

        g2d.fill(ff1);
        g2d.fill(ff2);
        
        if (!playBtn.getState())
        {
            if (ffState == 1)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x2", symbolsRect.x+symbolsRect.width/3, symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (ffState == 2)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x4", symbolsRect.x+symbolsRect.width/3, symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (ffState == 3)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x8", symbolsRect.x+symbolsRect.width/3, symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (ffState == 4)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x16", symbolsRect.x+(int)Math.floor(symbolsRect.width/3.5), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
            else if (ffState == 5)
            {
                int lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight()+5;
                g2d.setFont(TextUtils.SMALL_FONT);
                g2d.drawString("x32", symbolsRect.x+(int)Math.floor(symbolsRect.width/3.5), symbolsRect.y+symbolsRect.height/2+lineHeight);
            } 
        }
    }
}