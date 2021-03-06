package raspimediacenter.Data.Models.TV;

import java.util.List;
import raspimediacenter.Logic.Utilities.ScraperUtils;

public class TVSeriesContainer {

    public List<TVSeries> results;

    public int getTotalEpisodes ()
    {
        int episodes = 0;
        for (TVSeries series : results)
        {
            episodes += series.number_of_episodes;
        }
        return episodes;
    }
    
    public int getTotalSeasons ()
    {
        int totalSeasons = 0;
        
        for (TVSeries show : results)
        {
            totalSeasons += ScraperUtils.getNumberOfSeasons(show);
        }
        
        return totalSeasons;
    }
    
    public String getGenresString (int resultsNum)
    {
        String genres = "";
        String temp = "";
        
        for (int x = 0; x < results.get(resultsNum).genres.size(); x++)
        {
            temp = results.get(resultsNum).genres.get(x).getGenre();
            
            if (temp.matches("Science Fiction"))
            {
                temp = "Sci-Fi";
            }
            
            if (x < results.get(resultsNum).genres.size()-1)
            {
                genres += temp + ", ";
            }
            else
            {
                genres += temp;
            }
        }
        
        return genres;
    }
    
    public static class TVSeries {

        private String backdrop_path;
        public List<SeriesCreator> created_by;
        private String first_air_date;
        public List<SeriesGenre> genres;
        private int id;
        private String last_air_date;
        private String name;
        public List<SeriesNetworks> networks;
        private String[] episode_run_time;
        private int number_of_episodes;
        private int number_of_seasons;
        private String[] origin_country;
        private String original_language;
        private String overview;
        private String poster_path;
        private String status;
        private float vote_average;

        public TVSeries() {
        }
        
        public String[] getEpisodeRunTime() {
            return episode_run_time;
        }
        
        public String getBackdropPath() {
            return backdrop_path;
        }

        public String getFirstAirDate() {
            return first_air_date;
        }

        public int getID() {
            return id;
        }

        public String getStartYear () {
            
            String year = first_air_date.substring(0, 4);
            return year;
        }
        
        public String getEndYear() {
            String year = last_air_date.substring(0, 4);
            return year;
        }
        
        public String getLastAirDate() {
            return last_air_date;
        }

        public String getName() {
            return name;
        }

        public int getNumberOfEpisodes() {
            return number_of_episodes;
        }

        public int getNumberOfSeasons() {
            return number_of_seasons;
        }

        public String[] getOriginCountry() {
            return origin_country;
        }

        public String getOriginalLanguage() {
            return original_language;
        }

        public String getOverview() {
            return overview;
        }

        public String getPosterPath() {
            return poster_path;
        }

        public String getStatus() {
            return status;
        }

        public float getRatingAverage() {
            return vote_average;
        }
        
        public String getGenresString ()
        {
            String genresString = "";
            String temp;

            for (int x = 0; x < genres.size(); x++)
            {
                temp = genres.get(x).getGenre();

                if (temp.matches("Science Fiction"))
                {
                    temp = "Sci-Fi";
                }

                if (x < genres.size()-1)
                {
                    genresString += temp + ", ";
                }
                else
                {
                    genresString += temp;
                }
            }

            return genresString;
        }
        
        public static class SeriesCreator {

            private String name;

            public String getName() {
                return name;
            }
        }

        public static class SeriesGenre {

            private String name;

            public String getGenre() {
                return name;
            }
        }

        public static class SeriesNetworks {

            private String name;

            public String getNetwork() {
                return name;
            }
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