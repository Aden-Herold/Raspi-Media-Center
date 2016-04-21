package raspimediacenter.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import javax.swing.*;

public class MainMenu extends JPanel{

    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = dim.width;
    private final int screenHeight = dim.height;
    
    private float menuTransparency = 0.6f;
    private double menuScreenPosition = 0.7; //Percentage value of the screen from the top
    private final int menuHeight = 120;
    
    public MainMenu () {
        
    }
    
    private void createMenuBar (Graphics2D paint) {
        
        int menuPosY = (int)(screenHeight * menuScreenPosition);
        final Color[] backgroundGradient = {new Color(124,124,124), new Color(0, 0, 0),
                                                };
    
        final float[] gradientFractions = {0.0f, 1f};
        
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, menuPosY),
                                                    new Point2D.Double(0, menuPosY+menuHeight),
                                                    gradientFractions,
                                                    backgroundGradient);
        
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuTransparency));
        paint.fillRect(0, menuPosY, screenWidth, menuHeight);
    }
    
     @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        createMenuBar(paint);
    }
}
