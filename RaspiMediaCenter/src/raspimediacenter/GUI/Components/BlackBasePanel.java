package raspimediacenter.GUI.Components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import raspimediacenter.GUI.SceneManager;

public class BlackBasePanel extends JPanel {
    
    public BlackBasePanel ()
    {
        super();
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D paint = (Graphics2D) g;
        
        //Start with default black canvas
        paint.setColor(Color.BLACK);
        paint.fillRect(0, 0, SceneManager.getScreenWidth(), SceneManager.getScreenHeight());
    }
}
