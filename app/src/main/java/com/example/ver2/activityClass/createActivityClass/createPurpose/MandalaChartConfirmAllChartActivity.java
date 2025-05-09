/*
    作成したマンダラチャートを確認するActivity
    ズーム機能やアニメーションズーム（Fragmentでやるかも）を用いて、9*9のマンダラチャートすべてを表示する
*/

package com.example.ver2.activityClass.createActivityClass.createPurpose;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;

public class MandalaChartConfirmAllChartActivity extends AppCompatActivity {

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


    @SuppressLint("clickableViewAccessibility") //View.onTouchListenerを使う場合にLint警告を抑制
    @Override
    protected void onCreate(Bundle savedInstanceState){
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
    }


    //ScaleGestureDetectorのリスナー実装（内部クラス）
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){
            currentScale *= detector.getScaleFactor();
            currentScale = Math.max(minScale, Math.min(currentScale, maxScale));

            mandalaGridLayout.setScaleX(currentScale);
            mandalaGridLayout.setScaleY(currentScale);

            //ズームの中心をジェスチャーの中心に合わせる場合のtranslationは省略
            //detector.getFocusX(), detector.getFocusY()を使用

            return true; //イベントの消費
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector){
            return true; //スケールジェスチャーを有効にする
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector){
            //何もしなくてもいい
        }
    }

    //GestureDetectorのリスナー実装(内部クラス)
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(
                MotionEvent e1,
                MotionEvent e2,
                float distanceX, //移動された距離
                float distanceY  //移動された距離
        ){
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
        public boolean onDown(MotionEvent e){
            return true ; //イベントを受け付ける
        }

        //必要に応じて他のジェスチャーメソッドもオーバーライド
        //onSingleTapUpなどはアニメーションズームのトリガーに利用可能

    }


}
