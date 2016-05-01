package raspimediacenter.GUI.Components.MenuComponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import static raspimediacenter.GUI.Scenes.Scene.getMenuColor;

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
        final Color[] backgroundGradient = {getMenuColor().darker(), new Color(0, 0, 0, 0)};
        final float[] gradientFractions = {0.5f, 1f};
        LinearGradientPaint panelGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(400, 0),
                                                    gradientFractions,
                                                    backgroundGradient);
        paint.setPaint(panelGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(Scene.getMenuTransparency()-0.2f)); 
        paint.fillRect(0, 0, 500, 95);
        
        final Color[] separatorGradient = {getMenuColor().darker().darker().darker(), new Color(0, 0, 0, 0)};
        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(400, 0),
                                                    gradientFractions,
                                                    separatorGradient);
        paint.setPaint(separatorGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawLine(0, 95, 500, 95);
        paint.drawLine(0, 94, 500, 94);
    }
    
    private void paintTimePanel (Graphics2D paint)
    {
        final Color[] backgroundGradient = {getMenuColor().darker(), new Color(0, 0, 0, 0)};
        final float[] gradientFractions = {0.5f, 1f};
        LinearGradientPaint panelGrad = new LinearGradientPaint(
                                                    new Point2D.Double(SceneManager.getScreenWidth(), 0),
                                                    new Point2D.Double(SceneManager.getScreenWidth()-400, 0),
                                                    gradientFractions,
                                                    backgroundGradient);
        paint.setPaint(panelGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(Scene.getMenuTransparency()-0.2f)); 
        paint.fillRect(SceneManager.getScreenWidth()-500, 0, 500, 95);
        
        final Color[] separatorGradient = {getMenuColor().darker().darker().darker(), new Color(0, 0, 0, 0)};
        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(SceneManager.getScreenWidth(), 0),
                                                    new Point2D.Double(SceneManager.getScreenWidth()-400, 0),
                                                    gradientFractions,
                                                    separatorGradient);
        paint.setPaint(separatorGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawLine(SceneManager.getScreenWidth(), 95, SceneManager.getScreenWidth()-500, 95);
        paint.drawLine(SceneManager.getScreenWidth(), 94, SceneManager.getScreenWidth()-500, 94);
    }
    
    private void paintMenuBar (Graphics2D paint) {
        
        
        int menuPosY = (int)(SceneManager.getScreenHeight() * MENU_POSITION);
        
        final Color[] backgroundGradient = {new Color(0, 0, 0, 0), getMenuColor().darker(), getMenuColor().darker(), new Color(0, 0, 0, 0)};
        final float[] gradientFractions = {0f, 0.1f, 0.9f, 1f};
        LinearGradientPaint panelGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(SceneManager.getScreenWidth(), 0),
                                                    gradientFractions,
                                                    backgroundGradient);
        paint.setPaint(panelGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(Scene.getMenuTransparency())); 
        paint.fillRect(0, menuPosY, SceneManager.getScreenWidth(), MENU_HEIGHT);
        
        final Color[] separatorGradient = {new Color(0, 0, 0, 0), Scene.getDarkerMenuColor(3), Scene.getDarkerMenuColor(3), new Color(0, 0, 0, 0)};
        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(SceneManager.getScreenWidth(), 0),
                                                    gradientFractions,
                                                    separatorGradient);
        paint.setPaint(separatorGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        //Top Border
        paint.drawLine(0, menuPosY, SceneManager.getScreenWidth(), menuPosY);
        paint.drawLine(0, menuPosY+1, SceneManager.getScreenWidth(), menuPosY+1);
        
        //Bottom Border
        paint.drawLine(0, menuPosY+MENU_HEIGHT, SceneManager.getScreenWidth(), menuPosY+MENU_HEIGHT);
        paint.drawLine(0, menuPosY+MENU_HEIGHT-1, SceneManager.getScreenWidth(), menuPosY+MENU_HEIGHT-1);
        
        //OLD MENU
        /*
        //Create Menu Background
        final Color[] backgroundGradient = {getMenuColor(), new Color(20, 20, 20)};
        final float[] gradientFractions = {0.0f, 1f};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, menuPosY),
                                                    new Point2D.Double(0, menuPosY+MENU_HEIGHT),
                                                    gradientFractions,
                                                    backgroundGradient); 
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getMenuTransparency()));
        paint.fillRect(0, menuPosY, SceneManager.getScreenWidth(), MENU_HEIGHT);
                */
    }
}
