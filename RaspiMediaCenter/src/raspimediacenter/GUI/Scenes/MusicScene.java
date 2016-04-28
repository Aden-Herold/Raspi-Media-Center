package raspimediacenter.GUI.Scenes;

import raspimediacenter.GUI.SceneManager;

public class MusicScene extends FileBrowserScene {
    
    public MusicScene(SceneManager sceneManager) {
        super(sceneManager);
        Scene.setCurrentScene("Music");
        createListDisplay("");
    }
}
