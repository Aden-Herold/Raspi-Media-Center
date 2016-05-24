package raspimediacenter.GUI.Components.Images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.Logic.Utilities.ImageUtils;

public class ImageCollectionBackground extends SceneComponent {

    private final int IMG_WIDTH = GUI.getScreenWidth(); 
    private final int IMG_HEIGHT = GUI.getScreenHeight();
    
    private ArrayList<String> imageCollections;
    private BufferedImage backgroundImage;
    
    public ImageCollectionBackground(ArrayList<String> list) 
    {
        imageCollections = list;
    }
    
    public void setupBackgrounds (int linkNum)
    {
        backgroundImage = ImageUtils.getImageFromPath(imageCollections.get(linkNum));
    }
    
    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, GUI.getScreenWidth(), GUI.getScreenHeight());
        
        g2d.drawImage(backgroundImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
    }
}
