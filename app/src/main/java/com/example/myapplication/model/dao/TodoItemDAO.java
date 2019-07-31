package com.example.myapplication.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.myapplication.model.entities.TodoItem;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TodoItemDAO {

    @Insert
    Single<Long> create(TodoItem todoItem);

    @Update
    Single<Integer> update(TodoItem todoItem);

    @Delete
    Single<Integer> delete(TodoItem todoItem);

    @Query("SELECT * FROM todo_items WHERE id==:todoListItemId")
    Flowable<TodoItem> findById(long todoListItemId);

    @RawQuery(observedEntities = TodoItem.class)
    LiveData<List<TodoItem>> getTodoItems(SupportSQLiteQuery query);

    @Query("SELECT * FROM todo_items WHERE todo_list_id = :todoListId AND is_deleted = 0")
    Flowable<List<TodoItem>> findByTodoListId(long todoListId);
}
