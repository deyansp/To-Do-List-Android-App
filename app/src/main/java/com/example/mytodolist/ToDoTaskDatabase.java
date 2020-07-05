package com.example.mytodolist;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import static androidx.room.Room.*;

@Database(entities = ToDoTask.class, version = 1, exportSchema = false)
public abstract class ToDoTaskDatabase extends RoomDatabase {

    private static ToDoTaskDatabase instance;

    public abstract ToDoTaskDao taskDao();

    public static synchronized ToDoTaskDatabase getInstance(Context context) {
        // create db if it doesn't exist
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ToDoTaskDatabase.class, "tasks_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        // otherwise return its instance
        return instance;
    }
}
