package app.com.hss.cooking.magatama.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.api.ServerAPIMain;
import app.com.hss.cooking.magatama.api.ServerAPISidemenu;
import app.com.hss.cooking.magatama.api.ServerAPITabMenu;
import app.com.hss.cooking.magatama.api.ServerAPITopBlock;

public class AsyncTaskSplash extends AsyncTask<Void, Void, Void> {

	private Context mContext;
	private Globals mGlobals;

	/**
	 * コンストラクタ
	 * @param activity (※引数は基本的に実装任せであって必須ではない）
	 */
	public AsyncTaskSplash(Context context, Globals globals) {
		mContext = context;
		mGlobals = globals;
	}

	/**
	 * バックグラウンド処理
	 * ※UI関連処理はonPostExecuteメソッドへ実装
	 */
	@Override
	protected Void doInBackground(Void... params) {
		ServerAPIMain.getInstance(mContext, mGlobals).update();
		ServerAPIColorTheme.getInstance(mContext, mGlobals).update();
		ServerAPITopBlock.getInstance(mContext, mGlobals).update();
		ServerAPISidemenu.getInstance(mContext, mGlobals).update();
		ServerAPITabMenu.getInstance(mContext, mGlobals).update();
		return null;
	}

	/**
	 * UI関連処理はこのメソッドへ実装(※スレッド処理完了後にコールされる)
	 * @param params
	 * @return
	 */
	@Override
	protected void onPostExecute(Void result) {
		// ※メインスレッドから呼ばれるのでUI関連操作が可能

	}
	
}
