package net.parasec.pan.exchange;

import com.google.gson.Gson;


public class BitstampJSON {
	private final static com.google.gson.Gson GSON = new com.google.gson.Gson();

  	public static final class ExchangeOrder {
        	private double price;
        	private double amount;
        	private int type;
        	private int id;
        	private String datetime;

        	private String orig;

        	public ExchangeOrder(){}

        	public double getPrice() {
            		return price;
        	}

        	public double getAmount() {
            		return amount;
        	}

        	public int getType() {
            		return type;
        	}

        	public int getId() {
            		return id;
        	}

        	public String getDateTime() {
            		return datetime;
        	}

        	public String getOrig() {
            		return orig;
        	}

        	public ExchangeOrder setOrig(final String orig) {
            		this.orig = orig;
            		return this;
        	}
  	}

  	public static BitstampJSON.ExchangeOrder parseExchangeOrder(final String s) {
    		return GSON.fromJson(s, BitstampJSON.ExchangeOrder.class);
  	}
}
