package com.dreamdigitizers.drugmanagement.views.abstracts;

import com.dreamdigitizers.drugmanagement.data.models.MedicineTime;

public interface IViewMedicineTimeEdit extends IView {
    void bindData(MedicineTime pModel);
}