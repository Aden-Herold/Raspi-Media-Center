package raspimediacenter.GUI.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import raspimediacenter.GUI.MainMenu;

public class MenuButton extends JButton {
 
    private final String backgroundImagePath = "src/raspimediacenter/GUI/Fonts/MonoSpatial.ttf";
    private boolean isFocused = false;
    private MainMenu menu;
    private String buttonName;

    private final FocusListener focusListener = new FocusListener() {

        @Override
        public void focusGained(FocusEvent e) {
            isFocused = true;
            menu.updateInfoLabel(buttonName);
        }

        @Override
        public void focusLost(FocusEvent e) {
            isFocused = false;
            setForeground(Color.white);
        }
    };
    
    private final MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mousePressed(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            requestFocus();
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };
    
    public MenuButton(String s, MainMenu menu) {
        
        super(s);
        
        this.menu = menu;
        buttonName = s;
        
        loadFont();
        Font menuFont = new Font("MonoSpatial", Font.BOLD, 60);

        setFont(menuFont);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.white);
        addFocusListener(focusListener);
        addMouseListener(mouseListener);
    }
    
    private void loadFont() {
        
        try 
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(backgroundImagePath)));
        } 
        catch (IOException | FontFormatException e) 
        {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void paint(Graphics g) 
    {       
        Graphics2D g2 = (Graphics2D) g.create();
        
        if (isFocused)
        {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.6));
        }
        else 
        {
          g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.2));  
        }
        
        super.paint(g2);
        g2.dispose();
    }
}
