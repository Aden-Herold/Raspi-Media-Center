package raspimediacenter.GUI.Scenes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import raspimediacenter.GUI.Components.MenuComponents.MenuButton;
import raspimediacenter.GUI.Components.MenuComponents.MenuPanel;
import raspimediacenter.GUI.Components.MenuComponents.MenuPanelGraphics;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtilities;

public class MainMenuScene extends Scene {

    private final MenuPanelGraphics menuGraphics;
    private final MenuPanel menuPanel;
    
    public MainMenuScene ( ) 
    {
        super();
        Scene.setCurrentScene("Main Menu");

        bgCanvas.setBackgroundImage(ImageUtilities.getImageFromPath(backgroundImagesPath+"/background1.jpg"));
        
        //Add Background to Frame
        loadBackgrounds();
        bgCanvas.setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        SceneManager.getContentPane().add(bgCanvas, 0, 0);
        
        menuGraphics = new MenuPanelGraphics();
        menuPanel = new MenuPanel();
        
        createMenuButtons();
        
        setBounds(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
        setOpaque(false);
        SceneManager.getContentPane().add(this, 1, 0);
    }
    
    public void updateInformation (String buttonName)
    {
        menuPanel.updateInfoLabel(buttonName);
    }
    
    private void createMenuButtons () {
        
        JButton moviesBtn = new MenuButton("MOVIES", this);
        JButton tvShowsBtn = new MenuButton("TV SHOWS", this);
        JButton musicBtn = new MenuButton("MUSIC", this);
        JButton imagesBtn = new MenuButton("IMAGES", this);
        
        moviesBtn.addActionListener(new MainMenuScene.menuOptionSelected());
        tvShowsBtn.addActionListener(new MainMenuScene.menuOptionSelected());
        
        int menuOptionsWidth = (int)(SceneManager.getScreenWidth()/5);
        int menuPosY = (int)(SceneManager.getScreenHeight() * menuGraphics.getMenuPosition());
        
        add(moviesBtn);
        add(tvShowsBtn);
        add(musicBtn);
        add(imagesBtn);
        
        moviesBtn.setBounds(menuOptionsWidth/2, menuPosY-5, 400, menuGraphics.getMenuHeight());
        tvShowsBtn.setBounds(menuOptionsWidth+(menuOptionsWidth/2), menuPosY-5, 400, menuGraphics.getMenuHeight());
        musicBtn.setBounds((menuOptionsWidth*2)+(menuOptionsWidth/2), menuPosY-5, 400, menuGraphics.getMenuHeight());
        imagesBtn.setBounds((menuOptionsWidth*3)+(menuOptionsWidth/2), menuPosY-5, 400, menuGraphics.getMenuHeight());
        
        moviesBtn.requestFocus();
    } 
   
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        paint.setRenderingHint(RenderingHints.KEY_RENDERING, 
                               RenderingHints.VALUE_RENDER_QUALITY);
        
        menuGraphics.paintMenuGraphics(paint);
    }
    
    //EVENT LISTENERS
    private class menuOptionSelected implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
             
            try 
            {   
                String option = ((JButton)ae.getSource()).getText();
                SceneManager.loadScene(option.toLowerCase());
            }
            catch (UnsupportedOperationException e) {
                
                System.out.println(e.getMessage());
            }
        }   
    }
}