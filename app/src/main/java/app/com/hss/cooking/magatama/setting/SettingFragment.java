package app.com.hss.cooking.magatama.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.api.ServerAPITopBlock;
import app.com.hss.cooking.magatama.stampcard.AdapterChoiceSex;
import app.com.hss.cooking.magatama.stampcard.DialogWarningStamp;
import app.com.hss.cooking.magatama.stampcard.EditTextRegister;
import app.com.hss.cooking.magatama.stampcard.SocialUtils;
import app.com.hss.cooking.magatama.stampcard.TextViewW6;
import app.com.hss.cooking.magatama.utils.AppConstants;
import app.com.hss.cooking.magatama.utils.JsonParserUtils;


public class SettingFragment extends Fragment {

    private Globals gl;
    private AQuery aq;

    private String shopName;
    private String email;
    private boolean registed = false;
    private TextView mTvBirthDay, mTvConfig;
    private EditTextRegister mEdtName;
    private EditTextRegister mEdtFour, mEdtFive, mEdtSix;
    private View mViewOne, mViewTwo, mViewThree, mViewFour, mViewFive,
            mViewSix;
    private TextViewW6 mTvFourTitle, mTvFiveTitle, mTvSixTitle;
    private ProgressDialog progressDialog;
    protected String mSex = "0";
    private Calendar mCalendar;
    private Button mBtnRegister;
    private ToggleButton mBtnEnablePush;
    private Spinner mSpinerSex;
    private String[] mArraySex;
    private Button mBtnHomePage;
    private RelativeLayout mRlHomePage;
    private String mUrl = "";


    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    public SettingFragment() {

    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);

