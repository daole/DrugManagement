package com.dreamdigitizers.drugmanagement.activities;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.dreamdigitizers.drugmanagement.Constants;
import com.dreamdigitizers.drugmanagement.R;
import com.dreamdigitizers.drugmanagement.fragments.NavigationDrawerFragment;
import com.dreamdigitizers.drugmanagement.fragments.NavigationDrawerFragment.INavigationDrawerItemSelectListener;
import com.dreamdigitizers.drugmanagement.fragments.screens.Screen;
import com.dreamdigitizers.drugmanagement.fragments.screens.Screen.IScreenActionsListener;
import com.dreamdigitizers.drugmanagement.fragments.screens.ScreenFamilyMemberList;
import com.dreamdigitizers.drugmanagement.fragments.screens.ScreenMedicineList;
import com.dreamdigitizers.drugmanagement.fragments.screens.ScreenSchedule;

public class MainActivity extends MyActivity implements INavigationDrawerItemSelectListener, IScreenActionsListener {
    private Screen mCurrentScreen;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        this.setContentView(R.layout.activity__main);

        this.mNavigationDrawerFragment = (NavigationDrawerFragment)this.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        this.mNavigationDrawerFragment.setUp(R.array.navigation_drawer_icons, R.array.navigation_drawer_titles, R.id.navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            this.getMenuInflater().inflate(R.menu.menu__main, pMenu);
            this.restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = pItem.getItemId();

        return super.onOptionsItemSelected(pItem);
    }

    @Override
    public void onBackPressed() {
        if(this.mNavigationDrawerFragment.isDrawerOpen()){
            this.mNavigationDrawerFragment.closeDrawer();
        }else {
            if(this.mCurrentScreen != null) {
                boolean isHandled = this.mCurrentScreen.onBackPressed();
                if(isHandled) {
                    return;
                }
            }

            int backStackEntryCount = this.getSupportFragmentManager().getBackStackEntryCount();
            if(backStackEntryCount <= 1) {
                this.finish();
                return;
            }

            this.mNavigationDrawerFragment.setItemChecked(Constants.NAVIGATION_DRAWER_ITEM_ID__SCHEDULE);
            this.changeScreen(new ScreenSchedule());
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int pPosition) {
        // Update the menu__main content by replacing fragments
        Screen screen = null;
        switch (pPosition) {
            case Constants.NAVIGATION_DRAWER_ITEM_ID__SCHEDULE:
                screen = new ScreenSchedule();
                break;
            case Constants.NAVIGATION_DRAWER_ITEM_ID__FAMILY:
                screen = new ScreenFamilyMemberList();
                break;
            case Constants.NAVIGATION_DRAWER_ITEM_ID__MEDICINES:
                screen = new ScreenMedicineList();
                break;
            case Constants.NAVIGATION_DRAWER_ITEM_ID__MEDICINE_CATEGORIES:
                break;
            case Constants.NAVIGATION_DRAWER_ITEM_ID__MEDICINE_TIME:
                break;
            case Constants.NAVIGATION_DRAWER_ITEM_ID__MEDICINE_INTERVALS:
                break;
            default:
                break;
        }

        if(screen != null) {
            this.changeScreen(screen);
        }
    }

    @Override
    public void onSetCurrentScreen(Screen pCurrentScreen) {
        this.mCurrentScreen = pCurrentScreen;
    }

    @Override
    public void onChangeScreen(Screen pScreen) {
        this.changeScreen(pScreen);
    }

    @Override
    public void onBack() {
        this.back();
    }

    public void restoreActionBar() {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
    }
}
