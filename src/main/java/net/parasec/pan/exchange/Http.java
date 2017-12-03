package net.parasec.pan.exchange;

import java.util.Map;


public interface Http {
	String get(String url);
	String post(String url, Map<String, String> params);
}