        gl = new Globals();
        gl.initPreference(getActivity());
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_view, container, false);
        aq = new AQuery(view);

        aq.id(R.id.setting_userRegist_label).visibility(View.GONE);
        aq.id(R.id.setting_userRegistLayout).visibility(View.GONE);
        aq.id(R.id.setting_inqueryLayout).visibility(View.GONE);

        setupColorTheme();
        settingInfoAsyncJson();

        aq.id(R.id.setting_userRegistLayout).clickable(true)
                .clicked(this, "userRegistClicked");

        // version
        aq.id(R.id.setting_versionLayout).clickable(true)
                .clicked(this, "versionClicked");

        // お問合せ
        aq.id(R.id.setting_inqueryLayout).clickable(true)
                .clicked(this, "inqueryClicked");

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);

        asyncGetNotify();

        // view stamp-card-new.
        mTvBirthDay = (TextView) view.findViewById(R.id.tv_sub_birth);
        mEdtName = (EditTextRegister) view.findViewById(R.id.edt_name);
        mEdtName.clearFocus();
        // view.
        mViewOne = view.findViewById(R.id.view_name);
        mViewTwo = view.findViewById(R.id.view_sex);
        mViewThree = view.findViewById(R.id.view_birth);
        mViewFour = view.findViewById(R.id.view_four);
        mViewFive = view.findViewById(R.id.view_five);
        mViewSix = view.findViewById(R.id.view_six);
        // Title
        mTvFourTitle = (TextViewW6) view.findViewById(R.id.tv_title_four);
        mTvFiveTitle = (TextViewW6) view.findViewById(R.id.tv_title_five);
        mTvSixTitle = (TextViewW6) view.findViewById(R.id.tv_title_six);
        // content
        mEdtFour = (EditTextRegister) view.findViewById(R.id.edt_name_four);
        mEdtFive = (EditTextRegister) view.findViewById(R.id.edt_name_five);
        mEdtSix = (EditTextRegister) view.findViewById(R.id.edt_name_six);
        // btn register.
        mBtnRegister = (Button) view.findViewById(R.id.btn_ok);
        mBtnEnablePush = (ToggleButton) view
                .findViewById(R.id.toggle_enable_push);
        mTvConfig = (TextView) view.findViewById(R.id.tv_title_config);
        mCalendar = Calendar.getInstance();
        // choice sex.
        mSpinerSex = (Spinner) view.findViewById(R.id.spinner_sex);
        mArraySex = getResources().getStringArray(R.array.sex);

        mBtnHomePage = (Button) view.findViewById(R.id.btn_homepage);
        mRlHomePage = (RelativeLayout) view.findViewById(R.id.companyInfoLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRlHomePage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUrl.length() > 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(mUrl)));
                }
            }
        });
        mBtnEnablePush
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        mBtnEnablePush.setEnabled(false);
                        if (isChecked) {
                            asyncUpdateNotify("1");
                        } else {
                            asyncUpdateNotify("0");
                        }
                    }
                });
        mBtnRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Update info of user.
                if (SocialUtils.isConnected(getActivity())) {
                    registerUser();
                } else {
                    showDialogError(getString(R.string.error_lost_connect));
                }

            }
        });
        mViewThree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String date = (dayOfMonth > 9) ? String
                                        .valueOf(dayOfMonth) : "0"
                                        + String.valueOf(dayOfMonth);
                                String month = (monthOfYear + 1 > 9) ? String
                                        .valueOf(monthOfYear + 1) : "0"
                                        + String.valueOf(monthOfYear + 1);
                                String dateSetter = (new StringBuilder()
                                        .append(year).append("-").append(month)
                                        .append("-").append(date).append(""))
                                        .toString();
                                mTvBirthDay.setText(dateSetter);
                            }
                        }, year, month, day);
                dialog.show();
            }
        });
        // Adapter for spinner.
        ArrayList<String> listSex = new ArrayList<String>();
        for (int i = 0; i < mArraySex.length; i++) {
            listSex.add(mArraySex[i]);
        }
        final AdapterChoiceSex mAdapter = new AdapterChoiceSex(getActivity(),
                listSex);
        mSpinerSex.setAdapter(mAdapter);
        // spinner.
        mSpinerSex.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                mSex = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        getConfigUI();
    }

    /**
     * Get config ui from server to create ui with user design.
     */
    private void getConfigUI() {
        showDialogRegister(getString(R.string.msg_loading));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());
        String url = gl.getUrlConfigRegister();
        aq.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                try {
                    JSONObject objectData = new JSONObject(object);
                    int statusCode = JsonParserUtils.getIntValues(objectData,
                            AppConstants.KEY_PARSER.STATUS, 0);
                    if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                        JSONObject objectResult = objectData
                                .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                        String cliendID = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.CLIENT_ID);
                        String fullNameStatus = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.FULL_NAME_STATUS);
                        String birthday_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.BIRTH_DAY_STATUS);
                        String gender_status = JsonParserUtils.getStringValues(
                                objectResult,
                                AppConstants.KEY_PARSER.GENDER_STATUS);
                        String question_1 = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.QUESTION1);
                        String question_1_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.QUESTION1_STATUS);
                        String question_2 = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.QUESTION2);
                        String question_2_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.QUESTION2_STATUS);
                        String question_3 = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.QUESTION3);
                        String question_3_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.QUESTION3_STATUS);
                        loadDataToUi(fullNameStatus, birthday_status,
                                gender_status, question_1, question_1_status,
                                question_2, question_2_status, question_3,
                                question_3_status);
                        dismissLoading();
                    } else if (statusCode == AppConstants.STATUS_CODE.USER_HAS_REGISTED) {
                        JSONObject objectResult = objectData
                                .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                        String cliendID = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.CLIENT_ID);
                        String fullNameStatus = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.FULL_NAME_STATUS);
                        String birthday_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.BIRTH_DAY_STATUS);
                        String gender_status = JsonParserUtils.getStringValues(
                                objectResult,
                                AppConstants.KEY_PARSER.GENDER_STATUS);
                        String question_1 = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.QUESTION1);
                        String question_1_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.QUESTION1_STATUS);
                        String question_2 = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.QUESTION2);
                        String question_2_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.QUESTION2_STATUS);
                        String question_3 = JsonParserUtils
                                .getStringValues(objectResult,
                                        AppConstants.KEY_PARSER.QUESTION3);
                        String question_3_status = JsonParserUtils
                                .getStringValues(
                                        objectResult,
                                        AppConstants.KEY_PARSER.QUESTION3_STATUS);
                        // get info user.
                        JSONObject objectUser = objectResult
                                .getJSONObject(AppConstants.KEY_PARSER.PROFILE);
                        String fullName = JsonParserUtils.getStringValues(
                                objectUser, AppConstants.KEY_PARSER.FULL_NAME);
                        String birthDay = JsonParserUtils.getStringValues(
                                objectUser, AppConstants.KEY_PARSER.BIRTHDAY);
                        String gender = JsonParserUtils.getStringValues(
                                objectUser, AppConstants.KEY_PARSER.GENDER);
                        String reply_1 = JsonParserUtils.getStringValues(
                                objectUser, AppConstants.KEY_PARSER.REPLY_1);
                        String reply_2 = JsonParserUtils.getStringValues(
                                objectUser, AppConstants.KEY_PARSER.REPLY_2);
                        String reply_3 = JsonParserUtils.getStringValues(
                                objectUser, AppConstants.KEY_PARSER.REPLY_3);
                        loadDataToUi(fullNameStatus, birthday_status,
                                gender_status, question_1, question_1_status,
                                question_2, question_2_status, question_3,
                                question_3_status);
                        loadDataToView(fullName, birthDay, gender, reply_1,
                                reply_2, reply_3);
                        dismissLoading();
                    } else {
                        dismissLoading();
                        showDialogErrorGetInfo(getString(R.string.register_fail));
                    }
                } catch (JSONException e) {
                    dismissLoading();
                    showDialogErrorGetInfo(getString(R.string.register_fail));
                    e.printStackTrace();
                }
            }
        });
    }

    protected void loadDataToView(String fullName, String birthDay,
                                  String gender, String reply_1, String reply_2, String reply_3) {
        if (gender == null)
            gender = "0";
        int index = Integer.valueOf(gender);
        switch (index) {
            case 1:
                mSpinerSex.setSelection(1);
                break;
            case 2:
                mSpinerSex.setSelection(2);
                break;
            default:
                mSpinerSex.setSelection(0);
                break;
        }
        mSex = gender;
        mEdtName.setText(fullName);
        if (birthDay.length() > 0) {
            mTvBirthDay.setText(birthDay);
            try {
                long dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                        Locale.getDefault()).parse(birthDay).getTime();
                mCalendar.setTimeInMillis(dateFormat);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        mEdtFour.setText(reply_1);
        mEdtFive.setText(reply_2);
        mEdtSix.setText(reply_3);
        mBtnRegister.setText(getString(R.string.btn_update_info));
    }

    protected void loadDataToUi(String fullNameStatus, String birthday_status,
                                String gender_status, String question_1, String question_1_status,
                                String question_2, String question_2_status, String question_3,
                                String question_3_status) {
        if (fullNameStatus.equals(AppConstants.CONFIG_VIEW.SHOW)) {
            mViewOne.setVisibility(View.VISIBLE);
        } else {
            mViewOne.setVisibility(View.GONE);
        }

        if (gender_status.equals(AppConstants.CONFIG_VIEW.SHOW)) {
            mViewTwo.setVisibility(View.VISIBLE);
        } else {
            mViewTwo.setVisibility(View.GONE);
        }

        if (birthday_status.equals(AppConstants.CONFIG_VIEW.SHOW)) {
            mViewThree.setVisibility(View.VISIBLE);
        } else {
            mViewThree.setVisibility(View.GONE);
        }

        if (question_1_status.equals(AppConstants.CONFIG_VIEW.SHOW)) {
            mViewFour.setVisibility(View.VISIBLE);
            mTvFourTitle.setText(question_1);
        } else {
            mViewFour.setVisibility(View.GONE);
        }
        if (question_2_status.equals(AppConstants.CONFIG_VIEW.SHOW)) {
            mViewFive.setVisibility(View.VISIBLE);
            mTvFiveTitle.setText(question_2);
        } else {
            mViewFive.setVisibility(View.GONE);
        }
        if (question_3_status.equals(AppConstants.CONFIG_VIEW.SHOW)) {
            mViewSix.setVisibility(View.VISIBLE);
            mTvSixTitle.setText(question_3);
        } else {
            mViewSix.setVisibility(View.GONE);
        }

        // HIROPRO Add user_client_stampが生成されていない場合、登録ボタンを非表示
        int namu_status = Integer.parseInt(fullNameStatus);
        if (namu_status == 0) {
            mBtnRegister.setVisibility(View.GONE);
        }

    }

    public void asyncGetNotify() {
        // 新しいバージョン
        // String url = gl.getUrlSetting() +
        // GCMRegistrar.getRegistrationId(getActivity());
        // aq.ajax(url, JSONArray.class, this, "asyncGetNotifyCallback");

        String url = gl.getUrlSetting();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());

        aq.ajax(url, params, JSONObject.class, this, "asyncGetNotifyCallback");
    }

    public void asyncGetNotifyCallback(String url, JSONObject json,
                                       AjaxStatus status) {
        if (json != null && status.getCode() == 200) {
            try {
                JSONObject obj = json.getJSONObject("setting");
                if (obj.getString("allow_flg").equals("1")) {
                    mBtnEnablePush.setChecked(true);
                } else {
                    mBtnEnablePush.setChecked(false);
                }
                mBtnEnablePush.setEnabled(true);
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "通知設定の取得に失敗しました",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "通知設定の取得に失敗しました", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void asyncUpdateNotify(String flag) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());
        params.put("notif", flag);

        aq.ajax(gl.getUrlSettingNotif(), params, String.class, this,
                "asyncUpdateNotifyCallback");
    }

    public void asyncUpdateNotifyCallback(String url, String json,
                                          AjaxStatus status) {
        Log.v("YOSHITAKA", "SETTING STATUS : " + status.getCode());

        Toast.makeText(getActivity(), "通知設定を更新しました", Toast.LENGTH_SHORT).show();

        mBtnEnablePush.setEnabled(true);
    }

    public void userRegistClicked(View v) {
        Fragment fragment;
        // if(registed) {
        // fragment = new UserInfoFragment();
        // } else {
        // fragment = new UserRegistFragment();
        // }
        if (checkRegisted()) {
            fragment = new UserInfoFragment();
        } else {
            fragment = new UserRegistFragment();
        }
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void versionClicked(View v) {
        Fragment fragment = new VersionInfoFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void inqueryClicked(View v) {
        ServerAPITopBlock apiData = ServerAPITopBlock.getInstance(
                getActivity(), new Globals());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        final String mail = apiData.getMailAddr();

        alertDialogBuilder.setTitle("メールアプリの起動");
        alertDialogBuilder.setMessage("メールアプリを起動しますか？");
        alertDialogBuilder.setPositiveButton("起動",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + mail));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "お問合せ");
                        intent.putExtra(Intent.EXTRA_TEXT, "本文");
                        startActivity(Intent.createChooser(intent, "select"));
                    }
                });
        alertDialogBuilder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.setCancelable(true);
        AlertDialog mailDialog = alertDialogBuilder.create();
        mailDialog.show();
    }

    public void notifyClicked(View v) {
        v.setEnabled(false);
        if (((CheckBox) v).isChecked()) {
            asyncUpdateNotify("1");
        } else {
            asyncUpdateNotify("0");
        }
    }

    public void settingInfoAsyncJson() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("client", gl.getClientID());
        // params.put("parent_id", parent_id);
        // params.put("start", mStart);
        // params.put("end", PAGE_COUNT);

        aq.ajax(gl.getUrlApiShopInfo(), params, JSONObject.class, this,
                "setListView");
    }

    /**
     * @param url
     * @param json
     * @param status
     */
    public void setListView(String url, JSONObject json, AjaxStatus status) {
        Log.v("MAGATAMA", "ShopFragment JSON : " + json);

        if (json != null) {
            try {
                shopName = json.getString("shop_name");
                email = json.getString("email");
                mUrl = json.optString("url", "");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("MAGATAMA", "店舗情報の読み込みでエラー");
            }
            setupShopItem();
        }
    }

    private void setupShopItem() {
        aq.id(R.id.setting_shopName).text(shopName);
        if (shopName == "" || shopName.length() == 0) {
            // aq.id(R.id.companyInfoLayout).visibility(View.GONE);
            aq.id(R.id.setting_shopName).text("未設定");
        }
        if (email == "" || email.length() == 0) {
            aq.id(R.id.setting_inqueryLayout).visibility(View.GONE);
        } else {
            aq.id(R.id.setting_inqueryLayout).visibility(View.VISIBLE);
        }
        mBtnHomePage.setVisibility(TextUtils.isEmpty(mUrl) ? View.GONE : View.VISIBLE);
    }

    private void setupColorTheme() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(
                getActivity(), new Globals());
        String jsonStr = api.getCurrentData();
        Log.e("result setting", jsonStr);
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject bg = jsonObj.getJSONObject("background");
            if (bg != null) {
                String color = "#" + bg.getString("color");
                String subColor = "#" + bg.getString("sub_color");
                // String fileName = "";
                // fileName = bg.getString("file_name");
//				mBtnRegister.setBackgroundColor(Color.parseColor(subColor));
                Log.e("sub color", "values:" + subColor);

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
                    aq.id(R.id.setting_bg).backgroundColor(
                            Color.parseColor(color));
                    aq.id(R.id.btn_ok).backgroundColor(Color.parseColor(subColor));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{
                            Color.parseColor(gradColorStart),
                            Color.parseColor(gradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(
                            Orientation.TOP_BOTTOM, colors);
                    aq.id(R.id.setting_bg).image(bgDraw);
                    aq.id(R.id.btn_ok).backgroundColor(Color.parseColor(subGradColor));
                }
                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {

                }

                // 画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = gl.getImgTopBackground() + fileName;
                    aq.id(R.id.setting_bg).progress(R.id.blank_image)
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

    private boolean checkRegisted() {
        boolean ret = true;
        String userId = gl.getPrefKeyUserID();
        if (userId.equals("")) {
            ret = false;
        }
        return ret;
    }

    // private void requestUserData() {
    // HashMap<String, String> params = new HashMap<String, String>();
    // params.put("client_id", gl.getClientID());
    // params.put("token", gl.getDeviceToken());
    //
    // String url = gl.getUrlApiUserLogin();
    // aq.ajax(url, params, String.class, new AjaxCallback<String>() {
    // @Override
    // public void callback(String url, String result, AjaxStatus status) {
    // Log.v("MAGATAMA", "request user date. url:"+url+" result:"+result);
    // if (status.getCode() != 200) {
    // registed = false;
    // } else {
    // registed = true;
    // }
    // }
    // });
    // }

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
     * Get config of rergister user.
     *
     * @param resId
     */
    private void showDialogErrorGetInfo(String resId) {
        DialogWarningStamp dialog = new DialogWarningStamp(getActivity(), resId);
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                getConfigUI();
            }

            @Override
            public void onCancel() {
                // Back to main.
                // TODO : Need finish.
            }
        });
        dialog.show();
    }

    // Using check validate when update information user.
    private boolean checkValidate() {
        if (mEdtName.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.msg_empty_name),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Dialog error register.
     *
     * @param resId
     */
    private void showDialogError(String resId) {
        DialogWarningStamp dialog = new DialogWarningStamp(getActivity(), resId);
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                registerUser();
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }

    private void registerUser() {
        if (!checkValidate())
            return;
        showDialogRegister("情報を送信しています...");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());
        params.put("fullname", mEdtName.getText().toString().trim());
        params.put("birthday", mTvBirthDay.getText().toString());
        params.put("gender", mSex);
        params.put("reply_1", mEdtFour.getText().toString());
        params.put("reply_2", mEdtFive.getText().toString());
        params.put("reply_3", mEdtSix.getText().toString());
        String url = gl.getUrlRegister();
        aq.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                JSONObject objectData;
                try {
                    objectData = new JSONObject(object);
                    int statusCode = JsonParserUtils.getIntValues(objectData,
                            AppConstants.KEY_PARSER.STATUS, 0);
                    if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                        // request focus.
                        mTvConfig.setFocusable(true);
                        mTvConfig.requestFocus();
                        mTvConfig.requestFocusFromTouch();
                        JSONObject objectResult = objectData
                                .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                        saveDataToDB(gl, objectResult);
                        Toast.makeText(getActivity(),
                                getString(R.string.msg_update_info),
                                Toast.LENGTH_SHORT).show();
                        mBtnRegister
                                .setText(getString(R.string.btn_update_info));
                        dismissLoading();
                    } else {
                        dismissLoading();
                        showDialogError(getString(R.string.register_fail));
                    }
                } catch (JSONException e) {
                    dismissLoading();
                    showDialogError(getString(R.string.register_fail));
                    e.printStackTrace();
                }
            }
        });
    }

    protected void saveDataToDB(Globals globals, JSONObject objectResult) {
        String userId = JsonParserUtils.getStringValues(objectResult,
                AppConstants.KEY_PARSER.USER_ID);
        JSONObject objectProfile;
        try {
            objectProfile = objectResult
                    .getJSONObject(AppConstants.KEY_PARSER.PROFILE);
            String idMemberShip = JsonParserUtils.getStringValues(
                    objectProfile, AppConstants.KEY_PARSER.MEMBER_SHIP_ID);
            globals.setPrefKeyMemberShip(idMemberShip);
            String nameRegister = JsonParserUtils.getStringValues(
                    objectProfile, AppConstants.KEY_PARSER.FULL_NAME);
            globals.saveNameRegister(nameRegister);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
