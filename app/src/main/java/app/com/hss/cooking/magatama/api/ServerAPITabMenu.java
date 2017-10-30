package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class ServerAPITabMenu extends ServerAPIAbstract {

	private static Globals mGlobals;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPITabMenu(Context context) {
		super(context);
	}

	/**
	 * ServerAPIMainインスタンス取得
	 *
	 * @param context
	 * @param globals
	 * 
	 * @return ServerAPITabMenu
	 */
	public static ServerAPITabMenu getInstance(Context context, Globals globals) {
		Log.v("MAGATAMA", "ServerAPITabMenu : getInstance");
		
		mGlobals = globals;
		ServerAPITabMenu serverAPI = new ServerAPITabMenu(context);
		return serverAPI;
	}

	/**
	 * SideMenuのAPIデータを更新
	 */
	@Override
	public void update() {
		// SIDEMENUのデータ更新
		super.update();

		Log.v("MAGATAMA", "ServerAPITabMenu update jsonTabmenu : " + this.getCurrentData());
		
		// TabMENUのデータ強制更新
		String jsonTabMenu   = this.getCurrentData();
		if (TextUtils.isEmpty(jsonTabMenu)) return;
		try {
			JSONObject jsonObj = new JSONObject(jsonTabMenu);
			JSONArray jsonArr = jsonObj.getJSONArray("list");
			int len = jsonArr.length();
			
			for (int i=0; i < len; i++) {
				//obj  = jsonArr.getJSONObject(i);

				ServerAPITabMenu tabMenu = ServerAPITabMenu.getInstance(mContext, mGlobals);
				tabMenu.forceUpdate();
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
		return mGlobals.getPrefKeyTabmenu();
	}

	@Override
	String getApiURLData() {
		//return mGlobals.getUrlSidemenu();
		return mGlobals.getUrlTabmenu();
	}

	@Override
	HashMap<String, String> getApiURLParamsData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("client", mGlobals.getClientID());
		return params;
	}

}
