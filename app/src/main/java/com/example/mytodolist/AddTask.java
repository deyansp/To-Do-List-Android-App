package com.example.mytodolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextInputLayout textInputTaskName;
    private TextInputLayout textInputTaskDate;
    private TextInputLayout textInputTaskDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Add New Task");
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        textInputTaskName = findViewById(R.id.text_input_task_name);
        textInputTaskDate = findViewById(R.id.text_input_task_date);
        /*findViewById(R.id.text_input_edit_text_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });*/
        textInputTaskDetails = findViewById(R.id.text_input_task_details);
    }

    public void openSelectDateDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Objects.requireNonNull(textInputTaskDate.getEditText()).setText(selectedDate);
    }

    private boolean validateTitle() {
        String title = Objects.requireNonNull(textInputTaskName.getEditText()).getText().toString().trim();
        if (title.isEmpty()) {
            textInputTaskName.setError("Field can't be empty");
            return false;
        } else if (title.length() > 60) {
            textInputTaskName.setError("Title too long");
            return false;
        } else {
            textInputTaskName.setError(null);
            return true;
        }
    }

    private boolean validateDetails() {
        String details = Objects.requireNonNull(textInputTaskDetails.getEditText()).getText().toString().trim();
        if (details.length() > 600) {
            textInputTaskDetails.setError("Text too long");
            return false;
        } else {
            textInputTaskDetails.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateTitle() | !validateDetails()) {
            return;
        }

        String title = Objects.requireNonNull(textInputTaskName.getEditText()).getText().toString().trim();
        ToDoTask newTask = new ToDoTask(title);

        String details = Objects.requireNonNull(textInputTaskDetails.getEditText()).getText().toString().trim();
        if (!details.isEmpty()) {
            newTask.setDetails(details);
        }
    }
}
