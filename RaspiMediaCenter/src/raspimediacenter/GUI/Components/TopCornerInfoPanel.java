package raspimediacenter.GUI.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.SceneManager;
import static raspimediacenter.GUI.Scenes.Scene.getMenuColor;
import static raspimediacenter.GUI.Scenes.Scene.getMenuTransparency;

public class TopCornerInfoPanel {
    
    private final String side;
    private JLabel headerLabel;
    private JLabel contentLabel;
    
    public TopCornerInfoPanel (String side)
    {
        this.side = side;
    }
    
    public String getHeaderInfo ()
    {
        return headerLabel.getText();
    }
    
    public String getContentInfo ()
    {
        return contentLabel.getText();
    }
    
    public void setHeaderInfo (String header)
    {
        headerLabel.setText(header);
    }
    
    public void setContentInfo (String content)
    {
        contentLabel.setText(content);
    }
    
    public JPanel createCategoryInfo (String header, String content) {
        
        int textAlignment;
        float panelAlignment;
        Rectangle bounds;
        
        if (side.toLowerCase().matches("left"))
        {
            textAlignment = SwingConstants.LEFT;
            panelAlignment = Component.LEFT_ALIGNMENT;
            bounds = new Rectangle(20, 15, 400, 55);
        }
        else 
        {
            textAlignment = SwingConstants.RIGHT;
            panelAlignment = Component.RIGHT_ALIGNMENT;
            bounds = new Rectangle(SceneManager.getScreenWidth()-420, 15, 400, 55);
        }
        
        JPanel boxPanel = new JPanel();
        boxPanel.setOpaque(false);
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        headerLabel = new StyledLabel(header, Font.BOLD, 25, textAlignment);
        contentLabel = new StyledLabel(content, Font.BOLD, 15, textAlignment);
        
        headerLabel.setAlignmentX(panelAlignment);
        contentLabel.setAlignmentX(panelAlignment);
        headerLabel.setOpaque(false);
        contentLabel.setOpaque(false);
        
        boxPanel.add(headerLabel);
        boxPanel.add(contentLabel);
        //panel.add(gridPanel);
        
        boxPanel.setBounds(bounds);
        return boxPanel;
    }
}
