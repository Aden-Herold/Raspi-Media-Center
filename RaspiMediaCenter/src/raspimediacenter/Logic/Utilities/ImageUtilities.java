package raspimediacenter.Logic.Utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class ImageUtilities {
    
    //RETURNS THE IMAGE FROM THE GIVEN PATH
    public static BufferedImage getImageFromPath (String path)
    {
        BufferedImage backgroundImage = null;
        
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
    
    //RETURNS AN ARRAYLIST OF IMAGES FROM AN ARRAYLIST OF IMAGE LOCATION PATHS
    public static ArrayList<BufferedImage> getImagesFromPaths (ArrayList<String> imagePaths) 
    {
        ArrayList<BufferedImage> images = new ArrayList<>();
        
        if (imagePaths != null)
            {
                for (String imgPath : imagePaths)
                {
                    BufferedImage tmpImg = ImageUtilities.getImageFromPath(imgPath);
                    if (tmpImg != null)
                    {
                        images.add(tmpImg);
                    }
                }
            }
        
        return images;
    }
    
    //SEARCHES THE DIRECTORY FOR ANY IMAGE FILES WITH EXTENSION '.JPG'
    //CAN SEARCH SUB-DIRECTORIES FOR IMAGES AND ADD THEM TO THE ARRAYLIST
    public static ArrayList<String> getAllImagesPathsInDir (File directory, boolean descendIntoSubDirs) throws IOException
    {
        ArrayList<String> imageList = new ArrayList<String>();
        File[] files = directory.listFiles();
        
        for (File file : files)
        {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg")))
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
