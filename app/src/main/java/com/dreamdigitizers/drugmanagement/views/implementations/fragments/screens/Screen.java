package com.dreamdigitizers.drugmanagement.views.implementations.fragments.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dreamdigitizers.drugmanagement.R;
import com.dreamdigitizers.drugmanagement.utils.DialogUtils;
import com.dreamdigitizers.drugmanagement.views.abstracts.IView;

public abstract class Screen extends Fragment implements IView {
	public static final String BUNDLE_KEY__ROW_ID = "row_id";

	private static final String ERROR_MESSAGE__CONTEXT_NOT_IMPLEMENTS_INTERFACE = "Activity must implement IScreenActionsListener.";

	protected IScreenActionsListener mIScreenActionsListener;

	@Override
	public void onAttach(Context pContext) {
		super.onAttach(pContext);
		try {
			this.mIScreenActionsListener = (IScreenActionsListener)pContext;
		} catch (ClassCastException e) {
			throw new ClassCastException(Screen.ERROR_MESSAGE__CONTEXT_NOT_IMPLEMENTS_INTERFACE);
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
		View view = this.loadView(pInflater, pContainer);
		this.retrieveScreenItems(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle pSavedInstanceState) {
		super.onActivityCreated(pSavedInstanceState);
		this.setHasOptionsMenu(true);
		if(pSavedInstanceState != null) {
			this.recoverInstanceState(pSavedInstanceState);
		}
		this.mapInformationToScreenItems();
	}

	@Override
	public void onResume() {
		super.onResume();
		this.mIScreenActionsListener.onSetCurrentScreen(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getView().getWindowToken(), 0);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mIScreenActionsListener = null;
	}

	@Override
	public void onCreateOptionsMenu(Menu pMenu, MenuInflater pInflater) {
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(this.getTitle());
	}

	@Override
	public Context getViewContext() {
		return this.getContext();
	}

	@Override
	public void showMessage(int pStringResourceId) {
		Toast.makeText(this.getActivity(), pStringResourceId, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showConfirmation(int pStringResourceId, DialogUtils.IOnDialogButtonClickListener pListener) {
		String title = this.getString(R.string.title__error_dialog);
		String positiveButtonText = this.getString(R.string.btn__ok);
		String negativeButtonText = this.getString(R.string.btn__no);
		String message = this.getString(pStringResourceId);
		DialogUtils.displayDialog(this.getActivity(), title, message, true, positiveButtonText, negativeButtonText, pListener);
	}

	@Override
	public void showError(int pStringResourceId) {
		String title = this.getString(R.string.title__error_dialog);
		String buttonText = this.getString(R.string.btn__ok);
		String message = this.getString(pStringResourceId);
		DialogUtils.displayErrorDialog(this.getActivity(), title, message, buttonText);
	}
	
	protected void addChildToViewGroup(ViewGroup pParent, View pChild, int pPosition) {
		if(pPosition >= 0) {
			pParent.addView(pChild, pPosition);
		} else {
			pParent.addView(pChild);
		}
	}
	
	protected void replaceViewInViewGroup(ViewGroup pParent, View pChild, int pPosition) {
		if(pPosition < 0) {
			pPosition = 0;
		}
		
		int childCount = pParent.getChildCount();
		if(pPosition >= childCount) {
			pPosition = childCount - 1;
		}
		
		pParent.removeViewAt(pPosition);
		pParent.addView(pChild, pPosition);
	}

	public boolean onBackPressed() {
		return false;
	}

	private ActionBar getActionBar() {
		return ((AppCompatActivity)this.getActivity()).getSupportActionBar();
	}
	
	protected abstract View loadView(LayoutInflater pInflater, ViewGroup pContainer);
	protected abstract void retrieveScreenItems(View pView);
	protected abstract void recoverInstanceState(Bundle pSavedInstanceState);
	protected abstract void mapInformationToScreenItems();
	protected abstract int getTitle();

	public interface IScreenActionsListener {
		void onSetCurrentScreen(Screen pCurrentScreen);
		void onChangeScreen(Screen pScreen);
		void onBack();
	}
}