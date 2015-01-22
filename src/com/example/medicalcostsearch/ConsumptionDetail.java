package com.example.medicalcostsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;


public class ConsumptionDetail extends HttpActivity {

	protected Button backToMain; // ���ذ�ť
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleByView(R.layout.activity_consumption_detail , R.layout.cost_title);//?
	
		
		 /** ���ذ�ť*/ 
		backToMain = (Button) findViewById(R.id.backButton);
		backToMain.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Intent mainIntent = new Intent(ConsumptionDetail.this,
				//		MainMenu.class);
				//ConsumptionDetail.this.startActivity(mainIntent);
				//ConsumptionDetail.this.finish();
				ConsumptionDetail.this.finish();
			}
		});
		

		// ����һ��BaseExpandableListAdapter����
				ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
					private String[] costTypes = new String[] { "ҩƷ��      ",
							"��е��      ", "סԺ��      ", "�����      " };
					private String[][] costItem = new String[][] {
							{ "��Ī����  ", "��ù��      ", "��ð��      ", "ֹ�Ƚ���  " },
							{ "������      ", "ע����      ", "������      " },
							{ "2014/10/28", "2014/10/29", "2014/10/30" },
							{ "��β��������", "�����������" } };
					private String[] typePrice = new String[] { "103Ԫ", "65Ԫ", "400Ԫ",
							"540Ԫ" };
					private String[][] itemPrice = new String[][] {
							{ "23Ԫ", "37Ԫ", "25Ԫ", "18Ԫ" }, { "30Ԫ", "15Ԫ", "20Ԫ" },
							{ "100Ԫ", "100Ԫ", "100Ԫ" }, { "500Ԫ", "40Ԫ" } };

					/* �ô���ʵ��TextView */
					private TextView getTextView() {
						AbsListView.LayoutParams layout_params = new AbsListView.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						TextView textView = new TextView(ConsumptionDetail.this);
						textView.setLayoutParams(layout_params);
						textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
						textView.setPadding(60, 10, 10, 10);
						// textView.setBackgroundColor(Color.CYAN);
						textView.setTextColor(Color.BLACK);
						textView.setTextSize(18);
						return textView;
					}

					// ��ȡָ����λ�á�ָ�����б�������б�������
					@Override
					public Object getChild(int groupPosition, int childPosition) {
						return costItem[groupPosition][childPosition];
					}

					public Object getChild2(int groupPosition, int childPosition) {
						return itemPrice[groupPosition][childPosition];
					}

					@Override
					public long getChildId(int groupPosition, int childPosition) {
						return childPosition;
					}

					@Override
					public int getChildrenCount(int groupPosition) {
						return costItem[groupPosition].length;
					}

					public int getChildrenCount2(int groupPosition) {
						return itemPrice[groupPosition].length;
					}

					// �÷�������ÿ����ѡ������
					@Override
					public View getChildView(int groupPosition, int childPosition,
							boolean isLastChild, View convertView, ViewGroup parent) {
						LinearLayout liner_layout = new LinearLayout(
								ConsumptionDetail.this);
						liner_layout.setOrientation(0);

						TextView textView1 = getTextView();
						TextView textView2 = new TextView(ConsumptionDetail.this);
						textView2 = getTextView();
						textView1.setText(getChild(groupPosition, childPosition)
								.toString()
								+ "          "
								+ getChild2(groupPosition, childPosition).toString());

						liner_layout.addView(textView1);
						return liner_layout;
					}

					// ��ȡָ����λ�ô���������
					@Override
					public Object getGroup(int groupPosition) {
						return costTypes[groupPosition];
					}

					public Object getGroup2(int groupPosition) {
						return typePrice[groupPosition];
					}

					@Override
					public int getGroupCount() {
						return costTypes.length;
					}

					public int getGroupCount2() {
						return typePrice.length;
					}

					@Override
					public long getGroupId(int groupPosition) {
						return groupPosition;
					}

					// �÷�������ÿ����ѡ������
					@Override
					public View getGroupView(int groupPosition, boolean isExpanded,
							View convertView, ViewGroup parent) {
						LinearLayout liner_layout = new LinearLayout(
								ConsumptionDetail.this);
						liner_layout.setOrientation(0);

						TextView textView1 = getTextView();
						textView1.setText(getGroup(groupPosition).toString()
								+ "          " + getGroup2(groupPosition).toString());
						textView1.setBackgroundColor(0xffC5FCBF);
						// textView2.setText(getGroup2(groupPosition).toString());
						liner_layout.addView(textView1);
						return liner_layout;
					}

					@Override
					public boolean isChildSelectable(int groupPosition,
							int childPosition) {
						return true;
					}

					@Override
					public boolean hasStableIds() {
						return true;
					}
				};
				ExpandableListView expandListView = (ExpandableListView) findViewById(R.id.cost_list);
				expandListView.setAdapter(adapter);

			}
	
	@Override
	protected JSONObject getJSONObject() {
		JSONObject params = new JSONObject();
		try {
			params.put("key", key);
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
			Toast.makeText(ConsumptionDetail.this, "��ѯʧ��������", Toast.LENGTH_SHORT).show();
		}
	}
}
