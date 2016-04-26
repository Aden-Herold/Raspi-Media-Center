package raspimediacenter.Logic.Utilities;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import raspimediacenter.Data.Models.TVSeasonContainer;
import raspimediacenter.Data.Models.TVSeasonContainer.TVSeason;
import raspimediacenter.Data.Models.TVSeriesContainer.TVSeries;

public class LocalParserUtility {

    private TVSeries series;
    private TVSeasonContainer season;

    public TVSeries parseSeries(String filePath) {
        try {
            Gson gson = new Gson();
            String json = readUrl(filePath);
            series = gson.fromJson(json, TVSeries.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LocalParserUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return series;
    }

    public TVSeasonContainer parseSeason(String filePath) {
        try {
            Gson gson = new Gson();
            String json = readUrl(filePath);
            season = gson.fromJson(json, TVSeasonContainer.class);
            System.out.println(season.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScraperUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LocalParserUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return season;
    }

    public TVSeason parseEpisode(TVSeasonContainer season, String fileName) {
        int episodeNo = trimFileName(fileName);
        return season.episodes.get(episodeNo);
    }

    public int trimFileName(String fileName) {
        Matcher matcher = Pattern.compile("E(\\d+)").matcher(fileName);
        matcher.find();

        int episodeNo = Integer.valueOf(matcher.group().substring(+1));
        return episodeNo;
    }

    private static String readUrl(String filePath) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
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
