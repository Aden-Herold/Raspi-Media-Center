package raspimediacenter.Logic.Utilities;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageUtilities {
    
    public static Image getImageFromPath (String path)
    {
        Image backgroundImage = null;
        
        try 
        {
            backgroundImage = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        if (backgroundImage != null)
        {
            return backgroundImage;
        }
        else
        {
            return null;
        }
    }
    
    public static ArrayList<Image> getImagesFromPaths (ArrayList<String> imagePaths) 
    {
        ArrayList<Image> images = new ArrayList<>();
        
        if (imagePaths != null)
            {
                for (String imgPath : imagePaths)
                {
                    Image tmpImg = ImageUtilities.getImageFromPath(imgPath);
                    if (tmpImg != null)
                    {
                        images.add(tmpImg);
                    }
                }
            }
        
        return images;
    }
    
    public static ArrayList<String> getAllImagesPathsInDir (File directory, boolean descendIntoSubDirs) throws IOException
    {
        ArrayList<String> imageList = new ArrayList<String>();
        File[] files = directory.listFiles();
        
        for (File file : files)
        {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")))
            {
                imageList.add(file.getCanonicalPath());
            }
            if (descendIntoSubDirs && file.isDirectory())
            {
                ArrayList<String> temp = getAllImagesPathsInDir(file, true);
                if (temp != null)
                {
                    imageList.addAll(temp);
                }
            }
        }
        if (imageList.size() > 0)
        {
            return imageList;
        }
        else 
        {
            return null;
        }
    }
}
