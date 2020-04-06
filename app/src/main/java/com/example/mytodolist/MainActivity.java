package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ToDoTask> tasks = new ArrayList<ToDoTask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        initTasks();
        initRecyclerView();
    }

    private void initTasks(){
        ToDoTask task1 = new ToDoTask("Call grandma");
        tasks.add(task1);
        ToDoTask task2 = new ToDoTask("Do the laundry");
        tasks.add(task2);
        ToDoTask task3 = new ToDoTask("Twerk my ass off awhdjkashdjksahjdks jkdahsjkdhsjhskadhkjs jksahdkjashdkahdjkashdjkshjkawdhjks");
        tasks.add(task3);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.TaskList);
        ToDoListAdapter toDoListAdapter = new ToDoListAdapter(tasks, this);

        recyclerView.setAdapter(toDoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
