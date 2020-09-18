package com.ailink.i;
 /**
  * 
  * @author 小木桩（staker）
  * 这个接口用来返回一些简单的结果的回到，必须一般情况下执行一个接口  只需要知道是否执行成功，然后错误结果就可以，无需太多复杂的操作
  */
public abstract class ResultListener {

	public abstract  void onSucess(Object object);//必须实现的
	 /**
	  * 后台有数据返回，但是返回的数据有错误，比如传参不对，或者后台服务器忙碌 等等
	  * @param dataError  错误的字符串原因
	  */
	 public void onDataError(String dataError){

	 }
}
