package com.dreamdigitizers.medicinenote.presenters.implementations;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dreamdigitizers.medicinenote.R;
import com.dreamdigitizers.medicinenote.data.ContentProviderMedicine;
import com.dreamdigitizers.medicinenote.data.dal.tables.Table;
import com.dreamdigitizers.medicinenote.data.dal.tables.TableMedicineTime;
import com.dreamdigitizers.medicinenote.presenters.abstracts.IPresenterMedicineTimeList;
import com.dreamdigitizers.medicinenote.utils.DialogUtils;
import com.dreamdigitizers.medicinenote.views.abstracts.IViewMedicineTimeList;

import java.util.ArrayList;
import java.util.List;

class PresenterMedicineTimeList implements IPresenterMedicineTimeList {
    private IViewMedicineTimeList mView;
    private SimpleCursorAdapter mAdapter;
    private List<Integer> mSelectedPositions;
    private List<Long> mSelectedRowIds;

    public PresenterMedicineTimeList(IViewMedicineTimeList pView) {
        this.mView = pView;
        this.mView.getViewLoaderManager().initLoader(0, null, this);
        this.mSelectedPositions = new ArrayList<>();
        this.mSelectedRowIds = new ArrayList<>();
        this.createAdapter();
    }

    @Override
    public void delete() {
        if(this.mSelectedRowIds.isEmpty()) {
            this.mView.showError(R.string.error__no_data_selected);
            return;
        }

        Uri uri = ContentProviderMedicine.CONTENT_URI__MEDICINE_TIME;
        final ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for(long rowId : this.mSelectedRowIds) {
            operations.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(uri, rowId)).build());
        }

        DialogUtils.IOnDialogButtonClickListener listener = new DialogUtils.IOnDialogButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Activity pActivity, String pTitle, String pMessage, boolean pIsTwoButton, String pPositiveButtonText, String pNegativeButtonText) {
                try {
                    PresenterMedicineTimeList.this.mView.getViewContext().getContentResolver().applyBatch(ContentProviderMedicine.AUTHORITY, operations);
                    PresenterMedicineTimeList.this.mSelectedPositions.clear();
                    PresenterMedicineTimeList.this.mSelectedRowIds.clear();
                    PresenterMedicineTimeList.this.mView.showMessage(R.string.message__delete_successful);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    PresenterMedicineTimeList.this.mView.showError(R.string.error__unknown_error);
                } catch (OperationApplicationException e) {
                    e.printStackTrace();
                    PresenterMedicineTimeList.this.mView.showError(R.string.error__unknown_error);
                }
            }

            @Override
            public void onNegativeButtonClick(Activity pActivity, String pTitle, String pMessage, boolean pIsTwoButton, String pPositiveButtonText, String pNegativeButtonText) {

            }
        };
        this.mView.showConfirmation(R.string.confirmation__delete_data, listener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int pId, Bundle pArgs) {
        String[] projection = new String[0];
        projection = TableMedicineTime.getColumns().toArray(projection);
        CursorLoader cursorLoader = new CursorLoader(this.mView.getViewContext(),
                ContentProviderMedicine.CONTENT_URI__MEDICINE_TIME, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> pLoader, Cursor pData) {
        this.mAdapter.swapCursor(pData);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> pLoader) {
        this.mAdapter.swapCursor(null);
    }

    private void createAdapter() {
        String[] from = new String[] {TableMedicineTime.COLUMN_NAME__MEDICINE_TIME_NAME, TableMedicineTime.COLUMN_NAME__MEDICINE_TIME_VALUE, Table.COLUMN_NAME__ID};
        int[] to = new int[] {R.id.lblMedicineTimeName, R.id.lblMedicineTimeValue, R.id.chkSelect};
        this.mAdapter = new SimpleCursorAdapter(this.mView.getViewContext(),
                R.layout.part__medicine_time, null, from, to, 0);
        this.mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View pView, Cursor pCursor, int pColumnIndex) {
                if (pView.getId() == R.id.chkSelect) {
                    CheckBox checkBox = (CheckBox)pView;
                    final int position = pCursor.getPosition();
                    final long rowId = pCursor.getLong(pCursor.getColumnIndex(Table.COLUMN_NAME__ID));
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton pButtonView, boolean pIsChecked) {
                            PresenterMedicineTimeList.this.check(position, rowId, pIsChecked);
                        }
                    });

                    if (PresenterMedicineTimeList.this.mSelectedPositions.contains(position)) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
                    return true;
                }
                return false;
            }
        });
        this.mView.setAdapter(this.mAdapter);
    }

    private void check(Integer pPosition, Long pRowId, boolean pIsChecked) {
        if(pIsChecked) {
            if(!this.mSelectedPositions.contains(pPosition)) {
                this.mSelectedPositions.add(pPosition);
            }
            if(!this.mSelectedRowIds.contains(pRowId)) {
                this.mSelectedRowIds.add(pRowId);
            }
        } else {
            if(this.mSelectedPositions.contains(pPosition)) {
                this.mSelectedPositions.remove(pPosition);
            }
            if(this.mSelectedRowIds.contains(pRowId)) {
                this.mSelectedRowIds.remove(pRowId);
            }
        }
    }
}
