/*
    WillCanMustフレームワークを利用した目標設定を行うActivity
    このActivityの後はSaveGoalActivityで目標を保存する（目標の名前、詳細、作成日、開始日、終了日、タスクを入力するActivity）
 */

package com.example.ver2.activityClass.createActivityClass.createGoal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.goalManagement.WillCanMust;

public class WillCanMustActivity extends AppCompatActivity {
    private EditText willEditText;
    private EditText canEditText;
    private EditText mustEditText;
    private EditText goalEditText;

    private WillCanMust willCanMust;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.willcanmust_scroll_layout);

        willEditText = findViewById(R.id.WillCanMust_editText_Will);
        canEditText = findViewById(R.id.WillCanMust_editText_Can);
        mustEditText = findViewById(R.id.WillCanMust_editText_Must);
        goalEditText = findViewById(R.id.WillCanMust_editText_Goal);

        //SaveGoalActivityから戻ってきた際、前に入力した情報を取得する --> 目標設定中はその情報をこのクラスとSaveGoalActivityクラスで保持する
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("willCanMust")) {
            willCanMust = intent.getParcelableExtra("willCanMust"); // "WillCanMust" という Key で WillCanMust オブジェクトを取得
            if (willCanMust != null) {
                // WillCanMust オブジェクトが存在する場合、EditText に値を設定
                willEditText.setText(willCanMust.getWill());
                canEditText.setText(willCanMust.getCan());
                mustEditText.setText(willCanMust.getMust());
                goalEditText.setText(willCanMust.getWcmGoal());
            }
        }

        //WillCanMustオブジェクトをSaveGoalActivityに送り、Activity遷移を行う
        Button saveButton = findViewById(R.id.willCanMust_saveButton);
        saveButton.setOnClickListener(view -> {
            String will = willEditText.getText().toString();
            String can = canEditText.getText().toString();
            String must = mustEditText.getText().toString();
            String goal = goalEditText.getText().toString();

            //始めの場合はここでインスタンス化する。二回目以降はWillCanMustオブジェクトが保持してあるので、それを上書きする
            if (willCanMust == null) {
                willCanMust = new WillCanMust(will, can, must, goal);
            } else {
                willCanMust.setWill(will);
                willCanMust.setCan(can);
                willCanMust.setMust(must);
                willCanMust.setWcmGoal(goal);
            }

            Intent intent_next = new Intent(WillCanMustActivity.this, SaveGoalActivity.class);
            intent_next.putExtra("willCanMust", willCanMust);
            startActivity(intent_next);
        });

        //何もしないで前のActivityに遷移
        Button backButton = findViewById(R.id.willCanMust_backButton);
        backButton.setOnClickListener(view -> {
            Intent intent_before = new Intent(WillCanMustActivity.this, CreateGoalChooseFrameworkActivity.class);
            startActivity(intent_before);
        });
    }
}
