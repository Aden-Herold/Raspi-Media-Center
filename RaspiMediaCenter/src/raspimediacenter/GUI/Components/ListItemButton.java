package raspimediacenter.GUI.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;

public class ListItemButton extends JButton {
    
    protected boolean isFocused = false;
    
    public ListItemButton(String s) {
        
        super(s);

        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        Font menuFont = new Font("Bombard", Font.PLAIN, fontSize);

        this.setHorizontalAlignment(SwingConstants.RIGHT);
        setFont(menuFont);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.white);
        
        this.setPreferredSize(new Dimension((int) (SceneManager.getScreenWidth()*0.24), 45));
        
        addMouseListener(new MouseAdapter() { 
            @Override
            public void mouseEntered(MouseEvent e) {
              requestFocus();
            }
        }); 
    }
    
    @Override
    public void paintComponent(Graphics g) 
    {       
        Graphics2D paint = (Graphics2D) g;
        final float[] gradientFractions = {0f, 0.7f};
        
        if (!isFocused)
        {
            final Color[] backgroundGradient = {new Color(0, 0, 0, 0), Scene.getMenuColor().darker()};
            LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                        new Point2D.Double(0, 0),
                                                        new Point2D.Double(getWidth(), getHeight()),
                                                        gradientFractions,
                                                        backgroundGradient);
            
            paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Scene.getMenuTransparency())); 
            paint.setPaint(menuGrad);
            paint.fillRect(0, 0, getWidth(), getHeight());
        }
        else 
        {
            final Color[] backgroundGradient = {new Color(0, 0, 0, 0), Scene.getLighterMenuColor(1)};
            LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                        new Point2D.Double(0, 0),
                                                        new Point2D.Double(getWidth(), getHeight()),
                                                        gradientFractions,
                                                        backgroundGradient);
            
            paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Scene.getMenuTransparency()));
            paint.setPaint(menuGrad);
            paint.fillRect(0, 0, getWidth(), getHeight());
        }
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        
        //Paint separator line 
        final Color[] backgroundGradient = {new Color(0, 0, 0, 0), Scene.getMenuColor().darker().darker().darker()};
        LinearGradientPaint separatorGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, getHeight()-1),
                                                    new Point2D.Double(getWidth(), getHeight()-1),
                                                    gradientFractions,
                                                    backgroundGradient);
        paint.setPaint(separatorGrad);
        paint.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
        
        
        super.paintComponent(paint);
        paint.dispose();
    }
}
