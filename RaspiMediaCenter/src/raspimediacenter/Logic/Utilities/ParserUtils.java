package raspimediacenter.Logic.Utilities;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import raspimediacenter.Data.Models.Movies.MovieContainer;
import raspimediacenter.Data.Models.Movies.MovieContainer.Movie;
import raspimediacenter.Data.Models.Music.MusicAlbumContainer;
import raspimediacenter.Data.Models.Music.MusicAlbumContainer.MusicAlbum;
import raspimediacenter.Data.Models.Music.MusicAlbumList;
import raspimediacenter.Data.Models.Music.MusicArtistContainer;
import raspimediacenter.Data.Models.Music.MusicArtistContainer.MusicArtist;
import raspimediacenter.Data.Models.Music.MusicArtistSearchContainer;
import raspimediacenter.Data.Models.Music.MusicTrackContainer;
import raspimediacenter.Data.Models.Music.MusicTrackContainer.MusicTrack;
import raspimediacenter.Data.Models.Music.MusicTrackSearchContainer;
import raspimediacenter.Data.Models.TV.TVEpisodeList;
import raspimediacenter.Data.Models.TV.TVSeasonContainer;
import raspimediacenter.Data.Models.TV.TVSeasonContainer.TVSeason;
import raspimediacenter.Data.Models.TV.TVSeriesContainer;
import raspimediacenter.Data.Models.TV.TVSeriesContainer.TVSeries;

public class ParserUtils {

    private Movie movie;
    private MovieContainer movieContainer;
    private TVSeries series;
    private TVSeriesContainer seriesContainer;
    private TVSeasonContainer season;
    private MusicArtistSearchContainer artistSearchContainer;
    private MusicTrackSearchContainer trackSearchContainer;
    private MusicArtist artist;
    private MusicTrack track;
    private MusicAlbum album;
    private MusicAlbumContainer albumContainer;
    private MusicTrackContainer trackContainer;
    private MusicArtistContainer artistContainer;
    private MusicAlbumList albumList;

    //Parses in JSON for a particular Movie from /Movies/ or remote
    public Movie parseMovie(String filePath, boolean remote) {
        Gson gson = new Gson();
        movie = gson.fromJson(prepareJSON(filePath, remote), Movie.class);
        return movie;
    }

