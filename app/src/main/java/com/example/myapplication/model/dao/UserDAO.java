package com.example.myapplication.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myapplication.model.entities.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDAO {

    @Insert()
    Maybe<Long> create(User user);

    @Query("SELECT * FROM users WHERE user_name==:userName")
    Single<User> findByUsername(String userName);

    @Update
    Completable update(User user);

    @Delete
    Completable delete(User user);

    @Query("SELECT * FROM users WHERE id==:userId")
    Single<User> getUser(long userId);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getUsers();

}
