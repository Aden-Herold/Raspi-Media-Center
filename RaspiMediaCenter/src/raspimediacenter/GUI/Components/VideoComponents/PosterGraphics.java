package raspimediacenter.GUI.Components.VideoComponents;

import raspimediacenter.GUI.SceneManager;

public class PosterGraphics extends VideoPreviewGraphics {
    
    private final String defaultPosterPath = "src/raspimediacenter/GUI/Resources/defaultPoster.jpg";
    
    public PosterGraphics ()
    {
        posterWidth = SceneManager.getScreenWidth()/8;
        posterHeight = SceneManager.getScreenHeight()/3;
        defaultPreviewImagePath = defaultPosterPath;
    }
}
