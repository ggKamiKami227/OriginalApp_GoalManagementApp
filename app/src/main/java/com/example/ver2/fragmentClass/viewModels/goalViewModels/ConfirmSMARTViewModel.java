/*
    ConfirmSMARTActivityクラスにおいてFragmentとの情報を更新するためのViewModel
 */
package com.example.ver2.fragmentClass.viewModels.goalViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ver2.dataClass.goalManagement.SMART;

public class ConfirmSMARTViewModel extends ViewModel {
    //MutableLiveData:LiveDataのサブクラスで、値を変更することのできる変数
    //final:これは参照先を表すことで、MutableLiveDataが保持するSMARTオブジェクトの内容は変更可能。つまり、finalが保証しているのは、smartLiveDataが常に同じMutableLiveDataインスタンスを参照し続けることだけ。
    private final MutableLiveData<SMART> smartLiveData = new MutableLiveData<>();

    //LiveData型で返すことで、外部から値を変更できないようにしている。
    public LiveData<SMART> getSmartLiveData() {
        return smartLiveData;
    }

    //smartLiveDataの値を更新するメソッドで、これを呼び出し更新することで、監視しているUIにデータ更新が通知される
    public void updateSmart(SMART smart) {
        smartLiveData.setValue(smart);
    }

}
