package raspimediacenter.GUI.Scenes.TV;

import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.FileLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoInformationPanel;
import raspimediacenter.GUI.GUI;
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
import raspimediacenter.GUI.SceneManager;

public class TVEpisodesScene extends Scene {

    //SCENE VARIABLES
    private final ArrayList<String> labelHeaders = new ArrayList<>(Arrays.asList("Creator:", "Runtime:", "Air Date:", "Network:"));
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    private VideoInformationPanel infoPanel;
    
    //DATA MODEL VARIABLES
    private static TVSeries show;  
    private static ArrayList<TVSeasonContainer> seasons;
    private static int seasonNumber = 1;
    private int numberOfSeasons = 0;
    
    public TVEpisodesScene (TVSeries show, int seasonNumber)
    {
        TVEpisodesScene.show = show;
        TVEpisodesScene.seasonNumber = seasonNumber-1;
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
        labelInfo.add(show.created_by.get(0).getName());
        
        String[] runtimes = show.getEpisodeRunTime();
        if (runtimes.length >= 2)
        {
            labelInfo.add(runtimes[0] + ", " + runtimes[1] + " mins");
        }
        else
        {
            labelInfo.add(runtimes[0] + " mins"); 
        }
        
        
        labelInfo.add(seasons.get(seasonNumber).episodes.get(linkNum).getAirDate());
        labelInfo.add(show.networks.get(0).getNetwork());
        
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
        infoPanel.setupInformationPanel("episode");
        
        //Create Preview Display
        int season = seasonNumber + 1;
        BufferedImage previewImage = ImageUtils.getImageFromPath("TV Shows/"+show.getName()+"/Season " + season + "/Stills/EP"+1+"_still.jpg");
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
        
        for (int x = 0; x < seasons.get(seasonNumber).episodes.size(); x++)
        {
            String epTitle = seasons.get(seasonNumber).episodes.get(x).getName();
            menuList.add(epTitle);
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
        SceneManager.loadVideo(show, seasonNumber, seasons.get(seasonNumber).episodes.get(focusedBtn).getName());
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
        int season = seasonNumber + 1;
        BufferedImage poster = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+show.getName()+"/Season " + season + "/Stills/EP"+linkNum+"_still.jpg");
        infoPanel.getPreviewGraphics().setCurrentPoster(poster);
    }
    
    @Override
    public void updateInformationLabels(int linkNum)
    {
        infoPanel.getInfoLabels().updateLabelContent(getLabelContents(linkNum));
        infoPanel.getStarRating().updateRating(seasons.get(seasonNumber).episodes.get(linkNum).getVoteAverage());
        infoPanel.getHUD().updateHUD(show.getName(), 
                "Seasons: "+ScraperUtils.getNumberOfSeasons(show));
        infoPanel.getOverview().setText(seasons.get(seasonNumber).episodes.get(linkNum).getOverview());
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