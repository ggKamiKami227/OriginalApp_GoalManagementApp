/*
    作成したマンダラチャートを確認するActivity
    ズーム機能やアニメーションズーム（Fragmentでやるかも）を用いて、9*9のマンダラチャートすべてを表示する
*/

package com.example.ver2.activityClass.createActivityClass.createPurpose;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.Task;
import com.example.ver2.dataClass.purposeManagement.Chart;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;

import java.util.HashMap;
import java.util.Map;

public class MandalaChartConfirmAllChartActivity extends AppCompatActivity {

    //ボタンの位置と数字を同期させるための変数
    private final int TopLeft = 1;
    private final int Top = 2;
    private final int TopRight = 3;
    private final int Left = 4;
    private final int Center = 5;
    private final int Right = 6;
    private final int BottomLeft = 7;
    private final int Bottom = 8;
    private final int BottomRight = 9;

    private final String goalNothing = "目標が入力されていません";
    private final String purposeNothing = "目的が入力されていません。エラーの可能性があります";
    private final String taskNothing = "タスクが入力されていません";

    //チャートはButtonで構成されている。ただし、リスナは今のところ付けない。
    //3*3チャートが9つあるため、それぞれFor文などで割り当てテキストを付ける
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, centerButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;

    private GridLayout topLeftChartGrid, topChartGrid, topRightChartGrid;
    private GridLayout leftChartGrid, centerChartGrid, rightChartGrid;
    private GridLayout bottomLeftChartGrid, bottomChartGrid, bottomRightChartGrid;

    private GridLayout mandalaGridLayout;

    //ジェスチャー検出器
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    //現在の拡大率と移動量
    private float currentScale = 1.0f;
    private float currentTranslationX = 0.0f;
    private float currentTranslationY = 0.0f;

    //拡大率の制限
    private final float minScale = 0.5f;
    private final float maxScale = 3.0f;

    MandalaChart mandalaChart;


