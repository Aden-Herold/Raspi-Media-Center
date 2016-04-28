package raspimediacenter.GUI.Scenes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import raspimediacenter.GUI.SceneManager;

public class MoviesScene extends FileBrowserScene {

    public MoviesScene(SceneManager sceneManager) {
        super(sceneManager);
        Scene.setCurrentScene("Movies");
    }
}
