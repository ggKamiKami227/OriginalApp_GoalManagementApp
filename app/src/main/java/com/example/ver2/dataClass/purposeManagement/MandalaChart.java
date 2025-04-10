package com.example.ver2.dataClass.purposeManagement;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import com.example.ver2.dataClass.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "mandala_charts")
public class MandalaChart extends Purpose implements Parcelable {
    private String purpose;
    //private List<String> goals; //chartと役割がかぶってる気がする
    private List<Chart> charts;

    //Typeの設定を忘れなこと
    public MandalaChart(int ID, String name, String description, Date createDate, Date startDate, Date finishDate, boolean state, List<Task> tasks, PurposeType type, List<Chart> charts) {
        super(ID, name, description, createDate, startDate, finishDate, state, tasks, type);
        this.purpose = purpose;
        //this.goals = goals;
        this.charts = charts;
    }


    public String getPurpose(){
        return purpose;
    }
    public Chart getChartbyID(int id){
        for(Chart chart:charts){
            if(chart.getID() == id){
                return chart;
            }
        }
        return null;
    }
    //public List<String> getGoals(){
    //    return goals;
    //}

    public List<Chart> getCharts(){
        return charts;
    }
    public void setPurpose(String purpose){
        this.purpose = purpose;
    }
    //public void setGoals(List<String> goals) {
    //    this.goals = goals;
    //}
    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }

    /*public void addGoal(String goal){
        if(goals == null){
            goals = new ArrayList<>();
        }
        goals.add(goal);
    }*/
    public void addChart(Chart chart){
        if(charts == null){
            charts = new ArrayList<>();
        }
        charts.add(chart);
    }

    //Parcelableの実装
    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(purpose);
        dest.writeTypedList(charts);
    }

    protected MandalaChart(Parcel in){
        super(in);
        purpose = in.readString();
        charts = in.createTypedArrayList(Chart.CREATOR);
    }

    public static final Creator<MandalaChart> CREATOR = new Creator<MandalaChart>(){
      @Override
      public MandalaChart createFromParcel(Parcel in){
          return new MandalaChart(in);
      }

      @Override
        public MandalaChart[] newArray(int size){
          return new MandalaChart[size];
      }
    };

}
