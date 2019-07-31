package com.example.myapplication.model.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.BR;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

@Entity(tableName = "todo_items")
public class TodoItem extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String description;
    @ColumnInfo(name = "is_complete")
    private boolean isComplete = false;
    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;
    private Date deadline;
    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "todo_list_id")
    private long todoListId;

    public TodoItem(long id, String name, String description, boolean isComplete, Date deadline, long todoListId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isComplete = isComplete;
        this.deadline = deadline;
        this.todoListId = todoListId;
    }

    @Ignore
    public TodoItem() {
    }

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
        notifyPropertyChanged(BR.complete);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Bindable
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
        notifyPropertyChanged(BR.deadline);
    }

    @Bindable
    public long getTodoListId() {
        return todoListId;
    }

    public void setTodoListId(long todoListId) {
        this.todoListId = todoListId;
        notifyPropertyChanged(BR.todoListId);
    }

    @Bindable
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoItem)) return false;
        TodoItem todoItem = (TodoItem) o;
        return getId() == todoItem.getId() &&
                getTodoListId() == todoItem.getTodoListId() &&
                getName().equals(todoItem.getName()) &&
                getDescription().equals(todoItem.getDescription()) &&
                isComplete() == todoItem.isComplete() &&
                getDeadline().equals(todoItem.getDeadline());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(getTodoListId())
                .append(getName())
                .append(getDescription())
                .append(isComplete())
                .append(getDeadline())
                .toHashCode();
    }
}
