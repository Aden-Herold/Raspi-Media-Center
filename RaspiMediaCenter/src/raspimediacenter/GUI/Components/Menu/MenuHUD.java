package raspimediacenter.GUI.Components.Menu;

import raspimediacenter.GUI.Components.MLabel;
import raspimediacenter.GUI.Components.SceneComponent;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ColorUtils;
import raspimediacenter.Logic.Utilities.TextUtils;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuHUD extends SceneComponent {

    private final DateFormat timeFormat = new SimpleDateFormat("h:mm a");
    private final DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
    
    private final Rectangle bounds;
    private final int shapeInset = (int)Math.floor(GUI.getScreenWidth()/51.2);
    
    private MLabel header1;
    private MLabel header2;
    private MLabel content1;
    private MLabel content2;
    private MLabel timeLabel;
    private MLabel dateLabel;
    
    // CONSTRUCTORS
    public MenuHUD () {
        bounds = new Rectangle();
    }
    
    public MenuHUD (int x, int y, int width, int height)
    {
        bounds = new Rectangle(x, y, width, height);
    }
    
    // SETTERS
    public void setHeaderInfo (String newHeader1, String newHeader2)
    {
        header1.setText(newHeader1);
        header2.setText(newHeader2);
    }
    
    public void setContentInfo (String newContent1, String newContent2)
    {
        content1.setText(newContent1);
        content2.setText(newContent2);
    }
    
    public void setTimeInfo (String time)
    {
        timeLabel.setText(time);
    }
    
    public void setDateInfo (String date)
    {
        dateLabel.setText(date);
    }
    
    public void setBounds (int x, int y, int width, int height)
    {
        bounds.x = x;
        bounds.y = y;
        bounds.width = width;
        bounds.height = height;
    }
    
    // SETUP FUNCTIONS
    public void setupHUD (String[] header, String[] content) {
        
        int width60 = (int)Math.floor(bounds.width*0.6);
        int width40 = (int)Math.floor(bounds.width*0.4);
        
        int headerWidth = (int)Math.floor(width60*0.2);
        int contentWidth = (int)Math.floor(width60*0.3)-shapeInset;

        int yPos = bounds.y+bounds.height-bounds.height/3;
        
        header1 = new MLabel(header[0], TextUtils.RIGHT_ALIGN, bounds.x+shapeInset, yPos, headerWidth, true);
        header2 = new MLabel(header[1], TextUtils.RIGHT_ALIGN, bounds.x+shapeInset+headerWidth+contentWidth, yPos, headerWidth, true);
        
        content1 = new MLabel(content[0], TextUtils.LEFT_ALIGN, bounds.x+shapeInset+headerWidth+10, yPos, contentWidth, false);
        content2 = new MLabel(content[1], TextUtils.LEFT_ALIGN, bounds.x+shapeInset+headerWidth*2+contentWidth+10, yPos, contentWidth, false);
        
        String dateS = dateFormat.format(new Date()).toUpperCase();
        String timeS = timeFormat.format(new Date()).toUpperCase();
        
        dateLabel = new MLabel(dateS, TextUtils.LEFT_ALIGN, bounds.x+width60+shapeInset/3, yPos, width40/2, false);
        timeLabel = new MLabel(timeS, TextUtils.LEFT_ALIGN, bounds.x+width60+shapeInset+width40/2, yPos, width40/2, true);
    }
    
    // UPDATE FUNCTIONS
    public void updateHUD(String[] header, String[] content)
    {
        header1.setText(header[0]);
        header2.setText(header[1]);
        content1.setText(content[0]);
        content2.setText(content[1]);
        dateLabel.setText(dateFormat.format(new Date()).toUpperCase());
        timeLabel.setText(timeFormat.format(new Date()).toUpperCase());
    }
    
    // DRAW FUNCTIONS
    @Override
    public void paintSceneComponent(Graphics2D g2d) {
        
        drawHUDShadow(g2d);
        drawHUD(g2d);
        drawHUDLabels(g2d);
    }
    
    private void drawHUDShadow (Graphics2D g2d)
    {
        GeneralPath shadow = new GeneralPath();
            //Top Line left side --> /``` 
            shadow.moveTo(bounds.x-2, bounds.y+bounds.height); 
            shadow.lineTo(bounds.x+shapeInset+4, bounds.y-10);
            shadow.lineTo(bounds.x+bounds.width-shapeInset-4, bounds.y-10);
            shadow.lineTo(bounds.x+bounds.width+2, bounds.y+bounds.height);

            
            // Inner line -> /``````
            shadow.lineTo(bounds.x+bounds.width, bounds.y+bounds.height);
            shadow.lineTo(bounds.x+bounds.width-shapeInset, bounds.y);
            shadow.lineTo(bounds.x+shapeInset, bounds.y);
            shadow.lineTo(bounds.x, bounds.y+bounds.height);
            
            //Bottom right line 
            shadow.lineTo(bounds.x-5, bounds.y+bounds.height);
        shadow.closePath();
        
        Color invis = new Color(0, 0, 0, 0);
        final float[] shadowFrac = {0.86f, 1f};
        final Color[] shadowGrad = {Color.BLACK, invis};
        LinearGradientPaint shadowGradPaint = new LinearGradientPaint(
                                                            new Point2D.Double(bounds.x, bounds.y+bounds.height),
                                                            new Point2D.Double(bounds.x, bounds.y-10),
                                                            shadowFrac,
                                                            shadowGrad);
        
        g2d.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()));
        g2d.setPaint(shadowGradPaint);
        g2d.fill(shadow);
    }
    
    private void drawHUD(Graphics2D g2d)
    {
        int width60 = (int)Math.floor(bounds.width*0.6);
        //HEADER && CONTENT PANEL
        Color menuColor = SceneManager.getMenuColor();
        g2d.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()-0.3f));
        
        GeneralPath infoPanel = new GeneralPath();
            infoPanel.moveTo(bounds.x, bounds.y+bounds.height);
            infoPanel.lineTo(bounds.x+shapeInset, bounds.y);
            infoPanel.lineTo(bounds.x+width60-shapeInset, bounds.y);
            infoPanel.lineTo(bounds.x+width60, bounds.y+bounds.height);
            infoPanel.lineTo(bounds.x, bounds.y+bounds.height);
        infoPanel.closePath();

        g2d.setPaint(menuColor);
        g2d.fill(infoPanel);
        
        //TIME && DATE PANEL
        GeneralPath timePanel = new GeneralPath();
            timePanel.moveTo(bounds.x+width60, bounds.y+bounds.height);
            timePanel.lineTo(bounds.x+width60-shapeInset, bounds.y);
            timePanel.lineTo(bounds.x+bounds.width-shapeInset, bounds.y);
            timePanel.lineTo(bounds.x+bounds.width, bounds.y+bounds.height);
            timePanel.lineTo(bounds.x+width60, bounds.y+bounds.height);
        timePanel.closePath();
            
        g2d.setComposite(AlphaComposite.SrcOver.derive(SceneManager.getMenuTransparency()-0.2f));
        g2d.setPaint(ColorUtils.darken(menuColor, 1));
        g2d.fill(timePanel); 
        
        //Create shadow overlay separating the two panels
        GeneralPath shadow = new GeneralPath();
            shadow.moveTo(bounds.x+width60-shapeInset, bounds.y);
            shadow.lineTo(bounds.x+width60, bounds.y+bounds.height);
            shadow.lineTo(bounds.x+width60+shapeInset, bounds.y+bounds.height);
            shadow.lineTo(bounds.x+width60+shapeInset, bounds.y);
        shadow.closePath();
        
        Color invis = new Color(0, 0, 0, 0);
        final float[] shadowFrac = {0f, 1f};
        final Color[] shadowGrad = {Color.black, invis};
        LinearGradientPaint shadowGradPaint = new LinearGradientPaint(
                                                            new Point2D.Double(bounds.x+width60-shapeInset, bounds.y),
                                                            new Point2D.Double(bounds.x+width60+shapeInset, bounds.y),
                                                            shadowFrac,
                                                            shadowGrad);
        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        g2d.setPaint(shadowGradPaint);
        g2d.fill(shadow);
        
        //Draw Separator line
        g2d.setPaint(ColorUtils.darken(menuColor, 3));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(bounds.x+width60-shapeInset, bounds.y, bounds.x+width60, bounds.y+bounds.height);
    }
    
    private void drawHUDLabels (Graphics2D g2d)
    {
        header1.paintSceneComponent(g2d);
        header2.paintSceneComponent(g2d);
        
        content1.paintSceneComponent(g2d);
        content2.paintSceneComponent(g2d);
        
        timeLabel.paintSceneComponent(g2d);
        dateLabel.paintSceneComponent(g2d);
    }
}
