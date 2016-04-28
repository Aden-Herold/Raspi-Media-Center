package raspimediacenter.GUI.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.LinearGradientPaint;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.FileBrowserScene;
import raspimediacenter.GUI.Scenes.Scene;

public class ListItemButton extends JButton {
    
    private final String menuFontPath = "src/raspimediacenter/GUI/Fonts/Bombard.ttf";
    private boolean isFocused = false;
    private FileBrowserScene menu;
    private String buttonName;
    private int linkNum;
    
    private final FocusListener focusListener = new FocusListener() {

        @Override
        public void focusGained(FocusEvent e) {
            isFocused = true;
            //menu.loadBackground(Scene.getCurrentScene()+"/" + buttonName + "/series_backdrop.jpg");
            //menu.updateCurrentPoster(Scene.getCurrentScene()+"/" + buttonName + "/series_poster.jpg");
            menu.updateInfoLabels(linkNum);
            menu.updateOverview(linkNum);
            menu.updateStarRating(linkNum);
            menu.setBackground(linkNum);
            menu.setPoster(linkNum);
        }

        @Override
        public void focusLost(FocusEvent e) {
            isFocused = false;
            setForeground(Color.white);
        }
    };
    
    public ListItemButton(String s, FileBrowserScene menu, int linkNum) {
        
        super(s);
        
        this.menu = menu;
        buttonName = s;
        this.linkNum = linkNum;
        
        loadFont();
        Font menuFont = new Font("Bombard", Font.PLAIN, 25);

        this.setHorizontalAlignment(SwingConstants.RIGHT);
        setFont(menuFont);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.white);
        addFocusListener(focusListener);
        this.setPreferredSize(new Dimension((int) (SceneManager.getScreenWidth()*0.24), 45));
        
        addMouseListener(new MouseAdapter() { 
            @Override
            public void mouseEntered(MouseEvent e) {
              requestFocus();
        }
        }); 
    }
    
    private void loadFont() {
        
        try 
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(menuFontPath)));
        } 
        catch (IOException | FontFormatException e) 
        {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void paintComponent(Graphics g) 
    {       
        Graphics2D paint = (Graphics2D) g;
        
        final float[] gradientFractions = {0.0f, 1f};
        
        //Start with default black canvas
        //paint.setColor(Color.BLACK);
        //paint.fillRect(0, 0, screenWidth, screenHeight);
        
        if (!isFocused)
        {
            final Color[] backgroundGradient = {new Color(0, 0, 0, 0), Scene.getMenuColor()};
            LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                        new Point2D.Double(0, 0),
                                                        new Point2D.Double(getWidth(), getHeight()),
                                                        gradientFractions,
                                                        backgroundGradient);
            
            paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); 
            paint.setPaint(menuGrad);
            paint.fillRect(0, 0, getWidth(), getHeight());
        }
        else 
        {
            final Color[] backgroundGradient = {new Color(0, 0, 0, 0), Color.black};
            LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                        new Point2D.Double(0, 0),
                                                        new Point2D.Double(getWidth(), getHeight()),
                                                        gradientFractions,
                                                        backgroundGradient);
            
            paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            paint.setPaint(menuGrad);
            paint.fillRect(0, 0, getWidth(), getHeight());
        }
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        super.paintComponent(paint);
        paint.dispose();
    }
}
