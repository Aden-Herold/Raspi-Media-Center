package raspimediacenter.Data.Models.Music;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MusicAlbumContainer {

    public MusicAlbum album;

    public static class MusicAlbum {

        private String name;
        private String artist;
        private String mbid;
        public List<AlbumImage> image;
        public AlbumTracks tracks;
        public AlbumTags tags;
        public AlbumWiki wiki;

        public String getName() {
            return name;
        }

        public String getArtist() {
            return artist;
        }

        public String getMBID() {
            return mbid;
        }

        public static class AlbumImage {

            @SerializedName("#text")
            private String text;

            private String size;

            public String getText() {
                return text;
            }

            public String getSize() {
                return size;
            }
        }

        public static class AlbumTracks {

            public List<AlbumTrack> track;

            public static class AlbumTrack {

                private String name;
                private String duration;

                public String getName() {
                    return name;
                }

                public Long getDuration() {
                    return Long.valueOf(duration);
                }
            }

        }

        public static class AlbumTags {

            public List<AlbumTag> tag;

            public static class AlbumTag {

                private String name;

                public String getName() {
                    return name;
                }
            }
        }

        public static class AlbumWiki {

            private String summary;
            
            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getSummary() {
                return summary;
            }
        }
    }
}

