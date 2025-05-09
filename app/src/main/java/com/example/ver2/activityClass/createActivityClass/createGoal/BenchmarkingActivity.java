/*
    ベンチマーキングフレームワークを利用した目標設定を行うActivity
    このActivityの後はSaveGoalActivityで目標を保存する（目標の名前、詳細、作成日、開始日、終了日、タスクを入力するActivity）
 */

package com.example.ver2.activityClass.createActivityClass.createGoal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.goalManagement.Benchmarking;

public class BenchmarkingActivity extends AppCompatActivity{
    private EditText goalEditText;
    private EditText targetEditText;
    private EditText benchMarkEditText;
    private EditText compareEditText;
    private Benchmarking benchmarking;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.benchmarking_scroll_layout);

        goalEditText = findViewById(R.id.benchmarking_2_editText);
        targetEditText = findViewById(R.id.benchmarking_3_editText);
        benchMarkEditText = findViewById(R.id.benchmarking_4_editText);
        compareEditText = findViewById(R.id.benchmarking_5_editText);

        //SaveGoalActivityから戻ってきた際、前に入力した情報を取得する --> 目標設定中はその情報をこのクラスとSaveGoalActivityクラスで保持する
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("benchmarking")) {
            benchmarking = intent.getParcelableExtra("benchmarking"); // "benchmarking" という Key で WillCanMust オブジェクトを取得
            if (benchmarking != null) {
                // Benchmarking オブジェクトが存在する場合、EditText に値を設定
                goalEditText.setText(benchmarking.getInitialGoal());
                targetEditText.setText(benchmarking.getTarget());
                benchMarkEditText.setText(benchmarking.getBenchMark());
                compareEditText.setText(benchmarking.getComparison());
            }
        }

        //BenchmarkingオブジェクトをSaveGoalActivityに送り、Activity遷移を行う
        Button saveButton = findViewById(R.id.benchmarking_saveButton);
        saveButton.setOnClickListener(view ->  {
                String goal = goalEditText.getText().toString();
                String target = targetEditText.getText().toString();
                String benchMark = benchMarkEditText.getText().toString();
                String compare = compareEditText.getText().toString();

                //始めの場合はここでインスタンス化する。二回目以降はBenchmarkingオブジェクトが保持してあるので、それを上書きする
                if (benchmarking == null) {
                    benchmarking = new Benchmarking(goal, target, benchMark, compare);
                } else {
                    benchmarking.setInitialGoal(goal);
                    benchmarking.setTarget(target);
                    benchmarking.setBenchMark(benchMark);
                    benchmarking.setComparison(compare);
                }

                Intent intent_next = new Intent(BenchmarkingActivity.this, SaveGoalActivity.class);
                intent_next.putExtra("benchmarking", benchmarking);
                startActivity(intent_next);
        });

        //何もしないで前のActivityに遷移
        Button backButton = findViewById(R.id.benchmarking_backButton);
        backButton.setOnClickListener(view -> {
                Intent intent_before = new Intent(BenchmarkingActivity.this, CreateGoalChooseFrameworkActivity.class);
                startActivity(intent_before);
        });

    }
}
