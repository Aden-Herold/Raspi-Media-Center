package raspimediacenter.GUI.Components.Video;

import raspimediacenter.GUI.Components.LibraryOverview;
import raspimediacenter.GUI.Components.StarRating;
import raspimediacenter.GUI.Components.LibraryHUD;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import java.awt.Dimension;
import java.util.ArrayList;

public class VideoInformationPanel {
    /*
    private TopCornerInfoPanel topInfoPanel;
    private OverviewDisplay overview;
    private StarRating starRating;
    private JPanel infoPanel;
    private final ArrayList<JLabel> infoLabels = new ArrayList<>();
    
    private ItemScrollPanel overviewPanel;
    */
    private final static double PANEL_SCEEN_PERCENT = 0.15; 
    private static final int PANEL_HEIGHT = (int)(Math.floor(GUI.getScreenHeight()/5));
    
    private InformationPanelGraphics panelGraphics;
    private VideoPreviewGraphics previewGraphics;
    private VideoInformationLabels infoLabels;
    private StarRating starRating;
    private LibraryHUD hudDisplay;
    private LibraryOverview overview;
    
    
    public VideoInformationPanel () {}
    
    //GETTERS
    public InformationPanelGraphics getPanelGraphics()
    {
        return panelGraphics;
    }
    
    public VideoPreviewGraphics getPreviewGraphics()
    {
        return previewGraphics;
    }
    
    public VideoInformationLabels getInfoLabels()
    {
        return infoLabels;
    }
    
    public StarRating getStarRating()
    {
        return starRating;
    }
    
    public LibraryHUD getHUD()
    {
        return hudDisplay;
    }
    
    public LibraryOverview getOverview()
    {
        return overview;
    }
    
    //SETTERS
    
    //FUNCTIONS
    public void setupInformationPanel(String previewType)
    {
        panelGraphics = new InformationPanelGraphics(PANEL_SCEEN_PERCENT, PANEL_HEIGHT);
        
        if (previewType.toLowerCase().matches("poster"))
        {
            previewGraphics = new PosterGraphics();
        }
        else
        {
            previewGraphics = new EpisodePreviewGraphics();
        }
    }
    
    public void setupInformationLabels (ArrayList<String> headers)
    {
        infoLabels = new VideoInformationLabels();
        
        int x = (int)(Math.floor(GUI.getScreenWidth()*0.0196))+previewGraphics.getPosterWidth();
        int y = (int) (GUI.getScreenHeight()-PANEL_HEIGHT+Math.floor(GUI.getScreenHeight()/24)*1.88);
        int width = (int) Math.floor(GUI.getScreenWidth()*0.4);
        int height = 240;
        
        infoLabels.setBounds(x, y, width, height);
        
        ArrayList<String> contents = SceneManager.getCurrentScene().getLabelContents(0);
        infoLabels.createLabelHeader(headers);
        infoLabels.createLabelContent(contents);
    }
    
    public void setupStarRating (float rating)
    {
        int x = (int)(Math.floor(GUI.getScreenWidth()*0.0196)*2)+previewGraphics.getPosterWidth();
        int y = (int)(GUI.getScreenHeight()-PANEL_HEIGHT+Math.floor(GUI.getScreenHeight()/24));
        starRating = new StarRating(x, y);
        starRating.updateRating(rating);
    }
    
    public void setupHUDPanel (String header, String content)
    {
        hudDisplay = new LibraryHUD("left");
        hudDisplay.setBounds(0, 0, 500, 100);
        hudDisplay.createCategoryInfo(header, content);
    }
    
    public void setupOverview (String text)
    {
        Dimension descSize = new Dimension();
        descSize.setSize(GUI.getScreenWidth()*0.45, (int)Math.floor(GUI.getScreenHeight()*0.15));
        
        int x = GUI.getScreenWidth()-descSize.width-50;
        int y = (int)(GUI.getScreenHeight()-PANEL_HEIGHT+Math.floor(GUI.getScreenHeight()/36));

                
        overview = new LibraryOverview(x, y, descSize.width, descSize.height);
        overview.setText(text);
    }
}
