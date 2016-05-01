package raspimediacenter.GUI.Scenes.TV;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.VideoComponents.InformationPanelGraphics;
import raspimediacenter.GUI.Components.VideoComponents.PosterGraphics;
import raspimediacenter.GUI.Components.VideoComponents.VideoInformationPanel;
import raspimediacenter.GUI.Components.VideoComponents.VideoListItemButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.VideoLibraryScene;
import raspimediacenter.Logic.Utilities.ImageUtilities;
import raspimediacenter.Logic.Utilities.ParserUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility;

public class TVSeasonsScene extends VideoLibraryScene{
    
    private static TVSeries show;
    private static ArrayList<TVSeasonContainer> seasons;
    private static int numberOfSeasons = 0;

    private final ArrayList<JButton> seasonsLinks = new ArrayList<>();
    private final ArrayList<String> infoLabels = new ArrayList<>(Arrays.asList("Network:", "Year:", "Status:", "Genre:", "Country:"));
    private static final ArrayList<BufferedImage> posters = new ArrayList<>();
    
    public TVSeasonsScene () {}
    
    public TVSeasonsScene (TVSeries show)
    {
        TVSeasonsScene.show = show;
        numberOfSeasons = ScraperUtility.getNumberOfSeasons(show);
        seasons = parseSeasonsList();
        
        Scene.setCurrentScene("TV Shows");
        Scene.setSubScene("Seasons");
        
        loadSeasonPosters();
        bgCanvas.loadFanartImagesIntoMemory("TV Shows/"+show.getName()+"/series_backdrop.jpg");
        bgCanvas.setBackgroundImage(0);
        
        //Create informationPanelGraphics
        infoPanelGraphics = new InformationPanelGraphics();
        infoPanel = new VideoInformationPanel();
        previewGraphics = new PosterGraphics();
        
        VideoLibraryScene.setPreviewImageWidth(previewGraphics.getPosterWidth());
        VideoLibraryScene.setPreviewImageHeight(previewGraphics.getPosterHeight());
        
        infoPanel.setupInfoPanel(infoLabels, generateShowInfo());
        infoPanel.createStarRating(show.getRatingAverage(), 10);
        infoPanel.createOverviewDisplay(seasons.get(0).getOverview());
        createLinkList();
        createListDisplay(seasonsLinks);
    }
    
    private ArrayList<TVSeasonContainer> parseSeasonsList ()
    {
        ParserUtility parser = new ParserUtility();
        ArrayList<TVSeasonContainer> seasonsList = new ArrayList<>();
        
        for (int x = 1; x <= numberOfSeasons; x++)
        {
            TVSeasonContainer season = parser.parseSeason("TV Shows/" + show.getName() + "/Season " + x + "/info.json", false);
            seasonsList.add(season);
        }
        return seasonsList;
    }
    
    private void createLinkList()
    {
        for (int x = 0; x < numberOfSeasons; x++)
        {
            int seasonNumber = x+1;
            JButton button = new VideoListItemButton("Season "+seasonNumber, this, x);
            button.addActionListener(new menuOptionSelected(show, seasonNumber));
            seasonsLinks.add(button);
        }
    }
    
    private void loadSeasonPosters ()
    {
        posters.clear();
        
        for (int x = 1; x <= numberOfSeasons; x++)
        {
            String path = "TV Shows/"+show.getName()+"/Season " + x + "/season_poster.jpg";
            BufferedImage poster = ImageUtilities.getImageFromPath(path);
            posters.add(poster);
        }
    }
    
    public static TVSeries getShow ()
    {
        return show;
    }
    
    public static ArrayList<TVSeasonContainer> getTVSeasons ()
    {
        return seasons;
    }
    
    public static BufferedImage getPosterImage (int linkNum)
    {
        if (linkNum >= posters.size())
        {
            return null;
        }
        else
        {
           return posters.get(linkNum); 
        }
    }
    
    public static ArrayList<String> generateShowInfo ()
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
    
    //EVENT LISTENERS
    private class menuOptionSelected implements ActionListener {

        private TVSeries show;
        private int season;
        
        public menuOptionSelected (TVSeries show, int season)
        {
            this.show = show;
            this.season = season;
        }
        
        @Override
        public void actionPerformed(ActionEvent ae) {
             
            try 
            {   
                String option = ((JButton)ae.getSource()).getText();
                SceneManager.loadScene("episodes", show, season);
            }
            catch (UnsupportedOperationException e) {
                
                System.out.println(e.getMessage());
            }
        }   
    }
}
