/*
    SMARTフレームワークを使用した目標の編集をするFragment
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
import com.example.ver2.dataClass.goalManagement.SMART;
import com.example.ver2.fragmentClass.viewModels.goalViewModels.ConfirmSMARTViewModel;

public class ConfirmSMARTEditFragment extends DialogFragment {
    private SMART smart;

    private EditText sEditText;
    private EditText mEditText;
    private EditText aEditText;
    private EditText rEditText;
    private EditText tEditText;
    private EditText gEditText;

    //LiveDataを使用したViewModel。これのSMARTオブジェクトをアップデートすることで、Activity側のUIもアップデートされる。
    ConfirmSMARTViewModel confirmSMARTViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //目標作成時に用いたLayoutを流用
        View view = inflater.inflate(R.layout.smart_scroll_layout, container, false);

        //ViewModelの取得
        confirmSMARTViewModel = new ViewModelProvider(requireActivity()).get(ConfirmSMARTViewModel.class);

        sEditText = view.findViewById(R.id.smart_scrollLayout_editText_Specific);
        mEditText = view.findViewById(R.id.smart_scrollLayout_editText_Measurable);
        aEditText = view.findViewById(R.id.smart_scrollLayout_editText_Achievable);
        rEditText = view.findViewById(R.id.smart_scrollLayout_editText_relevant);
        tEditText = view.findViewById(R.id.smart_scrollLayout_editText_timeBound);
        gEditText = view.findViewById(R.id.smart_scrollLayout_editText_goal);

        //Activityから送られてくる情報をセット
        Bundle bundle = getArguments();
        if (bundle != null) {
            smart = bundle.getParcelable("smart");
            if (smart != null) {
                sEditText.setText(smart.getSpecific());
                mEditText.setText(smart.getMeasurable());
                aEditText.setText(smart.getAchievable());
                rEditText.setText(smart.getRelevant());
                tEditText.setText(smart.getTimeBound());
                gEditText.setText(smart.getSmartGoal());
            }
        }

        //セーブボタンの処理
        Button saveButton = view.findViewById(R.id.smart_saveButton);
        saveButton.setOnClickListener(view1 -> {
            //入力内容を取得して更新
            smart.setSpecific(sEditText.getText().toString());
            smart.setMeasurable(mEditText.getText().toString());
            smart.setAchievable(aEditText.getText().toString());
            smart.setRelevant(rEditText.getText().toString());
            smart.setTimeBound(tEditText.getText().toString());
            smart.setSmartGoal(gEditText.getText().toString());

            //ViewModelに変更後のデータを保存
            confirmSMARTViewModel.updateSmart(smart);

            //Fragmentを閉じる
            dismiss();
        });

        //戻るボタンの処理
        Button backButton = view.findViewById(R.id.smart_backButton);
        backButton.setOnClickListener(view1 -> dismiss());

        return view;
    }
}
