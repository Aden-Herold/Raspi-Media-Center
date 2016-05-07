package raspimediacenter.GUI.Components.Video;

import raspimediacenter.GUI.Components.MLabel;
import raspimediacenter.GUI.GUI;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class VideoInformationLabels {

    private final int maxHeaderWidth = (int)Math.floor(GUI.getScreenWidth()/17);
    private final int maxContentWidth = 400;
    private final int labelGapX = GUI.getScreenHeight()/72;
    private final int labelGapY = (int)Math.floor(GUI.getScreenWidth()/73);
    
    private ArrayList<MLabel> labelHeaders;
    private ArrayList<MLabel> labelContents;
    
    private Rectangle bounds;
    
    public VideoInformationLabels () {}
    
    public void setBounds(int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
    }
    
    public void createLabelHeader (ArrayList<String> headers)
    {
        labelHeaders = new ArrayList<>();
        int yPos = 0;
        
        for (String header : headers)
        {
            MLabel label = new MLabel(header, TextUtils.RIGHT_ALIGN, bounds.x, bounds.y+yPos, maxHeaderWidth, true);
            labelHeaders.add(label);
            yPos += labelGapY;                   
        }
    }
    
    public void createLabelContent (ArrayList<String> contents)
    {
        labelContents = new ArrayList<>();
        int yPos = 0;
        
        for (String content : contents)
        {
            MLabel label = new MLabel(content, TextUtils.LEFT_ALIGN, bounds.x+maxHeaderWidth+labelGapX, bounds.y+yPos, maxContentWidth, false);
            labelContents.add(label);
            yPos += labelGapY;                   
        }
    }
    
    public void updateLabelContent (ArrayList<String> contents)
    {
        for (int x = 0; x < contents.size(); x++)
        {
            labelContents.get(x).setText(contents.get(x));
        }
    }
    
    public void drawLabels(Graphics2D g2d)
    {
        for (int x = 0; x < labelHeaders.size(); x++)
        {
            labelHeaders.get(x).paintSceneComponent(g2d);
            labelContents.get(x).paintSceneComponent(g2d);
        }
    }
}
