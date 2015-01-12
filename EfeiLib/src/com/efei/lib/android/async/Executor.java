package com.efei.lib.android.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum Executor
{
	INSTANCE;
	private static final int DEFAULT_THREAD_NUM = 3;
	private ExecutorService threadPool = Executors
			.newFixedThreadPool(DEFAULT_THREAD_NUM);

	public <T> IJob execute(final JobAsyncTask<T> task)
	{
		Future<T> future = threadPool.submit(new Callable<T>()
		{
			@Override
			public T call() throws Exception
			{
				try
				{
					task.preExecute();
					final T res = task.doInBackground();
					task.postExecute(res);
					return res;
				} catch (Throwable e)
				{
					task.error(e);
					return null;
				}
			}
		});
		task.setFuture(future);
		return task;
	}
}
