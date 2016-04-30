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
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ParserUtility;

public final class MenuPanel {
 
    private JLabel focusOptionInfo;
    private JLabel focusOptionItems;
    private JLabel timeLabel;
    private JLabel dateLabel;
    
    private final TVSeriesContainer tvSeries;
    private final MovieContainer movies;
    
    Timer clockUpdateTimer = new Timer(1000, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            timeLabel.setText(timeFormat.format(new Date()));
            
            DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
            dateLabel.setText(dateFormat.format(new Date()).toUpperCase());
        }
    });
    
    public MenuPanel () {
    
        ParserUtility parser = new ParserUtility();
        tvSeries = parser.parseSeriesList("TV Shows/series-list.json", false);
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        
        setupMainMenu();
    }

    public void setupMainMenu ()
    {
        createCategoryInfo();
        createTimeInfo();
        clockUpdateTimer.start();
    }
    
    private void createCategoryInfo () {
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        focusOptionInfo = new StyledLabel("LIBRARY - MOVIES", Font.BOLD, 25, SwingConstants.LEFT);
        focusOptionItems = new StyledLabel("MOVIES: 10 UNWATCHED: 3", Font.BOLD, 15, SwingConstants.LEFT);
        focusOptionInfo.setPreferredSize(new Dimension(400, 25));
        focusOptionItems.setPreferredSize(new Dimension(400, 25));
        
        panel.add(focusOptionInfo);
        panel.add(focusOptionItems);
        
        //focusOptionInfo.setBounds(20, 20, 400, 25);
        panel.setBounds(20, 15, 400, 55);
        SceneManager.getContentPane().add(panel, 3, 0);
    }
    
    private void createTimeInfo () {
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setOpaque(false);
        
        timeLabel = new StyledLabel("00:00 XX", Font.BOLD, 25, SwingConstants.RIGHT);
        dateLabel = new StyledLabel("XXX, XXX 00, 0000", Font.BOLD, 15, SwingConstants.RIGHT);
        timeLabel.setPreferredSize(new Dimension(400, 25));
        dateLabel.setPreferredSize(new Dimension(400, 25));
        
        panel.add(timeLabel);
        panel.add(dateLabel);
        
        //timeLabel.setBounds(SceneManager.getScreenWidth()-220, 20, 200, 25);
        panel.setBounds(SceneManager.getScreenWidth()-420, 15, 400, 55);
        SceneManager.getContentPane().add(panel, 3, 0);
    }
    
    public void updateInfoLabel (String buttonName) {
        
        focusOptionInfo.setText("LIBRARY - " + buttonName);
        
        if (buttonName.matches("MOVIES"))
        {
            focusOptionItems.setText("MOVIES: 192   WATCHED: 82");
        }
        else if (buttonName.matches("TV SHOWS"))
        {
            int files = tvSeries.results.size();
            int eps = tvSeries.getTotalEpisodes();
            focusOptionItems.setText("TV SHOWS: " + files + "   EPISODES: " + eps);
        }
        else if (buttonName.matches("MUSIC"))
        {
            focusOptionItems.setText("SONGS: 521");
        } 
        else if (buttonName.matches("IMAGES"))
        {
            focusOptionItems.setText("IMAGES: 125");
        } 
    }
}
