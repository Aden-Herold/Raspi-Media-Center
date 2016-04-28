package raspimediacenter.GUI.Components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import raspimediacenter.Logic.Utilities.ImageUtilities;

public class BackgroundCanvas extends JPanel {

    //SCREEN DIMENSIONS
    private final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = dim.width;
    private final int screenHeight = dim.height;
    
    //BACKGROUND GRADIENT - LOWEST PRIORITY
    private final Color[] backgroundGradient = {new Color(143,153,247),
                                                new Color(103, 119, 224),
                                                new Color(80, 45, 183),
                                                new Color(35, 0, 79)};
    
    private final float[] gradientFractions = {0.0f, 0.25f, 0.5f, 0.9f};

    //BACKGROUND VARIABLES
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ArrayList<BufferedImage> backgroundImages = new ArrayList<>();
    private ArrayList<BufferedImage> fanartImages;
    
    //BACKGROUND TRANSITION SPEED VARIABLES
    private Timer timer;
    private static final int TRANSITION_SPEED = 4000;
    private int userImageCounter = 0;
    
    //IMAGE TRANSITION FADE VARIABLES
    private BufferedImage fadeInImage;
    private BufferedImage fadeOutImage;
    private float alpha = 0f;
    private long startTime = -1;
    public static final long FADE_TIME = 2000;
    
    //BASE CONSTUCTOR
    public BackgroundCanvas() {}
    
    //FUNCTIONS
    public boolean loadImagesFromDir(String path)
    {
        try 
        {
            File directory = new File(path);
            imagePaths = ImageUtilities.getAllImagesPathsInDir(directory, true); 
            loadImagesFromPaths(imagePaths);
            
            return true;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public void setBackgroundImage (int imageNum)
    {
        backgroundImages.clear();
        backgroundImages.add(fanartImages.get(imageNum));
        repaint();
    }
    
    public void loadFanartImagesIntoMemory (ArrayList<String> imagePaths)
    {
        fanartImages = new ArrayList<>();
        fanartImages = ImageUtilities.getImagesFromPaths(imagePaths);
    }
    
    public void loadImageFromPath(String path)
    {
        ArrayList<String> tmpList = new ArrayList<>();
        tmpList.add(path);
        loadImagesFromPaths(tmpList);
    }
    
    private void loadImagesFromPaths (ArrayList<String> imagePaths)
    {
        backgroundImages = ImageUtilities.getImagesFromPaths(imagePaths);
        
        if (backgroundImages.size() > 1)
        {
            fadeInImage = backgroundImages.get(0);
            fadeOutImage = backgroundImages.get(0);
            timer = new Timer(TRANSITION_SPEED, new TimerListener());
            timer.start();
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        Graphics2D paint = (Graphics2D) g;
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);

        //Start with default black canvas
        paint.setColor(Color.BLACK);
        paint.fillRect(0, 0, screenWidth, screenHeight);
        
        //If only single image in array list
        if (backgroundImages.size() > 0 && backgroundImages.size() < 2)
        {
            System.out.println(backgroundImages.get(0));
            paint.drawImage(backgroundImages.get(0), 0, 0, screenWidth, screenHeight, this);
        }
        //If multiple images in array list and user or default images were found
        else if (imagePaths != null)
        {
            paint.setComposite(AlphaComposite.SrcOver.derive(alpha));
            paint.drawImage(fadeInImage, 0, 0, screenWidth, screenHeight, this);
            
            paint.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
            paint.drawImage(fadeOutImage, 0, 0, screenWidth, screenHeight, this); 
        }
        //Else - default to gradient
        else
        {
            LinearGradientPaint backgroundGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(0, screenHeight),
                                                    gradientFractions,
                                                    backgroundGradient);

            paint.setPaint(backgroundGrad);
            paint.fillRect(0, 0, screenWidth, screenHeight);
        }
    }
    
    //FADE TIMER - OVERWRITE DEFAULT TIMER BEHAVIOUR TO CHANGE OPACITY OVER TIME
    final Timer fadeTimer = new Timer(40, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
            } 
            else {
                long time = System.currentTimeMillis();
                long duration = time - startTime;
                      
                if (duration >= FADE_TIME) {
                    startTime = -1;
                    ((Timer) e.getSource()).stop();
                } 
                else {
                    alpha = 0f + (((float)duration) / ((float)FADE_TIME));
                }
                repaint();
            }
        }
    });
    
    //CHANGE BACKGROUND IMAGE AFTER EVERY FULL TIMER INTERVAL
    private class TimerListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent event) {

            //Increase image counter
            userImageCounter++;
            //Reset alpha opacity
            alpha = 0f;
            
            //Ensure fadeOutImage position doesn't go below 0
            if (userImageCounter == 0) {
                fadeOutImage = backgroundImages.get(userImageCounter);
            } else {
                fadeOutImage = backgroundImages.get(userImageCounter-1);
            }

            //Loop image counter when it reaches the end of the arraylist
            if (userImageCounter > backgroundImages.size()-1) {
                userImageCounter = 0;
            }

            //Set fadeInImage and start fadeTimer
            fadeInImage = backgroundImages.get(userImageCounter);
            fadeTimer.start();
            repaint();
        }
    }
}
