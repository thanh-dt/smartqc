package app.com.hss.cooking.magatama.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;

public class AppConstants {
    public static Typeface mTypeFont3;
    public static Typeface mTypeFont6;

    public static final int MAX_LENGTH_EDITTEXT = 30;
    public static final int TIME_OUT_REQUEST = 30000;

    public static class STATUS_CODE {
        public static int SUCCESS = 1;
        public static int USER_HAS_REGISTED = 2;
        public static int QR_CODE_OF_OTHER_STORE = -1;
    }

    public static class CONFIG_VIEW {
        public static final String SHOW = "1";
        public static final String INVISIBLE = "0";
    }

    public static class STATUS_PROMOT {
        public static final int USED = 1;
        public static final int NORMAL = 0;
    }

    public static class MEMBER_SHIP_STATUS {
        public static final int SHOW = 1;
        public static final int HIDDEN = 0;
    }

    public static class SNS_VALUE {
        public static final String SNS_ID_FACEBOOK = "1";
        public static final String SNS_ID_TWITTER = "2";
        public static final String SNS_ID_LINE = "3";
    }

    public static class KEY_PARSER {
        public static final String STATUS = "status";
        public static final String RESUTL = "result";
        public static final String CLIENT_ID = "client_id";
        public static final String FULL_NAME_STATUS = "fullname_status";
        public static final String BIRTH_DAY_STATUS = "birthday_status";
        public static final String GENDER_STATUS = "gender_status";
        public static final String QUESTION1 = "question_1";
        public static final String QUESTION1_STATUS = "question_1_status";
        public static final String QUESTION2 = "question_2";
        public static final String QUESTION2_STATUS = "question_2_status";
        public static final String QUESTION3 = "question_3";
        public static final String QUESTION3_STATUS = "question_3_status";
        public static final String QUESTION4 = "question_4";
        public static final String QUESTION4_STATUS = "question_4_status";
        public static final String QUESTION5 = "question_5";
        public static final String QUESTION5_STATUS = "question_5_status";
        public static final String QUESTION6 = "question_6";
        public static final String QUESTION6_STATUS = "question_6_status";
        public static final String USER_ID = "user_id";
        public static final String MEMBER_SHIP_ID = "membership_id";
        public static final String TYPE_ICON = "type_icon";
        public static final String PROMOTION_INFO = "promotion_info";
        public static final String FILE_NAME = "file_name";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String ID = "id";
        public static final String TICKET_QUANTITY = "ticket_quantity";
        public static final String POLICY = "policy";
        public static final String HOW_TO_GET_STAMP = "how_to_get_stamp";
        public static final String APP_GIFT = "app_gift";
        public static final String SNS_GIFT = "sns_gift";
        public static final String STAMP_MEMBER_INFO = "stamp_member_info";
        public static final String DATE = "date";
        public static final String PROFILE = "profile";
        public static final String PAYMENT_STATUS = "payment_status";
        public static final String DATE_DEADLINE = "date_deadline";
        public static final String QUANTITY = "quantity";
        public static final String STAMP_QUANTITY = "stamp_quantity";
        public static final String FULL_NAME = "fullname";
        public static final String BIRTHDAY = "birthday";
        public static final String GENDER = "gender";
        public static final String REPLY_1 = "reply_1";
        public static final String REPLY_2 = "reply_2";
        public static final String REPLY_3 = "reply_3";
        public static final String REPLY_4 = "reply_4";
        public static final String REPLY_5 = "reply_5";
        public static final String REPLY_6 = "reply_6";
        public static final String MEMBER_CARD_STATUS = "member_card_status";
        public static final String SHOP_NAME = "shop_name";
        public static final String PREF = "pref";
        public static final String CITY = "city";
        public static final String ADDRESS_OPT1 = "address_opt1";
        public static final String ADDRESS_OPT2 = "address_opt2";
        public static final String SNS = "sns";
        public static final String VALUE = "value";
        public static final String URL = "url";
        public static final String SNS_ID = "sns_id";
    }

    public static void loadFontForStamp(Context context) {
        mTypeFont3 = Typeface.createFromAsset(context.getAssets(),
                "font/HiraginoSansGBW3.otf");
        mTypeFont6 = Typeface.createFromAsset(context.getAssets(),
                "font/HIRAGINOSANSGBW6.otf");
    }

    public static Typeface getW3Font(Context context) {
        if (mTypeFont3 != null) {
            Log.e("ok", "ok");
            return mTypeFont3;
        } else {
            Log.e("ok", "fail");
            return Typeface.createFromAsset(context.getAssets(),
                    "font/HiraginoSansGBW3.otf");
        }
    }

    public static Typeface getW6Font(Context context) {
        if (mTypeFont6 != null) {
            Log.e("ok", "ok6");
            return mTypeFont6;
        } else {
            Log.e("ok", "fail6");
            return Typeface.createFromAsset(context.getAssets(),
                    "font/HIRAGINOSANSGBW6.otf");
        }
    }

    public static void startUriLink(Context context, Uri uri) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(i);
        } catch (Exception ex) {
            if (!uri.toString().toLowerCase().startsWith("http://") || !uri.toString().toLowerCase().startsWith("https://")) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + uri));
                context.startActivity(i);
            }
            ex.printStackTrace();
        }
    }
}
