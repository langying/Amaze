package app.engine.Amaze;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import app.engine.common.App;

public class AELifecycle implements Application.ActivityLifecycleCallbacks {

	private static final String kTAG = AELifecycle.class.getSimpleName();

	private static AELifecycle instance;

	public static AELifecycle init(Application application) {
		if (instance == null) {
			instance = new AELifecycle(application);
			application.registerActivityLifecycleCallbacks(instance);
		}
		return instance;
	}

	private int			mCount		= 0;
	private Runnable	mCheck		= null;
	private Handler		mHandler	= new Handler();

	private AELifecycle(Application application) {
	}

	@Override
	public void onActivityResumed(Activity activity) {
		mCount++;
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivityResumed:" + mCount);
		if (mCheck != null) {
			mHandler.removeCallbacks(mCheck);
		}
	}

	@Override
	public void onActivityPaused(Activity activity) {
		mCount--;
		if (mCheck != null) {
			mHandler.removeCallbacks(mCheck);
		}
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivityPaused:" + mCount);
	}

	@Override
	public void onActivityStopped(Activity activity) {
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivityStopped:" + mCount);
		if (mCheck != null) {
			mHandler.removeCallbacks(mCheck);
		}
		mHandler.postDelayed(mCheck = new Runnable() {
			@Override
			public void run() {
				if (mCount > 0) {
					Log.d(kTAG, "App still alived:" + mCount);
				}
				else {
					Log.d(kTAG, "App enter stoped:" + mCount);
					App.killSelf();
				}
			}
		}, 3000);
	}

	@Override
	public void onActivityStarted(Activity activity) {
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivityStarted");
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivityDestroyed");
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle bundle) {
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivityCreated");
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
		Log.d(kTAG, activity.getClass().getSimpleName() + ":onActivitySaveInstanceState");
	}
}
