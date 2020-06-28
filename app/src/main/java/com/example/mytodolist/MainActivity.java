package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    private void initTasks(){
        ToDoTask task1 = new ToDoTask("Call grandma", "On Viber this Tuesday.");
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

        toDoListAdapter.setOnItemClickListener(new ToDoListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                CharSequence text = tasks.get(position).title;
                Toast.makeText(getApplicationContext(), text + " Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnOptionsClick(int position) {
                CharSequence text = tasks.get(position).title;
                Toast.makeText(getApplicationContext(), text + " Options Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
