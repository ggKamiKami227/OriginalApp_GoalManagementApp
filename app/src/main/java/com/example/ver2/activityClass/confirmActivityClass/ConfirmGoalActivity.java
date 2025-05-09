/*
    作成した目標のGoalクラスの内容を確認するのと編集を行うことのできるクラス
    編集ボタンを押すことで、変種可能になる
 */
package com.example.ver2.activityClass.confirmActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ver2.Converters;
import com.example.ver2.R;
import com.example.ver2.recyclerViewClass.RecyclerViewTaskListAdapter;
import com.example.ver2.dataClass.GoalDataViewModel;
import com.example.ver2.dataClass.Task;
import com.example.ver2.dataClass.goalManagement.Benchmarking;
import com.example.ver2.dataClass.goalManagement.Goal;
import com.example.ver2.dataClass.goalManagement.GoalType;
import com.example.ver2.dataClass.goalManagement.Memo_Goal;
import com.example.ver2.dataClass.goalManagement.SMART;
import com.example.ver2.dataClass.goalManagement.WillCanMust;
import com.example.ver2.fragmentClass.confirmFragments.ConfirmGoalEditFragment;
import com.example.ver2.fragmentClass.confirmFragments.ConfirmTaskFragment;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmGoalViewModel;

import java.util.ArrayList;
import java.util.List;

public class ConfirmGoalActivity extends AppCompatActivity {
    //データ保存用のViewModel
    private GoalDataViewModel goalDataViewModel;

    private WillCanMust wcm;
    private SMART smart;
    private Benchmarking benchmarking;
    private Memo_Goal memo;

    private Goal goal;
    private List<Task> taskList = new ArrayList<>(); // タスクリスト

    private TextView goalNameTextView;
    private TextView goalDescriptionTextView;
    private CalendarView startDateCalendar;
    private CalendarView finishDateCalendar;

