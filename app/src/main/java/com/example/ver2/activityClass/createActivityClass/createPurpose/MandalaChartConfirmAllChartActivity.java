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
import android.widget.TableLayout;

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

    private TableLayout topLeftChartTable, topChartTable, topRightChartTable;
    private TableLayout leftChartTable, centerChartTable, rightChartTable;
    private TableLayout bottomLeftChartTable, bottomChartTable, bottomRightChartTable;

    private androidx.gridlayout.widget.GridLayout mandalaGridLayout;

    //ジェスチャー検出器
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private ScaleListener myScaleListener;

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
        myScaleListener = new ScaleListener();
        scaleGestureDetector = new ScaleGestureDetector(this, myScaleListener);
        gestureDetector = new GestureDetector(this, new GestureListener());


        //GridLayoutにタッチリスナーを設定
        mandalaGridLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //ScaleGestureDetectorにイベントを渡す
                boolean consumedByScale = scaleGestureDetector.onTouchEvent(motionEvent);

                //GestureDetectorにイベントを渡す
                //スケールジェスチャーが行われている間はパンを無効にするなどの制御も可能
                boolean consumedByGesture = false;
                if (!myScaleListener.isInScale()) {
                    consumedByGesture = gestureDetector.onTouchEvent(motionEvent);
                }

                //いずれかの検出器がイベントを消費したらtrueを返す
                //v.onTouchEvent(motionEvent)を含めると、GridLayout自体のデフォルトのタッチ処理も
                // 行われる可能性があるが、通常は検出器ですべて処理するため不要
                return consumedByScale || consumedByGesture;
            }
        });

        //それぞれのチャートを変数に割り当て
        topLeftChartTable = mandalaGridLayout.findViewById(R.id.topLeftChart);
        topChartTable = mandalaGridLayout.findViewById(R.id.topChart);
        topRightChartTable = mandalaGridLayout.findViewById(R.id.topRightChart);
        leftChartTable = mandalaGridLayout.findViewById(R.id.LeftChart);
        centerChartTable = mandalaGridLayout.findViewById(R.id.centerChart);
        rightChartTable = mandalaGridLayout.findViewById(R.id.RightChart);
        bottomLeftChartTable = mandalaGridLayout.findViewById(R.id.bottomLeftChart);
        bottomChartTable = mandalaGridLayout.findViewById(R.id.bottomChart);
        bottomRightChartTable = mandalaGridLayout.findViewById(R.id.bottomRightChart);

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
        private boolean isInScale = false;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // ズームの中心をジェスチャーの中心に設定
            mandalaGridLayout.setPivotX(detector.getFocusX());
            mandalaGridLayout.setPivotY(detector.getFocusY());

            currentScale *= detector.getScaleFactor();
            currentScale = Math.max(minScale, Math.min(currentScale, maxScale));

            mandalaGridLayout.setScaleX(currentScale);
            mandalaGridLayout.setScaleY(currentScale);

            return true; // イベントの消費
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isInScale = true; // スケール開始
            return true; // スケールジェスチャーを有効にする
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isInScale = false; // スケール終了
        }

        // スケール中かどうかを外部から取得するためのメソッド
        public boolean isInScale() {
            return isInScale;
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
            if (myScaleListener.isInScale()) {
                return false;
            }

            // Viewを移動させるには符号を反転させる
            currentTranslationX -= distanceX;
            currentTranslationY -= distanceY;

            // パンの境界制御を適用
            adjustTranslationToBounds();

            // ビューの移動を適用
            mandalaGridLayout.setTranslationX(currentTranslationX);
            mandalaGridLayout.setTranslationY(currentTranslationY);

            return true; // イベントを消費する
        }

        // GestureDetectorを使う場合は、onDownをオーバーライドして、trueを返すのが一般的
        @Override
        public boolean onDown(MotionEvent e) {
            return true; //イベントを受け付ける
        }
        //必要に応じて他のジェスチャーメソッドもオーバーライド
        //onSingleTapUpなどはアニメーションズームのトリガーに利用可能
    }

    // パンの境界を調整するヘルパーメソッド
    private void adjustTranslationToBounds() {
        // mandalaGridLayoutの実際の幅と高さ（拡大前のサイズ）
        int viewWidth = mandalaGridLayout.getWidth();
        int viewHeight = mandalaGridLayout.getHeight();

        // 親ビュー（ConstraintLayout）の幅と高さ
        View parent = (View) mandalaGridLayout.getParent();
        if (parent == null) return; // 親がない場合は処理しない
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();

        // 拡大後のコンテンツのサイズ
        float scaledWidth = viewWidth * currentScale;
        float scaledHeight = viewHeight * currentScale;

        // X軸の移動制限
        float maxTranslateX;
        float minTranslateX;

        // Y軸の移動制限
        float maxTranslateY; // ★ここに宣言を追加
        float minTranslateY; // ★ここに宣言を追加

        // ★★★ ここから追加・修正する部分 ★★★
        // Viewportの左端と右端のワールド座標 (GridLayoutの左上が(0,0)として)
        float viewportLeft = -currentTranslationX / currentScale;
        float viewportTop = -currentTranslationY / currentScale;
        float viewportRight = viewportLeft + parentWidth / currentScale;
        float viewportBottom = viewportTop + parentHeight / currentScale;

        // コンテンツの左端と右端のワールド座標 (GridLayoutの左上が(0,0)として)
        float contentLeft = 0;
        float contentTop = 0;
        float contentRight = viewWidth;
        float contentBottom = viewHeight;
        // ★★★ ここまで追加・修正する部分 ★★★

        if (scaledWidth < parentWidth) {
            // コンテンツが親ビューより小さい場合、中央に固定
            // TranslationXの目標値は、(parentWidth - scaledWidth) / 2.0f
            // これは現在のTranslationXをリセットするような挙動
            maxTranslateX = (parentWidth - scaledWidth) / 2.0f;
            minTranslateX = maxTranslateX; // 中央に固定
        } else {
            // コンテンツが親ビューより大きい場合、はみ出しを制限
            // 左端が画面左端より右に来ないように、右端が画面右端より左に来ないように
            // translationX の範囲は -(scaledWidth - parentWidth) / 2 から (scaledWidth - parentWidth) / 2
            // ただし、pivotX/Y の影響を考慮する必要があります。
            // 簡単な方法として、コンテンツの中心とビューポートの中心を合わせるようにする
            maxTranslateX = (scaledWidth - parentWidth) / 2.0f;
            minTranslateX = -(scaledWidth - parentWidth) / 2.0f;
        }

        if (scaledHeight < parentHeight) {
            maxTranslateY = (parentHeight - scaledHeight) / 2.0f;
            minTranslateY = maxTranslateY;
        } else {
            maxTranslateY = (scaledHeight - parentHeight) / 2.0f;
            minTranslateY = -(scaledHeight - parentHeight) / 2.0f;
        }

        currentTranslationX = Math.max(minTranslateX, Math.min(currentTranslationX, maxTranslateX));
        currentTranslationY = Math.max(minTranslateY, Math.min(currentTranslationY, maxTranslateY));
    }


    private void setChartButtonText() {
        //Mapを使ってコンパクトに書く
        Map<Integer, TableLayout> chartMap = new HashMap<>();
        chartMap.put(TopLeft, topLeftChartTable);
        chartMap.put(Top, topChartTable);
        chartMap.put(TopRight, topRightChartTable);
        chartMap.put(Left, leftChartTable);
        chartMap.put(Center, centerChartTable);
        chartMap.put(Right, rightChartTable);
        chartMap.put(BottomLeft, bottomLeftChartTable);
        chartMap.put(Bottom, bottomChartTable);
        chartMap.put(BottomRight, bottomRightChartTable);

        for (Map.Entry<Integer, TableLayout> entry : chartMap.entrySet()) {
            //Integerとintの関係に注意、IntegerはNullアリだけど、intはナシだから、Nullエラーが出る可能性がある
            Integer chartId = entry.getKey();
            TableLayout chart_layout = entry.getValue();
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

                    // 真ん中のボタン以外はイベントを透過させる
                    if (buttonId != Center) {
                        button.setClickable(false);
                        button.setFocusable(false);
                    } else {
                        // 真ん中のボタンはタップを有効にする
                        button.setClickable(true);
                        button.setFocusable(true);
                        // ここでOnClickListenerを設定することも可能だが、
                        // onSingleTapUpで一元管理するため、ここでは設定しない
                    }

                    //チャートの真ん中はgoalでTaskオブジェクトじゃないから、別分岐
                    if (buttonId != Center) {
                        if (mandalaChart.getChartByID(chartId).getTaskById(buttonId) != null) {
                            Task task = mandalaChart.getChartByID(chartId).getTaskById(buttonId);
                            if (task.getName() != null) {
                                if (!task.getName().isEmpty()) {
                                    button.setText(task.getName());
                                } else {
                                    button.setText(taskNothing);
                                }
                            } else {
                                button.setText(taskNothing);
                            }
                        }
                    } else {
                        if (mandalaChart.getChartByID(chartId) != null) {
                            Chart chart = mandalaChart.getChartByID(chartId);
                            if (chart.getGoal() != null) {
                                if (!chart.getGoal().isEmpty()) {
                                    button.setText(chart.getGoal());
                                } else {
                                    button.setText(goalNothing);
                                }
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

                    // 真ん中のボタン以外はイベントを透過させる
                    if (goalId != Center) {
                        button.setClickable(false);
                        button.setFocusable(false);
                    } else {
                        // 真ん中のボタンはタップを有効にする
                        button.setClickable(true);
                        button.setFocusable(true);
                        // ここでOnClickListenerを設定することも可能だが、
                        // onSingleTapUpで一元管理するため、ここでは設定しない
                    }

                    //中心はpurposeなので別にする
                    if (goalId != Center) {
                        if (mandalaChart.getChartByID(goalId) != null) {
                            if (mandalaChart.getChartByID(goalId).getGoal() != null) {
                                if (!mandalaChart.getChartByID(goalId).getGoal().isEmpty()) {
                                    button.setText(mandalaChart.getChartByID(goalId).getGoal());
                                } else {
                                    button.setText(goalNothing);
                                }
                            } else {
                                button.setText(goalNothing);
                            }
                        } else {
                            button.setText(goalNothing);
                        }
                    } else {
                        if (mandalaChart.getPurpose() != null) {
                            if (!mandalaChart.getPurpose().isEmpty()) {
                                button.setText(mandalaChart.getPurpose());
                            } else {
                                button.setText(purposeNothing);
                            }
                        } else {
                            button.setText(purposeNothing);
                        }
                    }
                }
            }
        }

    }

}
