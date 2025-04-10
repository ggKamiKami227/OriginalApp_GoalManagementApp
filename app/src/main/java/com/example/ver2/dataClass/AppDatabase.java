/*
    RoomDatabaseを継承した抽象クラス。データベースの定義とアクセスを提供
 */

package com.example.ver2.dataClass;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ver2.Converters;
import com.example.ver2.dataClass.goalManagement.Benchmarking;
import com.example.ver2.dataClass.goalManagement.BenchmarkingDao;
import com.example.ver2.dataClass.goalManagement.Goal;
import com.example.ver2.dataClass.goalManagement.GoalDao;
import com.example.ver2.dataClass.goalManagement.MemoGoalDao;
import com.example.ver2.dataClass.goalManagement.Memo_Goal;
import com.example.ver2.dataClass.goalManagement.SMART;
import com.example.ver2.dataClass.goalManagement.SMARTDao;
import com.example.ver2.dataClass.goalManagement.WillCanMust;
import com.example.ver2.dataClass.goalManagement.WillCanMustDao;
import com.example.ver2.dataClass.purposeManagement.MandalaChart;
import com.example.ver2.dataClass.purposeManagement.MandalaChartDao;
import com.example.ver2.dataClass.purposeManagement.MemoPurposeDao;
import com.example.ver2.dataClass.purposeManagement.Memo_Purpose;
import com.example.ver2.dataClass.purposeManagement.Purpose;
import com.example.ver2.dataClass.purposeManagement.PurposeDao;

// 2025/04/10 Purposeを追加しバージョンアップ:6
@Database(entities = {Goal.class,WillCanMust.class, SMART.class, Memo_Goal.class, Benchmarking.class, Purpose.class, MandalaChart.class, Memo_Purpose.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class}) // TypeConverter を指定
public abstract class AppDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract WillCanMustDao wcmDao();
    public abstract SMARTDao smartDao();
    public abstract BenchmarkingDao benchmarkingDao();
    public abstract MemoGoalDao memoGoalDao();

    public abstract PurposeDao purposeDao();
    public abstract MandalaChartDao mandalaChartDao();
    public abstract MemoPurposeDao memoPurposeDao();

    private static volatile AppDatabase INSTANCE;

    //シングルトンパターンでアプリ全体で共有する
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // バージョン変更時にリセット（開発用）
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