    @SuppressLint("clickableViewAccessibility") //View.onTouchListenerを使う場合にLint警告を抑制
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirm_chart_overall);

        //GridLayoutの参照を取得
        mandalaGridLayout = findViewById(R.id.mandalaChartOverall);

        //ジェスチャー検出器の初期化
        //Activityの場合はContextとして'this'を渡す
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        gestureDetector = new GestureDetector(this, new GestureListener());

        //GridLayoutにタッチリスナーを設定
        mandalaGridLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //ScaleGestureDetectorにイベントを渡す
                boolean consumedByScale = scaleGestureDetector.onTouchEvent(motionEvent);

                //GestureDetectorにイベントを渡す
                //スケールジェスチャーが行われている間はパンを無効にするなどの制御も可能
                boolean consumedByGesture = gestureDetector.onTouchEvent(motionEvent);

                //いずれかの検出器がイベントを消費したらtrueを返す
                //v.onTouchEvent(motionEvent)を含めると、GridLayout自体のデフォルトのタッチ処理も
                // 行われる可能性があるが、通常は検出器ですべて処理するため不要
                return consumedByScale || consumedByGesture;
            }
        });

        //それぞれのチャートを変数に割り当て
        topLeftChartGrid = findViewById(R.id.topLeftChart);
        topChartGrid = findViewById(R.id.topChart);
        topRightChartGrid = findViewById(R.id.topRightChart);
        leftChartGrid = findViewById(R.id.LeftChart);
        centerChartGrid = findViewById(R.id.centerChart);
        rightChartGrid = findViewById(R.id.RightChart);
        bottomLeftChartGrid = findViewById(R.id.bottomLeftChart);
        bottomChartGrid = findViewById(R.id.bottomChart);
        bottomRightChartGrid = findViewById(R.id.bottomRightChart);

        //作成したものを前のActivityから受け取る
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MandalaChart")) {
            mandalaChart = intent.getParcelableExtra("MandalaChart");
            setChartButtonText();
        }

        Button nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存するActivityに移行
                Intent intent_next = new Intent(MandalaChartConfirmAllChartActivity.this, SavePurposeActivity.class);
                intent_next.putExtra("MandalaChart", mandalaChart);
                startActivity(intent_next);
            }
        });

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_before = new Intent(MandalaChartConfirmAllChartActivity.this, MandalaChartExpansionActivity.class);
                intent_before.putExtra("MandalaChart", mandalaChart);
                startActivity(intent_before);
            }
        });



    }


    //ScaleGestureDetectorのリスナー実装（内部クラス）
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScale *= detector.getScaleFactor();
            currentScale = Math.max(minScale, Math.min(currentScale, maxScale));

            mandalaGridLayout.setScaleX(currentScale);
            mandalaGridLayout.setScaleY(currentScale);

            //ズームの中心をジェスチャーの中心に合わせる場合のtranslationは省略
            //detector.getFocusX(), detector.getFocusY()を使用

            return true; //イベントの消費
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true; //スケールジェスチャーを有効にする
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //何もしなくてもいい
        }
    }

    //GestureDetectorのリスナー実装(内部クラス)
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(
                MotionEvent e1,
                MotionEvent e2,
                float distanceX, //移動された距離
                float distanceY  //移動された距離
        ) {
            //Viewを移動させるには符号を反転させる
            currentTranslationX -= distanceX;
            currentTranslationY -= distanceY;

            mandalaGridLayout.setTranslationX(currentTranslationX);
            mandalaGridLayout.setTranslationY(currentTranslationY);

            //拡大時のパンの境界制御は省略

            return true; //イベントを消費する
        }

        // GestureDetectorを使う場合は、onDownをオーバーライドして、trueを返すのが一般的
        @Override
        public boolean onDown(MotionEvent e) {
            return true; //イベントを受け付ける
        }
        //必要に応じて他のジェスチャーメソッドもオーバーライド
        //onSingleTapUpなどはアニメーションズームのトリガーに利用可能
    }


    private void setChartButtonText() {
        //Mapを使ってコンパクトに書く
        Map<Integer, GridLayout> chartMap = new HashMap<>();
        chartMap.put(TopLeft, topLeftChartGrid);
        chartMap.put(Top, topChartGrid);
        chartMap.put(TopRight, topRightChartGrid);
        chartMap.put(Left, leftChartGrid);
        chartMap.put(Center, centerChartGrid);
        chartMap.put(Right, rightChartGrid);
        chartMap.put(BottomLeft, bottomLeftChartGrid);
        chartMap.put(Bottom, bottomChartGrid);
        chartMap.put(BottomRight, bottomRightChartGrid);

        for (Map.Entry<Integer, GridLayout> entry : chartMap.entrySet()) {
            //Integerとintの関係に注意、IntegerはNullアリだけど、intはナシだから、Nullエラーが出る可能性がある
            Integer chartId = entry.getKey();
            GridLayout chart_layout = entry.getValue();
            //Chartがあるか確認、また真ん中のChartはChartsに含まれていないので、そこはelse if で対処
            if (mandalaChart.getChartByID(chartId) != null) {

                //それぞれのchart内のボタンを変数に割り当てる
                topLeftButton = chart_layout.findViewById(R.id.button_topLeft);
                topButton = chart_layout.findViewById(R.id.button_top);
                topRightButton = chart_layout.findViewById(R.id.button_topRight);
                leftButton = chart_layout.findViewById(R.id.button_Left);
                centerButton = chart_layout.findViewById(R.id.button_center);
                rightButton = chart_layout.findViewById(R.id.button_Right);
                bottomLeftButton = chart_layout.findViewById(R.id.button_bottomLeft);
                bottomButton = chart_layout.findViewById(R.id.button_bottom);
                bottomRightButton = chart_layout.findViewById(R.id.button_bottomRight);

                Map<Integer, Button> buttonMap = new HashMap<>();
                buttonMap.put(TopLeft, topLeftButton);
                buttonMap.put(Top, topButton);
                buttonMap.put(TopRight, topRightButton);
                buttonMap.put(Left, leftButton);
                buttonMap.put(Center, centerButton);
                buttonMap.put(Right, rightButton);
                buttonMap.put(BottomLeft, bottomLeftButton);
                buttonMap.put(Bottom, bottomButton);
                buttonMap.put(BottomRight, bottomRightButton);

                for (Map.Entry<Integer, Button> buttonEntry : buttonMap.entrySet()) {
                    Integer buttonId = buttonEntry.getKey();
                    Button button = buttonEntry.getValue();
                    //チャートの真ん中はgoalでTaskオブジェクトじゃないから、別分岐
                    if (buttonId != Center) {
                        if (mandalaChart.getChartByID(chartId).getTaskById(buttonId) != null) {
                            Task task = mandalaChart.getChartByID(chartId).getTaskById(buttonId);
                            if (task.getName() != null && !task.getName().isEmpty()) {
                                button.setText(task.getName());
                            } else {
                                button.setText(taskNothing);
                            }
                        }
                    } else {
                        if (mandalaChart.getChartByID(chartId) != null) {
                            Chart chart = mandalaChart.getChartByID(chartId);
                            if (chart.getGoal() != null && !chart.getGoal().isEmpty()) {
                                button.setText(chart.getGoal());
                            } else {
                                button.setText(goalNothing);
                            }
                        }
                    }
                }
            }
            //真ん中はChartとしてオブジェクト化していないので、ここで分ける
            else if (chartId == Center) {
                //それぞれのchart内のボタンを変数に割り当てる
                topLeftButton = chart_layout.findViewById(R.id.button_topLeft);
                topButton = chart_layout.findViewById(R.id.button_top);
                topRightButton = chart_layout.findViewById(R.id.button_topRight);
                leftButton = chart_layout.findViewById(R.id.button_Left);
                centerButton = chart_layout.findViewById(R.id.button_center);
                rightButton = chart_layout.findViewById(R.id.button_Right);
                bottomLeftButton = chart_layout.findViewById(R.id.button_bottomLeft);
                bottomButton = chart_layout.findViewById(R.id.button_bottom);
                bottomRightButton = chart_layout.findViewById(R.id.button_bottomRight);

                Map<Integer, Button> buttonMap = new HashMap<>();
                buttonMap.put(TopLeft, topLeftButton);
                buttonMap.put(Top, topButton);
                buttonMap.put(TopRight, topRightButton);
                buttonMap.put(Left, leftButton);
                buttonMap.put(Center, centerButton);
                buttonMap.put(Right, rightButton);
                buttonMap.put(BottomLeft, bottomLeftButton);
                buttonMap.put(Bottom, bottomButton);
                buttonMap.put(BottomRight, bottomRightButton);

                for (Map.Entry<Integer, Button> buttonEntry : buttonMap.entrySet()) {
                    //真ん中は真ん中にpurpose,まわりはgoalってなってるから、goalIdにした
                    Integer goalId = buttonEntry.getKey();
                    Button button = buttonEntry.getValue();
                    //中心はpurposeなので別にする
                    if (goalId != Center) {
                        if (mandalaChart.getChartByID(goalId).getGoal() != null || !mandalaChart.getChartByID(goalId).getGoal().isEmpty()) {
                            button.setText(mandalaChart.getChartByID(goalId).getGoal());
                        } else {
                            button.setText(goalNothing);
                        }
                    } else {
                        if (mandalaChart.getPurpose() != null || !mandalaChart.getPurpose().isEmpty()) {
                            button.setText(mandalaChart.getPurpose());
                        } else {
                            button.setText(purposeNothing);
                        }
                    }
                }
            }
        }

    }

}
