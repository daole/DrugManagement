package com.dreamdigitizers.drugmanagement.presenters.implementations;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;

import com.dreamdigitizers.drugmanagement.R;
import com.dreamdigitizers.drugmanagement.data.DatabaseHelper;
import com.dreamdigitizers.drugmanagement.data.MedicineContentProvider;
import com.dreamdigitizers.drugmanagement.data.dal.tables.TableFamilyMember;
import com.dreamdigitizers.drugmanagement.presenters.interfaces.IPresenterFamilyMemberAdd;
import com.dreamdigitizers.drugmanagement.views.IViewFamilyMemberAdd;

class PresenterFamilyMemberAdd implements IPresenterFamilyMemberAdd {
    private IViewFamilyMemberAdd mViewFamilyMemberAdd;

    public PresenterFamilyMemberAdd(IViewFamilyMemberAdd pViewFamilyMemberAdd) {
        this.mViewFamilyMemberAdd = pViewFamilyMemberAdd;
    }

    @Override
    public void insert(String pFamilyMember) {
        int result = this.checkInputData(pFamilyMember);
        if(result != 0) {
            this.mViewFamilyMemberAdd.showError(result);
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableFamilyMember.COLUMN_NAME__FAMILY_MEMBER_NAME, pFamilyMember);
        Uri uri = this.mViewFamilyMemberAdd.getViewContext().getContentResolver().insert(
                MedicineContentProvider.CONTENT_URI__FAMILY_MEMBER, contentValues);

        long newId = Long.parseLong(uri.getLastPathSegment());
        if(newId == DatabaseHelper.DB_ERROR_CODE__CONSTRAINT) {
            this.mViewFamilyMemberAdd.showError(R.string.error__duplicated_data);
        } else if(newId == DatabaseHelper.DB_ERROR_CODE__OTHER) {
            this.mViewFamilyMemberAdd.showError(R.string.error__unknown_error);
        } else {
            this.mViewFamilyMemberAdd.showMessage(R.string.message__insert_successful);
        }
    }

    private int checkInputData(String pFamilyMember) {
        if(TextUtils.isEmpty(pFamilyMember)) {
            return R.string.error__blank_family_member;
        }
        return 0;
    }
}