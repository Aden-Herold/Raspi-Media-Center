package raspimediacenter.GUI.Components.Music;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import raspimediacenter.GUI.GUI;

public class EqualizerColumn {

    private final Rectangle bounds;
    private final Color color;
    
    private final int fillInterval;
    private double fillPercent;
    private int fillWidth = 0;
    private int targetWidth = 0;
    
    public EqualizerColumn (int x, int y, int w, int h, Color color)
    {
        bounds = new Rectangle(x, y, w, h);
        fillInterval = (int)Math.ceil(bounds.width*0.01);
        this.color = color;
        
        randomiseStart();
    }
    
    public void randomiseStart()
    {
        double rand = Math.random();
        int rounded = (int)Math.round(rand*100);
        
        fillWidth = fillInterval * rounded;
    }
    
    public void randomise()
    {
        double rand = Math.random();
        int rounded = (int)Math.round(rand*100);
        
        targetWidth = 0;
        
        targetWidth = fillInterval * rounded;
    }
    
    public void updateColumn ()
    {

        if (fillWidth < targetWidth)
        {
            increaseFill();
        }
        else if (fillWidth > targetWidth)
        {
            decreaseFill();
        }
        else
        {
            randomise();
        }
    }
    
    public void increaseFill()
    {
        fillWidth += fillInterval;
    }
    
    public void decreaseFill()
    {
        fillWidth -= fillInterval;
    }

    
    public void paintColumn(Graphics2D g2d)
    {
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.7f));
        g2d.setPaint(color);
        g2d.fillRect(GUI.getScreenWidth()-fillWidth, bounds.y, fillWidth, bounds.height);

        g2d.setPaint(Color.BLACK);
        g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
        
        //VERTICAL LINES
        int posX = GUI.getScreenWidth();
         int xOffset = (int)Math.ceil(bounds.width*0.02);
        for (int x = 0; x < 50; x++)
        {
            if (posX > GUI.getScreenWidth()-fillWidth)
            {
                g2d.drawLine(posX, bounds.y, posX, bounds.y+bounds.height);
                posX -= xOffset;
            }
            else
            {
                break;
            }
        }
        
        //HORIZONTAL LINES
        int posY = bounds.y;
        int yOffset = (int)Math.ceil(bounds.height*0.2);
        for (double x = 0; x < 5; x++)
        {
            g2d.drawLine(GUI.getScreenWidth(), posY, GUI.getScreenWidth()-fillWidth, posY);
            posY += yOffset;
        }
    }
}
