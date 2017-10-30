package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.app.Application;
import android.content.Context;

import com.duarise.bundlehelper.PreferenceHelper;

public class Globals extends Application {
    // 端末設定
    private Context mContext;
    private PreferenceHelper mPreferenceHelper;

    final private String CLIENT_ID;
    final private String DEVICE_TOKEN;
    final private String DEVICE_UUID;
    final private String USER_ID;

    // URL API設定
    final private String URL_BASE;
    final private String URL_API;
    final private String URL_WEBVIEW;
    final private String URL_IMG;
    final private String URL_VIDEOS;
    final private String URL_MENULIST;
    final private String URL_ADMIN;
    final private String URL_METHOD_GET;
    final private String URL_SETTING;
    final private String URL_SETTING_NOTIFICATION;
    final private String URL_TOKEN_ADD;
    final private String URL_SIDEMENU;
    final private String URL_TABMENU;
    final private String URL_API_NEWS;
    final private String URL_API_IINE;
    final private String URL_API_VIDEOS;

    final private String URL_API_BBS;
    final private String URL_API_BBS_ADD;
    final private String URL_API_BBS_UPDATE;
    final private String URL_API_BBS_DELETE;

    final private String URL_API_MENU;
    final private String URL_API_MENU_TOP;
    final private String URL_API_MENU_CATE;
    final private String URL_API_COUPON;
    final private String URL_API_REGIST;
    final private String URL_API_LOGIN;

    final private String URL_API_TOP;
    final private String URL_API_SHOPINFO;
    final private String URL_API_SHOP_CATE;
    final private String URL_API_COLOR_THEME;
    final private String URL_API_SPLASH;
    final private String URL_BOOT_LOG;
    final private String URL_API_CMS;
    final private String URL_API_COUPON_USE_COUNT;

    // URL IMGディレクトリ設定
    final private String URL_IMG_SIDEMENU;
    final private String URL_IMG_NEWS;
    final private String URL_IMG_MENU_TOP;
    final private String URL_IMG_MENU_CATE;
    final private String URL_IMG_MENU_ITEM;
    final private String URL_IMG_COUPON_ITEM;
    final private String URL_API_TOP_IMG;
    final private String URL_IMG_TOP;
    final private String URL_IMG_SPLASH;
    final private String URL_IMG_SHOPINFO;

    final private String URL_IMG_TOP_BACKGROUND;
    final private String URL_IMG_HEADER;
    final private String URL_IMG_SHOP_CATE;
    final private String URL_IMG_SHOP_MULTI;

    // URL VIDEOSディレクトリ設定
    final private String URL_VIDEO_NEWS;

    // SharedPreference Name
    final private String PREF_KEY_NAME;
    // Id membership.
    final private String PRF_KEY_MEMBER_SHIP;
    final private String PREF_KEY_NAME_REGISTER;
    final private String PREF_KEY_SUB_CLOR;
    final private String PREF_KEY_BACKGROUND_COLOR;

    final private String PREF_KEY_TOKEN;
    final private String PREF_KEY_USER_ID;
    final private String PREF_KEY_CMS_ZIP_HASH;

    final private String PREF_KEY_JSON;
    final private String PREF_KEY_SIDEMENU;
    final private String PREF_KEY_TABMENU;
    final private String PREF_KEY_NEWS;
    final private String PREF_KEY_MENU;
    final private String PREF_KEY_SUB_MENU;
    final private String PREF_KEY_BBS;
    final private String PREF_KEY_COLOR_THEME;
    final private String PREF_KEY_TOP_BLOCK;

    // stamp card api.

    private final String URL_GET_CONFIG_REGISTER;
    private final String URL_REGISTER;
    private final String URL_GET_INFO_STAMP;
    private final String URL_USING_PROMOT;
    private final String UPDATE_STAMP_COLECTION;
    private final String ADD_DAILY_STAMP;
    private final String URL_IMAGE_BASE;
    private final String URL_ADMIN_STAMP;
    private final String URL_MEMBER_SHIP;
    private final String URL_NEWS_DETAIL;

