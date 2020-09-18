package com.ailink.pojo;

import java.util.ArrayList;

/**
 * 称号实体类
 */
public class PoolPojo {
    public int poolId;//矿池的id
    public String poolName;//矿池名字

    public String mainOutput;//主要产出
    public String poolLogo;//矿池的LOGO

    public boolean open;//是否处于开放状态(对于本人是否开始)

    public int  emptyCount;//当前空位置数量（后面需要放一个空的矿工位置图标，有几个显示几个）


    public ArrayList<UserPojo> listMiner;//放置在这个矿池里面的矿工

    public ArrayList<RequirePojo> listRequire;//矿工还没有开启这个矿池的时候，会显示这个内容
    public ArrayList<RequirePojo> listPlaceRequire;//放置矿工的条件

    public String status;//矿池的开放状态，适用给所有人看到的   begin还没开始  open已经开始  end已经结束  queue正在排队



    public int online;// 暂时不用


    public String mineSum;//总的产出量
    public String mineLeave;//剩余产量
    public String beginTimeQueue;//排队时间
    public String beginTime;//开始时间
    public String endTime;//结束时间
    public String ratio;//"万算力/小时：43120LAN  之前叫做  矿池的总算力


}
