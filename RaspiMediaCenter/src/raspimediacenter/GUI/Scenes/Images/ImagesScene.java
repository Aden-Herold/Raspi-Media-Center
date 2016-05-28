package raspimediacenter.GUI.Scenes.Images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.FileLibrary;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.FileUtils;
import raspimediacenter.Logic.Utilities.ImageUtils;

public class ImagesScene extends Scene {
    
private final String SCENE_NAME = "IMAGES";
    
//SCENE VARIABLES
    private ArrayList<String> menuList;
    private boolean painting = false;
    private String directory;
    private ArrayList<String> fileList;
    
    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    
    public ImagesScene (String dir){
        directory = dir;
    }
    
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
        fileList = FileUtils.getFilesFronDir(directory);
        
        //Create Background
        BufferedImage backdrop = ImageUtils.getImageFromPath(fileList.get(0));
        background = new Background(false);
        background.setBackgroundImage(backdrop);
        
        //Create Library List
        sceneMenu = new FileLibrary();
        sceneMenu.setupLibraryList(menuList);
        
        paintScene();
    }

    @Override
    public ArrayList<String> createMenuList() {
        ArrayList<String> menuList = new ArrayList<>();
        
        menuList = FileUtils.getAllFileNamesFromDir(directory);
        
        return menuList;
    }

    @Override
    public void unloadScene() {
        background.unload();
        background = null;
        sceneMenu.unloadMenu();
        sceneMenu = null;
    }

    // EVENT FUNCTIONS
    @Override
    public void buttonClicked() {
        
    }

    // UPDATE FUNCTIONS
    @Override
    public void updateBackground(int linkNum) {
        BufferedImage backdrop = ImageUtils.getImageFromPath(fileList.get(linkNum));
        background.setBackgroundImage(backdrop);
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

                if (!GUI.getBuffer().contentsLost())
                {
                    GUI.getBuffer().show();
                }
            }
            finally 
            {
                g2d.dispose();
                painting = false;
            }
        }
    }
}