    //Parses in JSON for a remote Movie search results, or a local list in /Movies/ containing info about all TV Shows the user has
    public MovieContainer parseMovieList(String filePath, boolean remote) {
        Gson gson = new Gson();
        movieContainer = gson.fromJson(prepareJSON(filePath, remote), MovieContainer.class);
        if (!remote) {
            Collections.sort(movieContainer.results, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return movie1.getTitle().compareTo(movie2.getTitle());
                }
            });
        }
        return movieContainer;
    }

    //Parses in JSON for a particular TV Series from /TV Shows/ or remote
    public TVSeries parseSeries(String filePath, boolean remote) {
        Gson gson = new Gson();
        series = gson.fromJson(prepareJSON(filePath, remote), TVSeries.class);
        return series;
    }

    //Parses in JSON for either a remote TV Series search results, or a local list in /TV Shows/ containing info about all TV Shows the user has.
    public TVSeriesContainer parseSeriesList(String filePath, boolean remote) {
        Gson gson = new Gson();
        seriesContainer = gson.fromJson(prepareJSON(filePath, remote), TVSeriesContainer.class);
        if (!remote) {
            Collections.sort(seriesContainer.results, new Comparator<TVSeries>() {
                public int compare(TVSeries series1, TVSeries series2) {
                    return series1.getName().compareTo(series2.getName());
                }
            });
        }
        return seriesContainer;
    }

    public MusicTrackContainer parseTrackList(String filePath, boolean remote) {
        Gson gson = new Gson();
        trackContainer = gson.fromJson(prepareJSON(filePath, remote), MusicTrackContainer.class);
        if(!remote) {
        Collections.sort(trackContainer.tracks, new Comparator<MusicTrack>() {
            @Override
            public int compare(MusicTrack o1, MusicTrack o2) {
                return o1.track.getName().compareTo(o2.track.getName());
            }
        });
        }
        return trackContainer;
    }

    public MusicArtistContainer parseArtistList(String filePath, boolean remote) {
        Gson gson = new Gson();
        artistContainer = gson.fromJson(prepareJSON(filePath, remote), MusicArtistContainer.class);
        if (!remote) {
            Collections.sort(artistContainer.artists, new Comparator<MusicArtist>() {
                public int compare(MusicArtist artist1, MusicArtist artist2) {
                    return artist1.artist.getName().compareTo(artist2.artist.getName());
                }
            });
        }
        return artistContainer;
    }

    public TVEpisodeList parseEpisodeList(String filePath) {
        Gson gson = new Gson();
        TVEpisodeList episodeList = gson.fromJson(prepareJSON(filePath, false), TVEpisodeList.class);
        Collections.sort(episodeList.episodes, new Comparator<TVSeason>() {
            @Override
            public int compare(TVSeason o1, TVSeason o2) {
                return o1.getEpisodeNumber() - o2.getEpisodeNumber();
            }
        });
        return episodeList;
    }

    public MusicAlbumList parseAlbumList(String filePath) {
        Gson gson = new Gson();
        albumList = gson.fromJson(prepareJSON(filePath, false), MusicAlbumList.class);
        Collections.sort(albumList.albums, new Comparator<MusicAlbum>() {
            @Override
            public int compare(MusicAlbum o1, MusicAlbum o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return albumList;
    }

    //Parses in JSON for a particular season of a TV series from /TV Shows/showName/
    public TVSeasonContainer parseSeason(String filePath, boolean remote) {
        Gson gson = new Gson();
        season = gson.fromJson(prepareJSON(filePath, remote), TVSeasonContainer.class);
        return season;
    }

    //Trims the episode name down to an episode number, and then returns the episode object from the previously parsed season.
    public TVSeason parseEpisode(TVSeasonContainer season, String fileName) {
        int episodeNo = trimFileName("E", fileName);
        return season.episodes.get(episodeNo);
    }

    public MusicArtistSearchContainer parseArtistResults(String filePath, boolean remote) {
        Gson gson = new Gson();
        artistSearchContainer = gson.fromJson(prepareJSON(filePath, remote), MusicArtistSearchContainer.class);
        return artistSearchContainer;
    }

    public MusicArtist parseArtist(String filePath, boolean remote) {
        Gson gson = new Gson();
        artist = gson.fromJson(prepareJSON(filePath, remote), MusicArtist.class);
        return artist;
    }

    public MusicTrackSearchContainer parseTrackResults(String filePath, boolean remote) {
        Gson gson = new Gson();
        trackSearchContainer = gson.fromJson(prepareJSON(filePath, remote), MusicTrackSearchContainer.class);
        return trackSearchContainer;
    }

    public MusicTrack parseTrack(String filePath, boolean remote) {
        Gson gson = new Gson();
        track = gson.fromJson(prepareJSON(filePath, remote), MusicTrack.class);
        return track;
    }

    public MusicAlbum parseRemoteAlbum(String filePath) {
        Gson gson = new Gson();
        albumContainer = gson.fromJson(prepareJSON(filePath, true), MusicAlbumContainer.class);
        return albumContainer.album;
    }

    public MusicAlbum parseLocalAlbum(String filePath) {
        Gson gson = new Gson();
        album = gson.fromJson(prepareJSON(filePath, false), MusicAlbum.class);
        return album;
    }

    //Prepares JSON input by either extracting JSON from a URL (remote = true) or from a local path (remote = false)
    public String prepareJSON(String filePath, boolean remote) {
        String json = null;
        try {
            json = readUrl(filePath, remote);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ParserUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }

    //When a new movie is scraped, information about the movie is appended as a JSON object to /Movies/movie-list.json's input.
    public void appendToMovieList(Movie movie) {
        File file = new File("Movies/movie-list.json");
        MovieContainer container;
        boolean entryExists = false;
        if (!file.exists()) {
            beginJSONOutput(file, "{" + "results" + ":[{}]}");
            container = parseMovieList("Movies/movie-list.json", false);
            container.results.clear();
        } else {
            container = parseMovieList("Movies/movie-list.json", false);
        }
        for (int i = 0; i < container.results.size(); i++) {
            if (container.results.get(i).getID() == movie.getID()) {
                entryExists = true;
                break;
            }
        }
        if (!entryExists) {
            container.results.add(movie);
            saveAmendedMovieList(container);
        }
    }

    //TODO: Check if directory still exists for series-list
    public boolean directoryExists(String path) {
        File file = new File(path);
        if (file.isDirectory() && file.exists()) {
            return true;
        }
        return false;
    }

    //When a new series is scraped, information about the series is appended as a JSON object to /Tv Shows/series-list.json's input
    public void appendToSeriesList(TVSeries series) {
        File file = new File("TV Shows/series-list.json");
        TVSeriesContainer container;
        boolean entryExists = false;
        if (!file.exists()) {
            beginJSONOutput(file, "{" + "results" + ":[{}]}");
            container = parseSeriesList("TV Shows/series-list.json", false);
            container.results.remove(0);
        } else {
            container = parseSeriesList("TV Shows/series-list.json", false);
        }
        for (int i = 0; i < container.results.size(); i++) {
            if (container.results.get(i).getID() == series.getID()) {
                entryExists = true;
                break;
            }
        }
        if (!entryExists) {
            container.results.add(series);
            saveAmendedSeriesList(container);
        }
    }

    public void appendToTrackList(MusicTrack track, String name, String albumName) {
        File file = new File("Music/" + name + "/" + albumName + "/track-list.json");
        MusicTrackContainer container;
        boolean entryExists = false;
        if (!file.exists()) {
            beginJSONOutput(file, "{" + "tracks" + ":[{}]}");
            container = parseTrackList("Music/" + name + "/" + albumName + "/track-list.json", false);
            container.tracks.remove(0);
        } else {
            container = parseTrackList("Music/" + name + "/" + albumName + "/track-list.json", false);
        }
        for (int i = 0; i < container.tracks.size(); i++) {
            if (container.tracks.get(i).track.getMBID().equalsIgnoreCase(track.track.getMBID())) {
                entryExists = true;
                break;
            }
        }
        if (!entryExists) {
            container.tracks.add(track);
            saveAmendedTrackList(container, name, albumName);
        }
    }

    public void appendToArtistList(MusicArtist artist) {
        File file = new File("Music/artist-list.json");
        MusicArtistContainer container;
        boolean entryExists = false;
        if (!file.exists()) {
            beginJSONOutput(file, "{" + "artists" + ":[{}]}");
            container = parseArtistList("Music/artist-list.json", false);
            container.artists.remove(0);
        } else {
            container = parseArtistList("Music/artist-list.json", false);
        }
        for (int i = 0; i < container.artists.size(); i++) {
            if (container.artists.get(i).artist.getMBID().equalsIgnoreCase(artist.artist.getMBID())) {
                entryExists = true;
                break;
            }
        }
        if (!entryExists) {
            container.artists.add(artist);
            saveAmendedArtistList(container);
        }
    }

    public void appendToAlbumList(MusicAlbum album) {
        File file = new File("Music/" + album.getArtist() + "/album-list.json");
        MusicAlbumList list;
        boolean entryExists = false;
        if (!file.exists()) {
            beginJSONOutput(file, "{" + "albums" + ":[{}]}");
            list = parseAlbumList("Music/" + album.getArtist() + "/album-list.json");
            list.albums.remove(0);
        } else {
            list = parseAlbumList("Music/" + album.getArtist() + "/album-list.json");
        }
        for (int i = 0; i < list.albums.size(); i++) {
            if (list.albums.get(i).getMBID().equalsIgnoreCase(album.getMBID())) {
                entryExists = true;
                break;
            }
        }
        if (!entryExists) {
            list.albums.add(album);
            saveAmendedAlbumList(list, album.getArtist());
        }
    }

    public void appendToEpisodeList(TVSeason season, String show) {
        File file = new File("TV Shows/" + show + "/Season " + season.getSeasonNumber() + "/episode-list.json");
        TVEpisodeList list;
        boolean entryExists = false;
        if (!file.exists()) {
            beginJSONOutput(file, "{episodes:[{}]}");
            list = parseEpisodeList("TV Shows/" + show + "/Season " + season.getSeasonNumber() + "/episode-list.json");
            list.episodes.remove(0);
        } else {
            list = parseEpisodeList("TV Shows/" + show + "/Season " + season.getSeasonNumber() + "/episode-list.json");
        }
        for (int i = 0; i < list.episodes.size(); i++) {
            if (list.episodes.get(i).getID() == season.getID()) {
                entryExists = true;
                break;
            }
        }
        if (!entryExists) {
            list.episodes.add(season);
            saveAmendedEpisodeList(list, show, season.getSeasonNumber());
        }
    }

    public void cleanupSeriesList() {
        File file = new File("TV Shows/series-list.json");
        if (file.exists()) {
            TVSeriesContainer oldList, newList;
            oldList = parseSeriesList("TV Shows/series-list.json", false);
            newList = oldList;
            for (int i = 0; i < oldList.results.size(); i++) {
                if (!directoryExists(oldList.results.get(i).getName())) {
                    newList.results.remove(i);
                }
            }
            saveAmendedSeriesList(newList);
        }
    }

    public void cleanupMovieList() {
        File file = new File("Movies/movie-list.json");
        if (file.exists()) {
            MovieContainer oldList, newList;
            oldList = parseMovieList("Movies/movie-list.json", false);
            newList = oldList;
            for (int i = 0; i < oldList.results.size(); i++) {
                if (!directoryExists(oldList.results.get(i).getTitle())) {
                    newList.results.remove(i);
                }
            }
            saveAmendedMovieList(newList);
        }
    }

    public void cleanupArtistList() {
        File file = new File("Music/artist-list.json");
        if (file.exists()) {
            MusicArtistContainer oldList, newList;
            oldList = parseArtistList("Music/artist-list.json", false);
            newList = oldList;
            for (int i = 0; i < oldList.artists.size(); i++) {
                if (!directoryExists(oldList.artists.get(i).artist.getName())) {
                    newList.artists.remove(i);
                }
            }
            saveAmendedArtistList(newList);
        }
    }

    public void cleanupAlbumList(String artist) {
        File file = new File("Music/"+ artist + "album-list.json");
        if (file.exists()) {
            MusicAlbumList oldList, newList;
            oldList = parseAlbumList("Music/" + artist + "artist-list.json");
            newList = oldList;
            for (int i = 0; i < oldList.albums.size(); i++) {
                if (!directoryExists(oldList.albums.get(i).getName())) {
                    newList.albums.remove(i);
                }
            }
            saveAmendedAlbumList(newList, artist);
        }
    }

    //Called after appendToSeriesList, outputs modified JSON to /TV Shows/series-list.json to save changes
    public void saveAmendedSeriesList(TVSeriesContainer container) {
        File infoFile = new File("TV Shows/series-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(container);
        beginJSONOutput(infoFile, s);
    }

    //Called after appendtoMovieList, outputs modified JSON to /Movies/movie-list.json to save changes
    public void saveAmendedMovieList(MovieContainer container) {
        File infoFile = new File("Movies/movie-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(container);
        beginJSONOutput(infoFile, s);
    }

    public void saveAmendedTrackList(MusicTrackContainer container, String name, String albumName) {
        File infoFile = new File("Music/" + name + "/" + albumName + "/track-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(container);
        beginJSONOutput(infoFile, s);
    }

    public void saveAmendedArtistList(MusicArtistContainer container) {
        File infoFile = new File("Music/artist-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(container);
        beginJSONOutput(infoFile, s);
    }

    public void saveAmendedAlbumList(MusicAlbumList list, String name) {
        File infoFile = new File("Music/" + name + "/album-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(list);
        beginJSONOutput(infoFile, s);
    }

    public void saveAmendedEpisodeList(TVEpisodeList list, String name, int seasonNo) {
        File infoFile = new File("TV Shows/" + name + "/Season " + seasonNo + "/episode-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(list);
        beginJSONOutput(infoFile, s);
    }

    //Writes output to a specified JSON file
    public void beginJSONOutput(File outputFile, String output) {
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter outputStream = new OutputStreamWriter(fos);
            outputStream.write(output);
            outputStream.close();
            fos.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtils.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtils.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Using regex, trims integers following 'E' out of episode filename, for example: "Some.Show.S01E01" becomes 01 and 1 after integer is parsed
    public int trimFileName(String pattern, String fileName) {
        Matcher matcher = Pattern.compile(pattern + "(\\d+)").matcher(fileName);
        matcher.find();
        int episodeNo = Integer.valueOf(matcher.group().substring(+pattern.length()));
        return episodeNo;
    }

    //Prepares search term to be appended to TMDB URI. Removes illegal characters to prevent an exception
    public String encodeURLParameter(String parameter) {
        String encodedName = null;
        try {
            encodedName = URLEncoder.encode(parameter, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ScraperUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encodedName;
    }

    //Reads in input from the specified URL (remote = true) or path (remote = false) and returns the read input
    private static String readUrl(String urlPath, boolean remote) throws Exception {
        BufferedReader reader = null;
        try {
            if (remote) {
                URL url = new URL(urlPath);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
            } else {
                reader = new BufferedReader(new FileReader(urlPath));
            }
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }
            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}
