package net.parasec.pan.exchange;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.*;

import java.util.*;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;


// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e37

// todo: try http://square.github.io/okhttp/


public class Http {

	private final HttpClient httpclient;


	public Http() {
		this.httpclient = new DefaultHttpClient(); //HttpClients.createDefault();
		// The cost of this operation is about 15-30 ms, depending on the JRE used. Disabling stale connection check may result in slight performance improvement, especially for small payload responses, at the risk of getting an I/O error when executing a request over a connection that has been closed at the server side
		// http://hc.apache.org/httpclient-3.x/performance.html
		this.httpclient.getParams().setParameter("http.connection.stalecheck", false);
	}

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

    	private String convertStreamToString(final InputStream is) throws Exception {
		//final java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
		//return s.hasNext() ? s.next() : "";
		// https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
		final ByteArrayOutputStream result = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) != -1) {
    			result.write(buffer, 0, len);
		}
		return result.toString("UTF-8");
    	}
}
