package raspimediacenter.GUI.Scenes;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerMenu;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;

public class VideoPlayerScene extends Scene {

    private final TVSeries show;
    private final int seasonNumber;
    private final String episodeName;
    
    private boolean painting = false;
    private SceneMenu sceneMenu;
    private EmbeddedVideoPlayer player;

    public VideoPlayerScene (TVSeries show, int seasonNumber, String episodeName)
    {
        this.show = show;
        this.seasonNumber = seasonNumber+1;
        this.episodeName = episodeName;

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
        while(true)
        {
            paintScene();
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(VideoPlayerScene.class.getName()).log(Level.SEVERE, null, ex);
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
        
<<<<<<< HEAD
        String file = "TV Shows/Breaking Bad/vid.mp4";
=======
        player = new EmbeddedVideoPlayer();    
        sceneMenu = new VideoPlayerMenu(); 
        sceneMenu.setupLibraryList(null);
        
        String file = "TV Shows/"+show.getName()+"/Season "+seasonNumber+"/Dexter S01E01 - Dexter.avi";
>>>>>>> 37ba4149e3560c85f1aafbee24503faca83e09ef
        player.play(file);
        
        painting = false;
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
