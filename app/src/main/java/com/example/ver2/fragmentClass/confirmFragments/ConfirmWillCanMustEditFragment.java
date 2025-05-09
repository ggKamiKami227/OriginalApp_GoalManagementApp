/*
    WillCanMustフレームワークを使用した目標の編集をするFragment
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
import com.example.ver2.dataClass.goalManagement.WillCanMust;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmWillCanMustViewModel;

public class ConfirmWillCanMustEditFragment extends DialogFragment {
    private EditText willEditText;
    private EditText canEditText;
    private EditText mustEditText;
    private EditText goalEditText;
    private WillCanMust wcm;

    //LiveDataを使用したViewModel。これのWillCanMustオブジェクトをアップデートすることで、Activity側のUIもアップデートされる。
    ConfirmWillCanMustViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //目標作成時に用いたLayoutを流用
        View view = inflater.inflate(R.layout.willcanmust_scroll_layout, container, false);

        //ViewModelの取得
        viewModel = new ViewModelProvider(requireActivity()).get(ConfirmWillCanMustViewModel.class);

        willEditText = view.findViewById(R.id.WillCanMust_editText_Will);
        canEditText = view.findViewById(R.id.WillCanMust_editText_Can);
        mustEditText = view.findViewById(R.id.WillCanMust_editText_Must);
        goalEditText = view.findViewById(R.id.WillCanMust_editText_Goal);

        //Activityから送られてくる情報をセット
        Bundle bundle = getArguments();
        if (bundle != null) {
            wcm = bundle.getParcelable("wcm");
            if (wcm != null) {
                willEditText.setText(wcm.getWill());
                canEditText.setText(wcm.getCan());
                mustEditText.setText(wcm.getMust());
                goalEditText.setText(wcm.getWcmGoal());
            }
        }

        //セーブボタンの処理
        Button saveButton = view.findViewById(R.id.willCanMust_saveButton);
        saveButton.setOnClickListener(view1 -> {
            //入力内容を取得して更新
            wcm.setWill(willEditText.getText().toString());
            wcm.setCan(canEditText.getText().toString());
            wcm.setMust(mustEditText.getText().toString());
            wcm.setWcmGoal(goalEditText.getText().toString());

            //ViewModelに変更後のデータを保存
            viewModel.updateWcm(wcm);

            //Fragmentを閉じる
            dismiss();
        });

        //戻るボタンの処理
        Button backButton = view.findViewById(R.id.willCanMust_backButton);
        backButton.setOnClickListener(view1 -> dismiss());

        return view;
    }
}
