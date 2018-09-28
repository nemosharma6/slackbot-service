package util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
public class RestClient {

	public JSONObject get(String url) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("accept","application/json");
		HttpResponse response = httpClient.execute(httpGet);
		JSONObject entity = new JSONObject(EntityUtils.toString(response.getEntity()));
		return entity;
	}
	
	public static RestClient getInstance(){
		return new RestClient();
	}
	
	public String getTiny(String url) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://tinyurl.com/api-create.php?url=" + url);
		httpGet.addHeader("accept","application/json");
		HttpResponse response = httpClient.execute(httpGet);
		return EntityUtils.toString(response.getEntity());
	}
}
