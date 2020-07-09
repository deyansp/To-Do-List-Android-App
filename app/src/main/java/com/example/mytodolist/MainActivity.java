package com.example.mytodolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    public static final int ADD_NOTE_REQ_CODE = 1;
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

        // left and right swiping gestures for deleting tasks
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(toDoListAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


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
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                // adding the new task to view
                ToDoTask newTask = data.getParcelableExtra("New Task");
                taskViewModel.insert(newTask);
                Toast.makeText(getApplicationContext(), "Added new task", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "New task not saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
}
