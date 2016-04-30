package raspimediacenter.GUI.Components.VideoComponents;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import raspimediacenter.GUI.Components.ListItemButton;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.VideoLibraryScene;

public class VideoListItemButton extends ListItemButton {

    private VideoLibraryScene menu;
    private int linkNum;
    private String subScene;
    
    private final FocusListener focusListener = new FocusListener() {

        @Override
        public void focusGained(FocusEvent e) {
            isFocused = true;
            if (subScene.toLowerCase().matches("seasons"))
            {
                menu.updateBackground();      
            }
            else
            {
                menu.updateBackground(linkNum);
            }

            menu.updatePoster(linkNum);
            menu.updateInformation(linkNum);
            menu.updateOverview(linkNum);
        }

        @Override
        public void focusLost(FocusEvent e) {
            isFocused = false;
            setForeground(Color.white);
        }
    };
    
    public VideoListItemButton(String s, VideoLibraryScene menu, int linkNum) {
        super(s);
        this.menu = menu;
        this.linkNum = linkNum;
        subScene = Scene.getSubScene();
        addFocusListener(focusListener);
    }
}
