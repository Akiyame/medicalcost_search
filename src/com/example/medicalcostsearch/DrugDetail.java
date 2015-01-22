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
	
	protected Button backToMain; // ���ذ�ť
	private String[] drug_names = new String[] { "άC����Ƭ", "��ù��", "��Ī����"};
	private String[] drug_ids = new String[] { "123456", "31123", "4134431"};
	private String[] prices = new String[] { "23", "25", "29"};
	private String[] functions = new String[] { "��ð", "����", "����" };
	private final String DATABASE_PATH = android.os.Environment		//����һ��·����������sd������math�ļ���
            .getExternalStorageDirectory().getAbsolutePath()
            + "/medical";
	private AutoCompleteTextView entry;	//����һ���Զ�������
	private final String DATABASE_FILENAME = "trial.db";	//���ݿ��ļ���
	private SQLiteDatabase database;   //����һ�����ݿ�
	private Button search;	//����button�ؼ�

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
		
		database = openDatabase();		//�����ݿ�
		search = (Button) findViewById(R.id.searchButton);	
		entry = (AutoCompleteTextView) findViewById(R.id.auto_search);
		search.setOnClickListener(this);	//�󶨼�����
		entry.addTextChangedListener(this);		//�󶨼�����
		
		// ����һ��List���ϣ�List���ϵ�Ԫ����Map
				List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
				Cursor cursor = database.rawQuery("select * from drug", null);
				startManagingCursor(cursor);
				cursor.moveToFirst();
				do {
					Map<String, Object> listItem = new HashMap<String, Object>();
					listItem.put("drug_names", cursor.getString(cursor.getColumnIndex("name")));
					listItems.add(listItem);
				} while (cursor.moveToNext());
				
				// ����һ��SimpleAdapter
				SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
						R.layout.drug_array_item, new String[] { 
								"drug_names","prices","functions" }, new int[] {
								R.id.drug_names,R.id.prices, R.id.functions,});
				ListView list = (ListView) findViewById(R.id.drug_listView);
				// ΪListView����Adapter
				list.setAdapter(simpleAdapter);		
				
				/*// ����һ��ArrayAdapter����װ�Զ�ƥ�������   �����ڹ̶�����
				ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_dropdown_item_1line, drug_names);
				actv = (AutoCompleteTextView) findViewById(R.id.auto_search);
				// ����Adapter
				actv.setAdapter(searchAdapter);*/
		}
	

	//Ϊ�˽�Cursor������AutoCompleteTextView����󶨣� DictionaryAdapter������CursorAdapter��̳С�
		public class MathAdapter extends CursorAdapter//AutoCompleteTextView��ʹ�õ��Զ����Adapter��
		{
			private LayoutInflater layoutInflater;
			@Override
			//����CursorAdapter���е�convertToString����ֱ�ӷ�����Cursor����ĵ�ַ����ˣ���MathAdapter���б��븲��convertToString�������Է��ص�ǰѡ�еĹ�ʽ��
			//��ѡ��AutoCompleteTextView������б���ĳһ����ʽ����ϵͳ����convertToString�����ķ���ֵ������AutoCompleteTextView����е��ı�����ˣ�����ʹ��Cursor��getString�������Ӧ���ֶ�ֵ��
			public CharSequence convertToString(Cursor cursor)
			{
				return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("_id"));
				//CursorAdapter���е�convertToString������Դ���룺���cursor��Ϊnull������Cursor����ĵ�ַ(cursor.toString())
				//return cursor == null ? "" : cursor.toString();
			}
			private void setView(View view, Cursor cursor)
			{
				TextView tvWordItem = (TextView) view;
				tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
			}
				@Override
			public void bindView(View view, Context context, Cursor cursor)//�����Ѿ����ڵ��б�����б����Ѿ���������Ӧ���������
			{
				setView(view, cursor);
			}
				@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent)//���������µ��б���ڸ÷�������Ҫ����һ��View��������ʾ��ǰ���б���
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
		
		public void afterTextChanged(Editable s){}//�����ַ�������������̳�TextWatcher��ķ�����
		public void beforeTextChanged(CharSequence s, int start, int count,int after){}//�̳�TextWatcher��ķ���
		public void onTextChanged(CharSequence s, int start, int before, int count){
			//  ���뽫name�ֶεı�����Ϊ_id 
			Cursor cursor = database.rawQuery("select name as _id from drug where name like ?",new String[]{ s.toString() + "%" });
			startManagingCursor(cursor);
			MathAdapter mathAdapter = new MathAdapter(this,cursor, true);
			entry.setAdapter(mathAdapter);
		}//�̳�TextWatcher��ķ���
		
		public void onClick(View view)
		{
			String sql = "select * from drug where name=?";		
			Cursor findCursor = database.rawQuery(sql, new String[]
			{ entry.getText().toString() });
			startManagingCursor(findCursor);
			String result = "δ�ҵ��ù�ʽ.";
			//  ������ҹ�ʽ���ƣ���ʾ�乫ʽ��
			if (findCursor.getCount() > 0)
			{
				//  ����ʹ��moveToFirst��������¼ָ���ƶ�����1����¼��λ��
				findCursor.moveToFirst();
				result = "ҩ������:" + findCursor.getString(findCursor.getColumnIndex("name")) + "\n" +
						 "ҩ��۸�:" + findCursor.getString(findCursor.getColumnIndex("price")) + "\n" +
						 "ҩ������:" + findCursor.getString(findCursor.getColumnIndex("funtion"));
			}
			//  ��ʾ��ѯ����Ի���
			new AlertDialog.Builder(this).setTitle("��ѯ���").setMessage(result).setPositiveButton("�ر�", null).show();
		}
			
	
		//���Ʋ��򿪱���Ӣ�ĵ��ʵ����ݿ��ļ���ϵͳ�Զ�����/sdcard/mathĿ¼������res\rawĿ¼�е�main.db�ļ����Ƶ�/sdcard/dictionaryĿ¼��
		private SQLiteDatabase openDatabase()
		  {
			// ���math.db�ļ��ľ���·��
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
	        try
	        {
	            
	            // ���/sdcard/mathĿ¼�д��ڣ��������Ŀ¼
	            if (!dir.exists())
	                dir.mkdir();
	            // �����/sdcard/mathĿ¼�в����� math.db�ļ������res\rawĿ¼�и�������ļ���SD����Ŀ¼��/sdcard/math��
	            if (!(new File(databaseFilename)).exists())
	            {
	                // ��÷�װmath.db�ļ���InputStream�����ڳ����һ������ʱ�����ļ����Ƶ��ֻ��ڴ��SD����ĳ��Ŀ¼�У�Ȼ���ٴ򿪸����ݿ��ļ���
	            	InputStream is = getResources().openRawResource(R.raw.drug);//���ƵĻ���������ʹ��getResources().openRawResource�������res\rawĿ¼����Դ��InputStream����
	                FileOutputStream fos = new FileOutputStream(databaseFilename);//Ȼ�󽫸�InputStream�����е�����д��������Ŀ¼����Ӧ�ļ��С�
	                byte[] buffer = new byte[10000];
	                int count = 0;
	                // ��ʼ����math.db�ļ�
	                while ((count = is.read(buffer)) > 0)
	                {
	                    fos.write(buffer, 0, count);
	                }
		            fos.close();
	                is.close();
	            }
	           
	        }
	        catch (Exception e){} // ��/sdcard/mathĿ¼�е�math.db�ļ�
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
			Toast.makeText(DrugDetail.this, "��ѯʧ��������", Toast.LENGTH_SHORT).show();
		}
	}
}
