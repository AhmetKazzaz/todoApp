package com.example.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.TodolistRecyclerviewItemBinding;
import com.example.myapplication.model.entities.TodoList;
import com.example.myapplication.ui.TodoListsDiffCallback;

import java.util.ArrayList;

public class TodoListsAdapter extends RecyclerView.Adapter<TodoListsAdapter.TodoListViewHolder> {

    private OnItemClickListener itemClickListener;
    private OnExportToExcelClicked onExportToExcelClicked;
    private ArrayList<TodoList> todoLists = new ArrayList<>();

    public ArrayList<TodoList> getTodoLists() {
        return todoLists;
    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodolistRecyclerviewItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.todolist_recyclerview_item, parent, false);
        return new TodoListViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        holder.itemBinding.setTodoList(todoLists.get(position));
    }

    @Override
    public int getItemCount() {
        return todoLists.size();
    }

    public void setTodoLists(ArrayList<TodoList> newTodoLists) {
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new TodoListsDiffCallback(todoLists,
                newTodoLists), false);
        todoLists = newTodoLists;
        result.dispatchUpdatesTo(this);
    }

    class TodoListViewHolder extends RecyclerView.ViewHolder {
        TodolistRecyclerviewItemBinding itemBinding;

        public TodoListViewHolder(@NonNull TodolistRecyclerviewItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            itemBinding.getRoot().setOnClickListener(v -> {
                if (itemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    itemClickListener.onItemClicked(todoLists.get(getAdapterPosition()));
            });
            itemBinding.ibExportToExcel.setOnClickListener(v -> {
                if (onExportToExcelClicked != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    onExportToExcelClicked.onExportClicked(todoLists.get(getAdapterPosition()));
            });
        }

    }


    public interface OnItemClickListener {
        void onItemClicked(TodoList todoList);
    }

    public interface OnExportToExcelClicked {
        void onExportClicked(TodoList todoList);
    }

    public void setOnExportToExcelClicked(OnExportToExcelClicked onExportToExcelClicked) {
        this.onExportToExcelClicked = onExportToExcelClicked;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
