package raspimediacenter.GUI.Components.VideoComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.Components.ItemScrollPanel;
import raspimediacenter.GUI.Components.OverviewDisplay;
import raspimediacenter.GUI.Components.StarRating;
import raspimediacenter.GUI.Components.StyledLabel;
import raspimediacenter.GUI.Components.TopCornerInfoPanel;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.TV.TVEpisodesScene;
import raspimediacenter.GUI.Scenes.TV.TVSeasonsScene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;
import raspimediacenter.GUI.Scenes.VideoLibraryScene;
import raspimediacenter.Logic.Utilities.ScraperUtility;

public class VideoInformationPanel {
    
    private TopCornerInfoPanel topInfoPanel;
    private OverviewDisplay overview;
    private StarRating starRating;
    private JPanel infoPanel;
    private final ArrayList<JLabel> infoLabels = new ArrayList<>();
    
    private ItemScrollPanel overviewPanel;
    
    
    public VideoInformationPanel () 
    {
        
    }

    public TopCornerInfoPanel getTopInfoPanel ()
    {
        return topInfoPanel;
    }
    
    public void updateInformation (int linkNum)
    {
        ArrayList<String> labelInfo = null;
        
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            labelInfo = MoviesScene.generateMoviesInfo(linkNum);
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            if (Scene.getSubScene().toLowerCase().matches("seasons"))
            {
                labelInfo = TVSeasonsScene.generateShowInfo();
            }
            else if (Scene.getSubScene().toLowerCase().matches("episodes"))
            {
                labelInfo = TVEpisodesScene.generateEpisodeInfo(linkNum);
            }
            else
            {
                labelInfo = TVSeriesScene.generateTVSeriesInfo(linkNum);
            }  
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            labelInfo = TVSeriesScene.generateTVSeriesInfo(linkNum);
        }

