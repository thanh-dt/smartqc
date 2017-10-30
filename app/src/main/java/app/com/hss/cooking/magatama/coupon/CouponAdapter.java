package app.com.hss.cooking.magatama.coupon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.MySQLiteOpenHelper;

public class CouponAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CouponItem> mCouponItems;
	private Map<Integer, View> positionView = new HashMap<Integer, View>();
	private View mainView;
	static SQLiteDatabase mydb;
	MySQLiteOpenHelper hlpr;
	Activity activity;
	
	public void add(CouponItem newsItem) {
		mCouponItems.add(newsItem);
	}

	@Override
	public int getCount() {
		return mCouponItems.size();
	}

	@Override
	public CouponItem getItem(int position) {
		// TODO Auto-generated method stub
		return mCouponItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public CouponAdapter(Context context, List<CouponItem> objects) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCouponItems = objects;
		activity = (Activity) context;
		hlpr = new MySQLiteOpenHelper(context);
		mydb = hlpr.getWritableDatabase();
	}
	
	// 一行ごとのビューを作成する。
	public View getView(final int position, View convertView, ViewGroup parent) {
		
//		View rootView = convertView;
//		if (convertView == null) {
//			rootView = mInflater.inflate(R.layout.coupon_list_row, null);
//		}
		
		View rootView = mInflater.inflate(R.layout.coupon_list_row, null);
		final AQuery aq = new AQuery(rootView);
		
		final CouponItem item = this.getItem(position);
		
		// マージンとるための空行
		if(item != null && item.getBlankFlg().equals("1")){
			rootView = mInflater.inflate(R.layout.menu_list_row_blank, null);
			return rootView;
		}

		if(item.getUseFlg().equals("1")){
			LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.couponLimitLayout);
			layout.setVisibility(View.INVISIBLE);
			ImageView sumi = (ImageView) rootView.findViewById(R.id.couponUsed);
			sumi.setVisibility(View.VISIBLE);
		}
		
		Log.v("MAGATAMA", "getView coupon item : "+position);
		
//		setPositionView(position, rootView);
		
		Button useBtn = (Button) rootView.findViewById(R.id.useBtn);
		useBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Log.v("MAGATAMA", "couponAdapter useBtnClick position:"+position);
				useCoupon(view, position, item, aq);
			}
		});
		
