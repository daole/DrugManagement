package com.dreamdigitizers.drugmanagement.presenters.implementations;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;

import com.dreamdigitizers.drugmanagement.R;
import com.dreamdigitizers.drugmanagement.data.DatabaseHelper;
import com.dreamdigitizers.drugmanagement.data.MedicineContentProvider;
import com.dreamdigitizers.drugmanagement.data.dal.tables.TableMedicineCategory;
import com.dreamdigitizers.drugmanagement.presenters.interfaces.IPresenterMedicineCategoryAdd;
import com.dreamdigitizers.drugmanagement.views.IViewMedicineCategoryAdd;

class PresenterMedicineCategoryAdd implements IPresenterMedicineCategoryAdd {
    private IViewMedicineCategoryAdd mViewMedicineCategoryAdd;

    public PresenterMedicineCategoryAdd(IViewMedicineCategoryAdd pViewMedicineCategoryAdd) {
        this.mViewMedicineCategoryAdd = pViewMedicineCategoryAdd;
    }

    @Override
    public void insert(String pMedicineCategoryName, String pMedicineCategoryNote) {
        int result = this.checkInputData(pMedicineCategoryName);
        if(result != 0) {
            this.mViewMedicineCategoryAdd.showError(result);
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableMedicineCategory.COLUMN_NAME__MEDICINE_CATEGORY_NAME, pMedicineCategoryName);
        if(!TextUtils.isEmpty(pMedicineCategoryNote)) {
            contentValues.put(TableMedicineCategory.COLUMN_NAME__MEDICINE_CATEGORY_NOTE, pMedicineCategoryNote);
        }

        Uri uri = this.mViewMedicineCategoryAdd.getViewContext().getContentResolver().insert(
                MedicineContentProvider.CONTENT_URI__MEDICINE_CATEGORY, contentValues);
        long newId = Long.parseLong(uri.getLastPathSegment());
        if(newId == DatabaseHelper.DB_ERROR_CODE__CONSTRAINT) {
            this.mViewMedicineCategoryAdd.showError(R.string.error__duplicated_data);
        } else if(newId == DatabaseHelper.DB_ERROR_CODE__OTHER) {
            this.mViewMedicineCategoryAdd.showError(R.string.error__unknown_error);
        } else {
            this.mViewMedicineCategoryAdd.showMessage(R.string.message__insert_successful);
        }
    }

    private int checkInputData(String pMedicineCategoryName) {
        if(TextUtils.isEmpty(pMedicineCategoryName)) {
            return R.string.error__blank_medicine_category;
        }
        return 0;
    }
}
