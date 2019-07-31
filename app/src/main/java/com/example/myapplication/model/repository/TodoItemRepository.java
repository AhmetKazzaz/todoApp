package com.example.myapplication.model.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.example.myapplication.model.AppDatabase;
import com.example.myapplication.model.dao.TodoItemDAO;
import com.example.myapplication.model.entities.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class TodoItemRepository {
    private TodoItemDAO todoItemDAO;
    private static TodoItemRepository instance;

    public static TodoItemRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TodoItemRepository(application);
        }

        return instance;
    }

    private TodoItemRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        todoItemDAO = appDatabase.todoItemDAO();
    }


    public Single<Long> create(TodoItem todoItem) {
        return todoItemDAO.create(todoItem);
    }

    public Single<Integer> update(TodoItem todoItem) {
        return todoItemDAO.update(todoItem);
    }

    public Single<Integer> delete(TodoItem todoItem) {
        return todoItemDAO.delete(todoItem);
    }

    public Flowable<List<TodoItem>> findByTodoListId(long todoListId) {
        return todoItemDAO.findByTodoListId(todoListId);
    }

    public LiveData<List<TodoItem>> getTodoItems(TodoItemSearchCriteria criteria) {
        return todoItemDAO.getTodoItems(
                buildQuery(criteria)
        );
    }

    public Flowable<TodoItem> findById(long todoItemId) {
        return todoItemDAO.findById(todoItemId);
    }


    private SupportSQLiteQuery buildQuery(TodoItemSearchCriteria criteria) {

        SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("todo_items");

        ArrayList<Object> args = new ArrayList<>();
        ArrayList<String> clauses = new ArrayList<>();

        if (criteria.todoListId != -1) {
            clauses.add("todo_list_id = ?");
            args.add(criteria.todoListId);
        }

        if (criteria.searchKeyword != null && !criteria.searchKeyword.isEmpty()) {
            clauses.add("name LIKE ?");
            args.add("%" + criteria.searchKeyword + "%");
        }


        switch (criteria.completionState) {
            case Complete:
                clauses.add("is_complete = 1");
                break;
            case Incomplete:
                clauses.add("is_complete = 0");
                break;
        }

        switch (criteria.expiryState) {
            case Expired:
                clauses.add("deadline <= ?");
                args.add(new Date().getTime());
                break;
            case NotExpired:
                clauses.add("deadline >= ?");
                args.add(new Date().getTime());
                break;
        }

        switch (criteria.orderBy) {
            case CreateDate:
                builder.orderBy("created_at");
                break;
            case Deadline:
                builder.orderBy("deadline");
                break;
            case Name:
                builder.orderBy("name");
                break;
            case Status:
                builder.orderBy("is_complete");
                break;
        }

        clauses.add("is_deleted = 0");
        builder.selection(TextUtils.join(" AND ", clauses), args.toArray());

        Log.d("QUERY", builder.create().getSql());

        return builder.create();
    }
}

