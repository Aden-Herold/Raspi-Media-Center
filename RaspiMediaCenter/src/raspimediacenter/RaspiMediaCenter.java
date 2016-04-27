package raspimediacenter;

import raspimediacenter.GUI.*;
import raspimediacenter.Logic.Utilities.ParserUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility;

public class RaspiMediaCenter {

    public static void main(String[] args) {

        ScraperUtility scraper = new ScraperUtility();
        ParserUtility parser = new ParserUtility();
        //scraper.beginTVScrape();
        
        SceneManager sceneManager = new SceneManager();
    }
}
