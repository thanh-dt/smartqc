/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import app.com.hss.cooking.R;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "HTTP";

	private Handler toaster;
	private WakeLock wakelock;
	private KeyguardLock keylock;
	private String message;
	private final static int WC = RelativeLayout.LayoutParams.WRAP_CONTENT;

	@Override
    public void onCreate() {
        super.onCreate();
        toaster = new Handler(); 
    }

	@Override
	protected void onRegistered(Context context, String registrationId) {
		// displayMessage(context, getString(R.string.gcm_registered));
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");

		Globals gl = new Globals();

		// displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			Log.i(TAG, "Device RegiID=" + registrationId);
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	public void postCallback(String url, String res, AjaxStatus status) {
		Log.i(TAG, "postCallback");

		if (res != null) {
			// successful ajax call
		} else {
			// ajax error
		}
	}

//	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
//	@Override
//	protected void onMessage(Context context, Intent intent) {
//		Log.i(TAG, "Received message");
//		// String message = getString(R.string.gcm_message);
//		// displayMessage(context, message);
//		// notifies user
//		String message = intent.getStringExtra("message");
//		generateNotification(context, message);
//		
//		SharedPreferences prefs = getSharedPreferences("tabIndex", Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = prefs.edit();
//		
//		editor.putInt("tabIndex", 2);
//		editor.apply();
//	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onMessage(Context context, Intent intent) {
		// String message = getString(R.string.gcm_message);
		// displayMessage(context, message);
		// notifies user
		message = intent.getStringExtra("message");
		generateNotification(context, message);
		
		SharedPreferences prefs = getSharedPreferences("tabIndex", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt("tabIndex", 2);
		editor.apply();
		
 		//スリープ状態から復帰する
		wakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.ON_AFTER_RELEASE, "disableLock");
		wakelock.acquire();
		
		//スクリーンロックを解除する
		KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	    keylock = keyguard.newKeyguardLock("disableLock");
		keylock.disableKeyguard();
		
		//トースト表示
		CharSequence v = intent.getCharSequenceExtra("rl");
		toast(v);
	    
		sleep(4000); //4000ミリ秒Sleepする
		
		//スリープ、ロック解除リリース処理
		keylock.reenableKeyguard();
		wakelock.release();

	 }

	
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.app_name);
		// notifies user
		generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		// displayMessage(context,
		// getString(R.string.gcm_recoverable_error,errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name) + " からお知らせがあります。";

		Intent notificationIntent = new Intent(context, ViewActivity.class);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

//    	notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}

	  //トースト表示処理
	  private void toast(CharSequence v) {
	        toaster.post(new Runnable() {
				@Override
	            public void run() {	        		
				    Resources r = getResources();
				    Bitmap bmp1 = BitmapFactory.decodeResource(r, R.drawable.popup);

				    //ポップアップ表示画像
				    ImageView v = new ImageView(GCMIntentService.this);  
					v.setImageBitmap(bmp1);

				    //ポップアップ画像サイズ取得
				    float img_w = bmp1.getWidth();
				    float img_h = bmp1.getHeight();

			  		//画面サイズ取得
					WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
				    Display disp = wm.getDefaultDisplay();
				    Point size = new Point();
				    disp.getSize(size);
				    
				    //画面の幅・高さ
				    @SuppressWarnings("deprecation")
					float width = disp.getWidth();	    
				    float height = disp.getHeight();
				    
				    //比率 座標調節用
				    float ratioX = width / 720;
				    float ratioY = height /1280;
				    
					//座標位置計算
				    float w_position = width - img_w;
				    float h_position = height - img_h;
				    
				    //座標位置計算
				    float ratio_left = w_position / 2;
				    float ratio_top = h_position / 2;
	        		
	        		//アプリ名 + からのお知らせ + ニュースタイトル
	        		String news_title = message;
	        		String title = GCMIntentService.this.getString(R.string.app_name) + " からお知らせがあります。\n\n" + news_title;
	        		EditText edit1 = new EditText(GCMIntentService.this);
	                edit1.setWidth((int)(480*ratioX));
	                edit1.setTextSize(16.0f);
	                edit1.setTextColor(Color.WHITE);
	                edit1.setBackgroundColor(Color.rgb(55, 149, 189));
	                edit1.setText(title, BufferType.NORMAL);
	                edit1.setFocusable(false);
	                
	    	        // マージンをピクセル単位で指定
	    	        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(WC, WC);
	    	        params1.setMargins((int)(ratio_left), (int)(ratio_top-50), 0, 0);

	    	        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(WC, WC);
	    	        params2.setMargins((int)(ratio_left+20), (int)(ratio_top+(130*ratioY)), 0, 0);
	                
	                //レイアウト
	                RelativeLayout rl = new RelativeLayout(GCMIntentService.this);
	        		rl.addView(v, params1);
	        		rl.addView(edit1, params2);
	                
	        		//表示トーストの調節処理
	        		Toast toast = new Toast(GCMIntentService.this);
	        		toast.setDuration(Toast.LENGTH_LONG);
	        		toast.setView(rl);
	        		toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
	        		toast.show();

	            }
	        });
	    }

	  protected synchronized void sleep(long msec)
	    {	
	    	try
	    	{
	    		wait(msec);
	    	}catch(InterruptedException e){}
	    }

}
