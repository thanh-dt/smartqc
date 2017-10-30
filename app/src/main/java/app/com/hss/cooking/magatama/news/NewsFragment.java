package app.com.hss.cooking.magatama.news;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class NewsFragment extends Fragment {
	private final static int PAGE_COUNT = 5;
	private Globals gl;
	private AQuery aq;

	private static NewsAdapter mNewsAdapter;
	private int mStart = 0;
	private boolean mIsLoading = false;
	private View mViewFooter;
	private PullToRefreshListView mListView;

	private int mMax = 5;
	private int jsonLength = 0;
	private static int itemId = 0;
	private ArrayList<NewsItem> mItems;
	private boolean mAddFooterMarginFlg = false;
	
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static NewsFragment newInstance() {
		itemId = 0;
		NewsFragment fragment = new NewsFragment();
		return fragment;
	}
	
	public static NewsFragment newInstance(int id) {
		itemId = id;
		NewsFragment fragment = new NewsFragment();
		return fragment;
	}

	public NewsFragment() {
	}

	@Override
	public void onAttach(Activity act){
		super.onAttach(act);

		mNewsAdapter = new NewsAdapter(act, new ArrayList<NewsItem>());
		Log.v("MAGATAMA", "NewsAdapter : " + mNewsAdapter);
	} 

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);

		Log.v("MAGATAMA", "NewsFragment : onCreateView");

		gl = new Globals();
		aq = new AQuery(view);

		mListView = (PullToRefreshListView) view.findViewById(R.id.news_listView);
		mListView.setAdapter(mNewsAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				Log.v("MAGATAMA", "NewsFragment itemClick");
			}
		});
		
		// 一番下ロード
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			public void onLastItemVisible() {
				if(itemId!=0) {
					// 記事単体のときはロードしない
					return;
				}
				if ( ! mIsLoading && mStart > 0) {
					getFooter().setVisibility(View.VISIBLE);
					mMax += 5;
					newsListAsyncJson();
				}
			}
		});

		// 再ダウンロード
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mNewsAdapter = new NewsAdapter(getActivity(), new ArrayList<NewsItem>());
				mListView.setAdapter(mNewsAdapter);
				mStart = 0;
				mMax = 5;
				mAddFooterMarginFlg = false;
				newsListAsyncJson();
			}
		});
		newsListAsyncJson();

		android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
        
		setupColorTheme();
        
		return view;
	}
		
	/**
	 *
	 */
	public void newsListAsyncJson() {
		mIsLoading = true;
		Map<String, Object> params = new HashMap<String, Object>();
		gl.initPreference(getActivity());
		params.put("client_id", gl.getClientID());
		params.put("token", gl.getDeviceToken());
		// params.put("offset", mStart);
		// params.put("limit", PAGE_COUNT);

		aq.ajax(gl.getUrlApiNews(), params, JSONArray.class, this, "setListView");
	}
	
	/**
	 * @param url
	 * @param json
	 * @param status
	 */
	public void setListView(String url, JSONArray json, AjaxStatus status) {
		if (json != null) {
			mItems = new ArrayList<NewsItem>();
			
			try {
				jsonLength = json.length();
				Log.v("MAGATAMA", "jsonLength: " + jsonLength);
								
				if (mMax >= jsonLength) {
					mMax = jsonLength;
				}
				for (int i = 0; i < mMax; i++) {
					Log.v("MAGATAMA", "mStart: "+mStart + " mMax: "+mMax);
					if (mStart > i) {
						continue;
					}
					
					JSONObject obj = json.getJSONObject(i);

					NewsItem currentItem = new NewsItem();
					currentItem.setId(obj.getString("id"));
					currentItem.setTitle(obj.getString("title"));
					currentItem.setDescription(obj.getString("body"));
					currentItem.setDate(obj.getString("send_at"));
					currentItem.setType(obj.getString("file_type"));
					currentItem.setIineCount(obj.getString("iine_cnt"));
					currentItem.setIineFlg(obj.getString("iine_flg"));
					String image = obj.getString("file_name");
					
					int type = Integer.parseInt(obj.getString("file_type"));
					if (type!=0) {
						switch (type) {
						case 1:
							currentItem.setImage(gl.getUrlImgNews() + image);
							break;
						case 2:
							currentItem.setImage(gl.getUrlVideoNews() + image);
							break;
						case 3:
							currentItem.setImage(obj.getString("youtube"));
							break;
						default:
							break;
						}
						Log.v("MAGATAMA", gl.getUrlImgNews() + image);
					}
					
					if( itemId != 0 ) {
						// 1件だけ表示のとき
						int id = Integer.parseInt(obj.getString("id"));
						if( itemId == id ) {
							mNewsAdapter.add(currentItem);
							mItems.add(currentItem);
							mStart += 1;
							// フッターのマージン用に空行を追加
							setBlankItem();
						}
					} else {
						mNewsAdapter.add(currentItem);
						mItems.add(currentItem);
						mStart += 1;
					}
				}
								
				if (mMax == 0 || mMax < PAGE_COUNT) {
					mStart = -1;
					getFooter().setVisibility(View.GONE);
				}
				
				if(mMax >= jsonLength) {
					setBlankItem();
				}
				// 一番下まで来た時、フッターのマージン用に空行を追加する
				if(mStart==jsonLength && !mAddFooterMarginFlg) {
					setBlankItem();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Log.v("MAGATAMA", e.getMessage());
			}
			
		} else {
			aq.id(R.id.news_blank_text).visible();
			aq.id(R.id.news_back_icon).visible();
		}
		
		mIsLoading = false;
		getFooter().setVisibility(View.GONE);

		mNewsAdapter.notifyDataSetChanged();
		mListView.onRefreshComplete();
	}
	
	private void setBlankItem() {
		if(!mAddFooterMarginFlg){
			NewsItem blankItem = new NewsItem();
			blankItem.setBlankFlg("1");
			mNewsAdapter.add(blankItem);
			mAddFooterMarginFlg = true;
		}
	}
	
	private void setupColorTheme() {
		ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(getActivity(), new Globals());
		String jsonStr = api.getCurrentData();
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONObject bg = jsonObj.getJSONObject("background");
			if (bg != null) {
				String color = "#" + bg.getString("color");
				String type = bg.getString("type");
				String gradColorStart = "#" + bg.getString("gradation1");
				String gradColorEnd = "#" + bg.getString("gradation2");
//				String barGradColorStart = "#" + bg.getString("bar_gradation1");
//				String barGradColorEnd = "#" + bg.getString("bar_gradation2");
//				String subGradColor = "#" + bg.getString("sub_gradation");

				// 背景設定
				if(type.equals("1")) {
					// 単一カラー
					aq.id(R.id.news_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.news_bg).image(bgDraw);
				}
				
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
				}
				
				// 背景画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.news_bg).progress(R.id.blank_image).image(image);
				}
			}
		} catch(Exception e) {
			Log.v("MAGATAMA", "TopFragment: setupColorTheme error: " + e);
		}

	}
	
	private View getFooter() {
		if (mViewFooter == null) {
			// mViewFooter = getLayoutInflater().inflate(R.layout.view_footer, null);
			mViewFooter = aq.id(R.id.footer).getView();
		}
		return mViewFooter;
	}
}
