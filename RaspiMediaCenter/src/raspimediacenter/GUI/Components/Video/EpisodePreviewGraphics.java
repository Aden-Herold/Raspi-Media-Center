package raspimediacenter.GUI.Components.Video;

import raspimediacenter.GUI.GUI;

public class EpisodePreviewGraphics extends VideoPreviewGraphics {
   
    private final String defaultEpisodePreviewPath = "src/fullscreengui/GUI/Resources/defaultEpPreview.jpg";
    
    public EpisodePreviewGraphics ()
    {
        posterWidth = GUI.getScreenWidth()/4;
        posterHeight = GUI.getScreenHeight()/4;
        defaultPreviewImagePath = defaultEpisodePreviewPath;
    }
}
