/*
    ConfirmGoalActivityで生成されるConfirmGoalEditFragmentのタスク追加、編集で使用するViewModel
 */

package com.example.ver2.fragmentClass.viewModels.goalViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ver2.dataClass.Task;

import java.util.ArrayList;
import java.util.List;

public class ConfirmTaskEditViewModel extends ViewModel {
    //MutableLiveData:LiveDataのサブクラスで、値を変更することのできる変数
    //final:これは参照先を表すことで、MutableLiveDataが保持するtaskListオブジェクトの内容は変更可能。つまり、finalが保証しているのは、taskListLiveDataが常に同じMutableLiveDataインスタンスを参照し続けることだけ。
    private final MutableLiveData<List<Task>> taskListLiveData = new MutableLiveData<>();

    //LiveDataで返すことで、外部から値を変更できないようにしている。
    public LiveData<List<Task>> getTaskListLiveData(){
        return taskListLiveData;
    }

    //taskListLiveDataの値を更新するメソッドで、これを呼び出し更新することで、監視しているUIにデータ変更が通知される
    public void updateTaskList(List<Task> taskList){
        taskListLiveData.setValue(taskList);
    }

    //タスクを追加するメソッド。IDをここで設定するようにした。2025/03/22
    public void addTask(Task newTask) {
        List<Task> currentList = taskListLiveData.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        int maxId = 0;
        for (Task task : currentList) {
            if (task.getID() > maxId) {
                maxId = task.getID();
            }
        }
        newTask.setID(maxId + 1);
        currentList.add(newTask);
        taskListLiveData.setValue(currentList);
    }

    //taskを削除するめそっど
    public void removeTask(Task taskToRemove) {
        List<Task> currentList = taskListLiveData.getValue();
        if (currentList != null) {
            currentList.remove(taskToRemove);
            taskListLiveData.setValue(currentList);
        }
    }

    //すでにあるTaskを編集した際のメソッド
    public void changeTask(Task task) {
        List<Task> taskList = taskListLiveData.getValue();
        if(taskList != null) {
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getID() == task.getID()) {
                    taskList.set(i, task); // リスト内のオブジェクトを新しいTaskオブジェクトで置き換える
                    break; // IDが一致するオブジェクトは1つしかないため、ループを抜ける
                }
            }
        }
        taskListLiveData.setValue(taskList);
    }
}
