package com.blockchain.utils;

import com.blockchain.model.AgreeStatus;
import com.blockchain.model.FinancingStatus;

public class MStatusUtils
{

	/**
	 * return 1表示当前该甲进行，2表示当前该乙进行
	 */
	public static int agreeStatusDivision(AgreeStatus status)
	{
		if (status.equals(AgreeStatus.init) || status.equals(AgreeStatus.sign) || status
				.equals(AgreeStatus.repaid))
		{
			return 2;
		}else if(status.equals(AgreeStatus.shipped) || status.equals(AgreeStatus.comfirmship))
		{
			return 1;
		}
		return 0;
	}


	public static AgreeStatus getNextAgreeStatus(AgreeStatus status)
	{
		return AgreeStatus.values()[status.ordinal() + 1];
	}

	public static FinancingStatus getNextFinancingStatus(FinancingStatus status)
	{
		return FinancingStatus.values()[status.ordinal() + 1];
	}
}
