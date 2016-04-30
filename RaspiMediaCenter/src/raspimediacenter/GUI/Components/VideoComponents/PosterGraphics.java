package raspimediacenter.GUI.Components.VideoComponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;

public class PosterGraphics {
    
    private BufferedImage currentPoster;
    private static final Dimension maximumSize = new Dimension(200, 300);
    private static int posterWidth = maximumSize.width;
    private static int posterHeight = maximumSize.height;
    
    public PosterGraphics ()
    {
        posterWidth = SceneManager.getScreenWidth()/8;
        posterHeight = SceneManager.getScreenHeight()/3;
    }
    
    public static int getPosterHeight ()
    {
        return posterHeight;
    }
    
    public static int getPosterWidth ()
    {
        return posterWidth;
    }
    
    public BufferedImage getCurrentPoster ()
    {
        return currentPoster;
    }
    
    public void displayPoster (Graphics2D paint)
    {
        paint.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        paint.setColor(Color.black);
        paint.fillRect((int)Math.floor(SceneManager.getScreenWidth()*0.0196)-5, SceneManager.getScreenHeight()-posterHeight-(int)Math.floor(SceneManager.getScreenWidth()*0.0196)-5,
                posterWidth+10, posterHeight+10);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawImage(currentPoster, (int)Math.floor(SceneManager.getScreenWidth()*0.0196), SceneManager.getScreenHeight()-posterHeight-(int)Math.floor(SceneManager.getScreenWidth()*0.0196),
                posterWidth, posterHeight, null);
    }
    
    public void updatePoster (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            currentPoster = MoviesScene.getPosterImage(linkNum);
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            currentPoster = TVSeriesScene.getPosterImage(linkNum);
        }
    }
}
