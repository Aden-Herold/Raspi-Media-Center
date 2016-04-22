package raspimediacenter.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import raspimediacenter.GUI.Components.MenuButton;

public class MainMenu extends JPanel{

    private final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = dim.width;
    private final int screenHeight = dim.height;
    private JLabel focusOptionInfo;
    private JLabel focusOptionItems;
    private JLabel timeLabel;
    private JLabel dateLabel;
    
    //MENU SETTINGS
    private float MENU_TRANSPARENCY = 0.9f; //Percentage value of menu transparency
    private double MENU_POSITION = 0.7; //Percentage value of the screen from the top
    private final int MENU_HEIGHT = 120; //Height of the menu bar
    private Color MENU_COLOR = new Color(0, 153, 204); //Color of menu elements
    
    Timer clockUpdateTimer = new Timer(1000, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            timeLabel.setText(timeFormat.format(new Date()));
            
            DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
            dateLabel.setText(dateFormat.format(new Date()).toUpperCase());
        }
    });
    
    public MainMenu () {
        
        this.setLayout(null);
        createMenuButtons();
        createCategoryInfo();
        createTimeInfo();
        clockUpdateTimer.start();
    }
    
    private void createMenuButtons () {
        
        JButton moviesBtn = new MenuButton("MOVIES", this);
        JButton tvShowsBtn = new MenuButton("TV SHOWS", this);
        JButton musicBtn = new MenuButton("MUSIC", this);
        JButton imagesBtn = new MenuButton("IMAGES", this);

        int menuOptionsWidth = (int)(screenWidth/5);
        int menuPosY = (int)(screenHeight * MENU_POSITION);
        
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
        
        focusOptionInfo = new JLabel();
        focusOptionInfo.setText("LIBRARY - MOVIES");
        focusOptionInfo.setFont(new Font("Bombard", Font.BOLD, 25));
        focusOptionInfo.setForeground(Color.white);
        
        focusOptionItems = new JLabel();
        focusOptionItems.setText("MOVIES: 10 UNWATCHED: 3");
        focusOptionItems.setFont(new Font("Bombard", Font.BOLD, 15));
        focusOptionItems.setForeground(Color.white);
        
        this.add(focusOptionInfo);
        this.add(focusOptionItems);
        
        focusOptionInfo.setBounds(20, 20, 400, 25);
        focusOptionItems.setBounds(20, 45, 400, 25);
    }
    
    private void createTimeInfo () {
        
        timeLabel = new JLabel();
        timeLabel.setText("00:00 XX");
        timeLabel.setFont(new Font("Bombard", Font.BOLD, 25));
        timeLabel.setForeground(Color.white);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        dateLabel = new JLabel();
        dateLabel.setText("XXX, XXX 00, 0000");
        dateLabel.setFont(new Font("Bombard", Font.BOLD, 15));
        dateLabel.setForeground(Color.white);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        this.add(timeLabel);
        this.add(dateLabel);
        
        timeLabel.setBounds(screenWidth-220, 20, 200, 25);
        dateLabel.setBounds(screenWidth-220, 45, 200, 25);
    }
    
    public void updateInfoLabel (String buttonName) {
        
        focusOptionInfo.setText("LIBRARY - " + buttonName);
        
        if (buttonName.matches("MOVIES"))
        {
            focusOptionItems.setText("FILES: 192 UNWATCHED: 82");
        }
        else if (buttonName.matches("TV SHOWS"))
        {
            focusOptionItems.setText("FILES: 2812 UNWATCHED: 2712");
        }
        else if (buttonName.matches("MUSIC"))
        {
            focusOptionItems.setText("FILES: 521");
        } 
        else if (buttonName.matches("IMAGES"))
        {
            focusOptionItems.setText("FILES: 125");
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
        final Color[] backgroundGradient = {new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue()),
                                            new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue(), 90), 
                                            new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue(), 0)};
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
        final Color[] backgroundGradient = {new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue()),
                                            new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue(), 90), 
                                            new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue(), 0)};
        final float[] gradientFractions = {0.0f, 0.7f, 1f};
        Rectangle2D rect = new Rectangle2D.Double(screenWidth-450, -120, 900, 240);
        RadialGradientPaint menuGrad = new RadialGradientPaint(
                                                    rect,
                                                    gradientFractions,
                                                    backgroundGradient,
                                                    CycleMethod.NO_CYCLE);
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        paint.fillOval(screenWidth-450, -120, 900, 240);
        
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    
    private void paintMenuBar (Graphics2D paint) {
        
        int menuPosY = (int)(screenHeight * MENU_POSITION);
        
        //Create Menu Background
        final Color[] backgroundGradient = {new Color(MENU_COLOR.getRed(), MENU_COLOR.getGreen(), MENU_COLOR.getBlue(), 150), 
                                            new Color(20, 20, 20, 225)};
        final float[] gradientFractions = {0.0f, 1f};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, menuPosY),
                                                    new Point2D.Double(0, menuPosY+MENU_HEIGHT),
                                                    gradientFractions,
                                                    backgroundGradient); 
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, MENU_TRANSPARENCY));
        paint.fillRect(0, menuPosY, screenWidth, MENU_HEIGHT);
    }
}