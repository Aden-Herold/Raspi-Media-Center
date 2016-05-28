package raspimediacenter.Logic.Utilities;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import raspimediacenter.Data.Models.Movies.MovieContainer;
import raspimediacenter.Data.Models.Movies.MovieContainer.Movie;
import raspimediacenter.Data.Models.Music.MusicAlbumContainer;
import raspimediacenter.Data.Models.Music.MusicAlbumContainer.MusicAlbum;
import raspimediacenter.Data.Models.Music.MusicArtistContainer;
import raspimediacenter.Data.Models.Music.MusicArtistContainer.MusicArtist;
import raspimediacenter.Data.Models.Music.MusicArtistSearchContainer;
import raspimediacenter.Data.Models.Music.MusicArtistSearchContainer.MusicArtistResult.ArtistMatches.FoundArtist;
import raspimediacenter.Data.Models.Music.MusicTrackContainer;
import raspimediacenter.Data.Models.Music.MusicTrackContainer.MusicTrack;
import raspimediacenter.Data.Models.Music.MusicTrackSearchContainer;
import raspimediacenter.Data.Models.Music.MusicTrackSearchContainer.MusicTrackResult.TrackMatches.FoundTrack;
import raspimediacenter.Data.Models.TV.TVEpisodeList;
import raspimediacenter.Data.Models.TV.TVSeasonContainer;
import raspimediacenter.Data.Models.TV.TVSeriesContainer;
import raspimediacenter.Data.Models.TV.TVSeriesContainer.TVSeries;
import uk.co.caprica.vlcj.filter.AudioFileFilter;
import uk.co.caprica.vlcj.filter.VideoFileFilter;

public class ScraperUtils {

    public static final String BACKDROP_SIZE = "w1920";
    public static final String POSTER_SIZE = "w780";
    public static final String STILL_SIZE = "w300";

    public static int progress, currentProgress, maxProgress;
    public static String progressText;
    public static boolean tvScraping, movieScraping, musicScraping;

    private String tmbdApiKey = System.getenv("API_KEY");
    private String tmdbBaseURI = "http://api.themoviedb.org/3/";
    private String tmdbBaseImageURI = "http://image.tmdb.org/t/p/";

    private String lastFMApiKey = System.getenv("LAST_FM");
    private String lastFMBaseURI = "http://ws.audioscrobbler.com/2.0/";

    BufferedImage backdropImage = null, posterImage = null, stillImage = null, artistImage = null, albumImage = null;

    private Movie movie;
    private MovieContainer movieContainer;
    private TVSeries series;
    private TVSeriesContainer tvSeries;
    private TVSeasonContainer tvSeason;

    private MusicAlbum album;
    private MusicAlbumContainer albumContainer;
    private MusicArtist artist;
    private MusicArtistContainer artistContainer;
    private FoundArtist foundArtist;
    private MusicArtistSearchContainer artistSearchContainer;
    private MusicTrack track;
    private MusicTrackContainer trackContainer, progressTrackContainer;
    private FoundTrack foundTrack;
    private MusicTrackSearchContainer trackSearchContainer;
    private MusicArtistContainer artistList;

    private ParserUtils parser = new ParserUtils();
    private AudioFileFilter audioFilter = new AudioFileFilter();
    private VideoFileFilter videoFilter = new VideoFileFilter();
    private String[] audioFormats = audioFilter.getExtensions();
    private String[] videoFormats = videoFilter.getExtensions();

    public class TVScraperThread implements Runnable {

        private boolean updateAll;
        private boolean withImages;

        public TVScraperThread(boolean updateAll, boolean withImages) {
            this.updateAll = updateAll;
            this.withImages = withImages;
        }

        @Override
        public void run() {
            tvScraping = true;
            parser.cleanupSeriesList();
            startTVScrape(updateAll);
            scrapeTVImages(withImages);
            tvScraping = false;
        }
    }

    public class MovieScraperThread implements Runnable {

        private boolean updateAll;
        private boolean withImages;

        public MovieScraperThread(boolean updateAll, boolean withImages) {
            this.updateAll = updateAll;
            this.withImages = withImages;
        }

        @Override
        public void run() {
            movieScraping = true;
            parser.cleanupMovieList();
            startMovieScrape(updateAll);
            scrapeMovieImages(withImages);
            movieScraping = false;
        }
    }

    public class MusicScraperThread implements Runnable {

        private boolean updateAll;
        private boolean withImages;

