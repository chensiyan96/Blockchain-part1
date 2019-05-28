package com.blockchain.model;

import java.util.Date;

public class Agreement
{
	public int id;
	public String terms;
	public Date createTime;

	//A发起合同
	public int partyA;
	public int partyB;

	public AgreeStatus status;

	public int creditId;
}
