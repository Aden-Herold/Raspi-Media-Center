package raspimediacenter.GUI.Components;

import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.Timer;

public class LibraryOverview extends SceneComponent {

    private String text;
    private ArrayList<String> lines;
    
    private int lineHeight;
    private int minOffset = -1;
    private float currentOffset = -1;
    private int sizeDif = -1;
    private int overviewHeight;
    
    private long startTime = -1;
    public static final long SCROLL_TIME = 11500;
    
    private final Rectangle bounds;
    
    public LibraryOverview (int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
    }
    
    public void setText(String text)
    {
        this.text = text;
        startTime = -1;
        minOffset = -1;
        currentOffset = -1;
        scrollTimer.stop();
    } 
    
    private void wordWrap (Graphics2D g2d)
    {
        lines = new ArrayList<>();
        String wrappedString = "";

        FontMetrics fm = g2d.getFontMetrics();

	int curX = 0;
        
        String[] words = text.split(" ");
        
	for (String word : words)
	{
            int wordWidth = fm.stringWidth(word + " ");

            if (curX + wordWidth >= bounds.width)
            {
                    lines.add(wrappedString);
                    wrappedString = "";
                    curX = 0;
            }

            wrappedString += word + " ";
            curX += wordWidth;
	}
        
        lines.add(wrappedString);
    }

    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        g2d.setClip(bounds);
        wordWrap(g2d);
        
        if (minOffset < 0)
        {
            lineHeight = g2d.getFontMetrics(TextUtils.STANDARD_FONT).getHeight();
            overviewHeight = lineHeight*lines.size();
            currentOffset = minOffset = lineHeight/2;
            sizeDif = (overviewHeight - bounds.height)-minOffset+10;
            
            if (sizeDif > 0)
            {
                scrollTimer.start();
            }
        }
        
        float offset = currentOffset;
        for (String line : lines)
        {
            g2d.drawString(line, bounds.x, bounds.y+offset);
            offset+=lineHeight;
        }
    }
    
    //FADE TIMER - OVERWRITE DEFAULT TIMER BEHAVIOUR TO CHANGE OPACITY OVER TIME
    final Timer scrollTimer = new Timer(100, (ActionEvent e) -> {
        if (startTime < 0) {
            startTime = System.currentTimeMillis();
        }
        else {
            long time = System.currentTimeMillis(); 
            long duration = time - startTime;
            
            if (duration >= SCROLL_TIME) 
            {
                //Do Nothing
                if (duration >= SCROLL_TIME+2000)
                {
                    startTime = -1;
                }
                else if (duration >= SCROLL_TIME+1000)
                {
                    currentOffset = minOffset;
                }
            }
            else 
            {
                currentOffset = minOffset - (sizeDif * ((float)duration / (float)SCROLL_TIME));
            }
            SceneManager.getCurrentScene().paintScene();
        }
    });
}
