package raspimediacenter.GUI.Scenes.Music;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import raspimediacenter.Data.Models.Music.MusicAlbumContainer;
import raspimediacenter.Data.Models.Music.MusicAlbumList;
import raspimediacenter.Data.Models.Music.MusicArtistContainer;
import raspimediacenter.Data.Models.Music.MusicTrackContainer;
import raspimediacenter.GUI.Components.Music.MusicBackground;
import raspimediacenter.GUI.Components.Music.MusicLibrary;
import raspimediacenter.GUI.Components.Music.MusicTrackLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.ParserUtils;

public class MusicTrackScene extends Scene {

     //SCENE VARIABLES
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private MusicBackground background;
    private SceneMenu sceneMenu;
    
    // DATA MODEL VARIABLES
    private static MusicArtistContainer.MusicArtist artist;
    private static MusicAlbumContainer.MusicAlbum album;
    private static MusicTrackContainer trackList;
    
    // MUSIC SCENE FUNCTIONS
    public MusicTrackScene (MusicArtistContainer.MusicArtist artist, MusicAlbumContainer.MusicAlbum album)
    {
        MusicTrackScene.artist = artist;
        MusicTrackScene.album = album;
    }
    
    // SCENE FUNCTIONS
    //GETTERS
    @Override 
    public EmbeddedVideoPlayer getPlayer()
    {
        return null;
    }
    
    @Override
    public SceneMenu getMenu() {

        if (sceneMenu != null)
        {
            return sceneMenu;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public ArrayList<String> getLabelContents (int linkNum)
    {
        return null;
    }
    
    // SETUP/TEARDOWN FUNCTIONS
    @Override
    public void setupScene() {
        ParserUtils parser = new ParserUtils();
        trackList = parser.parseTrackList("Music/"+artist.artist.getName()+"/"+album.getName()+"/track-list.json", false);
        
         //Create Library List
        sceneMenu = new MusicTrackLibrary();
        sceneMenu.setupMusicList(createMenuList(), createDurationList(), null);
        
        background = new MusicBackground();
        
        paintScene();
    }
    
    @Override 
    public ArrayList<String> createMenuList()
    {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (int x = 0 ; x < trackList.tracks.size(); x++)
        {
            menuList.add(trackList.tracks.get(x).track.getName());
        }
        
        return menuList;
    }
    
    private ArrayList<String> createDurationList()
    {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (int x = 0 ; x < trackList.tracks.size(); x++)
        {
            double dur = ((trackList.tracks.get(x).track.getDuration()/1000)/60);
            String duration = String.valueOf(dur);
            menuList.add(duration + " mins");
        }
        
        return menuList;
    }
    
    @Override
    public void unloadScene() {
        background.unload();
        background = null;
        sceneMenu.unloadMenu();
        sceneMenu = null;
    }
    
    // EVENT FUNCTIONS
    @Override
    public void buttonClicked ()
    {

    }
    
    //UPDATE FUNCTIONS
    @Override
    public void updateBackground (int linkNum)
    {

    }
    
    @Override 
    public void updatePreviewImage (int linkNum)
    {

    }
    
    @Override
    public void updateInformationLabels(int linkNum)
    {

    }
    
    //DRAW FUNCTIONS
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
                background.paintSceneComponent(g2d);
                sceneMenu.drawMenu(g2d);

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
        }
    }
}