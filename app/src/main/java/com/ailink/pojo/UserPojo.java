package com.ailink.pojo;

public class UserPojo {
//	public long id;//主键
	public long userId;//当前登录股票管家用户的id，(当前登录用户的id) （为多用户登录设置的）
	public String avatarUrl;//用户的头像的url（前面还要加上前缀url地址）

	public String userName;//用户的账户名（一般是手机号）
	public String userDesc;//用户的描述信息（属性）
	public String userNickName;


	public String refreshToken=null;//这个是用户的信息
	public String accessToken=null;
	public String userPhone;
	public String userTitle;//用户的称号（专属）不是每个用户都有的
	public String canAction;//对于这个矿工，我本人可以对他进行的操作    fire可以解雇他（代表他是我的矿工）  buy 可以购买他（我有矿工位，而且他不是我的矿工）
	//buy-可购买, fire-可解雇, master-主人, noMoney - 买不起, me - 自己,    replace -替换（当出现这个字段的时候 replaceNum 字段）
	public int replaceNum;//可替换的次数


	public String  minerStatus;//这个矿工的挖矿状态，stop已满， minering正在挖矿


	public double totalAsset;//总资产
	public double priceNew;//身价
	public double priceOld;//买入矿工时候的身价，即买入价


	public int sex;//1是男 2女的
	public int speed ;//算力
	public int speedGift ;//通过送礼获得的算力增加
	public int added;//需要溢价购买矿工时候所花费的溢价金额


	public int homeBangValue;//首页排行榜金额使用
	public String homeBangText;//对应value的文字描述可能是金额，也可能是算力
	public int homeValueRank;//排名


	public int pkgId;//用户的所在矿位置的包裹id
	public int minerPkgCount;//这个用户的矿工位
	public int tradeCount;//近24小时这个用户被交易的次数\
	public int tradeRank;//交易数量排名
	public int priceRank;//身价排名


	public int poolId;//所在的矿池id
	public  String poolName;//所在的矿池名字
	public String poolLogo;//所在的矿池头像





	public boolean review;//true 代表头像正在审核当中，false代表已经审核通过了
	public boolean priceDown;//是否需要溢价购买矿工，true的话是需要溢价购买
	public boolean canPlace;//是否可以把矿工放置到矿池



}
