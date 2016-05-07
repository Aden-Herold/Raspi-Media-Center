package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.GUI;
import java.util.ArrayList;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JLabel;

public class StarRating {
    
    private final ArrayList<MLabel> stars = new ArrayList<>();
    private final int maximumRating;
    private final int starGap = (int)Math.floor(GUI.getScreenWidth()/56.8);
    private final int width = 400;
    
    private Rectangle bounds;
    
    public StarRating (int x, int y)
    {
        this(10, x, y);
    }
    
    public StarRating (int maxRating, int x, int y)
    {
        maximumRating = maxRating;
        
        bounds = new Rectangle(x, y, width, 50);
        
        intialiseStars();
    }

    public void updateRating(float rating)
    {
        int ratingRounded;
                
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
                stars.get(x).setSpecial(true);
                stars.get(x).setOpacity(1f);
            }
            else
            {
                stars.get(x).setSpecial(false);
                stars.get(x).setOpacity(0.5f);
            }
        }
        
        stars.get(maximumRating).setOpacity(1f);
        stars.get(maximumRating).setText(String.valueOf(rating));
    }
    
    private void intialiseStars()
    {
        Font defaultFont = new JLabel().getFont();
        defaultFont = defaultFont.deriveFont(Font.PLAIN, TextUtils.STAR_RATING_FONT_SIZE);
        int gap = 0;
        for (int x = 0; x < maximumRating+1; x++)
        {
            MLabel star = new MLabel("\u2605", TextUtils.LEFT_ALIGN, bounds.x+gap, bounds.y, 50, true);
            star.setFont(defaultFont);
            
            //star.setFont(new Font("Bombard", Font.BOLD, fontSize));
            //star.setForeground(Scene.getMenuColor().brighter());
            //star.setPreferredSize(new Dimension(fontSize+10, 30));
            
            stars.add(star);
            gap += starGap;
        }
        
        Font numFont = TextUtils.STANDARD_FONT;
        numFont = numFont.deriveFont(Font.PLAIN, TextUtils.STAR_RATING_FONT_SIZE-5);
        
        stars.get(maximumRating).setFont(numFont);
        stars.get(maximumRating).setSpecial(false);
        stars.get(maximumRating).setText("0");
    }
    
    public void drawStarRating(Graphics2D g2d)
    {
        for (MLabel star : stars) {
            star.paintSceneComponent(g2d);
        }
    }
}
