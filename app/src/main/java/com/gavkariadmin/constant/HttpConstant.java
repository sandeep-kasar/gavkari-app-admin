package com.gavkariadmin.constant;


import com.gavkariadmin.BuildConfig;

public class HttpConstant {

    /**
     * base url for api and image access
     */
    //public static final String BASE_URL = "http://10.0.2.2/gawakariapp/index.php/";//localhost

    public static final String BASE_URL = BuildConfig.HOST;

    public static final String BASE_EVENT_DOWNLOAD_URL = BuildConfig.MEDIA+"event/";

    public static final String BASE_NEWS_DOWNLOAD_URL = BuildConfig.MEDIA+"news/";

    public static final String BASE_AVATAR_DOWNLOAD_URL = BuildConfig.MEDIA+"avatar/";

    public static final String BASE_BUYSALE_DOWNLOAD_URL = BuildConfig.MEDIA+"buy-sale/";

    /**
     * headers
     */
    public static final String JSON_TYPE = "Content-Type:application/json";
    public static final String CACHE_CONTROL = "Cache-Control:no-cache";

    /**
     * Operations.
     */

    public static final String UPLOAD_EVENT = "event/upload";

    public static final String UPLOAD_NEWS = "news/upload";

    public static final String UPLOAD_AVATAR = "user/upload";


    public static final String SIGN_IN = "admin/login";


    public static final String ALL_EVENTS = "admin/events";

    public static final String GET_EVENT_MEDIA= "event/photos/";

    public static final String ACCEPT_EVENTS = "admin/event/accept";

    public static final String MY_AD_DELETE = "user/myads/delete/";


    public static final String ALL_NEWS = "admin/news";

    public static final String GET_NEWS_MEDIA= "news/photos/";

    public static final String ACCEPT_NEWS = "admin/news/accept";

    public static final String DELETE_NEWS = "user/mynews/delete/";


    public static final String ALL_ADS = "admin/buysale";

    public static final String GET_ADS_MEDIA= "buysale/photos";

    public static final String ACCEPT_AD = "admin/buysale/accept";

    public static final String DELETE_AD = "admin/buysale/delete/";


    /**
     * Api code.
     */
    public static final int SUCCESS = 1;
    public static final int EMPTY_REQUEST = -1;
    public static final int FIELD_IS_EMPTY = -2;
    public static final int FAIL_TO_INSERT = -3;
    public static final int DUPLICATE_DATA = -4;
    public static final int NO_DATA_AVAILABLE = -5;

}
