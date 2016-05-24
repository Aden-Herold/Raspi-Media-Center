package raspimediacenter.Logic.Utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FileUtils {

    //RETURNS ALL FOLDER NAMES WITHIN DIRECTORY
    public static ArrayList<String> getAllSubDirsFromPath (String path)
    {
        ArrayList<String> subDirs;
        
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
              return new File(current, name).isDirectory();
            }
        });
        
        subDirs = new ArrayList<>(Arrays.asList(directories));
        return subDirs;
    }
    
    public static ArrayList<String> getAllSubDirsFromPathEndsWith (String path, String endsWith)
    {
        ArrayList<String> subDirs;
        
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                
                File file = new File(current, name);
                
                if (file.isDirectory())
                {
                    if (file.getName().endsWith(endsWith))
                    {
                        return true;
                    }
                }

              return false;
            }
        });
        
        subDirs = new ArrayList<>(Arrays.asList(directories));
        return subDirs;
    }

    public static ArrayList<String> getAllFileNamesFromDir (String directoryName)
    {
        ArrayList<String> files = new ArrayList<>();
        File directory = new File(directoryName);
        
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) 
        {
            if (file.isFile()) 
            {
                files.add(file.getName());
            } 
        }
        
        return files;
    }
    
    public static ArrayList<String> getFilesFronDir (String directoryName)
    {
        ArrayList<String> files = new ArrayList<>();
        File directory = new File(directoryName);
        
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) 
        {
            if (file.isFile()) 
            {
                files.add(file.getAbsolutePath());
            } 
        }
        
        return files;
    }
    
    public static String getRandomFileFromDir (String directoryName)
    {
        ArrayList<String> files = getFilesFronDir(directoryName);

        Random rand = new Random();
        int randomNum = rand.nextInt((files.size()-1 - 0) + 1) + 0;
        
        return files.get(randomNum);
    }
}