    /**
     * 初期化
     */
    public Globals() {
        // 端末設定
        CLIENT_ID = "19";
        DEVICE_TOKEN = "";
        DEVICE_UUID = "00000000-C170-1001-B000-001C4D75DAA9";
        USER_ID = "";
        /**
         *  URL API設定
         */
        // URL : ベース
//		URL_BASE    = "http://api.myapp-tiara.com/";
//		URL_ADMIN 	= "http://admin.myapp-tiara.com/";

//		URL_BASE    = "http://tiara.api.hirolabo.net/";
//		URL_ADMIN 	= "http://tiara.admin.hirolabo.net/";

//		URL_BASE     = "http://ep.api.hirolabo.net/";
//		URL_ADMIN	 = "http://ep.admin.hirolabo.net/";

//		URL_BASE     = "http://new.api.i-post.jp/";
//		URL_ADMIN	 = "http://new.admin.i-post.jp/";

//		URL_BASE     = "http://api.appcooking.jp/";
//		URL_ADMIN	 = "http://admin.appcooking.jp/";

        // TeST
//        URL_BASE = "http://api-ack.hanelsoft.vn:28080/";
//        URL_ADMIN = "http://admin-ack.hanelsoft.vn:28080/";
//        URL_ADMIN_STAMP = "http://admin-ack.hanelsoft.vn:28080/";
//         Go live phase 1
//        URL_BASE = "http://test-api.appcooking.jp/";
//        URL_ADMIN = "http://test-admin.appcooking.jp/";
//        URL_ADMIN_STAMP = "http://test-app.appcooking.jp/";

//        URL_BASE = "http://192.168.31.127:2001/";
//        URL_ADMIN = "http://192.168.31.127:2000/";
//        URL_ADMIN_STAMP = "http://192.168.31.127:2002/";

//        URL_BASE = "http://192.168.31.243:2001/";
//        URL_ADMIN = "http://192.168.31.243:2000/";
//        URL_ADMIN_STAMP = "http://192.168.31.243:2002/";

        URL_BASE = "http://akapi.navigo-tech.com/";
        URL_ADMIN = "http://akadmin.navigo-tech.com/";
        URL_ADMIN_STAMP = "http://akapp.navigo-tech.com/";

        URL_SETTING = URL_BASE + "setting/" + CLIENT_ID + "/";
        URL_SETTING_NOTIFICATION = URL_BASE + "setting/notification";

        // URL : API
        //URL_API      = URL_BASE + "api/";
        URL_API = URL_BASE;
        // URL : WebView
        URL_WEBVIEW = URL_BASE + "app/";
        // URL : Image ファイル
        URL_IMG = URL_ADMIN + "assets/img/";
        //URL_IMG      = URL_ADMIN;

        // URL : Video ファイル
        URL_VIDEOS = URL_BASE + "videos/";
        // URL : MenuList ファイル
        URL_MENULIST = URL_BASE + "menulist/";

        // METHOD GET方式 クライアントID
        URL_METHOD_GET = "?client_id=" + CLIENT_ID;

        // TOKEN ADD/ UPDATE
        URL_TOKEN_ADD = URL_API + "token";
        // SideMenu]
        URL_SIDEMENU = URL_BASE + "sidemenu/" + CLIENT_ID + "/";
        URL_TABMENU = URL_BASE + "tabmenu/" + CLIENT_ID + "/";
        // NEWS
        URL_API_NEWS = URL_BASE + "news/" + CLIENT_ID + "/";

        URL_API_IINE = URL_BASE + "news/iine/add/";

        URL_API_CMS = URL_ADMIN + "/assets/img/app/cms/" + CLIENT_ID + "/";

        // VIDEOS
        URL_API_VIDEOS = URL_API + "videos/" + URL_METHOD_GET;
        // BBS
        URL_API_BBS = URL_API + "bbs/" + URL_METHOD_GET;
        URL_API_BBS_ADD = URL_API + "bbs/add.php";
        URL_API_BBS_UPDATE = URL_API + "bbs/update.php";
        URL_API_BBS_DELETE = URL_API + "bbs/delete.php";

        // MENU LIST
        //URL_API_MENU   = URL_API + "menuitem/" + URL_METHOD_GET;
        URL_API_MENU = URL_BASE + "menu/" + CLIENT_ID + "/";
        URL_API_MENU_TOP = URL_IMG + "app/menu/top/" + CLIENT_ID + "/";
        URL_API_MENU_CATE = URL_IMG + "app/menu/cate/" + CLIENT_ID + "/";

        URL_API_TOP_IMG = URL_IMG + "app/top/" + CLIENT_ID + "/";

        // COUPON LIST
        URL_API_COUPON = URL_BASE + "coupon/" + CLIENT_ID + "/";
        URL_API_COUPON_USE_COUNT = URL_BASE + "coupon/use/add/";

        // USER REGIST / LOGIN
        //URL_API_REGIST = URL_API + "regist/";
        URL_API_REGIST = URL_API + "user/add/";
        //URL_API_LOGIN  = URL_API + "login/";
        URL_API_LOGIN = URL_API + "user/";

        URL_API_TOP = URL_BASE + "top/" + CLIENT_ID + "/";

        URL_API_SHOPINFO = URL_BASE + "shop/profile/" + CLIENT_ID + "/";
        URL_IMG_SHOPINFO = URL_ADMIN + "assets/img/app/shop/" + CLIENT_ID + "/";
        URL_API_SHOP_CATE = URL_BASE + "shop/multi/" + CLIENT_ID + "/";
        URL_IMG_SHOP_CATE = URL_ADMIN + "assets/img/app/multi_shop/" + CLIENT_ID + "/";

        URL_API_COLOR_THEME = URL_BASE + "theme/" + CLIENT_ID + "/";
        URL_API_SPLASH = URL_BASE + "splash/" + CLIENT_ID + "/";
        URL_IMG_SPLASH = URL_ADMIN + "assets/img/app/splash/" + CLIENT_ID + "/";

        URL_IMG_TOP_BACKGROUND = URL_ADMIN + "assets/img/app/theme/background/" + CLIENT_ID + "/";
        URL_IMG_HEADER = URL_ADMIN + "assets/img/app/theme/header/" + CLIENT_ID + "/";

        //URL NEWS DETAIL
        URL_NEWS_DETAIL = URL_BASE + "news/detail/" + CLIENT_ID + "/";
        /**
         * URL ディレクトリ設定
         */
        // URL IMGディレクトリ設定
        URL_IMG_SIDEMENU = URL_BASE + "img/sidemenu/icon/";
        //URL_IMG_NEWS      = URL_BASE + "img/news/";
        URL_IMG_NEWS = URL_IMG + "app/news/" + CLIENT_ID + "/";
        URL_IMG_MENU_TOP = URL_BASE + "img/menuitem/";
        URL_IMG_MENU_CATE = URL_IMG + "app/menu/cate/" + CLIENT_ID + "/";
        URL_IMG_MENU_ITEM = URL_IMG + "app/menu/item/" + CLIENT_ID + "/";
        URL_IMG_COUPON_ITEM = URL_IMG + "app/coupon/" + CLIENT_ID + "/";
        URL_IMG_TOP = URL_IMG + "app/top/" + CLIENT_ID + "/";

        URL_IMG_SHOP_MULTI = URL_IMG + "app/multi_shop/" + CLIENT_ID + "/";

        // URL VIDEOSディレクトリ設定
        URL_VIDEO_NEWS = URL_BASE + "videos/news/";

        // 起動時
        URL_BOOT_LOG = URL_BASE + "log/action/count/";

        /**
         * SharedPreference
         */
        PREF_KEY_NAME = "_pref_api_data";
        PREF_KEY_TOKEN = "_pref_key_token";
        PREF_KEY_USER_ID = "_pref_key_user_id";
        PREF_KEY_JSON = "_pref_key_json";
        PREF_KEY_SIDEMENU = "_pref_key_sidemenu";
        PREF_KEY_TABMENU = "_pref_key_tabmenu";
        PREF_KEY_NEWS = "_pref_key_news";
        PREF_KEY_MENU = "_pref_key_menu";
        PREF_KEY_SUB_MENU = "_pref_key_sub_menu";
        PREF_KEY_BBS = "_pref_key_bbs";
        PREF_KEY_COLOR_THEME = "_pref_key_color_theme";
        PREF_KEY_TOP_BLOCK = "_pref_key_top_block";
        PREF_KEY_CMS_ZIP_HASH = "_pref_key_cms_zip_hash";
        PRF_KEY_MEMBER_SHIP = "pref_key_member_ship";
        PREF_KEY_NAME_REGISTER = "pref_key_name_register";
        PREF_KEY_SUB_CLOR = "pref_key_sub_color";
        PREF_KEY_BACKGROUND_COLOR = "pref_key_background_color";

        // Stamp card.
        URL_GET_CONFIG_REGISTER = URL_BASE + "user/check";
        URL_REGISTER = URL_BASE + "user/register";
        URL_GET_INFO_STAMP = URL_BASE + "stamp";
        URL_USING_PROMOT = URL_BASE + "stamp/payment";
        UPDATE_STAMP_COLECTION = URL_BASE + "stamp/qrcode";
        ADD_DAILY_STAMP = URL_BASE + "stamp/appstartup";
        URL_IMAGE_BASE = URL_ADMIN_STAMP + "assets/img/app/stamp/";
        URL_MEMBER_SHIP = URL_BASE + "user/membercard";
    }

