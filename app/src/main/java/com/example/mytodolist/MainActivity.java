package com.example.mytodolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.sax.TextElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_TASK_REQ_CODE = 1;
    public static final int EDIT_TASK_REQ_CODE = 2;

    private ToDoTaskViewModel taskViewModel;
    //private ToDoListAdapter toDoListAdapter;
    private boolean showingCompletedTasks = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState != null)
            showingCompletedTasks = savedInstanceState.getBoolean("Tasks Displayed");
        initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saving the array of ToDoTask objects
        outState.putBoolean("Tasks Displayed", showingCompletedTasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        //if (showingCompletedTasks)

        return true;
    }

    //@Override
    public boolean onOptionsItemSelected(MenuItem item, final ToDoTaskViewModel taskViewModel) {
        switch (item.getItemId()) {
            case R.id.addTask:
                startAddTaskActivity();
                return true;
            case R.id.viewDoneTasks:
                if (!showingCompletedTasks) {
                    item.setIcon(R.drawable.ic_pending_tasks);
                    taskViewModel.switchToCompletedTasks();
                    showingCompletedTasks = true;
                } else {
                    item.setIcon(R.drawable.ic_done);
                    taskViewModel.getAllUncompletedTasks();
                    showingCompletedTasks = false;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.TaskList);
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

        // left and right swiping gestures for editing and deleting tasks
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    final ToDoTask tempTask = toDoListAdapter.getTaskAt(position);
                    deleteTask(toDoListAdapter.getTaskAt(position));
                    Snackbar.make(recyclerView, "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addTask(tempTask);
                                }
                            }).show();
                }
                if (direction == ItemTouchHelper.LEFT) {
                    toDoListAdapter.notifyItemChanged(position);
                    //Log.i("RESULT", "RECYCLER VIEW: " + toDoListAdapter.getTaskAt(position).getId());
                    startEditTaskActivity(toDoListAdapter.getTaskAt(position));
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .addSwipeRightLabel("Delete")
                        .setSwipeRightLabelColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark))
                        .addSwipeLeftActionIcon(R.drawable.ic_edit)
                        .addSwipeLeftLabel("Edit")
                        .setSwipeLeftLabelColor(ContextCompat.getColor(MainActivity.this, R.color.white))
                        .create()
                        .decorate();
            }
        }).attachToRecyclerView(recyclerView);

        toDoListAdapter.setOnItemClickListener(new ToDoListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                TaskInfoDialog dialog = TaskInfoDialog.newInstance(toDoListAdapter.getTaskAt(position));
                dialog.show(getSupportFragmentManager(), "Task Info Dialog");
            }

            @Override
            public void OnCheckboxClick(int position, boolean markAsDone) {
                if (markAsDone) {
                    toDoListAdapter.getTaskAt(position).setIsDone(true);
                    Toast.makeText(getApplicationContext(), "Marked DONE", Toast.LENGTH_SHORT).show();
                } else {
                    toDoListAdapter.getTaskAt(position).setIsDone(false);
                    Toast.makeText(getApplicationContext(), "Marked NOT DONE", Toast.LENGTH_SHORT).show();
                }
                updateTask(toDoListAdapter.getTaskAt(position));
                toDoListAdapter.notifyItemChanged(position);
            }
        });

        //toDoListAdapter.notifyDataSetChanged();
    }

    /*public void openTaskInfoDialog(ToDoTask task) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.task_info_dialog,null);

        dialog.setView(view)
                .setTitle("Task Details")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        TextView taskTitle = view.findViewById(R.id.task_title);
        taskTitle.setText(task.getTitle());

        TextView taskDetails = view.findViewById(R.id.task_details);
        if (task.getDetails() != null)
            taskDetails.setText(task.getDetails());
        else
            taskDetails.setText("None");

        TextView taskDate = view.findViewById(R.id.task_date);
        if (task.getDeadline() != null)
            taskDate.setText(task.getDeadline());
        else
            taskDate.setText("None");

        dialog.create();
        dialog.show();
    }*/

    public void addTask(ToDoTask task) {
        try {
            taskViewModel.insert(task);
            Toast.makeText(getApplicationContext(), "Added new task", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateTask(ToDoTask task) {
        try {
            taskViewModel.update(task);
            Toast.makeText(getApplicationContext(), "Updated task", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteTask(ToDoTask task) {
        try {
            taskViewModel.delete(task);
            Toast.makeText(getApplicationContext(), "Deleted task", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void startAddTaskActivity() {
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        startActivityForResult(intent, ADD_TASK_REQ_CODE);
    }

    public void startEditTaskActivity(ToDoTask task) {
        Intent intentEdit = new Intent(this, AddEditTaskActivity.class);
        intentEdit.putExtra("Edit Task", task);
        startActivityForResult(intentEdit, EDIT_TASK_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQ_CODE && resultCode == RESULT_OK) {
            // adding the new task to view
            ToDoTask newTask = data.getParcelableExtra("New Task");
            addTask(newTask);
                // updating existing task
            } else if (requestCode == EDIT_TASK_REQ_CODE && resultCode == RESULT_OK) {
                ToDoTask newTask = data.getParcelableExtra("Edit Task");
                updateTask(newTask);
            } else {
                Toast.makeText(getApplicationContext(), "Activity task cancelled", Toast.LENGTH_SHORT).show();
            }
    }

}
