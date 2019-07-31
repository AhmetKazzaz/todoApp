package com.example.myapplication;

import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.utility.CsvHelper;
import com.example.myapplication.utility.FileHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CsvWriterTest {

    private List<TodoItem> todoItemsMock; //still not used

    private String[] fileHeaders;

    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        TodoItem todoItem = new TodoItem(1, "todoItem1", "descriton",
                true, new Date(), 1);
        TodoItem todoItem1 = new TodoItem(2, "todoItem2", "descriton2",
                false, new Date(), 2);
        todoItem.setCreatedAt(new Date());
        todoItem1.setCreatedAt(new Date());

        todoItemsMock = new ArrayList<>();
        todoItemsMock.add(todoItem);
        todoItemsMock.add(todoItem1);
        fileHeaders = new String[]{
                "name",
                "description",
                "status",
                "deadline",
                "createdat",
        };
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    }


    @Test
    public void fileBeingCreatedAndHoldsSameRows() {
        StringWriter stringWriter = new StringWriter();
        String todolistName = "todoListNameMock";
        boolean isCreated = CsvHelper.getInstance().convertToCsv(todoItemsMock, todolistName, fileHeaders, stringWriter);
        assertTrue(isCreated);

        List<List<String>> rows = parseCsv(stringWriter.getBuffer().toString());
        assertEquals(rows.size(), todoItemsMock.size() + 1); // header and rows

        for (TodoItem item : todoItemsMock) {
            int index = todoItemsMock.indexOf(item);
            assertEquals(rows.get(index + 1).get(0), item.getName());
            assertEquals(rows.get(index + 1).get(1), item.getDescription());
            assertEquals(rows.get(index + 1).get(2), item.isComplete() ? "Complete" : "Incomplete");
            assertEquals(rows.get(index + 1).get(3), dateFormat.format(item.getDeadline()));
            assertEquals(rows.get(index + 1).get(4), dateFormat.format(item.getCreatedAt()));
        }
    }

    @Test
    public void dateFormattedCorrectly() {
        FileHelper fileHelper = new FileHelper();
        String dateStr = fileHelper.formatDate(dateFormat, new Date());
        assertNotEquals(dateStr, "");
        String dateStr2  = fileHelper.formatDate(null, null);
        assertNotNull(dateStr2);
    }

    private List<List<String>> parseCsv(String fileContents) {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(fileContents)) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }

        return records;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next().replaceAll("\"", ""));
            }
        }
        return values;
    }


}
