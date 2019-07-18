package com.app.whoot.util;

/**
 * Created by Sunrise on 4/19/2018.
 */

public class UrlUtil {



    //public static String Url="https://hk.whoot.com/whoot-local";    //正式环境
    public static String Url="https://qc.whoot.com/whoot-local";    //测试环境
    //public static String imgUrl="https://qc.whoot.com/filesvr/cos/file";     //腾讯图片地址
    public static String imgUrl="https://hk.whoot.com/gcs/file";      //google图片地址
    //public static String Terms="https://qc.whoot.com/portal/terms.html";           //服务条款
    //public static String Policy="https://qc.whoot.com/portal/policy.html";         //隐私政策
    public static String Terms="https://about.whoot.com/terms.html";           //服务条款
    public static String Policy="https://about.whoot.com/policy.html";         //隐私政策
    public static String Token_HK="https://about.whoot.com/token-cn.html";    //兑换说明
    public static String Token_US="https://about.whoot.com/token-en.html";
    public static String About_HK="https://about.whoot.com/about-cn.html";     //关于Whoot！
    public static String About_US="https://about.whoot.com/about-en.html";
    public static String Faq_HK="https://about.whoot.com/faq-cn.html";
    public static String Faq_US="https://about.whoot.com/faq-en.html";   //常見問題
    public static String ACTIVITY_HK="https://hk.whoot.com/events/kol.html?lang=cn";
    public static String ACTIVITY_US="https://hk.whoot.com/events/kol.html?lang=en";
    //kol活动
    public static String KOL_EVENT_HK="https://qc.whoot.com/web/user/event.html?lang=tc";
    public static String KOL_EVENT_US="https://qc.whoot.com/web/user/event.html?lang=en";

    //邀请有礼

    //public static String INVITE="https://hk.whoot.com/events/invite.html?code=";
    public static String INVITE="https://qc.whoot.com/web/user/invite.html?lang=";



    //kol活动
    public static String EVENTS="https://hk.whoot.com/events/event.html?lang=";


    //public static String Share="https://app.whoot.com/share.html?";      //hk分享链接
    public static String Share="https://app.whoot.com/static/share.html?";      //hk迁移后分享链接
    //public static String Share="https://qc.whoot.com/portal/share.html?";      //qc分享链接

    //public static String Configuration="https://cfg.ingcreations.com/whoot/whootserv_dev";
    public static String Configuration="https://qc.ingcreations.com/cfg/whoot/whootserv_dev";


    public static String Release="https://cfg.ingcreations.com/whoot/whootservers-and-release";  //HK迁移后动态获取服务器地址
    //public static String Release="https://qc.whoot.com/cfg/whoot/whootservers-business-qc";  //qc动态获取服务器地址

    public static String Google_maps="https://maps.googleapis.com/maps/api/place/textsearch/json?key=AIzaSyCnes2tlykl97JvjWKgAg-BeHBrrkhtkHM&query=";

  /* public static String shop="whoot-local/user/shop/get";    //获取门店
    public static String shop_info="whoot-local/user/shop/info";   //门店详细
    public static String shop_info_new="whoot-local/user/shop/v1.1/info";  //门店详情v1.1
    public static String search="whoot-local/user/shop/search";    //搜索门店
    public static String coupon="whoot-local/user/coupon/list";     //优惠卷列表
    public static String shop_fav="whoot-local/user/shop/fav";     //Top5门店
    public static String url_login="whoot-local/portal/login";       //用户登录
    public static String comments="whoot-local/user/comment";       //所有评论
    public static String profile="whoot-local/user/profile";         //更新账号信息
    public static String history="whoot-local/user/coupon/history"; //优惠卷使用历史
    public static String hotsearch="whoot-local/user/comm/hotsearch";  //热门搜索
    public static String check="whoot-local/user/checkUserName";  //用户敏感字检验
    public static String comme="whoot-local/user/shop/comments"; //用户门店评论
    public static String couponId="whoot-local/user/coupon/result/"; //查询扫码结果
    public static String comment="whoot-local/user/comment";  //用户评论
    public static String autocomplete="whoot-local/user/comm/autocomplete";  //联想自动补全
    public static String giftlist="whoot-local/user/gift/list";  //礼包列表
    public static String detail="whoot-local/user/gift/detail"; //礼包详情
    public static String expiredlist="whoot-local/user/gift/expiredList"; //过期礼包列表
    public static String historygift="whoot-local/user/gift/history";   //礼包兑换记录
    public static String resultgift="whoot-local/user/gift/result/"; //查询礼包兑换结果
    public static String resultgift_new="whoot-local/user/gift/v1.1/result/"; //查询礼包兑换结果v1.1
    public static String award="whoot-local/user/gift/award";    //分享成功，发放奖励
    public static String isemailreg="whoot-local/portal/isEmailReg"; //邮箱是否存在
    public static String login="whoot-local/portal/v1.1/login"; //用户登录v1.1
    public static String check_name="whoot-local/portal/checkUserName"; //用户名敏感词校验
    public static String bux_sum="whoot-local/user/bux/sum";//获取bux总积分
    public static String bux_user="whoot-local/user/bux";//获取积分详情列表
    public static String delete_favshop="whoot-local/user/favShop";//取消门店收藏
    public static String favshop="whoot-local/user/favShop";//收藏门店
    public static String list_favshop="whoot-local/user/favShop"; //门店列表
    public static String saveinfo="whoot-local/user/shop/bestReviewAndSavedInfo";//门店评论和收藏信息
    public static String logout="whoot-local/portal/logout"; //退出
    public static String gift_expiredList="whoot-local/user/gift/expiredList";//过期礼包列表
    public static String gift_expired="whoot-local/user/gift/expired";//删除所有过期礼包
    public static String coupon_expired="whoot-local/user/coupon/expired";//过期优惠券
    public static String resetPwdLink="whoot-local/portal/resetPwdLink"; //发送重设密码链接
    public static String getComments="whoot-local/user/comment/";   //用户评论编辑*/

