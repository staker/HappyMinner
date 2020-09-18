package com.ailink.pojo;

public class PkgInfoPojo {

    public int status;//1代表收矿成功，其他代表错误
    public String errMsg;//收矿发生错误，错误信息

//    public double assetAdd;//获取到的矿石
//    public double assetNew;//现在用户本人的资产



    public String addToken;//收到矿的数量
    public String tokenType;//矿的种类  可能是蓝钻也可能是ETH
    public int poolId;//矿池的id


}
