package raspimediacenter;

import raspimediacenter.GUI.*;
import raspimediacenter.Logic.Utilities.LocalParserUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility;

public class RaspiMediaCenter {

    public static void main(String[] args) {

        ScraperUtility scraper = new ScraperUtility();
        LocalParserUtility parser = new LocalParserUtility();
        //scraper.beginTVScrape();
        
        SceneManager sceneManager = new SceneManager();
    }
}
