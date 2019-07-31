package com.example.myapplication.model.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.model.entities.TodoList;

import java.util.List;

public class TodoListAndItems {
    @Embedded
    private TodoList todoList;

    @Relation(parentColumn = "id", entityColumn = "todo_list_id", entity = TodoItem.class)
    private List<TodoItem> todoItems;


    public TodoList getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }
}
