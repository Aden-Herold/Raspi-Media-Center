package raspimediacenter.GUI.Components.Video.VideoPlayer;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class VideoPlayerButton {

    public abstract Rectangle getRect();
    
    public abstract boolean getState();
    
    public abstract void setFocused(boolean isFocused);
    
    public abstract void setState (boolean state);
    
    public abstract void performAction ();
    
    public abstract void drawButton(Graphics2D g2d);
}
