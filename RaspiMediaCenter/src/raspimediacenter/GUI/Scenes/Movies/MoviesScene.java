package raspimediacenter.GUI.Scenes.Movies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import raspimediacenter.Data.Models.MovieContainer;
import raspimediacenter.GUI.Components.VideoComponents.InformationPanelGraphics;
import raspimediacenter.GUI.Components.VideoComponents.PosterGraphics;
import raspimediacenter.GUI.Components.VideoComponents.VideoInformationPanel;
import raspimediacenter.GUI.Components.VideoComponents.VideoListItemButton;
import raspimediacenter.GUI.Scenes.Scene;
import raspimediacenter.GUI.Scenes.VideoLibraryScene;
import raspimediacenter.Logic.Utilities.ImageUtilities;
import raspimediacenter.Logic.Utilities.ParserUtility;

public class MoviesScene extends VideoLibraryScene {

    private static MovieContainer movies;
    private final ArrayList<JButton> seriesLinks = new ArrayList<>();
    private final ArrayList<String> infoLabels = new ArrayList<>(Arrays.asList("Year:", "Rating:", "Runtime:", "Languages:", "Genres:"));
    private static final ArrayList<BufferedImage> posters = new ArrayList<>();
    
    public MoviesScene() {
        super();
        Scene.setCurrentScene("Movies");
        
        ParserUtility parser = new ParserUtility();
        movies = parser.parseMovieList("Movies/movie-list.json", false);
        
        loadMovieBackgrounds();
        loadMoviePosters();
        bgCanvas.setBackgroundImage(0);
        
        //Create informationPanelGraphics
        infoPanelGraphics = new InformationPanelGraphics();
        infoPanel = new VideoInformationPanel();
        previewGraphics = new PosterGraphics();
        
        VideoLibraryScene.setPreviewImageWidth(previewGraphics.getPosterWidth());
        VideoLibraryScene.setPreviewImageHeight(previewGraphics.getPosterHeight());

        infoPanel.setupInfoPanel(infoLabels, generateMoviesInfo(0));
        infoPanel.createStarRating(movies.results.get(0).getVoteAverage(), 10);
        infoPanel.createOverviewDisplay(movies.results.get(0).getOverview());
        createLinkList();
        createListDisplay(seriesLinks);
    }
    
    private void createLinkList()
    {
        for (int x = 0; x < movies.results.size(); x++)
        {
            if (!movies.results.get(x).getTitle().matches("")){
                JButton button = new VideoListItemButton(movies.results.get(x).getTitle(), this, x);
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
        String genres = movies.getGenresString(linkNum);
        String langs = movies.getLanguagesString(linkNum);

        ArrayList<String> labelInfo = new ArrayList<>();
        labelInfo.add(movies.results.get(linkNum).getReleaseYear());
        labelInfo.add("PG-13");
        labelInfo.add(String.valueOf(movies.results.get(linkNum).getRuntime()) + " mins");
        labelInfo.add(langs);
        labelInfo.add(genres);
        
        return labelInfo;
    }
    
    public static BufferedImage getPosterImage (int linkNum)
    {
        return posters.get(linkNum);
    }
}
