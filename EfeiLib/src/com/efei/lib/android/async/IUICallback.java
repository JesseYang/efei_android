package com.efei.lib.android.async;

public interface IUICallback<Result> {
	void onPreExecute();

	void onPostExecute(Result result);

	void onProgressUpdate(int percent, Object... params);

	void onCanceled();
}