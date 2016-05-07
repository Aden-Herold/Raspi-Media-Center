package raspimediacenter.GUI.Components.Video;

import raspimediacenter.GUI.GUI;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtils;
import raspimediacenter.Logic.Utilities.ColorUtils;

public class VideoPreviewGraphics {
    
    private BufferedImage currentPoster;
    private static final Dimension maximumSize = new Dimension(480, 270);
    protected int posterWidth = maximumSize.width;
    protected int posterHeight = maximumSize.height;
    protected String defaultPreviewImagePath = "";
    
    public VideoPreviewGraphics ()
    {
        
    }
    
    public int getPosterHeight ()
    {
        return posterHeight;
    }
    
    public int getPosterWidth ()
    {
        return posterWidth;
    }
    
    public BufferedImage getCurrentPoster ()
    {
        return currentPoster;
    }
    
    public void setCurrentPoster (BufferedImage poster)
    {
        if (poster == null)
        {
            currentPoster = ImageUtils.getImageFromPath(defaultPreviewImagePath);
        }
        else
        {
            currentPoster = poster;  
        }
    }
    
    public void displayPoster (Graphics2D paint)
    {
        //Menu Panel Fake Inset
        paint.setComposite(AlphaComposite.SrcOver.derive(1f)); 
        paint.setColor(ColorUtils.darken(SceneManager.getMenuColor(), 3));
        paint.drawRect(
                        (int)Math.floor(GUI.getScreenWidth()*0.0196)-GUI.getScreenWidth()/170-1,
                        GUI.getScreenHeight()-InformationPanelGraphics.getPanelHeight(),
                        posterWidth+((GUI.getScreenWidth()/170)*2)+2, GUI.getScreenHeight()
                      );
        paint.drawRect(
                        (int)Math.floor(GUI.getScreenWidth()*0.0196)-GUI.getScreenWidth()/170-2,
                        GUI.getScreenHeight()-InformationPanelGraphics.getPanelHeight(),
                        posterWidth+((GUI.getScreenWidth()/170)*2)+2, GUI.getScreenHeight()
                      );
        
        paint.setComposite(AlphaComposite.SrcOver.derive(0.8f));
        paint.setColor(ColorUtils.darken(SceneManager.getMenuColor(), 2));
        paint.fillRect(
                        (int)Math.floor(GUI.getScreenWidth()*0.0196)-GUI.getScreenWidth()/170,
                        GUI.getScreenHeight()-InformationPanelGraphics.getPanelHeight(),
                        posterWidth+((GUI.getScreenWidth()/170)*2)+2, GUI.getScreenHeight()
                      );
        
        
        
        //Image Border
        paint.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        paint.setColor(Color.black);
        paint.fillRect((int)Math.floor(GUI.getScreenWidth()*0.0196)-5, GUI.getScreenHeight()-posterHeight-(int)GUI.getScreenWidth()/170-5,
              posterWidth+10, posterHeight+10);
        
        //Image
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawImage(currentPoster, (int)Math.floor(GUI.getScreenWidth()*0.0196), GUI.getScreenHeight()-posterHeight-GUI.getScreenWidth()/170,
                posterWidth, posterHeight, null);
    }
    
    private void verifyImage (BufferedImage poster)
    {
        if (poster == null)
        {
            currentPoster = ImageUtils.getImageFromPath(defaultPreviewImagePath);
        }
        else
        {
            currentPoster = poster;  
        }
    }
}
