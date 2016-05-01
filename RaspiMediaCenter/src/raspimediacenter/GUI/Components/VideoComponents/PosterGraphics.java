package raspimediacenter.GUI.Components.VideoComponents;

import raspimediacenter.GUI.SceneManager;

public class PosterGraphics extends VideoPreviewGraphics {
    
    private final String defaultPosterPath = "src/raspimediacenter/GUI/Resources/defaultPoster.jpg";
    
    public PosterGraphics ()
    {
        posterWidth = (int)Math.floor(SceneManager.getScreenWidth()/6.4);
        posterHeight = (int)Math.floor(SceneManager.getScreenHeight()/2.4);
        defaultPreviewImagePath = defaultPosterPath;
    }
}