    public void initPreference(Context context) {
        mContext = context;
        mPreferenceHelper = PreferenceHelper.getInstance(mContext, this.getPrefKeyApiName());
    }

    /**
     * GET : CLIENT ID
     *
     * @return CLIENT_ID
     */
    public String getClientID() {
        return this.CLIENT_ID;
    }

    /**
     * GET : UUID
     *
     * @return DEVICE_UUID
     */
    public String getUUID() {
        return this.DEVICE_UUID;
    }

    /**
     * GET : URL API
     *
     * @return URL_API
     */
    public String getUrlApi() {
        return this.URL_API;
    }


    /**
     * GET : URL WebView
     *
     * @return URL_WEBVIEW
     */
    public String getUrlWebView() {
        return this.URL_WEBVIEW;
    }

    /**
     * GET : URL Image
     *
     * @return URL_IMG
     */
    public String getUrlImage() {
        return this.URL_IMG;
    }

    /**
     * GET : URL Video
     *
     * @return URL_VIDEOS
     */
    public String getUrlVideos() {
        return this.URL_VIDEOS;
    }

    /**
     * GET : URL MenuList
     *
     * @return URL_MENULIST
     */
    public String getUrlMenuList() {
        return this.URL_MENULIST;
    }

    /**
     * GET : URL Token
     *
     * @return URL_TOKEN_ADD
     */
    public String getUrlToken() {
        return this.URL_TOKEN_ADD;
    }

