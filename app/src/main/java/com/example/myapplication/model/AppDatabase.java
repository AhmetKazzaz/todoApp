package com.example.myapplication.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.model.converters.DateConverter;
import com.example.myapplication.model.dao.TodoListDAO;
import com.example.myapplication.model.dao.TodoItemDAO;
import com.example.myapplication.model.dao.UserDAO;
import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.model.entities.TodoList;
import com.example.myapplication.model.entities.User;


@Database(entities = {User.class, TodoList.class, TodoItem.class}, version = 6, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "todo_app_database";
    private static AppDatabase instance;

    public abstract UserDAO userDAO();

    public abstract TodoListDAO todoListDAO();

    public abstract TodoItemDAO todoItemDAO();



    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
