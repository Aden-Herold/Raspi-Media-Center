package raspimediacenter.GUI;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JFrame;

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

        //Start application at MainMenu
        MainMenu mainMenu = new MainMenu();
        //mainMenu.createWithGradient(frame);
        mainMenu.createWithImage(frame);
        
        //set properties for the JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        frame.setVisible(true);
    }
}
