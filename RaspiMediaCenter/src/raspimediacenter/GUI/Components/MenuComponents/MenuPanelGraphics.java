package raspimediacenter.GUI.Components.MenuComponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import raspimediacenter.GUI.SceneManager;
import static raspimediacenter.GUI.Scenes.Scene.getMenuColor;
import static raspimediacenter.GUI.Scenes.Scene.getMenuTransparency;

public class MenuPanelGraphics {
    
    private final double MENU_POSITION = 0.7; //Percentage value of the screen from the top
    private final int MENU_HEIGHT = (int)Math.floor(SceneManager.getScreenHeight()*0.084); //Height of the menu bar
    
    public MenuPanelGraphics () {}
    
    public double getMenuPosition ()
    {
        return MENU_POSITION;
    }
    
    public int getMenuHeight ()
    {
        return MENU_HEIGHT;
    }
    
    public void paintMenuGraphics (Graphics2D paint)
    {
        paintInfoPanel(paint);
        paintTimePanel(paint);
        paintMenuBar(paint);
    }
    
    private void paintInfoPanel (Graphics2D paint)
    {
        final Color[] backgroundGradient = {new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue()),
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 90), 
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 0)};
        final float[] gradientFractions = {0.0f, 0.7f, 1f};
        Rectangle2D rect = new Rectangle2D.Double(-450, -120, 900, 240);
        RadialGradientPaint menuGrad = new RadialGradientPaint(
                                                    rect,
                                                    gradientFractions,
                                                    backgroundGradient,
                                                    MultipleGradientPaint.CycleMethod.NO_CYCLE);
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        paint.fillOval(-450, -120, 900, 240);
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void paintTimePanel (Graphics2D paint)
    {
        final Color[] backgroundGradient = {new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue()),
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 90), 
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 0)};
        final float[] gradientFractions = {0.0f, 0.7f, 1f};
        Rectangle2D rect = new Rectangle2D.Double(SceneManager.getScreenWidth()-450, -120, 900, 240);
        RadialGradientPaint menuGrad = new RadialGradientPaint(
                                                    rect,
                                                    gradientFractions,
                                                    backgroundGradient,
                                                    MultipleGradientPaint.CycleMethod.NO_CYCLE);
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        paint.fillOval(SceneManager.getScreenWidth()-450, -120, 900, 240);
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void paintMenuBar (Graphics2D paint) {
        
        int menuPosY = (int)(SceneManager.getScreenHeight() * MENU_POSITION);
        
        //Create Menu Background
        final Color[] backgroundGradient = {new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 150), 
                                            new Color(20, 20, 20, 225)};
        final float[] gradientFractions = {0.0f, 1f};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, menuPosY),
                                                    new Point2D.Double(0, menuPosY+MENU_HEIGHT),
                                                    gradientFractions,
                                                    backgroundGradient); 
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getMenuTransparency()));
        paint.fillRect(0, menuPosY, SceneManager.getScreenWidth(), MENU_HEIGHT);
    }
}
