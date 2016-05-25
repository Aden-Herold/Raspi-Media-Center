package raspimediacenter.GUI.Components.Images;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.Data.Models.Images.ImageCollectionsContainer;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtils;

public class ImageCollectionBackground extends SceneComponent {

    private final int IMG_WIDTH = GUI.getScreenWidth(); 
    private final int IMG_HEIGHT = GUI.getScreenHeight();
    
    private BufferedImage fadeInImage;
    private BufferedImage fadeOutImage;
    
    private Thread imageLoadThread;
    private Thread rendererThread;
    private int userImageCounter = 0;
    private boolean isFading = false;
    private boolean isLoading = false;
    private float alpha = 0f;
    private long startTime = -1;
    public final long FADE_TIME = 5000;
    
    private ArrayList<String> imagePaths;
    private ArrayList<ImageCollectionsContainer> collectionsContainer;
    private BufferedImage backgroundImage;
    private ArrayList<BufferedImage> starterImages;
    
    public ImageCollectionBackground(ArrayList<BufferedImage> starterImages, ArrayList<ImageCollectionsContainer> collections) 
    {
        this.starterImages = starterImages;
        collectionsContainer = collections;
    }
    
    public void changeBackground (int linkNum)
    {
        isLoading = true;
        isFading = false;
        alpha = 0f;
        startTime = -1;
        userImageCounter = 0;
        backgroundImage = starterImages.get(linkNum);
        imagePaths = collectionsContainer.get(linkNum).imagePaths;
        
        if (imageLoadThread == null || !imageLoadThread.isAlive())
        {
            imageLoadThread= new Thread(){
                @Override
                public synchronized void run()
                {
                    while (isLoading)
                    {
                        loadFadeImages();
                    }
                }
            };
        
            imageLoadThread.start();
        }
    }

    private void loadFadeImages()
    {
        if (imagePaths != null)
        {
            updateFadeImages();
        }

        if (!isLoading)
        {
            if (rendererThread == null || !rendererThread.isAlive())
            {
                rendererThread = new Thread(new renderer());
                isFading = true;
                rendererThread.start();
            }
            else
            {
                isFading = true;
            }
        }
    }
    
    private void updateFade()
    {
        if (startTime < 0) {
            startTime = System.currentTimeMillis();
        }
        else {
            long time = System.currentTimeMillis(); 
            long duration = time - startTime;
            
            if (duration >= FADE_TIME) {
                if (duration >= FADE_TIME*2)
                { 
                    updateFadeImages();
                    startTime = -1;  
                }
            }
            else {
                alpha = 0f + (((float)duration) / ((float)FADE_TIME));
            }
        }
        
        SceneManager.getCurrentScene().paintScene();
        
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(Background.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateFadeImages ()
    {
        //Increase image counter
        userImageCounter++;

        //Ensure fadeOutImage position doesn't go below 0
        if (userImageCounter == 0) {
            fadeOutImage = ImageUtils.getImageFromPath(imagePaths.get(userImageCounter));
        } else {
            fadeOutImage = ImageUtils.getImageFromPath(imagePaths.get(userImageCounter-1));
        }

        //Loop image counter when it reaches the end of the arraylist
        if (userImageCounter > imagePaths.size()-1) {
            userImageCounter = 0;
        }

        //Set fadeInImage and start fadeTimer
        fadeInImage = ImageUtils.getImageFromPath(imagePaths.get(userImageCounter));  
        //Reset alpha opacity
        alpha = 0f;
        
        isLoading = false;
    }
    
    private class renderer implements Runnable
    {
        @Override
        public synchronized void run() {
            while (isFading)
            {
                updateFade();
            }
        }
    }
    
    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        g2d.setPaint(Color.BLACK);
        g2d.fillRect(0, 0, GUI.getScreenWidth(), GUI.getScreenHeight());
        
        if (isFading && !isLoading)
        {
            g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
            g2d.drawImage(fadeOutImage, 0, 0, GUI.getScreenWidth(), GUI.getScreenHeight(), null);
            
            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2d.drawImage(fadeInImage, 0, 0, GUI.getScreenWidth(), GUI.getScreenHeight(), null);
        }
        else
        {
            g2d.drawImage(backgroundImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        }
    }
}
