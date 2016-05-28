package raspimediacenter.GUI.Scenes;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import raspimediacenter.Data.Models.TV.TVSeasonContainer.TVSeason;
import raspimediacenter.Data.Models.TV.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerMenu;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;

public class VideoPlayerScene extends Scene {

    private final String SCENE_NAME = "VIDEO PLAYER";
    
    private final TVSeries show;
    private final TVSeason episode;
    
    private boolean painting = false;
    private SceneMenu sceneMenu;
    private EmbeddedVideoPlayer player;
    private boolean running = true;

    public VideoPlayerScene (TVSeries show, TVSeason episode)
    {
        this.show = show;
        this.episode = episode;
    }

    // GETTERS
    @Override
    public String getSceneName ()
    {
        return SCENE_NAME;
    }
    
    @Override 
    public EmbeddedVideoPlayer getPlayer()
    {
        return player;
    }
    
    @Override
    public SceneMenu getMenu() {
        return sceneMenu;
    }

    @Override
    public ArrayList<String> getLabelContents(int linkNum) {
        return null;
    }

    private void update()
    {
        while(running)
        {
            paintScene();
            
            try {
                Thread.sleep(100);
            } 
            catch (InterruptedException ex) {
                System.out.println("Thread could not sleep: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public void setupScene() {
        
        Thread thread = new Thread(){
            @Override 
            public void run()
            {
                update();
            }
        };
        
        player = new EmbeddedVideoPlayer();    
        sceneMenu = new VideoPlayerMenu(); 
        sceneMenu.setupLibraryList(null);
        
        //String file = "TV Shows/"+show.getName()+"/Season "+episode.getSeasonNumber()+"/E"+episode.getEpisodeNumber()+" - "+episode.getName();
        //player.playEpisode(reeeeeeeeeeeeeee, reeeeeeeeeeeee);
        
        painting = false;
        thread.start();
    }

    @Override
    public ArrayList<String> createMenuList() {
        return null;
    }

    @Override
    public void unloadScene() 
    {
        running = false;
        if (player.isPlaying())
        {
            player.stop();
        }
        player.removeMediaControls();
        player = null;
        
        sceneMenu =  null;
    }

    @Override
    public void buttonClicked() 
    {
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

        if (!painting)
        {
            painting = true;
            Graphics2D g2d = (Graphics2D)(EmbeddedVideoPlayer.getBuffer().getDrawGraphics());
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try 
            {
                sceneMenu.drawMenu(g2d);
                
                if (!EmbeddedVideoPlayer.getBuffer().contentsLost())
                {
                    EmbeddedVideoPlayer.getBuffer().show();
                }
            }
            finally 
            {
                g2d.dispose();
                painting = false;
            }
        }
    }
}
