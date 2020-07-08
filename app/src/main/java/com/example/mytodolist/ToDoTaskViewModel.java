package com.example.mytodolist;
import android.app.Application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

//public class ToDoTaskViewModel {
    public class ToDoTaskViewModel extends AndroidViewModel {
        private DatabaseHandler repository;
        private LiveData<List<ToDoTask>> allTasks;

        public ToDoTaskViewModel(@NonNull Application application) {
            super(application);
            repository = new DatabaseHandler(application);
            allTasks = repository.getAllTasks();
        }
        public void insert(ToDoTask task) {
            repository.insert(task);
        }
        public void update(ToDoTask task) {
            repository.update(task);
        }
        public void delete(ToDoTask task) {
            repository.delete(task);
        }

        public LiveData<List<ToDoTask>> getAllTasks() {
            return allTasks;
        }

        public LiveData<List<ToDoTask>> getAllCompletedTasks() {
            allTasks = repository.getAllCompletedTasks();
            return allTasks;
        }

        public LiveData<List<ToDoTask>> getAllUncompletedTasks() {
            allTasks = repository.getAllUncompletedTasks();
            return allTasks;
        }

        public ToDoTask getTaskById(int id_num) {
            return repository.getTaskById(id_num);
        }
    }
//}
