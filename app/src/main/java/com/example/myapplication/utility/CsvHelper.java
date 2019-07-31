package com.example.myapplication.utility;

import com.example.myapplication.model.entities.TodoItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


/**
 * This singelton class generates a file in csv format from the todoItems in a chosen todoList
 */

public class CsvHelper {

    private static CsvHelper instance;
    private FileHelper environmentHelper;

    public static CsvHelper getInstance() {
        if (instance == null) {
            instance = new CsvHelper();
        }
        return instance;
    }

    private CsvHelper() {
        environmentHelper = new FileHelper();
    }

    // abstracted method to be called for views.
    public File convertToCsv(List<TodoItem> items, String todoListName, String[] fileHeaders) {

        if (environmentHelper.isExternalStorageWritable()) {
            File file = environmentHelper.getCreatedDirectory();

            //writes to the newly generated file in the downloads directory
            File fileToWrite = environmentHelper.createCsvFile(file, todoListName + items.get(0).getTodoListId());

            FileWriter writer;
            try {
                writer = new FileWriter(fileToWrite);
                convertToCsv(items, todoListName, fileHeaders, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return fileToWrite;
        }

        return null;
    }

    /**
     * Method that takes required params to generate a csv file
     *
     * @param items:        the TodoItems to genearete a csv from
     * @param todoListName: The TodoList name
     * @param fileHeaders:  The headers of the excel file passed.
     * @return: returns a integer representing a string to indicate whether the file was created or not.
     */
    public boolean convertToCsv(List<TodoItem> items, String todoListName, String[] fileHeaders, Writer out) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            CSVWriter csvWriter = new CSVWriter(out);
            csvWriter.writeNext(fileHeaders);

            for (TodoItem item : items) {
                String status = item.isComplete() ? "Complete" : "Incomplete";
                String[] row = {
                        item.getName(),
                        item.getDescription(),
                        status,
                        environmentHelper.formatDate(df, item.getDeadline()),
                        environmentHelper.formatDate(df, item.getCreatedAt())
                };
                csvWriter.writeNext(row);
            }

            csvWriter.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }
}

