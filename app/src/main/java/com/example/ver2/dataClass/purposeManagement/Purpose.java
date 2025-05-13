/*
    目的クラスの基本となるクラス。これをスーパークラスとして、フレームワークごとにサブクラスを生成する
 */
package com.example.ver2.dataClass.purposeManagement;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ver2.dataClass.Task;
import com.example.ver2.Converters;

import java.util.Date;  //Date型はroomに非対応のため、コンバーターが必要
import java.util.List;

@Entity(tableName = "purposes")
@TypeConverters(Converters.class)
public class Purpose implements Parcelable {
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
    //private List<Task> tasks; //サブクラスのほうでタスクを設定するからここでやると分からなくなるかもだから、いったん消す
    @TypeConverters(Converters.class)
    private PurposeType type;    //MandalaChart or Memo

    public Purpose(String name, String description, Date createDate, Date startDate, Date finishDate, boolean state, PurposeType type) {
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.state = state;
        //this.tasks = tasks;
        this.type = type;
    }

    //Purposeを直接入れられた時のコンストラクタ
    @Ignore
    public Purpose(Purpose purpose){
        this.name = purpose.getName();
        this.description = purpose.getDescription();
        this.createDate = purpose.getCreateDate();
        this.startDate = purpose.getStartDate();
        this.finishDate = purpose.getFinishDate();
        this.state = purpose.getState();
        //this.tasks = tasks;
        this.type = purpose.getType();
    }

    //全部nullで初期化する際のコンストラクタを明示する
    @Ignore
    public Purpose(){
        this.name = null;
        this.description = null;
        this.createDate = null;
        this.startDate = null;
        this.finishDate = null;
        this.state = false;
        //もしかしたらここでTypeのエラーが出るかも
        this.type = null;
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", state=" + state +
                //", tasks=" + tasks +
                ", type='" + type + '\'' +
                '}';
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public boolean getState() {
        return state;
    }

    public PurposeType getType(){return type;}

    //public List<Task> getTasks() {
    //    return tasks;
    //}

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    //public void setTasks(List<Task> tasks) {
    //    this.tasks = tasks;
    //}

    public void setType(PurposeType type) {
        this.type = type;
    }

    public boolean isPurposeExist() {
        //存在している場合はtrue
        return name != null && description != null && createDate != null &&
                startDate != null && finishDate != null;
    }

    //Purposeオブジェクトの中身だけ変更したい場合に使用
    public void updatePurpose(Purpose purpose){
        this.name = purpose.getName();
        this.description = purpose.getDescription();
        this.createDate = purpose.getCreateDate();
        this.startDate = purpose.getStartDate();
        this.finishDate = purpose.getFinishDate();
        this.state = purpose.getState();
        this.type = purpose.getType();
    }

    //Parcelableの実装
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(ID);
        dest.writeString(name);
        dest.writeString(description);

        //日にち：始めにインスタンス化する際はすべてnullだからnullの場合の初期値を設定する必要がある
        Long createDateTimestamp = Converters.dateToTimestamp(createDate);
        dest.writeLong(createDateTimestamp != null ? createDateTimestamp : 0L);

        Long startDateTimestamp = Converters.dateToTimestamp(startDate);
        dest.writeLong(startDateTimestamp != null ? startDateTimestamp : 0L);

        Long finishDateTimestamp = Converters.dateToTimestamp(finishDate);
        dest.writeLong(finishDateTimestamp != null ? finishDateTimestamp : 0L);

        dest.writeByte((byte) (state ? 1 : 0));

        dest.writeString(type == null ? null : type.name());
    }

    protected Purpose(Parcel in){
    ID = in.readInt();
    name = in.readString();
    description = in.readString();
        createDate = Converters.fromTimestamp(in.readLong());
        startDate = Converters.fromTimestamp(in.readLong());
        finishDate = Converters.fromTimestamp(in.readLong());
        state = in.readByte() != 0;
        String typeName = in.readString();
        type = (typeName == null) ? null : PurposeType.valueOf(typeName);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Creator<Purpose> CREATOR = new Creator<Purpose>(){
        @Override
        public Purpose createFromParcel(Parcel in){
            return new Purpose(in);
        }

        @Override
        public Purpose[] newArray(int size){
            return new Purpose[size];
        }
    };
}
