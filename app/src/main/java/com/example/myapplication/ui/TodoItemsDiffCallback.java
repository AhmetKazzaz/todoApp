package com.example.myapplication.ui;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.model.entities.TodoItem;

import java.util.ArrayList;

/**
 * DiffUtil Callback for updating the list when there is change deteced on them
 * Takes new todoItems list and old list.
 */
public class TodoItemsDiffCallback extends DiffUtil.Callback {

    private final ArrayList<TodoItem> oldItems;
    private final ArrayList<TodoItem> newItems;

    public TodoItemsDiffCallback(ArrayList<TodoItem> oldItems, ArrayList<TodoItem> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).getId() == newItems.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).equals(newItems.get(newItemPosition).getId());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
