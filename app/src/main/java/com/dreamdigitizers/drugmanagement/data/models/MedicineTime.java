package com.dreamdigitizers.drugmanagement.data.models;

import android.database.Cursor;

import com.dreamdigitizers.drugmanagement.Constants;
import com.dreamdigitizers.drugmanagement.data.dal.tables.Table;
import com.dreamdigitizers.drugmanagement.data.dal.tables.TableMedicineTime;

import java.util.ArrayList;
import java.util.List;

public class MedicineTime extends Model {
    private String mMedicineTimeName;
    private String[] mMedicineTimeValues;

    public String getMedicineTimeName() {
        return this.mMedicineTimeName;
    }

    public void setMedicineTimeName(String pMedicineTimeName) {
        this.mMedicineTimeName = pMedicineTimeName;
    }

    public String[] getMedicineTimeValues() {
        return this.mMedicineTimeValues;
    }

    public void setMedicineTimeValues(String[] pMedicineTimeValue) {
        this.mMedicineTimeValues = pMedicineTimeValue;
    }

    public static List<MedicineTime> fetchData(Cursor pCursor) {
        List<MedicineTime> list = new ArrayList<>();
        if (pCursor != null && pCursor.moveToFirst()) {
            do {
                long rowId = pCursor.getLong(Table.COLUMN_INDEX__ID);
                String medicineTimeName = pCursor.getString(TableMedicineTime.COLUMN_INDEX__MEDICINE_TIME_NAME);
                String medicineTimeValue = pCursor.getString(TableMedicineTime.COLUMN_INDEX__MEDICINE_TIME_VALUE);
                String[] medicineTimeValues = medicineTimeValue.split(Constants.DELIMITER__DATA);

                MedicineTime model = new MedicineTime();
                model.setId(rowId);
                model.setMedicineTimeName(medicineTimeName);
                model.setMedicineTimeValues(medicineTimeValues);

                list.add(model);
            } while (pCursor.moveToNext());
        }

        return list;
    }
}
