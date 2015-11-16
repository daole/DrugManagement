package com.dreamdigitizers.drugmanagement.presenters.abstracts;

import android.graphics.Bitmap;

import com.dreamdigitizers.drugmanagement.data.models.AlarmExtended;

public interface IPresenterAlarm extends IPresenter {
    Bitmap loadImage(String pFilePath, int pWidth, int pHeight);
    void select(long pRowId);
    boolean setAlarmDone(long pRowId);
}
