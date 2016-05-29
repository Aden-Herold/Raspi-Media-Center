package raspimediacenter.GUI.Scenes.Menus;

import raspimediacenter.Data.Models.Movies.MovieContainer;
import raspimediacenter.Data.Models.TV.TVSeriesContainer;
import raspimediacenter.GUI.Components.Background;
import raspimediacenter.GUI.Components.Menu.MainMenu;
import raspimediacenter.GUI.Components.Menu.MenuHUD;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.Logic.Utilities.ParserUtils;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.Data.Models.Images.ImageCollectionsContainer;
import raspimediacenter.GUI.Components.Menu.ProgressBar;
import raspimediacenter.GUI.Scenes.Images.ImageCollectionScene;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.FileUtils;
import raspimediacenter.Logic.Utilities.ImageUtils;
import raspimediacenter.Logic.Utilities.ScraperUtils;

public class MainMenuScene extends Scene {

    private final String SCENE_NAME = "MAIN MENU";

    //SCENE VARIABLES
    private final ArrayList<String> menuList = new ArrayList<>(Arrays.asList("MOVIES", "TV SHOWS", "MUSIC", "IMAGES"));
    private final ArrayList<String> subHeaders = new ArrayList<>(Arrays.asList("WATCHED:", "EPISODES:", "PLAYLISTS:", "COLLECTIONS:"));
    private boolean painting = false;

    //SCENE COMPONENTS
    private Background background;
    private SceneMenu sceneMenu;
    private MenuHUD menuHUD;
    private ProgressBar progressBar;
    int progress = 0;
    String progressString = "";

    // DATA MODEL VARIABLES
    MovieContainer movies;
    TVSeriesContainer tvSeries;
    ArrayList<ImageCollectionsContainer> imageCollections;
    
    ScraperUtils scraper = new ScraperUtils();

    public MainMenuScene() {
    }

    // GETTERS
    @Override
    public String getSceneName() {
        return SCENE_NAME;
    }

    @Override
    public EmbeddedVideoPlayer getPlayer() {
        return null;
    }

    @Override
    public SceneMenu getMenu() {
        if (sceneMenu != null) {
            return sceneMenu;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<String> getLabelContents(int linkNum) {
        return null;
    }

    private String[] getMenuInfo(String menuChoice) {
        String[] info = new String[2];

        if (menuChoice.toLowerCase().matches("movies")) {
            info[0] = String.valueOf(movies.results.size());
            info[1] = "0"; // total 
        } else if (menuChoice.toLowerCase().matches("tv shows")) {
            info[0] = String.valueOf(tvSeries.results.size()); // total tv shows
            info[1] = "3929"; //total episodes
        } else if (menuChoice.toLowerCase().matches("music")) {
            info[0] = "212"; // total music
            info[1] = "14"; // playlists
        } else if (menuChoice.toLowerCase().matches("images")) {
            info[0] = String.valueOf(getTotalImages()); // total images
            info[1] = String.valueOf(imageCollections.size()); // collections
        } else {
            info[0] = "ERR";
            info[1] = "ERR";
        }

        return info;
    }

    private int getTotalImages() {
        int total = 0;
        for (ImageCollectionsContainer cont : imageCollections) {
            total += cont.imagePaths.size();
        }
        return total;
    }

    private ArrayList<ImageCollectionsContainer> getImagePathsContainer() {
        ArrayList<ImageCollectionsContainer> container = new ArrayList<>();
        ArrayList<String> directories = FileUtils.getAllSubDirsFromPath("Images/");

        for (int x = 0; x < directories.size(); x++) {
            String dir = "Images/" + directories.get(x);
            try {
                container.add(new ImageCollectionsContainer(ImageUtils.getAllImagesPathsInDir(dir, true)));
            } catch (IOException ex) {
                Logger.getLogger(ImageCollectionScene.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return container;
    }

    // SETUP && TEAR DOWN
    @Override
    public void setupScene() {
        ParserUtils parser = new ParserUtils();
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        imageCollections = getImagePathsContainer();

        //Create Background
        background = new Background(true);

        //Create Progress Bar
        progressBar = new ProgressBar(0, 0, GUI.getScreenWidth(), 0);

        //Create Library List
        sceneMenu = new MainMenu();
        sceneMenu.setupLibraryList(createMenuList());

        //Create Information Panel
        menuHUD = new MenuHUD();

        int hudWidth = (int) Math.floor(GUI.getScreenWidth() * 0.4);
        int hudHeight = (int) Math.floor(GUI.getScreenHeight() / 24);
        menuHUD.setBounds(GUI.getScreenWidth() / 2 - hudWidth / 2,
                GUI.getScreenHeight() - hudHeight,
                hudWidth, hudHeight);

        String[] header = {"MOVIES:", "WATCHED:"};
        String[] content = getMenuInfo("movies");

        menuHUD.setupHUD(header, content);

        paintScene();
        
               // EmbeddedVideoPlayer player = new EmbeddedVideoPlayer();
        //player.playMovie("TV Shows/Breaking Bad/Season 1/E4 - Cancer Man.mp4");
    }

    @Override
    public ArrayList<String> createMenuList() {
        return menuList;
    }

    @Override
    public void unloadScene() {
        background.unload();
        //background = null;
        sceneMenu.unloadMenu();
        sceneMenu = null;
        menuHUD = null;
    }

    // EVENT FUNCTIONS
    @Override
    public void buttonClicked() {
        if (GUI.getPopup() != null) {
            String btnText = GUI.getPopup().getFocusedButton().getText();
            if (btnText.contains("Images")) {
                scraper.startScrapers(true, true);
            } else {
                scraper.startScrapers(true, false);
            }
        } else {
            int focusedBtn = sceneMenu.getFocusedButtonPos();
            SceneManager.loadScene(menuList.get(focusedBtn));
        }
    }

    // UPDATE FUNCTIONS
    @Override
    public void updateBackground(int linkNum) {
    }

    @Override
    public void updatePreviewImage(int linkNum) {
    }

    @Override
    public void updateInformationLabels(int linkNum) {

        String[] header = {menuList.get(linkNum) + ":", subHeaders.get(linkNum)};
        String[] content = getMenuInfo(menuList.get(linkNum));

        menuHUD.updateHUD(header, content);
    }

    // DRAW FUNCTIONS
    @Override
    public void paintScene() {
        if (!painting) {
            painting = true;
            Graphics2D g2d = (Graphics2D) (GUI.getBuffer().getDrawGraphics());
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try {
                background.paintSceneComponent(g2d);
                sceneMenu.drawMenu(g2d);
                menuHUD.paintSceneComponent(g2d);

                if (GUI.getPopup() != null) {
                    GUI.getPopup().drawMenu(g2d);
                }
                
                progressBar.setLoadingString(ScraperUtils.progressText);
                progressBar.setProgress(ScraperUtils.progress);
                

                if (ScraperUtils.movieScraping || ScraperUtils.musicScraping || ScraperUtils.tvScraping) {
                    progressBar.setVisible(true);
                    progressBar.paintSceneComponent(g2d);
                } else {
                    ScraperUtils.resetProgress();
                    progressBar.setVisible(false);
                }

                if (!GUI.getBuffer().contentsLost()) {
                    GUI.getBuffer().show();
                }
            } catch (Exception ex) {
            } finally {
                g2d.dispose();
                painting = false;
            }
        }
    }
}
