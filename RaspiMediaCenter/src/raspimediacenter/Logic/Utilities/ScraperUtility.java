package raspimediacenter.Logic.Utilities;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;

public class ScraperUtility {

    private String apiKey = System.getenv("API_KEY");
    private String baseURI = "http://api.themoviedb.org/3/";
    private TVSeriesContainer tvSeries;
    private TVSeasonContainer tvSeason;

    public void beginTVScrape() {
        File[] files = getTVDirectory("TV Shows");
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            String encodedName = null;
            try {
                encodedName = URLEncoder.encode(name, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
            }
            String jsonURI = constructSearchURI("tv", encodedName, "");
            try {
                parseShowSearchResults(jsonURI);
            } catch (Exception ex) {
                Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
            }
            saveLocalSeriesJSON(tvSeries.results.get(0), "TV Shows/" + files[i].getName() + "/");
            File[] subDirFiles = getTVDirectory("TV Shows/" + name);
            for (int j = 0; j < subDirFiles.length; j++) {
                if (subDirFiles[j].getName().toLowerCase().contains("season")) {
                    Matcher matcher = Pattern.compile("\\d+").matcher(subDirFiles[j].getName());
                    matcher.find();
                    int seasonNo = Integer.valueOf(matcher.group());
                    jsonURI = constructSeasonURI(tvSeries.results.get(0).getID(), seasonNo);
                    parseSeasonSearchResults(jsonURI);
                    saveLocalSeasonJSON(tvSeason, "TV Shows/" + name + "/" + subDirFiles[j].getName() + "/");
                }
            }
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

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
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

    public void saveLocalSeasonJSON(TVSeasonContainer season, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(season);
        try {
            FileOutputStream output = new FileOutputStream(infoFile);
            OutputStreamWriter outputStream = new OutputStreamWriter(output);
            outputStream.write(s);
            outputStream.close();
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveLocalSeriesJSON(TVSeries series, String filePath) {
        File infoFile = new File(filePath + "info.json");
        Gson gson = new Gson();
        String s = gson.toJson(series);
        try {
            FileOutputStream output = new FileOutputStream(infoFile);
            OutputStreamWriter outputStream = new OutputStreamWriter(output);
            outputStream.write(s);
            outputStream.close();
            output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parseSeasonSearchResults(String uri) {
        try {
            Gson gson = new Gson();
            String json = readUrl(uri);
            tvSeason = gson.fromJson(json, TVSeasonContainer.class);
        } catch (Exception ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parseShowSearchResults(String uri) {
        try {
            Gson gson = new Gson();
            String json = readUrl(uri);
            tvSeries = gson.fromJson(json, TVSeriesContainer.class);
        } catch (Exception ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

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
