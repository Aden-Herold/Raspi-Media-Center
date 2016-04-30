package raspimediacenter.GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;

public class StarRating {
    
    private final ArrayList<JLabel> stars = new ArrayList<>();
    private final JPanel ratingPanel;
    
    public StarRating ()
    {
        intialiseStars();
        
        ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        for (JLabel star : stars)
        {
            ratingPanel.add(star);
        }
        ratingPanel.setOpaque(false);
    }
    
    public JPanel getStarRatingPanel()
    {
        return ratingPanel;
    }
    
    public void updateRating(float rating)
    {
        int ratingRounded = Math.round(rating);
        
        for (int x = 0; x < stars.size(); x++)
        {
            
            if (x < ratingRounded)
            {
                stars.get(x).setForeground(Color.white);
            }
            else
            {
                stars.get(x).setForeground(new Color(116,118,117));
            }
        }
        
        stars.get(10).setForeground(Color.white);
        stars.get(10).setText(String.valueOf(ratingRounded));
    }
    
    private void intialiseStars()
    {
        int fontSize = 30;//(int)Math.floor(SceneManager.getScreenHeight()*0.021);
        
        for (int x = 0; x < 11; x++)
        {
            JLabel star = new StyledLabel("\u2605");
            
            star.setFont(new Font("Bombard", Font.BOLD, fontSize));
            star.setForeground(Scene.getMenuColor().brighter());
            star.setPreferredSize(new Dimension(fontSize+10, 30));
            
            stars.add(star);
        }
        
        stars.get(10).setText("0");
        stars.get(10).setForeground(Color.white);
        stars.get(10).setPreferredSize(new Dimension(100, 30));
    }
}
