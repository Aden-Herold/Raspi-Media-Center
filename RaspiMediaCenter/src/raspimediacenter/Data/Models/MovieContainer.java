package raspimediacenter.Data.Models;

import java.util.List;

public class MovieContainer {

    public List<Movie> results;

    public static class Movie {

        private String backdrop_path;
        public List<MovieGenre> genres;
        private int id;
        private String overview;
        private String poster_path; 
        public List<MovieProductionCompany> production_companies;
        private String release_date;
        private int runtime;
        private String tagline;
        private String title;
        private float vote_average;
        
        public String getBackdropPath() {
            return backdrop_path;
        }
        
        public int getID() {
            return id;
        }
        
        public String getOverview() {
            return overview;
        }
        
        public String getPosterPath() {
            return poster_path;
        }
        
        public String getReleaseDate() {
            return release_date;
        }
        
        public String getReleaseYear ()
        {
            String year = "";
            year = release_date.substring(0, 4);
            return year;
        }
        
        public int getRuntime() {
            return runtime;
        }
        
        public String getTagline() {
            return tagline;
        }
        
        public String getTitle() {
            return title;
        }
        
        public float getVoteAverage() {
            return vote_average;
        }
        
        public class MovieGenre {
            private String name;
            
            public String getGenre() {
                return name;
            }
        }
        
        public class MovieProductionCompany {
            private String name;
            
            public String getCompany() {
                return name;
            }
        }
    }
}
