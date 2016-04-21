package raspimediacenter.GUI;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import raspimediacenter.GUI.Components.Canvas;

public class MainMenu {
    
    private final String backgroundImagePath = "src/raspimediacenter/GUI/Resources/background1.jpg";
    private final File backgroundFile = new File(backgroundImagePath);
    private final Color[] backgroundGradient = {new Color(143,153,247),
                                                new Color(103, 119, 224),
                                                new Color(80, 45, 183),
                                                new Color(35, 0, 79)};
    
    private final float[] gradientFractions = {0.0f, 0.25f, 0.5f, 0.9f};
    
    public MainMenu () {
        
    }
    
    public void createWithGradient (JFrame frame) {
        
        Canvas canvas = new Canvas(backgroundGradient, gradientFractions);
        frame.getContentPane().add(canvas);
    }
    
    public void createWithImage (JFrame frame) {

        Canvas canvas = new Canvas(backgroundFile);
        frame.getContentPane().add(canvas);
    }
}
