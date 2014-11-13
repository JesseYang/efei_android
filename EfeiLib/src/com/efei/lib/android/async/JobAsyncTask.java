package com.efei.lib.android.async;

import java.util.concurrent.Future;

import android.os.Handler;
import android.os.Looper;

public class JobAsyncTask<Result> implements IJob {
	private IUICallback<Result> uiCallBack;
	private IBusinessCallback<Result> bussinessCallback;
	private Future<Result> future;

	private Handler handler = new Handler(Looper.getMainLooper());

	public JobAsyncTask(IBusinessCallback<Result> bussinessCallback,
			IUICallback<Result> uiCallback) {
		this.uiCallBack = uiCallback;
		this.bussinessCallback = bussinessCallback;
	}

	// thread pool call----start
	final Result doInBackground() {
		return bussinessCallback.onBusinessLogic(this);
	}

	// thread pool call----end

	// ui thread call----start
	@RunOnUIThread
	final void onPreExecute() {
		if (Looper.myLooper() == Looper.getMainLooper())
			uiCallBack.onPreExecute();
		else
			handler.post(new Runnable() {
				@Override
				public void run() {
					uiCallBack.onPreExecute();
				}
			});
	}

	@RunOnUIThread
	final void onPostExecute(final Result result) {
		if (Looper.myLooper() == Looper.getMainLooper())
			uiCallBack.onPostExecute(result);
		else
			handler.post(new Runnable() {
				@Override
				public void run() {
					uiCallBack.onPostExecute(result);
				}
			});
	}

	@RunOnUIThread
	final void cancel() {
		if (Looper.myLooper() == Looper.getMainLooper())
			uiCallBack.onCanceled();
		else
			handler.post(new Runnable() {
				@Override
				public void run() {
					uiCallBack.onCanceled();
				}
			});
	}

	@RunOnUIThread
	@Override
	public void publishProgress(final int percent, final Object... params) {
		if (Looper.myLooper() == Looper.getMainLooper())
			uiCallBack.onProgressUpdate(percent, params);
		else
			handler.post(new Runnable() {
				@Override
				public void run() {
					uiCallBack.onProgressUpdate(percent, params);
				}
			});
	}

	// ui thread call----end

	@Override
	public boolean isCanceled() {
		return future.isCancelled();
	}

	@Override
	public void cancel(boolean interrupt) {
		future.cancel(interrupt);
		while (future.isCancelled())
			;
		cancel();
	}

	void setFuture(Future<Result> future) {
		this.future = future;
	}

}
