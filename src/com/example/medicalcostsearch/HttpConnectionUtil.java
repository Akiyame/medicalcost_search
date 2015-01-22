package com.example.medicalcostsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

public class HttpConnectionUtil {
	public String ConnServerForResult(String url, JSONObject jsonObject) {// ArrayList
																	// nameValuePairs){
		// TODO Auto-generated method stub

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String result = null;

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
			Log.e("das", "dsaf111");
			HttpResponse response = httpClient.execute(httpPost);
			Log.e("das",  Integer.toString(response.getStatusLine().getStatusCode()));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Log.e("adsfdsafdsafads", nameValuePairs.toString());
				result = EntityUtils.toString(response.getEntity());
			}

		} 
		  catch (HttpHostConnectException e){
			 // Toast.makeText(Start, "µÇÂ½³É¹¦", Toast.LENGTH_SHORT).show();
		  }
		  catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	
	
}
