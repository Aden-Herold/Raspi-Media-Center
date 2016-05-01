package raspimediacenter.GUI.Components.MenuComponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.Data.Models.TVSeriesContainer;
import raspimediacenter.GUI.Components.StyledLabel;
import raspimediacenter.GUI.Components.TopCornerInfoPanel;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ParserUtility;

public final class MenuPanel {
 
    private TopCornerInfoPanel focusOptionInfo;
    private TopCornerInfoPanel timeDateInfo;
    
    private final TVSeriesContainer tvSeries;
    private final MovieContainer movies;
    
    //Date and Time Formats
    private DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    private DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
    
    //Label Info
    private int totalShows;
    private int totalSeasons;
    private int totalEpisodes;
    
    private int totalMovies;
    private int totalWatched;
    
    Timer clockUpdateTimer = new Timer(1000, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            timeDateInfo.setHeaderInfo(timeFormat.format(new Date()).toUpperCase());
            timeDateInfo.setContentInfo(dateFormat.format(new Date()).toUpperCase());
        }
    });
    
    public MenuPanel () {
    
        ParserUtility parser = new ParserUtility();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        
        totalShows = tvSeries.results.size();
        totalSeasons = tvSeries.getTotalSeasons();
        totalEpisodes = tvSeries.getTotalEpisodes();
        
        totalMovies = movies.results.size();
        totalWatched = 0;
        
        setupMainMenu();
    }

    public void setupMainMenu ()
    {
        focusOptionInfo = new TopCornerInfoPanel("left");
        timeDateInfo = new TopCornerInfoPanel("right");
        
        createCategoryInfo();
        createTimeInfo();
        clockUpdateTimer.start();
    }
    
    private void createCategoryInfo () {
        
        String header = "LIBRARY - MOVIES";
        String content = "MOVIES: "+ totalMovies +"  WATCHED: "+ totalWatched;
        
        JPanel panel = focusOptionInfo.createCategoryInfo(header, content);
        SceneManager.getContentPane().add(panel, 3, 0);
    }
    
    private void createTimeInfo () {
        
        String header = timeFormat.format(new Date()).toUpperCase();
        String content = dateFormat.format(new Date()).toUpperCase();
        
        JPanel panel = timeDateInfo.createCategoryInfo(header, content);
        
        SceneManager.getContentPane().add(panel, 3, 0);
    }
    
    public void updateInfoLabel (String buttonName) {
        
        focusOptionInfo.setHeaderInfo("LIBRARY - " + buttonName);
        
        if (buttonName.matches("MOVIES"))
        {
            focusOptionInfo.setContentInfo("MOVIES: "+ totalMovies +"  WATCHED: "+ totalWatched);
        }
        else if (buttonName.matches("TV SHOWS"))
        {
            focusOptionInfo.setContentInfo("SHOWS: " + totalShows + "  SEASONS: " + totalSeasons +"  EPISODES: " + totalEpisodes);
        }
        else if (buttonName.matches("MUSIC"))
        {
            focusOptionInfo.setContentInfo("SONGS: 521");
        } 
        else if (buttonName.matches("IMAGES"))
        {
            focusOptionInfo.setContentInfo("IMAGES: 125");
        } 
    }
}
