package com.ailink.constants;
/**
 * sava all const Integer
 * @author 小木桩（staker）
 */
public class ConstantInteger {


	/**
	 * 持仓的状态  活动状态和删除状态，卖出状态
	 */
	public interface PositionStatus{
		public static final int Status_Active = 1;//活动（默认）
		public static final int Status_Selled = 3;//已经被卖出
		public static final int Status_Delete = 2;//已经被删除，人为删除了	
	}

	
	/**
	 * 通用的状态  活动状态和删除状态
	 */
	public interface ActiveStatus{
		public static final int Status_Active = 1;//活动	
		public static final int Status_Delete = 2;//已经被删除	
	}

	
}
