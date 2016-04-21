package raspimediacenter.GUI.Components;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Canvas extends JPanel {

    private File backgroundFile = null;
    private Image backgroundImage = null;
    private Color[] backgroundGradient;
    private float[] gradientFractions;
    
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    final int screenWidth = dim.width;
    final int screenHeight = dim.height;
    
    public Canvas() {
        
    }
    
    public Canvas (File backgroundFile) {
        this.backgroundFile = backgroundFile;
        
        try {
            backgroundImage = ImageIO.read(backgroundFile);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    //Gradient Overload
    public Canvas (Color[] backgroundGradient, float[] gradientFractions) {
        this.backgroundGradient = backgroundGradient;
        this.gradientFractions = gradientFractions;
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);

        if (backgroundFile != null)
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
