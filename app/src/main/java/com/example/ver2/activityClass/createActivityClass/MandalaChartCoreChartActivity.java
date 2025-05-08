/*
    マンダラチャートの真ん中の3*3のマトリックス（チャート）を決めるActivity
 */
package com.example.ver2.activityClass.createActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.purposeManagement.Chart;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;

import java.util.HashMap;
import java.util.Map;

public class MandalaChartCoreChartActivity extends AppCompatActivity {
    //ボタンとEditTextのヒント
    private final String textHint = "目的達成に必要な目標を入力してください";
    //ボタンの位置と数字を同期させるための変数
    private final int TopLeft = 1;
    private final int Top = 2;
    private final int TopRight = 3;
    private final int Left = 4;
    private final int Right = 5;
    private final int BottomLeft = 6;
    private final int Bottom = 7;
    private final int BottomRight = 8;


    //どのボタンを押されているか判断するための変数
    private int buttonNumber;

    //チャートはボタンで構成する。真ん中のPurposeButtonのみテキストを変更するだけでリスナは付けない
    //2025-04-22 charts変数と同期させたい（考えなくてもいいかもだけど、左上から右に数えていって、それの順番でリストと紐づけさせたい）。IDでChartを管理すればいいかも？
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, purposeButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;

    private EditText goalEditText;

