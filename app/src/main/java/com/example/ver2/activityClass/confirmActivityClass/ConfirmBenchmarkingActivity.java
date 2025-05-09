/*
    ベンチマーキングフレームワークを使用した目標の確認をするクラス
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
import com.example.ver2.dataClass.goalManagement.Benchmarking;
import com.example.ver2.fragmentClass.confirmFragments.ConfirmBenchmarkingEditFragment;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmBenchmarkingViewModel;

public class ConfirmBenchmarkingActivity extends AppCompatActivity {
    private TextView goalTextView;
    private TextView targetTextView;
    private TextView benchMarkTextView;
    private TextView compareTextView;
    private Benchmarking benchmarking;

    //EditFragmentとの情報を共有やUIの更新に使用
    private ConfirmBenchmarkingViewModel confirmBenchmarkingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_benchmarking_scroll_layout);

        goalTextView = findViewById(R.id.confirm_benchmarking_GoalTextView);
        targetTextView = findViewById(R.id.confirm_benchmarking_TargetTextView);
        benchMarkTextView = findViewById(R.id.confirm_benchmarking_BenchmarkTextView);
        compareTextView = findViewById(R.id.confirm_benchmarking_ComparisonTextView);

        confirmBenchmarkingViewModel = new ViewModelProvider(ConfirmBenchmarkingActivity.this).get(ConfirmBenchmarkingViewModel.class);

        Intent intent = getIntent();
        //渡されたオブジェクトの確認とテキスト表示
        if (intent != null && intent.hasExtra("benchmarking")) {
            benchmarking = intent.getParcelableExtra("benchmarking"); // "benchmarking" という Key で benchmarkingオブジェクトを取得（データベースから（おそらく前のActivityで取得））
            if (benchmarking != null) {
                //ViewModelのMutableLiveDataをアップデート
                confirmBenchmarkingViewModel.updateBenchmarking(benchmarking);
            }
        }

        //UIがViewModelが保持するLiveDataが変更された際に通知され更新される
        confirmBenchmarkingViewModel.getBenchmarkingLiveData().observe(this, benchmarking -> {
            if (benchmarking != null) {
                goalTextView.setText(benchmarking.getInitialGoal());
                targetTextView.setText(benchmarking.getTarget());
                benchMarkTextView.setText(benchmarking.getBenchMark());
                compareTextView.setText(benchmarking.getComparison());
                //benchmarkingオブジェクトを更新
                this.benchmarking = benchmarking;
            }
        });

        //編集ボタン
        Button editButton = findViewById(R.id.confirm_benchmarking_Edit_button);
        editButton.setOnClickListener(view -> {
            //ViewModelから値を引っ張ってくる
            Benchmarking currentBenchmarking = confirmBenchmarkingViewModel.getBenchmarkingLiveData().getValue();
            if (currentBenchmarking != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("benchmarking", currentBenchmarking);

                ConfirmBenchmarkingEditFragment fragment = new ConfirmBenchmarkingEditFragment();
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "ConfirmBenchmarkingEditFragment");
            }
        });

        //次のActivityに遷移するボタン
        Button nextButton = findViewById(R.id.confirm_benchmarking_nextButton);
        nextButton.setOnClickListener(view -> {
            Intent intent_next = new Intent(ConfirmBenchmarkingActivity.this, ConfirmGoalActivity.class);
            intent_next.putExtra("benchmarking", benchmarking);
            startActivity(intent_next);
        });

        //前のActivityに戻るボタン
        Button backButton = findViewById(R.id.confirm_benchmarking_backButton);
        backButton.setOnClickListener(view -> {
            Intent intent_before = new Intent(ConfirmBenchmarkingActivity.this, ConfirmGoalListActivity.class);
            startActivity(intent_before);
        });
    }
}
