package com.dreamdigitizers.drugmanagement.presenters.implementations;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;

import com.dreamdigitizers.drugmanagement.Constants;
import com.dreamdigitizers.drugmanagement.R;
import com.dreamdigitizers.drugmanagement.data.MedicineContentProvider;
import com.dreamdigitizers.drugmanagement.data.dal.tables.TableMedicine;
import com.dreamdigitizers.drugmanagement.data.models.Medicine;
import com.dreamdigitizers.drugmanagement.presenters.abstracts.IPresenterMedicineSelect;
import com.dreamdigitizers.drugmanagement.views.abstracts.IViewMedicineSelect;

class PresenterMedicineSelect implements IPresenterMedicineSelect {
    private IViewMedicineSelect mView;
    private SimpleCursorAdapter mAdapter;

    public PresenterMedicineSelect(IViewMedicineSelect pView) {
        this.mView = pView;
        this.mView.getViewLoaderManager().initLoader(0, null, this);
        this.createAdapter();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int pId, Bundle pArgs) {
        String[] projection = new String[0];
        projection = TableMedicine.getColumns().toArray(projection);
        CursorLoader cursorLoader = new CursorLoader(this.mView.getViewContext(),
                MedicineContentProvider.CONTENT_URI__MEDICINE, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> pLoader, Cursor pData) {
        String[] projection = new String[0];
        projection = TableMedicine.getColumns().toArray(projection);
        MatrixCursor extras = new MatrixCursor(projection);
        extras.addRow(new Object[] {Constants.ROW_ID__NO_SELECT, "", this.mView.getViewContext().getString(R.string.lbl__select), "", ""});
        Cursor cursor = new MergeCursor(new Cursor[]{extras, pData});
        this.mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> pLoader) {
        this.mAdapter.swapCursor(null);
    }

    @Override
    public boolean checkInputData(Medicine pMedicine, String pDose) {
        if(pMedicine == null) {
            this.mView.showError(R.string.error__blank_medicine);
            return false;
        }

        if(TextUtils.isEmpty(pDose)) {
            this.mView.showError(R.string.error__blank_dose);
            return false;
        }

        return true;
    }

    @Override
    public void close() {
        this.mView.getViewLoaderManager().destroyLoader(0);
    }

    private void createAdapter() {
        String[] from = new String[] {TableMedicine.COLUMN_NAME__MEDICINE_NAME};
        int[] to = new int[] {android.R.id.text1};
        this.mAdapter = new SimpleCursorAdapter(this.mView.getViewContext(),
                android.R.layout.simple_spinner_item, null, from, to, 0);
        this.mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mView.setAdapter(this.mAdapter);
    }
}
