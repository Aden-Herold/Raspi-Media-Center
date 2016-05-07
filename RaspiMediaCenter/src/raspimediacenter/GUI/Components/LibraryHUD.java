package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.GUI;
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

public class LibraryHUD extends SceneComponent {
    
    private final String side;
    private final Rectangle bounds;
    
    private MLabel headerLabel;
    private MLabel contentLabel;
    
    public LibraryHUD (String side)
    {
        this.side = side;
        bounds = new Rectangle();
    }
    
    public LibraryHUD (String side, int x, int y, int width, int height)
    {
        this.side = side;
        bounds = new Rectangle(x, y, width, height);
    }
    
    // GETTERS
    public String getHeaderInfo ()
    {
        return headerLabel.getText();
    }
    
    public String getContentInfo ()
    {
        return contentLabel.getText();
    }
    
    // SETTERS
    public void setHeaderInfo (String header)
    {
        headerLabel.setText(header);
    }
    
    public void setContentInfo (String content)
    {
        contentLabel.setText(content);
    }
    
    public void setBounds (int x, int y, int width, int height)
    {
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
    }
    
    // SETUP FUNCTIONS
    public void createCategoryInfo (String header, String content) {
        
        int textAlignment;
        Rectangle labelBounds;
        int yPos = (int)Math.floor(GUI.getScreenHeight()/38.9);
        int yGap = GUI.getScreenHeight()/36;
        
        if (side.toLowerCase().matches("left"))
        {
            textAlignment = TextUtils.LEFT_ALIGN;
            labelBounds = new Rectangle(bounds.x+20, bounds.y+yPos, bounds.width, bounds.height/2);
        }
        else 
        {
            textAlignment = TextUtils.RIGHT_ALIGN;
            labelBounds = new Rectangle(GUI.getScreenWidth()-bounds.width-20, bounds.y+yPos, bounds.width, bounds.height/2);
        }
        
        headerLabel = new MLabel(header, textAlignment, labelBounds.x, labelBounds.y, labelBounds.width, true);
        
        Font contentFont = TextUtils.STANDARD_FONT;
        contentFont = contentFont.deriveFont(Font.PLAIN, TextUtils.SMALL_FONT_SIZE);
        contentLabel = new MLabel(content, textAlignment, labelBounds.x, labelBounds.y+yGap, labelBounds.width, false);
        contentLabel.setFont(contentFont);
    }
    
    // UPDATE FUNCTIONS
    public void updateHUD(String header, String content)
    {
        headerLabel.setText(header);
        contentLabel.setText(content);
    }
    
    // DRAW FUNCTIONS
    public void drawHUD(Graphics2D g2d)
    {
        headerLabel.paintSceneComponent(g2d);
        contentLabel.paintSceneComponent(g2d);
    }
    
    private void paintTopPanel (Graphics2D paint)
    {
        int height = (int)Math.floor(GUI.getScreenHeight()/15.1);
        Color menuColor = SceneManager.getMenuColor();
        
        final Color[] backgroundGradient = {ColorUtils.darken(menuColor, 1), new Color(0, 0, 0, 0)};
        final float[] gradientFractions = {0.5f, 1f};
        LinearGradientPaint panelGrad = new LinearGradientPaint(
                                                    new Point2D.Double(bounds.x, bounds.y),
                                                    new Point2D.Double(bounds.width, bounds.y),
                                                    gradientFractions,
                                                    backgroundGradient);
        paint.setPaint(panelGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()-0.2f)); 
        
        if (side.toLowerCase().matches("left"))
        {
            paint.fillRect(bounds.x, bounds.y, bounds.width, bounds.height); 
        }
        else
        {
            paint.fillRect(bounds.x+bounds.width, bounds.y, bounds.x, bounds.height);
        }
        
        final Color[] separatorGradient = {ColorUtils.darken(menuColor, 3), new Color(0, 0, 0, 0)};
        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(bounds.x, bounds.y),
                                                    new Point2D.Double(bounds.width, bounds.y),
                                                    gradientFractions,
                                                    separatorGradient);
        paint.setPaint(separatorGrad);
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        
        if (side.toLowerCase().matches("left"))
        {
            paint.drawLine(bounds.x, bounds.height, bounds.width, bounds.height);
            paint.drawLine(bounds.x, bounds.height-1, bounds.width, bounds.height-1); 
        }
        else
        {
            paint.drawLine(bounds.x+bounds.width, bounds.height, bounds.x, bounds.height);
            paint.drawLine(bounds.x+bounds.width, bounds.height-1, bounds.x, bounds.height-1);
        }
    }

    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        paintTopPanel(g2d);
        drawHUD(g2d);
    }
}
