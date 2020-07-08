package com.example.mytodolist;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler  {
    private ToDoTaskDao taskDao;
    //private List<ToDoTask> allTasks;

    public DatabaseHandler(Application app) {
        ToDoTaskDatabase db = ToDoTaskDatabase.getInstance(app);
        taskDao = db.taskDao();
    }

    public void insert(ToDoTask task) {
        taskDao.insert(task);
    }

    public void update(ToDoTask task) {
        taskDao.update(task);
    }

    public void delete(ToDoTask task) {
        delete(task);
    }

    public LiveData<List<ToDoTask>> getAllTasks() {
        LiveData<List<ToDoTask>> tasksList = taskDao.getAllTasks();

        // casting to ArrayList used by activities
        return tasksList;
    }

    public LiveData<List<ToDoTask>> getAllCompletedTasks() {
        LiveData<List<ToDoTask>> tasksList = taskDao.getAllCompletedTasks();

        // casting to ArrayList used by activities
        return tasksList;
    }

    public LiveData<List<ToDoTask>> getAllUncompletedTasks() {
        LiveData<List<ToDoTask>> tasksList = taskDao.getAllUncompletedTasks();

        // casting to ArrayList used by activities
        return tasksList;
    }

    public ToDoTask getTaskById(int id_num) {
        ToDoTask task = taskDao.getTaskById(id_num);

        return task;
    }
}
