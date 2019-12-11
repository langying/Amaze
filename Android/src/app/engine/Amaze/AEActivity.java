package app.engine.Amaze;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AEActivity extends Activity {

	protected final ImageLoader			mImgLoader;
	protected final DisplayImageOptions	mImgOption;

	public AEActivity() {
		mImgLoader = ImageLoader.getInstance();
		mImgOption = new DisplayImageOptions.Builder()			//
				.cacheOnDisk(true)								// 设置下载的图片是否缓存在SD卡中
				.cacheInMemory(true)							// 设置下载的图片是否缓存在内存中
				.considerExifParams(true)						//
				.bitmapConfig(Bitmap.Config.RGB_565)			// 设置图片的解码类型
				.showImageOnFail(R.drawable.app_icon)			// 设置图片加载/解码过程中错误时候显示的图片
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)	// 设置图片以如何的编码方式显示
				.build();										// 构建完成
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void setContentView(int resId) {
		super.setContentView(resId);
		enableBackBtn();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		enableBackBtn();
	}

	private void enableBackBtn() {
		View back = findViewById(R.id.app_back);
		if (back != null) {
			back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}
}
