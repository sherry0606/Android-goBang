package com.uwindsor.lib.service;

import com.uwindsor.lib.util.AtomicIntegerUtil;

/**
 * Created by Sherry on 2017/3/15.
 */
public class Packet {
	
	private int id=AtomicIntegerUtil.getIncrementID();
	private byte[] data;
	
	public int getId() {
		return id;
	}

	public void pack(String txt)
	{
		data=txt.getBytes();
	}
	
	public byte[] getPacket()
	{
		return data;
	}
}