    RecyclerView taskRecyclerView;
    RecyclerViewTaskListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_goal_layout);

        goalNameTextView = findViewById(R.id.confirmGoalLayout_goalName);
        goalDescriptionTextView = findViewById(R.id.confirmGoalLayout_goalDescription);
        startDateCalendar = findViewById(R.id.confirmGoalLayout_startDateCalendar);
        finishDateCalendar = findViewById(R.id.confirmGoalLayout_finishDateCalendar);

        Intent intent = getIntent();

        //ViewModelを生成
        goalDataViewModel = new ViewModelProvider(ConfirmGoalActivity.this).get(GoalDataViewModel.class);
        //EditFragmentとの情報を共有やUIの更新に使用 Localで宣言した（Warningがあったから）
        ConfirmGoalViewModel confirmGoalViewModel = new ViewModelProvider(ConfirmGoalActivity.this).get(ConfirmGoalViewModel.class);

        //intentで前のActivityからそれぞれのフレームワークの情報を取得する。（１つだけnullじゃない）
        if (intent != null) {
            if (intent.hasExtra("wcm")) {
                wcm = intent.getParcelableExtra("wcm");
                //戻ったときの挙動：すでにオブジェクトがある場合はということ
                if (wcm != null && wcm.isGoalExist()) {
                    goal = new Goal(wcm.getName(), wcm.getDescription(), wcm.getCreateDate(), wcm.getStartDate(), wcm.getFinishDate(), wcm.isState(), wcm.getTasks(), wcm.getType());
                } else {
                    goal = new Goal(null, null, null, null, null, false, null, GoalType.WILL_CAN_MUST);
                }
            } else if (intent.hasExtra("smart")) {
                smart = intent.getParcelableExtra("smart");
                if (smart != null && smart.isGoalExist()) {
                    goal = new Goal(smart.getName(), smart.getDescription(), smart.getCreateDate(), smart.getStartDate(), smart.getFinishDate(), smart.isState(), smart.getTasks(), smart.getType());
                } else {
                    goal = new Goal(null, null, null, null, null, false, null, GoalType.SMART);
                }
            } else if (intent.hasExtra("benchmarking")) {
                benchmarking = intent.getParcelableExtra("benchmarking");
                if (benchmarking != null && benchmarking.isGoalExist()) {
                    goal = new Goal(benchmarking.getName(), benchmarking.getDescription(), benchmarking.getCreateDate(), benchmarking.getStartDate(), benchmarking.getFinishDate(), benchmarking.isState(), benchmarking.getTasks(), benchmarking.getType());
                } else {
                    goal = new Goal(null, null, null, null, null, false, null, GoalType.BENCHMARKING);
                }
            } else if (intent.hasExtra("memo_goal")) {
                memo = intent.getParcelableExtra("memo_goal");
                if (memo != null && memo.isGoalExist()) {
                    goal = new Goal(memo.getName(), memo.getDescription(), memo.getCreateDate(), memo.getStartDate(), memo.getFinishDate(), memo.isState(), memo.getTasks(), memo.getType());
                } else {
                    goal = new Goal(null, null, null, null, null, false, null, GoalType.MEMO_GOAL);
                }
            }
            //ViewModelのGoalオブジェクトをアップデート
            confirmGoalViewModel.updateGoal(goal);
        }

        //UIがViewModelが保持するLiveDataが変更された際に通知され更新される
        confirmGoalViewModel.getGoalLiveData().observe(this, goalLiveData -> {
            //goalオブジェクトを更新
            goal = goalLiveData;
            setActivityComponent();
        });

        //オブジェクトを更新して前のActivityに遷移する
        Button backButton = findViewById(R.id.confirmGoalLayout_backButton);
        backButton.setOnClickListener(view  -> {
            //オブジェクトに保存 ここで前のActivityに遷移
            if (wcm != null) {
                wcm.updateGoal(goal);
                Intent intent_before = new Intent(ConfirmGoalActivity.this, ConfirmWillCanMustActivity.class);
                intent_before.putExtra("willCanMust", wcm);
                startActivity(intent_before);
            } else if (benchmarking != null) {
                benchmarking.updateGoal(goal);
                Intent intent_before = new Intent(ConfirmGoalActivity.this, ConfirmBenchmarkingActivity.class);
                intent_before.putExtra("benchmarking", benchmarking);
                startActivity(intent_before);
            } else if (smart != null) {
                smart.updateGoal(goal);
                Intent intent_before = new Intent(ConfirmGoalActivity.this, ConfirmSMARTActivity.class);
                intent_before.putExtra("smart", smart);
                startActivity(intent_before);
            } else if (memo != null) {
                memo.updateGoal(goal);
                Intent intent_before = new Intent(ConfirmGoalActivity.this, ConfirmMemoGoalActivity.class);
                intent_before.putExtra("memo_goal", memo);
                startActivity(intent_before);
            }
        });

        //次のActivity（ConfirmGoalListActivityに戻る）ボタン
        Button confirmGoalButton = findViewById(R.id.confirmGoalLayout_confirmGoalButton);
        confirmGoalButton.setOnClickListener(view -> {
            //ViewModelのMutableLiveDataが変更された際に通知され(observe)goalインスタンスは最新の状態であると考えられる
            //編集していない場合でもデータベースに保存するメソッドをしておく
            //データベースに保存
            if (wcm != null) {
                wcm.updateGoal(goal);
                goalDataViewModel.updateWCM(wcm);
            } else if (benchmarking != null) {
                benchmarking.updateGoal(goal);
                goalDataViewModel.updateBenchmarking(benchmarking);
            } else if (smart != null) {
                smart.updateGoal(goal);
                goalDataViewModel.updateSMART(smart);
            } else if (memo != null) {
                memo.updateGoal(goal);
                goalDataViewModel.updateMemo_Goal(memo);
            }

            Intent intent_next = new Intent(ConfirmGoalActivity.this, ConfirmGoalListActivity.class);
            startActivity(intent_next);
        });

        //編集ボタン
        Button editButton = findViewById(R.id.confirm_goal_Edit_button);
        editButton.setOnClickListener(view -> {
                ConfirmGoalEditFragment fragment = new ConfirmGoalEditFragment();
                fragment.show(getSupportFragmentManager(), "ConfirmGoalEditFragment");
        });
    }

    //UIの初期化、または変更が通知された際にアップデートするメソッド
    private void setActivityComponent() {
        // Goal オブジェクトの値を UI に設定
        //中身がnullでも、初期値で設定されるらしい
        goalNameTextView.setText(goal.getName());
        goalDescriptionTextView.setText(goal.getDescription());
        if (goal.getStartDate() != null) {
            startDateCalendar.setDate(Converters.dateToTimestamp(goal.getStartDate()));
        }

        if (goal.getFinishDate() != null) {
            finishDateCalendar.setDate(Converters.dateToTimestamp(goal.getFinishDate()));
        }

        // RecyclerView の設定
        taskRecyclerView = findViewById(R.id.confirmGoalLayout_taskList);
        taskList = new ArrayList<>(); // taskList を空の ArrayList で初期化

        //taskListの更新
        if (goal.getTasks() != null) { // goal.getTasks() が null でない場合
            taskList = goal.getTasks(); // Goal オブジェクトの TaskList を taskList に代入
        }

        adapter = new RecyclerViewTaskListAdapter(taskList);
        taskRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::openConfirmTaskFragment);
    }

    //タスクを確認するためのFragment
    private void openConfirmTaskFragment(int id) {
        Task confirmTask = findTaskByID(id);
        //Fragmentに初期値を設定する
        Bundle bundle = new Bundle();
        bundle.putParcelable("task", confirmTask);
        //Fragmentを開始
        ConfirmTaskFragment confirmTaskFragment = new ConfirmTaskFragment();
        confirmTaskFragment.setArguments(bundle);
        confirmTaskFragment.show(getSupportFragmentManager(), "ConfirmTaskFragment");
    }

    private Task findTaskByID(int id) {
        for (Task task : taskList) {
            if (task.getID() == id) {
                return task;
            }
        }
        return null;
    }
}
