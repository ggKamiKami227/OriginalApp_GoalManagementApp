/*
    Activityからデータベースに保存や取得を行うためのViewModel
 */

package com.example.ver2.dataClass;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
/*
    LiveDataについて
    Android Jetpackのコンポーネントの一つで、監視可能なデータホルダー。
    LiveDataで保持しているデータが変更されると、それを監視しているオブジェクト（主にUI）に自動的に通知される仕組み
 */
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ver2.dataClass.goalManagement.Benchmarking;
import com.example.ver2.dataClass.goalManagement.Goal;
import com.example.ver2.dataClass.goalManagement.Memo_Goal;
import com.example.ver2.dataClass.goalManagement.SMART;
import com.example.ver2.dataClass.goalManagement.WillCanMust;
import com.example.ver2.dataClass.goalManagement.GoalType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoalDataViewModel extends AndroidViewModel {
    //データベースのアクセスを提供
    private final AppDatabase db;
    //データベース操作を非同期で実行するため
    private final ExecutorService executorService;
    //データベースから取得したGoalオブジェクトを保持してUIに通知
    private final MutableLiveData<List<Goal>> allGoals = new MutableLiveData<>();

    //引数のApplication：このアプリそのものを示していて、アプリケーションレベルのコンテキストやグローバルな状態へのアクセスを提供する。
    public GoalDataViewModel(Application application) {
        super(application);
        //データベースのインスタンスを取得
        db = AppDatabase.getInstance(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    //外部から変更させないため戻り値をLiveDataに
    public LiveData<List<Goal>> getAllGoals() {
        return allGoals;
    }

    //データベースに保存されているGoalをスーパークラスとするサブクラス(SMART,WillCanMust,Benchmarking,Memo_Goal)
    //を取得してgoalsにGoalオブジェクトとして入れる。ConfirmGoalListActivityでGoalオブジェクトだけ表示したい場合に使用。
    public void loadGoalListFromDatabase() {
        executorService.execute(() -> {
            try {
                List<SMART> smartGoals = db.smartDao().getAllSMART();
                List<Benchmarking> benchmarkingGoals = db.benchmarkingDao().getAllBenchmarking();
                List<WillCanMust> wcmGoals = db.wcmDao().getAllWillCanMust();
                List<Memo_Goal> memoGoals = db.memoGoalDao().getAllMemoGoals();

                Log.d("Debug:Load'&SaveData", "SMART Goals: " + smartGoals.size());
                Log.d("Debug:Load'&SaveData", "Benchmarking Goals: " + benchmarkingGoals.size());
                Log.d("Debug:Load'&SaveData", "WCM Goals: " + wcmGoals.size());
                Log.d("Debug:Load'&SaveData", "Memo Goals: " + memoGoals.size());

                List<Goal> newGoalList = new ArrayList<>();
                newGoalList.addAll(smartGoals);
                newGoalList.addAll(benchmarkingGoals);
                newGoalList.addAll(wcmGoals);
                newGoalList.addAll(memoGoals);

                allGoals.postValue(newGoalList);
            } catch (Exception e) {
                Log.e("GoalDataViewModel:Load", "Error loading goals", e);
            }
        });
    }

    //ゲッター。それぞれのクラスごとに分けて、idを用いて検索 LiveDataで返している(外部から変更されないため)。
    public LiveData<SMART> getSMARTByID(int id) {
        return db.smartDao().getSMARTById(id);
    }

    public LiveData<WillCanMust> getWCMByID(int id) {
        return db.wcmDao().getWillCanMustById(id);
    }

    public LiveData<Benchmarking> getBenchmarkingByID(int id) {
        return db.benchmarkingDao().getBenchmarkingById(id);
    }

    public LiveData<Memo_Goal> getMemoGoalByID(int id) {
        return db.memoGoalDao().getMemoGoalById(id);
    }

    //削除メソッド。idを用いて削除するデータを検索。基本的にConfirmGoalListActivityから削除するので、削除した際に、データベースを再取得する必要がある。
    public void deleteSMARTByID(int id) {
        executorService.execute(() -> {
            try {
                db.smartDao().deleteById(id);
                //デバッグ：削除後、もう一度GoalListを表示すると落ちる
                //削除後にデータベースを再取得
                loadGoalListFromDatabase();
            } catch(Exception e){
                Log.e("GoalDataViewModel:Delete", "Error deleting SMART goal", e);
            }
        });
    }

    public void deleteWCMByID(int id) {
        executorService.execute(() -> {
            try {
                db.wcmDao().deleteById(id);
                loadGoalListFromDatabase();
            } catch(Exception e){
                Log.e("GoalDataViewModel:Delete", "Error deleting WillCanMust goal", e);
            }
        });
    }

    public void deleteBenchmarkingByID(int id) {
        executorService.execute(() -> {
            try {
                db.benchmarkingDao().deleteById(id);
                loadGoalListFromDatabase();
            }catch(Exception e){
                Log.e("GoalDataViewModel:Delete", "Error deleting Benchmarking goal", e);
            }
        });
    }

    public void deleteMemoGoalByID(int id) {
        executorService.execute(() -> {
            try {
                db.memoGoalDao().deleteById(id);
                loadGoalListFromDatabase();
            } catch(Exception e){
                Log.e("GoalDataViewModel:Delete", "Error deleting Memo_Goal goal", e);
            }
        });
    }

    //Goalの編集時に使用される、データベースに保存されているデータを編集する際に使用
    public void updateSMART(SMART smart) {
        executorService.execute(() -> db.smartDao().update(smart));
    }

    public void updateBenchmarking(Benchmarking benchmarking) {
        executorService.execute(() -> db.benchmarkingDao().update(benchmarking));
    }

    public void updateWCM(WillCanMust wcm) {
        executorService.execute(() -> db.wcmDao().update(wcm));
    }

    public void updateMemo_Goal(Memo_Goal memo) {
        executorService.execute(() -> db.memoGoalDao().update(memo));
    }

    //新しい目標を追加する際に使用するメソッド。nullチェックを用いて、nullじゃないオブジェクト（保存するオブジェクト）を判断する
    //一応、Goalオブジェクトも送られてきて、ここでもう一度サブクラス内のスーパークラスの属性などを保存して、Typeもここで決めてから保存される
    public void insertGoal(Goal goal, WillCanMust wcm, Benchmarking benchmarking, SMART smart, Memo_Goal memo) {
        executorService.execute(() -> {
            try {
                if (wcm != null) {
                    wcm.updateGoal(goal);
                    wcm.setType(GoalType.WILL_CAN_MUST);
                    db.wcmDao().insert(wcm);
                } else if (benchmarking != null) {
                    benchmarking.updateGoal(goal);
                    benchmarking.setType(GoalType.BENCHMARKING);
                    db.benchmarkingDao().insert(benchmarking);
                } else if (smart != null) {
                    smart.updateGoal(goal);
                    smart.setType(GoalType.SMART);
                    db.smartDao().insert(smart);
                } else if (memo != null) {
                    memo.updateGoal(goal);
                    memo.setType(GoalType.MEMO_GOAL);
                    db.memoGoalDao().insert(memo);
                }
            } catch(Exception e){
                Log.e("GoalDataViewModel:Insert", "Error inserting goal", e);
            }
        });
    }


}
