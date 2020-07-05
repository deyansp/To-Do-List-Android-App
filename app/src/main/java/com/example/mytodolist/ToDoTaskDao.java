package com.example.mytodolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// interface through which the Room library will generate the necessary db code
@Dao
public interface ToDoTaskDao  {
    @Insert
    void insert(ToDoTask task);

    @Update
    void update(ToDoTask task);

    @Delete
    void delete(ToDoTask task);
    // TODO implement conversion in MainActivity to ArrayList from List since Room doesn't support ArrayList
    @Query("SELECT * FROM to_do_tasks")
    List<ToDoTask> getAllTasks();

    @Query("SELECT * FROM to_do_tasks WHERE isDone = 'true'")
    List<ToDoTask> getAllCompletedTasks();

    @Query("SELECT * FROM to_do_tasks WHERE isDone = 'false'")
    List<ToDoTask> getAllUncompletedTasks();
}
