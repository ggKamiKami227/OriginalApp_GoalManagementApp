/*
    TaskクラスやChartクラスなど、属性にオブジェクトを入れる場合、データベースに保存する場合とき、このコンバーターを使う必要がある。
 */
package com.example.ver2;

import androidx.room.TypeConverter;

import com.example.ver2.dataClass.Task;
import com.example.ver2.dataClass.goalManagement.GoalType;
import com.example.ver2.dataClass.purposeManagement.Chart;
import com.example.ver2.dataClass.purposeManagement.PurposeType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

public class Converters {
    //Dateクラス：日にちのクラス。Java.utilのやつ
    private static final Gson gson = new Gson();
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }

    //Taskクラス
    @TypeConverter
    public static String fromTaskList(List<Task> tasks){
        return gson.toJson(tasks);
    }

    @TypeConverter
    public static List<Task> toTaskList(String value){
        return gson.fromJson(value, new TypeToken<List<Task>>() {}.getType());
    }

    //GoalTypeクラス
    @TypeConverter
    public static String fromGoalType(GoalType goalType) {
        return goalType == null ? null : goalType.name(); // Enumの名前を文字列として保存
    }

    @TypeConverter
    public static GoalType toGoalType(String value) {
        return value == null ? null : GoalType.valueOf(value); // 文字列からEnumに変換
    }

    //Chartクラス
    @TypeConverter
    public static String fromChartList(List<Chart> charts){
        return gson.toJson(charts);
    }

    @TypeConverter
    public static List<Chart> toChartList(String value){
        return gson.fromJson(value, new TypeToken<List<Chart>>() {}.getType());
    }

    //PurposeTypeクラス
    @TypeConverter
    public static String fromPurposeType(PurposeType purposeType){
        return purposeType == null ? null : purposeType.name();
    }

    @TypeConverter
    public  static PurposeType toPurposeType(String value){
        return value == null ? null : PurposeType.valueOf(value); //文字列からEnumに変換
    }
}
