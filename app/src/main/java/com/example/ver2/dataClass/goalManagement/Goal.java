/*
    目標データの基本となるクラス。これをスーパークラスとして、フレームワークごとにサブクラスを作成。
*/

package com.example.ver2.dataClass.goalManagement;

import static java.lang.String.valueOf;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.*;

import androidx.room.TypeConverters;

import com.example.ver2.dataClass.Task;
//データベースにそのまま入れられないオブジェクトを入れられるように変換するクラス
import com.example.ver2.Converters;

@Entity(tableName = "goals")
@TypeConverters(Converters.class)
public class Goal implements Parcelable {
    //これは自動生成されるからコンストラクタに入れる必要はない
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String name;
    private String description;
    @TypeConverters(Converters.class)
    private Date createDate;
    @TypeConverters(Converters.class)
    private Date startDate;
    @TypeConverters(Converters.class)
    private Date finishDate;
    private boolean state;
    @TypeConverters(Converters.class)
    private List<Task> tasks;
    //どのフレームワークを使用しているか（クラス）判別するため
    private GoalType type;

    // コンストラクタ（IDは自動生成）
    public Goal(String name, String description, Date createDate, Date startDate, Date finishDate, boolean state, List<Task> tasks, GoalType type) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.state = state;
        this.tasks = tasks != null ? tasks : new ArrayList<>(); //null対策
        this.type = type;
    }

    //コピーコンストラクタ：Goalオブジェクトだけ独立させて編集したりする際に使用される
    @Ignore
    public Goal(Goal goal) {
        //private変数は同じクラス内であれば、どのインスタンスからでもアクセスできる(=引数のGoalオブジェクトはこのGoalオブジェクトからアクセスしているため取得可能)
        this(goal.name, goal.description, goal.createDate, goal.startDate, goal.finishDate, goal.state, goal.tasks, goal.type);
    }

    //Goalオブジェクトの中身だけ変更したい場合に使用
    public void updateGoal(Goal goal) {
        this.name = goal.getName();
        this.description = goal.getDescription();
        this.createDate = goal.getCreateDate();
        this.startDate = goal.getStartDate();
        this.finishDate = goal.getFinishDate();
        this.state = goal.isState();
        this.tasks = goal.getTasks();
        this.type = goal.getType();
    }

    // ゲッター&セッター
    public int getID() {
        return ID;
    }

    //IDのセッターは必要ない可能性あり
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    public GoalType getType() {
        return type;
    }

    public void setType(GoalType type) {
        this.type = type;
    }

    //今のところActivity遷移で使う
    public boolean isGoalExist() {
        //存在している場合true
        return name != null && description != null && createDate != null &&
                startDate != null && finishDate != null;
    }

    // タスク管理メソッド 必要ない可能性あり、タスクリストをActivityクラスとかFragmentクラスで作って、それをセッターで入れればいいだけの可能性(上書きするってこと)
//    public void addTask(Task task) {
//        if (this.tasks == null) {
//            this.tasks = new ArrayList<>();
//        }
//        this.tasks.add(task);
//    }

    public void removeTask(Task task) {
        if (this.tasks != null) {
            this.tasks.remove(task);
        }
    }

    public void updateTask(Task task) {
        if (this.tasks != null) {
            for (int i = 0; i < this.tasks.size(); i++) {
                if (this.tasks.get(i).getID() == task.getID()) {
                    this.tasks.set(i, task);
                    break;
                }
            }
        }
    }

    // Parcelable の実装
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(description);

        //Serializableだと互換性がなくバグの原因になるため。また、始めにGoalをインスタンス化する際はすべてnullだからnullの場合の初期値を設定する必要がある
        Long createDateTimestamp = Converters.dateToTimestamp(createDate);
        dest.writeLong(createDateTimestamp != null ? createDateTimestamp : 0L);

        Long startDateTimestamp = Converters.dateToTimestamp(startDate);
        dest.writeLong(startDateTimestamp != null ? startDateTimestamp : 0L);

        Long finishDateTimestamp = Converters.dateToTimestamp(finishDate);
        dest.writeLong(finishDateTimestamp != null ? finishDateTimestamp : 0L);

        dest.writeByte((byte) (state ? 1 : 0));

        // 3/18 14:50  ListをParcelでやる場合、writeList()とwriteTypedList()がある。受け取り側もこれに合わせて書かなくちゃ行けない
        //dest.writeList(tasks); // Task クラスも Parcelable であること
        dest.writeTypedList(tasks); // 修正

        //Intentで情報が送られない問題(ConfirmGoalActivityとConfirmBenchmarkingActivityの間の問題：EnumをIntで送る)
        //.ordinal():列挙型の各定数が定義された順番に対応する序数（インデックス）を返すメソッド
        //dest.writeInt(type == null ? -1 : type.ordinal()); // enum の序数を書き込む (null の場合は -1)
        dest.writeString(type == null ? null : type.name());
    }

    protected Goal(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        description = in.readString();
        createDate = Converters.fromTimestamp(in.readLong());
        startDate = Converters.fromTimestamp(in.readLong());
        finishDate = Converters.fromTimestamp(in.readLong());
        state = in.readByte() != 0;

        //createTypedArrayList():Parcelに書き込まれたParcelableオブジェクトのリストを復元する。ParcelからParcelableオブジェクトのArrayListを読み取るために使用。ジェネリック型TのArrayListを返し、Parcelable.Creator<T>を引数として受け取る
        tasks = in.createTypedArrayList(Task.CREATOR); // Task クラスも Parcelable であること
        //tasks = new ArrayList<>();
        //in.readList(tasks, Task.class.getClassLoader());

        //Intentで情報が送られない問題(ConfirmGoalActivityとConfirmBenchmarkingActivityの間の問題：EnumをIntでもらう)
        //.values():Enumのすべての値を配列として返す。　in.readInt():ParcelからEnumの序数を読み取る。 .values()[]:配列から指定された序数の要素を取得
        //int typeOrdinal = in.readInt();
        //Log.d("GoalType Debugging",valueOf(typeOrdinal));
        //type = (typeOrdinal == -1) ? null : GoalType.values()[typeOrdinal]; // 序数から enum を復元 (null の場合は null)
        String typeName = in.readString();
        type = (typeName == null) ? null : GoalType.valueOf(typeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        @Override
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        @Override
        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

}

