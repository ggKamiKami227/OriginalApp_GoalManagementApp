/*
    Purposeクラスをスーパークラスとするサブクラスの種類を判別するのに使用する（フレームワークを判別する）
 */

package com.example.ver2.dataClass.purposeManagement;

public enum PurposeType {
    MANDALA_CHART("MandalaChart"),
    MEMO_PURPOSE("MemoPurpose");

    private final String value;

    PurposeType(String value){this.value = value;}

    @Override
    public String toString(){
        return value;
    }
}
