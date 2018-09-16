package net.parasec.pan.exchange;

import org.apache.log4j.Logger;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;


public class BitstampExchange implements Exchange {
	private static final Logger LOG = Logger.getLogger(BitstampExchange.class);     

	private final Http http = new OKHttp();	

	private String cid;
	private String key;
	private String sec;

	private long lastNonce = System.currentTimeMillis();

	public BitstampExchange(String cid, String key, String sec) {
		this.cid = cid;
		this.key = key;
		this.sec = sec;
		LOG.info("new bitstamp-exchange connection. start_nonce = " + lastNonce);	
	}

	private String hash(String nonce, String cid, String sec, String key) {
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(sec.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			sha256_HMAC.update(nonce.getBytes());
			sha256_HMAC.update(cid.getBytes());
			sha256_HMAC.update(key.getBytes());
			byte[] hash = sha256_HMAC.doFinal();
			return String.format("%064x", new BigInteger(1, hash)).toUpperCase();
		} catch(Exception e) {
			return null;
		}
	}

	private String getNonce() {
		return Long.toString(lastNonce++);
	}

	private Map<String,String> initParams() {
		String nonce = getNonce();
		LOG.info("nonce = " + nonce);

		Map<String,String> params = new HashMap<String,String>();

		params.put("key", key);
		params.put("signature", hash(nonce, cid, sec, key));
		params.put("nonce", nonce);

		return params;
	}

	public ExchangeOrderResponse limitOrder(String market, Direction direction, long volume, long price) {
		if(market == null) 
			return new ExchangeOrderResponse(false, ExchangeError.NOT_SUPPORTED, "you must specify an instrument.");

		final String url;
		if(direction.equals(Direction.BID))
			url = "https://www.bitstamp.net/api/v2/buy/"+market+"/";
		else
			url = "https://www.bitstamp.net/api/v2/sell/"+market+"/";

		Map<String, String> params = initParams();

		// ----
		// note, for other pairs, need to switch on market type.
		// ----

		// cents to dollar
		String usd = String.format("%.2f", price*0.01);

		// satoshi to btc or photons to ltc
		String asset = String.format("%.8f", volume*0.00000001);
							  
		params.put("price", usd);
		params.put("amount", asset);

		String response = null;

		try {
			LOG.info(direction + " " + asset + " @ $" + usd);
			long l = System.currentTimeMillis();
			response = http.post(url, params);
			LOG.info("bitstamp api raw response: " + response + " (" + (System.currentTimeMillis() - l) + "ms)");
		} catch(Exception e1) {
			return new ExchangeOrderResponse(false, ExchangeError.CONNECTION_ERROR, e1.getMessage());
		}

		if(response == null)
			return new ExchangeOrderResponse(false, ExchangeError.UNKNOWN, "null response");
		try {
			if(response.contains("error")) {
				return new ExchangeOrderResponse(false, ExchangeError.UNEXPECTED_RESPONSE, response);
			} else {
				BitstampJSON.ExchangeOrder eo = BitstampJSON.parseExchangeOrder(response);
				return new ExchangeOrderResponse(Long.toString(eo.getId()), response);
			}
		} catch(Exception e2) {
			LOG.error(e2, e2);
			return new ExchangeOrderResponse(false, ExchangeError.UNEXPECTED_RESPONSE, response);
		}
	}

	private String attemptCancel(String orderId) throws Exception {
		final String url = "https://www.bitstamp.net/api/cancel_order/";
		final Map<String, String> params = initParams();
		params.put("id", orderId); 
		return http.post(url, params);
	}

	public ExchangeResponse cancel(String orderId) { 
		String resp = null;
		try {
			LOG.info("cancel " + orderId);
			long l = System.currentTimeMillis();
			resp = attemptCancel(orderId);
			LOG.info("bitstamp api raw response: " + resp + " (" + (System.currentTimeMillis() - l) + "ms)");
		} catch(Exception e1) {
			LOG.error(e1, e1);
			return new ExchangeResponse(false, ExchangeError.CONNECTION_ERROR, e1.getMessage());
		}
		if(resp == null) {
			return new ExchangeResponse(false, ExchangeError.UNKNOWN, "null response");		
		}
		// this can happen if try to cancel before bitstamp processes order
		// (they enqueue incomming orders)
		if("{\"error\": \"Order not found\"}".equals(resp)) {
			return new ExchangeResponse(false, ExchangeError.INVALID_ID, resp);
		}
		if(resp.contains("error")) {
			return new ExchangeResponse(false, ExchangeError.UNKNOWN, resp);
		}
		return new ExchangeResponse();
	}

	// not used yet - attempt cancel with with backoff/retry
	private boolean cancel(String orderId, int delay) throws Exception {
		String response = attemptCancel(orderId);
		if("true".equals(response)) {
	    		return true;
		}

		LOG.warn("attempt #1 to cancel " + orderId + " failed: " + response);

		try {
		
	    		int attempts = 39; //6.5 minutes sum(cumsum(rep(500,39)))/1000
	    		long holdOff = 0;

	    		do {
		    
				holdOff += delay;
				Thread.sleep(holdOff);

				response = attemptCancel(orderId);

				if("true".equals(response)) {
					return true;
				}

				LOG.warn("attempt #" + Integer.toString((41-attempts)) + " to cancel " + orderId + " failed: " + response);

	   	 	} while(--attempts > 0);

		} catch(final InterruptedException e) {
	    		Thread.currentThread().interrupt();
	    		throw e;
		}

		LOG.error("cancel failed. server response:");
		LOG.error(response);

		return false;
	}

}
