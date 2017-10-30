package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.api.ServerAPITopBlock;
import app.com.hss.cooking.magatama.asynctask.AsyncTaskSplash;
import app.com.hss.cooking.magatama.stampcard.ActivityRegisterStampCard;
import app.com.hss.cooking.magatama.utils.PermissionUtils;

import static app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.CommonUtilities.SENDER_ID;

public class SplashActivity extends Activity implements OnClickListener, LocationListener {

    private Globals gl;
    private AQuery aq;

    private AlertDialog.Builder mAlertDialogBuilder;
    private AsyncTask<Void, Void, Void> mRegisterTask;

    private VideoView video;
    private Button btnStart;
    //	private Button btnRegist;
    private Button btnLogin;
    private Vibrator vib;

    private LocationManager locationManager;
    private int providerNum = 0;

    //	private int movie_cnt;
    private boolean FinishFlag;

    private boolean isFirstBoots = false;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Globals初期化
        gl = new Globals();
        gl.initPreference(this);

        aq = new AQuery(this);

        sendBootLog();
//        checkFirstBoot();

        // APIサーバーからデータ取得
        AsyncTaskSplash taskSplash = new AsyncTaskSplash(this, gl);
        taskSplash.execute();

        // GCM関連処理
        this.initGCM();

        // セットアップ
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//		movie_cnt = 1;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // ボタン
        this.setupButtons();

        asyncSplashImage();
        setupColorTheme();
        userDataAsyncJson();

        // VideoPlayer
        //this.setupMovie(R.raw.bg02_1);

