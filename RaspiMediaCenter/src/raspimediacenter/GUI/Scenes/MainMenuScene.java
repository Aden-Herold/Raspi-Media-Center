package raspimediacenter.GUI.Scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.GUI.Components.MenuButton;
import raspimediacenter.GUI.Components.StyledLabel;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ParserUtility;

public class MainMenuScene extends Scene {

    private JLabel focusOptionInfo;
    private JLabel focusOptionItems;
    private JLabel timeLabel;
    private JLabel dateLabel;
    
    private final double MENU_POSITION = 0.7; //Percentage value of the screen from the top
    private final int MENU_HEIGHT = (int)Math.floor(SceneManager.getScreenHeight()*0.084); //Height of the menu bar
    
    private final SceneManager sceneManager;
    private TVSeriesContainer tvSeries;
    
    Timer clockUpdateTimer = new Timer(1000, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            timeLabel.setText(timeFormat.format(new Date()));
            
            DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
            dateLabel.setText(dateFormat.format(new Date()).toUpperCase());
        }
    });
    
    public MainMenuScene (SceneManager sceneManager) {
        
        super();
        
        this.sceneManager = sceneManager;
        ParserUtility parser = new ParserUtility();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        
        //Add Background to Frame
        loadBackgrounds();
        bgCanvas.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        SceneManager.getContentPane().add(bgCanvas, 0, 0);
        
        this.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        this.setOpaque(false);
        SceneManager.getContentPane().add(this, 1, 0);
        
        createMenuButtons();
        createCategoryInfo();
        createTimeInfo();
        clockUpdateTimer.start();
    }
    
    private void createMenuButtons () {
        
        JButton moviesBtn = new MenuButton("MOVIES", this);
        moviesBtn.addActionListener(new menuOptionSelected());
        JButton tvShowsBtn = new MenuButton("TV SHOWS", this);
        tvShowsBtn.addActionListener(new menuOptionSelected());
        JButton musicBtn = new MenuButton("MUSIC", this);
        musicBtn.addActionListener(new menuOptionSelected());
        JButton imagesBtn = new MenuButton("IMAGES", this);

        int menuOptionsWidth = (int)(SceneManager.getScreenWidth()/5);
        int menuPosY = (int)(SceneManager.getScreenHeight() * MENU_POSITION);
        
        this.add(moviesBtn);
        this.add(tvShowsBtn);
        this.add(musicBtn);
        this.add(imagesBtn);
        
        moviesBtn.setBounds(menuOptionsWidth/2, menuPosY-5, 400, MENU_HEIGHT);
        tvShowsBtn.setBounds(menuOptionsWidth+(menuOptionsWidth/2), menuPosY-5, 400, MENU_HEIGHT);
        musicBtn.setBounds((menuOptionsWidth*2)+(menuOptionsWidth/2), menuPosY-5, 400, MENU_HEIGHT);
        imagesBtn.setBounds((menuOptionsWidth*3)+(menuOptionsWidth/2), menuPosY-5, 400, MENU_HEIGHT);
        
        moviesBtn.requestFocus();
    }
    
    private void createCategoryInfo () {
        
        focusOptionInfo = new StyledLabel("LIBRARY - MOVIES", Font.BOLD, 25, SwingConstants.LEFT);
        focusOptionItems = new StyledLabel("MOVIES: 10 UNWATCHED: 3", Font.BOLD, 15, SwingConstants.LEFT);
        
        this.add(focusOptionInfo);
        this.add(focusOptionItems);
        
        focusOptionInfo.setBounds(20, 20, 400, 25);
        focusOptionItems.setBounds(20, 45, 400, 25);
    }
    
    private void createTimeInfo () {
        
        timeLabel = new StyledLabel("00:00 XX", Font.BOLD, 25, SwingConstants.RIGHT);
        dateLabel = new StyledLabel("XXX, XXX 00, 0000", Font.BOLD, 15, SwingConstants.RIGHT);
        
        this.add(timeLabel);
        this.add(dateLabel);
        
        timeLabel.setBounds(SceneManager.getScreenWidth()-220, 20, 200, 25);
        dateLabel.setBounds(SceneManager.getScreenWidth()-220, 45, 200, 25);
    }
    
    public void updateInfoLabel (String buttonName) {
        
        focusOptionInfo.setText("LIBRARY - " + buttonName);
        
        if (buttonName.matches("MOVIES"))
        {
            focusOptionItems.setText("MOVIES: 192   WATCHED: 82");
        }
        else if (buttonName.matches("TV SHOWS"))
        {
            int files = tvSeries.results.size();
            int eps = tvSeries.getTotalEpisodes();
            focusOptionItems.setText("TV SHOWS: " + files + "   EPISODES: " + eps);
        }
        else if (buttonName.matches("MUSIC"))
        {
            focusOptionItems.setText("SONGS: 521");
        } 
        else if (buttonName.matches("IMAGES"))
        {
            focusOptionItems.setText("IMAGES: 125");
        } 
    }
 
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        paintMenuBar(paint);
        paintInfoPanel(paint);
        paintTimePanel(paint);
    }
    
    private void paintInfoPanel (Graphics2D paint)
    {
        final Color[] backgroundGradient = {new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue()),
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 90), 
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 0)};
        final float[] gradientFractions = {0.0f, 0.7f, 1f};
        Rectangle2D rect = new Rectangle2D.Double(-450, -120, 900, 240);
        RadialGradientPaint menuGrad = new RadialGradientPaint(
                                                    rect,
                                                    gradientFractions,
                                                    backgroundGradient,
                                                    CycleMethod.NO_CYCLE);
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        paint.fillOval(-450, -120, 900, 240);
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void paintTimePanel (Graphics2D paint)
    {
        final Color[] backgroundGradient = {new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue()),
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 90), 
                                            new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 0)};
        final float[] gradientFractions = {0.0f, 0.7f, 1f};
        Rectangle2D rect = new Rectangle2D.Double(SceneManager.getScreenWidth()-450, -120, 900, 240);
        RadialGradientPaint menuGrad = new RadialGradientPaint(
                                                    rect,
                                                    gradientFractions,
                                                    backgroundGradient,
                                                    CycleMethod.NO_CYCLE);
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        paint.fillOval(SceneManager.getScreenWidth()-450, -120, 900, 240);
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void paintMenuBar (Graphics2D paint) {
        
        int menuPosY = (int)(SceneManager.getScreenHeight() * MENU_POSITION);
        
        //Create Menu Background
        final Color[] backgroundGradient = {new Color(getMenuColor().getRed(), getMenuColor().getGreen(), getMenuColor().getBlue(), 150), 
                                            new Color(20, 20, 20, 225)};
        final float[] gradientFractions = {0.0f, 1f};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, menuPosY),
                                                    new Point2D.Double(0, menuPosY+MENU_HEIGHT),
                                                    gradientFractions,
                                                    backgroundGradient); 
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getMenuTransparency()));
        paint.fillRect(0, menuPosY, SceneManager.getScreenWidth(), MENU_HEIGHT);
    }
   
    //EVENT LISTENERS
    private class menuOptionSelected implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
             
            try 
            {   
                String option = ((JButton)ae.getSource()).getText();
                sceneManager.loadScene(option.toLowerCase());
            }
            catch (UnsupportedOperationException e) {
                
                System.out.println(e.getMessage());
            }
        }   
    }
}