package com.example.myapplication.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.TodoitemRecyclerviewItemBinding;
import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.ui.TodoItemsDiffCallback;

import java.util.ArrayList;

public class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.TodoItemViewHolder> {

    private OnItemClickListener itemClickListener;
    private OnDeleteItemClickListener deleteItemClickListener;
    private OnMarkItemCompletedClickListener markItemCompletedClickListener;
    private ArrayList<TodoItem> items = new ArrayList<>();

    public ArrayList<TodoItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public TodoItemsAdapter.TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodoitemRecyclerviewItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.todoitem_recyclerview_item, parent, false);
        return new TodoItemsAdapter.TodoItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
        holder.itemBinding.setTodoItem(items.get(position));

        // setting a stroke line on the textViews if the item's status is complete
        if (items.get(position).isComplete()) {
            holder.itemBinding.etName.setPaintFlags(holder.itemBinding.etName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemBinding.etDescription.setPaintFlags(holder.itemBinding.etDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.itemBinding.etName.setPaintFlags(holder.itemBinding.etName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.itemBinding.etDescription.setPaintFlags(holder.itemBinding.etDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<TodoItem> newItems) {
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new TodoItemsDiffCallback(items,
                newItems), false);
        items = newItems;
        result.dispatchUpdatesTo(this);
    }

    class TodoItemViewHolder extends RecyclerView.ViewHolder {
        TodoitemRecyclerviewItemBinding itemBinding;

        public TodoItemViewHolder(@NonNull TodoitemRecyclerviewItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;


            itemBinding.getRoot().setOnClickListener(v -> {
                if (itemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    itemClickListener.onItemClicked(items.get(getAdapterPosition()));
            });
            createOptionsMenu(itemBinding);
        }

        /**
         * This method creates the options menu for the item.
         * Menu has 2 options, delete mark completed or incomplete
         *
         * @param itemBinding: the binding class for the item viewHolder
         */
        private void createOptionsMenu(@NonNull TodoitemRecyclerviewItemBinding itemBinding) {
            itemBinding.ibOptions.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), itemBinding.ibOptions);
                popup.inflate(R.menu.todo_item_rv_menu);
                popup.getMenu().findItem(R.id.mark_completed).setVisible(!items.get(getAdapterPosition()).isComplete());
                popup.getMenu().findItem(R.id.mark_incomplete).setVisible(items.get(getAdapterPosition()).isComplete());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            if (deleteItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                                deleteItemClickListener.onDeleteItemClicked(items.get(getAdapterPosition()));
                            break;
                        case R.id.mark_completed:
                            if (markItemCompletedClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                                markItemCompletedClickListener.onMarkItemCompletedClicked(
                                        items.get(getAdapterPosition()), true);
                            break;
                        case R.id.mark_incomplete:
                            if (markItemCompletedClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                                markItemCompletedClickListener.onMarkItemCompletedClicked(
                                        items.get(getAdapterPosition()), false);
                            break;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(TodoItem todoItem);
    }

    public interface OnDeleteItemClickListener {
        void onDeleteItemClicked(TodoItem todoItem);
    }

    public interface OnMarkItemCompletedClickListener {
        void onMarkItemCompletedClicked(TodoItem todoList, boolean isComplete);
    }

    public void setDeleteItemClickListener(OnDeleteItemClickListener deleteItemClickListener) {
        this.deleteItemClickListener = deleteItemClickListener;
    }

    public void setMarkItemCompletedClickListener(OnMarkItemCompletedClickListener markItemCompletedClickListener) {
        this.markItemCompletedClickListener = markItemCompletedClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
