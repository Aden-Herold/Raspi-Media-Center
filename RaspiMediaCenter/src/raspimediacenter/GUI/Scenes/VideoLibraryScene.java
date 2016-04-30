package raspimediacenter.GUI.Scenes;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import raspimediacenter.GUI.Components.ItemScrollPanel;
import raspimediacenter.GUI.Components.VideoComponents.InformationPanelGraphics;
import raspimediacenter.GUI.Components.VideoComponents.VideoInformationPanel;
import raspimediacenter.GUI.Components.VideoComponents.VideoPreviewGraphics;
import raspimediacenter.GUI.SceneManager;

public class VideoLibraryScene extends Scene {

    protected BufferedImage currentFanart;
    protected final double LIST_LENGTH = 0.8; 
    
    private static int PREVIEW_IMG_WIDTH;
    private static int PREVIEW_IMG_HEIGHT;
    
    //SCENE COMPONENTS
    protected InformationPanelGraphics infoPanelGraphics;
    protected VideoPreviewGraphics previewGraphics;
    protected VideoInformationPanel infoPanel;
    
    public VideoLibraryScene () 
    {
        super();
        
        //Add Background to Frame
        bgCanvas.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        SceneManager.getContentPane().add(bgCanvas, 0, 0);

        setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        setOpaque(false);
        SceneManager.getContentPane().add(this, 2, 0);
    }
    
    //STATIC GETTER AND SETTERS
    public static int getPreviewImageWidth()
    {
        return PREVIEW_IMG_WIDTH;
    }
    
    public static int getPreviewImageHeight()
    {
        return PREVIEW_IMG_HEIGHT;
    }
    
    public static void setPreviewImageWidth (int width)
    {
        PREVIEW_IMG_WIDTH = width;
    }
    
    public static void setPreviewImageHeight (int height)
    {
        PREVIEW_IMG_HEIGHT = height;
    }
    
    //UPDATE METHODS
    public void updateBackground ()
    {
        bgCanvas.setBackgroundImage(0);
    }
    
    public void updateBackground (int linkNum)
    {
        bgCanvas.setBackgroundImage(linkNum);
    }
    
    public void updatePoster (int linkNum)
    {
        previewGraphics.updatePoster(linkNum);
        repaint();
    }
    
    public void updateInformation (int linkNum)
    {
        infoPanel.updateStarRating(linkNum);
        infoPanel.updateInformation(linkNum);
    }
    
    public void updateOverview (int linkNum)
    {
        infoPanel.updateOverview(linkNum);
    }

    //CREATE METHDOS
    public void createListDisplay(ArrayList<JButton> linkList)
    {
        Dimension listSize = new Dimension();
        listSize.setSize(SceneManager.getScreenWidth()*0.25, SceneManager.getScreenHeight()*LIST_LENGTH);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setOpaque(false);
        panel.setFocusable(false);

        for (JButton link : linkList)
        {
            panel.add(link);
        }
        
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flowPanel.setOpaque(false);
        flowPanel.setFocusable(false);
        flowPanel.add(panel);
        
        ItemScrollPanel filesList = new ItemScrollPanel(flowPanel);
        filesList.setBounds(SceneManager.getScreenWidth()-listSize.width+22, 0, listSize.width, listSize.height);
        
        SceneManager.getContentPane().add(filesList, 1, 0);
        
        linkList.get(0).requestFocus();
    }
    
    //PAINT COMPONENT
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        infoPanelGraphics.createInformationPanel(paint);
        previewGraphics.displayPoster(paint);
    }

}
