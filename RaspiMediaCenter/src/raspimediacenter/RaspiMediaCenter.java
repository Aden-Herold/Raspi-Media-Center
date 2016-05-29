package raspimediacenter;

import raspimediacenter.GUI.GUI;
import raspimediacenter.Logic.Players.EmbeddedVideoPlayer;
import raspimediacenter.Logic.Utilities.ScraperUtils;

public class RaspiMediaCenter {

    public static void main(String[] args) {

        ScraperUtils scraper = new ScraperUtils();
        scraper.startScrapers(false, false);

        GUI gui = new GUI();
    }
}
