package raspimediacenter.GUI.Scenes.TV;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import raspimediacenter.Data.Models.TVSeriesContainer;
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

public class TVSeriesScene extends VideoLibraryScene{
    
    private static TVSeriesContainer tvSeries;
    private final ArrayList<JButton> seriesLinks = new ArrayList<>();
    private final ArrayList<String> infoLabels = new ArrayList<>(Arrays.asList("Network:", "Year:", "Status:", "Genre:", "Country:"));
    private static final ArrayList<BufferedImage> posters = new ArrayList<>();
    
    public TVSeriesScene() {
        super();
        Scene.setCurrentScene("TV Shows");
        
        ParserUtility parser = new ParserUtility();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        
        loadSeriesBackgrounds();
        loadSeriesPosters();
        bgCanvas.setBackgroundImage(0);
        
        //Create informationPanelGraphics
        infoPanelGraphics = new InformationPanelGraphics();
        infoPanel = new VideoInformationPanel();
        previewGraphics = new PosterGraphics();
        
        VideoLibraryScene.setPreviewImageWidth(previewGraphics.getPosterWidth());
        VideoLibraryScene.setPreviewImageHeight(previewGraphics.getPosterHeight());

        infoPanel.setupInfoPanel(infoLabels, generateTVSeriesInfo(0));
        infoPanel.createStarRating(tvSeries.results.get(0).getRatingAverage());
        infoPanel.createOverviewDisplay(tvSeries.results.get(0).getOverview());
        createLinkList();
        createListDisplay(seriesLinks);
    }
    
    private void createLinkList()
    {
        for (int x = 0; x < tvSeries.results.size(); x++)
        {
            if (!tvSeries.results.get(x).getName().matches("")){
                JButton button = new VideoListItemButton(tvSeries.results.get(x).getName(), this, x);
                button.addActionListener(new menuOptionSelected(tvSeries.results.get(x)));
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
        String genres = tvSeries.getGenresString(linkNum);

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
        if (linkNum >= posters.size())
        {
            return null;
        }
        else
        {
           return posters.get(linkNum); 
        }
    }
    
    //EVENT LISTENERS
    private class menuOptionSelected implements ActionListener {

        private TVSeries show;
        
        public menuOptionSelected (TVSeries show)
        {
            this.show = show;
        }
        
        @Override
        public void actionPerformed(ActionEvent ae) {
             
            try 
            {   
                String option = ((JButton)ae.getSource()).getText();
                SceneManager.loadScene("seasons", show);
            }
            catch (UnsupportedOperationException e) {
                
                System.out.println(e.getMessage());
            }
        }   
    }
}
