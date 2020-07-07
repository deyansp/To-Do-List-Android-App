package com.example.mytodolist;

import android.app.Application;

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

    public ArrayList<ToDoTask> getAllTasks() {
        List<ToDoTask> tasksList = taskDao.getAllTasks();

        // casting to ArrayList used by activities
        return (ArrayList<ToDoTask>) tasksList;
    }

    public ArrayList<ToDoTask> getAllCompletedTasks() {
        List<ToDoTask> tasksList = taskDao.getAllCompletedTasks();

        // casting to ArrayList used by activities
        return (ArrayList<ToDoTask>) tasksList;
    }

    public ArrayList<ToDoTask> getAllUncompletedTasks() {
        List<ToDoTask> tasksList = taskDao.getAllUncompletedTasks();

        // casting to ArrayList used by activities
        return (ArrayList<ToDoTask>) tasksList;
    }

    public ToDoTask getTaskById(int id_num) {
        ToDoTask task = taskDao.getTaskById(id_num);

        return task;
    }
}
