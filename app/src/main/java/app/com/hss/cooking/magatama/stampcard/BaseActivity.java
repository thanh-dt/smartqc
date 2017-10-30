package app.com.hss.cooking.magatama.stampcard;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.androidquery.AQuery;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;


public abstract class BaseActivity extends FragmentActivity {
	private Globals mGlobals;
	private AQuery mAquery;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(getLayoutId());
		initVariable();
		initControls();
		mGlobals = new Globals();
		mGlobals.initPreference(getApplicationContext());
		mAquery = new AQuery(getApplicationContext());
	}

	public Globals getGlobalConnect() {
		if (mGlobals == null) {
			mGlobals = new Globals();
			mGlobals.initPreference(getApplicationContext());
		}
		return mGlobals;
	}

	public AQuery getQueryconnect() {
		return mAquery != null ? mAquery : new AQuery(getApplicationContext());
	}

	protected abstract int getLayoutId();

	protected abstract void initVariable();

	protected abstract void initControls();

}
