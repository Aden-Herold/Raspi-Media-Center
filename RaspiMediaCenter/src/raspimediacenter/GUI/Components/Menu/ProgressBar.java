package raspimediacenter.GUI.Components.Menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.TextUtils;

public class ProgressBar extends SceneComponent {

    private final int BAR_HEIGHT = (int)Math.floor(GUI.getScreenHeight()*0.02);
    
    private final Rectangle bounds;
    private double progressPercent = 0;
    private String loadingString;
    private boolean isVisible = false;
    
    public ProgressBar (int x, int y, int w, int h)
    {
        bounds  = new Rectangle(x, y, w, h);
    }
    
    // GETTERS
    public double getProgress()
    {
        return progressPercent;
    }
    
    public boolean getVisible()
    {
        return isVisible;
    }
    
    public String getLoadingString()
    {
        return loadingString;
    }
    
    // SETTERS
    public void setProgress(double progress)
    {
        progressPercent = progress;
    }
    
    public void setVisible (boolean visible)
    {
        isVisible = visible;
    }
    
    public void setLoadingString (String message)
    {
        loadingString = message;
    }

    @Override
    public void paintSceneComponent(Graphics2D g2d) 
    {       
        if (isVisible)
        {
            g2d.setColor(ColorUtils.darken(SceneManager.getMenuColor(), 2));
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f));
            
            g2d.fillRect(bounds.x, bounds.y, bounds.width, BAR_HEIGHT);
            
            g2d.setColor(ColorUtils.brighten(SceneManager.getMenuColor(), 1));
            g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
            
            int progress = (int)Math.round((GUI.getScreenWidth()/100)*progressPercent);
            g2d.fillRect(bounds.x, bounds.y+BAR_HEIGHT/4, progress, BAR_HEIGHT/2);
            
            g2d.setColor(ColorUtils.darken(SceneManager.getMenuColor(), 1));
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f));
            
            g2d.fillRect(bounds.x, bounds.y+BAR_HEIGHT, bounds.width, BAR_HEIGHT);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(TextUtils.TINY_FONT);
            int stringWidth = g2d.getFontMetrics(TextUtils.TINY_FONT).stringWidth(loadingString);
            g2d.drawString(loadingString, bounds.x+bounds.width/2-stringWidth/2, bounds.y+BAR_HEIGHT*2-BAR_HEIGHT/3);
        }
    }
}
