/*
    ベンチマーキングフレームワークを使用した目標の編集をするFragment
 */

package com.example.ver2.fragmentClass.confirmFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ver2.R;
import com.example.ver2.dataClass.goalManagement.Benchmarking;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmBenchmarkingViewModel;

public class ConfirmBenchmarkingEditFragment extends DialogFragment {
    private EditText goalEditText;
    private EditText targetEditText;
    private EditText benchMarkEditText;
    private EditText compareEditText;
    private Benchmarking benchmarking;

    //LiveDataを使用したViewModel。これのBenchmarkingオブジェクトをアップデートすることで、Activity側のUIもアップデートされる
    ConfirmBenchmarkingViewModel confirmBenchmarkingViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //目標作成時に用いたLayoutを流用
        View view = inflater.inflate(R.layout.benchmarking_scroll_layout, container, false);

        // ViewModel の取得
        confirmBenchmarkingViewModel = new ViewModelProvider(requireActivity()).get(ConfirmBenchmarkingViewModel.class);

        goalEditText = view.findViewById(R.id.benchmarking_2_editText);
        targetEditText = view.findViewById(R.id.benchmarking_3_editText);
        benchMarkEditText = view.findViewById(R.id.benchmarking_4_editText);
        compareEditText = view.findViewById(R.id.benchmarking_5_editText);

        // Activity から送られてくる情報をセット
        Bundle bundle = getArguments();
        if (bundle != null) {
            benchmarking = bundle.getParcelable("benchmarking");
            if (benchmarking != null) {
                goalEditText.setText(benchmarking.getInitialGoal());
                targetEditText.setText(benchmarking.getTarget());
                benchMarkEditText.setText(benchmarking.getBenchMark());
                compareEditText.setText(benchmarking.getComparison());
            }
        }

        // セーブボタンの処理
        Button saveButton = view.findViewById(R.id.benchmarking_saveButton);
        saveButton.setOnClickListener(view1 -> {
            // 入力内容を取得して更新
            benchmarking.setInitialGoal(goalEditText.getText().toString());
            benchmarking.setTarget(targetEditText.getText().toString());
            benchmarking.setBenchMark(benchMarkEditText.getText().toString());
            benchmarking.setComparison(compareEditText.getText().toString());

            // ViewModel に変更後のデータを保存
            confirmBenchmarkingViewModel.updateBenchmarking(benchmarking);

            // Fragment を閉じる
            dismiss();
        });

        // 戻るボタンの処理
        Button backButton = view.findViewById(R.id.benchmarking_backButton);
        backButton.setOnClickListener(view1 -> dismiss());

        return view;
    }
}
