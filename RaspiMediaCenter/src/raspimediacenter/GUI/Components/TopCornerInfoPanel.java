package raspimediacenter.GUI.Components;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.SceneManager;

public class TopCornerInfoPanel extends JPanel {
    
    private final String side;
    private JLabel headerLabel;
    private JLabel contentLabel;
    
    public TopCornerInfoPanel (String side)
    {
        this.side = side;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
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
        
        headerLabel = new StyledLabel(header, Font.BOLD, 25, textAlignment);
        contentLabel = new StyledLabel(content, Font.BOLD, 15, textAlignment);
        
        headerLabel.setAlignmentX(panelAlignment);
        contentLabel.setAlignmentX(panelAlignment);
        headerLabel.setOpaque(false);
        contentLabel.setOpaque(false);
        
        this.add(headerLabel);
        this.add(contentLabel);
        
        this.setBounds(bounds);
        return this;
    }
}
