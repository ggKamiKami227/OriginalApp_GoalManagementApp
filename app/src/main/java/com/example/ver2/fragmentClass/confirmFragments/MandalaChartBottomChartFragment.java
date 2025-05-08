/*
    マンダラチャートのサブチャート（周りの3*3のチャート）を入力するフラグメント
    MandalaChartExpansionActivityから呼ばれる。
    Fragmentを生成する際は、押したボタンの番号をFragment側に送り、それをもとにFragmentで入力する
    Fragmentとの情報共有はMandalaChartBottomChartViewModelを用いて共有する。
    Chartの中のTasksを最初に初期化するのはここかもしれないから、ここでしっかりと宣言しておく
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
import com.example.ver2.dataClass.Task;
import com.example.ver2.dataClass.purposeManagement.Chart;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.fragmentClass.viewModels.MandalaChartBottomChartViewModel;

import java.util.HashMap;
import java.util.Map;

public class MandalaChartBottomChartFragment extends DialogFragment {
    //EditTextのヒント（何も入力されていない場合）
    private final String textHint = "入力してください";
    private final String buttonHint = "タスクを入力してください";
    private final String initialEditTextHint = "ボタンを押してタスクを入力してください";

    //前に押したボタン、チャートの場所を記憶するための変数
    private int beforeButton;

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
    private Button leftButton, goalButton, rightButton;
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

        //テキストとかどこのボタンを押したか記憶する変数などの初期化
        taskEditText.setHint(initialEditTextHint);
        beforeButton = 0;

        topLeftButton = view.findViewById(R.id.goal_button_1);
        topButton = view.findViewById(R.id.goal_button_2);
        topRightButton = view.findViewById(R.id.goal_button_3);
        leftButton = view.findViewById(R.id.goal_button_4);
        goalButton = view.findViewById(R.id.purpose_button);
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
                //chartの中のタスクを生成
                // ID は 1 から 8 まで順番に割り振る
                for (int i = 1; i <= 8; i++) {
                    if(chart.getTaskById(i) == null) {
                        chart.addTask(new Task(i, "", "", null, null, null, false));
                    }
                }
                updateButtonText();
                setChartButtonClickListener();
            }
        }

        Button saveButton = view.findViewById(R.id.button_chart_save);
        Button backButton = view.findViewById(R.id.button_back);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                // Taskの更新
                if(beforeButton != 0){
                    String newTaskName = taskEditText.getText().toString();
                    chart.getTaskById(beforeButton).setName(newTaskName);
                }

                //これちゃんと機能するか考える必要あるかも
                //Chartを更新
                mandalaChart.updateChart(chart);

                //Chartを更新後、ViewModelのMandalaChartオブジェクトを更新
                mandalaChartBottomChartViewModel.updateMandalaChart(mandalaChart);

                dismiss();
            }
        });

        //backButtonは保存せずに終わる（警告を出すFragmentがあったほうがいい）
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                dismiss();
            }
        });
        return view;
    }

    //ボタンのリスナを設定する。Chartの情報を更新（Task）する役目と、EditTextを更新する。
    private void setChartButtonClickListener(){
        topLeftButton.setOnClickListener(createChartButtonClickListener(TopLeft));
        topButton.setOnClickListener(createChartButtonClickListener(Top));
        topRightButton.setOnClickListener(createChartButtonClickListener(TopRight));
        leftButton.setOnClickListener(createChartButtonClickListener(Left));
        rightButton.setOnClickListener(createChartButtonClickListener(Right));
        bottomLeftButton.setOnClickListener(createChartButtonClickListener(BottomLeft));
        bottomButton.setOnClickListener(createChartButtonClickListener(Bottom));
        bottomRightButton.setOnClickListener(createChartButtonClickListener(BottomRight));
    }

    private View.OnClickListener createChartButtonClickListener(final int id){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Taskの更新。beforeButton変数を利用して、変更するタスクを指定
                if(beforeButton != 0){
                    String newTaskName = taskEditText.getText().toString();
                    chart.getTaskById(beforeButton).setName(newTaskName);
                }

                //beforeButtonを今押しているものに変更
                beforeButton = id;

                //taskEditTextのテキストを更新
                if(chart.getTaskById(id).getName().isEmpty() || chart.getTaskById(id).getName() == null)
                    taskEditText.setHint(textHint);
                else
                    taskEditText.setText(chart.getTaskById(id).getName());
            }
        };
    }

    private void updateButtonText() {
        //リストでまとめる
        //Goalボタン：Goalボタンのテキストを更新
        goalButton.setText(chart.getGoal());

        //Mapを使うことでコンパクトに書く
        Map<Integer, Button> buttonMap = new HashMap<>();
        buttonMap.put(TopLeft, topLeftButton);
        buttonMap.put(Top, topButton);
        buttonMap.put(TopRight, topRightButton);
        buttonMap.put(Left, leftButton);
        buttonMap.put(Right, rightButton);
        buttonMap.put(BottomLeft, bottomLeftButton);
        buttonMap.put(Bottom, bottomButton);
        buttonMap.put(BottomRight, bottomRightButton);

        for (Map.Entry<Integer, Button> entry : buttonMap.entrySet()) {
            Integer taskId = entry.getKey();
            Button button = entry.getValue();
            if(chart != null) {
                if (chart.getTaskById(taskId) != null && !chart.getTaskById(taskId).getName().isEmpty()) {
                    button.setText(chart.getTaskById(taskId).getName());
                } else {
                    button.setText(buttonHint);
                }
            }
        }
    }
}
