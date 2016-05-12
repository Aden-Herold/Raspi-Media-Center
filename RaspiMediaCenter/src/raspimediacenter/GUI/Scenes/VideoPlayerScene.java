package raspimediacenter.GUI.Scenes;

import java.util.ArrayList;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;

public class VideoPlayerScene extends Scene {

    private final TVSeries show;
    private final int seasonNumber;
    private final String episodeName;
    
    private EmbeddedVideoPlayer player;

    public VideoPlayerScene (TVSeries show, int seasonNumber, String episodeName)
    {
        this.show = show;
        this.seasonNumber = seasonNumber+1;
        this.episodeName = episodeName;

        player = new EmbeddedVideoPlayer();     
    }

    @Override
    public SceneMenu getMenu() {
        return null;
    }

    @Override
    public ArrayList<String> getLabelContents(int linkNum) {
        return null;
    }

    @Override
    public void setupScene() {

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                paintScene();
            }
        };
        
        String file = "TV Shows/"+show.getName()+"/Season "+seasonNumber+"/Dexter S01E01 - Dexter.avi";
        player.play(file);
        
        thread.start();
    }

    @Override
    public ArrayList<String> createMenuList() {
        return null;
    }

    @Override
    public void unloadScene() {
        
        player.stop();
        player = null;
        
    }

    @Override
    public void buttonClicked() {
        
    }

    @Override
    public void updateBackground(int linkNum) {
        
    }

    @Override
    public void updatePreviewImage(int linkNum) {
        
    }

    @Override
    public void updateInformationLabels(int linkNum) {
        
    }

    @Override
    public void paintScene() {

    }
}
