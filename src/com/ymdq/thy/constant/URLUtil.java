package com.ymdq.thy.constant;

/**
 * 
 * <网络请求url地址>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class URLUtil
{
    //    public static final String SERVER = "http://mobile.myx8.cn/";
//    public static final String SERVER = "http://mobile.crossroad.love/";
    
                public static final String SERVER = "http://192.168.0.250:8080/community-mobile/";
    //public static final String SERVER = "http://10.167.130.144:8080/p_gateway/";
    /**
     * 分享地址
     * */
    public static final String SHARE_URL = SERVER + "index.html";
    
    /**
     * 隐私条款
     */
    public static final String UN_OPEN_URL = SERVER + "licenseAgreement.html";
    
    /**
     * 用户登陆
     */
    public static final String USER_LOGIN = "Bus400201";
    
    /**
     * 初始化
     */
    public static final String INIT = "Bus100101";
    
    /**
     * 请求轮播通告列表
     */
    public static final String LOOP_ADVERT = "Bus100201";
    
    /**
     * 新鲜事儿查询
     */
    public static final String FRESH_NEWS = "Bus100901";
    
    /**
     * 便民信息查询
     */
    public static final String MY_CENTRAL_LISTS = "Bus100801";
    
    /**
     * 我的账单—账单查询
     */
    public static final String ACCOUNT_MANAGET = "Bus101001";
    
    /**
     * 我的账单—缴费清单查询
     */
    public static final String PAYMENT_LIST = "Bus101101";
    
    /**
     * 我的消息查询
     */
    public static final String MY_MESSAGE = "Bus100701";
    
    /**
     * 邮包列表查询
     */
    public static final String MY_DELIVERTY = "Bus100601";
    
    /**
     * 工单列表查询
     */
    public static final String MY_TICKET_LSIT = "Bus200201";
    
    /**
     * 工单详情查询
     */
    public static final String MY_TICKET_DETAIL = "Bus200301";
    
    /**
     * 工单评价
     */
    public static final String MY_TICKET_EVALUATE = "Bus200401";
    
    /**
     * APP用户提醒（用于查询新消息等数量）
     */
    public static final String NO_READ_DELIVERTY = "Bus101201";
    
    /**
     * 轮播通告—详情查询
     */
    public static final String ADVERT_DETAIL = "Bus100301";
    
    /**
     * 轮播通告—赞&踩&分享
     */
    public static final String ADVERT_PRAISE_BLAME_SHARE = "Bus100401";
    
    /**
     * 轮播通告—评论
     */
    public static final String ADVERT_EVALUATE = "Bus100501";
    
    /**
     * 工单提交—报修&投诉&表扬
     */
    public static final String PROPERTY_REPAIR_COMMIT = "Bus200101";
    
    /**
     * 3.3.1    邻里—广场查询
     */
    public static final String COMMUNITY_SQUARE_QUERY = "Bus300101";
    
    /**
     * 3.3.2    邻里—社区列表查询
     */
    public static final String COMMUNITY_LIST_QUERY = "Bus300201";
    
    /**
     * 3.3.3   邻里—我关注的社区动态列表查询
     */
    public static final String COMMUNITY_DYNAMIC_JOINED = "Bus300301";
    
    /**
     * 3.3.12   话题—话题评论列表查询
     */
    public static final String COMMUNITY_COMMENT_LIST = "Bus301201";
    
    /**
     * 3.3.11   话题—话题评论
     */
    public static final String COMMUNITY_ADD_COMMENT = "Bus301101";
    
    /**
     * 3.3.4    社区—社区基本信息查询
     */
    public static final String COMMUNITY_DETAILINFO_QUERY = "Bus300401";
    
    /**
     * 3.3.5    社区—社区话题列表查询
     */
    public static final String COMMUNITY_TOPIC_LIST = "Bus300501";
    
    /**
     * 3.3.8   话题—话题点赞&取消赞
     */
    public static final String COMMUNITY_ADD_PRAISE = "Bus300801";
    
    /**
     * 3.3.9   话题—话题分享
     */
    public static final String COMMUNITY_SHARE_TOPIC = "Bus300901";
    
    /**
     * 3.3.7    话题—发表话题
     */
    public static final String COMMUNITY_POST_TOPIC = "Bus300701";
    
    /**
     * 3.3.15   用户—创建社区
     */
    public static final String COMMUNITY_CREATE = "Bus301501";
    
    /**
     * 3.3.17   用户—社区基本信息维护
     */
    public static final String COMMUNITY_EDIT = "Bus301701";
    
    /**
     * 3.3.6    社区—关注&取消关注
     */
    public static final String COMMUNITY_ADD_QUIT = "Bus300601";
    
    /**
     * 3.3.13   用户—用户基本信息查询
     */
    public static final String COMMUNITY_QUERY_PERSONINFO = "Bus301301";
    
    /**
     * 3.3.10   话题—话题投票
     */
    public static final String COMMUNITY_TOPIC_ARGEE = "Bus301001";
    
    /**
     * 3.3.19   话题—话题详情查询
     */
    public static final String COMMUNITY_TOPIC_DETAILS = "Bus301901";
    
    /**
     * 3.3.14   用户—用户话题列表查询
     */
    public static final String COMMUNITY_MY_TOPIC = "Bus301401";
    
    /**
     * 3.3.18   用户—话题置顶&取消置顶&删除
     */
    public static final String COMMUNITY_UPORDELETE_TOPIC = "Bus301801";
    
    /**
     * 3.3.16  用户—删除社区
     */
    public static final String COMMUNITY_DELETE_GROUP = "Bus301601";
    
    /**
     * 3.5.3    崩溃日志上传
     */
    public static final String UPLOAD_CRASH_FILE = "Bus500301";
    
    /**
     * 房屋查询
     */
    public static final String HOUSE_QUERY = "Bus401001";
    
    /**
     * 验证码
     */
    public static final String HOUSE_CODE = "Bus400301";
    
    /**
     * 绑定房屋
     */
    public static final String BINDING_HOUSE = "Bus401101";
    
    /**
     * 注册
     */
    public static final String USER_REGISTER = "Bus400101";
    
    /**
     * 验证用户名
     */
    public static final String USER_SURE_USERNAME = "Bus400801";
    
    /**
     * 设置新密码
     */
    public static final String USER_SET_NEW_PASSWORD = "Bus400901";
    
    /**
     * 修改密码
     */
    public static final String USER_MANAGE_PASSWORD = "Bus400701";
    
    /**
     * 个人信息
     */
    public static final String USER_PERSON_INFO = "Bus400401";
    
    /**
     * 账号绑定房屋信息查询
     */
    public static final String USER_BINDING_HOUSE_LIST = "Bus401201";
    
    /**
     * 解绑房屋
     */
    public static final String USER_UNBINDING_HOUSE = "Bus401501";
    
    /**
     * 头像上传
     */
    public static final String USER_UPLOAD_HEAD_PIC = "Bus400501";
    
    /**
     * 个人信息修改
     */
    public static final String USER_UPDATA_INFO = "Bus400601";
    
    /**
     * 房屋绑定账号查询
     */
    public static final String HOUSE_USER_INFO = "Bus401301";
    
    /**
     * 冻结恢复账号
     */
    public static final String HOUSE_USER_FROZEN = "Bus401401";
    
    /**
     * 版本更新检查
     */
    public static final String SET_UPDATE = "Bus500201";
    
    /**
     * 意见反馈
     */
    public static final String FEEDBACK = "Bus500101";
    
    /**
     * 小区检索
     */
    public static final String QUERY_COMMUNITY = "Bus101301";
    
    /**
     * 社区话题举报
     */
    public static final String REPORT = "Bus302201";
    
    /**
     * 社区-社区话题转移
     */
    public static final String TRANSLATE_TOPIC = "Bus302001";
    
    /**
     * 3.3.21   新回复消息列表 v1.1.0（新增）
     */
    public static final String COMMUNITY_COMMENT_MESSAGE = "Bus302101";
    
    //===================================   V2.0   ===================================
    
    /**
     * 3.2.5    表扬—信息列表查询 v2.0
     */
    public static final String BUS_200501 = "Bus200501";
    
    /**
     * 3.2.6    表扬—表扬接口 v2.0
     */
    public static final String BUS_200601 = "Bus200601";
    
    /**
     * 3.2.7    表扬—标签查询 v2.0
     */
    public static final String BUS_200701 = "Bus200701";
    
    /**
     * 3.2.8    表扬—评论列表查询 v2.0
     */
    public static final String BUS_200801 = "Bus200801";
    
    /**
     * 3.3.11社区—小区话题列表查询v2.0 
     */
    public static final String BUS_302301 = "Bus302301";

    /**
     * 3.3.1    话题—发表话题V2.0
     */
    public static final String BUS_300702 = "Bus300702";
    
    /**
     * 3.3.8    话题—话题详情查询V2.0
     */
    public static final String BUS301902 = "Bus301902";
    
    /**
     * 3.3.5    话题—话题评论列表查询V2.0
     */
    public static final String BUS301202 = "Bus301202";
    
    /**
     * 3.3.2    话题—话题点赞&取消赞V2.0
     */
    public static final String BUS300802 = "Bus300802";
    
    /**
     * 3.3.4    话题—话题评论&回复V2.0
     */
    public static final String BUS301102 = "Bus301102";
}
