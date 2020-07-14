package com.example.mytodolist;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DatabaseHandler  {
    private ToDoTaskDao taskDao;
    private LiveData<List<ToDoTask>> allTasks;

    public DatabaseHandler(Application app) {
        ToDoTaskDatabase db = ToDoTaskDatabase.getInstance(app);
        taskDao = db.taskDao();
        //allTasks = taskDao.getAllTasks();
    }

    public void insert(ToDoTask task) {
        new InsertToDoTaskAsyncTask(taskDao).execute(task);
    }

    public void update(ToDoTask task) {
        new UpdateToDoTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(ToDoTask task) {
        new DeleteToDoTaskAsyncTask(taskDao).execute(task);
    }

    // used by main activity to display either pending or completed tasks
   public LiveData<List<ToDoTask>> getAllTasks(boolean showCompletedTasks) {
        LiveData<List<ToDoTask>> tasksList;
        if (showCompletedTasks)
             tasksList = taskDao.getAllCompletedTasks();
        else
            tasksList = taskDao.getAllUncompletedTasks();

        return tasksList;
    }

    public LiveData<List<ToDoTask>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public LiveData<List<ToDoTask>> getAllCompletedTasks() {
        return taskDao.getAllCompletedTasks();
    }

    public LiveData<List<ToDoTask>> getAllUncompletedTasks() {
        return taskDao.getAllUncompletedTasks();
    }

    public ToDoTask getTaskById(int id_num) {
        return taskDao.getTaskById(id_num);
    }

    private static class InsertToDoTaskAsyncTask extends AsyncTask<ToDoTask, Void, Void> {
        private ToDoTaskDao taskDao;

        private InsertToDoTaskAsyncTask(ToDoTaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(ToDoTask... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateToDoTaskAsyncTask extends AsyncTask<ToDoTask, Void, Void> {
        private ToDoTaskDao taskDao;

        private UpdateToDoTaskAsyncTask(ToDoTaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(ToDoTask... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteToDoTaskAsyncTask extends AsyncTask<ToDoTask, Void, Void> {
        private ToDoTaskDao taskDao;

        private DeleteToDoTaskAsyncTask(ToDoTaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(ToDoTask... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }
}
