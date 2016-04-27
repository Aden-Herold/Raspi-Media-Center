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
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeasonContainer.TVSeason;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;

public class ParserUtility {

    private TVSeries series;
    private TVSeriesContainer seriesContainer;
    private TVSeasonContainer season;

    public TVSeries parseSeries(String filePath, boolean remote) {
        Gson gson = new Gson();
        series = gson.fromJson(prepareJSON(filePath, remote), TVSeries.class);
        return series;
    }

    public TVSeriesContainer parseSeriesList(String filePath, boolean remote) {
        Gson gson = new Gson();
        seriesContainer = gson.fromJson(prepareJSON(filePath, remote), TVSeriesContainer.class);
        return seriesContainer;
    }

    public TVSeasonContainer parseSeason(String filePath, boolean remote) {
        Gson gson = new Gson();
        season = gson.fromJson(prepareJSON(filePath, remote), TVSeasonContainer.class);
        return season;
    }

    public TVSeason parseEpisode(TVSeasonContainer season, String fileName) {
        int episodeNo = trimFileName("E", fileName);
        return season.episodes.get(episodeNo);
    }

    public String prepareJSON(String filePath, boolean remote) {
        String json = null;
        try {
            json = readUrl(filePath, remote);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ParserUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }

    public void appendToSeriesList(TVSeries series) {
        File file = new File("TV Shows/series-list.json");
        TVSeriesContainer container;
        if(file.exists()) {
            container = parseSeriesList("TV Shows/series-list.json", false);
        } else {
            beginJSONOutput(file, "{" + "results" + ":[{}]}");
            container = parseSeriesList("TV Shows/series-list.json", false);
        }
        container.results.add(series);
        saveAmendedSeriesList(container);
    }

    public void saveAmendedSeriesList(TVSeriesContainer container) {
        File infoFile = new File("TV Shows/series-list.json");
        Gson gson = new Gson();
        String s = gson.toJson(container);
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

    public int trimFileName(String pattern, String fileName) {
        Matcher matcher = Pattern.compile(pattern + "(\\d+)").matcher(fileName);
        matcher.find();
        int episodeNo = Integer.valueOf(matcher.group().substring(+pattern.length()));
        return episodeNo;
    }

    public String encodeURLParameter(String parameter) {
        String encodedName = null;
        try {
            encodedName = URLEncoder.encode(parameter, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encodedName;
    }

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
