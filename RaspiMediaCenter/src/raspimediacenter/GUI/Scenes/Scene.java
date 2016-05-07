package raspimediacenter.GUI.Scenes;

import raspimediacenter.GUI.Components.SceneMenu;
import java.util.ArrayList;

public abstract class Scene {
    
    //GETTERS
    public abstract SceneMenu getMenu();
    
    public abstract ArrayList<String> getLabelContents(int linkNum);
    
    
    // SETUP/TEARDOWN FUNCTIONS
    public abstract void setupScene();
    
    public abstract ArrayList<String> createMenuList();
    
    public abstract void unloadScene();

    //EVENT FUNCTIONS
    public abstract void buttonClicked();
    
    //UPDATE FUNCTIONS
    public abstract void updateBackground(int linkNum);
    
    public abstract void updatePreviewImage (int linkNum);
    
    public abstract void updateInformationLabels (int linkNum);
    
    //DRAW FUNCTIONS
    public abstract void paintScene();
}
