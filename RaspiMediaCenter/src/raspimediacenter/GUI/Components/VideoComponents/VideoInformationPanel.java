/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspimediacenter.GUI.Components.VideoComponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.Components.ItemScrollPanel;
import raspimediacenter.GUI.Components.OverviewDisplay;
import raspimediacenter.GUI.Components.StarRating;
import raspimediacenter.GUI.Components.StyledLabel;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;

/**
 *
 * @author Robert
 */
public class VideoInformationPanel {
    
    private OverviewDisplay overview;
    private StarRating starRating;
    
    private JPanel infoPanel;
    private final ArrayList<JLabel> infoLabels = new ArrayList<>();
    
    private ItemScrollPanel overviewPanel;
    
    
    public VideoInformationPanel () 
    {
        
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
            labelInfo = TVSeriesScene.generateTVSeriesInfo(linkNum);
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
            overview.updateOverview(TVSeriesScene.getTVSeries().results.get(linkNum).getOverview());
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
            starRating.updateRating(TVSeriesScene.getTVSeries().results.get(linkNum).getRatingAverage());
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
        

        infoPanel.setBounds((int)(Math.floor(SceneManager.getScreenWidth()*0.0196))+PosterGraphics.getPosterWidth(), 
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+50),
                (int) Math.floor(SceneManager.getScreenWidth()*0.4),
                240);
        SceneManager.getContentPane().add(infoPanel, 3, 0);
    }
    
    public JPanel setupInfoLabels (ArrayList<String> labels)
    {
        JPanel gridLayout = new JPanel(new GridLayout(0,1));
        gridLayout.setOpaque(false);
        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelsPanel.setOpaque(false);
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        
        for (String label : labels)
        {
            JLabel infoLabel = new StyledLabel(label, Font.PLAIN, fontSize, SwingConstants.RIGHT);
            Dimension bounds = infoLabel.getPreferredSize();
            bounds.height = (int)Math.floor(SceneManager.getScreenHeight()*0.03);
            infoLabel.setPreferredSize(bounds);
            gridLayout.add(infoLabel);
        }

        labelsPanel.add(gridLayout);
        /*
        labelsPanel.setBounds((int)(Math.floor(SceneManager.getScreenWidth()*0.0196)*2)+320-(240/2), 
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+50),
                300, 240);
                */
        //SceneManager.getContentPane().add(labelsPanel, 3, 0);
        return labelsPanel;
    }
    
    public JPanel createInfoDisplay (ArrayList<String> labelValues)
    {
        JPanel gridLayout = new JPanel(new GridLayout(0,1));
        gridLayout.setOpaque(false);
        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelsPanel.setOpaque(false);
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        
        for (String info : labelValues)
        {
            
            JLabel infoLabel = new StyledLabel(info, Font.PLAIN, fontSize, SwingConstants.LEFT);
            infoLabels.add(infoLabel);
            Dimension bounds = infoLabel.getPreferredSize();
            bounds.height = (int)Math.floor(SceneManager.getScreenHeight()*0.03);
            infoLabel.setPreferredSize(bounds);
            gridLayout.add(infoLabel);
        }
        
        labelsPanel.add(gridLayout);
        //labelsPanel.setPreferredSize(new Dimension(200, 200));
        /*
        labelsPanel.setBounds((int)(Math.floor(SceneManager.getScreenWidth()*0.0196)*3)+320+(240/2), 
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+50),
                (int) Math.floor(SceneManager.getScreenWidth()*0.4), 240);
                */
        //SceneManager.getContentPane().add(labelsPanel, 3, 0);
        
        return labelsPanel;
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

    public void createStarRating (float rating)
    {
        Dimension ratingSize = new Dimension();
        ratingSize.setSize(SceneManager.getScreenWidth()*0.4, 200);
        
        starRating = new StarRating();
        JPanel ratingPanel = starRating.getStarRatingPanel();
        
        starRating.updateRating(rating);
        
        ratingPanel.setBounds((int)Math.floor(SceneManager.getScreenWidth()*0.0196)*2+PosterGraphics.getPosterWidth(),
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*InformationPanelGraphics.getPanelScreenPercent()))+15),
                ratingSize.width, ratingSize.height);
        SceneManager.getContentPane().add(ratingPanel, 3, 0);
    }
}
