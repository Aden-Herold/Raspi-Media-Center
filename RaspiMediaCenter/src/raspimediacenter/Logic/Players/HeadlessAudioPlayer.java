package raspimediacenter.Logic.Players;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import raspimediacenter.Data.Models.Music.MusicAlbumContainer.MusicAlbum;
import raspimediacenter.Data.Models.Music.MusicAlbumList;
import raspimediacenter.Data.Models.Music.MusicTrackContainer;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ScraperUtils;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class HeadlessAudioPlayer {

    private final int MAX_RATE = 4;

    private final HeadlessMediaPlayer player;
    private final MediaListPlayer listPlayer;
    private MediaPlayerFactory mFactory;
    private String VLCLibPath = System.getProperty("user.dir") + "/VLC/";
    private ScraperUtils scraper = new ScraperUtils();

    private MediaList list;

    private boolean isPaused;
    private boolean isMuted;
    private int rate = 1;
    private boolean firstPlay;

    public HeadlessAudioPlayer() {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLCLibPath);
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        mFactory = new MediaPlayerFactory();
        player = mFactory.newHeadlessMediaPlayer();

        list = mFactory.newMediaList();
        listPlayer = mFactory.newMediaListPlayer();
        listPlayer.setMediaPlayer(player);
        listPlayer.setMode(MediaListPlayerMode.LOOP);
        listPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                if (!firstPlay) {
                    SceneManager.getCurrentScene().getMenu().focusNextButton();
                }
                firstPlay = false;
            }
        });
    }

    //======================
    //       GETTERS
    //======================
    public HeadlessMediaPlayer getPlayer() {
        return player;
    }

    public long getLength() {
        return player.getLength();
    }

    public int getVolume() {
        return player.getVolume();
    }

    public long getTime() {
        return player.getTime();
    }

    public int getRate() {
        return rate;
    }

    //===========================
    //   MEDIA PLAYER FUNCTIONS
    //===========================
    public void loadAlbum(String path, MusicTrackContainer track) {
        File files[] = ScraperUtils.getDirectories(path, false);
        boolean isValid = false;
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                if (scraper.isAudioExtension(files[i].getName())) {
                    for (int j = 0; j < track.tracks.size(); j++) {
                        if (trimExtension(files[i].getName()).equalsIgnoreCase(track.tracks.get(j).track.getName())) {
                            isValid = true;
                            break;
                        }
                    }
                    if (isValid) {
                        try {
                            isValid = false;
                            list.addMedia(files[i].getCanonicalPath());
                        } catch (IOException ex) {
                            Logger.getLogger(HeadlessAudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    public String trimExtension(String file) {
        file = file.substring(0, file.length() - 4);
        return file;
    }

    public void playAll() {
        firstPlay = true;
        PlayAllThread play = new PlayAllThread();
        new Thread(play).start();
    }

    public void play(int index) {
        firstPlay = true;
        PlayThread play = new PlayThread(index);
        new Thread(play).start();
    }

    public void togglePause() {
        rate = 1;
        isPaused = !isPaused;
        player.setPause(isPaused);
    }

    public void stop() {
        player.stop();
        player.release();
    }

    public void incrementVolume(int increment) {
        player.setVolume(getVolume() + increment);
    }

    public void toggleMute() {
        isMuted = !isMuted;
        player.mute(isMuted);
    }

    public void nextTrack() {
        listPlayer.playNext();
    }

    public void previousTrack() {
        listPlayer.playPrevious();
    }

    public class PlayThread implements Runnable {

        private int index;

        public PlayThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            if (list != null) {
                listPlayer.stop();
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
            if (list != null) {
                listPlayer.stop();
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
