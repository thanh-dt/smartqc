package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class ServerAPITopBlock extends ServerAPIAbstract {

    private static Globals mGlobals;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public ServerAPITopBlock(Context context) {
        super(context);
    }

    /**
     * ServerAPITopBlockインスタンス取得
     *
     * @param context
     * @param globals
     * @return ServerAPITopBlock
     */
    public static ServerAPITopBlock getInstance(Context context, Globals globals) {
        Log.v("MAGATAMA", "ServerAPIColorTheme : getInstance");

        mGlobals = globals;
        ServerAPITopBlock serverAPI = new ServerAPITopBlock(context);
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

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            ServerAPITopBlock api = ServerAPITopBlock.getInstance(mContext, mGlobals);
            api.forceUpdate();

        } catch (Exception e) {

        }

    }


    @Override
    String getPrefName() {
        return mGlobals.getPrefKeyApiName();
    }

    @Override
    String getPrefKeyData() {
        return mGlobals.getPrefKeyTopBlock();
    }

    @Override
    String getApiURLData() {
        return mGlobals.getUrlApiTop();
    }

    @Override
    HashMap<String, String> getApiURLParamsData() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client", mGlobals.getClientID());
        return params;
    }

    public String getMailAddr() {
        String jsonString = this.getCurrentData();
        String ret = "";
        try {
            JSONObject telBlock = new JSONObject(jsonString).getJSONObject("list").getJSONObject("tel");
            ret = telBlock.getString("email");
        } catch (JSONException e) {
            Log.v("MAGATAMA", "getMailAddr Error :" + e.getMessage());
        }

        return ret;
    }

    public String getTel() {
        String jsonString = this.getCurrentData();
        String ret = "";
        try {
            JSONObject telBlock = new JSONObject(jsonString).getJSONObject("list").getJSONObject("tel");
            ret = telBlock.getString("tel");
        } catch (JSONException e) {
            Log.v("MAGATAMA", "getMailAddr Error :" + e.getMessage());
        }

        return ret;
    }

    public String getGpsLat() {
        String jsonString = this.getCurrentData();
        String ret = "";
        try {
            JSONObject telBlock = new JSONObject(jsonString).getJSONObject("list").getJSONObject("gps");
            ret = telBlock.getString("lat");
        } catch (JSONException e) {
            Log.v("MAGATAMA", "getMailAddr Error :" + e.getMessage());
        }

        return ret;
    }

    public String getGpsLng() {
        String jsonString = this.getCurrentData();
        String ret = "";
        try {
            JSONObject telBlock = new JSONObject(jsonString).getJSONObject("list").getJSONObject("gps");
            ret = telBlock.getString("lng");
        } catch (JSONException e) {
            Log.v("MAGATAMA", "getMailAddr Error :" + e.getMessage());
        }

        return ret;
    }

    // HIROPRO Add
    public String getMemberCardStatus() {
        String jsonString = this.getCurrentData();
        String ret = "";
        try {
            JSONObject member_status = new JSONObject(jsonString).getJSONObject("list").getJSONObject("0");
            ret = member_status.getString("member_card_status");
        } catch (JSONException e) {
            Log.v("MAGATAMA", "getMemberCardStatus Error :" + e.getMessage());
        }

        return ret;
    }

}
