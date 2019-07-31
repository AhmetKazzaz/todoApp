package com.example.myapplication.utility;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that deals with the file system
 */
public class FileHelper {

    public FileHelper() {

    }

    //Date formatting to display date in String
    public String formatDate(SimpleDateFormat df, Date date) {
        return date != null ? df.format(date) : "";
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    //returns the created directory or the already existent directory
    public File getCreatedDirectory() {
        // Get the directory for the user's public downloads directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Todo-List");
        if (!file.mkdirs()) {
            Log.e("FileUtils", "Directory not created");
        }
        return file;
    }

    //creates the csv file with the given name and path.
    public File createCsvFile(File path, String fileName) {
        return new File(path + " - " + fileName + ".csv");
    }
}
