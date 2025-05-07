/*
    マンダラチャートのサブチャート（周りの3*3のチャート）を入力するフラグメント
    MandalaChartExpansionActivityから呼ばれる。
    Fragmentを生成する際は、押したボタンの番号をFragment側に送り、それをもとにFragmentで入力する
    Fragmentとの情報共有はMandalaChartBottomChartViewModelを用いて共有する。
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
import com.example.ver2.dataClass.purposeManagement.Chart;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.fragmentClass.viewModels.MandalaChartBottomChartViewModel;

public class MandalaChartBottomChartFragment extends DialogFragment {
    //EditTextのヒント（何も入力されていない場合）
    private final String textHint = "入力してください";
    private final String buttonHint = "タスクを入力してください";

    //ボタンの位置と数字を同期させるための変数
    private final int TopLeft = 1;
    private final int Top = 2;
    private final int TopRight = 3;
    private final int Left = 4;
    private final int Right = 5;
    private final int BottomLeft = 6;
    private final int Bottom = 7;
    private final int BottomRight = 8;

    //チャートはボタンで構成する。真ん中のPurposeButtonのみテキストを変更するだけでリスナは付けない
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, purposeButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;


    private EditText taskEditText;

    private MandalaChart mandalaChart;
    private Chart chart;
    private int chartID;

    MandalaChartBottomChartViewModel mandalaChartBottomChartViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.bottom_chart, container,false);

        //ViewModelの取得
        mandalaChartBottomChartViewModel = new ViewModelProvider(requireActivity()).get(MandalaChartBottomChartViewModel.class);

        topLeftButton = view.findViewById(R.id.goal_button_1);
        topButton = view.findViewById(R.id.goal_button_2);
        topRightButton = view.findViewById(R.id.goal_button_3);
        leftButton = view.findViewById(R.id.goal_button_4);
        purposeButton = view.findViewById(R.id.purpose_button);
        rightButton = view.findViewById(R.id.goal_button_5);
        bottomLeftButton = view.findViewById(R.id.goal_button_6);
        bottomButton = view.findViewById(R.id.goal_button_7);
        bottomRightButton = view.findViewById(R.id.goal_button_8);

        Bundle bundle = getArguments();
        if(bundle != null){
            mandalaChart = bundle.getParcelable("mandalaChart");
            if (mandalaChart != null) {
                //チャートの初期化
                mandalaChartBottomChartViewModel.getSelectedChartID().observe(this, currentChartID -> {
                    if(currentChartID != null){
                        chartID = currentChartID;
                    }
                });
                chart = mandalaChart.getChartByID(chartID);
            }
        }

        return view;
    }

    private void updateButtonText() {
        //リストでまとめる
        //purposeボタン
        purposeButton.setText(mandalaChart.getPurpose());


        //MandalaChartクラスのチャートを指定して取得
        if (mandalaChart.getChartByID(TopLeft) != null) {
            if (mandalaChart.getChartByID(TopLeft).getGoal() != null && !mandalaChart.getChartByID(TopLeft).getGoal().isEmpty()) {
                topLeftButton.setText(mandalaChart.getChartByID(TopLeft).getGoal());
            } else {
                topLeftButton.setText(buttonHint);
            }
        } else {
            topLeftButton.setText(buttonHint);
        }

        if (mandalaChart.getChartByID(Top) != null) {
            if (mandalaChart.getChartByID(Top).getGoal() != null) {
                topButton.setText(mandalaChart.getChartByID(Top).getGoal());
            } else {
                topButton.setText(buttonHint);
            }
        } else {
            topButton.setText(buttonHint);
        }

        if (mandalaChart.getChartByID(TopRight) != null) {
            if (mandalaChart.getChartByID(TopRight).getGoal() != null) {
                topRightButton.setText(mandalaChart.getChartByID(TopRight).getGoal());
            } else {
                topRightButton.setText(buttonHint);
            }
        } else {
            topRightButton.setText(buttonHint);
        }
        if (mandalaChart.getChartByID(Left) != null) {
            if (mandalaChart.getChartByID(Left).getGoal() != null) {
                leftButton.setText(mandalaChart.getChartByID(Left).getGoal());
            } else {
                leftButton.setText(buttonHint);
            }
        } else {
            leftButton.setText(buttonHint);
        }

        if(mandalaChart.getChartByID(Right) != null){
            if(mandalaChart.getChartByID(Right).getGoal() != null){
                rightButton.setText(mandalaChart.getChartByID(Right).getGoal());
            }else{
                rightButton.setText(buttonHint);
            }
        }else{
            rightButton.setText(buttonHint);
        }

        if (mandalaChart.getChartByID(BottomLeft) != null){
            if(mandalaChart.getChartByID(BottomLeft).getGoal() != null){
                bottomLeftButton.setText(mandalaChart.getChartByID(BottomLeft).getGoal());
            }else{
                bottomLeftButton.setText(buttonHint);
            }
        }else{
            bottomLeftButton.setText(buttonHint);
        }

        if(mandalaChart.getChartByID(Bottom) != null){
            if(mandalaChart.getChartByID(Bottom).getGoal() != null){
                bottomButton.setText(mandalaChart.getChartByID(Bottom).getGoal());
            }else{
                bottomButton.setText(buttonHint);
            }
        }else{
            bottomButton.setText(buttonHint);
        }

        if(mandalaChart.getChartByID(BottomRight) != null){
            if(mandalaChart.getChartByID(BottomRight).getGoal() != null){
                bottomRightButton.setText(mandalaChart.getChartByID(BottomRight).getGoal());
            }else{
                bottomRightButton.setText(buttonHint);
            }
        }else{
            bottomButton.setText(buttonHint);
        }
    }
}
