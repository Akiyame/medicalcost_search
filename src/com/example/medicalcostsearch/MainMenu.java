package com.example.medicalcostsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainMenu extends Activity implements OnPageChangeListener{

	/* �����ǻ���ͼƬ�ı��� */
	private ViewPager viewPager;
	private ImageView[] tips;// װ����ImageView����
	private ImageView[] mImageViews;// װImageView����
	private int[] imgIdArray;// ͼƬ��Դid����
	private String key = null;
	private String name = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȡ��������Activity��Intent����
        Intent intent =getIntent();
        /*ȡ��Intent�и��ӵ�����*/
		key = intent.getStringExtra("key");
		key = "t1";
		name = intent.getStringExtra("name");
		
		// Ϊmain������ӱ�����
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main_menu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);

		/* ������viewPage��ʵ�ִ��� */
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		// ����ͼƬ��ԴID
		imgIdArray = new int[] { R.drawable.item01, R.drawable.item02,
				R.drawable.item03,R.drawable.item04 };
		// �������뵽ViewGroup��
		tips = new ImageView[imgIdArray.length];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	                10, 10);
			//�������ĸ���������������ͼ�����ĸ���ͼ�ı߿����
	        layoutParams.setMargins(5, 280, 5, 0);
			imageView.setLayoutParams(layoutParams);
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageView);
		}
		// ��ͼƬװ�ص�������
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}
		// ����Adapter
		viewPager.setAdapter(new MyViewPageAdapter());
		// ���ü�������Ҫ�����õ��ı���
		viewPager.setOnPageChangeListener(this);
		// ����ViewPager��Ĭ����, ����Ϊ���ȵ�100���������ӿ�ʼ�������󻬶�
		viewPager.setCurrentItem((mImageViews.length) * 100);

		
		/* ����Ϊgridview���� */
		GridView gv = (GridView) findViewById(R.id.grid_view);
		// ΪGridView����������
		gv.setAdapter(new MyGridViewAdapter(this));
		// ע������¼�
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//Toast.makeText(MainMenu.this, "pic" + position,
				//		Toast.LENGTH_SHORT).show();
				if(position == 0){
					skipTo(EssentialInfo.class);
				} else if (position == 1){
					skipTo(ConsumptionDetail.class);
				} else if (position == 2){
					skipTo(DrugDetail.class);
				} else if (position == 3){
					skipTo(ConsumptionHistory.class);
				} 
			}
		});
	}
	
	private void skipTo(Class<? extends Activity> claz){
		Log.e("skpip11","aaa");
		Intent intent= new Intent();
		intent.setClass(MainMenu.this,claz);
		intent.putExtra("key", key);  
		MainMenu.this.startActivity(intent);
	}

/*y����Ϊviewpage��������*/
//����̳�pageradapter�����ٱ�����д������ĸ����� 1 instantiateItem(ViewGroup, int)
public class MyViewPageAdapter extends PagerAdapter {

	// ��ȡҪ�����Ŀؼ��������������������Ի����Ĺ����Ϊ������ô�����Ӧ����չʾ�Ĺ��ͼƬ��ImageView����
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	// ���ж���ʾ���Ƿ���ͬһ��ͼƬ���������ǽ�����������ȽϷ��ؼ���
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// PagerAdapterֻ��������Ҫ��ʾ��ͼƬ�����������ͼƬ�����˻���ķ�Χ���ͻ���������������ͼƬ����
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mImageViews[position
				% mImageViews.length]);

	}

	/* ����ͼƬ��ȥ���õ�ǰ��position ���� ͼƬ���鳤��ȡ�����ǹؼ�*/
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mImageViews[position
				% mImageViews.length], 0);
		return mImageViews[position % mImageViews.length];
	}
}

@Override
public void onPageScrollStateChanged(int arg0) {

}

@Override
public void onPageScrolled(int arg0, float arg1, int arg2) {

}

@Override
public void onPageSelected(int arg0) {
	setImageBackground(arg0 % mImageViews.length);
}

/* ����ѡ�е�tip�ı���*/
private void setImageBackground(int selectItems) {
	for (int i = 0; i < tips.length; i++) {
		if (i == selectItems) {
			tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
		} else {
			tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
		}
	}
}



// ����Ϊgridview��������
class MyGridViewAdapter extends BaseAdapter {
	private Context context;// �����Ķ���
	// ͼƬ����
	private Integer[] imgs = { R.drawable.diagnose, R.drawable.cost,
			R.drawable.medicine, R.drawable.history };

	MyGridViewAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return imgs.length;
	}

	public Object getItem(int item) {
		return item;
	}

	public long getItemId(int id) {
		return id;
	}

	// ����GridView��View����
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));// ����ImageView���󲼾�
			imageView.setAdjustViewBounds(false);// ���ñ߽����
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// ���ÿ̶ȵ�����
			imageView.setPadding(3, 3, 3, 3);// ���ü��
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(imgs[position]);// ΪImageView����ͼƬ��Դ
		return imageView;
	}

}
}
