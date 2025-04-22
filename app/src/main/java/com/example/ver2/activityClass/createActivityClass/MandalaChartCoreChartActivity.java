/*
    マンダラチャートの真ん中の3*3のマトリックス（チャート）を決めるActivity
 */
package com.example.ver2.activityClass.createActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.purposeManagement.Chart;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;

import java.util.ArrayList;
import java.util.List;

public class MandalaChartCoreChartActivity extends AppCompatActivity {
    //チャートはボタンで構成する。真ん中のPurposeButtonのみテキストを変更するだけでリスナは付けない
    //2025-04-22 charts変数と同期させたい（考えなくてもいいかもだけど、左上から右に数えていって、それの順番でリストと紐づけさせたい）
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, purposeButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;

    private MandalaChart mandalaChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
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

        //作成途中のものを適用
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("MandalaChart")){
            mandalaChart = intent.getParcelableExtra("MandalaChart");
            if(mandalaChart != null){

            }
        }
    }

    //ボタンのテキストを更新するメソッド
    private void updateButtonText(){
        //リストでまとめる
        List<String> goalsText = new ArrayList<>();
        for(Chart chart : mandalaChart.getCharts()){
            goalsText.add(chart.getGoal());
        }
    }
}
