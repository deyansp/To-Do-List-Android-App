package com.example.mytodolist;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// adapter for the MainActivity RecyclerView with the tasks
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder>{
    // stores the ToDoTask object that are displayed
    private List<ToDoTask> tasks = new ArrayList<>();

    // interface for OnClick listeners that can be implemented differently for each activity
    OnItemClickListener mListener;
    public interface OnItemClickListener {
        void OnItemClick(int position);
        void OnCheckboxClick(int position, boolean markAsDone);
        void OnOptionsClick(int position, String action);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // getting the layout for each recyclerview item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // called for each item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // ticking its checkbox if it has been marked as done
        if (tasks.get(position).getIsDone())
            holder.checkBox.setChecked(true);
        // setting the task name
        holder.mainText.setText(tasks.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // updates adapter with list of ToDoTasks, called when the ViewModel notices db changes
    public void setTasks(List<ToDoTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public ToDoTask getTaskAt(int position) {
        return tasks.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // variables for the Card elements
        CheckBox checkBox;
        TextView mainText;
        ImageButton optionsButton;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            mainText = itemView.findViewById(R.id.mainText);
            optionsButton = itemView.findViewById(R.id.optionsButton);
            layout = itemView.findViewById(R.id.mainLayout);

            // listener for general clicks that opens the Task details dialog
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        final int position = getAdapterPosition();
                        // making sure the item still exists
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.OnItemClick(position);
                        }
                    }
                }
            });
            // listener for clicks on the options icon to the right
            optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final int position = getAdapterPosition();
                        // making sure the item still exists
                        if (position != RecyclerView.NO_POSITION) {
                            //creating a popup menu
                            PopupMenu popup = new PopupMenu(v.getContext(), v);
                            //inflating menu from xml resource
                            popup.inflate(R.menu.task_options);
                            //adding click listener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.edit:
                                            mListener.OnOptionsClick(position, "edit");
                                            return true;
                                        case R.id.delete:
                                            mListener.OnOptionsClick(position, "delete");
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            //displaying the popup
                            popup.show();
                        }
                    }
                }
            });
            // listener for when tasks are marked as done or pending
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        // making sure the item still exists
                        if (position != RecyclerView.NO_POSITION) {
                            if (checkBox.isChecked())
                                mListener.OnCheckboxClick(position, true);
                            else
                                mListener.OnCheckboxClick(position, false);
                        }
                    }
                }
            });
        }
    }
}
