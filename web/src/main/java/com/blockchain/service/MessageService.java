package com.blockchain.service;

import com.blockchain.dao.MessageMapper;
import com.blockchain.model.Message;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService
{

	@Autowired
	private MessageMapper messageMapper;

	public int create(int partyA, int partyB, String msg)
			throws Exception
	{
		Message m = new Message();
		m.createTime = new Date();
		m.msg = msg;
		m.partyA = partyA;
		m.partyB = partyB;
		messageMapper.insertMessage(m);

		if (m.id == 0)
		{
			throw new Exception("合同创建错误");
		}
		return m.id;
	}

	public Message getMessage(int id)
	{
		return messageMapper.getMessage(id);
	}

	public List<Message> getMessageRec(int uid)
	{
		return messageMapper.getMessages(uid);
	}

	public void updateStatus(int id)
	{
		messageMapper.updateStatus(1, id);
	}

}
