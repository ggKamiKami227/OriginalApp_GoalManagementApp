/*
    目的設定で使用するフレームワークを選択するActivity
 */

package com.example.ver2.activityClass.createActivityClass.createPurpose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;
import com.example.ver2.activityClass.createActivityClass.CreateTopChooseActivity;

public class CreatePurposeChooseFrameworkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_purpose_choose_framework);

        //MandalaChart
        Button mandalaChartButton = findViewById(R.id.MandalaChart_Button);
        mandalaChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatePurposeChooseFrameworkActivity.this, MandalaChartTopActivity.class);
                startActivity(intent);
            }
        });

        //自由記述形式
        Button memoPurposeButton = findViewById(R.id.Memo_Purpose_Button);
        memoPurposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

        //前のActivity（CreateTopChooseActivity）へ遷移する
        Button backButton = findViewById(R.id.create_purpose_choose_framework_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatePurposeChooseFrameworkActivity.this, CreateTopChooseActivity.class);
                startActivity(intent);
            }
        });
    }
}
