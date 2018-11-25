package com.locydragon.abf;

import java.math.BigInteger;

public class QueueJob {
	public static BigInteger nowID = new BigInteger("0");
	public String queueId;
	public String fileName;
	public boolean isSame(String id) {
		return id.equals(this.queueId);
	}
}
