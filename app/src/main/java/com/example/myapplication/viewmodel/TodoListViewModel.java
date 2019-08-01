package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.R;
import com.example.myapplication.model.Auth;
import com.example.myapplication.model.entities.TodoList;
import com.example.myapplication.model.repository.TodoItemRepository;
import com.example.myapplication.model.repository.TodoListRepository;
import com.example.myapplication.utility.CsvHelper;
import com.example.myapplication.utility.SingleLiveEvent;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TodoListViewModel extends BaseViewModel {

    private MutableLiveData<List<TodoList>> onListUpdated = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> onTodoListTempDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> onTodoListEmpty = new SingleLiveEvent<>();
    private SingleLiveEvent<File> onFileCreated = new SingleLiveEvent<>();


    public LiveData<List<TodoList>> onListUpdated() {
        return onListUpdated;
    }

    private TodoListRepository todoListRepository;
    private TodoItemRepository todoItemRepository;
    private final Auth auth;

    public TodoListViewModel(@NonNull Application application) {
        super(application);
        auth = Auth.getInstance(application);
        todoListRepository = TodoListRepository.getInstance(application);
        todoItemRepository = TodoItemRepository.getInstance(application);

        getTodoListsByUserId(auth.currentUserId(application));
    }

    public void createTodoList(TodoList todoList) {
        todoList.setUserId(auth.currentUserId(getApplication()));
        toDispose(
                todoListRepository.create(todoList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(todoListId -> {

                        }, error -> {
                        })
        );
    }

    public void exportToExcel(long todoListId, String todoListName) {
        toDispose(
                todoItemRepository.findByTodoListId(todoListId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(todoItems -> {
                            if (todoItems.size() > 0) {
                                onFileCreated.postValue(
                                        CsvHelper.getInstance()
                                                .convertToCsv(todoItems, todoListName, fileHeadersForExcel()));
                            } else {
                                onTodoListEmpty.postValue(true);
                            }
                        }, this::onQueryError)
        );

    }

    private String[] fileHeadersForExcel() {
        return new String[]{
                getApplication().getString(R.string.NAME),
                getApplication().getString(R.string.DESCRIPTION),
                getApplication().getString(R.string.STATUS_SMALL_LETTERS),
                getApplication().getString(R.string.DEADLINE),
                getApplication().getString(R.string.CREATED_AT),
        };
    }

    public void updateTodoList(TodoList todoList, boolean isTempDeleted) {
        toDispose(
                todoListRepository.update(todoList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updatedRow -> {
                                    if (isTempDeleted)
                                        onTodoListTempDeleted.setValue(true);
                                },
                                this::onQueryError)
        );

    }

    public void tempDeleteTodoList(TodoList todoList, boolean setDeleted) {
        todoList.setDeleted(setDeleted);
        updateTodoList(todoList, setDeleted);
    }

    public void deleteTodoList(TodoList todoList) {
        toDispose(
                todoListRepository.delete(todoList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(deletedRow -> {
                        }, this::onQueryError)
        );
    }

    public void getTodoListsByUserId(long userId) {
        toDispose(
                todoListRepository.findByUserId(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userAndTodoLists -> onListUpdated.postValue(userAndTodoLists), this::onQueryError)
        );
    }

    public void logout() {
        auth.clearAuthData(getApplication());
    }

    public SingleLiveEvent<Boolean> onTodoListTempDeleted() {
        return onTodoListTempDeleted;
    }

    public SingleLiveEvent<File> onFileCreated() {
        return onFileCreated;
    }

    public SingleLiveEvent<Boolean> onTodoListEmpty() {
        return onTodoListEmpty;
    }
}
