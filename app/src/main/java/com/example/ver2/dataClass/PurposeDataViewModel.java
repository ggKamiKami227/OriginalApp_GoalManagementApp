/*
    Activityからデータベースに保存や取得を行うためのViewModel
 */

package com.example.ver2.dataClass;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.dataClass.purposeManagement.MemoPurposeDao;
import com.example.ver2.dataClass.purposeManagement.Memo_Purpose;
import com.example.ver2.dataClass.purposeManagement.Purpose;
import com.example.ver2.dataClass.purposeManagement.PurposeType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PurposeDataViewModel extends AndroidViewModel {
    //データベースのアクセスを提供
    private final AppDatabase db;
    //データベース操作を非同期で実行するため
    private final ExecutorService executorService;
    //データベースから取得したPurposeオブジェクトを保持してUIに通知（おそらくList化して見せる場合に用いる？）
    private final MutableLiveData<List<Purpose>> allPurposes = new MutableLiveData<>();

    //引数のApplication：このアプリそのものを示していて、アプリケーションレベルのコンテキストやグローバルな状態へのアクセスを提供する。
    public PurposeDataViewModel(Application application) {
        super(application);
        //データベースのインスタンスを取得
        db = AppDatabase.getInstance(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    //外部から変更させないため戻り値をLiveDataに
    public LiveData<List<Purpose>> getAllPurposes() {
        return allPurposes;
    }

    //データベースに保存されているPurposeをスーパークラスとするサブクラス（MandalaChart、Purpose_Memo）
    //を取得してpurposesにPurposeオブジェクトとして入れる。Purposeのリスト（Purposeに入っている情報だけ）を作る時
    public void loadPurposeListFromDatabase() {
        executorService.execute(() -> {
            try {
                List<MandalaChart> mandalaCharts = db.mandalaChartDao().getAllMandalaCharts();
                List<Memo_Purpose> memoPurposes = db.memoPurposeDao().getAllMemoPurposes();

                List<Purpose> newPurposeList = new ArrayList<>();
                newPurposeList.addAll(mandalaCharts);
                newPurposeList.addAll(memoPurposes);

                allPurposes.postValue(newPurposeList);
            } catch (Exception e) {
                Log.e("PurposeDataViewModel:Load", "Error loading goals", e);
            }
        });
    }

    //ゲッター。それぞれのクラスごとに分けて、idを用いて検索、LiveDataで返している
    public LiveData<MandalaChart> getMandalaChartByID(int id) {
        return db.mandalaChartDao().getMandalaChartById(id);
    }

    //public LiveData<Memo_Purpose> getMemo_PurposeByID(int id){ return db.memoPurposeDao().getMemoPurposeById(id); }

    //新しい目的を追加する際に使用するメソッド。nullチェックを用いて、nullじゃないオブジェクト（保存するオブジェクト）を判断する
    //一応、Purposeオブジェクトも送られてきて、ここでもう一度サブクラス内のスーパークラスの属性などを変更して、Typeもここで決めて保存される
    public void insertPurpose(Purpose purpose, MandalaChart mandalaChart, Memo_Purpose memo) {
        executorService.execute(() -> {
            try{
                if(mandalaChart != null){
                    mandalaChart.updatePurpose(purpose);
                    mandalaChart.setType(PurposeType.MANDALA_CHART);
                    db.mandalaChartDao().insert(mandalaChart);
                }else if(memo != null){
                    memo.updatePurpose(purpose);
                    memo.setType(PurposeType.MEMO_PURPOSE);
                    db.memoPurposeDao().insert(memo);
                }
            }catch(Exception e){
                Log.e("PurposeDataViewModel:Insert","Error inserting purpose", e);
            }
        });
    }
}
