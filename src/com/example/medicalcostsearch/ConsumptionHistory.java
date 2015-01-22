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
import android.widget.Toast;

public class ConsumptionHistory extends HttpActivity {
	private static final int HISTORY_LENGTH = 110;

	protected Button backToMain; // 返回按钮
	/*我先用数组给你存着，封装数据对象你来如何？~~~~~*/
	private String[] times = new String[HISTORY_LENGTH];
	private String[] diagResults = new String[HISTORY_LENGTH] ;
	private String[] costs = new String[HISTORY_LENGTH];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleByView(R.layout.activity_consumption_history , R.layout.history_title);//?

		backToMain = (Button) findViewById(R.id.backButton);
		backToMain.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Intent mainIntent = new Intent(ConsumptionHistory.this,
				//		MainMenu.class);
				//ConsumptionHistory.this.startActivity(mainIntent);
				ConsumptionHistory.this.finish();
			}
		});

		/*
		 * ListView list = (ListView) findViewById(R.id.listView); // 定义一个数组
		 * String[] arr = {"2014/09/28             感冒                   500",
		 * "2014/10/06             发烧                   800",
		 * "2014/10/23             胆结石            2000"}; // 将数组包装ArrayAdapter
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>
		 * (this,R.layout.array_item, arr); // 为ListView设置Adapter
		 * list.setAdapter(adapter);
		 */
		fetchJSON();

		// 创建一个List集合，List集合的元素是Map
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < times.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
		//	listItem.put("header", imageIds[i]); //放入照片
			listItem.put("times", times[i]);
			listItem.put("diagResults", diagResults[i]);
			listItem.put("costs", costs[i]);
			listItems.add(listItem);
		}
		// 创建一个SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.history_array_item, new String[] { "times",
						"diagResults", "costs" }, new int[] { R.id.times, R.id.diagResults,
						R.id.costs });
		ListView list = (ListView) findViewById(R.id.listView);
		// 为ListView设置Adapter
		list.setAdapter(simpleAdapter);
		
		/*还需要为左上角的返回Button添加监听事件，返回到主界面*/
		
/*     不需要监听吧。只显示数据就好	但是点击item的时候还是会有灰色的痕迹，怎么破？？？？？！！！！！！！！！！！！！！！！
 	// 为ListView的列表项单击事件绑定事件监听器
		list.setOnItemClickListener(new OnItemClickListener() {
			// 第position项被单击时激发该方法。
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println(times[position] + "被单击了");
			}
		});
		
		list.setOnItemSelectedListener(new OnItemSelectedListener() {
			// 第position项被选中时激发该方法。
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println(times[position] + "被选中了");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});*/
	}
	
	@Override
	protected JSONObject getJSONObject() {
		JSONObject params = new JSONObject();
		try {
			params.put("user_id", key);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return params;
	}

	
	@Override
	protected String getUrl() {
		return "history.php";
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
			for (int i=1; i<array.length(); i++ ){
				try {
					json = array.getJSONObject(i);
					times[i-1] = json.getString("date");
					diagResults[i-1] = json.getString("disease");
					costs[i-1] = json.getString("cost");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (result.equals("failed")) {
			Toast.makeText(ConsumptionHistory.this, "查询失败请重试", Toast.LENGTH_SHORT).show();
		}
		
	}
}
