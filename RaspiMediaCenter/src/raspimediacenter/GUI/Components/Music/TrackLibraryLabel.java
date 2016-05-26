package raspimediacenter.GUI.Components.Music;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import raspimediacenter.GUI.Components.MLabel;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.TextUtils;

public class TrackLibraryLabel extends SceneComponent {

    private final int LABEL_WIDTH = (int)Math.floor(GUI.getScreenWidth()/4);
    private final int LABEL_HEIGHT = (int)Math.floor(GUI.getScreenHeight()*0.05);
    private final int PADDING = (int)(Math.floor(LABEL_HEIGHT * 0.2));
    
    private final Rectangle bounds;
    private final MLabel nameLabel;
    private final MLabel durationLabel;
    
    private boolean isVisible = true;
    private boolean isFocused = false;
    
    public TrackLibraryLabel (String name, String durations, int x, int y, boolean isVisible)
    {
        this.isVisible = isVisible;

        bounds = new Rectangle(x, y, LABEL_WIDTH, LABEL_HEIGHT);

        int durationLabelWidth = LABEL_WIDTH/3;
        
        nameLabel = new MLabel(name, 
                                                    TextUtils.LEFT_ALIGN, 
                                                    bounds.x+PADDING*2, 
                                                    bounds.y+bounds.height-(int)Math.floor(bounds.height/2.5), 
                                                    LABEL_WIDTH-durationLabelWidth, true);
        
        durationLabel = new MLabel(durations, 
                                                    TextUtils.LEFT_ALIGN, 
                                                    bounds.x+nameLabel.getWidth()+PADDING, 
                                                    bounds.y+bounds.height-(int)Math.floor(bounds.height/2.5), 
                                                    durationLabelWidth, false);
    }
   
    // GETTERS
    public Rectangle getBounds()
    {
        return bounds;
    }
    
    public int getY()
    {
        return bounds.y;
    }
    
    public int getX()
    {
        return bounds.x;
    }
    
    public int getWidth()
    {
        return bounds.width;
    }
    
    public int getHeight()
    {
        return bounds.height;
    }

    public boolean getFocused()
    {
        return isFocused;
    }
   
    public boolean getVisible()
    {
        return isVisible;
    }
    
    // SETTERS
    public void setPosition (int x, int y)
    {
        bounds.x = x;
        bounds.y = y;
    }

    public void setVisible (boolean isVisible)
    {
        this.isVisible = isVisible;
    }
    
    public void setFocused (boolean isFocused)
    {
        this.isFocused = isFocused;
    }
    
    //FUNCTIONS
    public void updateX (int x)
    {
        nameLabel.updateX(x);
        durationLabel.updateX(x);
        bounds.x = bounds.x + x;
    }
    
    public void updateY (int y)
    {
        nameLabel.updateY(y);
        durationLabel.updateY(y);
        bounds.y = bounds.y + y;
    }
    
    //DRAW FUNCTIONS
    @Override
    public void paintSceneComponent(Graphics2D g2d) 
    {
         if (isVisible)
        {
            final float[] gradientFractions = {0f, 0.99f};
            Color menuColor = SceneManager.getMenuColor();

            if (!isFocused)
            {
                final Color[] backgroundGradient = {ColorUtils.darken(menuColor, 1), new Color(0, 0, 0, 0)};
                LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                            new Point2D.Double(bounds.x, bounds.y),
                                                            new Point2D.Double(bounds.x+bounds.width, bounds.y+bounds.height),
                                                            gradientFractions,
                                                            backgroundGradient);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, SceneManager.getMenuTransparency())); 
                g2d.setPaint(menuGrad);
                g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
            else 
            {
                final Color[] backgroundGradient = {ColorUtils.brighten(menuColor, 1), new Color(0, 0, 0, 0)};
                LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                            new Point2D.Double(bounds.x, bounds.y),
                                                            new Point2D.Double(bounds.x+bounds.width, bounds.y+bounds.height),
                                                            gradientFractions,
                                                            backgroundGradient);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, SceneManager.getMenuTransparency()));
                g2d.setPaint(menuGrad);
                g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));


            nameLabel.paintSceneComponent(g2d);
            durationLabel.paintSceneComponent(g2d);
        }
    }
}