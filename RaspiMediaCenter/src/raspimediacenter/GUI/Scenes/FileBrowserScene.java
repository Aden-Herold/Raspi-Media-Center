package raspimediacenter.GUI.Scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import javax.swing.JPanel;
import raspimediacenter.GUI.Components.ItemScrollPanel;
import raspimediacenter.GUI.Components.ListItemButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtilities;

public class FileBrowserScene extends Scene {
 
    private SceneManager sceneManager;
    protected Image currentFanart;
    protected Image currentPoster;
    
    public FileBrowserScene (SceneManager sceneManager) {
        
        super();
        
        this.sceneManager = sceneManager;
        
        //Add Background to Frame
        loadBackground("src/raspimediacenter/GUI/Resources/UserBackgrounds/thehobbit_desolationofsmaug_fanart.jpg");
        bgCanvas.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        SceneManager.getContentPane().add(bgCanvas, 0, 0);

        this.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        this.setOpaque(false);
        SceneManager.getContentPane().add(this, 2, 0);
    }
    
    public void setCurrentPoster (Image currentPoster)
    {
        this.currentPoster = currentPoster;
    }
    
    public void updateCurrentPoster (String path)
    {
        Image poster = ImageUtilities.getImageFromPath(path);
        setCurrentPoster(poster);
        repaint();
    }
    
    public void createListDisplay (String directory) {

        Dimension listSize = new Dimension();
        listSize.setSize(SceneManager.getScreenWidth()*0.25, SceneManager.getScreenHeight()*0.7);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setOpaque(false);

        for (int i=0; i<50; i++)
        {
            ListItemButton button = new ListItemButton("Breaking Bad - Final Showdown "+i, this);
            panel.add(button);
        }
        
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flowPanel.setOpaque(false);
        flowPanel.add(panel);
      
        ItemScrollPanel filesList = new ItemScrollPanel(panel, this);
        filesList.setBounds(SceneManager.getScreenWidth()-listSize.width, 0, listSize.width, listSize.height);
        SceneManager.getContentPane().add(filesList, 1, 0);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        drawDetailsHUD(paint);
    }
    
    protected void drawDetailsHUD(Graphics2D paint)
    {
        final Color[] backgroundGradient = {new Color(0, 0, 0, 0), Scene.getMenuColor()};
        final float[] gradientFractions = {0.0f, 1f};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                       new Point2D.Double(0, (int)(SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*0.4)))),
                                       new Point2D.Double(0, (int)Math.floor(SceneManager.getScreenHeight())),
                                                          gradientFractions,
                                                          backgroundGradient);
            
            paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1)); 
            paint.setPaint(menuGrad);
            paint.fillRect(0, (int)(SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*0.4))),
                    SceneManager.getScreenWidth(), (int)Math.floor(SceneManager.getScreenHeight()*0.4));
        
    }
    
    protected void drawPoster (Graphics2D paint)
    {
        paint.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        paint.setColor(Color.black);
        paint.fillRect(45, SceneManager.getScreenHeight()-480-60,
                330, 490);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawImage(currentPoster, 50, SceneManager.getScreenHeight()-480-55,
                320, 480, this);
    }
}
