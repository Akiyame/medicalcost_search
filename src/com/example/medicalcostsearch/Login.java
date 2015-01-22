package com.example.medicalcostsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends HttpActivity {
	EditText idNum;
	EditText password;
	String msgi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleByView(R.layout.activity_login , R.layout.login_title);
		
		Button myButton = (Button) findViewById(R.id.sign_in_button);
		idNum = (EditText) findViewById(R.id.idNum);
		password = (EditText) findViewById(R.id.password);
		myButton.setOnClickListener(new MyButtonListener());
		//Log.e("login", "begin2");
	}
	
	@Override
	protected JSONObject getJSONObject(){
		JSONObject params = new JSONObject();
		try {
			params.put("loginname", idNum.getText().toString());
			params.put("loginpassword", password.getText().toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return params;
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
		String key = null;
		String name = null;
		//msgi=json.getString("Num")+"*"+json.getString("Name")+"*"+json.getString("Class")+"*"+json.getString("Money")+"*"+json.getString("Permitted");
		Log.e(result, result);
		if (result.equals("success")) {
			Log.e("success", "!");
			try {
				key = json.getString("Num");
				name = json.getString("Name");
				Log.e("Num", key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
			Intent intent= new Intent();
			intent.setClass(Login.this, MainMenu.class);
			intent.putExtra("name", name);  
			intent.putExtra("key", key);  
			Login.this.startActivity(intent);
			Login.this.finish();
		} else if (result.equals("failed")) {
			Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected String getUrl() {
		return  "login.php";
	}
}
