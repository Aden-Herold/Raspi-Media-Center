package raspimediacenter.Data.Models.Music;

import java.util.List;

public class MusicTrackContainer {

    public List<MusicTrack> tracks;

    public static class MusicTrack {

        public Track track;

        public static class Track {

            private String name;
            private String mbid;
            private String duration;
            public TrackArtist artist;
            public TrackAlbum album;
            public TrackTopTags toptags;
            public TrackWiki wiki;

            public String getName() {
                return name;
            }

            public String getMBID() {
                return mbid;
            }

            public long getDuration() {
                return Long.valueOf(duration);
            }

            public static class TrackArtist {

                private String name;

                public String getName() {
                    return name;
                }
            }

            public static class TrackAlbum {

                private String artist;
                private String title;
                private String mbid;

                public String getArtist() {
                    return artist;
                }

                public String getTitle() {
                    return title;
                }

                public String getMBID() {
                    return mbid;
                }
            }

            public static class TrackTopTags {

                public List<TrackTag> tag;

                public static class TrackTag {

                    private String name;

                    public String getName() {
                        return name;
                    }
                }
            }

            public static class TrackWiki {

                private String summary;

                public String getSummary() {
                    return summary;
                }
            }
        }
    }
}
