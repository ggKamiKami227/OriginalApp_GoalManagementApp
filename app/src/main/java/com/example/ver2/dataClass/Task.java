/*
    GoalやPurposeに使用されるTaskクラス。これは単体ではデータベースに保存しない。
    Goalクラスなどに含まれて保存される
    Listでこのクラスのオブジェクトを管理する場合、Convertersクラスに@TypeConverterが設定されている
 */

package com.example.ver2.dataClass;

import static java.lang.String.valueOf;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.ver2.Converters;

import java.util.Date;

public class Task implements Parcelable {
    //taskの順番ごとにIDをつけている
    private int ID;
    private String name;
    private String description;
    private Date createDate;
    private Date startDate;
    private Date finishDate;
    private boolean state;

    //コンストラクタ
    public Task(int ID, String name, String description, Date createDate, Date startDate, Date finishDate, boolean state) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.state = state;
    }

    //ゲッター＆セッター
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    //Parcelableオブジェクトの管理
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(description);
//Serializableだと互換性がなくバグの原因になるため。また、始めにGoalをインスタンス化する際はすべてnullだからnullの場合の初期値を設定する必要がある
        Long createDateTimestamp = Converters.dateToTimestamp(createDate);
        Long startDateTimestamp = Converters.dateToTimestamp(startDate);
        Long finishDateTimestamp = Converters.dateToTimestamp(finishDate);

        //ここでは正常
//        Log.d("Task_Times Debug","createDate : " + valueOf(createDateTimestamp));
//        Log.d("Task_Times Debug","startDate : " + valueOf(startDateTimestamp));
//        Log.d("Task_Times Debug","finishDate : " + valueOf(finishDateTimestamp));

        // 3/18 13:40 ここのNullチェックを-1とした。0だと1970年~~~みたいになるらしい
        dest.writeLong(createDateTimestamp != null ? createDateTimestamp : -1L);
        dest.writeLong(startDateTimestamp != null ? startDateTimestamp : -1L);
        dest.writeLong(finishDateTimestamp != null ? finishDateTimestamp : -1L);

        Log.d("Task_Times Debug", "createDate : " + valueOf(createDateTimestamp));
        Log.d("Task_Times Debug", "startDate : " + valueOf(startDateTimestamp));
        Log.d("Task_Times Debug", "finishDate : " + valueOf(finishDateTimestamp));

        dest.writeByte((byte) (state ? 1 : 0));

    }

    protected Task(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        description = in.readString();
        //3/18 13:40
        long createDateMillis = in.readLong();
        long startDateMillis = in.readLong();
        long finishDateMillis = in.readLong();

        Log.d("Task_LongDateConvert(create)", valueOf(createDateMillis));
        Log.d("Task_LongDateConvert(start)", valueOf(startDateMillis));
        Log.d("Task_LongDateConvert(finish)", valueOf(finishDateMillis));

        createDate = (createDateMillis != -1L) ? Converters.fromTimestamp(createDateMillis) : null;
        startDate = (startDateMillis != -1L) ? Converters.fromTimestamp(startDateMillis) : null;
        finishDate = (finishDateMillis != -1L) ? Converters.fromTimestamp(finishDateMillis) : null;

        state = in.readByte() != 0;

        Log.d("Task", "Task - ID: " + ID + ", name: " + name + ", state: " + state);
        Log.d("Task", "createDate: " + createDate + ", startDate: " + startDate + ", finishDate: " + finishDate);
    }

    //Parcelableオブジェクトを生成するためのCreatorを定義
    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
