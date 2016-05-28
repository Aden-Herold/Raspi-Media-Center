package raspimediacenter.GUI.Scenes.Music;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import raspimediacenter.Data.Models.Music.MusicAlbumList;
import raspimediacenter.Data.Models.Music.MusicArtistContainer.MusicArtist;
import raspimediacenter.GUI.Components.Music.MusicBackground;
import raspimediacenter.GUI.Components.Music.MusicLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.ParserUtils;

public class MusicAlbumScene extends Scene {

    private final String SCENE_NAME = "MUSIC ALBUMS";
    
     //SCENE VARIABLES
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private MusicBackground background;
    private SceneMenu sceneMenu;
    
    // DATA MODEL VARIABLES
    private static MusicArtist artist;
    private static MusicAlbumList albumList;
    
    // MUSIC SCENE FUNCTIONS
    public MusicAlbumScene (MusicArtist artist)
    {
        MusicAlbumScene.artist = artist;
    }
    
    // SCENE FUNCTIONS
    //GETTERS
    @Override
    public String getSceneName ()
    {
        return SCENE_NAME;
    }
    
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
        albumList = parser.parseAlbumList("Music/"+artist.artist.getName()+"/album-list.json");

         //Create Library List
        sceneMenu = new MusicLibrary();
        sceneMenu.setupMusicList(createArtPathsList(), createMenuList(), createBioList());
        
        background = new MusicBackground();
        
        paintScene();
    }
    
    @Override 
    public ArrayList<String> createMenuList()
    {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (int x = 0 ; x < albumList.albums.size(); x++)
        {
            menuList.add(albumList.albums.get(x).getName());
        }
        
        return menuList;
    }
    
    private ArrayList<String> createArtPathsList()
    {
        ArrayList<String> artPathsList = new ArrayList<>();
        String artPath = "";
        
        for (int x = 0 ; x < albumList.albums.size(); x++)
        {
            artPath = "Music/"+artist.artist.getName()+"/"+albumList.albums.get(x).getName()+"/album_art.jpg";
            artPathsList.add(artPath);
        }
        
        return artPathsList;
    }
    
    private ArrayList<String> createBioList()
    {
        ArrayList<String> bioList = new ArrayList<>();
        
        for (int x = 0 ; x < albumList.albums.size(); x++)
        {
            if (albumList.albums.get(x).wiki != null)
            {
                bioList.add(albumList.albums.get(x).wiki.getSummary());
            }
            else
            {
                bioList.add("");
            }
        }
        
        return bioList;
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
        int linkNum = sceneMenu.getFocusedButtonPos();
        SceneManager.loadTracks(artist, albumList.albums.get(linkNum));
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
