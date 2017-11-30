package net.parasec.pan.exchange;

import org.apache.log4j.Logger;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;


public class FakeBitstampExchange implements Exchange {
	private static final Logger LOG = Logger.getLogger(FakeBitstampExchange.class);     

	private String cid;
	private String key;
	private String sec;

	private int lastNonce = 0;

	private int fakeId = 31337;
	

	public FakeBitstampExchange(String cid, String key, String sec) {
		this.cid = cid;
		this.key = key;
		this.sec = sec;
	}

	public ExchangeOrderResponse limitOrder(String market, Direction direction, long volume, long price) {
		return new ExchangeOrderResponse(Integer.toString(fakeId++), "OK");
	}

	public ExchangeResponse cancel(int orderId) { 
		return new ExchangeResponse();
	}
}
