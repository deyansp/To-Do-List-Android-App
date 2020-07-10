package com.example.mytodolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder>{
    private List<ToDoTask> tasks = new ArrayList<>();
    Context context;
    OnItemClickListener mListener;


    /*public ToDoListAdapter(ArrayList<ToDoTask> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }*/

    public interface OnItemClickListener {
        void OnItemClick(int position);
        void OnCheckboxClick(int position, boolean markAsDone);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    // updates adapter with list of ToDoTasks
    public void setTasks(List<ToDoTask> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public ToDoTask getTaskAt(int position) {
        return tasks.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        // making sure the item still exists
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.OnItemClick(position);
                        }
                    }
                }
            });

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
