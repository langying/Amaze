package app.engine.Amaze.photo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import app.engine.Amaze.AEActivity;
import app.engine.Amaze.R;
import app.engine.common.UI;
import uk.co.senab.photoview.PhotoView;

/**
 * http://wuxiaolong.me/2015/09/14/PhotoView/
 *
 * @author hanqiong
 */
public class AEPhotoActivity extends AEActivity {

	private static final String TAG = AEPhotoActivity.class.getSimpleName();

	private ViewPager	mViewPager;
	private List<Item>	mItems	= new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initItems(getIntent().getStringExtra("json"));
		setContentView(R.layout.activity_photo);

		mViewPager = (ViewPager) findViewById(R.id.main_list_head);
		mViewPager.setOffscreenPageLimit(2);
		if (Integer.parseInt("1") < 0) {
			mViewPager.setPageTransformer(true, new Transformer(0.8f));
		}
		mViewPager.setAdapter(mPagerAdapter);
	}

	private void initItems(String str) {
		if (TextUtils.isEmpty(str)) {
			return;
		}
		try {
			mItems.clear();
			JSONArray array = new JSONArray(str);
			for (int idx = 0, len = array.length(); idx < len; idx++) {
				JSONObject item = array.getJSONObject(idx);
				mItems.add(Item.valueOf(item));
			}
			mPagerAdapter.notifyDataSetChanged();
		}
		catch (Exception e) {
			Log.e(TAG, "initItems parse json error:" + str, e);
		}
	}

	private PagerAdapter mPagerAdapter = new PagerAdapter() {
		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			return view == arg1;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object instantiateItem(ViewGroup parent, int position) {
			Item item = mItems.get(position);
			View view = getLayoutInflater().inflate(R.layout.activity_photo_item, parent, false);
			UI.setText(view, R.id.photo_item_txt, item.txt);
			final ProgressBar progress = (ProgressBar) view.findViewById(R.id.photo_item_progress);
			mImgLoader.displayImage(item.img, (PhotoView) view.findViewById(R.id.photo_item_img), mImgOption, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String uri, View arg1) {
					progress.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String uri, View arg1, FailReason arg2) {
					progress.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String uri, View arg1, Bitmap arg2) {
					progress.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingCancelled(String uri, View arg1) {
					progress.setVisibility(View.GONE);
				}
			});
			parent.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup parent, int position, Object object) {
			parent.removeView((View) object);
		}
	};

	private static class Item {
		public String	img;
		public String	txt;

		public static Item valueOf(JSONObject json) {
			Item item = new Item();
			item.img = json.optString("img");
			item.img = json.optString("txt");
			return item;
		}
	}

	private static class Transformer implements ViewPager.PageTransformer {

		private final float scaleAmount;

		public Transformer(float scalingStart) {
			scaleAmount = 1 - scalingStart;
		}

		@Override
		public void transformPage(View page, float position) {
			if (position >= 0f) {
				final int w = page.getWidth();
				float scaleFactor = 1 - scaleAmount * position;
				page.setAlpha(1 - position);
				page.setScaleX(scaleFactor);
				page.setScaleY(scaleFactor);
				page.setTranslationX(w * (1 - position) - w);
			}
		}
	}
}
