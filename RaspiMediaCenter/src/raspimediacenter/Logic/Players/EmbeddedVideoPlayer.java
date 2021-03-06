package raspimediacenter.Logic.Players;

import com.sun.jna.Native;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.Data.Models.TV.TVEpisodeList;
import raspimediacenter.Data.Models.TV.TVSeasonContainer;
import raspimediacenter.Data.Models.TV.TVSeasonContainer.TVSeason;
import raspimediacenter.GUI.GUI;
import raspimediacenter.GUI.Scenes.VideoPlayerScene;
import raspimediacenter.Logic.Utilities.ParserUtils;
import raspimediacenter.Logic.Utilities.ScraperUtils;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

public class EmbeddedVideoPlayer {

    private final int MAX_RATE = 32;

    private EmbeddedMediaPlayer player;
    private final String VLCLibPath = System.getProperty("user.dir") + "/VLC/";

    private final long CONTROLS_VIS_TIME = 5000; //5 seconds
    private long startTime = -1;
    private Thread controlManager;
    private boolean controlsUsed = false;
    private boolean controlsVisible = true;
    private boolean controlsUpdating = false;
    private boolean controlsHiding = true;
    private int controlsPosDif = 0;

    private boolean isPaused;
    private boolean isMuted;
    private int rate = 1;

    private TrackThread trackThread;
    private VideoPlayerScene playerScene;
    private static BufferStrategy buffer;
    private Canvas mediaControls;

    private MediaListPlayer listPlayer;
    private MediaList list;
    private ScraperUtils scraper = new ScraperUtils();
    private ParserUtils parser = new ParserUtils();

