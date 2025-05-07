/*
    MandalaChartExpansionActivityとMandalaChartBottomChartFragmentをつなげる、情報を更新するためのViewModel
    2025-04-29一応、前にやったこと、注意点を忘れたから書くけどViewModelを使って、FragmentまたはActivityからActivity、Fragmentの
    メソッドを呼び出すことは良くない、独立性の観点から、という話がありました。
 */

package com.example.ver2.fragmentClass.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ver2.dataClass.purposeManagement.MandalaChart;

public class MandalaChartBottomChartViewModel extends ViewModel {

    private final MutableLiveData<MandalaChart> mandalaChartMutableLiveData = new MutableLiveData<>();
    //IDをここに入れて、Fragmentで読み取れるようにする
    private final MutableLiveData<Integer> selectedChartID = new MutableLiveData<>();

    public LiveData<MandalaChart> getMandalaChartLiveData(){
        return mandalaChartMutableLiveData;
    }

    public void updateMandalaChart(MandalaChart mandalaChart){
        mandalaChartMutableLiveData.setValue(mandalaChart);
    }

    public LiveData<Integer> getSelectedChartID(){
        return selectedChartID;
    }

    public void selectChartID(int id){
        selectedChartID.setValue(id);
    }
}
