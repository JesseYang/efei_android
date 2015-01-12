package com.efei.android.module.question;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.RespQueOrNote;

public class QueFragment extends Fragment
{
	private QueListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_que, null);

		ListView lv = (ListView) view.findViewById(R.id.lv_que_or_note);
		adapter = new QueListAdapter();
		lv.setAdapter(adapter);
		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Executor.INSTANCE.execute(new JobAsyncTask<List<RespQueOrNote>>(new BizRunner_QueList(), uiCallbackQueList));
	}

	private IUICallback.Adapter<List<RespQueOrNote>> uiCallbackQueList = new IUICallback.Adapter<List<RespQueOrNote>>()
	{
		public void onPostExecute(java.util.List<RespQueOrNote> result)
		{
			adapter.content.addAll(result);
			adapter.notifyDataSetChanged();
		};

		public void onError(Throwable e)
		{
			System.out.println(e);
		};
	};
}
