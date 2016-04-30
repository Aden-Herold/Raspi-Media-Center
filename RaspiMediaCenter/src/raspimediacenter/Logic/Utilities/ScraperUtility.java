package raspimediacenter.Logic.Utilities;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.Data.Models.MovieContainer.Movie;
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;

public class ScraperUtility {

    public static final String BACKDROP_SIZE = "w1920";
    public static final String POSTER_SIZE = "w780";
    public static final String STILL_SIZE = "w300";

    private String apiKey = System.getenv("API_KEY");
    private String baseURI = "http://api.themoviedb.org/3/";
    private String baseImageURL = "http://image.tmdb.org/t/p/";
    BufferedImage backdropImage = null, posterImage = null, stillImage = null;
    private Movie movie;
    private MovieContainer movieContainer;
    private TVSeries series;
    private TVSeriesContainer tvSeries;
    private TVSeasonContainer tvSeason;
    private ParserUtility parser = new ParserUtility();

    public class ScraperThread implements Runnable {

        public void run() {
            parser.cleanupSeriesList();
            parser.cleanupMovieList();
            startTVScrape();
            startMovieScrape();
            scrapeTVImages();
            scrapeMovieImages();
        }
    }

    //Iterates through series directories and seasons directories within /TV Shows/, constructs a URI with a found series directory as a search term
    //and scrapes information about that series, as well as all of its seasons as JSON from The Movie Database. Images related to the series/seasons are also scraped
    //and saved in appropriate subdirectories.
    public void startTVScrape() {
        File[] files = getDirectories("TV Shows");
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                String jsonURI = constructSearchURI("tv", parser.encodeURLParameter(name), "");
                series = scraperParseSeries(jsonURI, series);
                name = renameDir("TV Shows/", name, series.getName());
                saveLocalSeriesJSON(series, "TV Shows/" + name + "/");
                File[] subDirFiles = getDirectories("TV Shows/" + name);
                for (int j = 0; j < subDirFiles.length; j++) {
                    String subDirName = subDirFiles[j].getName();
                    if (subDirName.toLowerCase().contains("season")) {
                        tvSeason = scraperParseSeason(jsonURI, series, subDirName);
                        saveLocalSeasonJSON(tvSeason, "TV Shows/" + name + "/" + subDirName + "/");
                    }
                }
            }
        }
    }

    //Iterates through movie directories within /Movies/, constructs a URI with a found movie directory as a search term
    //and scrapes information about that movie from The Movie Database. Images related to the movie are also scraped
    //and saved in the movie's directory.
    public void startMovieScrape() {
        File[] files = getDirectories("Movies");
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                String jsonURI = constructSearchURI("movie", parser.encodeURLParameter(name), "");
                movie = scraperParseMovie(jsonURI, movie);
                name = renameDir("Movies/", name, movie.getTitle());
                saveLocalMovieJSON(movie, "Movies/" + name + "/");
            }
        }
    }

    public void scrapeTVImages() {
        File[] files = getDirectories("TV Shows");
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String path = "TV Shows/series-list.json";
                tvSeries = parser.parseSeriesList(path, false);
                String name = tvSeries.results.get(i).getName();
                requestImageScrape(BACKDROP_SIZE, backdropImage, "series_backdrop.jpg", tvSeries.results.get(i).getBackdropPath(), "TV Shows/" + name + "/");
                requestImageScrape(POSTER_SIZE, posterImage, "series_poster.jpg", tvSeries.results.get(i).getPosterPath(), "TV Shows/" + name + "/");
                File[] subDirFiles = getDirectories("TV Shows/" + name);
                for (int j = 0; j < subDirFiles.length; j++) {
                    String subDirName = subDirFiles[j].getName();
                    requestImageScrape(POSTER_SIZE, posterImage, "season_poster.jpg", tvSeason.getPosterPath(), "TV Shows/" + name + "/" + subDirName + "/");
                    makeDirectory("TV Shows/" + name + "/" + subDirName + "/Stills/");
                    for (int k = 0; k < tvSeason.episodes.size(); k++) {
                        requestImageScrape(BACKDROP_SIZE, backdropImage, "EP" + (k + 1) + "_still.jpg", tvSeason.episodes.get(k).getStillPath(),
                                "TV Shows/" + name + "/" + subDirName + "/Stills/");
                    }
                }
            }
        }
    }

    public void scrapeMovieImages() {
        File[] files = getDirectories("Movies");
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String path = "Movies/movie-list.json";
                movieContainer = parser.parseMovieList(path, false);
                String name = movieContainer.results.get(i).getTitle();
                requestImageScrape(BACKDROP_SIZE, backdropImage, "movie_backdrop.jpg", movie.getBackdropPath(), "Movies/" + name + "/");
                requestImageScrape(POSTER_SIZE, posterImage, "movie_poster.jpg", movie.getPosterPath(), "Movies/" + name + "/");
            }
        }
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

    public TVSeasonContainer scraperParseSeason(String jsonURI, TVSeries series, String subDirName) {
        int seasonNo = parser.trimFileName("season ", subDirName.toLowerCase());
        jsonURI = constructSeasonURI(series.getID(), seasonNo);
        tvSeason = parser.parseSeason(jsonURI, true);
        return tvSeason;
    }

    //Prepares to scrape an image by first verifying if the image already exists in the chosen directory, and then
    //reading + saving the image. Checks that the imageURL given is not null, for cases when a still image path isn't provided by The Movie Database. 
    public void requestImageScrape(String size, BufferedImage image, String name, String imageURL, String path) {
        File file = new File(path + "/" + name);
        if (!file.exists() && imageURL != null) {
            System.out.println("Downloading " + name + " to " + path + ".");
            image = scrapeImage(size, imageURL);
            saveImage(image, name, path);
        }
    }

    //Reads in an image from http://image.tmdb.org/t/p/ using the path to either the backdrop, poster or still image belonging
    //to the series/season/episode.
    public BufferedImage scrapeImage(String imageSize, String imageURL) {
        URL url = null;
        BufferedImage image = null;
        try {
            url = new URL(baseImageURL + imageSize + "/" + imageURL);
            image = ImageIO.read(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    //Saves newly read image into the specified directory
    public void saveImage(BufferedImage image, String imageName, String destination) {
        File file = new File(destination + imageName);
        try {
            ImageIO.write(image, "jpg", file);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
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

    //Gets all subdirectories inside of the specified parent directory. Ignores files which
    //are not directories.
    public File[] getDirectories(String subDir) {
        File file = new File(subDir);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return files;
    }

    //constructs a URI for a search term for use with the 'The Movie Database API'.
    public String constructSearchURI(String type, String query, String year) {
        return baseURI + "search/" + type + "?query=" + query + "&year=" + year
                + "&api_key=" + apiKey;
    }

    //constructs a URI to return a movie using the 'The Movie Database API'.
    public String constructMovieURI(int id) {
        return baseURI + "movie/" + id + "?api_key=" + apiKey + "&append_to_response=release_dates";
    }

    //constructs a URI to return a series using the 'The Movie Database API'.
    public String constructSeriesURI(int id) {
        return baseURI + "tv/" + id + "?api_key=" + apiKey + "&append_to_response=release_dates";
    }

    //constructs a URI to return a season using the 'The Movie Database API'.
    public String constructSeasonURI(int id, int season) {
        return baseURI + "tv/" + id + "/season/" + season + "?api_key=" + apiKey;
    }
}
