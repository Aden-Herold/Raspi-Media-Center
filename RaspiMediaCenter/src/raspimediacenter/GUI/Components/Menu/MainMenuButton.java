package raspimediacenter.GUI.Components.Menu;

import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MainMenuButton extends SceneComponent {

    private Font stringFont = TextUtils.STANDARD_FONT;
    private final Rectangle button;
    private final String text;
    private boolean isFocused = false;
    private boolean isVisible = true;
    
    public MainMenuButton (String text, int x, int y, int width, int height)
    {
        this.text = text;        
        button = new Rectangle(x, y, width, height);
        this.isVisible = isVisible;
        
        stringFont = stringFont.deriveFont(Font.BOLD, TextUtils.MENU_FONT_SIZE);
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
        button.x = button.x + x;
    }
    
    public void updateY (int y)
    {
        button.y = button.y + y;
    }
    
    
    
    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        if (isVisible)
        {
            if (isFocused)
            {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            else
            {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            

            //Paint text
            g2d.setColor(Color.white);
            int stringWidth = g2d.getFontMetrics(stringFont).stringWidth(text);
            int stringHeight = g2d.getFontMetrics(stringFont).getHeight();
            g2d.setFont(stringFont);
            g2d.drawString(text, button.x+(button.width/2)-stringWidth/2, (button.y+button.height/2)+stringHeight/4);
        }
    }
}
