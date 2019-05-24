package com.blockchain.model;

import java.math.BigDecimal;
import java.util.Date;

public class Credit
{
	public int id;
	public BigDecimal money;
	public Date createTime;
	public Date deadline;

	//A掏钱给B
	public int partyA;
	public int partyB;

	//打借条的时候时0，还了是1，确认以及还了是2
	public int status;

}