    /**
     * GET : URL Sidemenu
     *
     * @return URL_SIDEMENU
     */
    public String getUrlSidemenu() {
        return this.URL_SIDEMENU;
    }

    /**
     * GET : URL Tabmenu
     *
     * @return URL_TABMENU
     */
    public String getUrlTabmenu() {
        return this.URL_TABMENU;
    }

    /**
     * GET : URL API News
     *
     * @return URL_API_NEWS
     */
    public String getUrlApiNews() {
        return this.URL_API_NEWS;
    }

    public String getUrlApiIine() {
        return this.URL_API_IINE;
    }


    /**
     * GET : URL API Videos
     *
     * @return URL_API_VIDEOS
     */
    public String getUrlApiVideos() {
        return this.URL_API_VIDEOS;
    }

    /**
     * GET : URL API BBS
     *
     * @return URL_API_BBS
     */
    public String getUrlApiBbs() {
        return this.URL_API_BBS;
    }

    /**
     * GET : URL API BBS Add
     *
     * @return URL_API_BBS_ADD
     */
    public String getUrlApiBbsAdd() {
        return this.URL_API_BBS_ADD;
    }

    /**
     * GET : URL API BBS Update
     *
     * @return URL_API_BBS_UPDATE
     */
    public String getUrlApiBbsUpdate() {
        return this.URL_API_BBS_UPDATE;
    }

    /**
     * GET : URL API BBS Delete
     *
     * @return URL_API_BBS_DELETE
     */
    public String getUrlApiBbsDelete() {
        return this.URL_API_BBS_DELETE;
    }

    /**
     * GET : URL API Menu
     *
     * @return URL_API_MENU
     */
    public String getUrlApiMenu() {
        return this.URL_API_MENU;
    }

    public String getUrlApiMenuTop() {
        return this.URL_API_MENU_TOP;
    }

    public String getUrlApiMenuCate() {
        return this.URL_API_MENU_CATE;
    }

    /**
     * GET : URL API Coupon
     *
     * @return URL_API_COUPON
     */
    public String getUrlApiCoupon() {
        return this.URL_API_COUPON;
    }