//		LinearLayout layout = (LinearLayout) view.findViewById(R.id.couponLimitLayout);
//		layout.setVisibility(View.GONE);
		
		Button policyBtn = (Button) rootView.findViewById(R.id.policyBtn);
		policyBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String rule = (String) item.getDescription();
				//String rule = (String) item.getPocliy();
				carousel_OnClickHandler(mainView, rule);
			}
		});
		
		FrameLayout layoutCaution = (FrameLayout) activity.findViewById(R.id.coupon_rule_wrapper);
		layoutCaution.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { caution_OnClickHandler(v); } 
		});
		
		if (item != null) {
						
			String useFlg = "0";
			String discount = "";
			String endDatetime = "";
			String imageFile = "";
			String type = "";
			String displayDays = "";
			try{
				discount = item.getDiscount().toString();
				endDatetime = item.getEndDatetime().toString();
				useFlg = item.getUseFlg().toString();
				imageFile = item.getFileName().toString();
				type = item.getType().toString();
				displayDays = item.getDisplayDays().toString();
			} catch (Exception e) {
				Log.v("MAGATAMA", "couponAdapter: list item error");
			}
			
			int couponType = Integer.parseInt(type);
			// クーポン種別
			switch(couponType){
			case 0:
				// 通常クーポン
				aq.id(R.id.useBtn).visibility(View.INVISIBLE);
				break;
			case 1:
				// 日数経過
				aq.id(R.id.useBtn).visibility(View.INVISIBLE);
				if(checkOverDisplayDays(Integer.parseInt(displayDays))){
					rootView = mInflater.inflate(R.layout.news_list_non, null);
					return rootView;
				}
				break;
			case 2:
				// 使いきり
				break;
			}
						
			if(useFlg.equals("1")){
				LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.couponLimitLayout);
				layout.setVisibility(View.INVISIBLE);
				ImageView sumi = (ImageView) rootView.findViewById(R.id.couponUsed);
				sumi.setVisibility(View.VISIBLE);
			}

			TextView textDiscount = (TextView) rootView.findViewById(R.id.couponDiscountNumber);
			Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "font/DINEngschriftStd.ttf");
			textDiscount.setTypeface(typeface);
			textDiscount.setText(discount);
			//textDiscount.setVisibility(View.INVISIBLE);
			TextView textDiscountLabel = (TextView) rootView.findViewById(R.id.couponDiscountLabel);
			textDiscountLabel.setTypeface(typeface);

			if(endDatetime.equals("")) {
				endDatetime = "なし";
			}
			TextView text = (TextView) rootView.findViewById(R.id.coupon_end_datetime);
			text.setText(endDatetime);
			
			String title = item.getTitle().toString();
			aq.id(R.id.coupon_list_title).text(title);
			
			if(discount.equals("") | discount.equals("0")){
				// 割引率エリアの非表示
				aq.id(R.id.coupon_discount_layout).visibility(View.GONE);
				
					// タイトル表示を中央に
					aq.id(R.id.coupon_list_title2).visibility(View.VISIBLE);
					aq.id(R.id.coupon_list_title2).text(title);
					aq.id(R.id.coupon_list_title).visibility(View.GONE);			
			}
			
			if(imageFile!=""){
				aq.id(R.id.coupon_top).background(0);
				aq.id(R.id.couponLimitLayout).background(0);
				aq.id(R.id.coupon_image_progress).visibility(View.VISIBLE);
				aq.id(R.id.coupon_custom_image_bottom).progress(R.id.blank_image).image(imageFile);
				//aq.id(R.id.coupon_custom_image).progress(R.id.blank_image).image(imageFile);
				aq.id(R.id.coupon_limit_label).visibility(View.VISIBLE);
				
				//画像全体をボタンにする
//				if(cnt == 0 && couponType==2) {
//					aq.id(R.id.coupon_custom_image_bottom).clickable(true);
//					aq.id(R.id.coupon_custom_image_bottom).clicked(new OnClickListener() {
//						public void onClick(View view) {
//							Log.v("MAGATAMA", "couponAdapter useBtnClick position:"+position);
//							useCoupon(view, position, item, aq);
//						}
//					});
//				}				
				// 非表示
//				aq.id(R.id.coupon_discount_layout).visibility(View.INVISIBLE);				
//				aq.id(R.id.coupon_list_title).visibility(View.INVISIBLE);
//				aq.id(R.id.coupon_list_title2).visibility(View.INVISIBLE);
//				aq.id(R.id.coupon_end_datetime).visibility(View.INVISIBLE);
//				aq.id(R.id.policyBtn).visibility(View.INVISIBLE);
//				aq.id(R.id.useBtn).visibility(View.INVISIBLE);
			}

		}
		return rootView;
	}
	
	private void carousel_OnClickHandler(View v, String rule) {
		FrameLayout layoutCaution = (FrameLayout) activity.findViewById(R.id.coupon_rule_wrapper);
		LinearLayout layoutBox = (LinearLayout) activity.findViewById(R.id.coupon_rule_box);
		if (layoutCaution.getVisibility() == View.GONE) {
			// ご利用条件: 文言セット
			TextView ruleText = (TextView)layoutBox.findViewById(R.id.coupon_rule_text);
			ruleText.setText(rule);

			// Fade-In Animation
			Animation animation = AnimationUtils.loadAnimation(activity, R.anim.coupon_fade_in);
			layoutBox.startAnimation(animation);
			layoutCaution.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 注意事項: OnClickイベントハンドラ
	 *
	 * 　・Fade-Outで注意事項を非表示へ
	 *
	 * @param v
	 */
	private void caution_OnClickHandler(View v) {
		if (v.getVisibility() == View.VISIBLE) {
			// Fade-In Animation
			Animation animation = AnimationUtils.loadAnimation(activity, R.anim.coupon_fade_out);
			v.startAnimation(animation);
			v.setVisibility(View.GONE);
		}
	}
	
//	private void setPositionView(int pos, View view) {
//		if( positionView.get(pos) == null ) {
//			positionView.put(pos, view);
//			TextView textDiscount = (TextView) view.findViewById(R.id.couponDiscountNumber);
//			Log.v("MAGATAMA", "setPositionView:"+pos+" discount:"+textDiscount.getText());
//		}
//	}
	
	public void useCoupon(final View view, final int position, final CouponItem item, final AQuery aq) {
		Log.v("MAGATAMA", "couponAdapter useCoupon");
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setTitle("クーポンの使用");
		alertDialogBuilder.setMessage("このクーポンを使用いたします。\nよろしいでしょうか？\n※再度ご利用ができなくなります。");
		alertDialogBuilder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
						ViewGroup parentParent = (ViewGroup) view.getParent().getParent();
						LinearLayout layout = (LinearLayout) parentParent.findViewById(R.id.couponLimitLayout);
						layout.setVisibility(View.INVISIBLE);
						Animation fade_out = AnimationUtils.loadAnimation(activity, R.anim.coupon_fade_out);
						layout.startAnimation(fade_out);						
						ImageView sumi = (ImageView) aq.id(R.id.couponUsed).getView();
						sumi.setVisibility(View.VISIBLE);
						Animation fade_in = AnimationUtils.loadAnimation(activity, R.anim.coupon_fade_in);
						sumi.startAnimation(fade_in);
						
						ContentValues values = new ContentValues();
						values.put("coupon_id", (String) item.getId());
						mydb.insert("use_coupon", null, values);
						mCouponItems.get(position).setUseFlg("1");
						String couponId = mCouponItems.get(position).getId().toString();
						sendUseCouponCount(couponId);
			}
		});
		alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();		
	}
	
//	public View getPositionView(int targetPosition) {
//		View targetView = positionView.get(targetPosition);
//		return targetView;
//	}
	
	private boolean checkOverDisplayDays(int days) {
		Date date = new Date();
		Date endDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);

		SharedPreferences pref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
		String str = pref.getString("first_boot_date", "");
		Log.v("MAGATAMA", "check first_boot_date: " + str);
		try {
			Calendar cal = Calendar.getInstance();
			endDate = sdf.parse(str);
			cal.setTime(endDate);
			cal.add(Calendar.DATE, days);
			endDate = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int ret = date.compareTo(endDate);
		
		if(ret>=0) {
			return true;
		}
		
		return false;		
	}
	
	public void sendUseCouponCount(String couponId) {
		Globals gl = new Globals();
		gl.initPreference(activity);
		AQuery aq = new AQuery(activity);
        HashMap<String, String> params = new HashMap<String, String>();
		params.put("client_id", gl.getClientID());
		params.put("coupon_id", couponId);
        
		aq.ajax(gl.getUrlApiCouponCount(), params, String.class, new AjaxCallback<String>() {
		    @Override
		    public void callback(String url, String result, AjaxStatus status) {
		    	if (status.getCode() != 200) {
			    	Log.v("MAGATAMA", "CouponCount error ("+ status.getCode() + "): " + result);
		    	}
		    }
		});
	}
	
	@Override
	public boolean isEnabled(int position) {
       return false;
	}

}
	