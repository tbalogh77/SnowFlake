package com.tomtom.snowflake;

import android.util.Log;

import java.util.Iterator;

/**
 * Created by t on 2017.06.23..
 */

public class DMatrix {
    int mnLength = 0;
    int mnElementCount = 0;
    int[] mElements = null;

    public DMatrix(int nLength) {
        mnLength = nLength;
        mnElementCount = countElementCount(mnLength);
        mElements = new int[mnElementCount];
     }
    public int getLength() {
        return mnLength;
    }
    protected int countElementCount(int nLength) {
        return nLength * nLength;
    }
    public int getElementIndex(int i, int j) {
        if ( i == j ) return -1;
        if  ( i < j ) return getElementIndex(j,i);
        return mnLength * j + i;
    }
    public int getElement(int i, int j) {
        int nIndex = getElementIndex(i,j);
        if ( -1 == nIndex ) return 0;
        return mElements[nIndex];
    }
    public void setElement(int i, int j, int value) {
        int nIndex = getElementIndex(i,j);
        if ( -1 == nIndex ) return;
        //Log.e("setElement ", " " + i + "; " +  j + " to  " + value);
        mElements[nIndex] = value;


        nIndex = getElementIndex(j, i);
        if ( -1 == nIndex ) return;
        //Log.e("setElement ", " " + i + "; " +  j + " to  " + value);
        mElements[nIndex] = value;

    }
    public boolean haveElement(int nIndex) {
        if (nIndex < mnLength)
            for (int i = 0; i < mnLength; i++) {
                if ( 0 != getElement(i, nIndex)) return true;
            }
        return false;
    }

    public void logElements() {
        for (int i = 0; i < mElements.length; i++) {
            int nElement = mElements[i];
            if (0!=nElement) Log.d("ELEMENT ", "[" + i + "] = " + nElement);
        }
    }
    public void test() {
        int val = 0;
        for (int i = 0; i < mnLength; i++) {
            for (int j = 0; j < mnLength; j++) {
                Log.d("TEST SET", "[" + i + "; " + j + "] = " + val);
                setElement(i,j,val++);
            }
        }
        logElements();
        for (int i = 0; i < mnLength; i++) {
            for (int j = 0; j < mnLength; j++) {
                Log.d("TEST GET", "[" + i + "; " + j + "] = " + getElement(i,j));
            }
        }
    }
}
