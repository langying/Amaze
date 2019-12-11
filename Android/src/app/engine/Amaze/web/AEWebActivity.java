package app.engine.Amaze.web;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import app.engine.AEView;
import app.engine.Amaze.AEActivity;
import app.engine.Amaze.R;

public class AEWebActivity extends AEActivity {

	private AEView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		String url = getIntent().getStringExtra("url");
		if (TextUtils.isEmpty(url)) {
			url = "file:///asset/player/index.js?type=1&src=http://aeapp.oss-cn-hangzhou.aliyuncs.com/360/map.jpg";
		}

		mView = (AEView) findViewById(R.id.web_view);
		mView.loadURL(url);
		mView.setCallback(new AEView.Callback() {
			@Override
			public void onFPS(final int fps) {
				Log.e("AEActivity", fps + "fps");
			}
		});

		findViewById(R.id.web_mode).setVisibility(View.VISIBLE);
		findViewById(R.id.web_mode).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mView.evalute("effect.requestPresent();");
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		mView.onResume();
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mView.onPause();
		getWindow().clearFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mView.onLowMemory();
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// mView.evalute("tttt();");
	// return false;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

}
