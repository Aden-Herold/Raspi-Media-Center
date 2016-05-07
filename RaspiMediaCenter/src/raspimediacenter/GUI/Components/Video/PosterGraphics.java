package raspimediacenter.GUI.Components.Video;

import raspimediacenter.GUI.GUI;

public class PosterGraphics extends VideoPreviewGraphics {
    
    private final String defaultPosterPath = "src/fullscreengui/GUI/Resources/defaultPoster.jpg";
    
    public PosterGraphics ()
    {
        posterWidth = (int)Math.floor(GUI.getScreenWidth()/6.4);
        posterHeight = (int)Math.floor(GUI.getScreenHeight()/2.4);
        defaultPreviewImagePath = defaultPosterPath;
    }
}
