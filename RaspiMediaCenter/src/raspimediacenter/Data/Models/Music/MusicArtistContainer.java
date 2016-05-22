package raspimediacenter.Data.Models.Music;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MusicArtistContainer {
    public List<MusicArtist> artists;
    
    public static class MusicArtist {
        public Artist artist;
        
        public static class Artist {
            private String name;
            private String mbid;
            public List<ArtistImage> image;
            public ArtistTags tags;
            public ArtistBio bio;
            
            public String getName() {
                return name;
            }
            
            public String getMBID() {
                return mbid;
            }
            
            public static class ArtistImage {
                @SerializedName("#text")
                private String text;
                
                private String size;
                
                public String getPath() {
                    return text;
                }
                
                public String getSize() {
                    return size;
                }
            }
            
            public static class ArtistTags {
                public List<ArtistTag> tag;
                
                public String getTagString()
                {
                    String tagString = "";
                    
                    if (tag != null)
                    {
                        for (int i = 0;  i < tag.size(); i++)
                        {
                            if (i < tag.size()-1)
                            {
                                tagString += tag.get(i).getName() + ", ";
                            }
                            else 
                            {
                                tagString += tag.get(i).getName();
                            }
                        }
                    }
                    
                    return tagString;
                }
                
                public static class ArtistTag {
                    private String name;
                    
                    public String getName() {
                        return name;
                    }
                }
            }
            
            public static class ArtistBio {
                private String summary;
                
                public String getSummary() {
                    return summary;
                }
            }
        }
    }
}
