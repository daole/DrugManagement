package com.dreamdigitizers.medicinenote.presenters.implementations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;

import com.dreamdigitizers.medicinenote.Constants;
import com.dreamdigitizers.medicinenote.R;
import com.dreamdigitizers.medicinenote.data.ContentProviderMedicine;
import com.dreamdigitizers.medicinenote.data.DatabaseHelper;
import com.dreamdigitizers.medicinenote.data.dal.tables.TableMedicine;
import com.dreamdigitizers.medicinenote.data.dal.tables.TableMedicineCategory;
import com.dreamdigitizers.medicinenote.presenters.abstracts.IPresenterMedicineAdd;
import com.dreamdigitizers.medicinenote.utils.FileUtils;
import com.dreamdigitizers.medicinenote.views.abstracts.IViewMedicineAdd;

class PresenterMedicineAdd implements IPresenterMedicineAdd {
    private IViewMedicineAdd mView;
    private SimpleCursorAdapter mAdapter;

    public PresenterMedicineAdd(IViewMedicineAdd pView) {
        this.mView = pView;
        this.mView.getViewLoaderManager().initLoader(0, null, this);
        this.createAdapter();
    }

    @Override
    public Bitmap loadImage(String pFilePath) {
        return FileUtils.decodeBitmapFromFile(pFilePath);
    }

    @Override
    public Bitmap loadImage(String pFilePath, int pWidth, int pHeight) {
        return FileUtils.decodeSampledBitmapFromFile(pFilePath, pWidth, pHeight);
    }

    @Override
    public void deleteImage(String pFilePath) {
        if(!TextUtils.isEmpty(pFilePath)) {
            FileUtils.deleteFile(pFilePath);
        }
    }

    @Override
    public void insert(String pMedicineName, long pMedicineCategoryId, String pMedicineImagePath, String pMedicineNote) {
        int result = this.checkInputData(pMedicineName);
        if(result != 0) {
            this.mView.showError(result);
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableMedicine.COLUMN_NAME__MEDICINE_NAME, pMedicineName);
        if(pMedicineCategoryId > Constants.ROW_ID__NO_SELECT) {
            contentValues.put(TableMedicine.COLUMN_NAME__MEDICINE_CATEGORY_ID, pMedicineCategoryId);
        }
        if(!TextUtils.isEmpty(pMedicineImagePath)) {
            contentValues.put(TableMedicine.COLUMN_NAME__MEDICINE_IMAGE_PATH, pMedicineImagePath);
        }
        if(!TextUtils.isEmpty(pMedicineNote)) {
            contentValues.put(TableMedicine.COLUMN_NAME__MEDICINE_NOTE, pMedicineNote);
        }

        Uri uri = this.mView.getViewContext().getContentResolver().insert(
                ContentProviderMedicine.CONTENT_URI__MEDICINE, contentValues);
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

    @Override
    public Loader<Cursor> onCreateLoader(int pId, Bundle pArgs) {
        String[] projection = new String[0];
        projection = TableMedicineCategory.getColumns().toArray(projection);
        CursorLoader cursorLoader = new CursorLoader(this.mView.getViewContext(),
                ContentProviderMedicine.CONTENT_URI__MEDICINE_CATEGORY, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> pLoader, Cursor pData) {
        String[] projection = new String[0];
        projection = TableMedicineCategory.getColumns().toArray(projection);
        MatrixCursor extras = new MatrixCursor(projection);
        extras.addRow(new Object[] {Constants.ROW_ID__NO_SELECT, this.mView.getViewContext().getString(R.string.lbl__select), ""});
        Cursor cursor = new MergeCursor(new Cursor[]{extras, pData});
        this.mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> pLoader) {
        this.mAdapter.swapCursor(null);
    }

    private void createAdapter() {
        String[] from = new String[] {TableMedicineCategory.COLUMN_NAME__MEDICINE_CATEGORY_NAME};
        int[] to = new int[] {android.R.id.text1};
        this.mAdapter = new SimpleCursorAdapter(this.mView.getViewContext(),
                android.R.layout.simple_spinner_item, null, from, to, 0);
        this.mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mView.setAdapter(this.mAdapter);
    }

    private int checkInputData(String pMedicineName) {
        if(TextUtils.isEmpty(pMedicineName)) {
            return R.string.error__blank_medicine_name;
        }
        return 0;
    }
}
