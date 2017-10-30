package app.com.hss.cooking.magatama.api;

import android.content.Context;
import android.util.Log;

import com.duarise.bundlehelper.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.CacheHelper;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.HttpHelper;

/**
 * APIサーバー関連処理を管理する抽象クラス
 *
 * ・abstractメソッドをオーバーライドしてください
 * ・本クラスを継承したクラスをAsyncTask#doInBackground内などで使用することを想定
 * ・APIからJSONデータを取得/保存(最新でないなら更新)することを想定
 * ・取得したデータはSharedPreferenceへ保存することを想定
 * ・SharedPreference内のデータ更新の必要性はAPIから取得するリビジョンナンバーで判断
 * ・リビジョンナンバー: データのバージョンを表す数値(文字列)
 * ・JSONデータはアプリで必要な情報データが入っている(表示用文言/画像URL...)
 */
public abstract class ServerAPIAbstract {

	protected Context          mContext;
	protected CacheHelper mCacheHelper;
	protected PreferenceHelper mPreferenceHelper;
	protected HttpHelper mHttpHelper;

	/**
	 * コンストラクタ
	 *
	 * @param context
	 */
	public ServerAPIAbstract(Context context) {
		mContext = context;
	}

	/**
	 * SharedPreference保存名: データ用
	 *
	 * @return
	 */
	abstract String getPrefName();

	/**
	 * SharedPreference保存キー: JSONデータ
	 * @return
	 */
	abstract String getPrefKeyData();

	/**
	 * APIサーバーURL: JSONデータ取得URL
	 *
	 * @return
	 */
	abstract String getApiURLData();

	/**
	 * APIサーバーURLパラメータ: JSONデータ取得用
	 *
	 * 実装はキーと値のペア(xN)が格納されたHashMapを生成して返す処理を想定
	 * ※不要の場合はnullを返して下さい
	 * @return
	 */
	abstract HashMap<String, String> getApiURLParamsData();

	/**
	 * アプリ初回起動時のデータ登録処理
	 *
	 * [想定している処理内容]
	 * ・APIサーバーから取得したデータをSharedPreferenceへ保存
	 * ・アプリ初回起動時のデータ登録フラグ更新(true)
	 *
	 */
	protected void regist() {
		String jsonData = this.getAPIData();
    	this.saveAPIData(jsonData);
    	// jsonDataのデータが空の場合は保存しない
//		if ( ! jsonData.isEmpty()) {
//			this.saveAPIData(jsonData);
//		}
	}

	/**
	 * SharedPreference内の保存データを最新データで強制更新
	 */
	public void forceUpdate() {
		this.regist();
	}

	/**
	 * SharedPreference内の保存データを最新データで更新
	 *
	 * [想定している処理内容]
	 * 		・ 最新: returnで抜けて処理しない
	 * 		・ 古い: APIサーバーからデータ取得
	 *			L SharedPreference内データ更新
	 */
	public void update() {
		this.regist();
		this.clearCache();
	}

	/**
	 * キャッシュクリア
	 *
	 * ・LruCache内の全キャッシュクリア
	 */
	public void clearCache() {
		this.getCacheHelper().evictAll();
	}

	/**
	 * SharedPreferenceからJSONデータ取得
	 *
	 * @return
	 */
	public String getCurrentData() {
		return (String) this.getPreferenceHelper().get(this.getPrefKeyData(), String.class);
	}

	/**
	 * APIサーバーからJSONデータ取得
	 *
	 * @return APIから取得したJSONデータを想定
	 */
	protected String getAPIData() {
		Log.v("MAGATAMA", "getApiURLData : " + this.getApiURLData() + " : getApiURLParamsData : " +this.getApiURLParamsData());
		return this.getHttpHelper().get(this.getApiURLData(), this.getApiURLParamsData());
	}

	/**
	 * APIサーバーから取得したJSONデータを保存
	 *
	 * @param json APIサーバーから取得したJSONデータ
	 * 保存先：SharedPreference
	 */
	private void saveAPIData(String json) {
		//if (json.isEmpty()) return;
		this.getPreferenceHelper().put(this.getPrefKeyData(), json);
	}

	/**
	 * StringをJSONObjectへ変換
	 *
	 * @param jsonString
	 * @return
	 */
	private JSONObject stringToJSONObject(String jsonString) {
		JSONObject json = null;
		try {
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * StringをJSONArrayへ変換
	 *
	 * @param jsonString
	 * @return
	 */
	protected JSONArray stringToJSONArray(String jsonString) {
		JSONArray json = null;
		try {
			json = new JSONArray(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * CacheHelperインスタンス取得
	 *
	 * @return
	 */
	private CacheHelper getCacheHelper() {
		if (mCacheHelper == null) {
			mCacheHelper = new CacheHelper(mContext);
		}
		return mCacheHelper;
	}

	/**
	 * PreferenceHelperインスタンス取得: データ用
	 * @return
	 */
	private PreferenceHelper getPreferenceHelper() {
		if (mPreferenceHelper == null) {
			mPreferenceHelper = PreferenceHelper.getInstance(mContext, this.getPrefName());
		}
		return mPreferenceHelper;
	}

	/**
	 * HttpHelperインスタンス取得
	 * @return
	 */
	private HttpHelper getHttpHelper() {
		if (mHttpHelper == null) {
			mHttpHelper = new HttpHelper();
		}
		return mHttpHelper;
	}
}
