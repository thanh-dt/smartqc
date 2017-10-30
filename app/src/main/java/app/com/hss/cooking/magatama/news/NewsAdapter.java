package app.com.hss.cooking.magatama.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.util.HashMap;
import java.util.List;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;

public class NewsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<NewsItem> mNewsItems;
	private Globals gl;
	private AQuery aq;
	private Context context;
	private String _newsId;

	public void add(NewsItem newsItem) {
		mNewsItems.add(newsItem);
	}

	@Override
	public int getCount() {
		return mNewsItems.size();
	}

	@Override
	public NewsItem getItem(int position) {
		// TODO Auto-generated method stub
		return mNewsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public NewsAdapter(Context context, List<NewsItem> objects) {
		gl = new Globals();
		gl.initPreference(context);
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNewsItems = objects;
	}

	// 一行ごとのビューを作成する。
	public View getView(final int position, View convertView, ViewGroup parent) {

//		View view = convertView;
//		if (convertView == null) {
//			view = mInflater.inflate(R.layout.news_list_row, null);
//		}
		View view = mInflater.inflate(R.layout.news_list_row, null);
		
		NewsItem item = this.getItem(position);
		
		Log.v("MAGATAMA", "news item : "+position);
		
		if (item != null) {
			
			// マージンとるための空行
			if(item.getBlankFlg().equals("1")){
				view = mInflater.inflate(R.layout.menu_list_row_blank, null);
				return view;
			}
			
			_newsId = item.getId().toString();
			
			aq = new AQuery(view);
			
			final int iineCount = Integer.parseInt(item.getIineCount().toString());
			final String newsId = item.getId().toString();
						
			Button iineTextBtn = (Button) view.findViewById(R.id.iineTextBtn);
			iineTextBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// いいね！送信
					sendIine(newsId, position,view);
				}
			});
			
			aq.id(R.id.news_list_title).text(item.getTitle().toString());
			aq.id(R.id.news_list_time).text(item.getDate().toString());
			aq.id(R.id.news_list_txt).text(item.getDescription().toString());
			
			if(item.getIineFlg().toString().equals("1")) {
				//aq.id(R.id.iineTextBtn).text("いいね！を取り消す");
				Resources res = context.getResources();
				aq.id(R.id.iineTextBtn).textColor(res.getColor(R.color.iine_blue));
			}
			
			//int iineCount = Integer.parseInt(item.getIineCount().toString());
			if(iineCount > 0) {
				String iine = "いいね！ " + iineCount + "件";
				aq.id(R.id.iine_count).text(iine);
			} else {
				aq.id(R.id.iine_count).visibility(View.INVISIBLE);
			}
			
			if (item.getImage() != null && item.getImage().length() > 0) {
				switch (Integer.parseInt(item.getType().toString())) {
					case 1:{
						this.setImage(aq, item);
					}
					break;
					case 2:{
						this.setVideo(aq, item);
					}
					break;
					case 3:{
						this.setWebView(aq, item);
					}
					break;
				}
			} else {
				aq.id(R.id.news_image_view).clear();
				this.setGone(aq);
			}
		}
//		else{
//			view = mInflater.inflate(R.layout.news_list_non, null);
//		}
		return view;
	}
	
	/**
	 * ImageViewセットアップ
	 * @param aq
	 * @param item
	 */
	private void setImage(AQuery aq, NewsItem item) {
		this.setGone(aq);
		aq.id(R.id.news_image_view).progress(R.id.blank_image).image(item.getImage().toString(), true, true, 400, 0, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
		aq.id(R.id.news_image_view).getView().setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @param aq
	 * @param item
	 */
	private void setVideo(AQuery aq, NewsItem item) {
		this.setGone(aq);
		aq.id(R.id.news_video_view).getView().setVisibility(View.VISIBLE);
		// コントローラの設定
		MediaController controller = new MediaController(aq.getContext());
		
		VideoView video = (VideoView)aq.id(R.id.news_video_view).getView();
		video.setMediaController(controller);
		controller.setAnchorView(video);
		
		video.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// 準備完了したときの処理
				mp.seekTo(0);
			}
		});
		
		video.setVideoURI(Uri.parse(item.getImage().toString()));
	}
	
	/**
	 * WebViewセットアップ
	 * @param aq
	 * @param item
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void setWebView(AQuery aq, NewsItem item) {
		this.setGone(aq);
		WebView webView = (WebView)aq.id(R.id.news_web_view).getView();
		webView.setWebChromeClient(new WebChromeClient());		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(item.getImage().toString());
		
		aq.id(R.id.news_web_view).getView().setVisibility(View.VISIBLE);
	}
	
	private void setGone(AQuery aq) {
		aq.id(R.id.news_image_view).getView().setVisibility(View.GONE);
		aq.id(R.id.news_web_view).getView().setVisibility(View.GONE);
		aq.id(R.id.news_video_view).getView().setVisibility(View.GONE);
	}
	
	public void sendIine(String newsId, final int pos, final View view) {
		//gl.initPreference(getActivity());
        HashMap<String, String> params = new HashMap<String, String>();
		params.put("client_id", gl.getClientID());
		params.put("news_id", newsId);
		params.put("token", gl.getDeviceToken());
		aq.ajax(gl.getUrlApiIine(), params, String.class, new AjaxCallback<String>() {
		    @Override
		    public void callback(String url, String result, AjaxStatus status) {
		    	if (status.getCode() != 200) {
		    		Toast.makeText(context, "通信に失敗しました", Toast.LENGTH_SHORT).show();
			    	Log.v("MAGATAMA", "Iine error ("+ status.getCode() + "): " + result);
		    	} else {
		    		Log.v("MAGATAMA", "いいね！：pos="+pos+" | "+mNewsItems.get(pos).getTitle().toString());
		    		int cnt = Integer.parseInt(mNewsItems.get(pos).getIineCount().toString());
		    		String flg = (String) mNewsItems.get(pos).getIineFlg();
		    		if(flg.equals("1")) {
		    			// 減算
		    			cnt = cnt - 1;
		    			flg = "0";
		    			Button btn = (Button) view.findViewById(R.id.iineTextBtn);
		    			Resources res = context.getResources();
		    			btn.setTextColor(res.getColor(R.color.iine_gray));
		    			//btn.setText("いいね！");
		    		} else {
		    			// 加算
		    			cnt = cnt + 1;
		    			flg = "1";
		    			Button btn = (Button) view.findViewById(R.id.iineTextBtn);		    			
		    			Resources res = context.getResources();
		    			btn.setTextColor(res.getColor(R.color.iine_blue));
		    			//btn.setText("いいね！を取り消す");
		    		}
		    		mNewsItems.get(pos).setIineFlg(flg);
		    		mNewsItems.get(pos).setIineCount(String.valueOf(cnt));

		    		String iine = "いいね！ " + cnt + "件";
		    		View parent = (View) view.getParent().getParent().getParent();
		    		TextView tv = (TextView) parent.findViewById(R.id.iine_count);
		    		tv.setText(iine);
		    		if(cnt > 0){
		    			tv.setVisibility(View.VISIBLE);
		    		} else {
		    			// 0件のときは表示しない
		    			tv.setVisibility(View.INVISIBLE);
		    		}
		    	}
		    }
		});
	}

	@Override
	public boolean isEnabled(int position) {
		boolean ret = true;
		// 空行はクリックできないようにする
		if(mNewsItems.get(position).getBlankFlg().equals("1")) {
			ret = false;
		}
		return ret;
	}
}