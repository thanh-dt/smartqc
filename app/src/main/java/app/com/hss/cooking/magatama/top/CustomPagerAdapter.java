package app.com.hss.cooking.magatama.top;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidquery.AQuery;

import java.util.ArrayList;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.coupon.CouponFragment;
import app.com.hss.cooking.magatama.menu.MenuFragment;
import app.com.hss.cooking.magatama.news.NewsFragment;

public class CustomPagerAdapter extends PagerAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<FrameLayout> mItemList;
	private ArrayList<TopImageItem> mTopImageItem;
	
	public CustomPagerAdapter(Context context) {
		super();
		mContext  = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItemList = new ArrayList<FrameLayout>();
		mTopImageItem = new ArrayList<TopImageItem>();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		FrameLayout layout = mItemList.get(position);
		Log.v("MAGATAMA", "Pager pos: "+position);

		
		TopImageItem item = mTopImageItem.get(position);
		int type = Integer.parseInt(item.getLinkType().toString());
		View imageView = (View) layout.findViewById(R.id.main_carousel_scroll);

		switch(type) {
		case 0:
			// リンクなし
			break;
		case 1:
			// URLリンク
			final String url = item.getUrl().toString();
			setupUrlLink(imageView, url);
//			imageView.setClickable(true);
//			imageView.setOnClickListener(new OnClickListener() {
//				public void onClick(View v) {
//					FragmentActivity act = (FragmentActivity) mContext;
//					Uri uri = Uri.parse(url);
//					Intent i = new Intent(Intent.ACTION_VIEW,uri);
//					act.startActivity(i);
//				}
//			});
			break;
		case 2:
			// アプリ内リンク
			final String linkApp = item.getLinkApp().toString();
			setupAppLink(imageView, linkApp);
			break;
		}
		
		container.addView(layout);
		return layout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getCount() {
		return this.mItemList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	/**
	 * ViewPagerへ表示するアイテムのセットアップ
	 *
	 *  ・画像表示: 非同期で指定URL先の画像を表示
	 *  　　　　　　　　L　ロード中: プログレス表示
	 *  　　　　　　　　L　画像がない: no-image画像表示
	 *
	 * @param urlList
	 */
	public void setupItemList(ArrayList<String> urlList) {
		for (String url : urlList) {
			FrameLayout layout = (FrameLayout) mInflater.inflate(R.layout.main_carousel, null);
			
			AQuery aq = new AQuery(layout);
			aq.id(R.id.main_carousel_scroll).progress(R.id.blank_image).image(url, true, false, 400, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
			mItemList.add(layout);
		}
	}
	
	public void add(TopImageItem item) {
		mTopImageItem.add(item);
	}
	
	private void setupUrlLink(View imageView, final String url) {
		imageView.setClickable(true);
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentActivity act = (FragmentActivity) mContext;
				Uri uri = Uri.parse(url);
				Intent i = new Intent(Intent.ACTION_VIEW,uri);
				act.startActivity(i);
			}
		});
	}
	
	private void setupAppLink(View imageView, final String linkApp) {
		imageView.setClickable(true);
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				replaceFragment(linkApp);
			}
		});
	}
	
	private void replaceFragment(String link){
		FragmentActivity act = (FragmentActivity) mContext;
		FragmentManager fragmentManager = act.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		fragmentTransaction.addToBackStack(null);
		if(link.equals("news")) {
			fragmentTransaction.replace(R.id.container, NewsFragment.newInstance(), "NewsFragment");
		} else if(link.equals("menu")) {
			fragmentTransaction.replace(R.id.container, MenuFragment.newInstance(), "MenuFragment");
		} else if(link.equals("coupon")) {
			fragmentTransaction.replace(R.id.container, CouponFragment.newInstance(), "CouponFragment");
		}
		fragmentTransaction.commit();
	}
	
}