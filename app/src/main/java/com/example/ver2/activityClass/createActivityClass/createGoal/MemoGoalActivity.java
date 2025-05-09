/*
    自由記述形式を利用した目標設定を行うActivity
    このActivityの後はSaveGoalActivityで目標を保存する（目標の名前、詳細、作成日、開始日、終了日、タスクを入力するActivity）
 */

package com.example.ver2.activityClass.createActivityClass.createGoal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;

import com.example.ver2.fragmentClass.BackConfirmListener;
import com.example.ver2.R;
import com.example.ver2.dataClass.goalManagement.Memo_Goal;

public class MemoGoalActivity extends AppCompatActivity implements BackConfirmListener {

    private EditText memoEditText;
    private Memo_Goal memoGoal;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.memo_goal_1);

        memoEditText = findViewById(R.id.memo_goal_1_editText);

        //SaveGoalActivityから戻ってきた際、前に入力した情報を取得する --> 目標設定中はその情報をこのクラスとSaveGoalActivityクラスで保持する
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("memo_goal")) {
            memoGoal = intent.getParcelableExtra("memo_goal");
            if (memoGoal != null) {
                memoEditText.setText(memoGoal.getMemo());
            }
        }

        //Memo_GoalオブジェクトをSaveGoalActivityに送り、Activity遷移を行う
        Button saveButton = findViewById(R.id.memo_goal_1_saveButton);
        saveButton.setOnClickListener(view -> {
                String memo = memoEditText.getText().toString();
                //始めの場合はここでインスタンス化する。二回目以降はMemo_Goalオブジェクトが保持してあるので、それを上書きする
                if (memoGoal == null) {
                    memoGoal = new Memo_Goal(memo);
                } else {
                    memoGoal.setMemo(memo);
                }
                //Intentを作成し、Memo_Goalオブジェクトをセット
                Intent intent_next = new Intent(MemoGoalActivity.this, SaveGoalActivity.class);
                intent_next.putExtra("memo_goal", memoGoal);
                //次のActivityを開始
                startActivity(intent_next);
        });

        //何もしないで前のActivityに遷移
        Button backButton = findViewById(R.id.memo_goal_1_backButton);
        backButton.setOnClickListener(view -> {
                Intent intent_before = new Intent(MemoGoalActivity.this, CreateGoalChooseFrameworkActivity.class);
                startActivity(intent_before);
        });
    }

    //Fragmentを表示して戻るときに警告っぽいのを出す
    @Override
    public void onPositiveButtonClicked() {
        Intent intent = new Intent(this, CreateGoalChooseFrameworkActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNegativeButtonClicked() {
        //何もしない
    }
}
