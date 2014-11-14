package com.efei.lib.android.async;

public interface IUICallback<Result> {
	void onPreExecute();

	void onPostExecute(Result result);

	void onProgressUpdate(int percent, Object... params);

	void onCanceled();

	public static class Adapter<Result> implements IUICallback<Result> {

		@Override
		public void onPreExecute() {
		}

		@Override
		public void onPostExecute(Result result) {
		}

		@Override
		public void onProgressUpdate(int percent, Object... params) {
		}

		@Override
		public void onCanceled() {
		}

	}
}