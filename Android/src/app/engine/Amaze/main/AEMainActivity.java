package app.engine.Amaze.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import app.engine.Amaze.AEActivity;
import app.engine.Amaze.R;
import app.engine.Amaze.detail.AEDetailActivity;
import app.engine.Amaze.play.AEPlayActivity;
import app.engine.Amaze.web.AEWebActivity;
import app.engine.common.IO;
import app.engine.common.UI;

@SuppressLint("InflateParams")
public class AEMainActivity extends AEActivity implements Runnable, View.OnClickListener {

	private static final String	TAG		= AEMainActivity.class.getSimpleName();
	private static final String	kURL	= "http://aeapp.oss-cn-hangzhou.aliyuncs.com/json/main.json";
	private static final String	kFile	= "main.json";
	private static final String	kName	= "data";

	private final Handler mHandler = new Handler();

	private Data		mData;
	private ListView	mListView;
	private ViewPager	mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.app_back).setVisibility(View.GONE);

		String text = IO.textWithSP(kFile, kName);
		if (TextUtils.isEmpty(text)) {
			text = IO.textWithAsset(kFile);
		}
		resetDataWithJson(text);

		View view = getLayoutInflater().inflate(R.layout.activity_main_head, null);
		view.findViewById(R.id.main_head_icon0).setOnClickListener(mOnIconClick);
		view.findViewById(R.id.main_head_icon1).setOnClickListener(mOnIconClick);
		view.findViewById(R.id.main_head_icon2).setOnClickListener(mOnIconClick);
		view.findViewById(R.id.main_head_icon3).setOnClickListener(mOnIconClick);
		mViewPager = (ViewPager) view.findViewById(R.id.main_list_head);
		mViewPager.setAdapter(mPagerAdapter);

		mListView = (ListView) findViewById(R.id.main_list);
		mListView.addHeaderView(view);
		mListView.setAdapter(mListAdapter);
		mListView.setOnScrollListener(new PauseOnScrollListener(mImgLoader, false, false));

		// 刷新数据
		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				return IO.textWithURL(params[0]);
			}

			@Override
			protected void onPostExecute(String json) {
				if (TextUtils.isEmpty(json)) {
					return;
				}
				resetDataWithJson(json);
				mListAdapter.notifyDataSetChanged();
				mPagerAdapter.notifyDataSetChanged();
			}
		}.execute(kURL);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.postDelayed(this, 5000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grants) {
		if (grants[0] == PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
		}
		else {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
				Toast.makeText(this, "请重试", Toast.LENGTH_SHORT).show();
			}
			else {
				// 表明用户彻底禁止弹出权限请求，那么调到设置页面吧
				Toast.makeText(this, "为了更好的效果，请打开摄像头权限", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:app.engine.Amaze")));
			}
		}
	}

	@Override
	public void run() {
		int idx = (mViewPager.getCurrentItem() + 1) % mPagerAdapter.getCount();
		mViewPager.setCurrentItem(idx, true);
		mHandler.postDelayed(this, 5000);
	}

	@Override
	public void onClick(View v) {
		String url = (String) v.getTag();
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if (Build.VERSION.SDK_INT >= 23) {
			int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
			if (permission != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 0);
				return;
			}
		}
		Intent intent = new Intent();
		intent.setClass(AEMainActivity.this, AEWebActivity.class);
		intent.putExtra("url", url);
		startActivity(intent);
	}

	private View.OnClickListener mOnIconClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.main_head_icon0: {
					Intent intent = new Intent();
					intent.setClass(AEMainActivity.this, AEWebActivity.class);
					intent.putExtra("url", "http://aeapp.oss-cn-hangzhou.aliyuncs.com/gallery/index.js");
					startActivity(intent);
					break;
				}
				case R.id.main_head_icon1: {
					Intent intent = new Intent();
					intent.setClass(AEMainActivity.this, AEPlayActivity.class);
					startActivity(intent);
					break;
				}
				case R.id.main_head_icon2: {
					Intent intent = new Intent();
					intent.setClass(AEMainActivity.this, AEDetailActivity.class);
					intent.putExtra("ID", 100);
					startActivity(intent);
					break;
				}
				case R.id.main_head_icon3: {
					Intent intent = new Intent();
					intent.setClass(AEMainActivity.this, AEWebActivity.class);
					intent.putExtra("url", "file:///asset/roller/index.js");
					startActivity(intent);
					break;
				}
				default: {
					break;
				}
			}
		}
	};

	private PagerAdapter mPagerAdapter = new PagerAdapter() {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return mData.head.size();
		}

		@Override
		public Object instantiateItem(ViewGroup parent, int position) {
			Item item = mData.head.get(position);
			View view = getLayoutInflater().inflate(R.layout.activity_main_head_item, parent, false);
			UI.setText(view, R.id.main_head_item_txt, null);
			UI.setOnClick(view, R.id.main_head_item_img, item.url, AEMainActivity.this);
			mImgLoader.displayImage(item.img, (ImageView) view.findViewById(R.id.main_head_item_img), mImgOption);
			parent.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup parent, int position, Object object) {
			parent.removeView((View) object);
		}
	};

	private BaseAdapter mListAdapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return mData.body.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.body.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return mData.body.get(position).line;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			Line item = mData.body.get(position);
			boolean isRow = item.line == 1;
			if (view == null) {
				if (isRow) {
					view = getLayoutInflater().inflate(R.layout.activity_main_list_item2, parent, false);
				}
				else {
					view = getLayoutInflater().inflate(R.layout.activity_main_list_item1, parent, false);
				}
			}
			if (isRow) {
				UI.setText(view, R.id.main_list_line_txt, item.item1.name);
			}
			else {
				UI.setText(view, R.id.main_list_item1_txt, item.item1.name);
				UI.setText(view, R.id.main_list_item2_txt, item.item2.name);
				UI.setOnClick(view, R.id.main_list_item1, item.item1.url, AEMainActivity.this);
				UI.setOnClick(view, R.id.main_list_item2, item.item2.url, AEMainActivity.this);
				mImgLoader.displayImage(item.item1.img, (ImageView) view.findViewById(R.id.main_list_item1_img), mImgOption);
				mImgLoader.displayImage(item.item2.img, (ImageView) view.findViewById(R.id.main_list_item2_img), mImgOption);
			}
			return view;
		}
	};

	private static class Item {
		public String	img;
		public String	url;
		public String	name;

		public static Item valueOf(JSONObject json) {
			Item item = new Item();
			item.img = json.optString("img");
			item.url = json.optString("url");
			item.name = json.optString("name");
			return item;
		}
	}

	private static class Line {
		public int	line;
		public Item	item1	= new Item();
		public Item	item2	= new Item();

		public static Line valueOf(JSONObject json) {
			Line item = new Line();
			item.line = json.optInt("line");
			item.item1.img = json.optString("img1");
			item.item2.img = json.optString("img2");
			item.item1.url = json.optString("url1");
			item.item2.url = json.optString("url2");
			item.item1.name = json.optString("name1");
			item.item2.name = json.optString("name2");
			return item;
		}
	}

	private static class Data {
		public final List<Item>	head	= new ArrayList<Item>();
		public final List<Line>	body	= new ArrayList<Line>();
	}

	private void resetDataWithJson(String str) {
		if (TextUtils.isEmpty(str)) {
			return;
		}
		try {
			Data data = new Data();
			JSONObject json = new JSONObject(str);
			JSONArray head = json.optJSONArray("head");
			for (int idx = 0, len = head.length(); idx < len; idx++) {
				JSONObject obj = head.getJSONObject(idx);
				data.head.add(Item.valueOf(obj));
			}
			JSONArray body = json.optJSONArray("body");
			for (int idx = 0, len = body.length(); idx < len; idx++) {
				JSONObject obj = body.getJSONObject(idx);
				data.body.add(Line.valueOf(obj));
			}
			mData = data;
			IO.commitSP(kFile, kName, json);
		}
		catch (Exception e) {
			Log.e(TAG, "jsonToItem() parse json error:" + str, e);
		}
	}
}
