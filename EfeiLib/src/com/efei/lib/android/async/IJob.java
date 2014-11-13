package com.efei.lib.android.async;

public interface IJob {
	boolean isCanceled();

	void publishProgress(int percent, Object... params);

	void cancel(boolean interrupt);
}