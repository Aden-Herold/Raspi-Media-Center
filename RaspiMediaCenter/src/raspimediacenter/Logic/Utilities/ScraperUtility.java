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
        BufferedImage backdropImage = null, posterImage = null, stillImage = null;
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            String jsonURI = constructSearchURI("tv", parser.encodeURLParameter(name), "");
            try {
                tvSeries = parser.parseSeriesList(jsonURI, true);
                parser.appendToSeriesList(tvSeries.results.get(0));
                TVSeries series = tvSeries.results.get(0);
                name = renameDir(name, series.getName());
                requestImageScrape(BACKDROP_SIZE, backdropImage, "series_backdrop.jpg", series.getBackdropPath(), "TV Shows/" + name + "/");
                requestImageScrape(POSTER_SIZE, posterImage, "series_poster.jpg", series.getPosterPath(), "TV Shows/" + name + "/");
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
                    requestImageScrape(POSTER_SIZE, posterImage, "season_poster.jpg", tvSeason.getPosterPath(), "TV Shows/" + name + "/" + subDirFiles[j].getName() + "/");
                    File stillsDir = new File("TV Shows/" + name + "/" + subDirFiles[j].getName() + "/Stills/");
                    if (!stillsDir.exists()) {
                        stillsDir.mkdir();
                    }
                    for (int k = 0; k < tvSeason.episodes.size(); k++) {
                        requestImageScrape(BACKDROP_SIZE, backdropImage, "EP" + (k + 1) + "_still.jpg", tvSeason.episodes.get(k).getStillPath(), 
                                "TV Shows/" + name + "/" + subDirFiles[j].getName() + "/Stills/");
                    }
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

    public void requestImageScrape(String size, BufferedImage image, String name, String imageURL, String path) {
        File file = new File(path + "/" + name);
        if (!file.exists() && imageURL != null) {
            System.out.println("Downloading " + name + " to " + path + ".");
            image = scrapeImage(size, imageURL);
            saveImage(image, name, path);
        }
    }
    
    public String renameDir(String oldName, String newName) {
        File dir = new File("TV Shows/" + oldName);
        File newDir = new File("TV Shows/" + newName);
        if(dir.isDirectory()) {
            dir.renameTo(newDir);
        }
        return newName;
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

