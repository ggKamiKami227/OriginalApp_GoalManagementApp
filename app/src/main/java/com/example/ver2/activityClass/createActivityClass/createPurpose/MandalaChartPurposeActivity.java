/*
    MandalaChartを利用した目的設定の目的を入力するActivity
    ここで始めてMandalaChartオブジェクトを作成する
 */

package com.example.ver2.activityClass.createActivityClass.createPurpose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;

public class MandalaChartPurposeActivity extends AppCompatActivity {
    private EditText purposeEditText;

    private MandalaChart mandalaChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mandalachart_purpose);

        purposeEditText = findViewById(R.id.purposeEditText);

        //作成途中のMandalaChartオブジェクトを保持する、またはそれがない場合新たにインスタンス化する
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MandalaChart")) {
            mandalaChart = intent.getParcelableExtra("MandalaChart");
            if (mandalaChart != null) {
                purposeEditText.setText(mandalaChart.getPurpose());
            }
        }

        //Chartを完成させていくActivity（MandalaChart）に遷移する
        Button nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MandalaChartを保存する（新規か上書か）
                String purpose = purposeEditText.getText().toString();
                if(mandalaChart == null){
                    mandalaChart = new MandalaChart(purpose);
                }else {
                    mandalaChart.setPurpose(purpose);
                }
                Intent intent_next = new Intent(MandalaChartPurposeActivity.this, MandalaChartCoreChartActivity.class);
                intent_next.putExtra("MandalaChart",mandalaChart);
                startActivity(intent_next);
            }
        });

        //前のActivity（MandalaChartTopActivity）に戻る。そこまではMandalaChartオブジェクトを保持する
        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MandalaChartを保存する（新規か上書か）
                String purpose = purposeEditText.getText().toString();
                if(mandalaChart == null){
                    mandalaChart = new MandalaChart(purpose);
                }else {
                    mandalaChart.setPurpose(purpose);
                }
                Intent intent_before = new Intent(MandalaChartPurposeActivity.this, MandalaChartTopActivity.class);
                intent_before.putExtra("MandalaChart", mandalaChart);
                startActivity(intent_before);
            }
        });

    }
}
