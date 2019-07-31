package com.example.myapplication.model.repository;

import android.app.Application;

import com.example.myapplication.model.AppDatabase;
import com.example.myapplication.model.dao.UserDAO;
import com.example.myapplication.model.entities.User;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class UsersRepository {

    private static UsersRepository sInstance;

    public static UsersRepository getInstance(Application application) {
        if (sInstance == null) {
            sInstance = new UsersRepository(application);
        }

        return sInstance;
    }

    private UserDAO userDAO;

    private UsersRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        userDAO = appDatabase.userDAO();
    }

    public Maybe<Long> create(User user) {
        return userDAO.create(user);
    }

    public Completable update(User user) {
        return userDAO.update(user);
    }

    public Completable delete(User user) {
        return userDAO.delete(user);
    }

    public Single<User> getUser(long userId) {
        return userDAO.getUser(userId);
    }

    public Single<User> findByUsername(String userName) {
        return userDAO.findByUsername(userName);
    }
}
