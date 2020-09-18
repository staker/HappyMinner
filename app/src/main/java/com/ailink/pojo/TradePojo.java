package com.ailink.pojo;

/**
 * 这是用户交易的类，里面包含  用户买卖的信息，比如买矿工，矿工被买走等等
 */
public class

TradePojo {

    public long minerId;
    public long oldBossId;//旧主人的用户id

    public long newBossId;//新主人的用户id

    public String msgType;//消息类型  grab  你抢了别人的矿工    fire解雇矿工   snatch我的矿工被别人买走    activityInvite 邀请别人奖励   activityInvitees    被邀请
//    activityGiveToken  自己进入游戏通知    buy  买了个无主矿工  transaction    你被购买了    activityTemplate_1 活动   system  系统补偿
    public String msgContent;//如果是activityTemplate_1或者是system  消息类型，就需要现实这个字段内容
    public String title;//系统补偿，活动   就获取这个title  因为不确定


    public String minerName;//
    public String oldBossName;//旧主人的用户名
    public String newBossName;//旧主人的用户名




    public long minerCodeNew;//
    public long minerCodeOld;//
    public String minerNameOld;//原有被自己替换掉的矿工
    public String minerNameNew;//新替换进来的矿工


    public double usedToken;//买矿工的花费

    public double openMineAdd;//解雇矿工获得的钱  邀请矿工获得的钱   矿工被买走后收矿的钱

    public double getFee;//矿工被买走之后获得的钱（本金+5%）  我被别人买走了之后获得的钱


    public double newPrice;//你被买走了之后新的身价
    public double sendMine;//活动或者系统公告的奖励

    public String time;
}
