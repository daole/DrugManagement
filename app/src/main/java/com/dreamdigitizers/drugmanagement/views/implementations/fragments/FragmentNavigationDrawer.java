package com.dreamdigitizers.drugmanagement.views.implementations.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamdigitizers.drugmanagement.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class FragmentNavigationDrawer extends FragmentBase {
    private static final String ERROR_MESSAGE__CONTEXT_NOT_IMPLEMENTS_INTERFACE = "Activity must implement INavigationDrawerItemSelectListener.";

    /**
     * Remember the position of the selected item.
     */
    private static final String BUNDLE_KEY__STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private INavigationDrawerItemSelectListener mINavigationDrawerItemSelectListener;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition;

    public FragmentNavigationDrawer() {
        this.mCurrentSelectedPosition = 0;
    }

    @Override
    public void onAttach(Context pContext) {
        super.onAttach(pContext);

        try {
            this.mINavigationDrawerItemSelectListener = (INavigationDrawerItemSelectListener)pContext;
        } catch (ClassCastException e) {
            throw new ClassCastException(FragmentNavigationDrawer.ERROR_MESSAGE__CONTEXT_NOT_IMPLEMENTS_INTERFACE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.mINavigationDrawerItemSelectListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle pOutState) {
        super.onSaveInstanceState(pOutState);

        pOutState.putInt(FragmentNavigationDrawer.BUNDLE_KEY__STATE_SELECTED_POSITION, this.mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration pNewConfig) {
        super.onConfigurationChanged(pNewConfig);

        // Forward the new configuration the drawer toggle component.
        this.mDrawerToggle.onConfigurationChanged(pNewConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pItem) {
        if (this.mDrawerToggle.onOptionsItemSelected(pItem)) {
            return true;
        }

        return super.onOptionsItemSelected(pItem);
    }

    @Override
    protected View loadView(LayoutInflater pInflater, ViewGroup pContainer) {
        this.mDrawerListView = (ListView)pInflater.inflate(R.layout.fragment__navigation_drawer, pContainer, false);
        return this.mDrawerListView;
    }

    @Override
    protected void retrieveScreenItems(View pView) {

    }

    @Override
    protected void recoverInstanceState(Bundle pSavedInstanceState) {
        if (pSavedInstanceState != null) {
            this.mCurrentSelectedPosition = pSavedInstanceState.getInt(FragmentNavigationDrawer.BUNDLE_KEY__STATE_SELECTED_POSITION);
        }
    }

    @Override
    protected void mapInformationToScreenItems(View pView) {
        this.mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView pParent, View pView, int pPosition, long pId) {
                FragmentNavigationDrawer.this.selectItem(pPosition);
            }
        });
    }

    @Override
    protected int getTitle() {
        return R.string.title__menu;
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param pFragmentContainerViewId   The android:id of this fragment in its activity's layout.
     * @param pDrawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int pIconsResourceId, int pTitlesResourceId, int pFragmentContainerViewId, DrawerLayout pDrawerLayout) {
        // set up the drawer's list view with items and click listener
        this.mDrawerListView.setAdapter(new NavigationDrawerListAdapter(this.getContext(), pIconsResourceId, pTitlesResourceId));
        this.setItemChecked(this.mCurrentSelectedPosition);

        // Select either the default item (0) or the last selected item.
        this.selectItem(this.mCurrentSelectedPosition);

        this.mFragmentContainerView = this.getActivity().findViewById(pFragmentContainerViewId);
        this.mDrawerLayout = pDrawerLayout;

        // set a custom shadow that overlays the menu__main content when the drawer opens
        this.mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        this.mDrawerToggle = new ActionBarDrawerToggle(
                this.getActivity(),  /* host Activity */
                this.mDrawerLayout, /* DrawerLayout object */
                R.string.desc__navigation_drawer_open,    /* "open drawer" description for accessibility */
                R.string.desc__navigation_drawer_close    /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerOpened(View pDrawerView) {
                super.onDrawerOpened(pDrawerView);
                if (!FragmentNavigationDrawer.this.isAdded()) {
                    return;
                }

                FragmentNavigationDrawer.this.getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerClosed(View pDrawerView) {
                super.onDrawerClosed(pDrawerView);
                if (!FragmentNavigationDrawer.this.isAdded()) {
                    return;
                }

                FragmentNavigationDrawer.this.getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        this.mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                FragmentNavigationDrawer.this.mDrawerToggle.syncState();
            }
        });

        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
    }

    public void setItemChecked(int pPosition) {
        this.mDrawerListView.setItemChecked(pPosition, true);
    }

    public boolean isDrawerOpen() {
        return this.mDrawerLayout != null && this.mDrawerLayout.isDrawerOpen(this.mFragmentContainerView);
    }

    public void closeDrawer() {
        this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
    }

    private void selectItem(int pPosition) {
        this.mCurrentSelectedPosition = pPosition;
        if (this.mDrawerListView != null) {
            this.setItemChecked(pPosition);
        }
        if (this.mDrawerLayout != null) {
            this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
        }
        if (this.mINavigationDrawerItemSelectListener != null) {
            this.mINavigationDrawerItemSelectListener.onNavigationDrawerItemSelected(pPosition);
        }
    }

    public static class NavigationDrawerListAdapter extends BaseAdapter {
        private Context mContext;
        private TypedArray mMenuItemIcons;
        private String[] mMenuItemTitles;

        public NavigationDrawerListAdapter(Context pContext, int pIconsKeys, int pTitlesKey) {
            this.mContext = pContext;
            this.mMenuItemIcons = this.mContext.getResources().obtainTypedArray(pIconsKeys);
            this.mMenuItemTitles = this.mContext.getResources().getStringArray(pTitlesKey);
        }

        @Override
        public int getCount() {
            return this.mMenuItemTitles.length;
        }

        @Override
        public Object getItem(int pPosition) {
            return null;
        }

        @Override
        public long getItemId(int pPosition) {
            return pPosition;
        }

        @Override
        public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
            ViewHolder viewHolder;
            if(pConvertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                pConvertView = layoutInflater.inflate(R.layout.part__navigation_drawer_item, pParent, false);

                viewHolder = new ViewHolder();
                viewHolder.mMenuItemIcon = (ImageView)pConvertView.findViewById(R.id.menu_item_icon);
                viewHolder.mMenuItemText = (TextView)pConvertView.findViewById(R.id.menu_item_text);
                viewHolder.mMenuItemCounter = (TextView)pConvertView.findViewById(R.id.menu_item_counter);
                pConvertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder)pConvertView.getTag();
            viewHolder.mMenuItemIcon.setImageResource(this.mMenuItemIcons.getResourceId(pPosition, -1));
            viewHolder.mMenuItemText.setText(this.mMenuItemTitles[pPosition]);

            return pConvertView;
        }

        private static class ViewHolder {
            public ImageView mMenuItemIcon;
            public TextView mMenuItemText;
            public TextView mMenuItemCounter;
        }
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface INavigationDrawerItemSelectListener {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int pPosition);
    }
}