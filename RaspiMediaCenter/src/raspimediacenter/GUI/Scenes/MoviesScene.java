package raspimediacenter.GUI.Scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.GUI.Components.ListItemButton;
import raspimediacenter.GUI.SceneManager;
import raspimediacenter.Logic.Utilities.ImageUtilities;
import raspimediacenter.Logic.Utilities.ParserUtility;

public class MoviesScene extends FileBrowserScene {

    private static MovieContainer movies;
    private ArrayList<JButton> seriesLinks = new ArrayList<>();
    private final ArrayList<String> infoLabels = new ArrayList<>(Arrays.asList("Year:", "Rating:", "Runtime:", "Languages:", "Genres:"));
    private static ArrayList<BufferedImage> posters = new ArrayList<>();
    
    public MoviesScene(SceneManager sceneManager) {
        super(sceneManager);
        Scene.setCurrentScene("Movies");
        
        ParserUtility parser = new ParserUtility();
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        
        loadMovieBackgrounds();
        loadMoviePosters();
        bgCanvas.setBackgroundImage(0);

        setupInfoLabels(infoLabels);
        createInfoDisplay(generateMoviesInfo(1));
        createOverviewDisplay(movies.results.get(0).getOverview());
        createLinkList();
        createListDisplay(seriesLinks);
        createStarRating(movies.results.get(0).getVoteAverage());
    }
    
    private void createLinkList()
    {
        for (int x = 0; x < movies.results.size(); x++)
        {
            if (!movies.results.get(x).getTitle().matches("")){
                JButton button = new ListItemButton(movies.results.get(x).getTitle(), this, x);
                seriesLinks.add(button);
            }
        }
    }
    
    private void loadMovieBackgrounds ()
    {
        ArrayList<String> backgroundPaths = new ArrayList<>();
        
        for (int x = 0; x < movies.results.size(); x++)
        {
            if (!movies.results.get(x).getTitle().matches(""))
            {
                String path = "Movies/" + movies.results.get(x).getTitle() + "/movie_backdrop.jpg";
                backgroundPaths.add(path);
            }
        }
        
        bgCanvas.loadFanartImagesIntoMemory(backgroundPaths);
    }
    
    private void loadMoviePosters ()
    {
        for (int x = 0; x < movies.results.size(); x++)
        {
            if (!movies.results.get(x).getTitle().matches(""))
            {
                String path = "Movies/" + movies.results.get(x).getTitle() + "/movie_poster.jpg";
                BufferedImage poster = ImageUtilities.getImageFromPath(path);
                posters.add(poster);
            }
        }
    }
    
    public static MovieContainer getMovies ()
    {
        return movies;
    }
    
    public static ArrayList<String> generateMoviesInfo (int linkNum)
    {
        String genres = "";
        String temp = "";
        
        for (int x = 0; x < movies.results.get(linkNum).genres.size(); x++)
        {
            temp = movies.results.get(linkNum).genres.get(x).getGenre();
            
            if (temp.matches("Science Fiction"))
            {
                temp = "Sci-Fi";
            }
            
            if (x < movies.results.get(linkNum).genres.size()-1)
            {
                genres += temp + ", ";
            }
            else
            {
                genres += temp;
            }
        }

        ArrayList<String> labelInfo = new ArrayList<>();
        labelInfo.add(movies.results.get(linkNum).getReleaseYear());
        labelInfo.add("PG-13");
        labelInfo.add(String.valueOf(movies.results.get(linkNum).getRuntime()) + " mins");
        labelInfo.add("English");
        labelInfo.add(genres);
        
        return labelInfo;
    }
    
    public static BufferedImage getPosterImage (int linkNum)
    {
        return posters.get(linkNum);
    }
}
