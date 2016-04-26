package raspimediacenter.Data.Models;

import java.util.List;

public class TVSeasonContainer {

    public List<TVSeason> episodes;

    private String name;
    private String overview;
    private int id;
    private String poster_path;
    private int season_number;

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public int getSeasonNumber() {
        return season_number;
    }

    public static class TVSeason {

        private String name;
        private String overview;
        private int id;
        private int episode_number;
        private int season_number;
        private String still_path;
        private float vote_average;

        public String getName() {
            return name;
        }

        public String getOverview() {
            return overview;
        }

        public int getID() {
            return id;
        }

        public float getVoteAverage() {
            return vote_average;
        }

        public int getSeasonNumber() {
            return season_number;
        }

        public String getStillPath() {
            return still_path;
        }

        @Override
        public String toString() {
            return "Title: " + this.name + "\n"
                    + "Description: " + this.overview + "\n"
                    + "Episode Number: " + this.episode_number + "\n"
                    + "Season Number: " + this.season_number + "\n"
                    + "Path to Still: " + this.still_path + "\n"
                    + "Average Rating: " + this.vote_average;
        }
    }

}