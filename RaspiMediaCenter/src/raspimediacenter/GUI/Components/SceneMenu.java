package raspimediacenter.GUI.Components;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class SceneMenu {

    //GETTERS
    public abstract boolean isScrollable();
    
    public abstract int getFocusedButtonPos();
    
    //SETTERS
    public abstract void setScrollable(boolean isScrollable);
    
    //SETUP FUNCTIONS
    public abstract void setupLibraryList(ArrayList<String> list);
    
    public abstract void setupMusicList(ArrayList<String> artPaths, ArrayList<String> nameList, ArrayList<String> tagList, ArrayList<String> bioList);
    
    public abstract void unloadMenu();
    
    //EVENT FUNCTIONS
    public abstract void clickedButton();
    
    public abstract void clickedButton(MouseEvent e);
    
    public abstract void focusHoveredButton(MouseEvent e);
    
    public abstract void focusNextButton();
    
    public abstract void focusPreviousButton();
    
    protected abstract void focusButton(int button);
    
    public abstract void scrollList(int direction, int amount);
    
    //DRAW FUNCTIONS
    public abstract void drawMenu(Graphics2D g2d);
}
