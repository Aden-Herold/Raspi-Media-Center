package raspimediacenter.Logic.Utilities;

import com.google.gson.Gson;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;

public class ScraperUtility {
    
    public static final String BACKDROP_SIZE = "w1280";
    public static final String POSTER_SIZE = "w780";
    public static final String STILL_SIZE = "w300";

    private String apiKey = System.getenv("API_KEY");
    private String baseURI = "http://api.themoviedb.org/3/";
    private String baseImageURL = "http://image.tmdb.org/t/p/";
    private TVSeriesContainer tvSeries;
    private TVSeasonContainer tvSeason;
    private ParserUtility parser = new ParserUtility();

    public void beginTVScrape() {
        File[] files = getTVDirectory("TV Shows");
        BufferedImage backdropImage, posterImage, stillImage = null;
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            String jsonURI = constructSearchURI("tv", parser.encodeURLParameter(name), "");
            try {
                tvSeries = parser.parseSeriesList(jsonURI, true);
                parser.appendToSeriesList(tvSeries.results.get(0));
                backdropImage = scrapeImage(BACKDROP_SIZE, tvSeries.results.get(0).getBackdropPath());
                posterImage = scrapeImage(POSTER_SIZE, tvSeries.results.get(0).getPosterPath());
                saveImage(backdropImage, "series_backdrop.jpg", "TV Shows/" + name + "/");
                saveImage(posterImage, "series_poster.jpg", "TV Shows/" + name + "/");
            } catch (Exception ex) {
                Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
            }
            saveLocalSeriesJSON(tvSeries.results.get(0), "TV Shows/" + name + "/");
            File[] subDirFiles = getTVDirectory("TV Shows/" + name);
            for (int j = 0; j < subDirFiles.length; j++) {
                if (subDirFiles[j].getName().toLowerCase().contains("season")) {
                    int seasonNo = parser.trimFileName("season ", subDirFiles[j].getName().toLowerCase());
                    jsonURI = constructSeasonURI(tvSeries.results.get(0).getID(), seasonNo);
                    tvSeason = parser.parseSeason(jsonURI, true);
                    saveLocalSeasonJSON(tvSeason, "TV Shows/" + name + "/" + subDirFiles[j].getName() + "/");
                }
            }
        }
    }

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

    public void saveImage(BufferedImage image, String imageName, String destination) {
        File file = new File(destination + imageName);
        try {
            ImageIO.write(image, "jpg", file);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveLocalSeasonJSON(TVSeasonContainer season, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(season);
        beginJSONOutput(infoFile, s);
    }

    public void saveLocalSeriesJSON(TVSeries series, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(series);
        beginJSONOutput(infoFile, s);
    }

    public void beginJSONOutput(File outputFile, String output) {
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            OutputStreamWriter outputStream = new OutputStreamWriter(fos);
            outputStream.write(output);
            outputStream.close();
            fos.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtility.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtility.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public File[] getTVDirectory(String subDir) {
        File file = new File(subDir);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return files;
    }

    public String constructSearchURI(String type, String query, String year) {
        return baseURI + "search/" + type + "?query=" + query + "&year=" + year
                + "&api_key=" + apiKey;
    }

    public String constructMovieURI(int id) {
        return baseURI + "movie/" + id + "?api_key=" + apiKey;
    }

    public String constructSeasonURI(int ID, int season) {
        return baseURI + "tv/" + ID + "/season/" + season + "?api_key=" + apiKey;
    }
}