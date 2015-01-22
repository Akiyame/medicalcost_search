package com.example.medicalcostsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

public class DrugDetail extends HttpActivity implements TextWatcher, OnClickListener {
	AutoCompleteTextView actv;
	
	protected Button backToMain; // 返回按钮
	private String[] drug_names = new String[] { "维C银翘片", "青霉素", "阿莫西林"};
	private String[] drug_ids = new String[] { "123456", "31123", "4134431"};
	private String[] prices = new String[] { "23", "25", "29"};
	private String[] functions = new String[] { "感冒", "消炎", "消炎" };
	private final String DATABASE_PATH = android.os.Environment		//定义一个路径，将会在sd卡创建math文件夹
            .getExternalStorageDirectory().getAbsolutePath()
            + "/medical";
	private AutoCompleteTextView entry;	//定义一个自动完成组件
	private final String DATABASE_FILENAME = "trial.db";	//数据库文件名
	private SQLiteDatabase database;   //定义一个数据库
	private Button search;	//声明button控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleByView(R.layout.activity_drug_detail , R.layout.drug_title);//?
		backToMain = (Button) findViewById(R.id.backButton);
		backToMain.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Intent mainIntent = new Intent(DrugDetail.this,
				//		MainMenu.class);
				//DrugDetail.this.startActivity(mainIntent);
				//DrugDetail.this.finish();
				DrugDetail.this.finish();
			}
		});
		
		database = openDatabase();		//打开数据库
		search = (Button) findViewById(R.id.searchButton);	
		entry = (AutoCompleteTextView) findViewById(R.id.auto_search);
		search.setOnClickListener(this);	//绑定监听器
		entry.addTextChangedListener(this);		//绑定监听器
		
		// 创建一个List集合，List集合的元素是Map
				List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
				Cursor cursor = database.rawQuery("select * from drug", null);
				startManagingCursor(cursor);
				cursor.moveToFirst();
				do {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("drug_names", cursor.getString(cursor.getColumnIndex("name")));
					listItems.add(listItem);
				} while (cursor.moveToNext());
				
				// 创建一个SimpleAdapter
				SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
						R.layout.drug_array_item, new String[] { 
								"drug_names","prices","functions" }, new int[] {
								R.id.drug_names,R.id.prices, R.id.functions,});
				ListView list = (ListView) findViewById(R.id.drug_listView);
				// 为ListView设置Adapter
				list.setAdapter(simpleAdapter);		
				
				/*// 创建一个ArrayAdapter，封装自动匹配的数组   可用于固定数组
				ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, drug_names);
				actv = (AutoCompleteTextView) findViewById(R.id.auto_search);
				// 设置Adapter
				actv.setAdapter(searchAdapter);*/
		}
	

	//为了将Cursor对象与AutoCompleteTextView组件绑定， DictionaryAdapter类必须从CursorAdapter类继承。
		public class MathAdapter extends CursorAdapter//AutoCompleteTextView所使用的自定义的Adapter类
		{
			private LayoutInflater layoutInflater;
			@Override
			//由于CursorAdapter类中的convertToString方法直接返回了Cursor对象的地址，因此，在MathAdapter类中必须覆盖convertToString方法，以返回当前选中的公式。
			//当选中AutoCompleteTextView组件中列表中某一个公式名后，系统会用convertToString方法的返回值来设置AutoCompleteTextView组件中的文本。因此，必须使用Cursor的getString来获得相应的字段值。
			public CharSequence convertToString(Cursor cursor)
			{
				return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("_id"));
				//CursorAdapter类中的convertToString方法的源代码：如果cursor不为null，返回Cursor对象的地址(cursor.toString())
				//return cursor == null ? "" : cursor.toString();
			}
			private void setView(View view, Cursor cursor)
			{
				TextView tvWordItem = (TextView) view;
				tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
			}
				@Override
			public void bindView(View view, Context context, Cursor cursor)//设置已经存在的列表项，该列表项已经生成了相应的组件对象
			{
				setView(view, cursor);
			}
				@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent)//负责设置新的列表项，在该方法中需要创建一个View对象来显示当前的列表项
			{
				View view = layoutInflater.inflate(R.layout.expression_list_item, null);
				setView(view, cursor);
				return view;
			}
			public MathAdapter(Context context, Cursor c, boolean autoRequery)
			{
				super(context, c, autoRequery);
				layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
		}
		
		public void afterTextChanged(Editable s){}//监视字符的输入情况（继承TextWatcher类的方法）
		public void beforeTextChanged(CharSequence s, int start, int count,int after){}//继承TextWatcher类的方法
		public void onTextChanged(CharSequence s, int start, int before, int count){
			//  必须将name字段的别名设为_id 
			Cursor cursor = database.rawQuery("select name as _id from drug where name like ?",new String[]{ s.toString() + "%" });
			startManagingCursor(cursor);
			MathAdapter mathAdapter = new MathAdapter(this,cursor, true);
			entry.setAdapter(mathAdapter);
		}//继承TextWatcher类的方法
		
		public void onClick(View view)
		{
			String sql = "select * from drug where name=?";		
			Cursor findCursor = database.rawQuery(sql, new String[]
			{ entry.getText().toString() });
			startManagingCursor(findCursor);
			String result = "未找到该公式.";
			//  如果查找公式名称，显示其公式名
			if (findCursor.getCount() > 0)
			{
				//  必须使用moveToFirst方法将记录指针移动到第1条记录的位置
				findCursor.moveToFirst();
				result = "药物名称:" + findCursor.getString(findCursor.getColumnIndex("name")) + "\n" +
						 "药物价格:" + findCursor.getString(findCursor.getColumnIndex("price")) + "\n" +
						 "药物作用:" + findCursor.getString(findCursor.getColumnIndex("funtion"));
			}
			//  显示查询结果对话框
			new AlertDialog.Builder(this).setTitle("查询结果").setMessage(result).setPositiveButton("关闭", null).show();
		}
			
	
		//复制并打开保存英文单词的数据库文件，系统自动创建/sdcard/math目录，并将res\raw目录中的main.db文件复制到/sdcard/dictionary目录中
		private SQLiteDatabase openDatabase()
		  {
			// 获得math.db文件的绝对路径
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
	        try
	        {
	            
	            // 如果/sdcard/math目录中存在，创建这个目录
	            if (!dir.exists())
	                dir.mkdir();
	            // 如果在/sdcard/math目录中不存在 math.db文件，则从res\raw目录中复制这个文件到SD卡的目录（/sdcard/math）
	            if (!(new File(databaseFilename)).exists())
	            {
	                // 获得封装math.db文件的InputStream对象。在程序第一次启动时将该文件复制到手机内存或SD卡的某个目录中，然后再打开该数据库文件。
	            	InputStream is = getResources().openRawResource(R.raw.drug);//复制的基本方法是使用getResources().openRawResource方法获得res\raw目录中资源的InputStream对象，
	                FileOutputStream fos = new FileOutputStream(databaseFilename);//然后将该InputStream对象中的数据写入其他的目录中相应文件中。
	                byte[] buffer = new byte[10000];
	                int count = 0;
	                // 开始复制math.db文件
	                while ((count = is.read(buffer)) > 0)
	                {
	                    fos.write(buffer, 0, count);
	                }
		            fos.close();
	                is.close();
	            }
	           
	        }
	        catch (Exception e){} // 打开/sdcard/math目录中的math.db文件
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
            return database;
	        //return null;
	     }

	
	@Override
	protected JSONObject getJSONObject() {
		JSONObject params = new JSONObject();
		try {
			params.put("dugName", key);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return params;
	}

	
	@Override
	protected String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void performOnJSON(JSONArray array) {
		JSONObject json = null;
		try {
			json = array.getJSONObject(0);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = null;
		try {
			result = json.getString("result");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//msgi=json.getString("Num")+"*"+json.getString("Name")+"*"+json.getString("Class")+"*"+json.getString("Money")+"*"+json.getString("Permitted");
		Log.e(result, result);
		if (result.equals("success")) {
			
		} else if (result.equals("failed")) {
			Toast.makeText(DrugDetail.this, "查询失败请重试", Toast.LENGTH_SHORT).show();
		}
	}
}
