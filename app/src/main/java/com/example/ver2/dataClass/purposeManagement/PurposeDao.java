package com.example.ver2.dataClass.purposeManagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface PurposeDao {
    //新しいPurposeオブジェクトをデータベースに挿入
    @Insert
    void insert(Purpose purpose);

    //既存のPurposeオブジェクトを更新
    @Update
    void update(Purpose purpose);

    //指定されたPurposeオブジェクトをデータベースから削除
    @Delete
    void delete(Purpose purpose);

    //指定されたPurposeオブジェクトをidを使用してデータベースから削除
    @Query("DELETE FROM purposes WHERE ID = :id")
    void deleteById(int id);

    //データベース内のすべてのPurposeオブジェクトを取得
    @Query("SELECT * FROM purposes")
    List<Purpose> getAllPurposes();

    //指定されたIDに対応するPurposeオブジェクトを取得
    @Query("SELECT * FROM purposes WHERE ID = :id")
    Purpose getPurposeById(int id);

    //Purposeオブジェクトのリストを取得
    @Query("SELECT * FROM purposes WHERE type = :type")
    List<Purpose> getPurposesByType(String type);

}
