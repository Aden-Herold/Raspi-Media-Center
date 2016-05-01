package raspimediacenter.GUI.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import raspimediacenter.GUI.Scenes.Scene;

public class StarRating {
    
    private final ArrayList<JLabel> stars = new ArrayList<>();
    private final JPanel ratingPanel;
    private final int maximumRating;
    
    public StarRating ()
    {
        this(10);
    }
    
    public StarRating (int maxRating)
    {
        maximumRating = maxRating;
        
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
        int ratingRounded = 0;
                
        if (maximumRating == 5)
        {
            ratingRounded = Math.round((rating/10)*5);
        }
        else
        {
            ratingRounded = Math.round(rating);
        }

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
        
        stars.get(maximumRating).setForeground(Color.white);
        stars.get(maximumRating).setText(String.valueOf(ratingRounded));
    }
    
    private void intialiseStars()
    {
        int fontSize = 30;//(int)Math.floor(SceneManager.getScreenHeight()*0.021);
        
        for (int x = 0; x < maximumRating+1; x++)
        {
            JLabel star = new StyledLabel("\u2605");
            
            star.setFont(new Font("Bombard", Font.BOLD, fontSize));
            star.setForeground(Scene.getMenuColor().brighter());
            star.setPreferredSize(new Dimension(fontSize+10, 30));
            
            stars.add(star);
        }
        
        stars.get(maximumRating).setText("0");
        stars.get(maximumRating).setForeground(Color.white);
        stars.get(maximumRating).setPreferredSize(new Dimension(100, 30));
    }
}
