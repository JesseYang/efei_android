package com.efei.lib.android.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.efei.lib.android.exception.EfeiException;

public enum Executor {
	INSTANCE;
	private static final int DEFAULT_THREAD_NUM = 3;
	private ExecutorService threadPool = Executors
			.newFixedThreadPool(DEFAULT_THREAD_NUM);

	// private Handler handler = new Handler(Looper.getMainLooper());

	public <T> IJob execute(final JobAsyncTask<T> task) {
		Future<T> future = threadPool.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				try {
					task.onPreExecute();
					final T res = task.doInBackground();
					task.onPostExecute(res);
					return res;
				} catch (Throwable e) {
					throw new EfeiException(e);
				}
			}
		});
		task.setFuture(future);
		return task;
	}
}
