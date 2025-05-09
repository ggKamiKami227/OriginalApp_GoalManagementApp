/*
    自由記述形式を使用した目標の編集をするFragment
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
import com.example.ver2.dataClass.goalManagement.Memo_Goal;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmMemoGoalViewModel;

public class ConfirmMemoGoalEditFragment extends DialogFragment {
    private EditText memoEditText;

    Memo_Goal memo;

    //LiveDataを使用したViewModel。これのMemo_Goalオブジェクトをアップデートすることで、Activity側のUIもアップデートされる
    ConfirmMemoGoalViewModel confirmMemoGoalViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //目標作成時に用いたLayoutを流用
        View view = inflater.inflate(R.layout.memo_goal_1, container, false);

        //ViewModelの取得
        confirmMemoGoalViewModel = new ViewModelProvider(requireActivity()).get(ConfirmMemoGoalViewModel.class);

        memoEditText = view.findViewById(R.id.memo_goal_1_editText);

        //Activityから送られてくる情報をセット
        Bundle bundle = getArguments();
        if (bundle != null) {
            memo = bundle.getParcelable("memo_goal");
            if (memo != null) {
                memoEditText.setText(memo.getMemo());
            }
        }

        //セーブボタンの処理
        Button saveButton = view.findViewById(R.id.memo_goal_1_saveButton);
        saveButton.setOnClickListener(view1 -> {
            //入力内容を取得して更新
            memo.setMemo(memoEditText.getText().toString());

            //ViewModelに変更後のデータを保存
            confirmMemoGoalViewModel.updateMemoGoal(memo);

            //Fragmentを閉じる
            dismiss();
        });

        //戻るボタンの処理
        Button backButton = view.findViewById(R.id.memo_goal_1_backButton);
        backButton.setOnClickListener(view1 -> dismiss());

        return view;
    }
}