    private MandalaChart mandalaChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_chart);

        topLeftButton = findViewById(R.id.goal_button_1);
        topButton = findViewById(R.id.goal_button_2);
        topRightButton = findViewById(R.id.goal_button_3);
        leftButton = findViewById(R.id.goal_button_4);
        purposeButton = findViewById(R.id.purpose_button);
        rightButton = findViewById(R.id.goal_button_5);
        bottomLeftButton = findViewById(R.id.goal_button_6);
        bottomButton = findViewById(R.id.goal_button_7);
        bottomRightButton = findViewById(R.id.goal_button_8);

        goalEditText = findViewById(R.id.goalNameEditText);

        //とりあえずここに置いておく 2025-04-23
        setButtonListener();

        //作成途中のものを適用,初めてMandalaChartのオブジェクトを生成するのはこれの前のアクティビティ
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MandalaChart")) {
            mandalaChart = intent.getParcelableExtra("MandalaChart");
            if (mandalaChart != null) {
                //ボタンのテキストを設定
                updateButtonText();
            }
        }

        Button nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_next = new Intent(MandalaChartCoreChartActivity.this, MandalaChartExpansionActivity.class);
                intent_next.putExtra("MandalaChart",mandalaChart);
                startActivity(intent_next);
            }
        });

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_before = new Intent(MandalaChartCoreChartActivity.this,MandalaChartPurposeActivity.class);
                intent_before.putExtra("MandalaChart",mandalaChart);
                startActivity(intent_before);
            }
        });

    }

    //ボタンのテキストを更新するメソッド
    private void updateButtonText() {
        //リストでまとめる
        //purposeボタン
        purposeButton.setText(mandalaChart.getPurpose());

        //Mapを使ってこんぱくとにかく
        Map<Integer, Button> buttonMap = new HashMap<>();
        buttonMap.put(TopLeft, topLeftButton);
        buttonMap.put(Top, topButton);
        buttonMap.put(TopRight, topRightButton);
        buttonMap.put(Left, leftButton);
        buttonMap.put(Right, rightButton);
        buttonMap.put(BottomLeft, bottomLeftButton);
        buttonMap.put(Bottom, bottomButton);
        buttonMap.put(BottomRight, bottomRightButton);

        for(Map.Entry<Integer, Button> entry : buttonMap.entrySet()){
            Integer chartId = entry.getKey();
            Button button = entry.getValue();
            if(mandalaChart.getChartByID(chartId) != null){
                if(mandalaChart.getChartByID(chartId).getGoal() != null || !mandalaChart.getChartByID(chartId).getGoal().isEmpty()){
                    button.setText(mandalaChart.getChartByID(chartId).getGoal());
                }else {
                    button.setText(textHint);
                }
            }else{
                button.setText(textHint);
            }
        }


//        //MandalaChartクラスのチャートを指定して取得
//        if (mandalaChart.getChartByID(TopLeft) != null) {
//            if (mandalaChart.getChartByID(TopLeft).getGoal() != null && !mandalaChart.getChartByID(TopLeft).getGoal().isEmpty()) {
//                topLeftButton.setText(mandalaChart.getChartByID(TopLeft).getGoal());
//            } else {
//                topLeftButton.setText(textHint);
//            }
//        } else {
//            topLeftButton.setText(textHint);
//        }
//
//        if (mandalaChart.getChartByID(Top) != null) {
//            if (mandalaChart.getChartByID(Top).getGoal() != null) {
//                topButton.setText(mandalaChart.getChartByID(Top).getGoal());
//            } else {
//                topButton.setText(textHint);
//            }
//        } else {
//            topButton.setText(textHint);
//        }
//
//        if (mandalaChart.getChartByID(TopRight) != null) {
//            if (mandalaChart.getChartByID(TopRight).getGoal() != null) {
//                topRightButton.setText(mandalaChart.getChartByID(TopRight).getGoal());
//            } else {
//                topRightButton.setText(textHint);
//            }
//        } else {
//            topRightButton.setText(textHint);
//        }
//        if (mandalaChart.getChartByID(Left) != null) {
//            if (mandalaChart.getChartByID(Left).getGoal() != null) {
//                leftButton.setText(mandalaChart.getChartByID(Left).getGoal());
//            } else {
//                leftButton.setText(textHint);
//            }
//        } else {
//            leftButton.setText(textHint);
//        }
//
//        if(mandalaChart.getChartByID(Right) != null){
//            if(mandalaChart.getChartByID(Right).getGoal() != null){
//                rightButton.setText(mandalaChart.getChartByID(Right).getGoal());
//            }else{
//                rightButton.setText(textHint);
//            }
//        }else{
//            rightButton.setText(textHint);
//        }
//
//        if (mandalaChart.getChartByID(BottomLeft) != null){
//            if(mandalaChart.getChartByID(BottomLeft).getGoal() != null){
//                bottomLeftButton.setText(mandalaChart.getChartByID(BottomLeft).getGoal());
//            }else{
//                bottomLeftButton.setText(textHint);
//            }
//        }else{
//            bottomLeftButton.setText(textHint);
//        }
//
//        if(mandalaChart.getChartByID(Bottom) != null){
//            if(mandalaChart.getChartByID(Bottom).getGoal() != null){
//                bottomButton.setText(mandalaChart.getChartByID(Bottom).getGoal());
//            }else{
//                bottomButton.setText(textHint);
//            }
//        }else{
//            bottomButton.setText(textHint);
//        }
//
//        if(mandalaChart.getChartByID(BottomRight) != null){
//            if(mandalaChart.getChartByID(BottomRight).getGoal() != null){
//                bottomRightButton.setText(mandalaChart.getChartByID(BottomRight).getGoal());
//            }else{
//                bottomRightButton.setText(textHint);
//            }
//        }else{
//            bottomButton.setText(textHint);
//        }
    }

    //ボタンのリスナを設定するメソッドで、ここでもしMandalaChartのChartsが作られていない場合、ここで足していく。（チャートの数とかの管理をしっかり行うこと）
    private void setButtonListener() {
        buttonNumber = 0;

        topLeftButton.setOnClickListener(createChartButtonClickListener(TopLeft));
        topButton.setOnClickListener(createChartButtonClickListener(Top));
        topRightButton.setOnClickListener(createChartButtonClickListener(TopRight));
        leftButton.setOnClickListener(createChartButtonClickListener(Left));
        rightButton.setOnClickListener(createChartButtonClickListener(Right));
        bottomLeftButton.setOnClickListener(createChartButtonClickListener(BottomLeft));
        bottomButton.setOnClickListener(createChartButtonClickListener(Bottom));
        bottomRightButton.setOnClickListener(createChartButtonClickListener(BottomRight));

//        topLeftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = TopLeft;
//                if (mandalaChart.getChartByID(TopLeft) != null) {
//                    if (mandalaChart.getChartByID(TopLeft).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(TopLeft).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは1
//                    mandalaChart.addChart(new Chart(TopLeft, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        topButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = Top;
//                if (mandalaChart.getChartByID(Top) != null) {
//                    if (mandalaChart.getChartByID(Top).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(Top).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは2
//                    mandalaChart.addChart(new Chart(Top, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        topRightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = TopRight;
//                if (mandalaChart.getChartByID(TopRight) != null) {
//                    if (mandalaChart.getChartByID(TopRight).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(TopRight).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは3
//                    mandalaChart.addChart(new Chart(TopRight, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = Left;
//                if (mandalaChart.getChartByID(Left) != null) {
//                    if (mandalaChart.getChartByID(Left).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(Left).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは5
//                    mandalaChart.addChart(new Chart(Left, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = Right;
//                if (mandalaChart.getChartByID(Right) != null) {
//                    if (mandalaChart.getChartByID(Right).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(Right).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは5
//                    mandalaChart.addChart(new Chart(Right, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        bottomLeftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = BottomLeft;
//                if (mandalaChart.getChartByID(BottomLeft) != null) {
//                    if (mandalaChart.getChartByID(BottomLeft).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(BottomLeft).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは6
//                    mandalaChart.addChart(new Chart(BottomLeft, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        bottomButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = Bottom;
//                if (mandalaChart.getChartByID(Bottom) != null) {
//                    if (mandalaChart.getChartByID(Bottom).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(Bottom).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは7
//                    mandalaChart.addChart(new Chart(Bottom, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
//
//        bottomRightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //入力された情報を保存する
//                updateChartGoal();
//
//                //入力を待つ
//                //ここで選択されているボタンの変更
//                buttonNumber = BottomRight;
//                if (mandalaChart.getChartByID(BottomRight) != null) {
//                    if (mandalaChart.getChartByID(BottomRight).getGoal() != null) {
//                        goalEditText.setText(mandalaChart.getChartByID(BottomRight).getGoal());
//                    } else {
//                        goalEditText.setHint(textHint);
//                    }
//                } else {
//                    //もし、チャートがなかったら、ここで新しくチャートを生成する。IDは8
//                    mandalaChart.addChart(new Chart(BottomRight, null, null, false));
//                    goalEditText.setHint(textHint);
//                }
//            }
//        });
    }

    //ボタンのリスナを省略して書くためのメソッド
    private View.OnClickListener createChartButtonClickListener(final int chartID){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //入力された情報を保存する
                updateChartGoal();

                //入力を待つ
                //ここで選択されているボタンの変更
                buttonNumber = chartID;
                if (mandalaChart.getChartByID(chartID) != null) {
                    if (mandalaChart.getChartByID(chartID).getGoal() != null || !mandalaChart.getChartByID(chartID).getGoal().isEmpty()) {
                        goalEditText.setText(mandalaChart.getChartByID(chartID).getGoal());
                    } else {
                        goalEditText.setHint(textHint);
                    }
                } else {
                    //もし、チャートがなかったら、ここで新しくチャートを生成する。
                    mandalaChart.addChart(new Chart(chartID, null, null, false));
                    goalEditText.setHint(textHint);
                }
            }
        };
    }

    private void updateChartGoal() {
        //入力された情報を保存する
        if (buttonNumber != 0) {
            String goal = goalEditText.getText().toString();
            //チャートのGoalを変更（チャート指定方法はIDとボタンのナンバーを一致させる。）
            mandalaChart.getChartByID(buttonNumber).setGoal(goal);
        }
    }
}
