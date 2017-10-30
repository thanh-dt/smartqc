package app.com.hss.cooking.magatama.stampcard;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.ViewActivity;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.utils.AppConstants;
import app.com.hss.cooking.magatama.utils.JsonParserUtils;


public class FragmentMainStampCard extends BaseFragment {
    private Button mBtnGoHowGetStamp, mBtnGetQRCode;
    private View mBtnLine, mBtnFacebook, mBtnTwitter, mViewShare;
    private TextViewW3 mTvNumberTicket, mTvDateExpired, mTvPolicy,
            mTvNoPromotion;
    private TextView mTvNoUser;
    private HeaderGridView mGrvMain;
    private AdapterMainStamp mAdapter;
    private ArrayList<StampObject> mArrayStamp;
    private ProgressDialog progressDialog;
    private LinearLayout layoutContainerStamp;
    //    private ExpandableHeightListView mListPromotion;
    private AdapterPromotion mAdapterPromotion;
    private ArrayList<PromotionObject> mArrayPromotion;
    private boolean isShowPhone = false;
    private boolean isShowShare = false;
    private int numerStampForPhone = 0;
    private int numberStampForSns = 0;
    private Globals mGlobals;
    private AQuery mAquery;
    private String mContentBarCode = "";
    // facebook.
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    protected String mShopName = "";
    protected String mAddress = "";
    protected String mShopUrl = "";
    protected String mLinkFacebook = "";
    protected String mLinkTwitter = "";
    protected String mLinkLine = "";
    private ScrollView mScrMain, mSrcPolicy;
    private int mColorButton = 0;

    public FragmentMainStampCard() {

    }

    public static FragmentMainStampCard newInstances() {
        FragmentMainStampCard fragment = new FragmentMainStampCard();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_main_stamp_card;
    }

    @Override
    protected void initVariable() {
        mGlobals = getGlobals();
        mAquery = getAquery();
        mBtnGoHowGetStamp = (Button) finViewById(R.id.btn_title_top_one);
        mTvNoUser = (TextView) finViewById(R.id.tv_main_name_user);
        mGrvMain = (HeaderGridView) finViewById(R.id.gv_content_stamp);
        mGrvMain.setExpanded(true);
        mBtnGetQRCode = (Button) finViewById(R.id.btn_get_qr_code);
        mBtnLine = finViewById(R.id.btn_line);
        mBtnFacebook = finViewById(R.id.btn_face_book);
        mBtnTwitter = finViewById(R.id.btn_twitter);
        mArrayStamp = new ArrayList<StampObject>();
        mAdapter = new AdapterMainStamp(getActivity(), mArrayStamp);
        layoutContainerStamp = (LinearLayout) finViewById(R.id.layout_container_stamp);
        mTvNumberTicket = (TextViewW3) finViewById(R.id.tv_stamp_three_title_content);
        mTvDateExpired = (TextViewW3) finViewById(R.id.tv_stamp_two_title_content);
        mTvPolicy = (TextViewW3) finViewById(R.id.tv_stamp_footer_title_content);
        mTvNoPromotion = (TextViewW3) finViewById(R.id.tv_no_promotion);
        mViewShare = finViewById(R.id.ll_share);
        mScrMain = (ScrollView) finViewById(R.id.scroll_main);
        mSrcPolicy = (ScrollView) finViewById(R.id.src_policy);
//        mListPromotion.setExpanded(true);
//        mListPromotion.setFocusable(false);
        mGrvMain.setAdapter(mAdapter);
        // promotions.
        mArrayPromotion = new ArrayList<PromotionObject>();
        mAdapterPromotion = new AdapterPromotion(getActivity(), mArrayPromotion, mGlobals.getSubColor(), mGlobals.getBGColor());
//        mListPromotion.setAdapter(mAdapterPromotion);
        // facebook.
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        mShopName = getString(R.string.app_name);
        mAddress = getString(R.string.app_name);
        mShopUrl = getString(R.string.app_name);
        mLinkFacebook = getString(R.string.app_name);
    }

    private static final int REQUEST_CAMERA = 0;

