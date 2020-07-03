package com.example.mytodolist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class ToDoTask implements Parcelable {
    String title;
    String details;
    boolean isDone;
    String deadline;

    public ToDoTask(String title) {
        this.title = title;
        this.isDone = false;
    }

    public ToDoTask(String title, String details) {
        this.title = title;
        this.details = details;
        this.isDone = false;
    }

    public ToDoTask(String title, String details, String date) {
        this.title = title;
        this.details = details;
        this.deadline = date;
        this.isDone = false;
    }

    protected ToDoTask(Parcel in) {
        title = in.readString();
        details = in.readString();
        isDone = in.readByte() != 0;
        deadline = in.readString();
    }

    public static final Creator<ToDoTask> CREATOR = new Creator<ToDoTask>() {
        @Override
        public ToDoTask createFromParcel(Parcel in) {
            return new ToDoTask(in);
        }

        @Override
        public ToDoTask[] newArray(int size) {
            return new ToDoTask[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean status) {
        isDone = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(details);
        parcel.writeByte((byte) (isDone ? 1 : 0));
        parcel.writeString(deadline);
    }
}
