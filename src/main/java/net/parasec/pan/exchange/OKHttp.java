package net.parasec.pan.exchange;

import org.apache.log4j.Logger;
import java.util.Map;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;


public class OKHttp implements Http {
	private static final Logger LOG = Logger.getLogger(OKHttp.class); 

	private final static String USER_AGENT = "_";

	private final OkHttpClient client;

	
	public OKHttp() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
		client = new OkHttpClient.Builder()
                		.retryOnConnectionFailure(true)
                        	.addInterceptor(logging)
                        	.build();
	}
	
	public String get(String url) {
		try {
			Request request = new Request.Builder()
					.url(url)
					.header("User-Agent", USER_AGENT)
					.build();
			Response response = client.newCall(request)
					.execute();
			return response.body().string();
		} catch(IOException e) {
			LOG.error(e, e);	
		}
		return null;
	}

	public String post(String url, Map<String, String> params) {
		try {
			FormBody.Builder builder = new FormBody.Builder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.add(entry.getKey(), entry.getValue());
			}
			RequestBody body = builder.build();
    			Request request = new Request.Builder()
					.url(url)
					.header("User-Agent", USER_AGENT)
					.post(body)
					.build();
   			Response response = client.newCall(request)
					.execute();
      			return response.body().string();
   		} catch(IOException e) {
			LOG.error(e, e);
		}
		return null; 
	}

}
