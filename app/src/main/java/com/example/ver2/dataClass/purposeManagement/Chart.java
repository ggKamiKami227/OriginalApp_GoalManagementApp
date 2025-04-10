package com.example.ver2.dataClass.purposeManagement;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ver2.dataClass.Task;

import java.util.ArrayList;
import java.util.List;

public class Chart implements Parcelable {
    private int ID;
    private String goal;
    private List<Task> tasks;
    private boolean state;

    public Chart(int ID, String goal, List<Task> tasks, boolean state) {
        this.ID = ID;
        this.goal = goal;
        this.tasks = tasks;
        this.state = state;
    }

    public int getID() {
        return ID;
    }

    public String getGoal() {
        return goal;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public boolean isState() {
        return state;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        if (this.tasks != null) {
            this.tasks.remove(task);
        }
    }

    //Parcel　の実装
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(ID);
        dest.writeString(goal);
        dest.writeTypedList(tasks);
        dest.writeByte((byte) (state ? 1 : 0));
    }

    protected Chart(Parcel in){
        ID = in.readInt();
        goal = in.readString();
        tasks = in.createTypedArrayList(Task.CREATOR);
        state = in.readByte() != 0;
    }

    public static final Creator<Chart> CREATOR = new Creator<Chart>(){
        @Override
        public Chart createFromParcel(Parcel in){
            return new Chart(in);
        }

        @Override
        public Chart[] newArray(int size){
            return new Chart[size];
        }
    };

}