        public MusicScraperThread(boolean updateAll, boolean withImages) {
            this.updateAll = updateAll;
            this.withImages = withImages;
        }

        @Override
        public void run() {
            musicScraping = true;
            parser.cleanupArtistList();
            startMusicScrape(updateAll);
            scrapeMusicImages(withImages);
            musicScraping = false;
        }
    }

    public void startScrapers(boolean updateAll, boolean withImages) {
        TVScraperThread tvScraper = new TVScraperThread(updateAll, withImages);
        MovieScraperThread movieScraper = new MovieScraperThread(updateAll, withImages);
        MusicScraperThread musicScraper = new MusicScraperThread(updateAll, withImages);

        maxProgress = calculateMaxProgress(updateAll, withImages);
        System.out.println(maxProgress);
        new Thread(tvScraper).start();
        new Thread(movieScraper).start();
        new Thread(musicScraper).start();
    }

    //Iterates through series directories and seasons directories within /TV Shows/, constructs a URI with a found series directory as a search term
    //and scrapes information about that series, as well as all of its seasons as JSON from The Movie Database. Images related to the series/seasons are also scraped
    //and saved in appropriate subdirectories.
    public void startTVScrape(boolean updateAll) {
        File[] files = getDirectories("TV Shows", true);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (updateAll || !new File("TV Shows/" + name + "/info.json").exists()) {
                    progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Series - " + files[i].getPath();
                    String jsonURI = constructTMDBSearchURI("tv", parser.encodeURLParameter(name), "");
                    series = scraperParseSeries(jsonURI, series);
                    name = renameDir("TV Shows/", name, series.getName());
                    saveLocalSeriesJSON(series, "TV Shows/" + name + "/");
                    incrementProgress();
                } else {
                    series = parser.parseSeries("TV Shows/" + name + "/info.json", false);
                    parser.appendToSeriesList(series);
                }
                File[] subDirFiles = getDirectories("TV Shows/" + name, true);
                for (int j = 0; j < subDirFiles.length; j++) {
                    String subDirName = subDirFiles[j].getName();
                    if (subDirName.toLowerCase().contains("season")) {
                        if (updateAll || !new File("TV Shows/" + name + "/" + subDirName + "/info.json").exists()) {
                            progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Season - " + subDirFiles[j].getPath();
                            tvSeason = scraperParseSeason(series, subDirName);
                            saveLocalSeasonJSON(tvSeason, "TV Shows/" + name + "/" + subDirName + "/");
                            incrementProgress();
                        } else {
                            tvSeason = parser.parseSeason("TV Shows/" + name + "/" + subDirName + "/info.json", false);
                        }
                        File[] episodeFiles = getDirectories("TV Shows/" + name + "/" + subDirName, false);
                        for (int k = 0; k < episodeFiles.length; k++) {
                            boolean episodeExists = false;
                            String episodeName = episodeFiles[k].getName();
                            if (isVideoExtension(episodeFiles[k].getName())) {
                                int episodeNum = parser.trimFileName("E", episodeName);
                                if (new File("TV Shows/" + name + "/" + subDirName + "/episode-list.json").exists()) {
                                    TVEpisodeList list = parser.parseEpisodeList("TV Shows/" + name + "/" + subDirName + "/episode-list.json");
                                    for (int l = 0; l < list.episodes.size(); l++) {
                                        if (trimEpisodeNumber(episodeName).equalsIgnoreCase(list.episodes.get(l).getName())) {
                                            episodeExists = true;
                                            break;
                                        }
                                    }
                                }
                                if ((!episodeExists && episodeNum != 0) || (updateAll && episodeNum != 0)) {
                                    progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Episode - " + episodeFiles[k].getPath();
                                    renameFile(episodeFiles[k], episodeFiles[k].getParent() + "/", "E" + episodeNum + " - " + tvSeason.episodes.get(episodeNum - 1).getName());
                                    addToEpisodeList(tvSeason, trimEpisodeNumber(episodeName), name);
                                    incrementProgress();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Iterates through movie directories within /Movies/, constructs a URI with a found movie directory as a search term
    //and scrapes information about that movie from The Movie Database. Images related to the movie are also scraped
    //and saved in the movie's directory.
    public void startMovieScrape(boolean updateAll) {
        File[] files = getDirectories("Movies", true);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (updateAll || !new File("Movies/" + name + "/info.json").exists()) {
                    progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Movie - " + files[i].getPath();
                    String jsonURI = constructTMDBSearchURI("movie", parser.encodeURLParameter(name), "");
                    movie = scraperParseMovie(jsonURI, movie);
                    if (movie != null) {
                        name = renameDir("Movies/", name, movie.getTitle());
                        saveLocalMovieJSON(movie, "Movies/" + name + "/");
                        incrementProgress();
                    }
                } else {
                    movie = parser.parseMovie("Movies/" + name + "/info.json", false);
                    parser.appendToMovieList(movie);
                }
            }
        }
    }

    public void startMusicScrape(boolean updateAll) {
        File[] files = getDirectories("Music", true);
        String jsonURI;
        boolean scrapeTrack = true;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                parser.cleanupAlbumList("Music/" + name + "/album-list.json");
                if (updateAll || !new File("Music/" + name + "/info.json").exists()) {
                    progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Artist - " + files[i].getPath();
                    jsonURI = constructArtistSearchURI(parser.encodeURLParameter(name));
                    artist = scraperParseArtist(jsonURI, artist);
                    incrementProgress();
                    name = renameDir("Music/", name, artist.artist.getName());
                    saveLocalArtistJSON(artist, "Music/" + name + "/");
                } else {
                    artist = parser.parseArtist("Music/" + name + "/info.json", false);
                    parser.appendToArtistList(artist);
                }
                File[] subDirFiles = getDirectories("Music/" + name + "/", false);
                for (int j = 0; j < subDirFiles.length; j++) {
                    String subDirName = subDirFiles[j].getName();
                    if (new File("Music/" + name + "/" + subDirName + "/track-list.json").exists()) {
                        trackContainer = parser.parseTrackList("Music/" + name + "/" + subDirName + "/track-list.json", false);
                    }
                    if (subDirFiles[j].isDirectory()) {
                        File[] existingAlbum = getDirectories("Music/" + name + "/" + subDirName, false);
                        for (int k = 0; k < existingAlbum.length; k++) {
                            if (isAudioExtension(existingAlbum[k].getName())) {
                                String existingAlbumName = existingAlbum[k].getName();
                                existingAlbumName = existingAlbumName.replaceFirst("[.][^.]+$", "");
                                if (updateAll || !new File("Music/" + name + "/" + subDirName + "/info.json").exists()
                                        || !new File("Music/" + name + "/" + subDirName + "/track-list.json").exists()) {
                                    scrapeTrack = true;
                                } else {
                                    for (int l = 0; l < trackContainer.tracks.size(); l++) {
                                        if (trackContainer.tracks.get(l).track.getName().equalsIgnoreCase(existingAlbumName)) {
                                            scrapeTrack = false;
                                            break;
                                        } else {
                                            scrapeTrack = true;
                                        }
                                    }
                                }
                                if (scrapeTrack) {
                                    progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Track - " + existingAlbum[k].getPath();
                                    jsonURI = constructTrackSearchURI(parser.encodeURLParameter(existingAlbumName), parser.encodeURLParameter(name));
                                    track = scraperParseTrack(jsonURI, artist);
                                    incrementProgress();
                                    progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Album - " + existingAlbum[k].getPath();
                                    album = scraperParseAlbum(track, k);
                                    incrementProgress();
                                    saveLocalAlbumJSON(album, track, "Music/" + name + "/" + album.getName() + "/");
                                    parser.appendToTrackList(track, name, album.getName());
                                }

                            }
                        }
                    } else if (isAudioExtension(subDirFiles[j].getName())) {
                        subDirName = subDirName.replaceFirst("[.][^.]+$", "");
                        progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Track - " + subDirFiles[j].getPath();
                        jsonURI = constructTrackSearchURI(parser.encodeURLParameter(subDirName), parser.encodeURLParameter(name));
                        track = scraperParseTrack(jsonURI, artist);
                        incrementProgress();
                        progressText = "(" + (currentProgress + 1) + "/" + maxProgress + ") - Scraping Album - " + subDirFiles[j].getPath();
                        album = scraperParseAlbum(track, j);
                        incrementProgress();
                        makeDirectory(System.getProperty("user.dir") + "/Music/" + name + "/" + album.getName());
                        renameFile(subDirFiles[j], "Music/" + name + "/" + album.getName() + "/", track.track.getName());
                        subDirFiles[j].renameTo(new File(System.getProperty("user.dir") + "/Music/" + name + "/"
                                + album.getName() + "/" + subDirName));
                        saveLocalAlbumJSON(album, track, "Music/" + name + "/" + album.getName() + "/");
                        parser.appendToTrackList(track, name, album.getName());
                    }
                }

            }
        }
    }

    public void scrapeTVImages(boolean withImages) {
        File[] files = getDirectories("TV Shows", true);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String path = "TV Shows/series-list.json";
                tvSeries = parser.parseSeriesList(path, false);
                String name = tvSeries.results.get(i).getName();
                requestImageScrape(BACKDROP_SIZE, backdropImage, "series_backdrop.jpg", tvSeries.results.get(i).getBackdropPath(), "TV Shows/" + name + "/", withImages);
                requestImageScrape(POSTER_SIZE, posterImage, "series_poster.jpg", tvSeries.results.get(i).getPosterPath(), "TV Shows/" + name + "/", withImages);
                File[] subDirFiles = getDirectories("TV Shows/" + name, true);
                for (int j = 0; j < subDirFiles.length; j++) {
                    String subDirName = subDirFiles[j].getName();
                    TVSeasonContainer season = parser.parseSeason("TV Shows/" + name + "/" + subDirName + "/info.json", false);
                    requestImageScrape(POSTER_SIZE, posterImage, "season_poster.jpg", season.getPosterPath(), "TV Shows/" + name + "/" + subDirName + "/", withImages);
                    makeDirectory(System.getProperty("user.dir") + "/TV Shows/" + name + "/" + subDirName + "/Stills/");
                    if (new File("TV Shows/" + name + "/" + subDirName + "/episode-list.json").exists()) {
                        TVEpisodeList list = parser.parseEpisodeList("TV Shows/" + name + "/" + subDirName + "/episode-list.json");
                        if (list != null) {
                            for (int k = 0; k < list.episodes.size(); k++) {
                                requestImageScrape(BACKDROP_SIZE, backdropImage, "EP" + list.episodes.get(k).getEpisodeNumber() + "_still.jpg", season.episodes.get(k).getStillPath(),
                                        "TV Shows/" + name + "/" + subDirName + "/Stills/", withImages);
                            }
                        }
                    }
                }
            }
        }
    }

