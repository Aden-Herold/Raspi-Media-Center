package raspimediacenter.GUI.Scenes.Movies;

import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.Data.Models.MovieContainer.Movie;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.FileLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoInformationPanel;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Utilities.ImageUtils;
import raspimediacenter.Logic.Utilities.ParserUtils;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;

public class MoviesScene extends Scene {

    //SCENE VARIABLES
    private final ArrayList<String> labelHeaders = new ArrayList<>(Arrays.asList("Network:", "Year:", "Status:", "Genre:", "Country:"));
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    private VideoInformationPanel infoPanel;
    
    // DATA MODEL VARIABLES
    MovieContainer movies;
    
    public MoviesScene (){}
    
    // GETTERS
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
    public ArrayList<String> getLabelContents(int linkNum) {
        String genres = movies.getGenresString(linkNum);
        String langs = movies.getLanguagesString(linkNum);

        ArrayList<String> labelInfo = new ArrayList<>();
        labelInfo.add(movies.results.get(linkNum).getReleaseYear());
        labelInfo.add("FEATURE NOT ADDED");
        labelInfo.add(String.valueOf(movies.results.get(linkNum).getRuntime()) + " mins");
        labelInfo.add(langs);
        labelInfo.add(genres);
        
        return labelInfo;
    }

    // SETUP && TEAR DOWN
    @Override
    public void setupScene() {
        ParserUtils parser = new ParserUtils();
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        
        //Create Background
        BufferedImage backdrop = ImageUtils.getImageFromPath("Movies/"+movies.results.get(0).getTitle()+"/movie_backdrop.jpg");
        background = new Background(false);
        background.setBackgroundImage(backdrop);
        
        //Create Library List
        sceneMenu = new FileLibrary();
        sceneMenu.setupLibraryList(createMenuList());
        
        //Create Information Panel
        infoPanel = new VideoInformationPanel();
        infoPanel.setupInformationPanel("poster");
        BufferedImage previewImage = ImageUtils.getImageFromPath("Movies/"+movies.results.get(0).getTitle()+"/movie_poster.jpg");
        infoPanel.getPreviewGraphics().setCurrentPoster(previewImage);
        infoPanel.setupInformationLabels(labelHeaders);
        infoPanel.setupStarRating(movies.results.get(0).getVoteAverage());
        infoPanel.setupHUDPanel(movies.results.get(0).getTitle(), 
                "Movies: "+movies.results.size());
        infoPanel.setupOverview(movies.results.get(0).getOverview());
        
        paintScene();
    }

    @Override
    public ArrayList<String> createMenuList() {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (Movie movie : movies.results)
        {
            menuList.add(movie.getTitle());
        }
        
        return menuList;
    }

    @Override
    public void unloadScene() {
        background = null;
        sceneMenu.unloadMenu();
        sceneMenu = null;
        infoPanel = null;
    }

    // EVENT FUNCTIONS
    @Override
    public void buttonClicked() {
        
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
        infoPanel.getPreviewGraphics().setCurrentPoster(poster);
    }

    @Override
    public void updateInformationLabels(int linkNum) {
        infoPanel.getInfoLabels().updateLabelContent(getLabelContents(linkNum));
        infoPanel.getStarRating().updateRating(movies.results.get(linkNum).getVoteAverage());
        infoPanel.getHUD().updateHUD(movies.results.get(linkNum).getTitle(), 
                "Movies: "+movies.results.size());
        infoPanel.getOverview().setText(movies.results.get(linkNum).getOverview());
    }

    // DRAW FUNCTIONS
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
                infoPanel.getPanelGraphics().createInformationPanel(g2d);
                infoPanel.getPreviewGraphics().displayPoster(g2d);
                infoPanel.getInfoLabels().drawLabels(g2d);
                infoPanel.getStarRating().drawStarRating(g2d);
                infoPanel.getHUD().drawHUD(g2d);
                infoPanel.getOverview().paintSceneComponent(g2d);

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
