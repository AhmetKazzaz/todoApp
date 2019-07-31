package com.example.myapplication.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapplication.model.entities.TodoList;
import com.example.myapplication.model.relations.TodoListAndItems;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TodoListDAO {

    @Insert
    Single<Long> create(TodoList todoList);

    @Update
    Single<Integer> update(TodoList todoList);

    @Delete
    Single<Integer> delete(TodoList todoList);

    @Query("SELECT * FROM todo_lists WHERE user_id = :userId AND is_deleted = 0")
    Flowable<List<TodoList>> findByUserId(long userId);

    @Query("SELECT * FROM todo_lists WHERE id==:todoListId")
    Flowable<TodoList> getTodoListById(long todoListId);

}
