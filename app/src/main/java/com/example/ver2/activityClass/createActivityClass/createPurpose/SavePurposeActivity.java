/*
    Purposeオブジェクトをデータベースに保存するActivityで、それはサブクラスのMandalaChart、MemoPurposeで保存される
    名前、詳細、作成日（自動）、開始日、終了日、これらを入力する
 */

package com.example.ver2.activityClass.createActivityClass.createPurpose;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ver2.R;
import com.example.ver2.activityClass.MainActivity;
import com.example.ver2.dataClass.PurposeDataViewModel;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.dataClass.purposeManagement.Memo_Purpose;
import com.example.ver2.dataClass.purposeManagement.Purpose;
import com.example.ver2.dataClass.purposeManagement.PurposeType;

import java.util.Calendar;
import java.util.Date;

public class SavePurposeActivity extends AppCompatActivity {
    private final String purposeNameHint = "目的の名前を入力してください";
    private final String purposeDescriptionHint = "目的の説明を入力してください";

    //データベースとのやり取りを行うViewModel
    private PurposeDataViewModel purposeDataViewModel;

    private MandalaChart mandalaChart;
    private Memo_Purpose memoPurpose;

    private Purpose purpose;

    private EditText purposeNameEditText;
    private EditText purposeDescriptionEditText;

    private Date startDate;
    private Date finishDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_purpose_layout);

        purposeDataViewModel = new ViewModelProvider(this).get(PurposeDataViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            //オブジェクトの種類に応じて分ける。始めてインスタンス化する場合は、それぞれのタイプを設定する
            if (intent.hasExtra("MandalaChart")) {
                mandalaChart = intent.getParcelableExtra("MandalaChart");
                //二回目にこのクラスに遷移した場合、前に入力した情報を保持するためのコード。初めての場合は、ここで始めてインスタンス化する
                if (mandalaChart != null && mandalaChart.isPurposeExist()) {
                    purpose = new Purpose(mandalaChart.getPurpose(), mandalaChart.getDescription(), mandalaChart.getCreateDate(), mandalaChart.getStartDate(), mandalaChart.getFinishDate(), mandalaChart.getState(), mandalaChart.getType());
                } else {
                    purpose = new Purpose(null, null, null, null, null, false, PurposeType.MANDALA_CHART);
                }
            }
            setActivityComponent(purpose);
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String purposeName = purposeNameEditText.getText().toString();
                String purposeDescription = purposeDescriptionEditText.getText().toString();

                //Purposeの中身を設定
                purpose.setName(purposeName);
                purpose.setDescription(purposeDescription);
                purpose.setCreateDate(new Date());
                purpose.setStartDate(startDate);
                purpose.setFinishDate(finishDate);
                purpose.setState(false);

                if (mandalaChart != null) {
                    mandalaChart.updatePurpose(purpose);
                } else if (memoPurpose != null) {
                    memoPurpose.updatePurpose(purpose);
                }

                //データベースに保存。引数は5津で、purposeはupdatePurpose(purpose)に使用、そのほかの引数はnullチェックを用いたViewModel側での処理
                purposeDataViewModel.insertPurpose(purpose, mandalaChart, memoPurpose);

                //ホームに戻る
                Intent intent_next = new Intent(SavePurposeActivity.this, MainActivity.class);
                startActivity(intent_next);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String purposeName = purposeNameEditText.getText().toString();
                String purposeDescription = purposeDescriptionEditText.getText().toString();

                //Purposeの中身を設定
                purpose.setName(purposeName);
                purpose.setDescription(purposeDescription);
                purpose.setCreateDate(new Date());
                purpose.setStartDate(startDate);
                purpose.setFinishDate(finishDate);
                purpose.setState(false);

                if (mandalaChart != null) {
                    mandalaChart.updatePurpose(purpose);
                    Intent intent_before = new Intent(SavePurposeActivity.this, MandalaChartConfirmAllChartActivity.class);
                    intent_before.putExtra("MandalaChart", mandalaChart);
                    startActivity(intent_before);
                } else if (memoPurpose != null) {
                    //2025-05-09：作成中
                    memoPurpose.updatePurpose(purpose);
                }
            }
        });
    }

    //UIやカレンダーのリスナ設定するメソッド
    private void setActivityComponent(Purpose purpose) {
        //EditTextの処理
        purposeNameEditText = findViewById(R.id.purposeNameEditText);
        purposeDescriptionEditText = findViewById(R.id.purposeDescriptionEditText);

        if (purpose != null) {
            if (purpose.getName() != null && !purpose.getName().isEmpty())
                purposeNameEditText.setText(purpose.getName());
            else
                purposeNameEditText.setHint(purposeNameHint);

            if (purpose.getDescription() != null && !purpose.getDescription().isEmpty())
                purposeDescriptionEditText.setText(purpose.getDescription());
            else
                purposeDescriptionEditText.setHint(purposeDescriptionHint);
        }

        //カレンダー
        Calendar calendar = Calendar.getInstance();

        DatePicker startDatePicker = findViewById(R.id.startDatePicker);
        DatePicker finishDatePicker = findViewById(R.id.finishDatePicker);

        if (purpose.getStartDate() != null) {
            calendar.setTime(purpose.getStartDate());
            startDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            //日付をこのクラスの変数にアタッチしておく
            startDate = purpose.getStartDate();
        } else {
            //nullだった場合、現在の日付を入れる
            startDate = calendar.getTime();
        }

        if (purpose.getFinishDate() != null) {
            calendar.setTime(purpose.getFinishDate());
            finishDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            //日付をこのクラスの変数にアタッチしておく
            finishDate = purpose.getFinishDate();
        } else {
            //nullだった場合、現在の日付を入れる
            finishDate = calendar.getTime();
        }

        // DatePicker のリスナー設定 (API レベル 26 以上)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDatePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(year, monthOfYear, dayOfMonth);
                startDate = calendar.getTime();
            });

            finishDatePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(year, monthOfYear, dayOfMonth);
                finishDate = calendar.getTime();
            });
        }

    }
}
