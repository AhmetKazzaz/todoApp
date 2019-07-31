package com.example.myapplication.viewmodel;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.myapplication.model.Auth;
import com.example.myapplication.model.entities.User;
import com.example.myapplication.utility.SingleLiveEvent;

public class HomeViewModel extends BaseViewModel {

    private SingleLiveEvent<User> _onAuthStatusChanged = new SingleLiveEvent<>();
    private Auth auth;

    private final Handler handler = new Handler();
    private final Runnable listenToAuth = () ->
            toDispose(
                    auth.status.subscribe(
                            status -> _onAuthStatusChanged.postValue(status.currentUser),
                            error -> _onAuthStatusChanged.postValue(null)
                    )
            );

    public HomeViewModel(@NonNull Application application) {
        super(application);

        auth = Auth.getInstance(application);

        handler.postDelayed(listenToAuth, 1000);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(listenToAuth);
        auth.dispose();
    }

    public LiveData<User> onAuthStatusChanged() {
        return _onAuthStatusChanged;
    }
}
