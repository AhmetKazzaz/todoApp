package com.example.myapplication.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.EmptyResultSetException;

import com.example.myapplication.R;
import com.example.myapplication.utility.SingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Baseview model responsible for dealing with the disposable objects
 * and posting errors when they occur. acts as a base for all extending viewmodels
 */
abstract public class BaseViewModel extends AndroidViewModel {
    private CompositeDisposable disposables = new CompositeDisposable();
    protected SingleLiveEvent<Error> onError = new SingleLiveEvent<>();

    BaseViewModel(@NonNull Application application) {
        super(application);
    }

    protected void postError(@StringRes int stringRes) {
        onError.postValue(new Error(stringRes));
    }

    protected void postError(@StringRes int stringRes, @NonNull Object[] args) {
        onError.postValue(new Error(stringRes, args));
    }

    protected void toDispose(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void onQueryError(Throwable error) {
        if (error instanceof EmptyResultSetException) {
            postError(R.string.RECORD_DOESNT_EXIST);
        }

        postError(R.string.UNKOWN_ERROR);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        disposables.clear();
    }

    public static class Error {
        @StringRes
        final int stringRes;
        final Object[] args;


        Error(@StringRes int stringRes, @Nullable Object[] args) {
            this.stringRes = stringRes;
            this.args = args;
        }

        Error(int stringRes) {
            this(stringRes, null);
        }

        public String getString(@NonNull Context context) {
            if (args == null)
                return context.getString(stringRes);

            return context.getString(stringRes, args);
        }
    }
}
