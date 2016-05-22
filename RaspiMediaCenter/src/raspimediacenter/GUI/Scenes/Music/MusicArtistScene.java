package raspimediacenter.GUI.Scenes.Music;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import raspimediacenter.Data.Models.Music.MusicArtistContainer;
import raspimediacenter.GUI.Components.Music.MusicBackground;
import raspimediacenter.GUI.Components.Music.MusicLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.ParserUtils;


public class MusicArtistScene extends Scene {

     //SCENE VARIABLES
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private MusicBackground background;
    private SceneMenu sceneMenu;
    
    // DATA MODEL VARIABLES
    private static MusicArtistContainer artistsList;
    
    // MUSIC SCENE FUNCTIONS
    public MusicArtistScene (){}
    
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
        artistsList = parser.parseArtistList("Music/artist-list.json", false);

         //Create Library List
        sceneMenu = new MusicLibrary();
        sceneMenu.setupMusicList(createArtPathsList(), createMenuList(), createTagsList(), createBioList());
        
        background = new MusicBackground();
        
        paintScene();
    }
    
    @Override 
    public ArrayList<String> createMenuList()
    {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (int x = 0 ; x < artistsList.artists.size(); x++)
        {
            menuList.add(artistsList.artists.get(x).artist.getName());
        }
        
        return menuList;
    }
    
    private ArrayList<String> createArtPathsList()
    {
        ArrayList<String> artPathsList = new ArrayList<>();
        String artPath = "";
        
        for (int x = 0 ; x < artistsList.artists.size(); x++)
        {
            artPath = "Music/"+artistsList.artists.get(x).artist.getName()+"/artist_portrait.jpg";
            artPathsList.add(artPath);
        }
        
        return artPathsList;
    }
    
     private ArrayList<String> createTagsList()
    {
        ArrayList<String> bioList = new ArrayList<>();
        
        for (int x = 0 ; x < artistsList.artists.size(); x++)
        {
            bioList.add(artistsList.artists.get(x).artist.tags.getTagString());
        }
        
        return bioList;
    }
    
    private ArrayList<String> createBioList()
    {
        ArrayList<String> bioList = new ArrayList<>();
        
        for (int x = 0 ; x < artistsList.artists.size(); x++)
        {
            bioList.add(artistsList.artists.get(x).artist.bio.getSummary());
        }
        
        return bioList;
    }
    
    @Override
    public void unloadScene() {
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