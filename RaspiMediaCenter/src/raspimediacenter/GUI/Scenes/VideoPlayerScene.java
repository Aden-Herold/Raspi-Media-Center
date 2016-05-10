package raspimediacenter.GUI.Scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.Logic.Players.VideoPlayer;

public class VideoPlayerScene extends Scene {

    private boolean painting = false;
    private final TVSeries show;
    private final int seasonNumber;
    private final String episodeName;
    
    private final VideoPlayer player;
    private BufferedImage image;
   
    
    public VideoPlayerScene (TVSeries show, int seasonNumber, String episodeName)
    {
        this.show = show;
        this.seasonNumber = seasonNumber+1;
        this.episodeName = episodeName;

        player = new VideoPlayer(this);
        
        image = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .createCompatibleImage(GUI.getScreenWidth(), GUI.getScreenHeight());
    }
    
    public void updateVideo(BufferedImage img)
    {
        image = img;
        paintScene();
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

        String file = "TV Shows/"+show.getName()+"/Season "+seasonNumber+"/Dexter S01E01 - Dexter.avi";
        player.play(file);
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
            BufferStrategy buffer = GUI.getScreen().getBufferStrategy();
            Graphics2D g2d = (Graphics2D)(buffer.getDrawGraphics());
            //g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try 
            {
                g2d.setPaint(Color.BLACK);
                g2d.drawRect(0, 0, GUI.getScreenWidth(), GUI.getScreenHeight());
                g2d.drawImage(image, 0, 0, GUI.getScreen());
                
                if (!buffer.contentsLost())
                {
                    buffer.show();
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
