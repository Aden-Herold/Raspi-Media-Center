package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class LibraryButton extends SceneComponent {

    private final Font stringFont = TextUtils.STANDARD_FONT;
    private final Rectangle button;
    private final String text;
    private boolean isFocused = false;
    private boolean isVisible = true;
    
    public LibraryButton (String text, int x, int y, int width, int height, boolean isVisible)
    {
        this.text = text;        
        button = new Rectangle(x, y, width, height);
        this.isVisible = isVisible;
    }

    //GETTERS
    public String getText()
    {
        return text;
    }
    
    public Rectangle getRect ()
    {
        return button;
    }
    
    public int getX ()
    {
        return button.x;
    }
    
    public int getY()
    {
        return button.y;
    }
    
    public int getWidth()
    {
        return button.width;
    }
    
    public int getHeight()
    {
        return button.height;
    }
    
    public boolean getVisible ()
    {
        return isVisible;
    }
    
    //SETTERS
    public void setFocused (boolean isFocused)
    {
        this.isFocused = isFocused;
    }
    
    public void setPosition (int x, int y)
    {
        button.x = x;
        button.y = y;
    }
    
    public void setVisible (boolean isVisible)
    {
        this.isVisible = isVisible;
    }
    
    //FUNCTIONS
    public void updateX (int x)
    {
        button.x += x;
    }
    
    public void updateY (int y)
    {
        button.y += y;
    }
    
    
    
    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        if (isVisible)
        {
            final float[] gradientFractions = {0f, 0.7f};
            Color menuColor = SceneManager.getMenuColor();

            if (!isFocused)
            {
                final Color[] backgroundGradient = {new Color(0, 0, 0, 0), ColorUtils.darken(menuColor, 1)};
                LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                            new Point2D.Double(button.x, button.y),
                                                            new Point2D.Double(button.x+button.width, button.y+button.height),
                                                            gradientFractions,
                                                            backgroundGradient);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, SceneManager.getMenuTransparency())); 
                g2d.setPaint(menuGrad);
                g2d.fill(button);
            }
            else 
            {
                final Color[] backgroundGradient = {new Color(0, 0, 0, 0), ColorUtils.brighten(menuColor, 1)};
                LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                            new Point2D.Double(button.x, button.y),
                                                            new Point2D.Double(button.x+button.width, button.y+button.height),
                                                            gradientFractions,
                                                            backgroundGradient);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, SceneManager.getMenuTransparency()));
                g2d.setPaint(menuGrad);
                g2d.fill(button);
            }

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            //Paint text
            g2d.setColor(Color.white);
            int stringWidth = g2d.getFontMetrics(stringFont).stringWidth(text);
            int stringHeight = g2d.getFontMetrics(stringFont).getHeight();
            g2d.setFont(stringFont);
            g2d.drawString(text, button.x+button.width-stringWidth-40, (button.y+button.height/2)+stringHeight/4);


            //Paint separator line 
            final Color[] backgroundGradient = {new Color(0, 0, 0, 0), ColorUtils.darken(menuColor, 3)};
            LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                        new Point2D.Double(button.x, button.y+button.height),
                                                        new Point2D.Double(button.x+button.width, button.y+button.height),
                                                        gradientFractions,
                                                        backgroundGradient);
            g2d.setPaint(separatorGrad);
            g2d.drawLine(button.x, button.y+button.height, button.x+button.width, button.y+button.height);
        }
    }
}
