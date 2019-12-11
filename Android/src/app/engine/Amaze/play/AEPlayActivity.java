package app.engine.Amaze.play;

import java.net.URLEncoder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import app.engine.Amaze.AEActivity;
import app.engine.Amaze.R;
import app.engine.Amaze.web.AEWebActivity;
import app.engine.common.UI;

public class AEPlayActivity extends AEActivity implements View.OnClickListener {

	private static final String	kTAG	= AEPlayActivity.class.getSimpleName();
	private static final String	kURL	= "file:///asset/player.bundle/%d.js?src=%s";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		findViewById(R.id.play_btn0).setOnClickListener(this);
		findViewById(R.id.play_btn1).setOnClickListener(this);
		findViewById(R.id.play_btn2).setOnClickListener(this);
		UI.setText(this, R.id.play_input, "http://aeapp.oss-cn-hangzhou.aliyuncs.com/ts/yk.ts");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.play_btn0: {
				playVideo(0);
				break;
			}
			case R.id.play_btn1: {
				playVideo(1);
				break;
			}
			case R.id.play_btn2: {
				playVideo(2);
				break;
			}
			default: {
				Log.e(kTAG, "onClick unknown type.");
				break;
			}
		}
	}

	private void playVideo(int type) {
		String url = null;
		try {
			url = ((EditText) findViewById(R.id.play_input)).getText().toString();
			url = URLEncoder.encode(url, "UTF-8");
			Intent intent = new Intent();
			intent.setClass(this, AEWebActivity.class);
			intent.putExtra("url", String.format(kURL, type, url));
			startActivity(intent);
		}
		catch (Exception e) {
			Log.e(kTAG, "playVideo error:" + url, e);
		}
	}
}
