package com.example.ver2.fragmentClass.viewModels.goalViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ver2.dataClass.Task;
import com.example.ver2.dataClass.goalManagement.Goal;

import java.util.ArrayList;
import java.util.List;

public class SaveGoalFragmentViewModel extends ViewModel {
    //MutableLiveData:LiveDataのサブクラスで、値を変更することのできる変数
    //final:参照先を表している
    private final MutableLiveData<Goal> goalMutableLiveData = new MutableLiveData<>();

    //LiveData型で返すことで、外部から値を変更できないようにしている。
    public LiveData<Goal> getGoalLiveData() {
        return goalMutableLiveData;
    }

    //goalMutableLiveDataの値を更新するメソッドで、これを呼び出し更新することで、監視しているUIにエータ変更が通知される
    public void updateGoal(Goal updatedGoal) {
        goalMutableLiveData.setValue(updatedGoal);
    }

    //Taskを追加するメソッド。IDをここで設定するようにした
    public void addTask(Task task) {
        Goal currentGoal = goalMutableLiveData.getValue();
        if (currentGoal != null) {
            List<Task> taskList = currentGoal.getTasks();
            if (taskList == null) {
                taskList = new ArrayList<>();
            }
            int maxID = 0;
            for (Task currentTask : taskList) {
                if (currentTask.getID() > maxID) {
                    maxID = currentTask.getID();
                }
            }
            task.setID(maxID + 1);
            taskList.add(task);
            currentGoal.setTasks(taskList);
            goalMutableLiveData.setValue(currentGoal);
            Log.d("GoalTaskList", goalMutableLiveData.getValue().getTasks().toString());
        }
    }

    //すでにあるタスクを編集した場合のメソッド
    public void changeTask(Task task) {
        Goal currentGoal = goalMutableLiveData.getValue();
        if (currentGoal != null) {
            List<Task> taskList = currentGoal.getTasks();
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getID() == task.getID()) {
                    taskList.set(i, task); // リスト内のオブジェクトを新しいTaskオブジェクトで置き換える
                    break; // IDが一致するオブジェクトは1つしかないため、ループを抜ける
                }
            }
            currentGoal.setTasks(taskList);
            goalMutableLiveData.setValue(currentGoal);
        }
    }

    //taskを削除するメソッド
    public void removeTask(Task task) {
        Goal currentGoal = goalMutableLiveData.getValue();
        if (currentGoal != null) {
            List<Task> taskList = currentGoal.getTasks();
            taskList.remove(task);
            currentGoal.setTasks(taskList);
            goalMutableLiveData.setValue(currentGoal);
        }
    }
}
