package raspimediacenter.GUI.Scenes.TV;

import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.FileLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoInformationPanel;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Utilities.ImageUtils;
import raspimediacenter.Logic.Utilities.ParserUtils;
import raspimediacenter.Logic.Utilities.ScraperUtils;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;

public class TVSeasonsScene extends Scene {

    //SCENE VARIABLES
    private final ArrayList<String> labelHeaders = new ArrayList<>(Arrays.asList("Network:", "Year:", "Status:", "Genre:", "Country:"));
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    private VideoInformationPanel infoPanel;
    
    //DATA MODEL VARIABLES
    private static TVSeries show;  
    private static ArrayList<TVSeasonContainer> seasons;
    private int numberOfSeasons = 0;
    
    public TVSeasonsScene (TVSeries show)
    {
        TVSeasonsScene.show = show;
    }
    
    // TV SEASONS METHODS
    private ArrayList<TVSeasonContainer> parseSeasonsList ()
    {
        ParserUtils parser = new ParserUtils();
        ArrayList<TVSeasonContainer> seasonsList = new ArrayList<>();
        
        for (int x = 1; x <= numberOfSeasons; x++)
        {
            TVSeasonContainer season = parser.parseSeason("TV Shows/" + show.getName() + "/Season " + x + "/info.json", false);
            seasonsList.add(season);
        }
        return seasonsList;
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
        String genres = show.getGenresString();

        ArrayList<String> labelInfo = new ArrayList<>();
        labelInfo.add(show.networks.get(0).getNetwork());
        labelInfo.add(show.getStartYear() + " - " + show.getEndYear());
        labelInfo.add(show.getStatus());
        labelInfo.add(genres);
        labelInfo.add(show.getOriginCountry()[0]);
        
        return labelInfo;
    }  
    
    // SETUP/TEARDOWN FUNCTIONS
    @Override
    public void setupScene() {
        numberOfSeasons = ScraperUtils.getNumberOfSeasons(show);
        seasons = parseSeasonsList();
        
        //Create Background
        BufferedImage backdrop = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+show.getName()+"/series_backdrop.jpg");
        background = new Background(false);
        background.setBackgroundImage(backdrop);
        
        //Create Library List
        sceneMenu = new FileLibrary();
        sceneMenu.setupLibraryList(createMenuList());
        
        //Create Information Panel
        infoPanel = new VideoInformationPanel();
        infoPanel.setupInformationPanel("poster");
        
        //Create Preview Display
        BufferedImage previewImage = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+show.getName()+"/series_poster.jpg");
        infoPanel.getPreviewGraphics().setCurrentPoster(previewImage);
        
        //Create Information Labels
        infoPanel.setupInformationLabels(labelHeaders);
        
        //Create Star Rating
        infoPanel.setupStarRating(show.getRatingAverage());
        
        //Create Top Corner HUD
        infoPanel.setupHUDPanel(show.getName(), 
                "Seasons: "+ScraperUtils.getNumberOfSeasons(show));
        
        //Create Overview
        infoPanel.setupOverview(show.getOverview());
        
        //setupRenderingThread();
        paintScene();
    }
    
    @Override 
    public ArrayList<String> createMenuList()
    {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (int x = 0; x < numberOfSeasons; x++)
        {
            int seasonNumber = x+1;
            menuList.add("Season "+seasonNumber);
        }
        
        return menuList;
    }
    
    @Override
    public void unloadScene() {
        background = null;
        sceneMenu = null;
        infoPanel = null;
        seasons = null;
        numberOfSeasons = 0;
    }
    
    // EVENT FUNCTIONS
    @Override
    public void buttonClicked ()
    {
        int focusedBtn = sceneMenu.getFocusedButtonPos();
        SceneManager.loadEpisodes(show, focusedBtn+1);
    }
    
    //UPDATE FUNCTIONS
    @Override
    public void updateBackground (int linkNum)
    {
        BufferedImage backdrop = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+show.getName()+"/series_backdrop.jpg");
        background.setBackgroundImage(backdrop);
    }
    
    @Override 
    public void updatePreviewImage (int linkNum)
    {
        linkNum+=1;
        BufferedImage poster = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+show.getName()+"/Season "+linkNum+"/season_poster.jpg");
        infoPanel.getPreviewGraphics().setCurrentPoster(poster);
    }
    
    @Override
    public void updateInformationLabels(int linkNum)
    {
        infoPanel.getInfoLabels().updateLabelContent(getLabelContents(linkNum));
        infoPanel.getStarRating().updateRating(show.getRatingAverage());
        infoPanel.getHUD().updateHUD(show.getName(), 
                "Seasons: "+ScraperUtils.getNumberOfSeasons(show));
        
        //Look for season overview ELSE use series overview
        String seasonOverview = seasons.get(linkNum).getOverview();
                
        if (seasonOverview != null)
        {
            if (seasonOverview.matches(""))
            {
                seasonOverview = show.getOverview();
            }
        }
        
        infoPanel.getOverview().setText(seasonOverview);
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