package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class ServerAPIMenu extends ServerAPIAbstract {

	private static Globals mGlobals;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPIMenu(Context context) {
		super(context);
	}

	/**
	 * ServerAPIMainインスタンス取得
	 *
	 * @param context
	 * @param globals
	 * @return
	 */
	public static ServerAPIMenu getInstance(Context context, Globals globals) {
		Log.v("MAGATAMA", "ServerAPIMenu getInstance");
		
		mGlobals = globals;
		ServerAPIMenu serverAPI = new ServerAPIMenu(context);
		return serverAPI;
	}

	/**
	 * Menu/SubMenuのAPIデータを更新
	 */
	@Override
	public void update() {
		// MENUのデータ更新
		super.update();

		Log.v("MAGATAMA", "ServerAPIMenu : update");
		Log.v("MAGATAMA", "ServerAPIMenu update this.getCurrentData() : " + this.getCurrentData());
		
		// SUBMENUのデータ強制更新
		String jsonMenu = this.getCurrentData();
		if (TextUtils.isEmpty(jsonMenu)) return;
		
		try {
			JSONObject jsonObj = new JSONObject(jsonMenu);
			JSONArray jsonArr = jsonObj.getJSONArray("list");
			int len = jsonArr.length();
			
			JSONObject obj;
			String parentId;

			for (int i=0; i < len; i++) {
				obj  = jsonArr.getJSONObject(i);
				parentId = obj.getString("id");

				ServerAPISubMenu subMenu = ServerAPISubMenu.getInstance(mContext, mGlobals, parentId);
				subMenu.forceUpdate();
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
		return mGlobals.getPrefKeyMenu();
	}

	@Override
	String getApiURLData() {
		return mGlobals.getUrlApiMenu();
	}

	@Override
	HashMap<String, String> getApiURLParamsData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("client", mGlobals.getClientID());
		return params;
	}

}
