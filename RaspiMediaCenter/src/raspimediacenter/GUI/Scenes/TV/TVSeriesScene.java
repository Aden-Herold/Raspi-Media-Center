package raspimediacenter.GUI.Scenes.TV;

import raspimediacenter.Data.Models.TVSeriesContainer;
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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class TVSeriesScene extends Scene {

    //SCENE VARIABLES
    private final ArrayList<String> labelHeaders = new ArrayList<>(Arrays.asList("Network:", "Year:", "Status:", "Genre:", "Country:"));
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    private VideoInformationPanel infoPanel;
    
    // DATA MODEL VARIABLES
    private static TVSeriesContainer tvSeries;
    
    // TV SERIES SCENE FUNCTIONS
    public TVSeriesScene (){}
    
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
        String genres = tvSeries.getGenresString(linkNum);

        ArrayList<String> labelInfo = new ArrayList<>();
        labelInfo.add(tvSeries.results.get(linkNum).networks.get(0).getNetwork());
        labelInfo.add(tvSeries.results.get(linkNum).getStartYear() + " - " + tvSeries.results.get(linkNum).getEndYear());
        labelInfo.add(tvSeries.results.get(linkNum).getStatus());
        labelInfo.add(genres);
        labelInfo.add(tvSeries.results.get(linkNum).getOriginCountry()[0]);
        
        return labelInfo;
    }
    
    // SETUP/TEARDOWN FUNCTIONS
    @Override
    public void setupScene() {
        ParserUtils parser = new ParserUtils();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        
        //Create Background
        BufferedImage backdrop = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+tvSeries.results.get(0).getName()+"/series_backdrop.jpg");
        background = new Background(false);
        background.setBackgroundImage(backdrop);
        
        //Create Library List
        sceneMenu = new FileLibrary();
        sceneMenu.setupLibraryList(createMenuList());
        
        //Create Information Panel
        infoPanel = new VideoInformationPanel();
        infoPanel.setupInformationPanel("poster");
        BufferedImage previewImage = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+tvSeries.results.get(0).getName()+"/series_poster.jpg");
        infoPanel.getPreviewGraphics().setCurrentPoster(previewImage);
        infoPanel.setupInformationLabels(labelHeaders);
        infoPanel.setupStarRating(tvSeries.results.get(0).getRatingAverage());
        infoPanel.setupHUDPanel(tvSeries.results.get(0).getName(), 
                "Seasons: "+ScraperUtils.getNumberOfSeasons(tvSeries.results.get(0)));
        infoPanel.setupOverview(tvSeries.results.get(0).getOverview());
        
        paintScene();
    }
    
    @Override 
    public ArrayList<String> createMenuList()
    {
        ArrayList<String> menuList = new ArrayList<>();
        
        for (TVSeries show : tvSeries.results)
        {
            menuList.add(show.getName());
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
    public void buttonClicked ()
    {
        int focusedBtn = sceneMenu.getFocusedButtonPos();
        SceneManager.loadSeasons(tvSeries.results.get(focusedBtn));
    }
    
    //UPDATE FUNCTIONS
    @Override
    public void updateBackground (int linkNum)
    {
        BufferedImage backdrop = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+tvSeries.results.get(linkNum).getName()+"/series_backdrop.jpg");
        background.setBackgroundImage(backdrop);
    }
    
    @Override 
    public void updatePreviewImage (int linkNum)
    {
        BufferedImage poster = ImageUtils.getImageFromPath(System.getProperty("user.dir") + "/TV Shows/"+tvSeries.results.get(linkNum).getName()+"/series_poster.jpg");
        infoPanel.getPreviewGraphics().setCurrentPoster(poster);
    }
    
    @Override
    public void updateInformationLabels(int linkNum)
    {
        infoPanel.getInfoLabels().updateLabelContent(getLabelContents(linkNum));
        infoPanel.getStarRating().updateRating(tvSeries.results.get(linkNum).getRatingAverage());
        infoPanel.getHUD().updateHUD(tvSeries.results.get(linkNum).getName(), 
                "Seasons: "+ScraperUtils.getNumberOfSeasons(tvSeries.results.get(linkNum)));
        infoPanel.getOverview().setText(tvSeries.results.get(linkNum).getOverview());
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