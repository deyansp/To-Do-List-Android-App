package com.example.mytodolist;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ToDoTask.class}, version = 1, exportSchema = false)
public abstract class ToDoTaskDatabase extends RoomDatabase {

    private static ToDoTaskDatabase instance;

    public abstract ToDoTaskDao taskDao();

    // synchronized to prevent db race conditions between AsyncTasks
    public static synchronized ToDoTaskDatabase getInstance(Context context) {
        // create db if it doesn't exist
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ToDoTaskDatabase.class, "tasks_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        // otherwise return its instance
        return instance;
    }

    // add two sample tasks when the database is first created
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // async task to insert data without freezing the UI thread
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ToDoTaskDao taskDao;
        private PopulateDbAsyncTask(ToDoTaskDatabase db) {
            taskDao = db.taskDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new ToDoTask("Water the plants", "Only the succulents!"));
            taskDao.insert(new ToDoTask("Submit coursework"));
            return null;
        }
    }
}
