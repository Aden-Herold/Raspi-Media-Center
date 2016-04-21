/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspimediacenter.GUI.Components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 *
 * @author Robert
 */
public class MenuButton extends JButton {
 
    Font menuFont = new Font("TimesRoman", Font.BOLD, 50);
    
    private final FocusListener focusListener = new FocusListener() {

        @Override
        public void focusGained(FocusEvent e) {
            setForeground(new Color(45, 89, 134));
        }

        @Override
        public void focusLost(FocusEvent e) {
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
    
    public MenuButton(String s) {
        
        super(s);
        
        setFont(menuFont);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.white);
        addFocusListener(focusListener);
        addMouseListener(mouseListener);
    }
}
