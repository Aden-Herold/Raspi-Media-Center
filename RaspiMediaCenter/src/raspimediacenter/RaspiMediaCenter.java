package raspimediacenter;

import raspimediacenter.GUI.*;
import raspimediacenter.Logic.Utilities.ParserUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility;
import raspimediacenter.Logic.Utilities.ScraperUtility.ScraperThread;

public class RaspiMediaCenter {

    public static void main(String[] args) {

        ScraperUtility scraper = new ScraperUtility();
        //ScraperUtility.ScraperThread thread = scraper.new ScraperThread();
        //Thread t = new Thread(thread);
        //t.start();
        ParserUtility parser = new ParserUtility();
        SceneManager sceneManager = new SceneManager();
    }
}
