package raspimediacenter.Logic.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImageUtilitiesTest {
    
    public ImageUtilitiesTest() {
    }

    /**
     * Test of getAllImagesPathsInDir method, of class ImageUtilities.
     */
    @Test
    public void testGetAllImagesPathsInDir() throws Exception {

        String dir = "Resources/UserBackgrounds/";
        ArrayList<String> imagePaths = new ArrayList<>();
        ArrayList<String> expectedPaths = new ArrayList<>();

        try {
            imagePaths = ImageUtils.getAllImagesPathsInDir(dir, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        
        File image1 = new File("src/raspimediacenter/GUI/Resources/UserBackgrounds/starwars_theforceawakens_fanart.jpg");
        File image2 = new File("src/raspimediacenter/GUI/Resources/UserBackgrounds/thehobbit_anunexpectedjourney_fanart.jpg");
        File image3 = new File("src/raspimediacenter/GUI/Resources/UserBackgrounds/thehobbit_desolationofsmaug_fanart.jpg");
        
        expectedPaths.add(image1.getAbsolutePath());
        expectedPaths.add(image2.getAbsolutePath());
        expectedPaths.add(image3.getAbsolutePath());

        assertEquals(imagePaths, expectedPaths); 
    }
    
}
