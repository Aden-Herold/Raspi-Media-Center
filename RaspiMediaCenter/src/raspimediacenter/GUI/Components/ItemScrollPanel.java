package raspimediacenter.GUI.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import raspimediacenter.GUI.Scenes.Scene;

public class ItemScrollPanel extends JScrollPane {
    
    private Scene scene;
    
    public ItemScrollPanel (Component c, Scene scene)
    {
        super(c);
        this.scene = scene;
        setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setViewportBorder(null);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.getVerticalScrollBar().setUI(new ScrollBarUI());
        this.setBorder(BorderFactory.createEmptyBorder());
    }
    
    private class ScrollBarUI extends BasicScrollBarUI {
        
        protected JButton createZeroButton() {
            JButton button = new JButton();
            Dimension zeroDim = new Dimension(0,0);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override 
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
        {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.5));
            g2d.setColor(new Color(255, 255, 255));
            g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        @Override 
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
        {
            Graphics2D g2d = (Graphics2D)g;
            Color menuColor = Scene.getMenuColor();
            g2d.setColor(new Color(menuColor.getRed(), menuColor.getGreen(), menuColor.getBlue()));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Scene.getMenuTransparency()));
            g2d.fillRect(thumbBounds.x+thumbBounds.width/3, thumbBounds.y, thumbBounds.width/2, thumbBounds.height);
        }
    }
}
