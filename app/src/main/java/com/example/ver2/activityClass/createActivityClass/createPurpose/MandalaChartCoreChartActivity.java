/*
    マンダラチャートの真ん中の3*3のマトリックス（チャート）を決めるActivity
 */
package com.example.ver2.activityClass.createActivityClass.createPurpose;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

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

    private TableLayout buttonTableLayout;
    //チャートはボタンで構成する。真ん中のPurposeButtonのみテキストを変更するだけでリスナは付けない
    //2025-04-22 charts変数と同期させたい（考えなくてもいいかもだけど、左上から右に数えていって、それの順番でリストと紐づけさせたい）。IDでChartを管理すればいいかも？
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, purposeButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;
    //ボタンの色を変更するときに使う
    private Button currentSelectedButton = null;

    private EditText goalEditText;

    private MandalaChart mandalaChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_chart);

        buttonTableLayout = findViewById(R.id.coreChart);

        topLeftButton = buttonTableLayout.findViewById(R.id.button_topLeft);
        topButton = buttonTableLayout.findViewById(R.id.button_top);
        topRightButton = buttonTableLayout.findViewById(R.id.button_topRight);
        leftButton = buttonTableLayout.findViewById(R.id.button_Left);
        purposeButton = buttonTableLayout.findViewById(R.id.button_center);
        rightButton = buttonTableLayout.findViewById(R.id.button_Right);
        bottomLeftButton = buttonTableLayout.findViewById(R.id.button_bottomLeft);
        bottomButton = buttonTableLayout.findViewById(R.id.button_bottom);
        bottomRightButton = buttonTableLayout.findViewById(R.id.button_bottomRight);

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
                intent_next.putExtra("MandalaChart", mandalaChart);
                startActivity(intent_next);
            }
        });

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_before = new Intent(MandalaChartCoreChartActivity.this, MandalaChartPurposeActivity.class);
                intent_before.putExtra("MandalaChart", mandalaChart);
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

        for (Map.Entry<Integer, Button> entry : buttonMap.entrySet()) {
            Integer chartId = entry.getKey();
            Button button = entry.getValue();
            if (mandalaChart.getChartByID(chartId) != null) {
                if (mandalaChart.getChartByID(chartId).getGoal() != null) {
                    if (!mandalaChart.getChartByID(chartId).getGoal().isEmpty())
                        button.setText(mandalaChart.getChartByID(chartId).getGoal());
                    else {
                        button.setText(textHint);
                    }
                } else {
                    button.setText(textHint);
                }
            } else {
                button.setText(textHint);
            }
        }
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
    }

    //ボタンのリスナを省略して書くためのメソッド
    private View.OnClickListener createChartButtonClickListener(final int chartID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //入力された情報を保存する
                updateChartGoal();

                //色変更するコード
                Button clickedButton = (Button)view;
                if(currentSelectedButton != null && currentSelectedButton != clickedButton){
                    currentSelectedButton.setSelected(false);
                }
                clickedButton.setSelected(true);
                currentSelectedButton = clickedButton;

                //入力を待つ
                //ここで選択されているボタンの変更
                if (mandalaChart.getChartByID(chartID) != null) {
                    if (mandalaChart.getChartByID(chartID).getGoal() != null) {
                        if (!mandalaChart.getChartByID(chartID).getGoal().isEmpty()) {
                            goalEditText.setText(mandalaChart.getChartByID(chartID).getGoal());
                        } else {
                            goalEditText.setHint(textHint);
                            goalEditText.setText("");
                        }
                    } else {
                        goalEditText.setHint(textHint);
                        goalEditText.setText("");
                    }
                } else {
                    Log.d("create new mandalaChart", "Create! MandalaChart");
                    //もし、チャートがなかったら、ここで新しくチャートを生成する。
                    mandalaChart.addChart(new Chart(chartID, null, null, false));
                    goalEditText.setHint(textHint);
                    goalEditText.setText("");
                }

                //2025-05-14 デバッグ：ボタンのテキストが変わらない。メソッド入れ忘れ
                updateButtonText();

                //2025-05-14:このタイミングを間違えるとChartの生成するIDが間違えるから注意
                buttonNumber = chartID;
            }
        };
    }


    private void updateChartGoal() {
        //入力された情報を保存する
        Log.d("button Number", valueOf(buttonNumber));

        if (buttonNumber != 0) {
            String goal = goalEditText.getText().toString();
            //チャートのGoalを変更（チャート指定方法はIDとボタンのナンバーを一致させる。）
            mandalaChart.getChartByID(buttonNumber).setGoal(goal);
        }
    }
}
