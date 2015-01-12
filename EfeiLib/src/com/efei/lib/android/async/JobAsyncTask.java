package com.efei.lib.android.async;

import java.util.concurrent.Future;

import android.os.Handler;
import android.os.Looper;

public class JobAsyncTask<Result> implements IJob
{
	private IUICallback<Result> uiCallBack;
	private IBusinessCallback<Result> bussinessCallback;
	private Future<Result> future;

	private Handler handler = new Handler(Looper.getMainLooper());

	public JobAsyncTask(IBusinessCallback<Result> bussinessCallback,
			IUICallback<Result> uiCallback)
	{
		this.uiCallBack = uiCallback;
		this.bussinessCallback = bussinessCallback;
	}

	// thread pool call----start
	final Result doInBackground()
	{
		return bussinessCallback.onBusinessLogic(this);
	}

	// thread pool call----end

	// ui thread call----start
	@RunOnUIThread
	final void preExecute()
	{
		runOnUIThread(new Runnable()
		{
			@Override
			public void run()
			{
				uiCallBack.onPreExecute();
			}
		});
	}

	@RunOnUIThread
	final void postExecute(final Result result)
	{
		runOnUIThread(new Runnable()
		{
			@Override
			public void run()
			{
				uiCallBack.onPostExecute(result);
			}
		});
	}

	@RunOnUIThread
	final void cancel()
	{
		runOnUIThread(new Runnable()
		{
			@Override
			public void run()
			{
				uiCallBack.onCancelled();
			}
		});
	}

	@RunOnUIThread
	@Override
	public void publishProgress(final int percent, final Object... params)
	{
		runOnUIThread(new Runnable()
		{
			@Override
			public void run()
			{
				uiCallBack.onProgressUpdate(percent, params);
			}
		});
	}

	@RunOnUIThread
	void error(final Throwable e)
	{
		runOnUIThread(new Runnable()
		{

			@Override
			public void run()
			{
				uiCallBack.onError(e);
			}
		});
	}

	// ui thread call----end

	@Override
	public boolean isCanceled()
	{
		return future.isCancelled();
	}

	@Override
	public void cancel(boolean interrupt)
	{
		future.cancel(interrupt);
		while (future.isCancelled())
			;
		cancel();
	}

	void setFuture(Future<Result> future)
	{
		this.future = future;
	}

	private void runOnUIThread(Runnable runnable)
	{
		if (Looper.getMainLooper() == Looper.myLooper())
			runnable.run();
		else
			handler.post(runnable);
	}

}
