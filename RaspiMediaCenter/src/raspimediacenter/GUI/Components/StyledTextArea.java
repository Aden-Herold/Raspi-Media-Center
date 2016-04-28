package raspimediacenter.GUI.Components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextArea;

public class StyledTextArea extends JTextArea {
    
    public StyledTextArea (String text, int fontWeight, int fontSize, int alignment)
    {
        //super (text);
        setText(text);
        setOpaque(false);
        setForeground(Color.white);
        setFont(new Font("Bombard", fontWeight, fontSize));
        setFocusable(false);
        setWrapStyleWord(true);
        setLineWrap(true);
    }
    
    @Override
    public void paintComponent(Graphics g) 
    {       
        Graphics2D paint = (Graphics2D) g;
        
        paint.setColor(Color.black);
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        paint.fillRect(0, 0, getWidth(), getHeight());
        paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        super.paintComponent(paint);
        paint.dispose();
    }
}
