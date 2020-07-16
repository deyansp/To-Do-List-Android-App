package com.example.mytodolist;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// class that holds the task details and is used by Room for the db table and queries
// implements Parcelable for easy passage of data between activities

@Entity(tableName = "to_do_tasks")
public class ToDoTask implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String details;
    private boolean isDone;
    private String deadline;

    public ToDoTask(String title) {
        this.title = title;
        this.isDone = false;
    }
    @Ignore
    public ToDoTask(String title, String details) {
        this.title = title;
        this.details = details;
        this.isDone = false;
    }
    @Ignore
    public ToDoTask(String title, String details, String date) {
        this.title = title;
        this.details = details;
        this.deadline = date;
        this.isDone = false;
    }

    // Parcelable method
    @Ignore
    protected ToDoTask(Parcel in) {
        id = in.readInt();
        title = in.readString();
        details = in.readString();
        isDone = in.readByte() != 0;
        deadline = in.readString();
    }

    // Parcelable method
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

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean status) {
        isDone = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(details);
        parcel.writeByte((byte) (isDone ? 1 : 0));
        parcel.writeString(deadline);
    }
}
