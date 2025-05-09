/*
    Goalの内容を編集するFragment
    ConfirmGoalViewModelのGoalオブジェクトをアップデートすることで変更を確定させる
 */
package com.example.ver2.fragmentClass.confirmFragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ver2.R;
import com.example.ver2.recyclerViewClass.RecyclerViewTaskListAdapter;
import com.example.ver2.dataClass.Task;
import com.example.ver2.dataClass.goalManagement.Goal;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmGoalViewModel;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmTaskEditViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfirmGoalEditFragment extends DialogFragment {
    private EditText goalNameEditText;
    private EditText goalDescriptionEditText;
    private DatePicker startDatePicker;
    private DatePicker finishDatePicker;

    private Goal goal;
    private List<Task> taskList;

    private Date startDate;
    private Date finishDate;

    //Activity側に変更されたことを通知するためのViewModel
    ConfirmGoalViewModel confirmGoalViewModel;

    //TaskFragmentと情報を共有、更新するときに使用
    ConfirmTaskEditViewModel confirmTaskEditViewModel;

    //TaskリストをUIでリスト化して表示するため
    RecyclerViewTaskListAdapter adapter;
    RecyclerView taskRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //save_goal_layoutを流用する
        View view = inflater.inflate(R.layout.save_goal_layout, container, false);

        //Activityと同じGoalオブジェクトを監視する
        confirmGoalViewModel = new ViewModelProvider(requireActivity()).get(ConfirmGoalViewModel.class);
        //LiveDataを監視
        confirmGoalViewModel.getGoalLiveData().observe(this, goalLiveData -> {
            //アップデートする
            //非同期処理だからTaskListとのアップデートをしっかり管理しないとTaskListがNullのままGoalが上書きされる可能性がある
            goal = goalLiveData;
            taskList = goal.getTasks();
            setFragmentComponent(view);
            //最初に中身がnullの状態のときのみ実行 ここでやらないとTaskListがNullになってGoalからTaskListが消える
            if (confirmTaskEditViewModel.getTaskListLiveData().getValue() == null) {
                confirmTaskEditViewModel.updateTaskList(taskList); // 最初のみ更新
            }
        });
        //confirmTaskEditFragmentとのやり取りで使用
        confirmTaskEditViewModel = new ViewModelProvider(requireActivity()).get(ConfirmTaskEditViewModel.class);
        //LiveDataを監視、TaskListに変更が加えられた際にgoalオブジェクトにもその値を変更し通知する
        confirmTaskEditViewModel.getTaskListLiveData().observe(this, taskListLiveData -> {
            //TaskListをアップデートして、GoalもViewModel側のやつをアップデートする
            if (goal != null && taskList != null) {
                goal.setTasks(taskListLiveData);
                //ここでこのクラスのtaskListもアップデートされる
                confirmGoalViewModel.updateGoal(goal);
            }

        });

        goalNameEditText = view.findViewById(R.id.goalNameEditText);
        goalDescriptionEditText = view.findViewById(R.id.goalDescriptionEditText);
        startDatePicker = view.findViewById(R.id.startDatePicker);
        finishDatePicker = view.findViewById(R.id.finishDatePicker);

        //セーブボタンの処理
        Button saveButton = view.findViewById(R.id.saveGoalButton);
        saveButton.setOnClickListener(view1 -> {
            //goalがここでNullになる可能性あり
            if (goal != null) {
                //内容を取得して更新
                goal.setName(goalNameEditText.getText().toString());
                goal.setDescription(goalDescriptionEditText.getText().toString());
                goal.setStartDate(startDate);
                goal.setFinishDate(finishDate);
            }
            //ViewModelのMutableLiveDataを変更してActivityのUIやオブジェクトに変更されたことを通知
            confirmGoalViewModel.updateGoal(goal);

            //Fragmentを終了する
            dismiss();
        });

        //前の画面（元のActivity）に戻るボタン
        Button backButton = view.findViewById(R.id.backButton_saveGoallayout);
        backButton.setOnClickListener(view_before -> dismiss());

        return view;
    }

    //UIを設定
    private void setFragmentComponent(View view) {

        goalNameEditText.setText(goal.getName());
        goalDescriptionEditText.setText(goal.getDescription());

        // カレンダーインスタンスの再利用
        Calendar calendar = Calendar.getInstance();

        if (goal.getStartDate() != null) {
            calendar.setTime(goal.getStartDate());
            startDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            startDate = goal.getStartDate();
        }

        if (goal.getFinishDate() != null) {
            calendar.setTime(goal.getFinishDate());
            finishDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            finishDate = goal.getFinishDate();
        }


        taskRecyclerView = view.findViewById(R.id.SaveGoal_taskList);
        //ここで使われているGoalオブジェクトはConfirmGoalViewModelから引っ張ってきたもの
        //ここに削除メソッドとかも含まれていて、TaskListがViewModelのものと同じでないと同期できない
        adapter = new RecyclerViewTaskListAdapter(taskList);
        adapter.setOnTaskRemoveListener(this::removeTask);
        taskRecyclerView.setAdapter(adapter);

        //RecyclerViewをタップしたときにTaskEditFragmentを表示するようにする
        //タスクを編集する場合
        adapter.setOnItemClickListener(this::openChangeTaskFragment);

        // DatePicker のリスナー設定 (API レベル 26 以上)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDatePicker.setOnDateChangedListener((datePickerView, year, monthOfYear, dayOfMonth) -> {
                calendar.set(year, monthOfYear, dayOfMonth);
                startDate = calendar.getTime();
            });

            finishDatePicker.setOnDateChangedListener((datePickerView, year, monthOfYear, dayOfMonth) -> {
                calendar.set(year, monthOfYear, dayOfMonth);
                finishDate = calendar.getTime();
            });
        }
    }

    //これもSaveGoalActivityから持ってきたやつ
    //新しく作る場合
    private void openNewTaskFragment() {
        if (isAdded() && getActivity() != null) {
            ConfirmTaskEditFragment fragment = new ConfirmTaskEditFragment();
            //FragmentからFragmentを生成してるからgetActivity()が必要になるんだと思う
            fragment.show(getActivity().getSupportFragmentManager(), "ConfirmTaskEditFragment");
        }
    }

    //現在のものを編集する場合
    private void openChangeTaskFragment(int id) {
        //id(RecyclerViewでタップしたところのposition)の編集
        Bundle bundle = new Bundle();
        bundle.putParcelable("task", findTaskByID(id));
        if (isAdded() && getActivity() != null) {
            ConfirmTaskEditFragment fragment = new ConfirmTaskEditFragment();
            fragment.setArguments(bundle);
            fragment.show(getActivity().getSupportFragmentManager(), "ConfirmTaskEditFragment");
        }
    }

    //タスクを探すメソッド
    private Task findTaskByID(int id) {
        for (Task task : taskList) {
            if (task.getID() == id) {
                return task;
            }
        }
        return null;
    }

    //RecyclerView(TaskListの)での削除ボタンのこと
    public void removeTask(int position) {
        List<Task> currentList = confirmTaskEditViewModel.getTaskListLiveData().getValue();

        if (currentList != null && position >= 0 && position < currentList.size()) {
            Task taskToRemove = currentList.get(position);
            confirmTaskEditViewModel.removeTask(taskToRemove); // ViewModelを通じて削除
            adapter.notifyItemRemoved(position); // RecyclerView に通知
        }
    }

}
