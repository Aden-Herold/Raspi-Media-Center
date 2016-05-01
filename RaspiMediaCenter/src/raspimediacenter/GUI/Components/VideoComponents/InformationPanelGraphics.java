package raspimediacenter.GUI.Components.VideoComponents;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import static raspimediacenter.GUI.Scenes.Scene.getMenuColor;
import static raspimediacenter.GUI.Scenes.Scene.getMenuTransparency;

public class InformationPanelGraphics {
    
    private final static double PANEL_SCEEN_PERCENT = 0.2; 
    private final int PANEL_HEIGHT;
    
    public InformationPanelGraphics () 
    {
        PANEL_HEIGHT = (int)(Math.floor(SceneManager.getScreenHeight()*PANEL_SCEEN_PERCENT));
    }
    
    public static double getPanelScreenPercent ()
    {
        return PANEL_SCEEN_PERCENT;
    }
    
    public int getPanelHeight()
    {
        return PANEL_HEIGHT;
    }
    
    public void createInformationPanel (Graphics2D paint)
    {
        paintBackPanel(paint);
        paintInformationArea(paint);
        paintGradientSheen(paint);
        paintSeparator(paint);
        paintTopPanel(paint);
    }
    
    private void paintBackPanel(Graphics2D paint)
    {
        //Paint full back panel
        paint.setComposite(AlphaComposite.SrcOver.derive(Scene.getMenuTransparency()));
        paint.setPaint(Scene.getMenuColor().darker());
        paint.fillRect(0, SceneManager.getScreenHeight()-PANEL_HEIGHT,
                       SceneManager.getScreenWidth(), PANEL_HEIGHT);
        
    }
    
    private void paintInformationArea (Graphics2D paint)
    {
        //Paint information background area
        final Color[] informationPanelGradient = {Scene.getMenuColor().darker(), new Color(0, 0, 0, 0)};
        final float[] infoGradFractions = {0.95f, 1f};
        LinearGradientPaint infoGrad = new LinearGradientPaint(
                                                new Point2D.Double(0, 
                                                        SceneManager.getScreenHeight()-PANEL_HEIGHT),
                                                new Point2D.Double(SceneManager.getScreenWidth()/2, 
                                                          SceneManager.getScreenHeight()-PANEL_HEIGHT),
                                                          infoGradFractions,
                                                          informationPanelGradient);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(Scene.getMenuTransparency()+0.1f));
        paint.setPaint(infoGrad);
        paint.fillRect(0, SceneManager.getScreenHeight()-PANEL_HEIGHT,
                       SceneManager.getScreenWidth()/2, PANEL_HEIGHT);
    }
    
    private void paintGradientSheen (Graphics2D paint)
    {
        //Paint gradient sheen over back panel
        final Color[] sheenGradient = {new Color(0, 0, 0, 0), Scene.getMenuColor().darker().darker().darker()};
        final float[] sheenGradFractions = {0.0f, 1f};
        LinearGradientPaint sheenGrad = new LinearGradientPaint(
                                       new Point2D.Double(0, SceneManager.getScreenHeight()-PANEL_HEIGHT),
                                       new Point2D.Double(0, PANEL_HEIGHT),
                                                          sheenGradFractions,
                                                          sheenGradient);
            
        paint.setComposite(AlphaComposite.SrcOver.derive(1f)); 
        paint.setPaint(sheenGrad);
        paint.fillRect(0, SceneManager.getScreenHeight()-PANEL_HEIGHT,
                       SceneManager.getScreenWidth(), PANEL_HEIGHT);
    }
    
    private void paintSeparator (Graphics2D paint)
    {
        //Paint separator line 
        paint.setColor(Scene.getMenuColor().darker().darker().darker());
        paint.drawLine(0, SceneManager.getScreenHeight()-PANEL_HEIGHT,
                       SceneManager.getScreenWidth(), SceneManager.getScreenHeight()-PANEL_HEIGHT);
    }
    
    private void paintTopPanel (Graphics2D paint)
    {
        final Color[] backgroundGradient = {getMenuColor(), new Color(0, 0, 0, 0)};
        final float[] gradientFractions = {0f, 1f};
        Rectangle2D rect = new Rectangle2D.Double(-450, -120, 900, 240);
        RadialGradientPaint menuGrad = new RadialGradientPaint(
                                                    rect,
                                                    gradientFractions,
                                                    backgroundGradient,
                                                    MultipleGradientPaint.CycleMethod.NO_CYCLE);
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(Scene.getMenuTransparency()));
        paint.fillOval(-450, -120, 900, 240);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
    }
}
