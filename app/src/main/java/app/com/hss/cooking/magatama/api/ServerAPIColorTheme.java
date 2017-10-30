package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class ServerAPIColorTheme extends ServerAPIAbstract {

	private static Globals mGlobals;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPIColorTheme(Context context) {
		super(context);
	}

	/**
	 * ServerAPIColorThemeインスタンス取得
	 *
	 * @param context
	 * @param globals
	 * 
	 * @return ServerAPIColorTheme
	 */
	public static ServerAPIColorTheme getInstance(Context context, Globals globals) {
		Log.v("MAGATAMA", "ServerAPIColorTheme : getInstance");
		
		mGlobals = globals;
		ServerAPIColorTheme serverAPI = new ServerAPIColorTheme(context);
		return serverAPI;
	}

	/**
	 * APIデータを更新
	 */
	@Override
	public void update() {
		
		super.update();
		Log.v("MAGATAMA", "ServerAPIColorTheme : update");
		
		// データ強制更新
		String jsonStr = this.getCurrentData();
		if (TextUtils.isEmpty(jsonStr)) return;
//		try {
//			JSONObject jsonObj = new JSONObject(json);
//			JSONArray jsonArr = jsonObj.getJSONArray("list");
//			int len = jsonArr.length();			
//			JSONObject obj;
//			for (int i=0; i < len; i++) {
//				obj  = jsonArr.getJSONObject(i);
//
//				ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(mContext, mGlobals);
//				api.forceUpdate();
//			}
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		try{
			JSONObject jsonObj = new JSONObject(jsonStr);
			ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(mContext, mGlobals);
			api.forceUpdate();

//			// header
//			JSONObject header = jsonObj.getJSONObject("header");
//			if (header != null) {
//				String type = header.getString("type");
//				String color = "#" + header.getString("color");
//				//String subColor = "#" + header.getString("sub_color");
//				//String gradColorStart = "#" + header.getString("gradation1");
//				//String gradColorEnd = "#" + header.getString("gradation2");
//				String barGradColorStart = "#" + header.getString("bar_gradation1");
//				String barGradColorEnd = "#" + header.getString("bar_gradation2");
//				//String subGradColor = "#" + header.getString("sub_gradation");
//				String fileName = header.getString("file_name");				
//			}

		} catch(Exception e) {
			
		}
		
		

		
	}


	@Override
	String getPrefName() {
		return mGlobals.getPrefKeyApiName();
	}

	@Override
	String getPrefKeyData() {
		return mGlobals.getPrefKeyColorTheme();
	}

	@Override
	String getApiURLData() {
		return mGlobals.getUrlApiColorTheme();
	}

	@Override
	HashMap<String, String> getApiURLParamsData() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("client", mGlobals.getClientID());
		return params;
	}
	
}
