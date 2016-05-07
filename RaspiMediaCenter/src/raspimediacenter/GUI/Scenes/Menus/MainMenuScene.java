package raspimediacenter.GUI.Scenes.Menus;

import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.Menu.MainMenu;
import raspimediacenter.GUI.Components.Menu.MenuHUD;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Utilities.ImageUtils;
import raspimediacenter.Logic.Utilities.ParserUtils;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuScene extends Scene {

    //SCENE VARIABLES
    private final ArrayList<String> menuList = new ArrayList<>(Arrays.asList("MOVIES", "TV SHOWS", "MUSIC", "IMAGES"));
    private final ArrayList<String> subHeaders = new ArrayList<>(Arrays.asList("WATCHED:", "EPISODES:", "PLAYLISTS:", "COLLECTIONS:"));
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    private MenuHUD menuHUD;
    
    // DATA MODEL VARIABLES
    MovieContainer movies;
    TVSeriesContainer tvSeries;
    
    public MainMenuScene (){}
    
    // GETTERS
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
    public ArrayList<String> getLabelContents(int linkNum) {
        return null;
    }
    
    private String[] getMenuInfo(String menuChoice)
    {
        String[] info = new String[2];
        
        if (menuChoice.toLowerCase().matches("movies"))
        {
            info[0] = String.valueOf(movies.results.size());
            info[1] = "0";
        }
        else if (menuChoice.toLowerCase().matches("tv shows"))
        {
            info[0] = String.valueOf(tvSeries.results.size());
            info[1] = "3929";
        }
        else if (menuChoice.toLowerCase().matches("music"))
        {
            info[0] = "212";
            info[1] = "14";
        }
        else if (menuChoice.toLowerCase().matches("images"))
        {
            info[0] = "256";
            info[1] = "4";
        }
        else
        {
            info[0] = "ERR";
            info[1] = "ERR";
        }
        
        return info;
    }

    // SETUP && TEAR DOWN
    @Override
    public void setupScene() {
        ParserUtils parser = new ParserUtils();
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        
        //Create Background
        background = new Background(true);
        
        //Create Library List
        sceneMenu = new MainMenu();
        sceneMenu.setupLibraryList(createMenuList());
        
        //Create Information Panel
        menuHUD = new MenuHUD();
        
        int hudWidth = (int)Math.floor(GUI.getScreenWidth()*0.4);
        int hudHeight = (int)Math.floor(GUI.getScreenHeight()/24);
        menuHUD.setBounds(GUI.getScreenWidth()/2-hudWidth/2,
                          GUI.getScreenHeight()-hudHeight,
                          hudWidth, hudHeight);
        
        String[] header = {"MOVIES:", "WATCHED:"};
        String[] content = getMenuInfo("movies");
        
        menuHUD.setupHUD(header, content);
        
        paintScene();
    }

    @Override
    public ArrayList<String> createMenuList() {
        return menuList;
    }

    @Override
    public void unloadScene() {
        background.unload();
        background = null;
        sceneMenu.unloadMenu();
        sceneMenu = null;
        menuHUD = null;
    }

    // EVENT FUNCTIONS
    @Override
    public void buttonClicked() {
        
        int focusedBtn = sceneMenu.getFocusedButtonPos();
        SceneManager.loadScene(menuList.get(focusedBtn));
    }

    // UPDATE FUNCTIONS
    @Override
    public void updateBackground(int linkNum) {
        BufferedImage backdrop = ImageUtils.getImageFromPath("Movies/"+movies.results.get(linkNum).getTitle()+"/movie_backdrop.jpg");
        background.setBackgroundImage(backdrop);
    }

    @Override
    public void updatePreviewImage(int linkNum) {
        BufferedImage poster = ImageUtils.getImageFromPath("Movies/"+movies.results.get(linkNum).getTitle()+"/movie_poster.jpg");
        background.setBackgroundImage(poster);
    }

    @Override
    public void updateInformationLabels(int linkNum) {
        
        String[] header = {menuList.get(linkNum)+":", subHeaders.get(linkNum)};
        String[] content = getMenuInfo(menuList.get(linkNum));
        
        menuHUD.updateHUD(header, content);
    }

    // DRAW FUNCTIONS
    @Override
    public void paintScene() {
        if (!painting)
        {
            painting = true;
            BufferStrategy buffer = GUI.getScreen().getBufferStrategy();
            Graphics2D g2d = (Graphics2D)(buffer.getDrawGraphics());
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try 
            {
                background.paintSceneComponent(g2d);
                sceneMenu.drawMenu(g2d);
                menuHUD.paintSceneComponent(g2d);

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