    public static String shop="user/shop/get";    //获取门店
    public static String all_shop="user/shop/all";    //获取全部门店
    public static String shop_info="user/shop/info";   //门店详细
    public static String shop_info_new="user/shop/v1.1/info";  //门店详情v1.1
    public static String search="user/shop/search";    //搜索门店
    public static String coupon="user/coupon/list";     //Token列表
    public static String shop_fav="user/shop/fav";     //Top5门店
    public static String url_login="portal/login";       //用户登录
    public static String comments="user/comment";       //所有评论
    public static String profile="user/profile";         //更新账号信息
    public static String history="user/coupon/history"; //优惠卷使用历史
    public static String hotsearch="user/comm/hotsearch";  //热门搜索
    public static String check="user/checkUserName";  //用户敏感字检验
    public static String comme="user/shop/comments"; //用户门店评论
    public static String couponId="user/coupon/result/"; //查询扫码结果
    public static String comment="user/comment";  //用户评论
    public static String autocomplete="user/comm/autocomplete";  //联想自动补全
    public static String giftlist="user/gift/list";  //礼包列表
    public static String detail="user/gift/detail"; //礼包详情
    public static String expiredlist="user/gift/expiredList"; //过期礼包列表
    public static String historygift="user/gift/history";   //礼包兑换记录
    public static String resultgift="user/gift/result/"; //查询礼包兑换结果
    public static String resultgift_new="user/gift/v1.1/result/"; //查询礼包兑换结果v1.1
    public static String award="user/gift/award";    //分享成功，发放奖励
    public static String isemailreg="portal/isEmailReg"; //邮箱是否存在
    public static String login="portal/v1.1/login"; //用户登录v1.1
    public static String check_name="portal/checkUserName"; //用户名敏感词校验
    public static String bux_sum="user/bux/sum";//获取bux总积分
    public static String delete_favshop="user/favShop";//取消门店收藏
    public static String favshop="user/favShop";//收藏门店
    public static String list_favshop="user/favShop"; //门店列表
    public static String saveinfo="user/shop/commentAndSavedInfo";//门店评论和收藏信息
    public static String logout="portal/logout"; //退出
    public static String gift_expiredList="user/gift/expiredList";//过期礼包列表
    public static String gift_expired="user/gift/expired";//删除所有过期礼包
    public static String coupon_expired="user/coupon/expired";//过期优惠券
    public static String resetPwdLink="portal/resetPwdLink"; //发送重设密码链接
    public static String bux_user="/user/bux";//获取积分详情列表
    public static String getComments="/user/comment/";   //用户评论编辑
    public static String sign="user/sign";  //每日签到
    public static String ChangePwd="user/changePwd"; //修改密码   1023 旧密码不正确
    public static String Mobile_code="portal/mobile/code"; //发送手机验证码(注册)
    public static String Mobile_resetPwdCode="portal/resetPwdCode";//发送手机验证码(重置、忘记密码)
    public static String Verify="portal/mobile/verify"; //校验手机验证码
    public static String Forgot_Verify="portal/mobile/forgotPwd/verify";//校验手机验证码(忘记密码)
    public static String Mobile_password="portal/mobile/password";//忘记密码（手机号）
    public static String Email_password="portal/password";//忘记密码（邮箱）
    public static String IsMobileReg="portal/isMobileReg";//手机号是否被占用
    public static String Bind_Http="user/bind"; //绑定手机号码
    public static final String check_code="activity/checkCode";
    public static final String COMMODITY_INFO="commodity/shopLevel";
    public static String Check_Code="activity/checkCode"; //活动码/邀请码 校验
    public static String History="user/activity/invite/History"; //邀请有礼
    public static String TOKEN_REDEEM="activity/redeem";   //兑换邀请码/活动码
}
