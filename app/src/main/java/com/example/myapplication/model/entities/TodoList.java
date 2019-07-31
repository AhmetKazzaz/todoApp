package com.example.myapplication.model.entities;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.BR;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity(tableName = "todo_lists")
public class TodoList extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user_id")
    private long userId;

    private String name;

    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;

    public TodoList(long id, long userId, String name, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Ignore
    public TodoList() {
    }

    @Bindable
    public long getId() {
        return id;
    }

    @Bindable
    public long getUserId() {
        return userId;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    public void setUserId(long userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.userId);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        TodoList todoList = (TodoList) o;
        return getId() == todoList.getId() &&
                getUserId() == todoList.getUserId() &&
                getName().equals(todoList.getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(getUserId())
                .append(getName())
                .toHashCode();
    }
}
