package aronharder.itemizer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by aronharder on 6/16/17.
 */
public class FileManager {
    private String filepath;
    private String filename;

    public FileManager(){
        filepath = "Itemizer/";
        filename = "Untitled";
    }

    public FileManager(String filepath, String filename){
        this.filepath = filepath;
        this.filename = filename;
    }

    /**
     * Loads every line of a file except the first (the first line is file settings)
     * @return - every line of the file associated with the file manager, except the first
     */
    //http://www.mkyong.com/java/how-to-read-file-in-java-fileinputstream/
    public String loadFileContents(){
        File f = new File(Environment.getExternalStorageDirectory(),filepath+filename);
        if (f.exists()){
            try {
                String content = "";
                FileInputStream fIn = new FileInputStream(f);
                int n = fIn.read();
                while (n != -1 ){
                    content+= (char) n;
                    n = fIn.read();
                }
                fIn.close();
                return content.substring(content.indexOf("\n")+1,content.length()); //Remove file settings
            } catch (IOException e){
                Log.e("ERROR", "Could not find file", e);
                return "";
            }
        }
        return "";
    }

    /**
     * Loads the first line of a file, which should be the file settings
     * @return - the first line of the file associated with the file manager
     */
    public String loadFileSettings(){
        File f = new File(Environment.getExternalStorageDirectory(),filepath+filename);
        if (f.exists()){
            try {
                String settings = "";
                FileInputStream fIn = new FileInputStream(f);
                int n = fIn.read();
                while ((char) n != '\n' ){
                    settings+= (char) n;
                    n = fIn.read();
                }
                fIn.close();
                return settings;
            } catch (IOException e){
                Log.e("ERROR", "Could not find file", e);
                return "";
            }
        }
        return "";
    }

    /**
     * Saves content to a file
     * @param content - the content to save
     * @return whether the save was successful
     */
    //http://stackoverflow.com/questions/8330276/write-a-file-in-external-storage-in-android
    public boolean saveFileContents(String content){
        String settings = loadFileSettings();
        try {
            File d = new File(Environment.getExternalStorageDirectory(), filepath);
            if (!d.exists()){
                if (!d.mkdir()){
                    Log.e("ERROR","Problem creating file directory");
                    return false;
                }
            }
            File f = new File(Environment.getExternalStorageDirectory(), filepath+filename);
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter outWriter = new OutputStreamWriter(fOut);
            outWriter.append(settings+"\n");
            outWriter.append(content);
            outWriter.close();
            fOut.close();
            return true;
        } catch (IOException e){
            Log.e("ERROR", "Could not create file", e);
            return false;
        }
    }

    /**
     * Saves settings to a file
     * @param settings - the settings to save
     * @return whether the save was successful
     */
    public boolean saveFileSettings(String settings){
        String content = loadFileContents();
        try {
            File d = new File(Environment.getExternalStorageDirectory(), filepath);
            if (!d.exists()){
                if (!d.mkdir()){
                    Log.e("ERROR","Problem creating file directory");
                    return false;
                }
            }
            File f = new File(Environment.getExternalStorageDirectory(), filepath+filename);
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter outWriter = new OutputStreamWriter(fOut);
            outWriter.append(settings+'\n');
            outWriter.append(content);
            outWriter.close();
            fOut.close();
            return true;
        } catch (IOException e){
            Log.e("ERROR", "Could not create file", e);
            return false;
        }
    }

    /**
     * Changes a file's name
     * @param new_filename - the new filename
     * @return whether the name change was successful
     */
    public boolean changeFile(String new_filename){
        File f = new File(Environment.getExternalStorageDirectory(), filepath+filename);
        File dest = new File(Environment.getExternalStorageDirectory(), filepath+new_filename);
        boolean r = f.renameTo(dest);
        if (r){
            filename = new_filename;
        }
        return r;
    }

    /**
     * Mutator to change filepatah
     * @param new_filepath - the new filepath
     */
    public void setFilepath(String new_filepath){
        filepath = new_filepath;
    }

    /**
     * Accessor to get the filepath
     * @return the filepath
     */
    public String getFilepath(){
        return filepath;
    }

    /**
     * Mutator to change filename
     * @param new_filename - the new filename
     */
    public void setFilename(String new_filename){
        filename = new_filename;
    }

    /**
     * Accessor to get the filename
     * @return the filename
     */
    public String getFilename(){
        return filename;
    }

}
