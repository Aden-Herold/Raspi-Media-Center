package raspimediacenter.GUI.Scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.ItemScrollPanel;
import raspimediacenter.GUI.Components.ListItemButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtilities;
import raspimediacenter.Logic.Utilities.ParserUtility;

public class TVSeriesScene extends FileBrowserScene{
    
    private TVSeriesContainer tvSeries;
    
    public TVSeriesScene(SceneManager sceneManager) {
        super(sceneManager);
        Scene.setCurrentScene("TV Shows");
        
        ParserUtility parser = new ParserUtility();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        
        currentFanart = ImageUtilities.getImageFromPath("TV Shows/" + tvSeries.results.get(1).getName() + "/series_backdrop.jpg");
        currentPoster = ImageUtilities.getImageFromPath("TV Shows/" + tvSeries.results.get(1).getName() + "/series_poster.jpg");
        
        loadBackground("TV Shows/" + tvSeries.results.get(1).getName() + "/series_backdrop.jpg");
        
        createListDisplay("");
    }
    
    @Override
    public void createListDisplay(String directory)
    {
        Dimension listSize = new Dimension();
        listSize.setSize(SceneManager.getScreenWidth()*0.25, SceneManager.getScreenHeight()*0.7);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setOpaque(false);

        for (TVSeries show : tvSeries.results)
        {
            ListItemButton button = new ListItemButton(show.getName(), this);
            panel.add(button);
        }
        
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flowPanel.setOpaque(false);
        flowPanel.add(panel);
        
        ItemScrollPanel filesList = new ItemScrollPanel(flowPanel, this);
        filesList.setBounds(SceneManager.getScreenWidth()-listSize.width, 0, listSize.width, listSize.height);
        SceneManager.getContentPane().add(filesList, 1, 0);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        drawDetailsHUD(paint);
        drawPoster(paint);
    }
}
