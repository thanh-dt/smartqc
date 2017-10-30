package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.top.MainteItem;

public class ServerAPIMain extends ServerAPIAbstract {

	private static Globals mGlobals;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPIMain(Context context) {
		super(context);
	}

	/**
	 * ServerAPIMainインスタンス取得
	 *
	 * @param context
	 * @param globals
	 * @return
	 */
	public static ServerAPIMain getInstance(Context context, Globals globals) {
		mGlobals = globals;
		ServerAPIMain serverAPI = new ServerAPIMain(context);
		return serverAPI;
	}

//	@Override
//	String getPrefName() {
//		return mGlobals.prefName;
//	}

//	@Override
//	String getPrefKeyRevision() {
//		return mGlobals.prefKeyRevision;
//	}

//	@Override
//	String getPrefKeyData() {
//		return mGlobals.prefKeyMain;
//	}

//	@Override
//	String getJsonKeyRevision() {
//		return mGlobals.prefKeyMain;
//	}

//	@Override
//	String getApiURLData() {
//		return mGlobals.mainURL;
//	}

	@Override
	HashMap<String, String> getApiURLParamsData() {
		HashMap<String, String> params = new HashMap<String, String>();
		//params.put("client", mGlobals.clientID);
		return params;
	}

    @Override
    String getPrefName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    String getPrefKeyData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    String getApiURLData() {
        // TODO Auto-generated method stub
        return null;
    }

//	@Override
//	String getApiURLRevision() {
//		return mGlobals.revisionURL;
//	}

//	@Override
//	HashMap<String, String> getApiURLParamsRevision() {
//		return null;
//	}


	/**
	 * マーキーに設定するテキストの取得
	 *
	 * @return
	 */
//	public ShopItem getMarquee(){
//		ShopItem shopItem = new ShopItem();
//		String jsonString = (String) this.getCurrentData();
//		try {
//			JSONObject jsonObj = new JSONObject(jsonString);
//			JSONObject obj = jsonObj.getJSONObject("profile");
//
//			shopItem.setTopic(obj.getString("topic"));
//
//		} catch (JSONException e) {
//			return null;
//		}
//		return shopItem;
//	}

	/**
	 * 画像URLリスト取得
	 *
	 * @return
	 */
	public List<String> getImageUrlList() {
		List<String> list = new ArrayList<String>();
		String jsonString = (String) this.getCurrentData();

		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			JSONArray jsonArr = jsonObj.getJSONArray("list");

			JSONObject obj;
			for (int i = 0; i < jsonArr.length(); i++) {
				obj = jsonArr.getJSONObject(i);
				String url = mGlobals.getUrlImage() + obj.getString("file_name");
				list.add(url);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

			return list;
	}

	/**
	 * お店情報の取得
	 *
	 * @return
	 */
//	public ShopItem getShopItem() {
//		ShopItem shopItem = new ShopItem();
//		String jsonString = (String) this.getCurrentData();
//		try {
//			JSONObject jsonObj = new JSONObject(jsonString);
//			JSONObject obj = jsonObj.getJSONObject("profile");
//
//			shopItem.setId(obj.getString("client_id"));
//			shopItem.setImage(obj.getString("file_name"));
//			shopItem.setLat(obj.getString("lat"));
//			shopItem.setLng(obj.getString("lng"));
//			shopItem.setShopName(obj.getString("shop_name"));
//			shopItem.setEmail(obj.getString("email"));
//			shopItem.setURL(obj.getString("url"));
//			shopItem.setOnlineShop(obj.getString("online_shop"));
//			shopItem.setTel(obj.getString("tel1") +"-"+ obj.getString("tel2") +"-"+ obj.getString("tel3"));
//			shopItem.setFax(obj.getString("fax1") +"-"+ obj.getString("fax2") +"-"+ obj.getString("fax3"));
//			shopItem.setOpen(obj.getString("open_hours"));
//			shopItem.setHoliday(obj.getString("holiday"));
//			shopItem.setZip(obj.getString("zip_code1") +"-"+ obj.getString("zip_code2"));
//			shopItem.setAddress(obj.getString("pref") + obj.getString("city") + obj.getString("address_opt1") + obj.getString("address_opt2"));
//			//shopItem.setAppoint(obj.getString("appoint"));
//		} catch (JSONException e) {
//			return null;
//		}
//		return shopItem;
//	}

    /**
     * メンテナンス情報の取得
     * 
     * @return
     */
    public MainteItem getMainteItem() {
        MainteItem mainteItem = new MainteItem();
        String jsonString = (String) this.getCurrentData();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            
            mainteItem.setMainte(jsonObj.getString("maintenance"));
//          mainteItem.setMainte("メンテ中");
            
            
            JSONObject obj = jsonObj.getJSONObject("shop_status");
            
            mainteItem.setId(obj.getString("id"));
            mainteItem.setClientId(obj.getString("client_id"));
            mainteItem.setStatus(obj.getString("status"));
//          mainteItem.setStatus("600");
            mainteItem.setExpredat(obj.getString("expired_at"));
            mainteItem.setUpdatedat(obj.getString("updated_at"));
        } catch (JSONException e) {
            Log.v("YOSHITAKA", "MAINTE ITEM : getMainteItem : error : " + e.getMessage());
            return null;
        }
        return mainteItem;
    }
    
}
