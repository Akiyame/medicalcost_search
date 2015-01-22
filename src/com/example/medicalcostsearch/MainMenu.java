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

	/* 以下是滑动图片的变量 */
	private ViewPager viewPager;
	private ImageView[] tips;// 装点点的ImageView数组
	private ImageView[] mImageViews;// 装ImageView数组
	private int[] imgIdArray;// 图片资源id数组
	private String key = null;
	private String name = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取得启动该Activity的Intent对象
        Intent intent =getIntent();
        /*取出Intent中附加的数据*/
		key = intent.getStringExtra("key");
		key = "t1";
		name = intent.getStringExtra("name");
		
		// 为main界面添加标题栏
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main_menu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title);

		/* 以下是viewPage的实现代码 */
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		// 载入图片资源ID
		imgIdArray = new int[] { R.drawable.item01, R.drawable.item02,
				R.drawable.item03,R.drawable.item04 };
		// 将点点加入到ViewGroup中
		tips = new ImageView[imgIdArray.length];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
	                10, 10);
			//方法的四个参数都是设置视图到它的父视图的边框距离
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
		// 将图片装载到数组中
		mImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdArray[i]);
		}
		// 设置Adapter
		viewPager.setAdapter(new MyViewPageAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		viewPager.setCurrentItem((mImageViews.length) * 100);

		
		/* 以下为gridview代码 */
		GridView gv = (GridView) findViewById(R.id.grid_view);
		// 为GridView设置适配器
		gv.setAdapter(new MyGridViewAdapter(this));
		// 注册监听事件
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

/*y以下为viewpage的适配器*/
//如果继承pageradapter，至少必须重写下面的四个方法 1 instantiateItem(ViewGroup, int)
public class MyViewPageAdapter extends PagerAdapter {

	// 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	// 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mImageViews[position
				% mImageViews.length]);

	}

	/* 载入图片进去，用当前的position 除以 图片数组长度取余数是关键*/
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

/* 设置选中的tip的背景*/
private void setImageBackground(int selectItems) {
	for (int i = 0; i < tips.length; i++) {
		if (i == selectItems) {
			tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
		} else {
			tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
		}
	}
}



// 以下为gridview的适配器
class MyGridViewAdapter extends BaseAdapter {
	private Context context;// 上下文对象
	// 图片数组
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

	// 创建GridView的View方法
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));// 设置ImageView对象布局
			imageView.setAdjustViewBounds(false);// 设置边界对齐
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 设置刻度的类型
			imageView.setPadding(3, 3, 3, 3);// 设置间距
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(imgs[position]);// 为ImageView设置图片资源
		return imageView;
	}

}
}
