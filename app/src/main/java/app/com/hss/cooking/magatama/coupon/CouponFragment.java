package app.com.hss.cooking.magatama.coupon;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.MySQLiteOpenHelper;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class CouponFragment extends Fragment {
	private final static int PAGE_COUNT = 10;
	private Globals gl;
	private AQuery aq;

	private static CouponAdapter mCouponAdapter;
	private int mStart = 0;
	private boolean mIsLoading = false;
	private View mViewFooter;
	//private PullToRefreshListView mListView;
	private ListView mListView;
	
	private ArrayList<CouponItem> mItems;
	
	private View view;
	LayoutInflater inflater;
	
	View listView;
	
	static SQLiteDatabase mydb;
	MySQLiteOpenHelper hlpr;
	
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static CouponFragment newInstance() {
		CouponFragment fragment = new CouponFragment();
		return fragment;
	}

	public CouponFragment() {
	}


	@Override
	public void onAttach(Activity act){
		super.onAttach(act);

		mCouponAdapter = new CouponAdapter(act, new ArrayList<CouponItem>());
		Log.v("MAGATAMA", "CouponAdapter : " + mCouponAdapter);
		
		hlpr = new MySQLiteOpenHelper(getActivity().getApplicationContext());
		mydb = hlpr.getWritableDatabase();
		
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coupon, container, false);
		this.view = view;
		this.inflater = inflater;
		
		Log.v("MAGATAMA", "CouponFragment : onCreateView");

		gl = new Globals();
		aq = new AQuery(view);

		mListView = (ListView) view.findViewById(R.id.listView1);
		mListView.setAdapter(mCouponAdapter);
		
		couponListAsyncJson();
		setupColorTheme();

		android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
		
		return view;
	}
		
	/**
	 *
	 */
	public void couponListAsyncJson() {
		mIsLoading = true;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client", gl.getClientID());
		params.put("start", mStart);
		params.put("end", PAGE_COUNT);
		
		aq.ajax(gl.getUrlApiCoupon(), params, JSONArray.class, this, "setListView");
	}

	/**
	 * @param url
	 * @param json
	 * @param status
	 */
	public void setListView(String url, JSONArray json, AjaxStatus status) {
		Log.v("MAGATAMA", "CouponFragment JSON : " + json);
		
		if (json != null) {
			mItems = new ArrayList<CouponItem>();
			ArrayList<CouponItem> usedItems = new ArrayList<CouponItem>();
			
			try {
				int max = json.length();
				JSONObject obj;
				for (int i = 0; i < max; i++) {
					obj = json.getJSONObject(i);

					CouponItem currentItem = new CouponItem();
					currentItem.setId(obj.getString("id"));
					currentItem.setTitle(obj.getString("title"));
					currentItem.setDescription(obj.getString("policy"));
					currentItem.setDiscount(obj.getString("discount"));
					currentItem.setUseCount(obj.getString("use_count"));
					currentItem.setUseFlg("0");
					currentItem.setDisplayDays(obj.getString("display_days"));
					currentItem.setEndDatetime(obj.getString("end_datetime"));
					currentItem.setEnableFlg(obj.getString("enable_flg"));
					currentItem.setType(obj.getString("coupon_type"));
					String image = obj.getString("file_name");
					
					if (image != null && image.length() > 0) {
						currentItem.setFileName(gl.getUrlImgCouponItem() + image);
					}

					// ここでcouponIdとDBの使用済みidをチェック
					boolean isUsed = checkUsed(Integer.parseInt(currentItem.getId().toString()));
					if(isUsed) {
						currentItem.setUseFlg("1");
						usedItems.add(currentItem);
					} else {
						mCouponAdapter.add(currentItem);
						mItems.add(currentItem);
						mStart += 1;
					}
					
					
				}
				if (max == 0 || max < PAGE_COUNT) {
					mStart = -1;
					getFooter().setVisibility(View.GONE);
				}
				
				// 使用済みを最後尾に追加
				for(int i=0; i<usedItems.size(); i++ ){
					//mItems.add(usedItems.get(i));
					mCouponAdapter.add(usedItems.get(i));
				}
				
				CouponItem blankItem = new CouponItem();
				blankItem.setBlankFlg("1");
				mCouponAdapter.add(blankItem);
					
			} catch (JSONException e) {
				e.printStackTrace();
				Log.v("MAGATAMA", "メニューの読み込みでエラーが出たよ！");
			}
		} else {
			aq.id(R.id.coupon_blank_text).visible();
			aq.id(R.id.coupon_back_icon).visible();
		}
		
		mIsLoading = false;
		getFooter().setVisibility(View.GONE);
		mCouponAdapter.notifyDataSetChanged();
	}
		
	public void setupColorTheme() {
		ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(getActivity(), new Globals());
		String jsonStr = api.getCurrentData();
		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONObject bg = jsonObj.getJSONObject("background");
			if (bg != null) {
				String color = "#" + bg.getString("color");
//				String subColor = "#" + bg.getString("sub_color");
				String type = bg.getString("type");
				String gradColorStart = "#" + bg.getString("gradation1");
				String gradColorEnd = "#" + bg.getString("gradation2");
//				String barGradColorStart = "#" + bg.getString("bar_gradation1");
//				String barGradColorEnd = "#" + bg.getString("bar_gradation2");
//				String subGradColor = "#" + bg.getString("sub_gradation");

				// 背景設定
				if(type.equals("1")) {
					// 単一カラー
					aq.id(R.id.coupon_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.coupon_bg).image(bgDraw);
				}
				
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
				}
				
				// 背景画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.coupon_bg).progress(R.id.blank_image).image(image);
				}
				
			}
		} catch(Exception e) {
			Log.v("MAGATAMA", "MenuDetailFragment: setupColorTheme error");
		}
		
	}

	
	private boolean checkUsed(int id) {
		Cursor cursor = null;
		try{
			String where = "coupon_id = ?";
			String param = String.valueOf(id);
			cursor = mydb.query("use_coupon", new String[] {"_id", "coupon_id"}, where, new String[]{param}, null, null, "_id DESC");
		}catch(Exception e){
			return false;			
		}
			
		if (cursor.getCount() != 0) {
			return true;
		}
		
		return false;
	}
	
	private View getFooter() {
		if (mViewFooter == null) {
			//			mViewFooter = getLayoutInflater().inflate(R.layout.view_footer, null);
			mViewFooter = aq.id(R.id.footer).getView();
		}
		return mViewFooter;
	}
}
