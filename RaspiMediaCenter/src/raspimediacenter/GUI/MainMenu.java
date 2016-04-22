package raspimediacenter.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import javax.swing.*;
import raspimediacenter.GUI.Components.MenuButton;

public class MainMenu extends JPanel{

    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = dim.width;
    private final int screenHeight = dim.height;
    private JLabel focusOptionInfo;
    
    //MENU SETTINGS
    private float MENU_TRANSPARENCY = 0.9f; //Percentage value of menu transparency
    private double MENU_POSITION = 0.7; //Percentage value of the screen from the top
    private final int MENU_HEIGHT = 120; //Height of the menu bar
    
    public MainMenu () {
        
        this.setLayout(null);
        createMenuButtons();
        createCategoryInfo();
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
        focusOptionInfo.setText("LIBRARY ITEMS - MOVIES");
        focusOptionInfo.setFont(new Font("Bombard", Font.BOLD, 25));
        focusOptionInfo.setForeground(Color.white);
        
        this.add(focusOptionInfo);
        
        focusOptionInfo.setBounds(20, 20, 400, 25);
    }
    
    public void updateInfoLabel (String buttonName) {
        
        focusOptionInfo.setText("LIBRARY ITEMS - " + buttonName);
    }
 
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        paintMenuBar(paint);
    }
    
    private void paintMenuBar (Graphics2D paint) {
        
        int menuPosY = (int)(screenHeight * MENU_POSITION);
        
        //Create Menu Background
        final Color[] backgroundGradient = {new Color(124,124,124), new Color(0, 0, 0)};
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
