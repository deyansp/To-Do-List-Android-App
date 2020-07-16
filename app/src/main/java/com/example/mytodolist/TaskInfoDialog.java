package com.example.mytodolist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

// opens the Task Details in the MainActivity on task clicks
public class TaskInfoDialog extends AppCompatDialogFragment {

    // passes the ToDoTask object to the fragment as the constructor needs to remain empty to override
    public static TaskInfoDialog newInstance(ToDoTask taskToDisplay) {
        TaskInfoDialog dialog = new TaskInfoDialog();

        Bundle args = new Bundle();
        args.putParcelable("task", taskToDisplay);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieving task to display
        ToDoTask task = getArguments().getParcelable("task");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task_info_dialog,null);

        dialog.setView(view)
                .setTitle("Task Details")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing to do, closes the window by default
                    }
                });

        // setting the relevant task fields for displaying
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

        return dialog.create();
    }
}
