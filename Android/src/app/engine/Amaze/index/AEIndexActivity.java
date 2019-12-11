package app.engine.Amaze.index;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import app.engine.Amaze.AEActivity;
import app.engine.Amaze.R;
import app.engine.Amaze.main.AEMainActivity;
import app.engine.common.Glob;

public class AEIndexActivity extends AEActivity {

	private static final int TIME = 2000;

	private final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				initSelf();
			}
		}, 500);
	}

	private void initSelf() {
		long start = System.currentTimeMillis();
		Glob.init(this);
		long dtime = TIME - (System.currentTimeMillis() - start);
		if (dtime <= 0) {
			openMain();
		}
		else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					openMain();
				}
			}, dtime);
		}
	}

	private void openMain() {
		startActivity(new Intent(this, AEMainActivity.class));
		finish();
	}
}
