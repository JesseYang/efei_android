package com.efei.android.module.question;

import java.util.List;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.QuestionOrNote;
import com.efei.lib.android.repository.QuestionNoteRepo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class QueFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_que, null);

		ListView lv = (ListView) view.findViewById(R.id.lv_que_or_note);
		final QueListAdapter adapter = new QueListAdapter();
		lv.setAdapter(adapter);

		Executor.INSTANCE.execute(new JobAsyncTask<List<QuestionOrNote>>(new IBusinessCallback<List<QuestionOrNote>>()
		{

			@Override
			public List<QuestionOrNote> onBusinessLogic(IJob job)
			{
				return QuestionNoteRepo.getInstance().loadAll();
			}
		}, new IUICallback.Adapter<List<QuestionOrNote>>()
		{
			@Override
			public void onPostExecute(List<QuestionOrNote> result)
			{
				adapter.content.addAll(result);
				adapter.notifyDataSetChanged();
			}
		}));
		return view;
	}
}
