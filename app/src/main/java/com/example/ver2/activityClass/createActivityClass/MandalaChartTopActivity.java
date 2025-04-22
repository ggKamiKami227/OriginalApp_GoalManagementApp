/*
    マンダラチャートを利用した目的設定を行うActivity
    このActivityは説明を行っているActivityだが、作成途中のMandalaChartオブジェクトをこのActivityまで保持するようにしている
 */
package com.example.ver2.activityClass.createActivityClass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;

public class MandalaChartTopActivity extends AppCompatActivity {

    private MandalaChart mandalaChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mandalachart_top);

        //一応このActivityまで作成途中のMandalaChartオブジェクトを保持しておく
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("MandalaChart")){
            mandalaChart = intent.getParcelableExtra("MandalaChart");
        }

        //次のActivity（MandalaChartPurposeActivity）へ遷移を行う
        Button nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_next = new Intent(MandalaChartTopActivity.this, MandalaChartPurposeActivity.class);
                //もし作成途中のMandalaChartオブジェクトがあった場合
                if(mandalaChart != null){
                    intent_next.putExtra("MandalaChart", mandalaChart);
                }
                startActivity(intent_next);
            }
        });

        //前のActivity（Create）
        Button backButton = findViewById(R.id.button_back);
    }
}
