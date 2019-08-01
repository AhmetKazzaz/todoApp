package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.model.repository.TodoItemRepository;
import com.example.myapplication.model.repository.TodoItemSearchCriteria;
import com.example.myapplication.utility.SingleLiveEvent;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.myapplication.model.repository.TodoItemSearchCriteria.CompletionState.Complete;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.CompletionState.Incomplete;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.CompletionState.NotSpecified;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.ExpiryState.Expired;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.ExpiryState.NotExpired;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.CreateDate;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.Deadline;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.Name;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.Status;

public class TodoItemViewModel extends BaseViewModel {

    private final TodoItemRepository repository;
    private MediatorLiveData<List<TodoItem>> onListUpdated = new MediatorLiveData<>();
    private SingleLiveEvent<Boolean> onItemTempDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Long> onItemCreated = new SingleLiveEvent<>();
    private TodoItemSearchCriteria criteria = new TodoItemSearchCriteria();

    private List<TodoItemSearchCriteria.OrderBy> orderValues = Arrays.asList(
            CreateDate,
            Deadline,
            Name,
            Status
    );

    private List<TodoItemSearchCriteria.CompletionState> statusValues = Arrays.asList(
            NotSpecified,
            Complete,
            Incomplete
    );

    private List<TodoItemSearchCriteria.ExpiryState> expiryValues = Arrays.asList(
            TodoItemSearchCriteria.ExpiryState.NotSpecified,
            Expired,
            NotExpired
    );
    private SingleLiveEvent<TodoItem> onTodoItemFound = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> onItemUpdated = new SingleLiveEvent<>();
    private long todoListId;

    public TodoItemViewModel(@NonNull Application application) {
        super(application);
        repository = TodoItemRepository.getInstance(application);
    }


    public void setTodoListId(long todoListId) {
        criteria.todoListId = todoListId;

        reloadData();
    }

    public void setSearchKeyword(String query) {
        criteria.searchKeyword = query;

        reloadData();
    }

    public void setOrderBy(int index) {
        criteria.orderBy = orderValues.get(index);

        reloadData();
    }

    public void setStatus(int index) {
        criteria.completionState = statusValues.get(index);

        reloadData();
    }

    public void setExpiryState(int index) {
        criteria.expiryState = expiryValues.get(index);

        reloadData();
    }

    public int getOrderBy() {
        return orderValues.indexOf(criteria.orderBy);
    }

    public int getStatus() {
        return statusValues.indexOf(criteria.completionState);
    }

    public int getExpiryState() {
        return expiryValues.indexOf(criteria.expiryState);
    }

    public void update(TodoItem item, boolean isTempDeleted) {
        toDispose(
                repository.update(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(affectedRows -> {
                            if (isTempDeleted)
                                onItemTempDeleted.setValue(true);
                            else {
                                onItemUpdated.postValue(affectedRows);
                            }

                        }, this::onQueryError)
        );
    }

    public void findById(long itemId) {
        toDispose(
                repository.findById(itemId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(todoItem -> {
                            onTodoItemFound.postValue(todoItem);
                        }, this::onQueryError));
    }

    public void updateStatus(TodoItem item, boolean isComplete) {
        item.setComplete(isComplete);
        toDispose(
                repository.update(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(affectedRows -> {
                        }, this::onQueryError)
        );
    }


    public void create(TodoItem item) {
        item.setCreatedAt(new Date(System.currentTimeMillis()));
        toDispose(
                repository.create(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(id -> onItemCreated.postValue(id), this::onQueryError)
        );
    }

    public void delete(TodoItem item) {
        toDispose(
                repository.delete(item)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(affectedRows -> {
                        }, this::onQueryError)
        );
    }

    public void tempDelete(TodoItem item, boolean setDeleted) {
        item.setDeleted(setDeleted);
        update(item, setDeleted);
    }

    public LiveData<List<TodoItem>> onListUpdated() {
        return onListUpdated;
    }

    public SingleLiveEvent<Boolean> onItemTempDeleted() {
        return onItemTempDeleted;
    }

    public SingleLiveEvent<Long> onItemCreated() {
        return onItemCreated;
    }

    public SingleLiveEvent<Integer> onItemUpdated() {
        return onItemUpdated;
    }

    public SingleLiveEvent<TodoItem> onTodoItemFound() {
        return onTodoItemFound;
    }

    private LiveData<List<TodoItem>> currentDataSource = null;

    private void reloadData() {

        if (currentDataSource != null) {
            onListUpdated.removeSource(currentDataSource);
        }

        currentDataSource = repository.getTodoItems(criteria);
        onListUpdated.addSource(currentDataSource, todoItems -> onListUpdated.postValue(todoItems));
    }


}
