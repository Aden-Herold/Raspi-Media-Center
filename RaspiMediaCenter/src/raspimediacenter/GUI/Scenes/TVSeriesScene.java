package raspimediacenter.GUI.Scenes;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.GUI.Components.ListItemButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtilities;
import raspimediacenter.Logic.Utilities.ParserUtility;

public class TVSeriesScene extends FileBrowserScene{
    
    private static TVSeriesContainer tvSeries;
    private ArrayList<JButton> seriesLinks = new ArrayList<>();
    private final ArrayList<String> infoLabels = new ArrayList<>(Arrays.asList("Network:", "Year:", "Status:", "Genre:", "Country:"));
    private static ArrayList<BufferedImage> posters = new ArrayList<>();
    
    public TVSeriesScene(SceneManager sceneManager) {
        super(sceneManager);
        Scene.setCurrentScene("TV Shows");
        
        ParserUtility parser = new ParserUtility();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        
        loadSeriesBackgrounds();
        loadSeriesPosters();
        bgCanvas.setBackgroundImage(0);

        setupInfoLabels(infoLabels);
        createInfoDisplay(generateTVSeriesInfo(1));
        createOverviewDisplay(tvSeries.results.get(0).getOverview());
        createLinkList();
        createListDisplay(seriesLinks);
        createStarRating(tvSeries.results.get(0).getRatingAverage());
    }
    
    private void createLinkList()
    {
        for (int x = 0; x < tvSeries.results.size(); x++)
        {
            if (!tvSeries.results.get(x).getName().matches("")){
                JButton button = new ListItemButton(tvSeries.results.get(x).getName(), this, x);
                seriesLinks.add(button);
            }
        }
    }
    
    private void loadSeriesBackgrounds ()
    {
        ArrayList<String> backgroundPaths = new ArrayList<>();
        
        for (int x = 0; x < tvSeries.results.size(); x++)
        {
            if (!tvSeries.results.get(x).getName().matches(""))
            {
                String path = "TV Shows/" + tvSeries.results.get(x).getName() + "/series_backdrop.jpg";
                backgroundPaths.add(path);
            }
        }
        
        bgCanvas.loadFanartImagesIntoMemory(backgroundPaths);
    }
    
    private void loadSeriesPosters ()
    {
        for (int x = 0; x < tvSeries.results.size(); x++)
        {
            if (!tvSeries.results.get(x).getName().matches(""))
            {
                String path = "TV Shows/" + tvSeries.results.get(x).getName() + "/series_poster.jpg";
                BufferedImage poster = ImageUtilities.getImageFromPath(path);
                posters.add(poster);
            }
        }
    }
    
    public static TVSeriesContainer getTVSeries ()
    {
        return tvSeries;
    }
    
    public static ArrayList<String> generateTVSeriesInfo (int linkNum)
    {
        String genres = "";
        
        for (int x = 0; x < tvSeries.results.get(linkNum).genres.size(); x++)
        {
            if (x < tvSeries.results.get(linkNum).genres.size()-1)
            {
                genres += tvSeries.results.get(linkNum).genres.get(x).getGenre() + ", ";
            }
            else
            {
                genres += tvSeries.results.get(linkNum).genres.get(x).getGenre();
            }
        }

        ArrayList<String> labelInfo = new ArrayList<>();
        labelInfo.add(tvSeries.results.get(linkNum).networks.get(0).getNetwork());
        labelInfo.add(tvSeries.results.get(linkNum).getStartYear() + " - " + tvSeries.results.get(linkNum).getEndYear());
        labelInfo.add(tvSeries.results.get(linkNum).getStatus());
        labelInfo.add(genres);
        labelInfo.add(tvSeries.results.get(linkNum).getOriginCountry()[0]);
        
        return labelInfo;
    }
    
    public static BufferedImage getPosterImage (int linkNum)
    {
        return posters.get(linkNum);
    }
}
