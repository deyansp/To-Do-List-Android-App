package com.example.mytodolist;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    // codes used for Activity results
    public static final int ADD_TASK_REQ_CODE = 1;
    public static final int EDIT_TASK_REQ_CODE = 2;

    // observes data and automatically updates the UI, also persists through most lifecycle events
    private ToDoTaskViewModel taskViewModel;
    // boolean for indication which tasks to retrieve and display
    private boolean showingCompletedTasks;
    // used for scheduling and cancelling reminder notifications
    private NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationHandler = new NotificationHandler(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // maintaining which tasks to display after screen rotation etc
        if (savedInstanceState != null)
            showingCompletedTasks = savedInstanceState.getBoolean("Tasks Displayed");
        else
            showingCompletedTasks = false;

        // creates the RecyclerView and sets the listeners
        initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saving the boolean which indicates which tasks to show
        outState.putBoolean("Tasks Displayed", showingCompletedTasks);
    }

    // toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu to add the items to the toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar_menu, menu);
        // change the toolbar icon if completed tasks are shown
        if (showingCompletedTasks)
            menu.findItem(R.id.viewDoneTasks).setIcon(R.drawable.ic_pending_tasks);
        return true;
    }

    // handles toolbar menu button clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTask:
                startAddTaskActivity();
                return true;
            case R.id.viewDoneTasks:
                Toast.makeText(getApplicationContext(), "UI updates on screen rotation (issue to fix)", Toast.LENGTH_SHORT).show();
                if (!showingCompletedTasks) {
                    item.setIcon(R.drawable.ic_pending_tasks);
                    taskViewModel.getAllCompletedTasks();
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

    // three methods for db access, wrapped in try/catch to prevent crashes
    public void addTask(ToDoTask task) {
        try {
            taskViewModel.insert(task);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateTask(ToDoTask task) {
        try {
            taskViewModel.update(task);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteTask(ToDoTask task) {
        try {
            notificationHandler.cancelScheduledNotification(task);
            taskViewModel.delete(task);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void startAddTaskActivity() {
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        startActivityForResult(intent, ADD_TASK_REQ_CODE);
    }

    public void startEditTaskActivity(ToDoTask task) {
        // storing which task to edit in the intent
        Intent intentEdit = new Intent(this, AddEditTaskActivity.class);
        intentEdit.putExtra("Edit Task", task);
        startActivityForResult(intentEdit, EDIT_TASK_REQ_CODE);
    }

    // modifies the database with the returned ToDoTask object, depending on the result status
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
            Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        // setting the layout
        final RecyclerView recyclerView = findViewById(R.id.TaskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adapter to set the data and onclick listeners
        final ToDoListAdapter toDoListAdapter = new ToDoListAdapter();
        recyclerView.setAdapter(toDoListAdapter);

        // ViewModel observes db for changes and updates the RecyclerView accordingly
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
                // if swiping to the right, delete the task
                if (direction == ItemTouchHelper.RIGHT) {
                    final ToDoTask tempTask = toDoListAdapter.getTaskAt(position);
                    deleteTask(toDoListAdapter.getTaskAt(position));
                    // option to undo the deletion
                    Snackbar.make(recyclerView, "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addTask(tempTask);
                                }
                            }).show();
                }
                // if swiping to the left, edit the task
                if (direction == ItemTouchHelper.LEFT) {
                    toDoListAdapter.notifyItemChanged(position);
                    startEditTaskActivity(toDoListAdapter.getTaskAt(position));
                }
            }

            // code for displaying the relevant background colour, icon, and labels during swiping
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                    float dY, int actionState, boolean isCurrentlyActive)
            {
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

        // handles listener for general clicks on a card in the RecyclerView
        toDoListAdapter.setOnItemClickListener(new ToDoListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                // displays the Task Details dialog
                TaskInfoDialog dialog = TaskInfoDialog.newInstance(toDoListAdapter.getTaskAt(position));
                dialog.show(getSupportFragmentManager(), "Task Info Dialog");
            }

            // handles listener for the more options (three dots) icon
            @Override
            public void OnOptionsClick(int position, String action) {
                if (action.equals("edit")) {
                    startEditTaskActivity(toDoListAdapter.getTaskAt(position));
                } else if (action.equals("delete")) {
                    final ToDoTask tempTask = toDoListAdapter.getTaskAt(position);
                    deleteTask(toDoListAdapter.getTaskAt(position));
                    // snackbar with the option of undoing the deletion
                    Snackbar.make(recyclerView, "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addTask(tempTask);
                                }
                            }).show();
                }
            }

            // marks tasks as done or not done, depending on the checkbox state
            @Override
            public void OnCheckboxClick(int position, boolean markAsDone) {
                if (markAsDone) {
                    // cancelling future reminder since the task is complete
                    notificationHandler.cancelScheduledNotification(toDoListAdapter.getTaskAt(position));

                    toDoListAdapter.getTaskAt(position).setIsDone(true);
                    Toast.makeText(getApplicationContext(), "Marked as Done", Toast.LENGTH_SHORT).show();
                } else {
                    toDoListAdapter.getTaskAt(position).setIsDone(false);
                    Toast.makeText(getApplicationContext(), "Marked as Not Done", Toast.LENGTH_SHORT).show();
                }
                // storing the change in the db and updating the UI
                updateTask(toDoListAdapter.getTaskAt(position));
                toDoListAdapter.notifyItemChanged(position);
            }
        });
    }
}