    /**
     * GET : URL API Top
     *
     * @return URL_API_TOP
     */
    public String getUrlApiTop() {
        return this.URL_API_TOP;
    }

    /**
     * GET : URL User Regist
     *
     * @return URL_API_REGIST
     */
    public String getUrlApiUserRegist() {
        return this.URL_API_REGIST;
    }

    /**
     * GET : URL User Login
     *
     * @return URL_API_LOGIN
     */
    public String getUrlApiUserLogin() {
        return this.URL_API_LOGIN;
    }

    /**
     * GET : URL Image Sidemenu
     *
     * @return URL_IMG_SIDEMENU
     */
    public String getUrlImgSidemenu() {
        return this.URL_IMG_SIDEMENU;
    }

    /**
     * GET : URL Image News
     *
     * @return URL_IMG_NEWS
     */
    public String getUrlImgNews() {
        return this.URL_IMG_NEWS;
    }

    /**
     * GET : URL Image Menu Top
     *
     * @return URL_IMG_MENU_TOP
     */
    public String getUrlImgMenuTop() {
        return this.URL_IMG_MENU_TOP;
    }

    /**
     * GET : URL Image Menu Category
     *
     * @return URL_IMG_MENU_CATE
     */
    public String getUrlImgMenuCate() {
        return this.URL_IMG_MENU_CATE;
    }

    /**
     * GET : URL Image Menu Item
     *
     * @return URL_IMG_MENU_ITEM
     */
    public String getUrlImgMenuItem() {
        return this.URL_IMG_MENU_ITEM;
    }

    public String getUrlImgCouponItem() {
        return this.URL_IMG_COUPON_ITEM;
    }

    /**
     * GET : URL Video News
     *
     * @return URL_VIDEO_NEWS
     */
    public String getUrlVideoNews() {
        return this.URL_VIDEO_NEWS;
    }

    /**
     * GET : SharedPreference Key Api
     *
     * @return PREF_NAME
     */
    public String getPrefKeyApiName() {
        return this.PREF_KEY_NAME;
    }

    /**
     * GET : SharedPreference Key Json
     *
     * @return PREF_KEY_JSON
     */
    public String getPrefKeyJson() {
        return this.PREF_KEY_JSON;
    }

    /**
     * GET : SharedPreference Key Token
     *
     * @return PREF_KEY_TOKEN
     */
    public String getPrefKeyToken() {
        return this.PREF_KEY_TOKEN;
    }

    /**
     * GET : SharedPreference Key SideMenu
     *
     * @return PREF_KEY_NEWS
     */
    public String getPrefKeySidemenu() {
        return this.PREF_KEY_SIDEMENU;
    }

    /**
     * GET : SharedPreference Key TabMenu
     *
     * @return PREF_KEY_NEWS
     */
    public String getPrefKeyTabmenu() {
        return this.PREF_KEY_TABMENU;
    }

    /**
     * GET : SharedPreference Key News
     *
     * @return PREF_KEY_NEWS
     */
    public String getPrefKeyNews() {
        return this.PREF_KEY_NEWS;
    }

    /**
     * GET : SharedPreference Key Menu
     *
     * @return PREF_KEY_MENU
     */
    public String getPrefKeyMenu() {
        return this.PREF_KEY_MENU;
    }

    /**
     * GET : SharedPreference Key Sub Menu
     *
     * @return PREF_KEY_SUB_MENU
     */
    public String getPrefKeySubMenu() {
        return this.PREF_KEY_SUB_MENU;
    }

    /**
     * GET : SharedPreference Key BBS
     *
     * @return PREF_KEY_SUB_MENU
     */
    public String getPrefKeyBbs() {
        return this.PREF_KEY_BBS;
    }

    /**
     * GET : SharedPreference Key User ID
     *
     * @return PREF_KEY_USER_ID
     */
    public String getPrefKeyUserID() {
        String id = (String) mPreferenceHelper.get(this.PREF_KEY_USER_ID, String.class);
        return id;
    }

    public String getDeviceToken() {
        String token = (String) mPreferenceHelper.get(this.PREF_KEY_TOKEN, String.class);
        return token;
    }

    public String getPrefKeyCmsZipHash() {
        String hash = (String) mPreferenceHelper.get(this.PREF_KEY_CMS_ZIP_HASH, String.class);
        return hash;
    }

