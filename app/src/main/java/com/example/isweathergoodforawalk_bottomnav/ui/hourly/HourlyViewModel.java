package com.example.isweathergoodforawalk_bottomnav.ui.hourly;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HourlyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HourlyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the hourly fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}