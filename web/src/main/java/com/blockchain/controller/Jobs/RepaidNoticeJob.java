package com.blockchain.controller.Jobs;

import com.blockchain.service.MessageService;
import com.blockchain.utils.JSON;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class RepaidNoticeJob implements Job
{

	@Autowired
	private MessageService messageService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		JobDataMap dataMap = arg0.getJobDetail().getJobDataMap();
		JSON j = (JSON) dataMap.get("data");
		try
		{
			messageService.create(j.getInt("partyA"), j.getInt("partyB"),
					j.getString("msg"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