    public String getPrefKeyColorTheme() {
        return this.PREF_KEY_COLOR_THEME;
    }

    public String getPrefKeyTopBlock() {
        return this.PREF_KEY_TOP_BLOCK;
    }

    /**
     * SET : Device Token
     *
     * @param token
     */
    public void setDeviceToken(String token) {
        this.mPreferenceHelper.put(this.PREF_KEY_TOKEN, token);
    }

    public void setPrefKeyUserId(String id) {
        this.mPreferenceHelper.put(this.PREF_KEY_USER_ID, id);
    }

    public void setPrefKeyCmsZipHash(String hash) {
        this.mPreferenceHelper.put(this.PREF_KEY_CMS_ZIP_HASH, hash);
    }

    public void setPrefKeyMemberShip(String hash) {
        this.mPreferenceHelper.put(this.PRF_KEY_MEMBER_SHIP, hash);
    }

    public void saveNameRegister(String name) {
        this.mPreferenceHelper.put(this.PREF_KEY_NAME_REGISTER, name);
    }

    public void saveSubColor(String color) {
        this.mPreferenceHelper.put(this.PREF_KEY_SUB_CLOR, color);
    }

    public void saveBGColor(String color) {
        this.mPreferenceHelper.put(this.PREF_KEY_BACKGROUND_COLOR, color);
    }

    public String getSubColor() {
        return (String) mPreferenceHelper.get(this.PREF_KEY_SUB_CLOR, String.class);
    }

    public String getBGColor() {
        return (String) mPreferenceHelper.get(this.PREF_KEY_BACKGROUND_COLOR, String.class);
    }

    public String getKeyMemberShip() {
        String stringMemberShip = (String) mPreferenceHelper.get(
                this.PRF_KEY_MEMBER_SHIP, String.class);
        return stringMemberShip;
    }

    public String getNameRegister() {
        return (String) mPreferenceHelper.get(this.PREF_KEY_NAME_REGISTER, String.class);
    }

    public String getUrlApiTopImage() {
        return this.URL_API_TOP_IMG;
    }

    public String getUrlImgTop() {
        return this.URL_IMG_TOP;
    }

    public String getUrlApiShopInfo() {
        return this.URL_API_SHOPINFO;
    }

    public String getUrlApiColorTheme() {
        return this.URL_API_COLOR_THEME;
    }

    public String getUrlApiSplash() {
        return this.URL_API_SPLASH;
    }

    public String getImgTopBackground() {
        return this.URL_IMG_TOP_BACKGROUND;
    }

    public String getImgHeader() {
        return this.URL_IMG_HEADER;
    }

    public String getUrlBootLog() {
        return this.URL_BOOT_LOG;
    }

    public String getUrlApiCouponCount() {
        return this.URL_API_COUPON_USE_COUNT;
    }

    public String getUrlApiShopCate() {
        return this.URL_API_SHOP_CATE;
    }

    public String getImgShopCate() {
        return this.URL_IMG_SHOP_CATE;
    }

    public String getUrlImgShopInfo() {
        return this.URL_IMG_SHOPINFO;
    }

    public String getUrlImgShopMulti() {
        return this.URL_IMG_SHOP_MULTI;
    }

    public String getUrlImgSplash() {
        return this.URL_IMG_SPLASH;
    }

    public String getUrlSetting() {
        return this.URL_SETTING;
    }

    public String getUrlSettingNotif() {
        return this.URL_SETTING_NOTIFICATION;
    }

    // Stamp card.

    public String getUrlConfigRegister() {
        return URL_GET_CONFIG_REGISTER;
    }

    public String getUrlRegister() {
        return URL_REGISTER;
    }

    public String getUrlContentStamp() {
        return URL_GET_INFO_STAMP;
    }

    public String getUrlUsingPromot() {
        return URL_USING_PROMOT;
    }

    public String getUrlUpdateColectionStamp() {
        return UPDATE_STAMP_COLECTION;
    }

    public String getUrlAddDailyStamp() {
        return ADD_DAILY_STAMP;
    }

    public String getUrlImageBase() {
        return URL_IMAGE_BASE;
    }

    public String getUrlShowMemberShip() {
        return URL_MEMBER_SHIP;
    }

}
