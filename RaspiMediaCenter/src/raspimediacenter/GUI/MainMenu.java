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
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import raspimediacenter.GUI.Components.MenuButton;

public class MainMenu extends JPanel{

    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = dim.width;
    private final int screenHeight = dim.height;
    
    //MENU SETTINGS
    private float menuTransparency = 0.6f;
    private double menuScreenPosition = 0.7; //Percentage value of the screen from the top
    private final int menuHeight = 120;
    
    public MainMenu () {
        
        this.setLayout(null);
        createMenuButtons();
    }
    
    private void paintMenuBar (Graphics2D paint) {
        
        int menuPosY = (int)(screenHeight * menuScreenPosition);
        
        //Create Menu Background
        final Color[] backgroundGradient = {new Color(124,124,124), new Color(0, 0, 0)};
        final float[] gradientFractions = {0.0f, 1f};
        LinearGradientPaint menuGrad = new LinearGradientPaint(
                                                    new Point2D.Double(0, menuPosY),
                                                    new Point2D.Double(0, menuPosY+menuHeight),
                                                    gradientFractions,
                                                    backgroundGradient); 
        paint.setPaint(menuGrad);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuTransparency));
        paint.fillRect(0, menuPosY, screenWidth, menuHeight);
    }
    
    private void createMenuButtons () {
        
        JButton moviesBtn = new MenuButton("MOVIES");
        JButton tvShowsBtn = new MenuButton("TV SHOWS");
        JButton musicBtn = new MenuButton("MUSIC");
        JButton imagesBtn = new MenuButton("IMAGES");

        int menuOptionsWidth = (int)(screenWidth/5);
        int menuPosY = (int)(screenHeight * menuScreenPosition);
        
        this.add(moviesBtn);
        this.add(tvShowsBtn);
        this.add(musicBtn);
        this.add(imagesBtn);
        
        moviesBtn.setBounds(menuOptionsWidth/2, menuPosY, 400, menuHeight);
        tvShowsBtn.setBounds(menuOptionsWidth+(menuOptionsWidth/2), menuPosY, 400, menuHeight);
        musicBtn.setBounds((menuOptionsWidth*2)+(menuOptionsWidth/2), menuPosY, 400, menuHeight);
        imagesBtn.setBounds((menuOptionsWidth*3)+(menuOptionsWidth/2), menuPosY, 400, menuHeight);
        
        moviesBtn.requestFocus();
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        paintMenuBar(paint);
    }
}
