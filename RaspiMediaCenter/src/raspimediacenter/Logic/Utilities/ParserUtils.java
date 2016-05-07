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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.Data.Models.MovieContainer.Movie;
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeasonContainer.TVSeason;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;

public class ParserUtils {

    private Movie movie;
    private MovieContainer movieContainer;
    private TVSeries series;
    private TVSeriesContainer seriesContainer;
    private TVSeasonContainer season;

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
        return seriesContainer;
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
    
    public void cleanupSeriesList() {
        File file = new File("TV Shows/series-list.json");
        if(file.exists()) {
            TVSeriesContainer oldList, newList;
            oldList = parseSeriesList("TV Shows/series-list.json", false);
            newList = oldList;
            for(int i = 0; i < oldList.results.size(); i++) {
                if(!directoryExists(oldList.results.get(i).getName())) {
                    newList.results.remove(i);
                }
            }
            saveAmendedSeriesList(newList);
        }
    }
    
    public void cleanupMovieList() {
        File file = new File("Movies/movie-list.json");
        if(file.exists()) {
            MovieContainer oldList, newList;
            oldList = parseMovieList("Movies/movie-list.json", false);
            newList = oldList;
            for(int i = 0; i < oldList.results.size(); i++) {
                if(!directoryExists(oldList.results.get(i).getTitle())) {
                    newList.results.remove(i);
                }
            }
            saveAmendedMovieList(newList);
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