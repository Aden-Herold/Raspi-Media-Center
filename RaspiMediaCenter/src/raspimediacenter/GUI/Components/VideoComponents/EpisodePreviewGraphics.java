package raspimediacenter.GUI.Components.VideoComponents;

import raspimediacenter.GUI.SceneManager;

public class EpisodePreviewGraphics extends VideoPreviewGraphics {
   
    private final String defaultEpisodePreviewPath = "src/raspimediacenter/GUI/Resources/defaultEpPreview.jpg";
    
    public EpisodePreviewGraphics ()
    {
        posterWidth = SceneManager.getScreenWidth()/6;
        posterHeight = SceneManager.getScreenHeight()/5;
        defaultPreviewImagePath = defaultEpisodePreviewPath;
    }
}
