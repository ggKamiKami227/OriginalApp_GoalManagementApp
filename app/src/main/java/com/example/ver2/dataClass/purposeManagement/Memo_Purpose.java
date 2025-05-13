package com.example.ver2.dataClass.purposeManagement;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.ver2.Converters;
import com.example.ver2.dataClass.Task;
import java.util.*;

@Entity(tableName = "memo_purposes")
@TypeConverters(Converters.class)
public class Memo_Purpose extends Purpose{
    @TypeConverters(Converters.class)
    private List<Task> tasks;
    private String memo; //いるかわからないけど一応

    public Memo_Purpose(String name, String description, Date createDate, Date startDate, Date finishDate,boolean state, PurposeType type, List<Task> tasks, String memo) {
        super(name, description, createDate, startDate, finishDate, state, type);
        this.tasks = tasks;
        this.memo = memo;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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
    public String getMemo(){
        return memo;
    }
    public void setMemo(String memo){
        this.memo = memo;
    }
}