        updateInfoLabels(labelInfo);
    }
    
    private void updateInfoLabels (ArrayList<String> labelInfo)
    {
        if (labelInfo != null)
        {
            for (int x = 0; x < infoLabels.size(); x++)
            {
                infoLabels.get(x).setText(labelInfo.get(x));
            }
        }
    }
    
    public void updateOverview (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            overview.updateOverview(MoviesScene.getMovies().results.get(linkNum).getOverview());
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            if (Scene.getSubScene().toLowerCase().matches("seasons"))
            {
                String seasonOverview = TVSeasonsScene.getTVSeasons().get(linkNum).getOverview();
                
                if (seasonOverview != null)
                {
                    if (seasonOverview.matches(""))
                    {
                        seasonOverview = TVSeasonsScene.getShow().getOverview();
                    }
                }
                
                overview.updateOverview(seasonOverview);
            }
            else if (Scene.getSubScene().toLowerCase().matches("episodes"))
            {
                overview.updateOverview(TVEpisodesScene.getTVSeasons().get(TVEpisodesScene.getSeasonNumber()-1).episodes.get(linkNum).getOverview());
            }
            else
            {
                overview.updateOverview(TVSeriesScene.getTVSeries().results.get(linkNum).getOverview());
            }
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        }
    }

    public void updateStarRating (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            starRating.updateRating(MoviesScene.getMovies().results.get(linkNum).getVoteAverage());
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            if (Scene.getSubScene().toLowerCase().matches("seasons"))
            {
                starRating.updateRating(TVSeasonsScene.getShow().getRatingAverage());
            }
            else if (Scene.getSubScene().toLowerCase().matches("episodes"))
            {
                starRating.updateRating(TVEpisodesScene.getTVSeasons().get(TVEpisodesScene.getSeasonNumber()-1).episodes.get(linkNum).getVoteAverage());
            }
            else
            {
                starRating.updateRating(TVSeriesScene.getTVSeries().results.get(linkNum).getRatingAverage());
            }   
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        }
    }
    
    public void updateTopInfoPanel (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {

        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            if (Scene.getSubScene().toLowerCase().matches("seasons"))
            {

            }
            else if (Scene.getSubScene().toLowerCase().matches("episodes"))
            {

            }
            else
            {
                topInfoPanel.setHeaderInfo(TVSeriesScene.getTVSeries().results.get(linkNum).getName().toUpperCase());
                topInfoPanel.setContentInfo("SEASONS: "+ScraperUtility.getNumberOfSeasons(TVSeriesScene.getTVSeries().results.get(linkNum)));
            }   
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        } 
    }
    
    public void setupInfoPanel (ArrayList<String> labels, ArrayList<String> labelValues)
    {
        int spacing = (int)(Math.floor(SceneManager.getScreenWidth()*0.0196));
        
        infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, spacing, 0));
        infoPanel.setOpaque(false);
        
        JPanel infoLabelsPanel = setupInfoLabels(labels);
        JPanel labelInfoPanel = createInfoDisplay(labelValues);
        
        infoPanel.add(infoLabelsPanel);
        infoPanel.add(labelInfoPanel);
        

        infoPanel.setBounds((int)(Math.floor(SceneManager.getScreenWidth()*0.0196))+VideoLibraryScene.getPreviewImageWidth(), 
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+50),
                (int) Math.floor(SceneManager.getScreenWidth()*0.4),
                240);
        SceneManager.getContentPane().add(infoPanel, 3, 0);
    }
    
    public JPanel setupInfoLabels (ArrayList<String> labels)
    {
        JPanel boxPanel = new JPanel();
        boxPanel.setOpaque(false);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        
        for (String label : labels)
        {
            JLabel infoLabel = new StyledLabel(label, Font.PLAIN, fontSize, SwingConstants.RIGHT);
            infoLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            boxPanel.add(infoLabel);
            boxPanel.add(Box.createRigidArea(new Dimension(0,5)));
        }

        return boxPanel;
    }
    
    public void createTopCornerInfo (String side, String header, String content) {
        
        topInfoPanel = new TopCornerInfoPanel(side);
        JPanel panel = topInfoPanel.createCategoryInfo(header, content);
        SceneManager.getContentPane().add(panel, 3, 0);
    }
    
    public JPanel createInfoDisplay (ArrayList<String> labelValues)
    {
        JPanel boxPanel = new JPanel();
        boxPanel.setOpaque(false);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        
        for (String info : labelValues)
        {
            
            JLabel infoLabel = new StyledLabel(info, Font.PLAIN, fontSize, SwingConstants.LEFT);
            infoLabels.add(infoLabel);
            boxPanel.add(infoLabel);
            boxPanel.add(Box.createRigidArea(new Dimension(0,5)));
        }
              
        return boxPanel;
    }
    
    public void createOverviewDisplay (String description) 
    {
        overview = new OverviewDisplay(description);
        overviewPanel = overview.getOverviewPanel();
        
        Dimension descSize = new Dimension();
        descSize.setSize(SceneManager.getScreenWidth()*0.45, (int)Math.floor(SceneManager.getScreenHeight()*0.18));
        
        overviewPanel.setBounds(SceneManager.getScreenWidth()-descSize.width-50,
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+15),
                descSize.width, descSize.height);
        
        SceneManager.getContentPane().add(overviewPanel, 3, 0); 
    }

    public void createStarRating (float rating, int maxRating)
    {
        Dimension ratingSize = new Dimension();
        ratingSize.setSize(SceneManager.getScreenWidth()*0.4, 200);
        
        starRating = new StarRating(maxRating);
        JPanel ratingPanel = starRating.getStarRatingPanel();
        
        starRating.updateRating(rating);
        
        ratingPanel.setBounds((int)Math.floor(SceneManager.getScreenWidth()*0.0196)*2+VideoLibraryScene.getPreviewImageWidth(),
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+15),
                ratingSize.width, ratingSize.height);
        SceneManager.getContentPane().add(ratingPanel, 3, 0);
    }
}
