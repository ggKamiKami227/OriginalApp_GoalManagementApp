/*
    マンダラチャートの周りの3*3のチャートを完成させていくActivity。
    ボタンでCoreChartが構成されていて、設定したいChartのボタンを押すことで、Fragmentを表示し、そこで入力させる。
 */
package com.example.ver2.activityClass.createActivityClass;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ver2.R;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.fragmentClass.confirmFragments.MandalaChartBottomChartFragment;
import com.example.ver2.fragmentClass.viewModels.MandalaChartBottomChartViewModel;

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

    //チャートはボタンで構成する。真ん中のPurposeButtonのみテキストを変更するだけでリスナは付けない
    private Button topLeftButton, topButton, topRightButton;
    private Button leftButton, purposeButton, rightButton;
    private Button bottomLeftButton, bottomButton, bottomRightButton;

    private MandalaChart mandalaChart;

    //Fragmentと情報を共有するためのViewModel
    MandalaChartBottomChartViewModel mandalaChartBottomChartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_chart_create_task);

        topLeftButton = findViewById(R.id.goal_button_1);
        topButton = findViewById(R.id.goal_button_2);
        topRightButton = findViewById(R.id.goal_button_3);
        leftButton = findViewById(R.id.goal_button_4);
        purposeButton = findViewById(R.id.purpose_button);
        rightButton = findViewById(R.id.goal_button_5);
        bottomLeftButton = findViewById(R.id.goal_button_6);
        bottomButton = findViewById(R.id.goal_button_7);
        bottomRightButton = findViewById(R.id.goal_button_8);

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
                /* //2025-04-29 このあと、確認画面に移行するか、このまま保存画面に移行するか
                Intent intent_next = new Intent(MandalaChartExpansionActivity.this, )
                intent_next.putExtra("MandalaChart",mandalaChart);
                startActivity(intent_next);;
                */
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
        private void updateButtonText () {
            //リストでまとめる
            //purposeボタン
            purposeButton.setText(mandalaChart.getPurpose());


            //MandalaChartクラスのチャートを指定して取得
            if (mandalaChart.getChartByID(TopLeft) != null) {
                if (mandalaChart.getChartByID(TopLeft).getGoal() != null && !mandalaChart.getChartByID(TopLeft).getGoal().isEmpty()) {
                    topLeftButton.setText(mandalaChart.getChartByID(TopLeft).getGoal());
                } else {
                    topLeftButton.setText(textNothing);
                }
            } else {
                topLeftButton.setText(textNothing);
            }

            if (mandalaChart.getChartByID(Top) != null) {
                if (mandalaChart.getChartByID(Top).getGoal() != null) {
                    topButton.setText(mandalaChart.getChartByID(Top).getGoal());
                } else {
                    topButton.setText(textNothing);
                }
            } else {
                topButton.setText(textNothing);
            }

            if (mandalaChart.getChartByID(TopRight) != null) {
                if (mandalaChart.getChartByID(TopRight).getGoal() != null) {
                    topRightButton.setText(mandalaChart.getChartByID(TopRight).getGoal());
                } else {
                    topRightButton.setText(textNothing);
                }
            } else {
                topRightButton.setText(textNothing);
            }
            if (mandalaChart.getChartByID(Left) != null) {
                if (mandalaChart.getChartByID(Left).getGoal() != null) {
                    leftButton.setText(mandalaChart.getChartByID(Left).getGoal());
                } else {
                    leftButton.setText(textNothing);
                }
            } else {
                leftButton.setText(textNothing);
            }

            if (mandalaChart.getChartByID(Right) != null) {
                if (mandalaChart.getChartByID(Right).getGoal() != null) {
                    rightButton.setText(mandalaChart.getChartByID(Right).getGoal());
                } else {
                    rightButton.setText(textNothing);
                }
            } else {
                rightButton.setText(textNothing);
            }

            if (mandalaChart.getChartByID(BottomLeft) != null) {
                if (mandalaChart.getChartByID(BottomLeft).getGoal() != null) {
                    bottomLeftButton.setText(mandalaChart.getChartByID(BottomLeft).getGoal());
                } else {
                    bottomLeftButton.setText(textNothing);
                }
            } else {
                bottomLeftButton.setText(textNothing);
            }

            if (mandalaChart.getChartByID(Bottom) != null) {
                if (mandalaChart.getChartByID(Bottom).getGoal() != null) {
                    bottomButton.setText(mandalaChart.getChartByID(Bottom).getGoal());
                } else {
                    bottomButton.setText(textNothing);
                }
            } else {
                bottomButton.setText(textNothing);
            }

            if (mandalaChart.getChartByID(BottomRight) != null) {
                if (mandalaChart.getChartByID(BottomRight).getGoal() != null) {
                    bottomRightButton.setText(mandalaChart.getChartByID(BottomRight).getGoal());
                } else {
                    bottomRightButton.setText(textNothing);
                }
            } else {
                bottomButton.setText(textNothing);
            }
        }
    }
