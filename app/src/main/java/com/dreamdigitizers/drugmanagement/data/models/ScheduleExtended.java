package com.dreamdigitizers.drugmanagement.data.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ScheduleExtended extends Schedule {
    private FamilyMember mFamilyMember;
    private MedicineTime mMedicineTime;
    private MedicineInterval mMedicineInterval;
    private List<TakenMedicineExtended> mTakenMedicineList;

    public ScheduleExtended() {

    }

    public ScheduleExtended(Schedule pSchedule) {
        this.setFamilyMemberId(pSchedule.getFamilyMemberId());
        this.setMedicineTimeId(pSchedule.getMedicineTimeId());
        this.setMedicineIntervalId(pSchedule.getMedicineIntervalId());
        this.setFallbackFamilyMemberName(pSchedule.getFallbackFamilyMemberName());
        this.setStartDate(pSchedule.getStartDate());
        this.setIsAlarm(pSchedule.getIsAlarm());
        this.setAlarmTimes(pSchedule.getAlarmTimes());
        this.setImagePath(pSchedule.getImagePath());
        this.setScheduleNote(pSchedule.getScheduleNote());
    }

    public FamilyMember getFamilyMember() {
        return this.mFamilyMember;
    }

    public void setFamilyMember(FamilyMember pFamilyMember) {
        this.mFamilyMember = pFamilyMember;
    }

    public MedicineTime getMedicineTime() {
        return this.mMedicineTime;
    }

    public void setMedicineTime(MedicineTime pMedicineTime) {
        this.mMedicineTime = pMedicineTime;
    }

    public MedicineInterval getMedicineInterval() {
        return this.mMedicineInterval;
    }

    public void setMedicineInterval(MedicineInterval pMedicineInterval) {
        this.mMedicineInterval = pMedicineInterval;
    }

    public List<TakenMedicineExtended> getTakenMedicines() {
        return this.mTakenMedicineList;
    }

    public void setTakenMedicines(List<TakenMedicineExtended> pTakenMedicineList) {
        this.mTakenMedicineList = pTakenMedicineList;
    }

    public static List<ScheduleExtended> fetchExtendedData(Cursor pCursor) {
        List<ScheduleExtended> list = new ArrayList<>();
        if (pCursor != null && pCursor.moveToFirst()) {
            do {
                ScheduleExtended model = ScheduleExtended.fetchExtendedDataAtCurrentPosition(pCursor);
                list.add(model);
            } while (pCursor.moveToNext());
        }

        return list;
    }

    public static ScheduleExtended fetchExtendedDataAtCurrentPosition(Cursor pCursor) {
        return ScheduleExtended.fetchExtendedDataAtCurrentPosition(pCursor, 0);
    }

    public static ScheduleExtended fetchExtendedDataAtCurrentPosition(Cursor pCursor, long pRowId) {
        ScheduleExtended model = null;

        if(pCursor != null) {
            Schedule schedule = Schedule.fetchDataAtCurrentPosition(pCursor, pRowId);
            FamilyMember familyMember = FamilyMember.fetchDataAtCurrentPosition(pCursor, schedule.getFamilyMemberId());
            MedicineTime medicineTime = MedicineTime.fetchDataAtCurrentPosition(pCursor, schedule.getMedicineTimeId());
            MedicineInterval medicineInterval = MedicineInterval.fetchDataAtCurrentPosition(pCursor, schedule.getMedicineIntervalId());
            List<TakenMedicineExtended> takenMedicines = TakenMedicineExtended.fetchExtendedData(pCursor);

            model = new ScheduleExtended(schedule);
            model.setFamilyMember(familyMember);
            model.setMedicineTime(medicineTime);
            model.setMedicineInterval(medicineInterval);
            model.setTakenMedicines(takenMedicines);
        }

        return  model;
    }
}