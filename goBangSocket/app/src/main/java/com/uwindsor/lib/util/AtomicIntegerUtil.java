package com.uwindsor.lib.util;
/**
 * 
 */


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sherry on 2017/3/15.
 */
public final class AtomicIntegerUtil {
	
	private static final AtomicInteger mAtomicInteger=new AtomicInteger();
	
	public static int  getIncrementID()
	{
		return mAtomicInteger.getAndIncrement();
	}
}
