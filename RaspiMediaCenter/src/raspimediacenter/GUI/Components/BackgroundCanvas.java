package raspimediacenter.GUI.Components;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BackgroundCanvas extends JPanel {

    private final String backgroundImagePath = "src/raspimediacenter/GUI/Resources/background1.jpg";
    private Image backgroundImage;
    private final Color[] backgroundGradient = {new Color(143,153,247),
                                                new Color(103, 119, 224),
                                                new Color(80, 45, 183),
                                                new Color(35, 0, 79)};
    
    private final float[] gradientFractions = {0.0f, 0.25f, 0.5f, 0.9f};
    
    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = dim.width;
    private final int screenHeight = dim.height;
    
    private boolean useBackgroundImage = false;
    
    //Base Constructor
    public BackgroundCanvas() {
        
    }

    public BackgroundCanvas(boolean useBackgroundImage){
        this.useBackgroundImage = useBackgroundImage;
        
        if (useBackgroundImage)
        {
            try {
                backgroundImage = ImageIO.read(new File(backgroundImagePath));
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);

        if (useBackgroundImage)
        {
            paint.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        }
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
}