        // スプラッシュ画面表示後に自動遷移
        Handler handler = new Handler();
        handler.postDelayed(new splashHandler(), 500);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.requestPermission(SplashActivity.this, 100, Manifest.permission.ACCESS_FINE_LOCATION)) {
            String gpsStatus = Secure.getString(this.getContentResolver(), Secure.LOCATION_PROVIDERS_ALLOWED);
            if (gpsStatus.matches(".*" + LocationManager.NETWORK_PROVIDER + ".*")) {
                providerNum++;
                // ネットワークから取得を開始する

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
            }

            if (gpsStatus.matches(".*" + LocationManager.GPS_PROVIDER + ".*")) {
                providerNum++;
                // GPSから取得を開始する
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onPause() {
        if (PermissionUtils.requestPermission(this, PermissionUtils.REQ_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (locationManager != null) {
                locationManager.removeUpdates(this);
            }
        }
        super.onPause();
    }

    /**
     * GPS情報の取得
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.v("MAGATAMA", "----------");
        Log.v("MAGATAMA", "Latitude" + String.valueOf(location.getLatitude()));
        Log.v("MAGATAMA", "Longitude" + String.valueOf(location.getLongitude()));
        Log.v("MAGATAMA", "Accuracy" + String.valueOf(location.getAccuracy()));
        Log.v("MAGATAMA", "Altitude" + String.valueOf(location.getAltitude()));
        Log.v("MAGATAMA", "Time" + String.valueOf(location.getTime()));
        Log.v("MAGATAMA", "Speed" + String.valueOf(location.getSpeed()));
        Log.v("MAGATAMA", "Bearing" + String.valueOf(location.getBearing()));
        Log.v("MAGATAMA", "----------");

        // 全てのプロバイダからの現在地を取得したら取得処理をやめる
        providerNum--;
        if (providerNum == 0) {
            locationManager.removeUpdates(this);
        }

        this.sendLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    }

    public void sendLocation(String lat, String lng) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());
        params.put("device", "2");
        params.put("lat", lat);
        params.put("lng", lng);

        aq.ajax(gl.getUrlToken(), params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                // APIのレスポンスが文字列で返ってくる
                Log.v("MAGATAMA", result);
            }
        });
    }

    /**
     * セットアップ：ボタン
     */
    public void setupButtons() {
        btnStart = (Button) findViewById(R.id.splash_btn_start);
        btnStart.setOnClickListener(this);

//		btnRegist = (Button)findViewById(R.id.splash_btn_regist);
//		btnRegist.setOnClickListener(this);
        aq.id(R.id.splash_btn_regist).clicked(this, "onClickNotificationTest");

        btnLogin = (Button) findViewById(R.id.splash_btn_login);
        btnLogin.setOnClickListener(this);
    }

    /**
     * セットアップ：ムービー
     */
    public void setupMovie(int raw) {
        String path = "android.resource://jp.co.hiropro.magatama1/" + raw;

        video = (VideoView) findViewById(R.id.videoView);
        Uri pathToVideo = Uri.parse(path);
        video.setVideoURI(pathToVideo);

        // コントローラの設定
        video.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 準備完了したときの処理
                mp.start();
            }
        });
        video.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 完了したときの処理
                mp.seekTo(0);
                mp.start();
            }
        });
    }

    public void setupSplashMovie(String fileName) {
        String path = gl.getUrlImgSplash() + fileName;

        video = (VideoView) findViewById(R.id.videoView);
        Uri pathToVideo = Uri.parse(path);
        video.setVideoURI(pathToVideo);

        // コントローラの設定
        video.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 準備完了したときの処理
                mp.start();
            }
        });
        video.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 完了したときの処理
                mp.seekTo(0);
                mp.start();
            }
        });
    }

    public void setupSplashImage(String fileName) {
        video = (VideoView) findViewById(R.id.videoView);
        video.setVisibility(View.INVISIBLE);
        String path = gl.getUrlImgSplash() + fileName;
        aq.id(R.id.imageView3).image(path, true, false, 400, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE).visibility(View.VISIBLE);
    }

    /**
     * ボタンイベント
     */
    @Override
    public void onClick(View v) {
        // TODO 自動生成されたメソッド・スタブ
        vib.vibrate(50);

        switch (v.getId()) {
            case R.id.splash_btn_start:
                this.clickStart();
                break;
            case R.id.splash_btn_regist:
                //this.clickRegist();
                break;
            case R.id.splash_btn_login:
                //this.clickLogin();
                break;
        }
    }

    /**
     * 「そのままスタート」
     */
    public void clickStart() {
        // HIROPRO Add
        ServerAPITopBlock apiData = ServerAPITopBlock.getInstance(this,
                new Globals());
        final String member_status = apiData.getMemberCardStatus();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.JAPANESE);
        SharedPreferences pref = getSharedPreferences("pref",
                Context.MODE_PRIVATE);
        String endDateStr = pref.getString("first_boot_date", "");

        // 初回起動かつ データがないもしくは、会員機能がOFFだった場合
        if (endDateStr.equals("") && member_status.equals("0") || endDateStr.equals("") && member_status.equals("")) {
            Date date = new Date();
            String dateStr = (String) sdf.format(date);
            Editor e = pref.edit();
            e.putString("first_boot_date", dateStr);
            e.commit();
            isFirstBoots = true;
        }
        // 初回起動かつ 会員機能がONだった場合
        if (endDateStr.equals("") && member_status.equals("1")) {
            Date date = new Date();
            String dateStr = (String) sdf.format(date);
            Editor e = pref.edit();
            e.putString("first_boot_date", dateStr);
            e.commit();
            isFirstBoots = true;
        }
        // すでに一回起動された以降の場合
        if (!endDateStr.equals("")) {
            isFirstBoots = false;
        }
//        isFirstBoots = true;
        if (isFirstBoots) {
            Intent goRegister = new Intent(SplashActivity.this,
                    ActivityRegisterStampCard.class);
            startActivity(goRegister);
//            analyticDownload();
            this.finish();
        } else {
            SplashActivity.this.moveToMainActivity();
        }
    }

    /**
     * 「新規登録をする」
     */
