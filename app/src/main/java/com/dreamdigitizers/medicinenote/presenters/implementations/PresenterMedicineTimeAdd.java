package com.dreamdigitizers.medicinenote.presenters.implementations;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;

import com.dreamdigitizers.medicinenote.Constants;
import com.dreamdigitizers.medicinenote.R;
import com.dreamdigitizers.medicinenote.data.ContentProviderMedicine;
import com.dreamdigitizers.medicinenote.data.DatabaseHelper;
import com.dreamdigitizers.medicinenote.data.dal.tables.TableMedicineTime;
import com.dreamdigitizers.medicinenote.presenters.abstracts.IPresenterMedicineTimeAdd;
import com.dreamdigitizers.medicinenote.utils.StringUtils;
import com.dreamdigitizers.medicinenote.views.abstracts.IViewMedicineTimeAdd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class PresenterMedicineTimeAdd implements IPresenterMedicineTimeAdd {
    private IViewMedicineTimeAdd mView;

    public PresenterMedicineTimeAdd(IViewMedicineTimeAdd pView) {
        this.mView = pView;
    }

    @Override
    public void insert(String pMedicineTimeName, List<String> pMedicineTimeValues) {
        int result = this.checkInputData(pMedicineTimeName, pMedicineTimeValues);
        if(result != 0) {
            this.mView.showError(result);
            return;
        }

        String medicineTimeValue = TextUtils.join(Constants.DELIMITER__DATA, pMedicineTimeValues);
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableMedicineTime.COLUMN_NAME__MEDICINE_TIME_NAME, pMedicineTimeName);
        contentValues.put(TableMedicineTime.COLUMN_NAME__MEDICINE_TIME_VALUE, medicineTimeValue);

        Uri uri = this.mView.getViewContext().getContentResolver().insert(
                ContentProviderMedicine.CONTENT_URI__MEDICINE_TIME, contentValues);
        long newId = Long.parseLong(uri.getLastPathSegment());
        if(newId == DatabaseHelper.DB_ERROR_CODE__CONSTRAINT) {
            this.mView.showError(R.string.error__duplicated_data);
        } else if(newId == DatabaseHelper.DB_ERROR_CODE__OTHER) {
            this.mView.showError(R.string.error__unknown_error);
        } else {
            this.mView.clearInput();
            this.mView.showMessage(R.string.message__insert_successful);
        }
    }

    private int checkInputData(String pMedicineTimeName, List<String> pMedicineTimeValues) {
        if(TextUtils.isEmpty(pMedicineTimeName)) {
            return R.string.error__blank_medicine_time_name;
        }
        if(pMedicineTimeValues == null || pMedicineTimeValues.size() <= 0) {
            return R.string.error__blank_medicine_time_value;
        }
        DateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT__TIME);
        for(String medicineTimeValue : pMedicineTimeValues) {
            if (TextUtils.isEmpty(medicineTimeValue)) {
                return R.string.error__blank_medicine_time_value;
            }
            if(!StringUtils.isTime(medicineTimeValue, dateFormat)) {
                return R.string.error__invalid_medicine_time_value;
            }
        }
        return 0;
    }
}
