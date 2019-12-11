package app.engine.manager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import app.engine.Amaze.R;
import app.engine.common.App;
import app.engine.common.Glob;
import app.engine.common.IO;
import app.engine.common.UI;

@SuppressLint({ "InflateParams", "UseSparseArrays" })
public class UpgradeManager extends BroadcastReceiver {

	private static final String	kTAG	= UpgradeManager.class.getSimpleName();
	private static final String	kURL	= "http://aeapp.oss-cn-hangzhou.aliyuncs.com/json/app.json";

	private static UpgradeManager instance;

	public static UpgradeManager sharedInstance() {
		if (instance == null) {
			instance = new UpgradeManager();
		}
		return instance;
	}

	private final Map<Long, String> mTaskIDs = new HashMap<Long, String>();

	private ProgressDialog mLoading;

	private UpgradeManager() {
		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		Glob.application.registerReceiver(this, filter);
	}

	@Override
	public void onReceive(Context ctx, Intent download) {
		long ID = download.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
		if (!mTaskIDs.containsKey(ID)) {
			return;
		}
		DownloadManager mgr = App.getService(DownloadManager.class);
		Cursor cursor = mgr.query(new Query().setFilterById(ID));
		if (cursor.moveToFirst()) {
			int idx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
			if (cursor.getInt(idx) == DownloadManager.STATUS_SUCCESSFUL) {
				Uri uri = mgr.getUriForDownloadedFile(ID);
				String mime = mgr.getMimeTypeForDownloadedFile(ID);

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setDataAndType(uri, mime);
				Glob.application.startActivity(intent);
			}
		}
		cursor.close();
	}

	public void showProgressDialog(Activity ctx, String txt) {
		if (mLoading == null || mLoading.isShowing()) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.dialog_loading, null);
		if (!TextUtils.isEmpty(txt)) {
			UI.setText(view, R.id.dialog_loading_txt, txt);
		}
		mLoading = new ProgressDialog(ctx, R.style.dialog_loading);
		mLoading.setCancelable(false);
		mLoading.setCanceledOnTouchOutside(false);
		mLoading.setContentView(view);
		mLoading.show();
	}

	public void closeProgressDialog() {
		if (mLoading != null) {
			mLoading.cancel();
			mLoading = null;
		}
	}

	public void checkUpdate() {
		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				try {
					String text = IO.textWithURL(params[0]);
					JSONObject json = new JSONObject(text);
					if (Glob.versionCode < json.optInt("version")) {
						return json.optString("download");
					}
				}
				catch (Exception e) {
					Log.e(kTAG, "initUpdate:doInBackground error:" + params[0], e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(final String url) {
				if (TextUtils.isEmpty(url)) {
					return;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(App.topActivity());
				builder.setTitle("发现新版本");
				builder.setMessage("是否升级");
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startDownload(url);
					}
				});
				AlertDialog dialog = builder.create();
				dialog.setCanceledOnTouchOutside(false);
				if (!Glob.isMIUI) {
					dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				}
				dialog.show();
			}
		}.execute(kURL);
	}

	private void startDownload(String url) {
		try {
			String name = url.substring(url.lastIndexOf("/") + 1);
			Request request = new Request(Uri.parse(url));
			request.setTitle(name);
			request.allowScanningByMediaScanner();
			request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
			request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);

			long ID = App.getService(DownloadManager.class).enqueue(request);
			mTaskIDs.put(ID, url);
		}
		catch (Exception e) {
			Log.e(kTAG, "startDownload:" + url, e);
			Toast.makeText(Glob.application, "下载失败", Toast.LENGTH_SHORT).show();
		}
	}
}
