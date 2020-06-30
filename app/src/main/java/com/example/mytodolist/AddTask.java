package com.example.mytodolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddTask extends AppCompatActivity {
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
        textInputTaskDetails = findViewById(R.id.text_input_task_details);
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
