package raspimediacenter.Data.Models;

import java.util.List;

public class TVSeriesContainer {

    public List<TVSeries> results;

    public static class TVSeries {

        private String poster_path;
        private String backdrop_path;
        private int id;
        private float vote_average;
        private String overview;
        private String name;

        public TVSeries() {
        }

        public String getName() {
            return name;
        }

        public int getID() {
            return id;
        }

        public float getAverage() {
            return vote_average;
        }

        public String getOverview() {
            return overview;
        }

        public String getPosterPath() {
            return poster_path;
        }

        public String getBackdropPath() {
            return backdrop_path;
        }

        @Override
        public String toString() {
            return "Title: " + this.name + "\n"
                    + "Description: " + this.overview + "\n"
                    + "Series ID: " + this.id + "\n"
                    + "Path to Poster: " + this.poster_path + "\n"
                    + "Path to Backdrop: " + this.poster_path + "\n"
                    + "Average Rating: " + this.vote_average;
        }
    }

}
