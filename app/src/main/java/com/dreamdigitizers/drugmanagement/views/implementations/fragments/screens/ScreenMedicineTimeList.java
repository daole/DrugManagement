package com.dreamdigitizers.drugmanagement.views.implementations.fragments.screens;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamdigitizers.drugmanagement.R;
import com.dreamdigitizers.drugmanagement.presenters.abstracts.IPresenterMedicineTimeList;
import com.dreamdigitizers.drugmanagement.presenters.implementations.PresenterFactory;
import com.dreamdigitizers.drugmanagement.views.abstracts.IViewMedicineTimeList;

public class ScreenMedicineTimeList extends Screen implements IViewMedicineTimeList {
    private ListView mListView;
    private TextView mLblEmpty;

    private IPresenterMedicineTimeList mPresenterMedicineTimeList;

    @Override
    public void onCreateOptionsMenu(Menu pMenu, MenuInflater pInflater) {
        super.onCreateOptionsMenu(pMenu, pInflater);
        pInflater.inflate(R.menu.menu__add_delete, pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pItem) {
        switch(pItem.getItemId()) {
            case R.id.menu_item__add:
                this.optionAddSelected();
                return true;
            case R.id.menu_item__delete:
                this.optionDeleteSelected();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(pItem);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        View rootView = pInflater.inflate(R.layout.screen__medicine_time_list, pContainer, false);
        return rootView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {
        this.mListView = (ListView)pView.findViewById(R.id.lstMedicineTimes);
        this.mLblEmpty = (TextView)pView.findViewById(R.id.lblEmpty);
    }

    @Override
    protected void recoverInstanceState(Bundle pSavedInstanceState) {

    }

    @Override
    protected void mapInformationToScreenItems() {
        this.mListView.setEmptyView(this.mLblEmpty);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pParent, View pView, int pPosition, long pRowId) {
                ScreenMedicineTimeList.this.listItemClick(pRowId);
            }
        });

        this.mPresenterMedicineTimeList = (IPresenterMedicineTimeList)PresenterFactory.createPresenter(IPresenterMedicineTimeList.class, this);
    }

    @Override
    protected int getTitle() {
        return R.string.title__screen_medicine_time_list;
    }

    @Override
    public LoaderManager getViewLoaderManager() {
        return this.getLoaderManager();
    }

    @Override
    public void setAdapter(ListAdapter pListAdapter) {
        this.mListView.setAdapter(pListAdapter);
    }

    private void optionAddSelected() {
        this.goToAddScreen();
    }

    private void optionDeleteSelected() {
        this.mPresenterMedicineTimeList.delete();
    }

    private void listItemClick(long pRowId) {
        this.goToEditScreen(pRowId);
    }

    private void goToAddScreen() {
        Screen screen = new ScreenMedicineTimeAdd();
        this.mIScreenActionsListener.onChangeScreen(screen);
    }

    private void goToEditScreen(long pRowId) {

    }
}
