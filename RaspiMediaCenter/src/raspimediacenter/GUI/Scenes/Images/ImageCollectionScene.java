package raspimediacenter.GUI.Scenes.Images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import raspimediacenter.Data.Models.Images.ImageCollectionsContainer;
import raspimediacenter.GUI.Components.FileLibrary;
import raspimediacenter.GUI.Components.Images.ImageCollectionBackground;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.FileUtils;
import raspimediacenter.Logic.Utilities.ImageUtils;

public class ImageCollectionScene extends Scene {

    private final String SCENE_NAME = "IMAGE COLLECTIONS";
    
    //SCENE VARIABLES
    private ArrayList<String> menuList;
    private boolean painting = false;
    
    //SCENE COMPONENTS
    private ImageCollectionBackground background;
    private SceneMenu sceneMenu;
    
    public ImageCollectionScene (){}
    
    // GETTERS
    @Override
    public String getSceneName ()
    {
        return SCENE_NAME;
    }
    
    @Override 
    public EmbeddedVideoPlayer getPlayer()
    {
        //NO VIDEO PLAYER
        return null;
    }
    
    @Override
    public SceneMenu getMenu() {
        if (sceneMenu != null)
        {
            return sceneMenu;
        }
        else
        {
            return null;
        }
    }

    @Override
    public ArrayList<String> getLabelContents(int linkNum) {
        //NO INFO PANEL
        return null;
    }

    // SETUP && TEAR DOWN
    @Override
    public void setupScene() {

        menuList = createMenuList();
        
        //Create Library List
        sceneMenu = new FileLibrary();
        sceneMenu.setupLibraryList(menuList);
        
        //Create Background
        background = new ImageCollectionBackground(getStarterImages(), getImagePathsContainer());
        background.changeBackground(0);

        paintScene();
    }

    @Override
    public ArrayList<String> createMenuList() {
        ArrayList<String> menuList = new ArrayList<>();
        
        menuList = FileUtils.getAllSubDirsFromPath("Images/");
        
        return menuList;
    }
    
    private ArrayList<BufferedImage> getStarterImages ()
    {
        ArrayList<BufferedImage> starterImages = new ArrayList<>();
        
        
        for (int x = 0;  x < menuList.size(); x++)
        {
            String dir = "Images/"+menuList.get(x)+"/";
            String str = ImageUtils.getFirstImagePathInDir(dir);
            starterImages.add(ImageUtils.getImageFromPath(str));
        }
        
        return  starterImages;
    }
    
    private ArrayList<ImageCollectionsContainer> getImagePathsContainer()
    {
        ArrayList<ImageCollectionsContainer> container = new ArrayList<>();
        
        for (int x = 0; x < menuList.size(); x++)
        {
            String dir = "Images/"+menuList.get(x);
            try {
                container.add(new ImageCollectionsContainer(ImageUtils.getAllImagesPathsInDir(dir, true)));
            } catch (IOException ex) {
                Logger.getLogger(ImageCollectionScene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return container;
    }

    @Override
    public void unloadScene() {
        background = null;
        sceneMenu.unloadMenu();
        sceneMenu = null;
    }

    // EVENT FUNCTIONS
    @Override
    public void buttonClicked() 
    {
        if (GUI.getPopup() != null)
        {
            String btnText = GUI.getPopup().getFocusedButton().getText();
            
            // Retrieve the user preference node for current user
            Preferences prefs = Preferences.userRoot();
            final String PREF_NAME = "backgroundPrefs";
            
            if (btnText.contains("Reset"))
            {
                final String newValue = "Resources/UserBackgrounds/";
                prefs.put(PREF_NAME, newValue);
            }
            else
            {
                int focused = sceneMenu.getFocusedButtonPos();
                final String newValue = "Images/"+menuList.get(focused)+"/";
                prefs.put(PREF_NAME, newValue);
            }
        }
        else
        {
            int dirNum = sceneMenu.getFocusedButtonPos();
            String dir = "Images/"+menuList.get(dirNum)+"/";
            SceneManager.loadImages(dir);
        }
    }

    // UPDATE FUNCTIONS
    @Override
    public void updateBackground(int linkNum) {
        background.changeBackground(linkNum);
    }

    @Override
    public void updatePreviewImage(int linkNum) {
    }

    @Override
    public void updateInformationLabels(int linkNum) {
    }

    // DRAW FUNCTIONS
    @Override
    public void paintScene() {
        if (!painting)
        {
            painting = true;
            Graphics2D g2d = (Graphics2D)(GUI.getBuffer().getDrawGraphics());
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try 
            {
                background.paintSceneComponent(g2d);
                sceneMenu.drawMenu(g2d);
                
                if (GUI.getPopup() != null)
                {
                    GUI.getPopup().drawMenu(g2d);
                }

                if (!GUI.getBuffer().contentsLost())
                {
                    GUI.getBuffer().show();
                }
            }
            catch (Exception ex){}
            finally 
            {
                g2d.dispose();
                painting = false;
            }
        }
    }
}