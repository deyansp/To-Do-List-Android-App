package com.example.mytodolist;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

// implements the ViewModel class which can update the UI automatically on db changes
// the ViewModel is preserved during most lifecycle changes so there is less db access overall
public class ToDoTaskViewModel extends AndroidViewModel {
    // used to perform db queries
    private DatabaseHandler databaseHandler;
    // observes this variable for changes and updates the MainActivity RecyclerView accordingly
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

    public LiveData<List<ToDoTask>> getAllUncompletedTasks() {
        this.allTasks = databaseHandler.getAllUncompletedTasks();
        return allTasks;
    }
}
