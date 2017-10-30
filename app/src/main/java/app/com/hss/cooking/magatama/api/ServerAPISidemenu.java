package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class ServerAPISidemenu extends ServerAPIAbstract {

	private static Globals mGlobals;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPISidemenu(Context context) {
		super(context);
	}

	/**
	 * ServerAPIMainインスタンス取得
	 *
	 * @param context
	 * @param globals
	 * 
	 * @return ServerAPISidemenu
	 */
	public static ServerAPISidemenu getInstance(Context context, Globals globals) {
		Log.v("MAGATAMA", "ServerAPISidemenu : getInstance");
		
		mGlobals = globals;
		ServerAPISidemenu serverAPI = new ServerAPISidemenu(context);
		return serverAPI;
	}

	/**
	 * SideMenuのAPIデータを更新
	 */
	@Override
	public void update() {
		// SIDEMENUのデータ更新
		super.update();

		Log.v("MAGATAMA", "ServerAPISidemenu : update");
//		Log.v("MAGATAMA", "ServerAPISidemenu update jsonSidemenu : " + this.getCurrentData());
		
		// SUBMENUのデータ強制更新
		String jsonSidemenu   = this.getCurrentData();
		if (TextUtils.isEmpty(jsonSidemenu)) return;
		try {
			JSONObject jsonObj = new JSONObject(jsonSidemenu);
			JSONArray jsonArr = jsonObj.getJSONArray("list");
			int len = jsonArr.length();
						
			for (int i=0; i < len; i++) {
				//JSONObject obj  = jsonArr.getJSONObject(i);

				ServerAPISidemenu sideMenu = ServerAPISidemenu.getInstance(mContext, mGlobals);
				sideMenu.forceUpdate();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	@Override
	String getPrefName() {
		return mGlobals.getPrefKeyApiName();
	}

	@Override
	String getPrefKeyData() {
		return mGlobals.getPrefKeySidemenu();
	}

	@Override
	String getApiURLData() {
		return mGlobals.getUrlSidemenu();
	}

	@Override
	HashMap<String, String> getApiURLParamsData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("client", mGlobals.getClientID());
		return params;
	}

}
