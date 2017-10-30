package app.com.hss.cooking.magatama.top;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;

import java.util.List;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.news.NewsItem;

public class NewsIndexAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<NewsItem> mNewsItems;
	//private Context context;
	
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

	public NewsIndexAdapter(Context context, List<NewsItem> objects) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNewsItems = objects;
		//this.context = context;
	}

	// 一行ごとのビューを作成する。
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.news_index_list_row, null);
		}
		NewsItem item = this.getItem(position);
		
		Log.v("MAGATAMA", "news item : "+item);
		
		if (item != null) {
			AQuery aq = new AQuery(view);
			
			// フォントからアイコン
			TextView icon = (TextView) view.findViewById(R.id.news_index_icon);
			Typeface typeface = Typeface.createFromAsset(aq.getContext().getAssets(), "font/ipost-icon01-regular.ttf");
			icon.setTypeface(typeface);
			icon.setTextSize(25);
			
			aq.id(R.id.news_list_title).text(item.getTitle().toString());
			aq.id(R.id.news_list_time).text(item.getDate().toString());
			aq.id(R.id.news_list_txt).text(item.getDescription().toString());
			if(item.getNewFlg().toString().equals("0")) {
				aq.id(R.id.news_status).visibility(View.INVISIBLE);
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
		}else{
			view = mInflater.inflate(R.layout.news_index_list_non, null);
		}
		return view;
	}
	
	/**
	 * ImageViewセットアップ
	 * @param aq
	 * @param item
	 */
	private void setImage(AQuery aq, NewsItem item) {
		this.setGone(aq);
		aq.id(R.id.news_image_view).image(item.getImage().toString(), true, true, 400, 0, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
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
}