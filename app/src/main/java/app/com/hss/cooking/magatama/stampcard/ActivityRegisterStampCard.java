package app.com.hss.cooking.magatama.stampcard;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.ViewActivity;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.utils.AppConstants;
import app.com.hss.cooking.magatama.utils.JsonParserUtils;


public class ActivityRegisterStampCard extends BaseActivity {
    private TextView mTvBirthDay;
    private EditTextRegister mEdtName;
    private Button mBtnOk, mBtnCancel;
    private View mViewOne, mViewTwo, mViewThree, mViewFour, mViewFive,
            mViewSix;
    private TextViewW6 mTvFourTitle, mTvFiveTitle, mTvSixTitle;
    private EditTextRegister mEdtFour, mEdtFive, mEdtSix;
    private String mSex = "0";
    private ProgressDialog progressDialog;
    private Globals mGlobals;
    private AQuery mQuery;
    private Spinner mSpinerSex;
    private String[] mArraySex;
    private Globals gl;
    private AQuery mAquery;
    private int mColorButton = 0;
    private View mViewMain;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_register_stamp;
    }

    @Override
    protected void initVariable() {
        mAquery = new AQuery(this);
        gl = new Globals();
        gl.initPreference(this);

        mEdtName = (EditTextRegister) findViewById(R.id.edt_name);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mTvBirthDay = (TextView) findViewById(R.id.tv_sub_birth);
        // View
        mViewOne = findViewById(R.id.view_name);
        mViewTwo = findViewById(R.id.view_sex);
        mViewThree = findViewById(R.id.view_birth);
        mViewFour = findViewById(R.id.view_four);
        mViewFive = findViewById(R.id.view_five);
        mViewSix = findViewById(R.id.view_six);
        // Title
        mTvFourTitle = (TextViewW6) findViewById(R.id.tv_title_four);
        mTvFiveTitle = (TextViewW6) findViewById(R.id.tv_title_five);
        mTvSixTitle = (TextViewW6) findViewById(R.id.tv_title_six);
        // content
        mEdtFour = (EditTextRegister) findViewById(R.id.edt_name_four);
        mEdtFive = (EditTextRegister) findViewById(R.id.edt_name_five);
        mEdtSix = (EditTextRegister) findViewById(R.id.edt_name_six);
        // choice sex.
        mSpinerSex = (Spinner) findViewById(R.id.spinner_sex);
        mArraySex = getResources().getStringArray(R.array.sex);
        mGlobals = getGlobalConnect();
        mQuery = getQueryconnect();
        mViewMain = findViewById(R.id.ll_main);
    }

    @Override
    protected void initControls() {
        setupColorTheme();
        mViewThree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityRegisterStampCard.this,
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

        mBtnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SocialUtils.isConnected(getApplicationContext())) {
                    registerUser();
                } else {
                    showDialogError(getString(R.string.error_lost_connect));
                }
            }
        });

        mBtnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (SocialUtils.isConnected(getApplicationContext())) {
//                    skipRegister();
//                } else {
//                    showDialogError(getString(R.string.error_lost_connect));
//                }
                goMainScreen();
            }
        });
        // Adapter for spinner.
        ArrayList<String> listSex = new ArrayList<String>();
        for (int i = 0; i < mArraySex.length; i++) {
            listSex.add(mArraySex[i]);
        }
        final AdapterChoiceSex mAdapter = new AdapterChoiceSex(
                ActivityRegisterStampCard.this, listSex);
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
        params.put("client_id", mGlobals.getClientID());
        params.put("token", mGlobals.getDeviceToken());
        String url = mGlobals.getUrlConfigRegister();
        // time out.
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object == null) {
                    dismissLoading();
                    showDialogErrorGetInfo(getString(R.string.register_fail));
                } else {
                    try {
                        JSONObject objectData = new JSONObject(object);
                        int statusCode = JsonParserUtils.getIntValues(
                                objectData, AppConstants.KEY_PARSER.STATUS, 0);
                        if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                            JSONObject objectResult = objectData
                                    .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                            String cliendID = JsonParserUtils.getStringValues(
                                    objectResult,
                                    AppConstants.KEY_PARSER.CLIENT_ID);
                            String fullNameStatus = JsonParserUtils
                                    .getStringValues(
                                            objectResult,
                                            AppConstants.KEY_PARSER.FULL_NAME_STATUS);
                            String birthday_status = JsonParserUtils
                                    .getStringValues(
                                            objectResult,
                                            AppConstants.KEY_PARSER.BIRTH_DAY_STATUS);
                            String gender_status = JsonParserUtils
                                    .getStringValues(
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
                            int valuesRegister = objectResult.optInt(AppConstants.KEY_PARSER.MEMBER_CARD_STATUS, 1);
                            loadDataToUi(fullNameStatus, birthday_status,
                                    gender_status, question_1,
                                    question_1_status, question_2,
                                    question_2_status, question_3,
                                    question_3_status,valuesRegister);
                            dismissLoading();
                        } else if (statusCode == AppConstants.STATUS_CODE.USER_HAS_REGISTED) {
                            JSONObject objectResult = objectData
                                    .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                            saveDataToDB(mGlobals, objectResult);
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.user_has_registed),
                                    Toast.LENGTH_SHORT).show();
                            goMainScreen();
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
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        // end.
        mQuery.ajax(url, params, String.class, callBack);
    }

    protected void loadDataToUi(String fullNameStatus, String birthday_status,
                                String gender_status, String question_1, String question_1_status,
                                String question_2, String question_2_status, String question_3,
                                String question_3_status,int valuesRegister) {
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
        if (valuesRegister == 0)
            skipRegister();
    }

    /**
     * Dialog error register.
     *
     * @param resId
     */
    private void showDialogError(String resId) {
        DialogWarningStamp dialog = new DialogWarningStamp(
                ActivityRegisterStampCard.this, resId);
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                registerUser();
            }

            @Override
            public void onCancel() {
                Intent iGoMain = new Intent(ActivityRegisterStampCard.this,
                        ViewActivity.class);
                startActivity(iGoMain);
                finish();
            }
        });
        dialog.show();
    }

    /**
     * Get config of rergister user.
     *
     * @param resId
     */
    private void showDialogErrorGetInfo(String resId) {
        DialogWarningStamp dialog = new DialogWarningStamp(
                ActivityRegisterStampCard.this, resId);
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                getConfigUI();
            }

            @Override
            public void onCancel() {
                Intent iGoMain = new Intent(ActivityRegisterStampCard.this,
                        ViewActivity.class);
                startActivity(iGoMain);
                finish();
            }
        });
        dialog.show();
    }

    private void registerUser() {
        if (!checkValidate())
            return;
        final Globals globals = getGlobalConnect();
        showDialogRegister("情報を送信しています...");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", globals.getClientID());
        params.put("token", globals.getDeviceToken());
        params.put("fullname", mEdtName.getText().toString().trim());
        params.put("birthday", mTvBirthDay.getText().toString());
        params.put("gender", mSex);
        params.put("reply_1", mEdtFour.getText().toString());
        params.put("reply_2", mEdtFive.getText().toString());
        params.put("reply_3", mEdtSix.getText().toString());
        String url = globals.getUrlRegister();
        // time out.
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object == null) {
                    dismissLoading();
                    showDialogError(getString(R.string.register_fail));
                } else {
                    JSONObject objectData;
                    try {
                        objectData = new JSONObject(object);
                        int statusCode = JsonParserUtils.getIntValues(
                                objectData, AppConstants.KEY_PARSER.STATUS, 0);
                        Log.e("data register", "values:" + object);
                        if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {

                            JSONObject objectResult = objectData
                                    .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                            saveDataToDB(globals, objectResult);
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.msg_success_register),
                                    Toast.LENGTH_SHORT).show();
                            // Call to api daily add stamp.
                            addDailyStamp();
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
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        // end.
        getQueryconnect().ajax(url, params, String.class, callBack);
    }


    private void setupColorTheme() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(
                ActivityRegisterStampCard.this, mGlobals);
        String jsonStr = api.getCurrentData();
        Log.e("color", "values:" + jsonStr);
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject bg = jsonObj.getJSONObject("background");
            JSONObject header = jsonObj.getJSONObject("header");
            if (bg != null) {
                String color = "#" + bg.getString("color");
                String subColor = "#" + bg.getString("sub_color");
                Log.e("sub color", "values:" + subColor);
                int colorButton = Color.parseColor(subColor);
                mColorButton = Color.parseColor(color);
                gl.saveBGColor(color);
                // mImvButton.setColorFilter(colorButton);

                // end
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
                String barGradColorStart = "#"
                        + header.getString("bar_gradation1");
                String barGradColorEnd = "#"
                        + header.getString("bar_gradation2");

                // 背景設定
                if (type.equals("1")) {
                    // 単一カラー
                    gl.saveSubColor(subColor);
                    mBtnOk.setBackgroundColor(colorButton);
                    mBtnCancel.setBackgroundColor(colorButton);
                    mAquery.id(R.id.header_bg).backgroundColor(
                            Color.parseColor(color));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{
                            Color.parseColor(barGradColorStart),
                            Color.parseColor(barGradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(
                            Orientation.BOTTOM_TOP, colors);
                    mBtnOk.setBackgroundColor(Color.parseColor(subGradColor));
                    mBtnCancel.setBackgroundColor(Color.parseColor(subGradColor));
                    gl.saveSubColor(subGradColor);
                    mAquery.id(R.id.header_bg).image(bgDraw);
                }
                // 背景設定
                if (type.equals("1")) {
                    // 単一カラー
                    mAquery.id(R.id.setting_bg).backgroundColor(
                            Color.parseColor(color));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{
                            Color.parseColor(gradColorStart),
                            Color.parseColor(gradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(
                            Orientation.TOP_BOTTOM, colors);
                    mAquery.id(R.id.setting_bg).image(bgDraw);
                }
                String fileName = "";
                try {
                    fileName = header.getString("file_name");
                } catch (Exception e) {

                }

                // 画像が設定されているとき
                if (fileName.length() > 0) {
//					String image = mGlobals.getImgTopBackground() + fileName;
                    String image = gl.getImgHeader() + fileName;
                    mAquery.id(R.id.titlebar_layout).visibility(View.INVISIBLE);
                    mAquery.id(R.id.header_bg).progress(R.id.blank_image)
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

    private void skipRegister() {
        showDialogRegister(getString(R.string.msg_loading));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", mGlobals.getClientID());
        params.put("token", mGlobals.getDeviceToken());
        params.put("fullname", "");
        params.put("birthday", "");
        params.put("gender", "0");
        params.put("reply_1", "");
        params.put("reply_2", "");
        params.put("reply_3", "");
        String url = mGlobals.getUrlRegister();
        // time out.
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object == null) {
                    dismissLoading();
                    showDialogError(getString(R.string.register_fail));
                } else {
                    JSONObject objectData;
                    try {
                        objectData = new JSONObject(object);
                        int statusCode = JsonParserUtils.getIntValues(
                                objectData, AppConstants.KEY_PARSER.STATUS, 0);
                        if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {

                            JSONObject objectResult = objectData
                                    .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                            saveDataToDB(mGlobals, objectResult);
                            // Call to api daily add stamp.
//                            addDailyStamp();
                            goMainScreen();
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
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        // end.
        getQueryconnect().ajax(url, params, String.class, callBack);
    }

    protected void addDailyStamp() {
        String url = mGlobals.getUrlAddDailyStamp();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", mGlobals.getClientID());
        params.put("token", mGlobals.getDeviceToken());
        // time out.
        AjaxCallback<String> callBack = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                if (object == null) {
                    goMainScreen();
                } else {
                    try {
                        JSONObject objectResult = new JSONObject(object);
                        int statusCode = JsonParserUtils.getIntValues(objectResult,
                                AppConstants.KEY_PARSER.STATUS, 0);
                        if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                            // show popup to user.
                            JSONObject objectData = objectResult
                                    .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                            String numberTicketAdd = JsonParserUtils
                                    .getStringValues(objectData,
                                            AppConstants.KEY_PARSER.STAMP_QUANTITY);
                            String content = getString(
                                    R.string.msg_add_stamp_daily, numberTicketAdd);
                            DialogShowShortContent dialog = new DialogShowShortContent(
                                    ActivityRegisterStampCard.this, content, mColorButton);
                            dialog.setOnDismissListener(new OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    goMainScreen();
                                }
                            });
                            dialog.show();
                        } else {
                            goMainScreen();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        callBack.timeout(AppConstants.TIME_OUT_REQUEST);
        // end.
        mQuery.ajax(url, params, String.class, callBack);
    }

    protected void saveDataToDB(Globals globals, JSONObject objectResult) {
        String userId = JsonParserUtils.getStringValues(objectResult,
                AppConstants.KEY_PARSER.USER_ID);
        globals.setPrefKeyUserId(userId);
        JSONObject objectProfile;
        try {
            objectProfile = objectResult
                    .getJSONObject(AppConstants.KEY_PARSER.PROFILE);
            String idMemberShip = JsonParserUtils.getStringValues(
                    objectProfile, AppConstants.KEY_PARSER.MEMBER_SHIP_ID);
            String nameRegister = JsonParserUtils.getStringValues(
                    objectProfile, AppConstants.KEY_PARSER.FULL_NAME);
            globals.setPrefKeyMemberShip(idMemberShip);
            globals.saveNameRegister(nameRegister);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean checkValidate() {
        if (mEdtName.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.msg_empty_name), Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        return true;
    }

    private void showDialogRegister(String msgLoading) {
        progressDialog = new ProgressDialog(ActivityRegisterStampCard.this);
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
     * Go main screen.
     */
    private void goMainScreen() {
        Intent iGoMain = new Intent(ActivityRegisterStampCard.this,
                ViewActivity.class);
        startActivity(iGoMain);
        finish();
    }
}
