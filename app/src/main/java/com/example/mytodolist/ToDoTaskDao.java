package com.example.mytodolist;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM to_do_tasks")
    LiveData<List<ToDoTask>> getAllTasks();

    @Query("SELECT * FROM to_do_tasks WHERE isDone = 1")
    LiveData<List<ToDoTask>> getAllCompletedTasks();

    @Query("SELECT * FROM to_do_tasks WHERE isDone = 0")
    LiveData<List<ToDoTask>> getAllUncompletedTasks();

    @Query("SELECT * FROM to_do_tasks WHERE id = :id_num ")
    ToDoTask getTaskById(int id_num);

}
