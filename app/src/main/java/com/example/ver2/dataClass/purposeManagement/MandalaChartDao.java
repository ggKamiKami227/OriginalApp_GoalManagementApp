package com.example.ver2.dataClass.purposeManagement;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface MandalaChartDao {
    // 新しいMandalaChartオブジェクトをデータベースに挿入
    @Insert
    void insert(MandalaChart mandalaChart);

    // 既存のMandalaChartオブジェクトを更新
    @Update
    void update(MandalaChart mandalaChart);

    // 指定されたMandalaChartオブジェクトをデータベースから削除
    @Delete
    void delete(MandalaChart mandalaChart);

    //指定されたMandalaChartオブジェクトをidで指定してデータベースから削除
    @Query("DELETE FROM mandala_charts WHERE id = :id")
    void deleteById(int id);

    // データベース内のすべてのMandalaChartオブジェクトを取得
    @Query("SELECT * FROM mandala_charts")
    List<MandalaChart> getAllMandalaCharts();

    // 指定されたIDに対応するMandalaChartオブジェクトを取得
    @Query("SELECT * FROM mandala_charts WHERE ID = :id")
    LiveData<MandalaChart> getMandalaChartById(int id);

    // 指定された目的に関連するMandalaChartオブジェクトのリストを取得
    @Query("SELECT * FROM mandala_charts WHERE purpose = :purpose")
    List<MandalaChart> getMandalaChartsByPurpose(String purpose);
}
