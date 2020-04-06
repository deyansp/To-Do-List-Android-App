package com.example.mytodolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder>{
    ArrayList<ToDoTask> tasks;
    Context context;

    public ToDoListAdapter(ArrayList<ToDoTask> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
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
        holder.mainText.setText(tasks.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
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
        }
    }
}
