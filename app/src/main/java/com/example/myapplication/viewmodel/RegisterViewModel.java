package com.example.myapplication.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.model.entities.User;
import com.example.myapplication.model.repository.UsersRepository;
import com.example.myapplication.utility.SingleLiveEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends BaseViewModel {

    private UsersRepository usersRepository;
    private SingleLiveEvent<Long> onUserCreated = new SingleLiveEvent<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        usersRepository = UsersRepository.getInstance(application);
    }

    public void createUser(User user) {
        Disposable disposable = usersRepository.create(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> onUserCreated.postValue(id),
                        error -> postError(R.string.COULDNT_CREATE_USER)
                );

        toDispose(disposable);
    }

    public void deleteUser(User user) {
        Disposable disposable = usersRepository.delete(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Toast.makeText(getApplication().getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show());

        toDispose(disposable);
    }

    public void updateUser(User user) {
        Disposable disposable = usersRepository.update(user)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> Toast.makeText(getApplication().getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show());
        toDispose(disposable);
    }

    public SingleLiveEvent<Long> onUserCreated() {
        return onUserCreated;
    }
}
