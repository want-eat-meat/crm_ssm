package com.example.comparator;

import com.example.pojo.TblDicValue;

import java.util.Comparator;

public class DicValueComparator implements Comparator<TblDicValue> {
    @Override
    public int compare(TblDicValue d1, TblDicValue d2) {
        return Integer.parseInt(d1.getOrderno()) - Integer.parseInt(d2.getOrderno());
    }
}
