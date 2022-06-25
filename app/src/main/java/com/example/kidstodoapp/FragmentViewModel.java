package com.example.kidstodoapp;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FragmentViewModel extends ViewModel {
    private static MutableLiveData<Boolean> inParentMode = new MutableLiveData<Boolean>();
    private static MutableLiveData<Bundle> returnBundle = new MutableLiveData<Bundle>();

    private static Boolean inParentModeSet = false;
    private static Boolean returnBundleSet = false;

    public static void setInParentMode(Boolean bool) {
        ParentModeUtility.setInParentMode(bool);
        inParentMode.setValue(bool);
        inParentModeSet = true;
    }

    public static LiveData<Boolean> isInParentMode() {return inParentMode;}

    public static void setReturnBundle(Bundle returnBundle) {
        FragmentViewModel.returnBundle.setValue(returnBundle);
        returnBundleSet = true;
    }

    public static LiveData<Bundle> getReturnBundle() {return returnBundle;}

    static Boolean inParentModeSet() {return inParentModeSet;}

    static void setInParentModeObserver(Boolean inParentModeSet) {FragmentViewModel.inParentModeSet = inParentModeSet;}

    static Boolean returnBundleSet() {return returnBundleSet;}

    static void setReturnBundleObserver(Boolean returnBundleSet) {FragmentViewModel.returnBundleSet = returnBundleSet;}
}
