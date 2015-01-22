package com.example.medicalcostsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EssentialInfo extends HttpActivity {
	private static final int INFO_LENGTH = 6;
	private static String[] info_name = new String[] { "性别：", "年龄：","手机号码：","本次诊断：","本次消费：","账户余额：" };
	private String[] info_content = new String[INFO_LENGTH] ;
	private String name;

	protected Button backToMain; // 返回按钮
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleByView(R.layout.activity_essential_info , R.layout.essential_title);//?
		backToMain = (Button) findViewById(R.id.backButton);
		backToMain.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EssentialInfo.this.finish();
			}
		});
		
		fetchJSON();
		TextView nameTx = (TextView) findViewById(R.id.user_name);
		nameTx.setText(name);
		TextView idTx = (TextView) findViewById(R.id.ID);
		idTx.setText(key);
		
		// 创建一个List集合，List集合的元素是Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < info_name.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("info_name", info_name[i]);
			listItem.put("info_content", info_content[i]);
			
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.essential_array_item, new String[] { "info_name",
						"info_content"}, new int[] { R.id.info_name, R.id.info_content});
		ListView list = (ListView) findViewById(R.id.info_listView);
		// 为ListView设置Adapter
		list.setAdapter(simpleAdapter);
	}

	@Override
	protected JSONObject getJSONObject() {
		JSONObject params = new JSONObject();
		try {
			params.put("user_id", "3234567890");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return params;
	}

	
	@Override
	protected String getUrl() {
		return "essentialinfo.php";
	}

	@Override
	protected void performOnJSON(JSONArray array) {
		JSONObject json = null;
		String result = null;
		try {
			json = array.getJSONObject(0);
			result = json.getString("result");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//msgi=json.getString("Num")+"*"+json.getString("Name")+"*"+json.getString("Class")+"*"+json.getString("Money")+"*"+json.getString("Permitted");
		Log.e(result, result);
		if (result.equals("success")) {
			try {
				try {
					json = array.getJSONObject(1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				name = json.getString("name");
				info_content[0] = json.getString("sex");
				info_content[1] = json.getString("age");
				info_content[2] = json.getString("phone");
				/*try {
					json = array.getJSONObject(2);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				
				info_content[3] ="感冒" ;//json.getString("latestInfo");
				info_content[4] = "1000";//json.getString("latestCost");
				info_content[5] = "2580";//json.getString("rmainingSum");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (result.equals("failed")) {
			Toast.makeText(EssentialInfo.this, "查询失败请重试", Toast.LENGTH_SHORT).show();
		}
		
	}
}
