package app.com.hss.cooking.magatama.api;

import android.content.Context;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class ServerAPISubMenu extends ServerAPIAbstract {

	private static Globals mGlobals;
	private static String mParentId;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPISubMenu(Context context) {
		super(context);
	}

	/**
	 * ServerAPIMainインスタンス取得
	 *
	 * @param context
	 * @param globals
	 * @return
	 */
	public static ServerAPISubMenu getInstance(Context context, Globals globals, String parentId) {
		mGlobals  = globals;
		mParentId = parentId;
		ServerAPISubMenu serverAPI = new ServerAPISubMenu(context);
		return serverAPI;
	}

	@Override
	String getPrefName() {
		return mGlobals.getPrefKeyApiName();
	}

	@Override
	String getPrefKeyData() {
		return mGlobals.getPrefKeySubMenu() + mParentId;
	}

	@Override
	String getApiURLData() {
		return mGlobals.getUrlApiMenu() + "/" + mParentId;
	}

	@Override
	HashMap<String, String> getApiURLParamsData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("client", mGlobals.getClientID());
		params.put("parent", mParentId);
		params.put("start",  "0");
		params.put("end",    "10");
		return params;
	}

}
