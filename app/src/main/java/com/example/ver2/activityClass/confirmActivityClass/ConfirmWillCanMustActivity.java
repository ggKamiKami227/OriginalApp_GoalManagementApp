/*
    WillCanMustフレームワークを使用した目標の確認をするクラス
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
import com.example.ver2.dataClass.goalManagement.WillCanMust;
import com.example.ver2.fragmentClass.confirmFragments.ConfirmWillCanMustEditFragment;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmWillCanMustViewModel;

public class ConfirmWillCanMustActivity extends AppCompatActivity {
    private TextView willTextView;
    private TextView canTextView;
    private TextView mustTextView;
    private TextView goalTextView;
    private WillCanMust wcm;

    //EditFragmentとの情報を共有やUIの更新に使用
    private ConfirmWillCanMustViewModel confirmWillCanMustViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_willcanmust_scroll_layout);

        willTextView = findViewById(R.id.confirm_WillCanMust_textView_Will);
        canTextView = findViewById(R.id.confirm_WillCanMust_textView_Can);
        mustTextView = findViewById(R.id.confirm_WillCanMust_textView_Must);
        goalTextView = findViewById(R.id.confirm_WillCanMust_textView_Goal);

        confirmWillCanMustViewModel = new ViewModelProvider(this).get(ConfirmWillCanMustViewModel.class);

        Intent intent = getIntent();
        //渡されたオブジェクトの確認とテキスト表示
        if (intent != null && intent.hasExtra("willCanMust")) {
            wcm = intent.getParcelableExtra("willCanMust");
            if (wcm != null) {
                //ViewModelのMutableLiveDataをアップデート
                confirmWillCanMustViewModel.updateWcm(wcm);
            }
        }

        //UIがViewModelが保持するLiveDataが変更された際に通知され更新される
        confirmWillCanMustViewModel.getWcmLiveData().observe(this, wcmLiveData -> {
            if (wcmLiveData != null) {
                willTextView.setText(wcmLiveData.getWill());
                canTextView.setText(wcmLiveData.getCan());
                mustTextView.setText(wcmLiveData.getMust());
                goalTextView.setText(wcmLiveData.getWcmGoal());
                //wcmオブジェクトを更新
                this.wcm = wcmLiveData;
            }
        });

        //編集ボタン
        Button editButton = findViewById(R.id.confirm_willCanMust_Edit_button);
        editButton.setOnClickListener(view -> {
            //ViewModelから値を引っ張ってくる
            WillCanMust currentWcm = confirmWillCanMustViewModel.getWcmLiveData().getValue();
            if (currentWcm != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("wcm", currentWcm);

                ConfirmWillCanMustEditFragment fragment = new ConfirmWillCanMustEditFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "ConfirmWillCanMustEditFragment");
            }
        });

        //次のActivityに遷移するボタン
        Button nextButton = findViewById(R.id.confirm_willCanMust_nextButton);
        nextButton.setOnClickListener(view -> {
            Intent intent_next = new Intent(ConfirmWillCanMustActivity.this, ConfirmGoalActivity.class);
            intent_next.putExtra("wcm", wcm);
            startActivity(intent_next);
        });

        //前のActivityに戻るボタン
        Button backButton = findViewById(R.id.confirm_willCanMust_backButton);
        backButton.setOnClickListener(view -> {
            Intent intent_before = new Intent(ConfirmWillCanMustActivity.this, ConfirmGoalListActivity.class);
            startActivity(intent_before);
        });
    }
}