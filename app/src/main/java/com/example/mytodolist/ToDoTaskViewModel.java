package com.example.mytodolist;
import android.app.Application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
    public class ToDoTaskViewModel extends AndroidViewModel {
        private DatabaseHandler databaseHandler;
        public LiveData<List<ToDoTask>> allTasks;

        public ToDoTaskViewModel(@NonNull Application application) {
            super(application);
            databaseHandler = new DatabaseHandler(application);
            allTasks = databaseHandler.getAllUncompletedTasks();
        }
        public void insert(ToDoTask task) {
            databaseHandler.insert(task);
        }
        public void update(ToDoTask task) {
            databaseHandler.update(task);
        }
        public void delete(ToDoTask task) {
            databaseHandler.delete(task);
        }

        public LiveData<List<ToDoTask>> getAllTasks() {
            return allTasks;
        }

        public LiveData<List<ToDoTask>> getAllCompletedTasks() {
            this.allTasks = databaseHandler.getAllCompletedTasks();
            return allTasks;
        }

        public void switchToCompletedTasks() {
            /*List<ToDoTask> myButt = allTasks.getValue();
            allTasks = databaseHandler.getAllCompletedTasks();
            //List<ToDoTask> switchedTasks = databaseHandler.getAllCompletedTasks().getValue();
            //allTasks.getValue().clear();
            //allTasks.getValue().addAll(switchedTasks);
            List<ToDoTask> myAss = allTasks.getValue();*/

        }

        public LiveData<List<ToDoTask>> getAllUncompletedTasks() {
            this.allTasks = databaseHandler.getAllUncompletedTasks();
            return allTasks;
        }

        public ToDoTask getTaskById(int id_num) {
            return databaseHandler.getTaskById(id_num);
        }
    }
