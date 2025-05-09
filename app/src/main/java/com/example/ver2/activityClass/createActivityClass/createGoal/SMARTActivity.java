/*
    SMARTフレームワークを利用した目標設定を行うActivity
    このActivityの後はSaveGoalActivityで目標を保存する（目標の名前、詳細、作成日、開始日、終了日、タスクを入力するActivity）
 */

package com.example.ver2.activityClass.createActivityClass.createGoal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.goalManagement.SMART;

public class SMARTActivity extends AppCompatActivity {
    private EditText specificEditText;
    private EditText measurableEditText;
    private EditText achievableEditText;
    private EditText relevantEditText;
    private EditText timeBoundEditText;
    private EditText goalEditText;

    private SMART smart;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.smart_scroll_layout);


        specificEditText = findViewById(R.id.smart_scrollLayout_editText_Specific);
        measurableEditText = findViewById(R.id.smart_scrollLayout_editText_Measurable);
        achievableEditText = findViewById(R.id.smart_scrollLayout_editText_Achievable);
        relevantEditText = findViewById(R.id.smart_scrollLayout_editText_relevant);
        timeBoundEditText = findViewById(R.id.smart_scrollLayout_editText_timeBound);
        goalEditText = findViewById(R.id.smart_scrollLayout_editText_goal);

        //SaveGoalActivityから戻ってきた際、前に入力した情報を取得する --> 目標設定中はその情報をこのクラスとSaveGoalActivityクラスで保持する
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("smart")) {
            smart = intent.getParcelableExtra("smart"); // "WillCanMust" という Key で WillCanMust オブジェクトを取得
            if (smart != null) {
                // WillCanMust オブジェクトが存在する場合、EditText に値を設定
                specificEditText.setText(smart.getSpecific());
                measurableEditText.setText(smart.getMeasurable());
                achievableEditText.setText(smart.getAchievable());
                relevantEditText.setText(smart.getRelevant());
                timeBoundEditText.setText(smart.getTimeBound());
                goalEditText.setText(smart.getSmartGoal());
            }
        }

        //SMARTオブジェクトをSaveGoalActivityに送り、Activity遷移を行う
        Button saveButton = findViewById(R.id.smart_saveButton);
        saveButton.setOnClickListener(view -> {
                String specific = specificEditText.getText().toString();
                String measurable = measurableEditText.getText().toString();
                String achievable = achievableEditText.getText().toString();
                String relevant = relevantEditText.getText().toString();
                String timeBound = timeBoundEditText.getText().toString();
                String goal = goalEditText.getText().toString();

                //始めの場合はここでインスタンス化する。二回目以降はSMARTオブジェクトが保持してあるので、それを上書きする
                if (smart == null) {
                    smart = new SMART(specific,measurable,achievable,relevant,timeBound,goal);
                } else {
                    smart.setSpecific(specific);
                    smart.setMeasurable(measurable);
                    smart.setAchievable(achievable);
                    smart.setRelevant(relevant);
                    smart.setTimeBound(timeBound);
                    smart.setSmartGoal(goal);
                }

                Intent intent_next = new Intent(SMARTActivity.this, SaveGoalActivity.class);
                intent_next.putExtra("smart", smart);
                startActivity(intent_next);
        });

        //何もしないで前のActivityに遷移
        Button backButton = findViewById(R.id.smart_backButton);
        backButton.setOnClickListener(view -> {
                Intent intent_before = new Intent(SMARTActivity.this, CreateGoalChooseFrameworkActivity.class);
                startActivity(intent_before);
        });

    }
}
