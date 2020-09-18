package com.ailink.db;


/**
 * 存一些比较常用的数据
 */
public class ApplicationData {
    private static ApplicationData applicationData = null;
    public boolean isFreeze=false;//账号是否已经冻结
    public int noticeCount;//未读取公告的数量
    public boolean replaceFlag=true;
    public boolean isShowRecommendMiner=false;
    public int poolId;

    public int meTipsCount=0;//我的界面是否显示红点  0显示红点  大于0显示数字   小于0不显示红点
    public int missionTipsCount=0;//任务是否显示红点  0显示红点  大于0显示数字   小于0不显示红点

    public String waKuangBuff;//算力加成提示
    public String topSpeedBuff;//算力加成提示

    public String homeSpeedBuff;//首页的挖矿收益加成提示

    public String signStatusShow;//是否可以签到  yes  可以签到，no 不可以签到
    public boolean homeMsgShow=false;

//
//    public LiveContentPojo liveContentPojo;
//    public LoginContentPojo loginContentPojo;

    /**
     * 单例
     */
    private ApplicationData() {
        init();//初始化一些交易所的基本数据
    }

    public static ApplicationData getInstance() {
        if (applicationData == null) {
            applicationData = new ApplicationData();
        }
        return applicationData;
    }

    private void init() {
//        liveContentPojo=new LiveContentPojo();
//        loginContentPojo= new LoginContentPojo();

    }

}
