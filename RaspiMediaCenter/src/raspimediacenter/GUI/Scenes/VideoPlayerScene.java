package raspimediacenter.GUI.Scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.Logic.Players.VideoPlayer;

public class VideoPlayerScene extends Scene {

    private boolean painting = false;
    private final TVSeries show;
    private final int seasonNumber;
    private final String episodeName;
    
    private VideoPlayer player;

    public VideoPlayerScene (TVSeries show, int seasonNumber, String episodeName)
    {
        this.show = show;
        this.seasonNumber = seasonNumber+1;
        this.episodeName = episodeName;

        player = new VideoPlayer();
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
            Graphics2D g2d = (Graphics2D)(GUI.getBuffer().getDrawGraphics());
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try 
            {
                player.paintFrame(g2d);
                
                if (!GUI.getBuffer().contentsLost())
                {
                    GUI.getBuffer().show();
                }
            }
            finally 
            {
                g2d.dispose();
                painting = false;
            }
            
            try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VideoPlayerScene.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
}
