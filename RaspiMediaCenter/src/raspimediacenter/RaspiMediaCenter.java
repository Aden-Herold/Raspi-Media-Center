package raspimediacenter;

import raspimediacenter.GUI.*;
import raspimediacenter.Logic.ResourceHandler;
import raspimediacenter.Logic.Utilities.ParserUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility;

public class RaspiMediaCenter {

    public static void main(String[] args) {

        ResourceHandler resourceHandler = new ResourceHandler();
        //ScraperUtility scraper = new ScraperUtility();
        //scraper.startScrapers();
        ParserUtility parser = new ParserUtility();
        SceneManager sceneManager = new SceneManager();
    }
}
