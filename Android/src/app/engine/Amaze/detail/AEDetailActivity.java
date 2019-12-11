package app.engine.Amaze.detail;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import app.engine.Amaze.AEActivity;
import app.engine.Amaze.R;
import app.engine.common.IO;
import app.engine.common.UI;

public class AEDetailActivity extends AEActivity {

	private static final String	TAG		= AEDetailActivity.class.getSimpleName();
	private static final String	kURL	= "http://aeapp.oss-cn-hangzhou.aliyuncs.com/%d/detail.json";
	private static final String	kFile	= "detail.json";

	private int			mID;
	private Data		mData;
	private View		mHeadView;
	private ListView	mListView;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		((TextView) findViewById(R.id.app_title)).setText("详情");

		mID = getIntent().getIntExtra("ID", 0);
		resetDataWithJson(IO.textWithAsset(kFile));

		mHeadView = getLayoutInflater().inflate(R.layout.activity_detail_list_head, null);
		UI.setText(mHeadView, R.id.detail_list_head_title, mData.title);
		UI.setText(mHeadView, R.id.detail_list_head_datime, mData.datime);
		UI.setText(mHeadView, R.id.detail_list_head_source, mData.source);
		mImgLoader.displayImage(mData.image, (ImageView) mHeadView.findViewById(R.id.detail_list_head_image), mImgOption);

		mListView = (ListView) findViewById(R.id.detail_list);
		mListView.addHeaderView(mHeadView);
		mListView.setAdapter(mListAdapter);
		mListView.setOnScrollListener(new PauseOnScrollListener(mImgLoader, false, false));

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
				UI.setText(mHeadView, R.id.detail_list_head_title, mData.title);
				UI.setText(mHeadView, R.id.detail_list_head_datime, mData.datime);
				UI.setText(mHeadView, R.id.detail_list_head_source, mData.source);
				mImgLoader.displayImage(mData.image, (ImageView) mHeadView.findViewById(R.id.detail_list_head_image), mImgOption);
			}
		}.execute(String.format(kURL, mID));
	}

	private BaseAdapter mListAdapter = new BaseAdapter() {
		@Override
		public int getCount() {
			return mData.items.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.items.get(position);
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
			return mData.items.get(position).type;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			Item item = mData.items.get(position);
			boolean isTxt = item.type == 1;
			if (view == null) {
				if (isTxt) {
					view = getLayoutInflater().inflate(R.layout.activity_detail_list_item1, parent, false);
				}
				else {
					view = getLayoutInflater().inflate(R.layout.activity_detail_list_item2, parent, false);
				}
			}
			if (isTxt) {
				UI.setText(view, R.id.detail_list_item1_txt, item.text);
			}
			else {
				mImgLoader.displayImage(item.text, (ImageView) view.findViewById(R.id.detail_list_item2_img), mImgOption);
			}
			return view;
		}
	};

	private void resetDataWithJson(String str) {
		try {
			JSONObject json = new JSONObject(str);
			mData = Data.valueOf(json);
		}
		catch (Exception e) {
			Log.e(TAG, "resetDataWithJson:" + str, e);
		}
	}

	public static class Data {
		public int			ID;
		public String		image;
		public String		title;
		public String		source;
		public String		datime;
		public List<Item>	items	= new ArrayList<Item>();

		public static Data valueOf(JSONObject json) {
			Data data = new Data();
			data.ID = json.optInt("ID");
			data.image = json.optString("image");
			data.title = json.optString("title");
			data.source = json.optString("source");
			data.datime = json.optString("datime");
			JSONArray items = json.optJSONArray("items");
			for (int idx = 0, len = items.length(); idx < len; idx++) {
				JSONObject obj = items.optJSONObject(idx);
				data.items.add(Item.valueOf(obj));
			}
			return data;
		}
	}

	public static class Item {
		public int		type;																																																																																																																							 // 2:img
		public String	text;

		public static Item valueOf(JSONObject json) {
			Item item = new Item();
			item.type = json.optInt("type");
			item.text = json.optString("text");
			return item;
		}
	}
}
