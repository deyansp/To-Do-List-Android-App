package com.example.mytodolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

// interface through which the Room library will generate the necessary db code
@Dao
public interface ToDoTaskDao  {
    @Insert
    void insert(ToDoTask task);

    @Update
    void update(ToDoTask task);

    @Delete
    void delete(ToDoTask task);

    @Query("SELECT * FROM to_do_tasks")
    ArrayList<ToDoTask> getAllTasks();

    @Query("SELECT * FROM to_do_tasks WHERE isDone = 'true'")
    ArrayList<ToDoTask> getAllCompletedTasks();

    @Query("SELECT * FROM to_do_tasks WHERE isDone = 'false'")
    ArrayList<ToDoTask> getAllUncompletedTasks();
}
