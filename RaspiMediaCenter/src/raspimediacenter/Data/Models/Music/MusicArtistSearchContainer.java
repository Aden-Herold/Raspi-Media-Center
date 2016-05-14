package raspimediacenter.Data.Models.Music;

import java.util.List;

public class MusicArtistSearchContainer {

    public MusicArtistResult results;

    public static class MusicArtistResult {

        public ArtistMatches artistmatches;

        public static class ArtistMatches {

            public List<FoundArtist> artist;

            public static class FoundArtist {

                private String name;
                private String mbid;

                public String getName() {
                    return name;
                }

                public String getMBID() {
                    return mbid;
                }
            }
        }
    }
}
