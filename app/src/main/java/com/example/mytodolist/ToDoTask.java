package com.example.mytodolist;

import java.util.Calendar;

public class ToDoTask {
    CharSequence title;
    CharSequence details;
    boolean isDone;
    Calendar deadline;

    public ToDoTask(String title) {
        this.title = title;
    }

    public ToDoTask(CharSequence title, CharSequence details) {
        this.title = title;
        this.details = details;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CharSequence getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

}
