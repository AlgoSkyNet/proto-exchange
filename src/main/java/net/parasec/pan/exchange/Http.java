package net.parasec.pan.exchange;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.*;

import java.util.*;


// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e37

public class Http {

	private final HttpClient httpclient = HttpClients.createDefault();


  	public String post(String url, Map<String,String> paramMap) {
    		HttpPost httppost = new HttpPost(url);
    		httppost.setHeader("User-Agent", "_");

    		List<NameValuePair> params = new ArrayList<NameValuePair>();
    		for(String key : paramMap.keySet()) {
      			params.add(new BasicNameValuePair(key, paramMap.get(key)));
    		}
    
    		try {
      			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
 
      			HttpResponse response = httpclient.execute(httppost);
			if(response == null)
				return null;      			

			HttpEntity entity = response.getEntity();
			if(entity == null)
				return null;

      			java.io.InputStream is = entity.getContent();

      			try {
         			return convertStreamToString(is);
      			} finally {
        			is.close();
      			}
        
    		} catch(Exception e) {
      			e.printStackTrace();
    		}

    		return null;
  	}

  	public String get(String url) {
    		HttpGet httpget = new HttpGet(url);
    		httpget.setHeader("User-Agent", "_");

    		try {
      			HttpResponse response = httpclient.execute(httpget);
			
			if(response == null)
				return null;
			
      			HttpEntity entity = response.getEntity();
			if(entity == null)
				return null;

      			java.io.InputStream is = entity.getContent();

      			try {
          			return convertStreamToString(is);
      			} finally {
        			is.close();
      			}

    		} catch(Exception e) {
      			e.printStackTrace();
    		}

    		return null;
  	}

    	private String convertStreamToString(java.io.InputStream is) {
		final java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
    	}
}
