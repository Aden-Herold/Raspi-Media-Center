package raspimediacenter.Data.Models.Music;

import java.util.List;


public class MusicTrackSearchContainer {

    public MusicTrackResult results;
    
    public static class MusicTrackResult {
        public TrackMatches trackmatches;
        
        public static class TrackMatches {
            public List<FoundTrack> track;
            
            public static class FoundTrack {
                private String name;
                private String artist;
                private String mbid;
                
                public String getName() {
                    return name;
                }
                
                public String getArtist() {
                    return artist;
                }
                
                public String getMBID() {
                    return mbid;
                }
            }
        }
    }
}
