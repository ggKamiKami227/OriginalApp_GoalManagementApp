/*
    ConfirmBenchmarkingActivityクラスにおいてFragmentとの情報を更新するためのViewModel
 */

package com.example.ver2.fragmentClass.viewModels.goalViewModels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ver2.dataClass.goalManagement.Benchmarking;

public class ConfirmBenchmarkingViewModel extends ViewModel {
    //MutableLiveData:LiveDataのサブクラスで、値を変更することのできる変数
    //final:これは参照先を表すことで、MutableLiveDataが保持するBenchmarkingオブジェクトの内容は変更可能。つまり、finalが保証しているのは、benchmarkingLiveDataが常に同じMutableLiveDataインスタンスを参照し続けることだけ。
    private final MutableLiveData<Benchmarking> benchmarkingLiveData = new MutableLiveData<>();

    //LiveData型で返すことで、外部から値を変更できないようにしている。
    public LiveData<Benchmarking> getBenchmarkingLiveData() {
        return benchmarkingLiveData;
    }

    //benchmarkingLiveDataの値を更新するメソッドで、これを呼び出し更新することで、監視しているUIにデータ変更が通知される
    public void updateBenchmarking(Benchmarking updatedBenchmarking) {
        benchmarkingLiveData.setValue(updatedBenchmarking);
    }
}
