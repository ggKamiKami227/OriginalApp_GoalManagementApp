/*
    目標設定で使用するフレームワークを選択するActivity
*/

package com.example.ver2.activityClass.createActivityClass.createGoal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.activityClass.createActivityClass.CreateTopChooseActivity;

public class CreateGoalChooseFrameworkActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.create_goal_choose_framework);

        //自由記述形式
        Button memoButton =  findViewById(R.id.Memo_Goal_Button);
        memoButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateGoalChooseFrameworkActivity.this, MemoGoalActivity.class);
                startActivity(intent);
        });
        //SMARTフレームワーク
        Button smartButton =  findViewById(R.id.SMART_Button);
        smartButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateGoalChooseFrameworkActivity.this, SMARTActivity.class);
                startActivity(intent);
        });
        //WillCanMustフレームワーク
        Button wcmButton =  findViewById(R.id.WillCanMust_Button);
        wcmButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateGoalChooseFrameworkActivity.this, WillCanMustActivity.class);
                startActivity(intent);
        });
        //ベンチマーキングフレームワーク
        Button benchMarkingButton =  findViewById(R.id.Benchmarking_Button);
        benchMarkingButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateGoalChooseFrameworkActivity.this, BenchmarkingActivity.class);
                startActivity(intent);
        });

        //戻るボタン。目的か目標かを決めるActivityに遷移
        Button backButton = findViewById(R.id.create_goal_choose_frameWork_backButton);
        backButton.setOnClickListener( view -> {
                Intent intent = new Intent(CreateGoalChooseFrameworkActivity.this, CreateTopChooseActivity.class);
                startActivity(intent);
        });
    }
}
