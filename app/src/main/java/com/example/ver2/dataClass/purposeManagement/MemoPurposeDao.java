package com.example.ver2.dataClass.purposeManagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface MemoPurposeDao {
    // 新しいMemo_Purposeオブジェクトをデータベースに挿入
    @Insert
    void insert(Memo_Purpose memoPurpose);

    // 既存のMemo_Purposeオブジェクトを更新
    @Update
    void update(Memo_Purpose memoPurpose);

    // 指定されたMemo_Purposeオブジェクトをデータベースから削除
    @Delete
    void delete(Memo_Purpose memoPurpose);

    //　指定されたMemo_Purposeオブジェクトをデータベースから削除
    @Query("DELETE FROM mandala_charts WHERE id = :id")
    void deleteById(int id);

    // データベース内のすべてのMemo_Purposeオブジェクトを取得
    @Query("SELECT * FROM memo_purposes")
    List<Memo_Purpose> getAllMemoPurposes();

    // 指定されたIDに対応するMemo_Purposeオブジェクトを取得
    @Query("SELECT * FROM memo_purposes WHERE ID = :id")
    Memo_Purpose getMemoPurposeById(int id);

    // 指定された状態に対応するMemo_Purposeオブジェクトのリストを取得
    @Query("SELECT * FROM memo_purposes WHERE state = :state")
    List<Memo_Purpose> getMemoPurposesByState(boolean state);
}