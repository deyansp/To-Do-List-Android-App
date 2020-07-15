package com.example.mytodolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;

public class AddEditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextInputLayout textInputTaskName;
    private TextInputLayout textInputTaskDate;
    private TextInputLayout textInputTaskDetails;

    // stores the user's selected due date
    private Calendar calendar;
    // used for checking and storing intent extras
    private Intent parentIntent;
    private ToDoTask editTask;
    // stores the new task details
    private String title;
    private String details;
    private String date;

    // used for scheduling reminder notification on the selected date
    NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        textInputTaskName = findViewById(R.id.text_input_task_name);
        textInputTaskDate = findViewById(R.id.text_input_task_date);
        textInputTaskDetails = findViewById(R.id.text_input_task_details);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        notificationHandler = new NotificationHandler(this);

        parentIntent = getIntent();
        // checking if a task will be edited or if a new one will be created
        // and adjusting the UI accordingly
        if (parentIntent.hasExtra("Edit Task")) {
            myToolbar.setTitle("Edit Task");
            editTask = parentIntent.getParcelableExtra("Edit Task");
            // setting the existing details to the input fields
            textInputTaskName.getEditText().setText(editTask.getTitle());

            if (editTask.getDeadline() != null)
                textInputTaskDate.getEditText().setText(editTask.getDeadline());

        if (editTask.getDetails() != null)
            textInputTaskDetails.getEditText().setText(editTask.getDetails());
        }
        else
            myToolbar.setTitle("Add New Task");

        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the back button
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);
    }

    // creating a DatePicker popup window
    public void openSelectDateDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // when the user sets the due date
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // storing date into Calendar variable
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        // formatting the selected date and updating the input field
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        textInputTaskDate.getEditText().setText(selectedDate);
    }

    private boolean validateTitle() {
        title = textInputTaskName.getEditText().getText().toString().trim();
        if (title.isEmpty()) {
            textInputTaskName.setError("Field can't be empty");
            return false;
        } else if (title.length() > 60) {
            textInputTaskName.setError("Title too long");
            return false;
        } else {
            // removing any previous errors
            textInputTaskName.setError(null);
            return true;
        }
    }

    private boolean validateDetails() {
        details = textInputTaskDetails.getEditText().getText().toString().trim();

        if (details.length() > 600) {
            textInputTaskDetails.setError("Text too long");
            return false;
        } else {
            // removing any previous errors
            textInputTaskDetails.setError(null);
            return true;
        }
    }

    // method called by the Save button
    public void sendTaskToMainActivity(View v) {
        // checking for input errors
        if (!validateTitle() | !validateDetails()) {
            return;
        }
        // storing the input in a new ToDoTask object
        ToDoTask newTask = new ToDoTask(title);

        if (!details.isEmpty()) {
            newTask.setDetails(details);
        }

        // date range is limited by the DatePickerFragment so no validation is necessary
        date = textInputTaskDate.getEditText().getText().toString().trim();

        if (!date.isEmpty()) {
            newTask.setDeadline(date);
            // scheduling reminder if an existing task's date was changed or if we're adding a new task
            if (editTask == null || !newTask.getDeadline().equals(editTask.getDeadline()))
                notificationHandler.scheduleNotification(calendar, newTask);
        }

        // returning result back to main activity
        if (parentIntent.hasExtra("Edit Task")) {
            // returning task id to update the correct task
            newTask.setId(editTask.getId());
            Intent intentResult = new Intent();
            intentResult.putExtra("Edit Task", newTask);
            setResult(RESULT_OK, intentResult);
        } else {
            // sending new ToDoTask object to main activity for db storage
            Intent intentResult = new Intent();
            intentResult.putExtra("New Task", newTask);
            setResult(RESULT_OK, intentResult);
        }
        finish();
    }

    // when the cancel or back buttons are clicked
    public void cancelActivity(View view) {
        Intent intentResult = new Intent();
        setResult(RESULT_CANCELED, intentResult);
        finish();
    }
}
