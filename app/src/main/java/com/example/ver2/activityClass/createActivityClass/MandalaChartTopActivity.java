/*
    マンダラチャートを利用した目的設定を行うActivity
    このActivityからMandalaChartを
 */
package com.example.ver2.activityClass.createActivityClass;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ver2.R;

public class MandalaChartTopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mandalachart_top);
    }
}
