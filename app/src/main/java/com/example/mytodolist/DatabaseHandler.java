package com.example.mytodolist;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

// abstraction class for db actions, can be extended in the future to use Firebase
// and alternate between local and cloud storage methods
public class DatabaseHandler  {

    // database access object for db queries
    private ToDoTaskDao taskDao;

    public DatabaseHandler(Application app) {
        ToDoTaskDatabase db = ToDoTaskDatabase.getInstance(app);
        taskDao = db.taskDao();
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


    // Room executes methods that return LiveData outside the UI thread by default
    // so no AsyncTasks are needed
    public LiveData<List<ToDoTask>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    public LiveData<List<ToDoTask>> getAllCompletedTasks() { return taskDao.getAllCompletedTasks(); }

    public LiveData<List<ToDoTask>> getAllUncompletedTasks() { return taskDao.getAllUncompletedTasks(); }

    // AsyncTasks to prevent the UI thread from freezing, try/catch code in MainActivity to enable messages to the user
    // The class needs to specify <parameters, progress, return type>
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
