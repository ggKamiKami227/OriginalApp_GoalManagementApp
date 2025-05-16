/*
    マンダラチャートの周りの3*3のチャートを完成させていくActivity。
    ボタンでCoreChartが構成されていて、設定したいChartのボタンを押すことで、Fragmentを表示し、そこで入力させる。
 */
package com.example.ver2.activityClass.createActivityClass.createPurpose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ver2.R;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.fragmentClass.purposeFragment.MandalaChartBottomChartFragment;
import com.example.ver2.fragmentClass.viewModels.MandalaChartBottomChartViewModel;

import java.util.HashMap;
import java.util.Map;

public class MandalaChartExpansionActivity extends AppCompatActivity {
    //もしCoreChartで埋まってない場所があった場合、そのChartは開けないようにして、ボタンにはこの文章を入れる
    private final String textNothing = "目標が入力されていません";
    //ボタンの位置と数字を同期させるための変数
    private final int TopLeft = 1;
    private final int Top = 2;
    private final int TopRight = 3;
    private final int Left = 4;
    private final int Right = 5;
    private final int BottomLeft = 6;
    private final int Bottom = 7;
    private final int BottomRight = 8;

    private TableLayout buttonTableLayout;

    //チャートはボタンで構成する。真ん中のPurposeButtonのみテキストを変更するだけでリスナは付けない
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, purposeButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;
    //ボタンの色を変更するときに使う
    private Button currentSelectedButton;

    private MandalaChart mandalaChart;

    //Fragmentと情報を共有するためのViewModel
    MandalaChartBottomChartViewModel mandalaChartBottomChartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_chart_create_task);

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


        mandalaChartBottomChartViewModel = new ViewModelProvider(MandalaChartExpansionActivity.this).get(MandalaChartBottomChartViewModel.class);

        //作成途中のものを適用
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MandalaChart")) {
            mandalaChart = intent.getParcelableExtra("MandalaChart");
            if (mandalaChart != null) {
                //ViewModelの情報をアップデート
                mandalaChartBottomChartViewModel.updateMandalaChart(mandalaChart);
            }
        }

        //UIの更新。ViewModelが保持するLiveDataが変更された際に通知され更新される
        mandalaChartBottomChartViewModel.getMandalaChartLiveData().observe(this, currentMandalaChart -> {
            if (currentMandalaChart != null) {
                mandalaChart = currentMandalaChart;
            }
            updateButtonText();
        });

        //Chartのボタンのリスナ設定メソッド
        setChartButtonListener();

        Button nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ボタンの色を変更
                Button clickedButton = (Button) view;
                if (currentSelectedButton != null && currentSelectedButton != clickedButton) {
                    currentSelectedButton.setSelected(false);
                }
                clickedButton.setSelected(true);
                currentSelectedButton = clickedButton;

                //確認画面に移行
                Intent intent_next = new Intent(MandalaChartExpansionActivity.this, MandalaChartConfirmAllChartActivity.class);
                intent_next.putExtra("MandalaChart", mandalaChart);
                startActivity(intent_next);
            }
        });

        Button backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_before = new Intent(MandalaChartExpansionActivity.this, MandalaChartCoreChartActivity.class);
                intent_before.putExtra("MandalaChart", mandalaChart);
                startActivity(intent_before);
            }
        });
    }

    //ボタンを押したときの動作。それぞれのボタンごとにIDをViewModel側に送り、Fragment側がそれを読み取ってどのチャートか判断する
    private void setChartButtonListener() {
        topLeftButton.setOnClickListener(createChartButtonClickListener(TopLeft));
        topButton.setOnClickListener(createChartButtonClickListener(Top));
        topRightButton.setOnClickListener(createChartButtonClickListener(TopRight));
        leftButton.setOnClickListener(createChartButtonClickListener(Left));
        rightButton.setOnClickListener(createChartButtonClickListener(Right));
        bottomLeftButton.setOnClickListener(createChartButtonClickListener(BottomLeft));
        bottomButton.setOnClickListener(createChartButtonClickListener(Bottom));
        bottomRightButton.setOnClickListener(createChartButtonClickListener(BottomRight));

    }

    //ボタンのリスナを省略して書くためのメソッド（数が多いのでまとめないと冗長で読みずらい）
    private View.OnClickListener createChartButtonClickListener(final int chartID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mandalaChartBottomChartViewModel.selectChartID(chartID);
                MandalaChart currentMandalaChart = mandalaChartBottomChartViewModel.getMandalaChartLiveData().getValue();
                if (currentMandalaChart != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("mandalaChart", currentMandalaChart);

                    MandalaChartBottomChartFragment fragment = new MandalaChartBottomChartFragment();
                    fragment.setArguments(bundle);
                    fragment.show(getSupportFragmentManager(), "MandalaChartBottomChartFragment");
                }
            }
        };
    }

    //ボタンのテキストを更新するメソッド
    private void updateButtonText() {
        //purposeボタン
        purposeButton.setText(mandalaChart.getPurpose());

        //Mapを使うことでコンパクトに書く
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
            //Integerとintの関係に注意IntegerはNullアリだけど、intはなしだから、Nullエラーが出る可能性がある
            Integer chartId = entry.getKey();
            Button button = entry.getValue();
            if (mandalaChart.getChartByID(chartId) != null) {
                if (mandalaChart.getChartByID(chartId).getGoal() != null) {
                    if (!mandalaChart.getChartByID(chartId).getGoal().isEmpty()) {
                        button.setText(mandalaChart.getChartByID(chartId).getGoal());
                    }else{
                        button.setText(textNothing);
                    }
                } else {
                    button.setText(textNothing);
                }
            }else{
                button.setText(textNothing);
            }
        }
    }
}
