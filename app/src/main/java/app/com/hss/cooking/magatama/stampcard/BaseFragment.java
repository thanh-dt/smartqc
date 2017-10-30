package app.com.hss.cooking.magatama.stampcard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;


public abstract class BaseFragment extends Fragment {
	private View mRootView;
	private Globals mGlobals;
	private AQuery mAquery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		 if (mRootView != null) {
			    ViewGroup parent = (ViewGroup) mRootView.getParent();
			    if (parent != null) {
			        parent.removeView(mRootView);
			    }
			}
		if (mRootView == null) {
			mRootView = inflater.inflate(getLayoutId(), container, false);
		}
		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if (mGlobals == null) {
			mGlobals = new Globals();
			mGlobals.initPreference(getActivity());
		}

		if (mAquery == null) {
			mAquery = new AQuery(getActivity());
		}

		initVariable();

		initControls();

	}

	public Globals getGlobals() {
		if (mGlobals == null) {
			mGlobals = new Globals();
			mGlobals.initPreference(getActivity());
		}
		return mGlobals;
	}

	public AQuery getAquery() {
		if (mAquery == null) {
			mAquery = new AQuery(getActivity());
		}
		return mAquery;
	}

	public View finViewById(int resId) {
		return mRootView.findViewById(resId);
	}

	protected abstract int getLayoutId();

	protected abstract void initVariable();

	protected abstract void initControls();

}