//	public void clickRegist() {
//		switch(movie_cnt) {
//		case 1:
//			this.setupMovie(R.raw.bg02_1);
//			break;
//		case 2:
//			this.setupMovie(R.raw.bg02_2);
//			break;
//		case 3:
//			this.setupMovie(R.raw.bg02_3);
//			break;
//		}
//		if (movie_cnt == 3) {
//			movie_cnt = 1;
//		} else {
//			movie_cnt ++;
//		}
//	}

    /**
     * 「ログイン」
     */
//	public void clickLogin() {
//		this.setupMovie(R.raw.bg01);
//	}

    /**
     * 自動画面遷移用のクラス
     */
    class splashHandler implements Runnable {
        public void run() {
            //			SplashActivity.this.moveToMainActivity();
        }
    }

    /**
     * 画面遷移処理
     * ※ネットワーク通信が不可の場合はアラート表示
     */
    private void moveToMainActivity() {
        boolean conected = this.isConnected();
        AlertDialog.Builder builder = this.getAlertDialogBuilder();

        final Intent intent = new Intent(getApplication(), ViewActivity.class);

        if (conected) {
            /**
             * スプラッシュ完了後に実行するActivityを指定します。
             */
            startActivity(intent);
            SplashActivity.this.finish();
        } else {
            // AlertDialog表示
            builder.setTitle("ネットワーク接続エラー");
            builder.setMessage("一部ご利用できない機能がございます。\nネットワークに接続の上、アプリケーションの起動をお願い致します。");
            builder.setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            });

            builder.create().show();
        }
    }

    /**
     * ネットワーク接続チェック
     *
     * @return
     */
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        } else {
            return cm.getActiveNetworkInfo().isConnected();
        }
    }

    /**
     * AlertDialogBuilderインスタンス取得
     *
     * @return
     */
    private AlertDialog.Builder getAlertDialogBuilder() {
        if (mAlertDialogBuilder == null) {
            mAlertDialogBuilder = new AlertDialog.Builder(this);
            mAlertDialogBuilder.setPositiveButton("OK", null);
        }
        return mAlertDialogBuilder;
    }

    /**
     * GCM関連処理
     * ※暫定的にメソッド切り分け
     */
    private String regID;

    private void initGCM() {
        mRegisterTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                regID = createToken();
                regID = getTokenRegister();
                if (TextUtils.isEmpty(regID)) {
                    regID = createToken();
                }
                Log.e("TOKEN", regID);
                ServerUtilities.register(SplashActivity.this, regID);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mRegisterTask = null;
            }
        };
        mRegisterTask.execute();
    }

    private String getTokenRegister() {
        String registrationId = gl.getDeviceToken();
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }

    private String createToken() {
        String token = "";
        if (GoogleServiceUtils.checkPlayServices(SplashActivity.this)) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try {
                token = instanceID.getToken(SENDER_ID,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                gl.setDeviceToken(token);
                Log.e("token----:", "---values :" + token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO 自動生成されたメソッド・スタブ
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.v("Status", "AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.v("Status", "OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v("Status", "TEMPORARILY_UNAVAILABLE");
                break;

        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            if (FinishFlag) {
                finish();
            } else {
                Toast.makeText(this, "もう一度押すと終了します。", Toast.LENGTH_SHORT).show();
                FinishFlag = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FinishFlag = false;
                    }


                }, 2000);
            }

            return true;
        }
    }

    /**
     * @param v
     */
    public void onClickNotificationTest(View v) {
        Intent i = new Intent(SplashActivity.this, SplashActivity.class);

        // ActionButtonをタッチした時に呼び出されるPendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentIntent(contentIntent);
        builder.setTicker("Ticker");
        builder.setContentTitle("ContentTitle");
        builder.setContentText("ContentText");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(largeIcon);
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS);
        builder.setAutoCancel(true);

        // BigTextStyle を適用
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(builder);
        bigTextStyle.bigText("BigText");
        bigTextStyle.setBigContentTitle("BigContentTitle");
        bigTextStyle.setSummaryText("SummaryText");

        // BigPictureStyle
        Bitmap bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle(builder);
        bigPictureStyle.bigPicture(bigPicture);
        bigPictureStyle.setBigContentTitle("BigContentTitle");
        bigPictureStyle.setSummaryText("SummaryText");

        // NotificationManagerを取得
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Notificationを作成して通知
        manager.notify(0x01, bigTextStyle.build());
    }

    public void sendBootLog() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("action", "0");

        aq.ajax(gl.getUrlBootLog(), params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                if (status.getCode() != 200) {
                    Log.v("MAGATAMA", "bootLog error (" + status.getCode() + "): " + result);
                }
            }
        });
    }

    public void asyncSplashImage() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client", gl.getClientID());

        aq.ajax(gl.getUrlApiSplash(), params, JSONArray.class, this, "setSplashImage");
    }

    public void setSplashImage(String url, JSONArray json, AjaxStatus status) throws JSONException {
        JSONObject obj = json.getJSONObject(0);
        String fileName = obj.getString("file_name");
        String fileType = obj.getString("file_type");
        if (fileType.equals("movie")) {
            setupSplashMovie(fileName);
        } else {
            setupSplashImage(fileName);
        }
    }

    public void userDataAsyncJson() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());

        aq.ajax(gl.getUrlApiUserLogin(), params, JSONArray.class, this, "setUserData");
    }

    public void setUserData(String url, JSONArray json, AjaxStatus status) throws JSONException {
        if (json != null) {
            JSONObject obj = json.getJSONObject(0);
            String userId = obj.getJSONObject("user").getString("user_id");
            gl.setPrefKeyUserId(userId);
        }
    }

    private void checkFirstBoot() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.JAPANESE);
        SharedPreferences pref = getSharedPreferences("pref",
                Context.MODE_PRIVATE);
        String endDateStr = pref.getString("first_boot_date", "");

        if (endDateStr.equals("")) {
            Date date = new Date();
            String dateStr = (String) sdf.format(date);
            Editor e = pref.edit();
            e.putString("first_boot_date", dateStr);
            e.commit();
            isFirstBoots = true;
        } else {
            isFirstBoots = false;
        }
    }

    private void setupColorTheme() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(this, new Globals());
        String jsonStr = api.getCurrentData();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject bg = jsonObj.getJSONObject("background");
            if (bg != null) {
                String type = bg.getString("type");
                String color = "#" + bg.getString("color");
                String subColor = "#" + bg.getString("sub_color");
                //String gradColorStart = "#" + bg.getString("gradation1");
                //String gradColorEnd = "#" + bg.getString("gradation2");
                String barGradColorStart = "#" + bg.getString("bar_gradation1");
                String barGradColorEnd = "#" + bg.getString("bar_gradation2");
                String subGradColor = "#" + bg.getString("sub_gradation");

                // 背景設定
                if (type.equals("1")) {
                    // 単一カラー
                    aq.id(R.id.splash_layout).backgroundColor(Color.parseColor(color));
                    aq.id(R.id.splash_btn_start).backgroundColor(Color.parseColor(subColor));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{Color.parseColor(barGradColorStart), Color.parseColor(barGradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(Orientation.BOTTOM_TOP, colors);
                    aq.id(R.id.splash_layout).getView().setBackground(bgDraw);
                    aq.id(R.id.splash_btn_start).backgroundColor(Color.parseColor(subGradColor));
                }

                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {

                }
                // 画像が設定されているとき
//				if(fileName.length() > 0) {
//					String image = gl.getImgTopBackground() + fileName;
//					aq.id(R.id.imageView3).image(image);
//				}

            }
        } catch (Exception e) {
            Log.v("MAGATAMA", "SplashActivity: setupColorTheme error");
        }

    }

}
