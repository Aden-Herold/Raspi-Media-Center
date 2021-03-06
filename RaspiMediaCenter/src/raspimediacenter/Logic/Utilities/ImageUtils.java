package raspimediacenter.Logic.Utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static raspimediacenter.Logic.Utilities.FileUtils.getFilesFronDir;

public class ImageUtils {
    
    //RETURNS THE IMAGE FROM THE GIVEN PATH
    public static BufferedImage getImageFromPath (String path)
    {
        BufferedImage backgroundImage = null;
        
        try 
        {
            ImageIO.setUseCache(false);
            backgroundImage = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage() + " @ " + path);
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
    
    public static BufferedImage getRandomImageFromDir (String directoryName)
    {
        ArrayList<String> files = null;
        try 
        {
            files = getAllImagesPathsInDir(directoryName, true);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ImageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        Random rand = new Random();
        int randomNum = rand.nextInt((files.size()-1 - 0) + 1) + 0;
        
        BufferedImage img = ImageUtils.getImageFromPath(files.get(randomNum));
        
        return img;
    }
    
    public static BufferedImage getRandomImageFromList(ArrayList<String> imagePaths)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt((imagePaths.size()-1 - 0) + 1) + 0;
        
        BufferedImage img = ImageUtils.getImageFromPath(imagePaths.get(randomNum));
        
        return img;
    }
    
    //RETURNS AN ARRAYLIST OF IMAGES FROM AN ARRAYLIST OF IMAGE LOCATION PATHS
    public static ArrayList<BufferedImage> getImagesFromPaths (ArrayList<String> imagePaths) 
    {
        ArrayList<BufferedImage> images = new ArrayList<>();
        
        if (imagePaths != null)
            {
                for (String imgPath : imagePaths)
                {
                    BufferedImage tmpImg = ImageUtils.getImageFromPath(imgPath);
                    if (tmpImg != null)
                    {
                        images.add(tmpImg);
                    }
                }
            }
        
        return images;
    }
    
    public static String getFirstImagePathInDir(String directory)
    {
        File dir = new File(System.getProperty("user.dir") + "/" + directory);
        File[] files = dir.listFiles();
        String str = null;
        
        for (File file : files)
        {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg")))
            {
                str = file.getPath();
                break;
            }
        }
        
        return str;
    }
    
    //SEARCHES THE DIRECTORY FOR ANY IMAGE FILES WITH EXTENSION '.JPG'
    //CAN SEARCH SUB-DIRECTORIES FOR IMAGES AND ADD THEM TO THE ARRAYLIST
    public static ArrayList<String> getAllImagesPathsInDir (String directory, boolean descendIntoSubDirs) throws IOException
    {
        File dir = new File(System.getProperty("user.dir") + "/" + directory);
        ArrayList<String> imageList = new ArrayList<>();
        File[] files = dir.listFiles();
        
        for (File file : files)
        {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg")))
            {
                imageList.add(file.getPath());
            }
            if (descendIntoSubDirs && file.isDirectory())
            {
                ArrayList<String> temp = getAllImagesPathsInDir(file.getPath(), true);
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
