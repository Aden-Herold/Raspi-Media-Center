package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MLabel extends SceneComponent {

    private Font font = TextUtils.STANDARD_FONT;
    private String text;
    private int alignment = TextUtils.LEFT_ALIGN;
    
    private final Rectangle label;
    private boolean specialLabel = false;
    private float opacity = 1f;
    
    public MLabel (String text, int alignment, int x, int y, int width, boolean specialLabel)
    {
        this.text = text;
        this.alignment = alignment;
        this.specialLabel = specialLabel;
        
        Graphics2D textGraphics = (Graphics2D)GUI.getScreen().getGraphics();
        int stringHeight = textGraphics.getFontMetrics(font).getHeight();
        
        label = new Rectangle(x, y, width, stringHeight+10);
    }
    
    //GETTERS
    public Rectangle getBounds()
    {
        return label;
    }
    
    public int getX()
    {
        return label.x;
    }
    
    public int getY()
    {
        return label.y;
    }
    
    public int getWidth()
    {
        return label.width;
    }
    
    public int getHeight()
    {
        return label.height;
    }
    
    public String getText()
    {
        return text;
    }
    
    //SETTERS
    public void setX(int x)
    {
        label.x = x;
    }
    
    public void setY (int y)
    {
        label.y = y;
    }
    
    public void setText (String text)
    {
        this.text = text;
    }
    
    public void setOpacity (float opacity)
    {
        this.opacity = opacity;
    }
    
    public void setFont (Font font)
    {
        this.font = font;
    }
    
    public void setSpecial (boolean isSpecial)
    {
        specialLabel = isSpecial;
    }
    
    //FUNCTIONS
    public void updateX (int x)
    {
        label.x += x;
    }
    
    public void updateY (int y)
    {
        label.y += y;
    }
            
    @Override
    public void paintSceneComponent(Graphics2D g2d) 
    {
        if (text != null)
        {
            g2d.setFont(font);

            FontMetrics metrics = g2d.getFontMetrics(font);

            if (specialLabel)
            {
                g2d.setColor(ColorUtils.brighten(SceneManager.getMenuColor(), 3));
            }
            else
            {
                g2d.setColor(Color.white);
            }

            g2d.setComposite(AlphaComposite.SrcOver.derive(opacity));

            if (alignment == TextUtils.LEFT_ALIGN)
            {
                g2d.drawString(text, label.x, label.y);
            }
            else if (alignment == TextUtils.RIGHT_ALIGN)
            {
                g2d.drawString(text, (label.x+label.width)-metrics.stringWidth(text), label.y);
            }
        }
    }
}
