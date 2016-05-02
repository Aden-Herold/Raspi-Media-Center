package raspimediacenter.GUI.Scenes.TV;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.VideoComponents.EpisodePreviewGraphics;
import raspimediacenter.GUI.Components.VideoComponents.InformationPanelGraphics;
import raspimediacenter.GUI.Components.VideoComponents.VideoInformationPanel;
import raspimediacenter.GUI.Components.VideoComponents.VideoListItemButton;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.VideoLibraryScene;
import raspimediacenter.Logic.Utilities.ImageUtilities;
import raspimediacenter.Logic.Utilities.ParserUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility;

public class TVEpisodesScene extends VideoLibraryScene {

    private static TVSeries show;
    private static ArrayList<TVSeasonContainer> seasons;
    private static int seasonNumber = 1;
    private static int numberOfSeasons = 0;

    private ArrayList<JButton> episodeLinks = new ArrayList<>();
    private ArrayList<String> infoLabels = new ArrayList<>(Arrays.asList("Creator:", "Runtime:", "Air Date:", "Network:"));
    private static ArrayList<BufferedImage> posters;
    
    public TVEpisodesScene () {}
    
    public TVEpisodesScene (TVSeries show, int season)
    {
        TVEpisodesScene.show = show;
        numberOfSeasons = ScraperUtility.getNumberOfSeasons(show);
        seasons = parseSeasonsList();
        seasonNumber = season;
        
        Scene.setCurrentScene("TV Shows");
        Scene.setSubScene("Episodes");
        
        loadEpisodePreviews();
        bgCanvas.loadFanartImagesIntoMemory("TV Shows/"+show.getName()+"/series_backdrop.jpg");
        bgCanvas.setBackgroundImage(0);
        
        //Create informationPanelGraphics
        infoPanelGraphics = new InformationPanelGraphics();
        infoPanel = new VideoInformationPanel();
        previewGraphics = new EpisodePreviewGraphics();
        
        VideoLibraryScene.setPreviewImageWidth(previewGraphics.getPosterWidth());
        VideoLibraryScene.setPreviewImageHeight(previewGraphics.getPosterHeight());
        
        infoPanel.setupInfoPanel(infoLabels, generateEpisodeInfo(0));
        infoPanel.createStarRating(seasons.get(seasonNumber-1).episodes.get(0).getVoteAverage(), 5);
        infoPanel.createOverviewDisplay(seasons.get(seasonNumber-1).episodes.get(0).getOverview());
        createLinkList();
        createListDisplay(episodeLinks);
    }
    
    private ArrayList<TVSeasonContainer> parseSeasonsList ()
    {
        ParserUtility parser = new ParserUtility();
        ArrayList<TVSeasonContainer> seasonsList = new ArrayList<>();
        
        
        for (int x = 1; x <= numberOfSeasons; x++)
        {
            TVSeasonContainer season = parser.parseSeason("TV Shows/"+show.getName()+"/Season "+x+"/info.json", false);
            seasonsList.add(season);
        }
        
        return seasonsList;
    }
    
    private void createLinkList()
    {
        for (int x = 0; x < seasons.get(seasonNumber-1).episodes.size(); x++)
        {
            String epTitle = seasons.get(seasonNumber-1).episodes.get(x).getName();
            JButton button = new VideoListItemButton(epTitle, this, x);
            //button.addActionListener(new TVSeriesScene.menuOptionSelected(tvSeries.results.get(x)));
            episodeLinks.add(button);
        }
    }
    
    private void loadEpisodePreviews()
    {
        posters = new ArrayList<>();
        
        for (int x = 1; x <= seasons.get(seasonNumber-1).episodes.size(); x++)
        {
            String path = "TV Shows/"+show.getName()+"/Season " + seasonNumber + "/Stills/EP"+x+"_still.jpg";
            BufferedImage poster = ImageUtilities.getImageFromPath(path);
            posters.add(poster);
        }
    }
    
    @Override
    public void unloadResources()
    {
        super.unloadResources();
        
        seasons = null;
        episodeLinks = null;
        infoLabels = null;
        posters = null;
        show = null;
    }
    
    //STATIC FUNCTIONS
    public static TVSeriesContainer.TVSeries getShow ()
    {
        return show;
    }
    
    public static ArrayList<TVSeasonContainer> getTVSeasons ()
    {
        return seasons;
    }
    
    public static int getSeasonNumber ()
    {
        return seasonNumber;
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
    
    public static ArrayList<String> generateEpisodeInfo (int listNum)
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
        
        
        labelInfo.add(seasons.get(seasonNumber-1).episodes.get(listNum).getAirDate());
        labelInfo.add(show.networks.get(0).getNetwork());
        
        return labelInfo;
    }
}
