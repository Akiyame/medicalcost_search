package com.example.medicalcostsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


public abstract class HttpActivity extends Activity {
	static final String URL_HEAD = "http://192.168.0.104/android_connect/";
	protected String key = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//取得启动该Activity的Intent对象
        Intent intent =getIntent();
        /*取出Intent中附加的数据*/
		key = intent.getStringExtra("key");
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectAll().penaltyLog()
		.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
		.penaltyLog().penaltyDeath().build());	

	}
	
	protected abstract String getUrl();
	
	protected abstract JSONObject getJSONObject();
	
	protected abstract void performOnJSON(JSONArray array) ;
	
	protected void fetchJSON(){
		HttpConnectionUtil conn = new HttpConnectionUtil();
		String url = URL_HEAD + getUrl();
		String strResult = null;
		
		JSONObject params = getJSONObject();
		Log.e("adf", url);
		//if paras==NULL   
		strResult = conn.ConnServerForResult(url, params);
		
		Log.e("adf", strResult);
		JSONArray array = null;
		try {
			array = new JSONArray(strResult);
			Log.e("json", "123");
			performOnJSON(array);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//为页面添加标题栏
	protected void setTitleByView(int viewId, int titleId){
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(viewId);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				titleId);
	}
	
	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			fetchJSON();
		}
	}
}
