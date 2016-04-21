package raspimediacenter.GUI;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import raspimediacenter.GUI.Components.BackgroundCanvas;

/**
 *
 * @author Robert
 */
public class GUI {
    
    //retrieve the local screen resolution
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    final int screen_Width = dim.width;
    final int screen_Height = dim.height;
    
    public GUI () {
       
        //Create a JFrame
        JFrame frame = new JFrame();
        //Start media center in full screen
        frame.setSize(screen_Width, screen_Height);

        //Define Panes - for overlapping elements
        JLayeredPane contentPane = new JLayeredPane();
        contentPane.setBounds(0, 0, screen_Width, screen_Height);
        
        //Add Background to Frame
        BackgroundCanvas background = new BackgroundCanvas(true);
        background.setBounds(0, 0, screen_Width, screen_Height);
        contentPane.add(background, new Integer(0), 0);

        
        //Start application at MainMenu - add to contentPane
        MainMenu mainMenu = new MainMenu();
        mainMenu.setBounds(0, 0, screen_Width, screen_Height);
        mainMenu.setOpaque(false);
        contentPane.add(mainMenu, new Integer(1), 0);
        frame.getContentPane().add(contentPane);
        
        //set properties for the JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setVisible(true);
    }
}
