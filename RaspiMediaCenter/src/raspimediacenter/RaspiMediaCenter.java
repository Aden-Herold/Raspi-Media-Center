package raspimediacenter;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javax.swing.SwingUtilities;
import raspimediacenter.GUI.*;
import raspimediacenter.Logic.Utilities.ParserUtils;
import raspimediacenter.Logic.Utilities.ScraperUtils;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class RaspiMediaCenter {

    public static void main(String[] args) {

        ScraperUtils scraper = new ScraperUtils();
        scraper.startScrapers();

        //GUI gui = new GUI();
    }
}
