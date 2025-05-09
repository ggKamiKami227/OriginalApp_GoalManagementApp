/*
    自由記述形式を使用した目標の確認をするクラス
    編集ボタンを押すことで、編集可能になる（FragmentDialogで表示）
 */

package com.example.ver2.activityClass.confirmActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ver2.R;
import com.example.ver2.dataClass.goalManagement.Memo_Goal;
import com.example.ver2.fragmentClass.confirmFragments.ConfirmMemoGoalEditFragment;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmMemoGoalViewModel;

public class ConfirmMemoGoalActivity extends AppCompatActivity {
    private TextView memoGoalTextView;
    private Memo_Goal memo;

    //EditFragmentとの情報を共有やUIの更新に使用
    ConfirmMemoGoalViewModel confirmMemoGoalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_memo_goal_layout);

        memoGoalTextView = findViewById(R.id.confirm_memo_goal_textView);

        confirmMemoGoalViewModel = new ViewModelProvider(this).get(ConfirmMemoGoalViewModel.class);

        //渡されたオブジェクトの確認とテキスト表示
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("memo_goal")) {
            memo = intent.getParcelableExtra("memo_goal");
            if (memo != null) {
                confirmMemoGoalViewModel.updateMemoGoal(memo);
            }
        }

        confirmMemoGoalViewModel.getMemoGoalLiveData().observe(this, memoGoal -> {
            if (memoGoal != null) {
                memoGoalTextView.setText(memoGoal.getMemo());
                //memoオブジェクトを更新
                this.memo = memoGoal;
            }
        });

        //編集ボタン
        Button editButton = findViewById(R.id.confirm_memo_goal_Edit_button);
        editButton.setOnClickListener(view -> {
            //ViewModelから値を引っ張ってくる
            Memo_Goal currentMemo = confirmMemoGoalViewModel.getMemoGoalLiveData().getValue();
            if (currentMemo != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("memo_goal", currentMemo);

                ConfirmMemoGoalEditFragment fragment = new ConfirmMemoGoalEditFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "ConfirmMemoGoalEditFragment");
            }
        });

        //次のActivityに遷移するボタン
        Button nextButton = findViewById(R.id.confirm_memo_goal_nextButton);
        nextButton.setOnClickListener(view -> {
            Intent intent_next = new Intent(ConfirmMemoGoalActivity.this, ConfirmGoalActivity.class);
            intent_next.putExtra("memo_goal", memo);
            startActivity(intent_next);
        });

        //前のActivityに戻るボタン
        Button backButton = findViewById(R.id.confirm_memo_goal_backButton);
        backButton.setOnClickListener(view -> {
            Intent intent_before = new Intent(ConfirmMemoGoalActivity.this, ConfirmGoalListActivity.class);
            startActivity(intent_before);
        });
    }
}
