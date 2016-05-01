package raspimediacenter.GUI.Components.VideoComponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.TV.TVEpisodesScene;
import raspimediacenter.GUI.Scenes.TV.TVSeasonsScene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;
import raspimediacenter.Logic.Utilities.ImageUtilities;

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
            currentPoster = ImageUtilities.getImageFromPath(defaultPreviewImagePath);
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
        paint.setColor(Scene.getDarkerMenuColor(3));
        paint.drawRect(
                        (int)Math.floor(SceneManager.getScreenWidth()*0.0196)-16,
                        SceneManager.getScreenHeight()-InformationPanelGraphics.getPanelHeight(),
                        posterWidth+32, SceneManager.getScreenHeight()
                      );
        paint.drawRect(
                        (int)Math.floor(SceneManager.getScreenWidth()*0.0196)-17,
                        SceneManager.getScreenHeight()-InformationPanelGraphics.getPanelHeight(),
                        posterWidth+32, SceneManager.getScreenHeight()
                      );
        
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.setColor(Scene.getDarkerMenuColor(2));
        paint.fillRect(
                        (int)Math.floor(SceneManager.getScreenWidth()*0.0196)-15,
                        SceneManager.getScreenHeight()-InformationPanelGraphics.getPanelHeight(),
                        posterWidth+30, SceneManager.getScreenHeight()
                      );
        
        
        
        //Image Border
        paint.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        paint.setColor(Color.black);
        paint.fillRect((int)Math.floor(SceneManager.getScreenWidth()*0.0196)-5, SceneManager.getScreenHeight()-posterHeight-(int)Math.floor(SceneManager.getScreenWidth()/102)-5,
              posterWidth+10, posterHeight+10);
        
        //Image
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawImage(currentPoster, (int)Math.floor(SceneManager.getScreenWidth()*0.0196), SceneManager.getScreenHeight()-posterHeight-(int)Math.floor(SceneManager.getScreenWidth()/102),
                posterWidth, posterHeight, null);
    }
    
    public void updatePoster (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            verifyImage(MoviesScene.getPosterImage(linkNum));
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            if (Scene.getSubScene().toLowerCase().matches("seasons"))
            {
                verifyImage(TVSeasonsScene.getPosterImage(linkNum));
            }
            else if (Scene.getSubScene().toLowerCase().matches("episodes"))
            {
                verifyImage(TVEpisodesScene.getPosterImage(linkNum));
            }
            else
            {
                verifyImage(TVSeriesScene.getPosterImage(linkNum));
            }  
        }
    }
    
    private void verifyImage (BufferedImage poster)
    {
        if (poster == null)
        {
            currentPoster = ImageUtilities.getImageFromPath(defaultPreviewImagePath);
        }
        else
        {
            currentPoster = poster;  
        }
    }
}