    @Override
    protected void initControls() {

        mBtnGoHowGetStamp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ViewActivity) getActivity()).goHowToGetStamp(isShowPhone,
                        isShowShare, numerStampForPhone, numberStampForSns);
            }
        });
        // Set No of user.
        mBtnGetQRCode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {
                        Snackbar.make(mScrMain, R.string.permission_camera_rationale,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction("Ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                    }
                                })
                                .show();
                    } else {
                        // Camera permission has not been granted yet. Request it directly.
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA);
                    }
                } else {
                    if (SocialUtils.isConnected(getActivity())) {
                        goGetBarCode();
                    } else {
                        Toast.makeText(getActivity(),
                                getString(R.string.msg_no_connect_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        mBtnLine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SocialUtils.isConnected(getActivity())) {
                    shareViaLine();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.msg_no_connect_internet),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        mBtnFacebook.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SocialUtils.isConnected(getActivity())) {
                    shareFacebookViaSdk();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.msg_no_connect_internet),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnTwitter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                shareViaTwitter();
            }
        });
        mAdapterPromotion.setListener(new AdapterPromotion.onUsingPromot() {

            @Override
            public void onRequest(final PromotionObject objectPromot) {
                if (SocialUtils.isConnected(getActivity())) {
                    DialogUsingStamp mDialogUsingStamp = new DialogUsingStamp(
                            getActivity(), objectPromot.getTitlePromot());
                    mDialogUsingStamp.setListenner(new DialogUsingStamp.onPickDialog() {

                        @Override
                        public void onOk() {
                            showDialogRegister(getString(R.string.msg_using_promot));
                            Globals globals = getGlobals();
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("client_id", globals.getClientID());
                            params.put("token", globals.getDeviceToken());
                            params.put("stamp_id", objectPromot.getId());
                            String url = globals.getUrlUsingPromot();
                            // time out.
                            AjaxCallback<String> callBack = new AjaxCallback<String>() {
                                @Override
                                public void callback(String url, String object,
                                                     AjaxStatus status) {
                                    if (object == null) {
                                        dismissLoading();
                                        showDialogErrUsingPromot(objectPromot);
                                    } else {
                                        JSONObject objectData;
                                        try {
                                            objectData = new JSONObject(object);
                                            int statusCode = JsonParserUtils
                                                    .getIntValues(
                                                            objectData,
                                                            AppConstants.KEY_PARSER.STATUS,
                                                            0);
                                            if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                                                getInfoInBackGround();
                                            } else {
                                                dismissLoading();
                                                showDialogErrUsingPromot(objectPromot);
                                            }
                                        } catch (Exception ex) {
                                            dismissLoading();
                                            showDialogErrUsingPromot(objectPromot);
                                        }
                                    }
                                }
                            };
                            callBack.timeout(AppConstants.TIME_OUT_REQUEST);
                            // end
                            getAquery().ajax(url, params, String.class,
                                    callBack);
                        }
                    });
                    mDialogUsingStamp.show();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.msg_no_connect_internet),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mScrMain.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSrcPolicy.getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        mSrcPolicy.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        getStampInfo();
        setupColorTheme();
        // show or hidden action bar.
        // show back button on action bar.
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);

        // facebook.
        shareDialog.registerCallback(callbackManager,
                new FacebookCallback<Sharer.Result>() {

                    @Override
                    public void onSuccess(Result result) {
                        if (result.getPostId() != null) {
                            Toast.makeText(
                                    getActivity(),
                                    getString(R.string.msg_share_facebook_success),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getActivity(),
                                getString(R.string.msg_share_fail),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }

                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (SocialUtils.isConnected(getActivity())) {
                    goGetBarCode();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.msg_no_connect_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(mScrMain, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showDialogErrUsingPromot(final PromotionObject objectPromot) {
        DialogWarningStamp dialogError = new DialogWarningStamp(getActivity(),
                getString(R.string.error_using_promot));
        dialogError.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                showDialogRegister(getString(R.string.msg_using_promot));
                Globals globals = getGlobals();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("client_id", globals.getClientID());
                params.put("token", globals.getDeviceToken());
                params.put("stamp_id", objectPromot.getId());
                String url = globals.getUrlUsingPromot();
                // time out.
                AjaxCallback<String> callBack = new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object,
                                         AjaxStatus status) {
                        if (object == null) {
                            dismissLoading();
                            showDialogErrUsingPromot(objectPromot);
                        } else {
                            JSONObject objectData;
                            try {
                                objectData = new JSONObject(object);
                                int statusCode = JsonParserUtils.getIntValues(
                                        objectData,
                                        AppConstants.KEY_PARSER.STATUS, 0);
                                if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                                    getInfoInBackGround();
                                } else {
                                    dismissLoading();
                                    showDialogErrUsingPromot(objectPromot);
                                }
                            } catch (Exception ex) {
                                dismissLoading();
                                showDialogErrUsingPromot(objectPromot);
                            }
                        }
                    }
                };
                callBack.timeout(AppConstants.TIME_OUT_REQUEST);
                // end
                getAquery().ajax(url, params, String.class, callBack);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub

            }
        });
        dialogError.show();
    }

    private void getStampInfo() {
        Globals globals = getGlobals();
        showDialogRegister(getString(R.string.msg_loading));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", globals.getClientID());
        params.put("token", globals.getDeviceToken());
        String url = globals.getUrlContentStamp();
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("result", "value:" + object);
                if (object == null) {
                    dismissLoading();
                    showDialogErrorGetInfo(getString(R.string.msg_error_get_info_card));
                } else {
                    updateDataUoUi(object);
                }
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        getAquery().ajax(url, params, String.class, callBack);
    }

    private void getInfoInBackGround() {
        Globals globals = getGlobals();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", globals.getClientID());
        params.put("token", globals.getDeviceToken());
        String url = globals.getUrlContentStamp();
        // time out.
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("result", "value:" + object);
                if (object == null) {
                    dismissLoading();
                    showDialogErrorGetInfo(getString(R.string.msg_error_get_info_card));
                } else {
                    updateDataUoUi(object);
                }
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        // end
        getAquery().ajax(url, params, String.class, callBack);
    }

    protected void updateDataUoUi(String object) {
        mArrayPromotion.clear();
        mArrayStamp.clear();
        JSONObject objectData;
        try {
            objectData = new JSONObject(object);
            int statusCode = JsonParserUtils.getIntValues(objectData,
                    AppConstants.KEY_PARSER.STATUS, 0);
            if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                JSONObject objectResult = objectData
                        .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                String memberShip = JsonParserUtils.getStringValues(
                        objectResult, AppConstants.KEY_PARSER.MEMBER_SHIP_ID);
                mTvNoUser.setText(getString(R.string.stamp_name_number,
                        memberShip));
                String policy = JsonParserUtils.getStringValues(objectResult,
                        AppConstants.KEY_PARSER.POLICY);
                if (policy.length() > 0) {
                    mTvPolicy.setText(policy);
                }
                String typeIcon = JsonParserUtils.getStringValues(objectResult,
                        AppConstants.KEY_PARSER.TYPE_ICON);
                String dateExpired = JsonParserUtils.getStringValues(
                        objectResult, AppConstants.KEY_PARSER.DATE_DEADLINE);
                //if (dateExpired.length() > 0) {
                //mTvDateExpired.setText(dateExpired);
                //}
                // HIROPRO Add 暫定 有効期限の設定がされてなく保存される値が1970-01-01 09:00:00の固定前提
                if (dateExpired.length() > 0 && !dateExpired.equals("0000-00-00 00:00:00")) {
                    mTvDateExpired.setText(dateExpired);
                }
                String sumTicket = JsonParserUtils.getStringValues(
                        objectResult, AppConstants.KEY_PARSER.TICKET_QUANTITY);
                setTotalNumberTicket(sumTicket);
                // GET HOW TO GET STAMP.
                JSONObject objectGetStamp = objectResult
                        .getJSONObject(AppConstants.KEY_PARSER.HOW_TO_GET_STAMP);
                JSONObject objectAppGift = objectGetStamp
                        .getJSONObject(AppConstants.KEY_PARSER.APP_GIFT);
                String statusGift = JsonParserUtils.getStringValues(
                        objectAppGift, AppConstants.KEY_PARSER.STATUS);
                String nuberStampForGift = JsonParserUtils.getStringValues(
                        objectAppGift, AppConstants.KEY_PARSER.QUANTITY);
                numerStampForPhone = Integer.valueOf(nuberStampForGift);
                isShowPhone = statusGift.equals("1") ? true : false;
                JSONObject objectSnS = objectGetStamp
                        .getJSONObject(AppConstants.KEY_PARSER.SNS_GIFT);
                String nuberStampForSns = JsonParserUtils.getStringValues(
                        objectSnS, AppConstants.KEY_PARSER.QUANTITY);
                if (nuberStampForSns.equals("null"))
                    nuberStampForSns = "0";
                numberStampForSns = Integer.valueOf(nuberStampForSns);
                String statusSns = JsonParserUtils.getStringValues(objectSnS,
                        AppConstants.KEY_PARSER.STATUS);
                isShowShare = statusSns.equals("1") ? true : false;
                showViewShare(isShowShare);
                // GET PROMOTION.
                JSONArray arrayPromotion = objectResult
                        .getJSONArray(AppConstants.KEY_PARSER.PROMOTION_INFO);
                if (arrayPromotion.length() > 0) {
                    for (int i = 0; i < arrayPromotion.length(); i++) {
                        JSONObject objectPromotion = arrayPromotion
                                .getJSONObject(i);
                        String nameUrl = JsonParserUtils.getStringValues(
                                objectPromotion,
                                AppConstants.KEY_PARSER.FILE_NAME);
                        String title = JsonParserUtils.getStringValues(
                                objectPromotion, AppConstants.KEY_PARSER.TITLE);
                        String description = JsonParserUtils.getStringValues(
                                objectPromotion,
                                AppConstants.KEY_PARSER.DESCRIPTION);
                        String id = JsonParserUtils.getStringValues(
                                objectPromotion, AppConstants.KEY_PARSER.ID);
                        String numberStamp = JsonParserUtils.getStringValues(
                                objectPromotion,
                                AppConstants.KEY_PARSER.TICKET_QUANTITY);
                        String statusPromotion = JsonParserUtils
                                .getStringValues(objectPromotion,
                                        AppConstants.KEY_PARSER.STATUS);
                        int paymentStatus = JsonParserUtils.getIntValues(
                                objectPromotion,
                                AppConstants.KEY_PARSER.PAYMENT_STATUS, 0);
                        PromotionObject objPromotion = new PromotionObject();
                        objPromotion.setId(id);
                        objPromotion.setStampNeed(numberStamp);
                        objPromotion.setContentPromot(description);
                        objPromotion.setTitlePromot(title);
                        objPromotion.setNumberStampHave(sumTicket);
                        objPromotion.setStatusPromot(paymentStatus);
                        objPromotion.setUrlThumb(mGlobals.getUrlImageBase()
                                + mGlobals.getClientID() + "/" + nameUrl);
                        mArrayPromotion.add(objPromotion);
                    }
                    mAdapterPromotion.notifyDataSetChanged();
                    layoutContainerStamp.removeAllViews();
                    for (int i = 0; i < mAdapterPromotion.getCount(); i++) {
                        View view = mAdapterPromotion.getView(i, null, null);
                        layoutContainerStamp.addView(view);
                    }
                } else {
                    mTvNoPromotion.setVisibility(View.VISIBLE);
                }

                // Get Stamp.
                JSONArray arrayStamp = objectResult
                        .getJSONArray(AppConstants.KEY_PARSER.STAMP_MEMBER_INFO);
                if (arrayStamp.length() > 0) {
                    for (int i = 0; i < arrayStamp.length(); i++) {
                        JSONObject objectStamp = arrayStamp.getJSONObject(i);
                        String date = JsonParserUtils.getStringValues(
                                objectStamp, AppConstants.KEY_PARSER.DATE);
                        StampObject objectStp = new StampObject();
                        objectStp.setId(String.valueOf(i + 1));
                        objectStp.setDate(date);
                        objectStp.setStyleStamp(Integer.valueOf(typeIcon));
                        mArrayStamp.add(objectStp);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                dismissLoading();
                // TODO : get content share.
                getContentShare();
            } else {
                dismissLoading();
                showDialogErrorGetInfo(getString(R.string.msg_error_get_info_card));
            }
        } catch (JSONException e) {
            dismissLoading();
            showDialogErrorGetInfo(getString(R.string.msg_error_get_info_card));
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        callbackManager.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            // Handle scan intent
            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                String contents = intent.getStringExtra("SCAN_RESULT");
                String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
                byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                int intentOrientation = intent.getIntExtra(
                        "SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                Integer orientation = (intentOrientation == Integer.MIN_VALUE) ? null
                        : intentOrientation;
                String errorCorrectionLevel = intent
                        .getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");

                Log.e("bar code", contents);
                // Update to server.
                if (contents.length() > 0) {
                    updateNumberStamp(contents);
                    mContentBarCode = contents;
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        } else {
            // Handle other intents
        }

    }

    private void updateNumberStamp(String numberStamp) {
        showDialogRegister(getString(R.string.msg_update_info));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", mGlobals.getClientID());
        params.put("token", mGlobals.getDeviceToken());
        params.put("qrcode", numberStamp);
        String url = mGlobals.getUrlUpdateColectionStamp();
        // time out.
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object == null) {
                    dismissLoading();
                    showDialogErrorUpdateStampWithBarCode();
                } else {
                    Log.e("result", "update code:" + object);
                    JSONObject objectResult;
                    try {
                        objectResult = new JSONObject(object);
                        int statusCode = JsonParserUtils
                                .getIntValues(objectResult,
                                        AppConstants.KEY_PARSER.STATUS, 0);
                        if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                            getInfoInBackGround();
                        } else if (statusCode == AppConstants.STATUS_CODE.QR_CODE_OF_OTHER_STORE) {
                            dismissLoading();
                            // This is qr-code of other store.
                            String msg = getString(R.string.msg_qr_code_of_other_store);
                            DialogShowShortContent dialog = new DialogShowShortContent(
                                    getActivity(), msg, mColorButton);
                            dialog.show();
                        } else {
                            dismissLoading();
                            showDialogErrorUpdateStampWithBarCode();
                        }
                    } catch (JSONException e) {
                        dismissLoading();
                        showDialogErrorUpdateStampWithBarCode();
                        e.printStackTrace();
                    }
                }
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        // end
        getAquery().ajax(url, params, String.class, callBack);
    }

    private void setupColorTheme() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(
                getActivity(), mGlobals);
        String jsonStr = api.getCurrentData();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject bg = jsonObj.getJSONObject("background");
            if (bg != null) {
                String color = "#" + bg.getString("color");
                String subColor = "#" + bg.getString("sub_color");
                int colorButton = Color.parseColor(color);
                mColorButton = colorButton;
                // String fileName = "";
                // fileName = bg.getString("file_name");
                String type = bg.getString("type");
                String gradColorStart = "#" + bg.getString("gradation1");
                String gradColorEnd = "#" + bg.getString("gradation2");
                // String barGradColorStart = "#" +
                // bg.getString("bar_gradation1");
                // String barGradColorEnd = "#" +
                // bg.getString("bar_gradation2");
                String subGradColor = "#" + bg.getString("sub_gradation");

                // 背景設定
                if (type.equals("1")) {
                    // 単一カラー
                    mAquery.id(R.id.setting_bg).backgroundColor(
                            Color.parseColor(color));
                    changeSubColorToView(subColor);
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{
                            Color.parseColor(gradColorStart),
                            Color.parseColor(gradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(
                            Orientation.TOP_BOTTOM, colors);
                    mAquery.id(R.id.setting_bg).image(bgDraw);
                    changeToGradiant(subGradColor);
                }
                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {

                }

                // 画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = mGlobals.getImgTopBackground() + fileName;
                    mAquery.id(R.id.setting_bg).progress(R.id.blank_image)
                            .image(image);
                }

                // aq.id(R.id.notifyLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.userInfoLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.companyInfoLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.setting_versionLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.setting_inqueryLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.settings_version_detail_btn).backgroundColor(Color.parseColor(subColor));
            }

        } catch (Exception e) {
            Log.v("MAGATAMA", "SettingFragment: setupColorTheme error");
        }
    }

    private void goGetBarCode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    /**
     * Share status via facebook app on device. if not install will send with
     * web.
     */
    private void shareViaFaceBook() {
        boolean isSendFacebook = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Content to share");
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
                0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                isSendFacebook = true;
                break;
            }
        }

        if (isSendFacebook) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Text");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            intent.putExtra(Intent.EXTRA_TITLE, "what the fuc");
            intent.setPackage("com.facebook.katana");
            startActivity(intent);
        } else {
            String urlToShare = "https://www.google.com";
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
                    + urlToShare;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }

    private void shareFacebookViaSdk() {
        String titleShare = getString(R.string.share_title_sns, mShopName);
        String contentMsg = mAddress + ", " + mShopUrl + "," + mLinkFacebook;
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            if (mShopUrl.length() > 0) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(titleShare)
                        .setContentDescription(
                                Html.fromHtml(contentMsg).toString())
                        .setContentUrl(Uri.parse(mShopUrl)).build();
                shareDialog.show(linkContent);
            } else {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(titleShare)
                        .setContentDescription(
                                Html.fromHtml(contentMsg).toString()).build();
                shareDialog.show(linkContent);
            }

        }
        // ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
        // .putString("og:type", "books.book")
        // .putString("og:title", "A Game of Thrones")
        // .putString("og:description", contentMsg)
        // .putString("books:isbn", "0-553-57340-3")
        // .build();
        // ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
        // .setActionType("books.reads")
        // .putObject("book", object)
        // .build();
        // ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
        // .setPreviewPropertyName("book")
        // .setAction(action)
        // .build();
        // ShareDialog.show(FragmentMainStampCard.this, content);
    }

    private void shareViaTwitter() {
        boolean isShowapp = false;
        String titleShare = getString(R.string.share_title_sns, mShopName);
        String contentMsg = mAddress + "\n " + mShopUrl + "\n " + mLinkTwitter;
        Log.e("share twitter", "content :" + contentMsg);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String message = titleShare + "\n " + contentMsg;
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Stamp Card Share");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
                0);
        for (final ResolveInfo app : activityList) {
            if (app.activityInfo.name.contains("com.twitter.android")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(
                        activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                isShowapp = true;
                break;
            } else {

            }
        }

        if (isShowapp) {
            getActivity().startActivity(shareIntent);
        } else {
            Toast.makeText(getActivity(),
                    getString(R.string.msg_error_need_download_twitter),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void shareViaLine() {
        boolean isShowLineApp = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String titleShare = getString(R.string.share_title_sns, mShopName);
        String contentMsg = mAddress + "\n " + mShopUrl + "\n " + mLinkLine;
        Log.e("share twitter", "content :" + contentMsg);
        String message = titleShare + "\n " + contentMsg;
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
                0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("line.android")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(
                        activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                isShowLineApp = true;
                break;
            }
        }

        if (isShowLineApp) {
            getActivity().startActivity(shareIntent);
        } else {
            Toast.makeText(getActivity(),
                    getActivity().getString(R.string.msg_no_line_app),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogRegister(String msgLoading) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(msgLoading);
        progressDialog.setCancelable(false);
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Set total number ticket.
     *
     * @param number
     */
    private void setTotalNumberTicket(String number) {
        mTvNumberTicket.setText(getString(R.string.stamp_main_content_two,
                number));
    }

    // Dialog error when get data from server. With conent message.
    private void showDialogErrorGetInfo(String resId) {
        DialogWarningStamp dialog = new DialogWarningStamp(getActivity(), resId);
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                getStampInfo();
            }

            @Override
            public void onCancel() {
                backToMain();
            }
        });
        dialog.show();
    }

    // Show dialog error update stamp with bar-code.
    private void showDialogErrorUpdateStampWithBarCode() {
        DialogWarningStamp dialog = new DialogWarningStamp(getActivity(),
                getString(R.string.msg_some_thing_wrong_try_again));
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                updateNumberStamp(mContentBarCode);
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }

    /**
     * Show and hidden view share.
     *
     * @param isShow
     */
    private void showViewShare(boolean isShow) {
        if (isShow) {
            mViewShare.setVisibility(View.VISIBLE);
        } else {
            mViewShare.setVisibility(View.GONE);
        }
    }

    private void getContentShare() {
        // showDialogRegister(getString(R.string.msg_loading));
        String url = mGlobals.getUrlApiShopInfo();
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object == null) {
                    dismissLoading();
                } else {
                    Log.e("config share", "values :" + object);
                    try {
                        JSONObject objectData = new JSONObject(object);
                        mShopName = JsonParserUtils.getStringValues(objectData,
                                AppConstants.KEY_PARSER.SHOP_NAME);
                        mShopUrl = JsonParserUtils.getStringValues(objectData,
                                AppConstants.KEY_PARSER.URL);
                        String pref = JsonParserUtils.getStringValues(objectData,
                                AppConstants.KEY_PARSER.PREF);
                        String city = JsonParserUtils.getStringValues(objectData,
                                AppConstants.KEY_PARSER.CITY);
                        String address_opt1 = JsonParserUtils.getStringValues(
                                objectData, AppConstants.KEY_PARSER.ADDRESS_OPT1);
                        String address_opt2 = JsonParserUtils.getStringValues(
                                objectData, AppConstants.KEY_PARSER.ADDRESS_OPT2);
                        JSONArray objectSNS = objectData
                                .getJSONArray(AppConstants.KEY_PARSER.SNS);
                        if (objectSNS.length() > 0) {
                            for (int i = 0; i < objectSNS.length(); i++) {
                                JSONObject objectItem = objectSNS.getJSONObject(i);
                                String sns_id = JsonParserUtils.getStringValues(
                                        objectItem, AppConstants.KEY_PARSER.SNS_ID);
                                if (sns_id
                                        .equalsIgnoreCase(AppConstants.SNS_VALUE.SNS_ID_FACEBOOK)) {
                                    String valueLinkFacebook = JsonParserUtils
                                            .getStringValues(objectItem,
                                                    AppConstants.KEY_PARSER.VALUE);
                                    mLinkFacebook = valueLinkFacebook;
                                } else if (sns_id
                                        .equalsIgnoreCase(AppConstants.SNS_VALUE.SNS_ID_TWITTER)) {
                                    String valueLinkTwitter = JsonParserUtils
                                            .getStringValues(objectItem,
                                                    AppConstants.KEY_PARSER.VALUE);
                                    mLinkTwitter = valueLinkTwitter;
                                } else if (sns_id
                                        .equalsIgnoreCase(AppConstants.SNS_VALUE.SNS_ID_LINE)) {
                                    String valueLinkLine = JsonParserUtils
                                            .getStringValues(objectItem,
                                                    AppConstants.KEY_PARSER.VALUE);
                                    mLinkLine = valueLinkLine;
                                }
                            }
                        }

                        mAddress = "[" + pref + "] " + city + " " + address_opt1
                                + " " + address_opt2;
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    dismissLoading();
                }
            }

            @Override
            public void failure(int code, String message) {
                // TODO Auto-generated method stub
                super.failure(code, message);
                dismissLoading();
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        mAquery.ajax(url, null, String.class, callBack);
    }

    private void backToMain() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        ((ViewActivity) getActivity()).goMainIpost();
    }

    private void changeSubColorToView(String sub_color) {
        int color = Color.parseColor(sub_color);
        mBtnGoHowGetStamp.setBackgroundColor(color);
        mBtnGetQRCode.setBackgroundColor(color);
        mBtnLine.setBackgroundColor(color);
        mBtnFacebook.setBackgroundColor(color);
        mBtnTwitter.setBackgroundColor(color);
    }

    private void changeToGradiant(String colorGradiant) {
        int color = Color.parseColor(colorGradiant);
        mBtnGoHowGetStamp.setBackgroundColor(color);
        mBtnGetQRCode.setBackgroundColor(color);
        mBtnLine.setBackgroundColor(color);
        mBtnFacebook.setBackgroundColor(color);
        mBtnTwitter.setBackgroundColor(color);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