    //======================
    //       CONSTRUCTOR
    //======================
    //Searches for the VLC libraries and plugins folder, 
    public EmbeddedVideoPlayer() {
        this.playerScene = playerScene;

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLCLibPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        MediaPlayerFactory mFactory = new MediaPlayerFactory();
        FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(GUI.getWindow());
        player = mFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
        player.setVideoSurface(mFactory.newVideoSurface(GUI.getScreen()));

        list = mFactory.newMediaList();
        listPlayer = mFactory.newMediaListPlayer();
        listPlayer.setMediaPlayer(player);
        listPlayer.setMode(MediaListPlayerMode.LOOP);

        listPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                //next item is called when the next track in the queue is lined up
            }
        });

        player.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            public void finished(EmbeddedMediaPlayer mediaPlayer) {
               //stuff to do when the video is finished
            }
        });

        setupMediaControls();
    }

    //======================
    //       GETTERS
    //======================
    public static BufferStrategy getBuffer() {
        return buffer;
    }

    //Returns the EmbeddedMediaPlayerComponent, to attach to the GUI
    public EmbeddedMediaPlayer getPlayer() {
        return player;
    }

    //Gets the length of the current media in seconds
    public long getLength() {
        return player.getLength();
    }

    //Gets the resolution of the current media, e.g width = 1920 height = 1080
    public Dimension getVideoDimension() {
        return player.getVideoDimension();
    }

    //Gets the aspect ratio of the current media.
    public String getAspectRatio() {
        return player.getAspectRatio();
    }

    //Gets the current volume level of the media player.
    public int getVolume() {
        return player.getVolume();
    }

    //Returns the id of the media's current chapter
    public int getCurrentChapter() {
        return player.getChapter();
    }

    //Returns the amount of chapters existing for the piece of media.
    public int getChapterCount() {
        return player.getChapterCount();
    }

    //Returns the current position in the media in seconds
    public long getTime() {
        return player.getTime();
    }

    //Returns the current rate for rewinding or fast-forwarding
    public int getRate() {
        return rate;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public int getSubtitlesCount() {
        return player.getSpuCount();
    }

    public boolean getControlsVisible() {
        return controlsVisible;
    }

    public boolean getControlsUpdating() {
        return controlsUpdating;
    }

    //======================
    //      FUNCTIONS
    //======================
    private void setupMediaControls() {
        mediaControls = new Canvas();
        mediaControls.setBackground(Color.BLACK);
        mediaControls.setIgnoreRepaint(true);
        mediaControls.setBounds(0, GUI.getScreenHeight() - GUI.getScreenHeight() / 10, GUI.getScreenWidth(), GUI.getScreenHeight() / 10);
        GUI.getWindow().getLayeredPane().add(mediaControls, 1, 0);

        mediaControls.createBufferStrategy(2);
        buffer = mediaControls.getBufferStrategy();

        toggleControls();
    }

    public void removeMediaControls() {
        //mediaControls.setVisible(false);
        GUI.getWindow().getLayeredPane().remove(mediaControls);
    }

    //======================
    //       MEDIA CONTROLS ANIMATION
    //======================
    public void toggleControls() {
        controlsUpdating = true;
        if (controlManager == null || !controlManager.isAlive()) {
            controlManager = new Thread(new controlsManager());
            controlManager.start();
        } else if (controlsVisible && controlsHiding) {
            controlsUsed = true;
        } else {
            controlsUsed = true;
            controlsHiding = false;
        }
    }

    private class controlsManager implements Runnable {

        @Override
        public void run() {
            while (controlsUpdating) {
                if (controlsVisible && controlsHiding) {
                    hideControls();
                } else {
                    showControls();
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EmbeddedVideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void hideControls() {
        int mcHeight = GUI.getScreenHeight() / 10 + 10;
        int yPos = mediaControls.getY() + 2;
        controlsPosDif += 2;
        if (controlsPosDif > mcHeight) {
            controlsHiding = false;
            controlsUpdating = false;
            controlsVisible = false;
        } else {
            mediaControls.setLocation(mediaControls.getX(), yPos);
        }
    }

    private void showControls() {
        int yPos = mediaControls.getY() - 2;
        controlsPosDif -= 2;
        if (controlsPosDif < 0) {
            controlsPosDif = 0;
            controlsVisible = true;
            if (startTime == -1) {
                startTime = System.currentTimeMillis();
            } else {
                long time = System.currentTimeMillis();
                long duration = time - startTime;

                if (!controlsUsed) {
                    if (duration >= CONTROLS_VIS_TIME) {
                        controlsHiding = true;
                        startTime = -1;
                    }
                } else {
                    controlsUsed = false;
                    startTime = -1;
                }
            }
        } else {
            mediaControls.setLocation(mediaControls.getX(), yPos);
        }
    }

    //===========================
    //   MEDIA PLAYER FUNCTIONS
    //===========================
    public void loadSeason(String path, TVEpisodeList episodeList) {
        File files[] = ScraperUtils.getDirectories(path, false);
        boolean isValid = false;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                if (scraper.isVideoExtension(files[i].getName())) {
                    for (int j = 0; j < episodeList.episodes.size(); j++) {
                        //System.out.println((scraper.trimEpisodeNumber(files[i].getName()) + " "  + (episodeList.episodes.get(j).getName())));
                        if (scraper.trimEpisodeNumber(files[i].getName()).equalsIgnoreCase(episodeList.episodes.get(j).getName())) {
                            isValid = true;
                            break;
                        }
                    }
                    if (isValid) {
                        try {
                            isValid = false;
                            list.addMedia(files[i].getCanonicalPath());
                        } catch (IOException ex) {
                            Logger.getLogger(EmbeddedVideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    //Plays the media file at the path specified in the method's parameter 
    public void playMedia(String fileName) {
        player.playMedia(fileName);
    }

    public String trimExtension(String file) {
        file = file.substring(0, file.length() - 4);
        return file;
    }

    public void playSeason() {
        PlayAllThread play = new PlayAllThread();
        new Thread(play).start();
    }

    public void playEpisode(int index) {
        PlayThread play = new PlayThread(index);
        new Thread(play).start();
    }

    //Toggles whether or not the media is paused or resumed.
    public void togglePause() {
        rate = 1;
        isPaused = !isPaused;
        player.setPause(isPaused);
    }

    //Stops the media and cleanly disposes of the media player
    public void stop() {
        player.stop();
        player.release();
    }

    //Increases the media player's volume by the specified increment
    public void increaseVolume(int increment) {
        player.setVolume(getVolume() + increment);
    }

    //decreases the media player's volume by the specified increment
    public void decreaseVolume(int increment) {
        player.setVolume(getVolume() - increment);
    }

    //Toggles whether or not the media player is muted or is outputing sound.
    public void toggleMute() {
        isMuted = !isMuted;
        player.mute(isMuted);
    }

    //Skips to the next available chapter in the video.
    public void nextChapter() {
        player.nextChapter();
    }

    //Returns to the last available chapter in the video.
    public void previousChapter() {
        player.previousChapter();

    }

    //Cycles between available subtitle files, and disables subtitles altogether once the end of
    //the list has been reached.
    public void toggleSubs() {
        int spu = player.getSpu();
        if (spu > -1) {
            spu++;
            if (spu > player.getSpuCount()) {
                spu = -1;
            }
        } else {
            spu = 0;
        }
        player.setSpu(spu);
    }

    //Depending on the integer passed in, will either rewind or fast-forward the video (0 = fast-forward, 1 = rewind)
    //subsequent triggers of rewind or fast-forward will increase the rate exponentially, until the max rate is reached, 
    //and the rate resets to 2x.
    public void toggleSeeking(int func) {
        if (rate < MAX_RATE) {
            if (rate == 1 && !isPaused) {
                togglePause();
                rate = 2;
                trackThread = new TrackThread(func);
                new Thread(trackThread).start();
            } else {
                rate *= 2;
            }
        } else {
            rate = 2;
        }
    }

    public class TrackThread implements Runnable {

        private int function;

        public TrackThread(int function) {
            this.function = function;
        }

        @Override
        public void run() {
            if (function == 0) {
                fastForward();
            } else {
                rewind();
            }
        }

        //gets the current position in the video, and adds 1 second multiplied by the rate. E.g., a rate of 32x will be adding 32 seconds
        //each iteration.
        public void fastForward() {
            while (isPaused) {
                seek(getTime() + (1 * rate));
            }
        }

        //Gets the current position in the video, and subtracts 1 second multiplied by the rate. E.g., a rate of 32x will be subtracting 32 seconds
        //each iteration.
        public void rewind() {
            while (isPaused) {
                seek(getTime() - (1 * rate));
            }
        }

        public void seek(long time) {
            player.setTime(time);
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(EmbeddedVideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class PlayThread implements Runnable {

        private int index;

        public PlayThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            //listPlayer.stop();
            if (list != null) {
                listPlayer.setMediaList(list);
                listPlayer.playItem(index);
            }
            try {
                Thread.currentThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HeadlessAudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public class PlayAllThread implements Runnable {

        @Override
        public void run() {
            //listPlayer.stop();
            if (list != null) {
                listPlayer.setMediaList(list);
                listPlayer.play();
            }
            try {
                Thread.currentThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HeadlessAudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
