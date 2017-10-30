package app.com.hss.cooking.magatama.shop;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class ShopSubCateFragment extends Fragment {
	private final static int PAGE_COUNT = 10;
	private Globals gl;
	private AQuery aq;

	private static ShopAdapter mShopAdapter;
	private int mStart = 0;
	private boolean mIsLoading = false;
	private View mViewFooter;
	private PullToRefreshListView mListView;
	
	private ArrayList<ShopItem> mItems;
	private View view;
	
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static ShopSubCateFragment newInstance() {
		ShopSubCateFragment fragment = new ShopSubCateFragment();
		return fragment;
	}

	public ShopSubCateFragment() {
	}

	@Override
	public void onAttach(Activity act){
		super.onAttach(act);		
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_shop_cate, container, false);
		
		mShopAdapter = new ShopAdapter(getActivity(), new ArrayList<ShopItem>());
		
		gl = new Globals();
		aq = new AQuery(view);

		mListView = (PullToRefreshListView) view.findViewById(R.id.shop_cate_listView);
		mListView.setAdapter(mShopAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int shopId = Integer.parseInt((String)mItems.get(position - 1).getId());
				replaceFragment(position, shopId);
			}
		});
		
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			public void onLastItemVisible() {
				if (!mIsLoading && mStart > 0) {
					getFooter().setVisibility(View.VISIBLE);
					listAsyncJson();
				}
			}
		});
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mShopAdapter = new ShopAdapter(getActivity(), new ArrayList<ShopItem>());
				mListView.setAdapter(mShopAdapter);
				mStart = 0;
				listAsyncJson();
			}
		});
		
		setupColorTheme();
		listAsyncJson();
		
		ActionBar actionBar = getActivity().getActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
		
		return view;
	}

    private void replaceFragment(int position, int shopId) {
    	
    	ShopDetailFragment fragment = new ShopDetailFragment();
    	Bundle bundle = new Bundle();

    	//bundle.putInt("position", position);
    	int parentId = getArguments().getInt("parent_id");
    	bundle.putInt("parent_id", parentId);
    	bundle.putInt("shop_id", shopId);
    	fragment.setArguments(bundle);

    	FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    	ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
    	ft.addToBackStack(null);
    	ft.replace(R.id.container, fragment);
    	ft.commit();
    }

	/**
	 *
	 */
	public void listAsyncJson() {
		mIsLoading = true;
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("client", gl.getClientID());
		//Bundle bundle = new Bundle();
		int parent_id = getArguments().getInt("parent_id");

		aq.ajax(gl.getUrlApiShopCate() + parent_id, params, JSONObject.class, this, "setListView");
	}

	/**
	 * @param url
	 * @param json
	 * @param status
	 */
	public void setListView(String url, JSONObject json, AjaxStatus status) {
		Log.v("MAGATAMA", "ShopSubCateFragment JSON : " + json);
		
		if (json != null) {
			mItems = new ArrayList<ShopItem>();
			
			try {
				
				JSONObject top = json.getJSONObject("shop_top");
				String fileName = top.getString("file_name");
				if(!fileName.equals("")){
					String topImage = gl.getUrlImgShopMulti() + top.getString("file_name");
					aq.id(R.id.shopTopImage).progress(R.id.blank_image).image(topImage, true, false, 400, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
				}
				
				JSONArray subCate = json.getJSONArray("sub_cate");
				int cateLen = subCate.length();
				
				JSONArray list = json.getJSONArray("list");
				int listLen = list.length();
				int cateId = -1;
				for (int i = 0; i < listLen; i++) {
					JSONObject obj = list.getJSONObject(i);

					ShopItem currentItem = new ShopItem();
					int pId = Integer.parseInt(obj.getString("parent_id"));
					//currentItem.setParentId(pId);
					
					if(pId!=cateId){
						for (int j = 0; j < cateLen; j++) {
							JSONObject cateObj = subCate.getJSONObject(j);
							int id = Integer.parseInt(cateObj.getString("id"));
							ShopItem cateItem = new ShopItem();
							cateItem = new ShopItem();
							cateItem.setId(cateObj.getString("id"));
							cateItem.setTitle(cateObj.getString("title"));
							cateItem.setIndexFlg(true);

							if(id==pId){
								mShopAdapter.add(cateItem);
								mItems.add(cateItem);
								cateId = pId;
							}
						}
					}
					
					currentItem.setId(obj.getString("id"));
					currentItem.setTitle(obj.getString("shop_name"));
					
					String image = obj.getString("file_name");
					
					if (image != null && image.length() > 0) {
						currentItem.setImage(gl.getUrlImgShopMulti() + image);
					}
					
					mShopAdapter.add(currentItem);
					mItems.add(currentItem);
					mStart += 1;
				}
				
				if (listLen == 0 || listLen < PAGE_COUNT) {
					mStart = -1;
					getFooter().setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.v("MAGATAMA", "メニューの読み込みでエラー");
			}
		}
		
		mIsLoading = false;
		getFooter().setVisibility(View.GONE);
		mShopAdapter.notifyDataSetChanged();
		mListView.onRefreshComplete();
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
					aq.id(R.id.menu_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.menu_bg).image(bgDraw);
				}
				
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
				}
				
				// 背景画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.menu_bg).progress(R.id.blank_image).image(image);
				}
				
			}			
		} catch(Exception e) {
			Log.v("MAGATAMA", "MenuDetailFragment: setupColorTheme error");
		}		
	}

	
	private View getFooter() {
		if (mViewFooter == null) {
			//			mViewFooter = getLayoutInflater().inflate(R.layout.view_footer, null);
			mViewFooter = aq.id(R.id.footer).getView();
		}
		return mViewFooter;
	}
}
