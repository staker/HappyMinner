package com.ailink.pojo;

public class DailyMissionPojo {
    public int id;//
    public String name="";
    public String desc;
    public String path;
    public String status;//当前的状态，已经完成，  没完成  yes   no  即将开启 off

    public String click;//yes 可以点击，no是不可以点击

    public int addToken;//奖励的蓝钻数量
    public String iconUrl;

    public int type=1;//1是每日任务  2 基础任务


    public boolean showTips=false;
    public int tipsNumber;//显示红点还是数字

}
