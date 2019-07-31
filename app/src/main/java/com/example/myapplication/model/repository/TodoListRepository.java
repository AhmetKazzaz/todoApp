package com.example.myapplication.model.repository;

import android.app.Application;

import com.example.myapplication.model.AppDatabase;
import com.example.myapplication.model.dao.TodoListDAO;
import com.example.myapplication.model.dao.UserDAO;
import com.example.myapplication.model.entities.TodoList;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class TodoListRepository {
    private TodoListDAO todoListDAO;
    private UserDAO userDAO;
    private static TodoListRepository instance;

    public static TodoListRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TodoListRepository(application);
        }

        return instance;
    }

    private TodoListRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        todoListDAO = appDatabase.todoListDAO();
        userDAO = appDatabase.userDAO();
    }

    public Single<Long> create(TodoList todoList) {
        return todoListDAO.create(todoList);
    }

    public Single<Integer> update(TodoList todoList) {
        return todoListDAO.update(todoList);
    }

    public Single<Integer> delete(TodoList todoList) {
        return todoListDAO.delete(todoList);
    }

    public Flowable<TodoList> getTodoListById(long todoListId) {
        return todoListDAO.getTodoListById(todoListId);
    }

    public Flowable<List<TodoList>> findByUserId(long userId) {
        return todoListDAO.findByUserId(userId);
    }
}
