package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Background extends SceneComponent {

    private final String PREF_NAME = "backgroundPrefs";
    private final String backgroundImagePath = "Resources/defaultBackground.jpg";
    private final String userImagesPath = "Resources/UserBackgrounds/";
    private ArrayList<String> imagePaths = null; 

    private BufferedImage defaultBackground;
    private BufferedImage currentBackground;
    private Thread rendererThread;
    
    //IMAGE TRANSITION FADE VARIABLES
    private BufferedImage fadeInImage;
    private BufferedImage fadeOutImage;
    private int userImageCounter = 0;
    private boolean isFading = false;
    private float alpha = 0f;
    private long startTime = -1;
    public final long FADE_TIME = 5000;
    
    public Background ()
    {
        this(true);
    }
    
    public Background (boolean useDefaultBackgrounds)
    {      
        if (useDefaultBackgrounds)
        {
            getDefaultBackgrounds();
            updateFadeImages();
            isFading = true;
            
            rendererThread = new Thread(new renderer());
            rendererThread.start();
        }  
        
        defaultBackground = ImageUtils.getImageFromPath(backgroundImagePath);
    }
    
    public Background (String path)
    {
            getBackgrounds(path);
            updateFadeImages();
            isFading = true;
            
            rendererThread = new Thread(new renderer());
            rendererThread.start();
    }
    
    public void unload()
    {
        fadeInImage = null;
        fadeOutImage = null;
        currentBackground = null;
        isFading = false;
    }
    
    public void setBackgroundImage (BufferedImage img)
    {
        if (img != null)
        {
            currentBackground = img;
        }
        else
        {
            currentBackground = defaultBackground;
        }
    }
    
    @Override
    public void paintSceneComponent(Graphics2D g) {

        if (isFading)
        {
            //Start with default black canvas
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GUI.getScreenWidth(), GUI.getScreenHeight());
            
            g.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
            g.drawImage(fadeOutImage, 0, 0, GUI.getScreenWidth(), GUI.getScreenHeight(), null);
            
            g.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g.drawImage(fadeInImage, 0, 0, GUI.getScreenWidth(), GUI.getScreenHeight(), null);
        }
        else
        {
            g.setComposite(AlphaComposite.SrcOver.derive(1f));
            g.drawImage(currentBackground, 0, 0, GUI.getScreenWidth(), GUI.getScreenHeight(), null);
        }
    }

    private void getDefaultBackgrounds ()
    {
        Preferences prefs = Preferences.userRoot();
        String bgPath = prefs.get(PREF_NAME, userImagesPath);
        getBackgrounds(bgPath);
    }
    
    public void getBackgrounds(String path)
    {
        try 
        {
            imagePaths = ImageUtils.getAllImagesPathsInDir(path, true);
            
            if (imagePaths == null)
            {
                isFading = false;
                currentBackground = defaultBackground;
            }
        }
        catch (IOException ex) 
        {
            ex.getMessage();
        } 
    }
    
    public void updateBackgroundCollection(String img, String dir)
    {
        isFading = false;
        setBackgroundImage(ImageUtils.getImageFromPath(img));
        
        getBackgrounds(dir);
        reset();
        isFading = true;
        rendererThread = new Thread(new renderer());
        rendererThread.start();
    }

    private void reset()
    {
        startTime = -1;
        userImageCounter = 0;
        updateFadeImages();
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
    }
    
    private class renderer implements Runnable
    {
        @Override
        public void run() {
            while (isFading)
            {
                updateFade();
            }
        }
    }

}
