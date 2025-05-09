/*
    ConfirmMemoGoalActivityクラスにおいてFragmentとの情報をを更新するためのViewModel
 */

package com.example.ver2.fragmentClass.viewModels.goalViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ver2.dataClass.goalManagement.Memo_Goal;

public class ConfirmMemoGoalViewModel extends ViewModel {
    //MutableLiveData:LiveDataのサブクラスで、値を変更することのできる変数
    //final:これは参照先を表すことで、MutableLiveDataが保持するMemo_Goalオブジェクトの内容は変更可能。つまり、finalが保証しているのは、memoLiveDataが常に同じMutableLiveDataインスタンスを参照し続けることだけ。
    private final MutableLiveData<Memo_Goal> memoLiveData = new MutableLiveData<>();

    //LiveData型で返すことで、外部から値を変更できないようにしている。
    public LiveData<Memo_Goal> getMemoGoalLiveData(){
        return memoLiveData;
    }

    //memoLiveDataの値を更新するメソッドで、これを呼び出し更新することで、監視しているUIにデータ変更が通知される
    public void updateMemoGoal(Memo_Goal updatedMemo){
        memoLiveData.setValue(updatedMemo);
    }
}
