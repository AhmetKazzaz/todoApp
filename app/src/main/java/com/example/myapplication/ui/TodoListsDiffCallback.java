package com.example.myapplication.ui;

import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.model.entities.TodoList;

import java.util.ArrayList;

/**
 * DiffUtil Callback for updating the list when there is change deteced on them
 * Takes new todolist list and old list.
 */
public class TodoListsDiffCallback extends DiffUtil.Callback {

    private ArrayList<TodoList> oldList;
    private ArrayList<TodoList> newList;

    public TodoListsDiffCallback(ArrayList<TodoList> oldList, ArrayList<TodoList> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // TODO: 7/27/2019 How to compare contents? I've overridden equals and hashcode methods!
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
