package com.example.mytodolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    volatile ArrayList<ToDoTask> tasks = new ArrayList<ToDoTask>();
    //ToDoListAdapter toDoListAdapter;

    private ToDoTaskViewModel taskViewModel;
    //DatabaseHandler dbHandler;
    //Handler mainThreadHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState != null) {
            tasks = savedInstanceState.getParcelableArrayList("Tasks Array");
        } else {

            //taskViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ToDoTaskViewModel.class);
            //getTasksFromDB();
        }
        initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // saving the array of ToDoTask objects
        outState.putParcelableArrayList("Tasks Array",tasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTask:
                startAddTaskActivity();
                return true;
            case R.id.viewDoneTasks:
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTasks(){
        ToDoTask task1 = new ToDoTask("Call grandma", "On Viber this Tuesday.");
        tasks.add(task1);
        ToDoTask task2 = new ToDoTask("Do the laundry");
        tasks.add(task2);
        ToDoTask task3 = new ToDoTask("Workout");
        tasks.add(task3);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.TaskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ToDoListAdapter toDoListAdapter = new ToDoListAdapter();
        recyclerView.setAdapter(toDoListAdapter);

        taskViewModel = ViewModelProviders.of(this).get(ToDoTaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<ToDoTask>>() {
            @Override
            public void onChanged(@Nullable List<ToDoTask> tasks) {
                toDoListAdapter.setTasks(tasks);
            }
        });

        /*toDoListAdapter.setOnItemClickListener(new ToDoListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                CharSequence text = tasks.get(position).getTitle();
                Toast.makeText(getApplicationContext(), text + " Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnOptionsClick(int position) {
                CharSequence text = tasks.get(position).getTitle();
                Toast.makeText(getApplicationContext(), text + " Options Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        //toDoListAdapter.notifyDataSetChanged();
    }

    public void startAddTaskActivity() {
            Intent intent = new Intent(this, AddTask.class);
            startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // adding the new task to view
                ToDoTask newTask = data.getParcelableExtra("New Task");
                //tasks.add(newTask);
                //saveTaskToDB(newTask);

                //toDoListAdapter.notifyDataSetChanged();
                //toDoListAdapter.notifyItemInserted(toDoListAdapter.getItemCount() + 1);
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "New task not saved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*public void getTasksFromDB() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                Log.i("DB", "run: get from db START");
                dbHandler = new DatabaseHandler(getApplication());
                //tasks = dbHandler.getAllUncompletedTasks();

                Log.i("DB", "run: get from db END");
                Log.i("DB", "run: status of first " + String.valueOf(tasks.get(0).getIsDone()));
            }
        });
        try {
            t.start();
            t.join();
            mainThreadHandler.post(new Runnable() {
                public void run() {
                    Log.i("DB", "run: num stuff in tasks " + String.valueOf(tasks.size()));
                    toDoListAdapter.notifyDataSetChanged();
                }
            });
        }
        catch (Exception e) {
            final Exception exception = e;
            e.printStackTrace();
            mainThreadHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void saveTaskToDB(ToDoTask task) {
        final ToDoTask task1 = task;
        new Thread(new Runnable() {
            public void run() {
                dbHandler.insert(task1);
                Log.i("DB INSERT", task1.getTitle());
                final boolean isDone = task1.getIsDone();
                 Log.i("DB INSERT", String.valueOf(isDone));
            }
        }).start();
    }*/
}
