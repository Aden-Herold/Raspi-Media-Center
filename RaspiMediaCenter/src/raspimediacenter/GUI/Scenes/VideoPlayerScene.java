package raspimediacenter.GUI.Scenes;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.util.ArrayList;
import raspimediacenter.Data.Models.TV.TVEpisodeList;
import raspimediacenter.Data.Models.TV.TVSeasonContainer.TVSeason;
import raspimediacenter.Data.Models.TV.TVSeriesContainer.TVSeries;
import raspimediacenter.GUI.Components.SceneMenu;
import raspimediacenter.GUI.Components.Video.VideoPlayer.VideoPlayerMenu;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.ScraperUtils;

public class VideoPlayerScene extends Scene {

    private final String SCENE_NAME = "VIDEO PLAYER";

    private final TVSeries show;
    private final TVSeason season;
    private final TVEpisodeList list;

    private int position;
    private int type;

    private boolean painting = false;
    private SceneMenu sceneMenu;
    private EmbeddedVideoPlayer player;
    private boolean running = true;

    private ScraperUtils scraper = new ScraperUtils();

    public VideoPlayerScene(TVSeries show, TVSeason season, TVEpisodeList list, int position, int type) {
        this.show = show;
        this.season = season;
        this.list = list;
        this.position = position;
        this.type = type;
    }

    // GETTERS
    @Override
    public String getSceneName() {
        return SCENE_NAME;
    }

    @Override
    public EmbeddedVideoPlayer getPlayer() {
        return player;
    }

    @Override
    public SceneMenu getMenu() {
        return sceneMenu;
    }

    @Override
    public ArrayList<String> getLabelContents(int linkNum) {
        return null;
    }

    private void update() {
        while (running) {
            paintScene();

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println("Thread could not sleep: " + ex.getMessage());
            }
        }
    }

    @Override
    public void setupScene() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                update();
            }
        };

        player = new EmbeddedVideoPlayer();
        sceneMenu = new VideoPlayerMenu();
        sceneMenu.setupLibraryList(null);
        boolean episodeFound = false;
        String episodeName = null;

        File[] episodes = ScraperUtils.getDirectories("TV Shows/" + show.getName() + "/Season " + list.episodes.get(position).getSeasonNumber() + "/", false);
        
        for (int i = 0; i < episodes.length; i++) {
            if (list.episodes.get(position).getName().equals(scraper.trimEpisodeNumber(episodes[i].getName()))) {
                episodeName = episodes[i].getName();
                episodeFound = true;
                break;
            }
        }
        
        if (episodeFound) {
            player.playMedia("TV Shows/" + show.getName() + "/Season " + list.episodes.get(position).getSeasonNumber() + "/" + episodeName);
        }
        
        painting = false;
        thread.start();
    }

    @Override
    public ArrayList<String> createMenuList() {
        return null;
    }

    @Override
    public void unloadScene() {
        running = false;
        if (player.isPlaying()) {
            player.stop();
        }
        player.removeMediaControls();
        player = null;

        sceneMenu = null;
    }

    @Override
    public void buttonClicked() {
    }

    @Override
    public void updateBackground(int linkNum) {

    }

    @Override
    public void updatePreviewImage(int linkNum) {

    }

    @Override
    public void updateInformationLabels(int linkNum) {

    }

    @Override
    public void paintScene() {

        if (!painting) {
            painting = true;
            Graphics2D g2d = (Graphics2D) (EmbeddedVideoPlayer.getBuffer().getDrawGraphics());
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            try {
                sceneMenu.drawMenu(g2d);

                if (!EmbeddedVideoPlayer.getBuffer().contentsLost()) {
                    EmbeddedVideoPlayer.getBuffer().show();
                }
            } finally {
                g2d.dispose();
                painting = false;
            }
        }
    }
}
