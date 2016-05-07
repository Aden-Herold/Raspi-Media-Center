package raspimediacenter.GUI.Components.Video;

import raspimediacenter.GUI.GUI;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;

public class InformationPanelGraphics {
    
    private static double PANEL_SCEEN_PERCENT; 
    private static int PANEL_HEIGHT;
    private final Color menuColor;
    
    public InformationPanelGraphics (double panelScreenPercent, int panelHeight) 
    {
        PANEL_SCEEN_PERCENT = panelScreenPercent;
        PANEL_HEIGHT = panelHeight;
        menuColor = SceneManager.getMenuColor();
    }
    
    public static double getPanelScreenPercent ()
    {
        return PANEL_SCEEN_PERCENT;
    }
    
    public static int getPanelHeight()
    {
        return PANEL_HEIGHT;
    }
    
    public void createInformationPanel (Graphics2D paint)
    {
        paintListBaseCover(paint);
        paintBackPanel(paint);
        //paintInformationArea(paint);
        paintGradientShadow(paint);
        paintSeparator(paint);
        paintTopPanel(paint);
    }
    
    private void paintBackPanel(Graphics2D paint)
    {
        //Paint full back panel
        paint.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()-0.1f));
        paint.setPaint(ColorUtils.darken(menuColor, 1));
        paint.fillRect(0, GUI.getScreenHeight()-PANEL_HEIGHT,
                       GUI.getScreenWidth(), PANEL_HEIGHT);
        
    }
    
    private void paintInformationArea (Graphics2D paint)
    {
        //Paint information background area
        final Color[] informationPanelGradient = {ColorUtils.darken(menuColor, 1), new Color(0, 0, 0, 0)};
        final float[] infoGradFractions = {0.95f, 1f};
        LinearGradientPaint infoGrad = new LinearGradientPaint(
                                        new Point2D.Double(0, GUI.getScreenHeight()-PANEL_HEIGHT),
                                        new Point2D.Double(GUI.getScreenWidth()/2, 
                                                          GUI.getScreenHeight()-PANEL_HEIGHT),
                                        infoGradFractions,
                                        informationPanelGradient);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()-0.3f));
        paint.setPaint(infoGrad);
        paint.fillRect(0, GUI.getScreenHeight()-PANEL_HEIGHT,
                       GUI.getScreenWidth()/2, PANEL_HEIGHT);
    }
    
    private void paintGradientShadow (Graphics2D paint)
    {
        //Paint gradient sheen over back panel
        final Color[] sheenGradient = {new Color(255, 255, 255, 128), new Color(0,0,0,0), new Color(0,0,0,0), new Color(0, 0, 0)};
        final float[] sheenGradFractions = {0f, 0.1f, 0.9f, 1f};
        LinearGradientPaint sheenGrad = new LinearGradientPaint(
                                        new Point2D.Double(0, GUI.getScreenHeight()-PANEL_HEIGHT),
                                        new Point2D.Double(0, GUI.getScreenHeight()),
                                        sheenGradFractions,
                                        sheenGradient);
            
        paint.setComposite(AlphaComposite.SrcOver.derive(0.2f)); 
        paint.setPaint(sheenGrad);
        paint.fillRect(0, GUI.getScreenHeight()-PANEL_HEIGHT,
                       GUI.getScreenWidth(), PANEL_HEIGHT);
    }
    
    private void paintSeparator (Graphics2D paint)
    {
        //Paint separator line 
        paint.setComposite(AlphaComposite.SrcOver.derive(1f)); 
        paint.setColor(ColorUtils.darken(menuColor, 3));
        paint.drawLine(0, GUI.getScreenHeight()-PANEL_HEIGHT,
                       GUI.getScreenWidth(), GUI.getScreenHeight()-PANEL_HEIGHT);
        paint.drawLine(0, GUI.getScreenHeight()-PANEL_HEIGHT+1,
                       GUI.getScreenWidth(), GUI.getScreenHeight()-PANEL_HEIGHT+1);
    }
    
    private void paintTopPanel (Graphics2D paint)
    {
        int height = (int)Math.floor(GUI.getScreenHeight()/15.1);
        
        final Color[] backgroundGradient = {ColorUtils.darken(menuColor, 1), new Color(0, 0, 0, 0)};
        final float[] gradientFractions = {0.5f, 1f};
        LinearGradientPaint panelGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(400, 0),
                                                    gradientFractions,
                                                    backgroundGradient);
        paint.setPaint(panelGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()-0.2f)); 
        paint.fillRect(0, 0, 500, height);
        
        final Color[] separatorGradient = {ColorUtils.darken(menuColor, 3), new Color(0, 0, 0, 0)};
        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, 0),
                                                    new Point2D.Double(400, 0),
                                                    gradientFractions,
                                                    separatorGradient);
        paint.setPaint(separatorGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawLine(0, height, 500, height);
        paint.drawLine(0, height-1, 500, height-1);
    }
    
    private void paintListBaseCover (Graphics2D paint)
    {
        final float[] gradientFractions = {0f, 0.3f};
        final Color[] backgroundGradient = {new Color(0, 0, 0, 0), ColorUtils.darken(menuColor, 1)};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(GUI.getScreenWidth()-GUI.getScreenWidth()/4, GUI.getScreenHeight()-PANEL_HEIGHT),
                                                    new Point2D.Double(GUI.getScreenWidth(), GUI.getScreenHeight()-PANEL_HEIGHT),
                                                    gradientFractions,
                                                    backgroundGradient);

        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); 
        paint.setPaint(menuGrad);
        paint.fillRect(GUI.getScreenWidth()-GUI.getScreenWidth()/4, GUI.getScreenHeight()-PANEL_HEIGHT, GUI.getScreenWidth()/4, PANEL_HEIGHT);
    }
}
