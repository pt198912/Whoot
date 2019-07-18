package com.app.whoot.util;

public class FireBaseEventKey {
    //埋点--点击事件
    public static final String REGISTER_START="whoot_click_register_start";//【新用户注册】按钮
    public static final String REGISTER_BY_EMAIL="whoot_click_register_byEmail";//【以邮箱地址注册】按钮
    public static final String REGISTER_GO="whoot_click_register_go";//新用户注册_【注册】按钮
    public static final String FBLOGIN_SKIP="whoot_click_fbLogin_skip";//授权成功后_【跳过】按钮
    public static final String FBLOGIN_GO="whoot_click_fbLogin_go";//授权成功后_【确定】按钮
    public static final String REFER_HELP="whoot_click_me_refer_help";//邀请有礼_【帮助】按钮
    public static final String REFER_COPY="whoot_click_me_refer_copy";//邀请有礼_【复制】按钮
    public static final String REFER_SHARE="whoot_click_me_refer_refer";//邀请有礼_【分享】按钮
    public static final String REFER_MYTOKEN="whoot_click_me_refer_myToken";//邀请有礼_【查看我的Token】按钮
    public static final String KOL_KOL_POPUP_JOIN="whoot_click_KOL_popUp_join";//弹出框【进入活动】按钮
    public static final String KOL_KOL_RULES="whoot_click_KOL_rules";//KOL活动页面_【活动规则】按钮
    public static final String CLICK_TOKEN_UNUESD_INPUTBOX="whoot_click_token_unused_inputBox";//Token_未使用_活动码输入框
    public static final String CLICK_TOKEN_UNUESD_INPUTBOX_GO="whoot_click_token_unused_inputBox_Go";//Token_未使用_活动码【确定】按钮
    public static final String CLICK_TOKEN_UNUSED_USE="whoot_click_token_unused_use";//Token_未使用_【使用】按钮
    public static final String CLICK_DISCOVER_SEARCHBTN="whoot_click_discover_searchBtn";//探索_【搜寻】按钮
    public static final String CLICK_DISCOVER_SEARCHPAGE_SEARCHBTN="whoot_click_discover_searchPage_searchBtn";//探索_搜寻页_【搜寻】按钮
    public static final String CLICK_DISCOVER_FILTER="whoot_click_discover_filter";//探索_筛选按钮（多个）
    public static final String CLICK_DISCOVER_PRICEFILTERGO="whoot_click_discover_priceFilterGo";//探索_筛选按钮_价格范围【确定】按钮
    public static final String CLICK_DISCOVER_BANNER="whoot_click_discover_banner";//点击banner


    //埋点--曝光事件
    public static final String SHOW_REGISTER_CELLPHONE="whoot_show_register_cellphoneNum";//新用户注册_手机_输入手机号页
    public static final String SHOW_REGISTER_CELLPHONE_VERIFYCODE="whoot_show_register_cellphone_verifyCode";//新用户注册_手机_输入验证码页
    public static final String SHOW_REGISTER_CELLPHONE_INFO="whoot_show_register_cellphone_info";//新用户注册_手机_输入资料页
    public static final String SHOW_REGISTER_EMAIL_INFO="whoot_show_register_email_Info";//新用户注册_邮箱_输入资料页
    public static final String  SHOW_ME_REFER="whoot_show_me_refer";//我的_邀请有礼页面
    public static final String  SHOW_RESTAURANT_DETAIL="whoot_show_restaurantDetail";//进入餐厅详情页面
    public static final String SHOW_PRODUCT_DETAIL_REDEEM="whoot_show_productDetail_redeem";//进入兑换产品页面
    public static final String SHOW_SEARCHPAGE="whoot_show_searchpage";//页面曝光-搜索页面

    //埋点--动作事件
    public static final String EVENT_LAUNCH="whoot_event_launch";//完成app启动
    public static final String EVENT_LOGIN="whoot_event_login";//完成登录
}
