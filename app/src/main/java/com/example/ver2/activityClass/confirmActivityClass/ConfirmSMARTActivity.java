/*
    SMARTフレームワークを使用した目標の確認をするクラス
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
import com.example.ver2.dataClass.goalManagement.SMART;
import com.example.ver2.fragmentClass.confirmFragments.ConfirmSMARTEditFragment;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmSMARTViewModel;

public class ConfirmSMARTActivity extends AppCompatActivity {
    private TextView specificTextView;
    private TextView measurableTextView;
    private TextView achiveableTextView;
    private TextView relevantTextView;
    private TextView timeBoundTextView;
    private TextView goalTextView;
    private SMART smart;

    //EditFragmentとの情報を共有やUIの更新に使用
    ConfirmSMARTViewModel confirmSMARTViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_smart_scroll_layout);

        specificTextView = findViewById(R.id.confirm_smart_textView_Specific);
        measurableTextView = findViewById(R.id.confirm_smart_textView_Measurable);
        achiveableTextView = findViewById(R.id.confirm_smart_textView_Achievable);
        relevantTextView = findViewById(R.id.confirm_smart_textView_relevant);
        timeBoundTextView = findViewById(R.id.confirm_smart_textView_timeBound);
        goalTextView = findViewById(R.id.confirm_smart_textView_goal);

        confirmSMARTViewModel = new ViewModelProvider(ConfirmSMARTActivity.this).get(ConfirmSMARTViewModel.class);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("smart")) {
            smart = intent.getParcelableExtra("smart");
            if (smart != null) {
                //ViewModelのMutableLiveDataをアップデート
                confirmSMARTViewModel.updateSmart(smart);
            }
        }

        //UIがViewModelが保持するLiveDataが変更された際に通知され更新される
        confirmSMARTViewModel.getSmartLiveData().observe(this, smartLiveData -> {
            if (smartLiveData != null) {
                specificTextView.setText(smartLiveData.getSpecific());
                measurableTextView.setText(smartLiveData.getMeasurable());
                achiveableTextView.setText(smartLiveData.getAchievable());
                relevantTextView.setText(smartLiveData.getRelevant());
                timeBoundTextView.setText(smartLiveData.getTimeBound());
                goalTextView.setText(smartLiveData.getSmartGoal());
                //smartオブジェクトを更新
                this.smart = smartLiveData;
            }
        });

        //編集ボタン
        Button editButton = findViewById(R.id.confirm_smart_Edit_button);
        editButton.setOnClickListener(view -> {
            //ViewModelから値を引っ張ってくる
            SMART currentSMART = confirmSMARTViewModel.getSmartLiveData().getValue();
            if (currentSMART != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("smart", currentSMART);

                ConfirmSMARTEditFragment fragment = new ConfirmSMARTEditFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "ConfirmSMARTEditFragment");
            }
        });

        //次のActivityに遷移するボタン
        Button nextButton = findViewById(R.id.confirm_smart_nextButton);
        nextButton.setOnClickListener(view -> {
            Intent intent_next = new Intent(ConfirmSMARTActivity.this, ConfirmGoalActivity.class);
            intent_next.putExtra("smart", smart);
            startActivity(intent_next);
        });

        //前のActivityに戻るボタン
        Button backButton = findViewById(R.id.confirm_smart_backButton);
        backButton.setOnClickListener(view -> {
            Intent intent_before = new Intent(ConfirmSMARTActivity.this, ConfirmGoalListActivity.class);
            startActivity(intent_before);
        });
    }
}
