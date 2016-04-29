package raspimediacenter.GUI.Scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import raspimediacenter.GUI.Components.ItemScrollPanel;
import raspimediacenter.GUI.Components.StarRating;
import raspimediacenter.GUI.Components.StyledLabel;
import raspimediacenter.GUI.Components.StyledTextArea;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtilities;

public class FileBrowserScene extends Scene {
 
    private SceneManager sceneManager;
    protected Image currentFanart;
    protected Image currentPoster;
    private ArrayList<JLabel> infoLabels = new ArrayList<>();
    private JTextArea descLabel;
    private StarRating starRating;
    
    protected final double LIST_LENGTH = 0.8;
    protected final double HUD_WIDTH = 0.2; 
    
    public FileBrowserScene (SceneManager sceneManager) {
        
        super(sceneManager);
        this.sceneManager = sceneManager;
        
        //Add Background to Frame
        bgCanvas.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        SceneManager.getContentPane().add(bgCanvas, 0, 0);
        
        this.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        this.setOpaque(false);
        SceneManager.getContentPane().add(this, 2, 0);
    }
    
    public void setBackground (int linkNum)
    {
        bgCanvas.setBackgroundImage(linkNum);
    }
    
    public void setPoster (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            currentPoster = MoviesScene.getPosterImage(linkNum);
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            currentPoster = TVSeriesScene.getPosterImage(linkNum);
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        }
        repaint();
    }
    
    public void setCurrentPoster (Image currentPoster)
    {
        this.currentPoster = currentPoster;
    }
    
    public void updateCurrentPoster (String path)
    {
        Image poster = ImageUtilities.getImageFromPath(path);
        setCurrentPoster(poster);
        repaint();
    }
    
    public void updateInfoLabels (int linkNum)
    {
        ArrayList<String> labelInfo = null;
        
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            labelInfo = MoviesScene.generateMoviesInfo(linkNum);
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            labelInfo = TVSeriesScene.generateTVSeriesInfo(linkNum);
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            labelInfo = TVSeriesScene.generateTVSeriesInfo(linkNum);
        }

        if (labelInfo != null)
        {
            for (int x = 0; x < infoLabels.size(); x++)
            {
                infoLabels.get(x).setText(labelInfo.get(x));
            }
        }
    }
    
    public void updateOverview (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            descLabel.setText(MoviesScene.getMovies().results.get(linkNum).getOverview());
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            descLabel.setText(TVSeriesScene.getTVSeries().results.get(linkNum).getOverview());
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        }
    }

    public void updateStarRating (int linkNum)
    {
        if (Scene.getCurrentScene().toLowerCase().matches("movies"))
        {
            starRating.updateRating(MoviesScene.getMovies().results.get(linkNum).getVoteAverage());
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("tv shows"))
        {
            starRating.updateRating(TVSeriesScene.getTVSeries().results.get(linkNum).getRatingAverage());
        }
        else if (Scene.getCurrentScene().toLowerCase().matches("music"))
        {
            
        }
    }
    
    public void createListDisplay(ArrayList<JButton> linkList)
    {
        Dimension listSize = new Dimension();
        listSize.setSize(SceneManager.getScreenWidth()*0.25, SceneManager.getScreenHeight()*LIST_LENGTH);

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setOpaque(false);
        panel.setFocusable(false);

        for (JButton link : linkList)
        {
            panel.add(link);
        }
        
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        flowPanel.setOpaque(false);
        flowPanel.setFocusable(false);
        flowPanel.add(panel);
        
        ItemScrollPanel filesList = new ItemScrollPanel(flowPanel, this);
        filesList.setBounds(SceneManager.getScreenWidth()-listSize.width+22, 0, listSize.width, listSize.height);
        
        SceneManager.getContentPane().add(filesList, 1, 0);
        
        linkList.get(0).requestFocus();
    }
    
    public void setupInfoLabels (ArrayList<String> labels)
    {
        JPanel gridLayout = new JPanel(new GridLayout(0,1));
        gridLayout.setOpaque(false);
        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        labelsPanel.setOpaque(false);
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        
        for (String label : labels)
        {
            JLabel infoLabel = new StyledLabel(label, Font.PLAIN, fontSize, SwingConstants.RIGHT);
            infoLabel.setPreferredSize(new Dimension(240, (int)Math.floor(SceneManager.getScreenHeight()*0.03)));
            gridLayout.add(infoLabel);
        }

        labelsPanel.add(gridLayout);
        labelsPanel.setBounds((int)(Math.floor(SceneManager.getScreenWidth()*0.0196)*2)+320-(240/2), 
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))+50),
                300, 240);
        SceneManager.getContentPane().add(labelsPanel, 3, 0);
    }
    
    public void createInfoDisplay (ArrayList<String> labelValues)
    {
        JPanel gridLayout = new JPanel(new GridLayout(0,1));
        gridLayout.setOpaque(false);
        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        labelsPanel.setOpaque(false);
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        
        for (String info : labelValues)
        {
            
            JLabel infoLabel = new StyledLabel(info, Font.PLAIN, fontSize, SwingConstants.LEFT);
            infoLabels.add(infoLabel);
            infoLabel.setPreferredSize(new Dimension((int) Math.floor(SceneManager.getScreenWidth()*0.2), (int)Math.floor(SceneManager.getScreenHeight()*0.03)));
            gridLayout.add(infoLabel);
        }
        
        labelsPanel.add(gridLayout);
        labelsPanel.setBounds((int)(Math.floor(SceneManager.getScreenWidth()*0.0196)*3)+320+(240/2), 
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))+50),
                (int) Math.floor(SceneManager.getScreenWidth()*0.4), 240);
        SceneManager.getContentPane().add(labelsPanel, 3, 0);
    }
    
    public void createOverviewDisplay (String description) 
    {
        Dimension descSize = new Dimension();
        descSize.setSize(SceneManager.getScreenWidth()*0.45, (int)Math.floor(SceneManager.getScreenHeight()*0.18));

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.setOpaque(false);
        panel.setFocusable(false);
        
        int fontSize = (int)Math.floor(SceneManager.getScreenWidth()*0.01);
        descLabel = new StyledTextArea(description, Font.PLAIN, fontSize, SwingConstants.LEFT);
        
        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        flowPanel.setOpaque(false);
        flowPanel.setFocusable(false);
        flowPanel.add(descLabel);
        
        ItemScrollPanel descPanel = new ItemScrollPanel(descLabel, this);
        descPanel.setBounds(SceneManager.getScreenWidth()-descSize.width-50,
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))+15),
                descSize.width, descSize.height);
        
        SceneManager.getContentPane().add(descPanel, 3, 0); 
    }

    public void createStarRating (float rating)
    {
        Dimension descSize = new Dimension();
        descSize.setSize(SceneManager.getScreenWidth()*0.4, 200);
        
        starRating = new StarRating();
        JPanel ratingPanel = starRating.getStarRatingPanel();
        
        starRating.updateRating(rating);
        
        ratingPanel.setBounds((int)Math.floor(SceneManager.getScreenWidth()*0.0196)*2+320,
                (int) (SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))+15),
                descSize.width, descSize.height);
        SceneManager.getContentPane().add(ratingPanel, 3, 0);
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        drawDetailsHUD(paint);
        drawPoster(paint);
    }
    
    protected void drawDetailsHUD(Graphics2D paint)
    {
        int hudWidth = (int)(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH));
        
        //Paint full back panel
        paint.setComposite(AlphaComposite.SrcOver.derive(0.75f));
        paint.setPaint(Scene.getMenuColor());
        paint.fillRect(0, SceneManager.getScreenHeight()-hudWidth,
                       SceneManager.getScreenWidth(), hudWidth);
        
        //Paint information background area
        final Color[] informationPanelGradient = {Scene.getMenuColor().darker(), new Color(0, 0, 0, 0)};
        final float[] infoGradFractions = {0.95f, 1f};
        LinearGradientPaint infoGrad = new LinearGradientPaint(
                                       new Point2D.Double(0, SceneManager.getScreenHeight()-hudWidth),
                                       new Point2D.Double(SceneManager.getScreenWidth()/2, SceneManager.getScreenHeight()-hudWidth),
                                                          infoGradFractions,
                                                          informationPanelGradient);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(0.95f));
        paint.setPaint(infoGrad);
        paint.fillRect(0, SceneManager.getScreenHeight()-hudWidth,
                       SceneManager.getScreenWidth()/2, hudWidth);
        
        //Paint gradient sheen over back panel
        final Color[] sheenGradient = {new Color(0, 0, 0, 0), Scene.getMenuColor().darker().darker().darker()};
        final float[] sheenGradFractions = {0.0f, 1f};
        LinearGradientPaint sheenGrad = new LinearGradientPaint(
                                       new Point2D.Double(0, (int)(SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH)))),
                                       new Point2D.Double(0, (int)Math.floor(SceneManager.getScreenHeight())),
                                                          sheenGradFractions,
                                                          sheenGradient);
            
        paint.setComposite(AlphaComposite.SrcOver.derive(1f)); 
        paint.setPaint(sheenGrad);
        paint.fillRect(0, (int)(SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))),
                       SceneManager.getScreenWidth(), (int)Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH));
        
        //Paint separator line 
        paint.setColor(Scene.getMenuColor().darker().darker().darker());
        paint.drawLine(0, (int)(SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))),
                       SceneManager.getScreenWidth(), (int)(SceneManager.getScreenHeight()-(Math.floor(SceneManager.getScreenHeight()*HUD_WIDTH))));
    }
    
    protected void drawPoster (Graphics2D paint)
    {
        paint.setComposite(AlphaComposite.SrcOver.derive(0.5f));
        paint.setColor(Color.black);
        paint.fillRect((int)Math.floor(SceneManager.getScreenWidth()*0.0196)-5, SceneManager.getScreenHeight()-480-(int)Math.floor(SceneManager.getScreenWidth()*0.0196)-5,
                330, 490);
        
        paint.setComposite(AlphaComposite.SrcOver.derive(1f));
        paint.drawImage(currentPoster, (int)Math.floor(SceneManager.getScreenWidth()*0.0196), SceneManager.getScreenHeight()-480-(int)Math.floor(SceneManager.getScreenWidth()*0.0196),
                320, 480, this);
    }
}