    public void scrapeMusicImages(boolean withImages) {
        File[] files = getDirectories("Music", true);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String path = "Music/artist-list.json";
                artistList = parser.parseArtistList(path, false);
                String name = artistList.artists.get(i).artist.getName();
                requestMusicImageScrape(artistImage, "artist_portrait.jpg", artistList.artists.get(i).artist.image.get(artistList.artists.get(i).artist.image.size() - 2).getPath(),
                        "Music/" + name + "/", withImages);
                File[] subDirFiles = getDirectories("Music/" + name, true);
                for (int j = 0; j < subDirFiles.length; j++) {
                    String subDirName = subDirFiles[j].getName();
                    album = parser.parseLocalAlbum("Music/" + name + "/" + subDirName + "/info.json");
                    requestMusicImageScrape(albumImage, "album_art.jpg", album.image.get(album.image.size() - 2).getText(), "Music/" + name + "/" + subDirName + "/", withImages);
                }
            }
        }
    }

    public void scrapeMovieImages(boolean withImages) {
        File[] files = getDirectories("Movies", true);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String path = "Movies/movie-list.json";
                movieContainer = parser.parseMovieList(path, false);
                String name = movieContainer.results.get(i).getTitle();
                requestImageScrape(BACKDROP_SIZE, backdropImage, "movie_backdrop.jpg", movieContainer.results.get(i).getBackdropPath(), "Movies/" + name + "/", withImages);
                requestImageScrape(POSTER_SIZE, posterImage, "movie_poster.jpg", movieContainer.results.get(i).getPosterPath(), "Movies/" + name + "/", withImages);
            }
        }
    }

    public void incrementProgress() {
        currentProgress++;
        //System.out.println(currentProgress + ": " + progressText);
        progress = currentProgress * 100 / maxProgress;
        System.out.println(progress + "%");
    }

    public static void resetProgress() {
        progress = currentProgress = 0;
        progressText = null;
    }

    public Movie scraperParseMovie(String jsonURI, Movie movie) {
        movieContainer = parser.parseMovieList(jsonURI, true);
        movie = movieContainer.results.get(0);
        jsonURI = constructMovieURI(movieContainer.results.get(0).getID());
        movie = parser.parseMovie(jsonURI, true);
        parser.appendToMovieList(movie);
        return movie;
    }

    public TVSeries scraperParseSeries(String jsonURI, TVSeries series) {
        tvSeries = parser.parseSeriesList(jsonURI, true);
        series = tvSeries.results.get(0);
        jsonURI = constructSeriesURI(tvSeries.results.get(0).getID());
        series = parser.parseSeries(jsonURI, true);
        parser.appendToSeriesList(series);
        return series;
    }

    public TVSeasonContainer scraperParseSeason(TVSeries series, String subDirName) {
        int seasonNo = parser.trimFileName("season ", subDirName.toLowerCase());
        String jsonURI = constructSeasonURI(series.getID(), seasonNo);
        tvSeason = parser.parseSeason(jsonURI, true);
        return tvSeason;
    }

    public void addToEpisodeList(TVSeasonContainer season, String episodeName, String show) {
        for (int i = 0; i < season.episodes.size(); i++) {
            if (episodeName.equalsIgnoreCase(season.episodes.get(i).getName())) {
                parser.appendToEpisodeList(season.episodes.get(i), show);
                break;
            }
        }
    }

    public MusicArtist scraperParseArtist(String jsonURI, MusicArtist artist) {
        artistSearchContainer = parser.parseArtistResults(jsonURI, true);
        foundArtist = artistSearchContainer.results.artistmatches.artist.get(0);
        jsonURI = constructGetInfoURI("artist", foundArtist.getMBID());
        artist = parser.parseArtist(jsonURI, true);

        if (artist != null) {
            if (artist.artist.bio != null) {
                artist.artist.bio.setSummary(trimString(artist.artist.bio.getSummary(), 0, "<a"));
            }
        }

        parser.appendToArtistList(artist);
        return artist;
    }

    public MusicTrack scraperParseTrack(String jsonURI, MusicArtist artist) {
        trackSearchContainer = parser.parseTrackResults(jsonURI, true);
        foundTrack = trackSearchContainer.results.trackmatches.track.get(0);
        jsonURI = constructGetInfoURI("track", foundTrack.getMBID());
        track = parser.parseTrack(jsonURI, true);

        if (track != null) {
            if (track.track.wiki != null) {
                track.track.wiki.setSummary(trimString(track.track.wiki.getSummary(), 0, "<a"));
            }
        }

        return track;
    }

    public MusicAlbum scraperParseAlbum(MusicTrack track, int index) {
        String jsonURI = constructGetInfoURI("album", track.track.album.getMBID());
        album = parser.parseRemoteAlbum(jsonURI);
        if (album != null) {
            if (album.wiki != null) {
                album.wiki.setSummary(trimString(album.wiki.getSummary(), 0, "<a"));
            }
            parser.appendToAlbumList(album);
        }
        return album;
    }

    //Prepares to scrape an image by first verifying if the image already exists in the chosen directory, and then
    //reading + saving the image. Checks that the imageURL given is not null, for cases when a still image path isn't provided by The Movie Database. 
    public void requestImageScrape(String size, BufferedImage image, String name, String imageURL, String path, boolean withImages) {
        File file = new File(path + "/" + name);
        if ((!file.exists() && imageURL != null) || (withImages && imageURL != null)) {
            progressText = "(" + currentProgress + "/" + maxProgress + ") Scraping Image - " + file.getPath();
            image = scrapeImage(size, imageURL);
            if (image != null) {
                saveImage(image, name, path);
                incrementProgress();
            }
        }
    }

    public void requestMusicImageScrape(BufferedImage image, String name, String imageURL, String path, boolean withImages) {
        File file = new File(path + "/" + name);
        if ((!file.exists() && imageURL != null) || (withImages && imageURL != null)) {
            progressText = "(" + currentProgress + "/" + maxProgress + ") Scraping Image - " + file.getPath();
            image = scrapeMusicImage(imageURL);
            if (image != null) {
                saveImage(image, name, path);
                incrementProgress();
            }
        }
    }

    public BufferedImage scrapeMusicImage(String imageURL) {
        URL url = null;
        BufferedImage image = null;
        try {
            url = new URL(imageURL);
            image = ImageIO.read(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convertToNonAlpha(image);
    }

    //Reads in an image from http://image.tmdb.org/t/p/ using the path to either the backdrop, poster or still image belonging
    //to the series/season/episode.
    public BufferedImage scrapeImage(String imageSize, String imageURL) {
        URL url = null;
        BufferedImage image = null;
        try {
            url = new URL(tmdbBaseImageURI + imageSize + "/" + imageURL);
            image = ImageIO.read(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convertToNonAlpha(image);
    }

    public BufferedImage convertToNonAlpha(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int[] rgb = image.getRGB(0, 0, w, h, null, 0, w);
        newImage.setRGB(0, 0, w, h, rgb, 0, w);
        return newImage;
    }

    //Saves newly read image into the specified directory
    public void saveImage(BufferedImage image, String imageName, String destination) {
        File file = new File(System.getProperty("user.dir") + "/" + destination + imageName);
        try {
            ImageIO.write(image, "jpg", file);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Makes the chosen directory if it doesn't already exist
    public void makeDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    //Renames the directory from the old name to the specified name. Is used to rename directories according
    //to scraped information.
    public String renameDir(String path, String oldName, String newName) {
        File dir = new File(path + oldName);
        File newDir = new File(path + newName);
        if (dir.isDirectory()) {
            dir.renameTo(newDir);
        }
        return newName;
    }

    public String trimEpisodeNumber(String fileName) {
        Matcher matcher = Pattern.compile(" - (.*)").matcher(fileName);
        matcher.find();
        String trimmed = matcher.group(1);
        return trimExtension(trimmed);
    }

    //Prepares to output a specified movie object to a local JSON file.
    public void saveLocalMovieJSON(Movie movie, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(movie);
        parser.beginJSONOutput(infoFile, s);
    }

    //Prepares to output a specified season object to a local JSON file.
    public void saveLocalSeasonJSON(TVSeasonContainer season, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(season);
        parser.beginJSONOutput(infoFile, s);
    }

    //Prepares to output a specified series object to a local JSON file.
    public void saveLocalSeriesJSON(TVSeries series, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(series);
        parser.beginJSONOutput(infoFile, s);
    }

    public void saveLocalArtistJSON(MusicArtist artist, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(artist);
        parser.beginJSONOutput(infoFile, s);
    }

    public void saveLocalAlbumJSON(MusicAlbum album, MusicTrack track, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(album);
        parser.beginJSONOutput(infoFile, s);
    }

    //Gets all subdirectories inside of the specified parent directory. Ignores files which
    //are not directories.
    public static File[] getDirectories(String subDir, boolean dirOnly) {
        File file = new File(subDir);
        File[] files;
        if (dirOnly) {
            files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
        } else {
            files = file.listFiles();
        }
        return files;
    }

    public static File[] getTracks(String subDir) {
        File file = new File(subDir);
        File[] files;
        files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isDirectory();
            }
        });
        return files;
    }

    public static int getNumberOfSeasons(TVSeries series) {
        File[] seasons = getDirectories(System.getProperty("user.dir") + "/TV Shows/" + series.getName(), true);
        int number = 0;
        Matcher matcher;
        for (int i = 0; i < seasons.length; i++) {
            matcher = Pattern.compile("Season (\\d+)").matcher(seasons[i].getName());
            if (matcher.find()) {
                int seasonNo = Integer.valueOf(matcher.group().substring(+7));
                if (seasonNo >= 1 && seasonNo <= series.getNumberOfSeasons()) {
                    number++;
                }
            }
        }
        return number;
    }

    public int getNumberOfEpisodes(TVSeries series, TVSeasonContainer season, int seasonNo) {
        int number = 0;
        Matcher matcher;
        if (seasonNo >= 1 && seasonNo <= series.getNumberOfSeasons()) {
            File[] episodes = getDirectories(System.getProperty("user.dir") + "/TV Shows/" + series.getName() + "/Season " + seasonNo, false);
            String episodeString = getEpisodeTitles(season);
            for (int i = 0; i < episodes.length; i++) {
                matcher = Pattern.compile("E(\\d+) - ").matcher(episodes[i].getName());
                if (matcher.find()) {
                    String episodeTitle = episodes[i].getName().substring(matcher.group().length());
                    episodeTitle = episodeTitle.substring(0, episodeTitle.length() - 4);
                    matcher = Pattern.compile(episodeTitle).matcher(episodeString);
                    if (matcher.find()) {
                        number++;
                    }
                }
            }
        }
        return number;
    }

    public String getEpisodeTitles(TVSeasonContainer seasons) {
        String names = null;
        for (int i = 0; i < seasons.episodes.size(); i++) {
            names += seasons.episodes.get(i).getName();
        }
        return names;
    }

    public boolean isAudioExtension(String fileName) {
        String extension = fileName.substring(fileName.length() - 3, fileName.length());
        for (int i = 0; i < audioFormats.length; i++) {
            if (extension.equalsIgnoreCase(audioFormats[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isVideoExtension(String fileName) {
        String extension = fileName.substring(fileName.length() - 3, fileName.length());
        for (int i = 0; i < videoFormats.length; i++) {
            if (extension.equalsIgnoreCase(videoFormats[i])) {
                return true;
            }
        }
        return false;
    }

    public int calculateMaxProgress(boolean updateAll, boolean withImages) {
        return calculateUnscrapedMovies(updateAll, withImages)
                + calculateUnscrapedMusic(updateAll, withImages)
                + calculateUnscrapedSeries(updateAll, withImages);
    }

    public int calculateUnscrapedMovies(boolean updateAll, boolean withImages) {
        int max = 0;
        File movieDirectories[] = getDirectories("Movies/", true);
        for (int i = 0; i < movieDirectories.length; i++) {
            if (updateAll) {
                max++;
                if (withImages) {
                    max += 2;
                } else {
                    if (!new File("Movies/" + movieDirectories[i].getName() + "/movie_backdrop.jpg").exists()) {
                        max++;
                    }
                    if (!new File("Movies/" + movieDirectories[i].getName() + "/movie_poster.jpg").exists()) {
                        max++;
                    }
                }
            } else {
                if (!new File("Movies/" + movieDirectories[i].getName() + "/info.json").exists()) {
                    max++;
                }
                if (!new File("Movies/" + movieDirectories[i].getName() + "/movie_backdrop.jpg").exists()) {
                    max++;
                }
                if (!new File("Movies/" + movieDirectories[i].getName() + "/movie_poster.jpg").exists()) {
                    max++;
                }
            }
        }
        return max;
    }

    public int calculateUnscrapedMusic(boolean updateAll, boolean withImages) {
        int max = 0;
        boolean trackExists = false;
        File artists[] = getDirectories("Music/", true);
        for (int i = 0; i < artists.length; i++) {
            String artist = artists[i].getName();
            if (updateAll) {
                max++;
                if (withImages) {
                    max++;
                } else {
                    if (!new File("Music/" + artist + "/artist_portrait.jpg").exists()) {
                        max++;
                    }
                }
            } else {
                if (!new File("Music/" + artist + "/info.json").exists()) {
                    max++;
                }
                if (!new File("Music/" + artist + "/artist_portrait.jpg").exists()) {
                    max++;
                }
            }
            File albums[] = getDirectories("Music/" + artist + "/", false);
            for (int j = 0; j < albums.length; j++) {
                if (albums[j].isDirectory()) {
                    String album = albums[j].getName();
                    if (updateAll) {
                        max++;
                        if (withImages) {
                            max++;
                        } else {
                            if (!new File("Music/" + artist + "/" + album + "/album_art.jpg").exists()) {
                                max++;
                            }
                        }
                    } else {
                        if (!new File("Music/" + artist + "/" + album + "/info.json").exists()) {
                            max++;
                        }
                        if (!new File("Music/" + artist + "/" + album + "/album_art.jpg").exists()) {
                            max++;
                        }
                    }
                    File tracks[] = getDirectories("Music/" + artist + "/" + album + "/", false);
                    if (new File("Music/" + artist + "/" + album + "/track-list.json").exists()) {
                        progressTrackContainer = parser.parseTrackList("Music/" + artist + "/" + album + "/track-list.json", false);
                    }
                    for (int k = 0; k < tracks.length; k++) {
                        String track = tracks[k].getName();
                        if (isAudioExtension(track)) {
                            if (updateAll) {
                                max++;
                            } else {
                                if (new File("Music/" + artist + "/" + album + "/track-list.json").exists()) {
                                    for (int l = 0; l < progressTrackContainer.tracks.size(); l++) {
                                        if (trimExtension(track).equalsIgnoreCase(progressTrackContainer.tracks.get(l).track.getName())) {
                                            trackExists = true;
                                            break;
                                        }
                                    }
                                    if (!trackExists) {
                                        trackExists = false;
                                        max++;
                                    }
                                } else {
                                    max++;
                                }
                            }
                        }
                    }
                } else {
                    if (isAudioExtension(albums[j].getName())) {
                        max += 2;
                    }
                }
            }
        }
        return max;
    }

    public int calculateUnscrapedSeries(boolean updateAll, boolean withImages) {
        int max = 0;
        File series[] = getDirectories("TV Shows/", true);
        for (int i = 0; i < series.length; i++) {
            String seriesName = series[i].getName();
            if (updateAll) {
                max++;
                if (withImages) {
                    max += 2;
                } else {
                    if (!new File("TV Shows/" + seriesName + "/series_backdrop.jpg").exists()) {
                        max++;
                    }
                    if (!new File("TV Shows/" + seriesName + "/series_poster.jpg").exists()) {
                        max++;
                    }
                }
            } else {
                if (!new File("TV Shows/" + seriesName + "/info.json").exists()) {
                    max++;
                }
                if (!new File("TV Shows/" + seriesName + "/series_backdrop.jpg").exists()) {
                    max++;
                }
                if (!new File("TV Shows/" + seriesName + "/series_poster.jpg").exists()) {
                    max++;
                }
            }
            File seasons[] = getDirectories("TV Shows/" + seriesName + "/", true);
            for (int j = 0; j < seasons.length; j++) {
                String season = seasons[j].getName();
                if (updateAll) {
                    max++;
                    if (withImages) {
                        max++;
                    } else {
                        if (!new File("TV Shows/" + seriesName + "/" + season + "/season_poster.jpg").exists()) {
                            max++;
                        }
                    }
                } else {
                    if (!new File("TV Shows/" + seriesName + "/" + season + "/info.json").exists()) {
                        max++;
                    }
                    if (!new File("TV Shows/" + seriesName + "/" + season + "/season_poster.jpg").exists()) {
                        max++;
                    }
                }
                File[] episodes = getDirectories("TV Shows/" + seriesName + "/" + season + "/", false);
                for (int k = 0; k < episodes.length; k++) {
                    boolean episodeExists = false;
                    String episode = episodes[k].getName();
                    if (isVideoExtension(episode)) {
                        if (new File("TV Shows/" + seriesName + "/" + season + "/episode-list.json").exists()) {
                            TVEpisodeList list = parser.parseEpisodeList("TV Shows/" + seriesName + "/" + season + "/episode-list.json");
                            for (int l = 0; l < list.episodes.size(); l++) {
                                if (list.episodes.get(l).getName().equalsIgnoreCase(trimEpisodeNumber(episode))) {
                                    if (updateAll) {
                                        max++;
                                    }
                                    episodeExists = true;
                                    break;
                                }
                            }
                            if (!episodeExists) {
                                max++;
                            }
                            if (withImages) {
                                max++;
                            } else {
                                if (!new File("TV Shows/" + seriesName + "/" + season + "/Stills/EP" + parser.trimFileName("E", episode) + "_still.jpg").exists()) {
                                    max++;
                                }
                            }
                        }

                    }
                }

            }
        }
        return max;
    }

    public String trimExtension(String file) {
        file = file.substring(0, file.length() - 4);
        return file;
    }

    public void renameFile(File file, String path, String rename) {
        String sub = file.getAbsolutePath();
        sub = sub.substring(sub.length() - 3, sub.length());
        file.renameTo(new File(path + rename + "." + sub));
    }

    public String trimString(String oldString, int start, String pattern) {
        String newString = oldString.substring(start, oldString.indexOf(pattern));
        return newString;
    }

    //constructs a URI for a search term for use with the 'The Movie Database API'.
    public String constructTMDBSearchURI(String type, String query, String year) {
        return tmdbBaseURI + "search/" + type + "?query=" + query + "&year=" + year
                + "&api_key=" + tmbdApiKey;
    }

    //constructs a URI to return a movie using the 'The Movie Database API'.
    public String constructMovieURI(int id) {
        return tmdbBaseURI + "movie/" + id + "?api_key=" + tmbdApiKey + "&append_to_response=release_dates";
    }

    //constructs a URI to return a series using the 'The Movie Database API'.
    public String constructSeriesURI(int id) {
        return tmdbBaseURI + "tv/" + id + "?api_key=" + tmbdApiKey + "&append_to_response=release_dates";
    }

    //constructs a URI to return a season using the 'The Movie Database API'.
    public String constructSeasonURI(int id, int season) {
        return tmdbBaseURI + "tv/" + id + "/season/" + season + "?api_key=" + tmbdApiKey;
    }

    public String constructArtistSearchURI(String artist) {
        return lastFMBaseURI + "?method=artist.search&artist=" + artist + "&api_key=" + lastFMApiKey + "&format=json";
    }

    public String constructTrackSearchURI(String track, String artist) {
        return lastFMBaseURI + "?method=track.search&track=" + track + "&artist=" + artist + "&api_key=" + lastFMApiKey + "&format=json";
    }

    public String constructGetInfoURI(String method, String mbid) {
        return lastFMBaseURI + "?method=" + method + ".getInfo&mbid=" + mbid + "&api_key=" + lastFMApiKey + "&format=json";
    }

}
