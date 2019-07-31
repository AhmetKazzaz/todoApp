package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.model.Auth;
import com.example.myapplication.model.entities.User;
import com.example.myapplication.model.repository.UsersRepository;
import com.example.myapplication.utility.SingleLiveEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    private UsersRepository usersRepository;
    private Auth auth;

    private SingleLiveEvent<User> onLoggedIn = new SingleLiveEvent<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        auth = Auth.getInstance(application);
        usersRepository = UsersRepository.getInstance(application);
    }

    public void signIn(User user) {
        Disposable disposable = usersRepository.findByUsername(user.getUserName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user1 -> {
                    if (user1.getPassword().equals(user.getPassword())) {
                        auth.setCurrentUser(user1.getId(), getApplication());
                        onLoggedIn.postValue(user1);
                    } else {
                        postError(R.string.PASSWORD_WRONG);
                    }
                }, error -> postError(R.string.PASSWORD_WRONG));

        toDispose(disposable);
    }

    public SingleLiveEvent<User> onLoggedIn() {
        return onLoggedIn;
    }

    public SingleLiveEvent<Error> onError() {
        return onError;
    }

}
