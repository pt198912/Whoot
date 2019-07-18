package com.app.whoot.util;

public class Constants {
    public static final int TYPE_COUPON_EXCHANGE_BROZEN=1;//铜到银
    public static final int TYPE_COUPON_EXCHANGE_SILVER=2;//银到金
    public static final int TYPE_COUPON_EXCHANGE_GOLDEN=3;//金到砖

    public static final int TYPE_BROZEN=1;//獲得銅幣
    public static final int TYPE_SILVER=2;//獲得銀幣
    public static final int TYPE_GOLD=3;//獲得金幣
    public static final int TYPE_DIAMOND=4;//獲得鑽石

    public static final int TYPE_COUPON_GAIN_COIN_BASE=3;//獲得銅幣
    public static final int TYPE_COUPON_GAIN_BROZEN=TYPE_COUPON_GAIN_COIN_BASE+TYPE_BROZEN;//獲得銅幣
    public static final int TYPE_COUPON_GAIN_SILVER=TYPE_COUPON_GAIN_COIN_BASE+TYPE_SILVER;//獲得銀幣
    public static final int TYPE_COUPON_GAIN_GOLD=TYPE_COUPON_GAIN_COIN_BASE+TYPE_GOLD;//獲得金幣
    public static final int TYPE_COUPON_GAIN_DIAMOND=TYPE_COUPON_GAIN_COIN_BASE+TYPE_DIAMOND;//獲得鑽石



    public static final String EVENT_TYPE_TOKEN_ANIM="event_type_token_anim";
    public static final String EVENT_TYPE_LOGIN_SUCCESS="event_type_login";
    public static final String EVENT_TYPE_TOKEN_EXCHANGE="event_type_token_exchange";
    public static final String EVENT_TYPE_SIGN="exvnt_type_sign";
    public static final String EVENT_TYPE_MESSAGING="event_type_messaging";
    public static final String EVENT_TYPE_FCM="event_type_fcm";
    public static final String EVENT_TYPE_SHOW_FIND_FRAG="show_find_frag";
    public static final String EVENT_TYPE_GAIN_TOKEN="gain_token";
    public enum SORT_TYPE{
        SORT_TYPE_DEFAULT,SORT_TYPE_RATING,SORT_TYPE_OPEN_STATUS
    }
    public static final int OPEN_STATUS_CLOSED=2;
    public static final int OPEN_STATUS_CLOSEING_SOON=1;
    public static final int OPEN_STATUS_OPEND=0;

    public static final String ACTION_START_MAIN_ACTIVITY_TO_MAP="to_map";
    public static final String ACTION_START_MAIN_ACTIVITY_TO_USED_TOKEN_PAGE="to_uesd_token";
    public static final String EXTRA_KEY_START_MAIN_ACTIVITY="extra_key_start_main_act";

    /**
     * 活动码不存在
     */
    public static final int ACTIVITY_CODE_NOTFOUND = 5900;
    /**
     * 活动码最大限制5
     */
    public static final int ACTIVITY_CODE_MAX = 5901;
    /**
     * 活动码最已经被使用
     */
    public static final int ACTIVITY_CODE_USED = 5902;
    /**
     * 不允许自己的邀请码
     */
    public static final int INVITER_CODE_SELF = 5903;
    /**
     * 邀请码不存在
     */
    public static final int INVITER_CODE_NOTFOUND = 5904;
    /**
     * 邀请码調用world服務異常
     */
    public static final int INVITER_CODE_WORLD_ERROR = 5905;
    /**
     * 邀请码活动码长度不正确
     */
    public static final int INVITER_ACTIVITY_CODE_LENGTH_ERROR = 5906;
    /**
     * 只能被邀请一次
     */
    public static final int COMMODITY_IDS_WORLD_ERROR = 5907;


    public static final int TOKEN_FROM_EXCHANGE=1;
    public static final int TOKEN_FROM_COMMENT=2;
    public static final int TOKEN_FROM_REGISTER=3;
    public static final int TOKEN_FROM_REFERRAL=4;
    public static final int TOKEN_FROM_INVITED=5;
    public static final int TOKEN_FROM_EVENT=6;
    public static final int TOKEN_FROM_DAILY_SIGN=7;
    public static final int TOKEN_FROM_VERSION_UPGRADE=8;

    public static final String SP_KEY_KOL_JOIN_FROM="kol_join_from";
    public static final String KOL_JOIN_FROM_MOBILE="MOBILE";
    public static final String KOL_JOIN_FROM_EMAIL="EMAIL";
    public static final String KOL_JOIN_FROM_FB="FB";
    public static final String KOL_JOIN_FROM_INPUT="INPUT";
    public static final String KOL_JOIN_FROM_INVITE="INVITE";

    public static final String SP_KEY_STORE_DETAIL_FROM="store_detail_from";
    public static final String GO_STORE_DETAIL_FROM_FAVOURITE="FAV";
    public static final String GO_STORE_DETAIL_FROM_KOL_ACT="ACTIVITY_KOL";
    public static final String GO_STORE_DETAIL_FROM_DISCOVERY="DISCOVERY";
    public static final String GO_STORE_DETAIL_FROM_SEARCH="SEARCH";
    public static final String GO_STORE_DETAIL_FROM_MAP="MAP";
    public static final String GO_STORE_DETAIL_FROM_MY_REVIEW="MY_REVIEW";
    public static final String GO_STORE_DETAIL_FROM_GIFT="GIFT";
    public static final String GO_STORE_DETAIL_FROM_WRITE_REVIEW="WRITE_REVIEW";
    public static final String GO_STORE_DETAIL_FROM_STORE_POPWINDOW="STORE_POPWINDOW";
    public static final String GO_STORE_DETAIL_FROM_USED_TOKEN="USED_TOKEN";

    public static final String SP_KEY_COUP_DETAIL_FROM="coup_detail_from";
    public static final String GO_COUP_DETAIL_FROM_STORE_DETAIL="SHOP";
    public static final String GO_COUP_DETAIL_FROM_KOL_ACT="ACTIVITY_KOL";
    public static final String GO_COUP_DETAIL_FROM_DISCOVERY="DISCOVERY";

}
