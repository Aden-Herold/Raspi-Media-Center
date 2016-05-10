package raspimediacenter.GUI;

import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Scenes.Menus.MainMenuScene;
import raspimediacenter.GUI.Scenes.Movies.MoviesScene;
import raspimediacenter.GUI.Scenes.TV.TVSeriesScene;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.TV.TVEpisodesScene;
import raspimediacenter.GUI.Scenes.TV.TVSeasonsScene;
import java.awt.Color;
import java.util.ArrayList;
import raspimediacenter.GUI.Scenes.VideoPlayerScene;

public class SceneManager {

    private static ArrayList<Scene> previousScenes;
    private static Scene currentScene;
    
    private static final Color darkblue = new Color(38, 64, 115);
    public static float MENU_TRANSPARENCY = 0.9f; //Percentage value of menu transparency
    public static Color INVERTED_COLOR;
    public static Color MENU_COLOR = darkblue; //Color of menu elements
    
    public SceneManager () {
        previousScenes = new ArrayList<>();
    }
    
    //STATIC GETTERS

    public static Color getMenuColor()
    {
        return MENU_COLOR;
    }
    
    public static float getMenuTransparency()
    {
        return MENU_TRANSPARENCY;
    }

    public static Scene getCurrentScene()
    {
        return currentScene;
    }
    
    public static Scene getLastPreviousScene()
    {
        Scene lastScene = previousScenes.get(previousScenes.size()-1);
        return lastScene;
    }
    
    public static void removeLastScene()
    {
        previousScenes.remove(previousScenes.size()-1);
    }
    
    //FUNCTIONS
    public static void loadScene (Scene scene)
    {
        unloadCurrentScene();
        currentScene = scene;
        currentScene.setupScene();
    }
    
    public static void loadScene (String scene)
    {
        previousScenes.add(currentScene);
        unloadCurrentScene();
        
        if (scene.toLowerCase().matches("main menu"))
        {
            currentScene = new MainMenuScene();
        }
        else if (scene.toLowerCase().matches("movies"))
        {
            currentScene = new MoviesScene();
        }
        else if (scene.toLowerCase().matches("tv shows"))
        {
            currentScene = new TVSeriesScene();
        }
        
        currentScene.setupScene();
    }
    
    public static void loadSeasons (TVSeries series)
    {
        previousScenes.add(currentScene);
        unloadCurrentScene();
        
        currentScene = new TVSeasonsScene(series);
        currentScene.setupScene();
    }
    
    public static void loadEpisodes (TVSeries series, int seasonNumber)
    {
        previousScenes.add(currentScene);
        unloadCurrentScene();
        
        currentScene = new TVEpisodesScene(series, seasonNumber);
        currentScene.setupScene(); 
    }
    
    public static void loadVideo (TVSeries series, int seasonNumber, String episode)
    {
        previousScenes.add(currentScene);
        unloadCurrentScene();
        
        currentScene = new VideoPlayerScene(series, seasonNumber, episode);
        currentScene.setupScene();
    }
    
    private static void unloadCurrentScene()
    {
        if (currentScene != null)
        {
            currentScene.unloadScene();
            currentScene = null;
        }
    }
}
