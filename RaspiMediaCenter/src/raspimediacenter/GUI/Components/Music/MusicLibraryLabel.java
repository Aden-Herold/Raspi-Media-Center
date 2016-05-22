package raspimediacenter.GUI.Components.Music;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import raspimediacenter.GUI.Components.LibraryOverview;
import raspimediacenter.GUI.Components.MLabel;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.ImageUtils;
import raspimediacenter.Logic.Utilities.TextUtils;

public class MusicLibraryLabel extends SceneComponent {

    private final int LABEL_WIDTH = (int)Math.floor(GUI.getScreenWidth()/2);
    private final int LABEL_HEIGHT = (int)Math.floor(GUI.getScreenHeight()*0.20);
    private final int ART_SIZE = (int) Math.floor(LABEL_HEIGHT * 0.9);
    private final int PADDING = (int)(Math.floor(LABEL_HEIGHT * 0.1)/2);
    
    private final Rectangle bounds;
    private final String artPath;
    private BufferedImage art;
    private final MLabel nameLabel;
    private final MLabel tagsLabel;
    private final LibraryOverview bioLabel;
    
    private boolean isVisible = true;
    private boolean isFocused = false;
    
    public MusicLibraryLabel (String artPath, String name, String tags, String bio, int x, int y, boolean isVisible)
    {
        this.isVisible = isVisible;
        this.artPath = artPath;
        art = ImageUtils.getImageFromPath(artPath);
        
        bounds = new Rectangle(x, y, LABEL_WIDTH, LABEL_HEIGHT);
        
        nameLabel = new MLabel(name, 
                                                    TextUtils.LEFT_ALIGN, 
                                                    bounds.x+ART_SIZE+(PADDING*2), 
                                                    bounds.y+PADDING*2+PADDING/2, 
                                                    bounds.width-ART_SIZE-(PADDING*2), false);
        
        tagsLabel = new MLabel(name, 
                                                    TextUtils.LEFT_ALIGN, 
                                                    bounds.x+ART_SIZE+(PADDING*2), 
                                                    bounds.y+nameLabel.getHeight()+PADDING, 
                                                    bounds.width-ART_SIZE-(PADDING*2), false);
        
        bioLabel = new LibraryOverview(bounds.x+ART_SIZE+(PADDING*2), 
                                                            bounds.y+(nameLabel.getHeight()*2)+PADDING/2, 
                                                            bounds.width-ART_SIZE-(PADDING*2), 
                                                            LABEL_HEIGHT-(nameLabel.getHeight()*2)+PADDING);
        bioLabel.setText(bio);
        bioLabel.setFont(TextUtils.SMALL_FONT);
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
    
    public BufferedImage getArt()
    {
        return art;
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
    
    public void setArt (BufferedImage art)
    {
        this.art = art;
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
        bounds.x = bounds.x + x;
    }
    
    public void updateY (int y)
    {
        bounds.y = bounds.y + y;
    }
    
    //DRAW FUNCTIONS
    @Override
    public void paintSceneComponent(Graphics2D g2d) 
    {
         if (isVisible)
        {
            final float[] gradientFractions = {0f, 0.7f};
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

            g2d.drawImage(art, bounds.x+PADDING, bounds.y+PADDING, ART_SIZE, ART_SIZE,  null);
            nameLabel.paintSceneComponent(g2d);
            //tagsLabel.paintSceneComponent(g2d);
            bioLabel.paintSceneComponent(g2d);
        }
    }
}
