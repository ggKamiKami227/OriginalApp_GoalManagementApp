package com.example.ver2.dataClass.goalManagement;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface GoalDao {
    // 新しいGoalオブジェクトをデータベースに挿入
    @Insert
    void insert(Goal goal);

    // 既存のGoalオブジェクトを更新
    @Update
    void update(Goal goal);

    // 指定されたGoalオブジェクトをデータベースから削除
    @Delete
    void delete(Goal goal);

    // 指定されたGoalオブジェクトをidを使用してデータベースから削除(ワンちゃんこのidが大文字小文字でバグがあるかも)
    @Query("DELETE FROM goals WHERE id = :id")
    void deleteById(int id);

    // データベース内のすべてのGoalオブジェクトを取得
    @Query("SELECT * FROM goals")
    List<Goal> getAllGoals();

    // 指定されたIDに対応するGoalオブジェクトを取得
    @Query("SELECT * FROM goals WHERE ID = :id")
    LiveData<Goal> getGoalById(int id);

    // 指定された状態に対応するGoalオブジェクトのリストを取得
    @Query("SELECT * FROM goals WHERE state = :state")
    List<Goal> getGoalsByState(boolean state);
}