package com.efei.android.module.list;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.edit.QuestiontEditActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespDeletedOrPuttedNotes;
import com.efei.lib.android.common.EfeiApplication;

public class QueFragment extends Fragment
{
	private QueListAdapter adapter;

	private View viewContainer;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		viewContainer = View.inflate(getActivity(), R.layout.fragment_que, null);
		viewContainer.findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
		ListView lv = (ListView) viewContainer.findViewById(R.id.lv_que_or_note);
		lv.setVisibility(View.GONE);
		lv.setOnItemClickListener(itemLsn);
		adapter = new QueListAdapter();
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
		return viewContainer;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		menu.add("É¾³ý").setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				ContextMenuInfo info = item.getMenuInfo();
				if (info instanceof AdapterContextMenuInfo)
				{
					QuestionOrNote2 note2 = adapter.content.get(((AdapterContextMenuInfo) info).position);
					Executor.INSTANCE.execute(new JobAsyncTask<RespDeletedOrPuttedNotes>(new BizRunner_DeleteQue(note2.metaData.get_id()),
							new UiDeleteItem(note2)));
				}
				return true;
			}
		});
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Executor.INSTANCE.execute(new JobAsyncTask<List<QuestionOrNote2>>(new BizRunner_QueList(), uiCallbackQueList));
	}

	private IUICallback.Adapter<List<QuestionOrNote2>> uiCallbackQueList = new IUICallback.Adapter<List<QuestionOrNote2>>()
	{
		public void onPostExecute(List<QuestionOrNote2> result)
		{
			viewContainer.findViewById(R.id.pb_progress).setVisibility(View.GONE);
			viewContainer.findViewById(R.id.lv_que_or_note).setVisibility(View.VISIBLE);
			adapter.content.clear();
			adapter.content.addAll(result);
			adapter.notifyDataSetChanged();
		};

		public void onError(Throwable e)
		{
			System.out.println(e);
		};
	};

	private OnItemClickListener itemLsn = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			QuestionOrNote2 note2 = adapter.content.get(position);
			EfeiApplication app = (EfeiApplication) getActivity().getApplication();
			app.addTemporary(Constants.TMP_QUE, note2);
			EfeiApplication.switchToActivity(QuestiontEditActivity.class);
		}
	};

	private class UiDeleteItem extends Adapter<RespDeletedOrPuttedNotes>
	{
		private QuestionOrNote2 note2Selected;

		public UiDeleteItem(QuestionOrNote2 note2)
		{
			this.note2Selected = note2;
		}

		public void onPostExecute(RespDeletedOrPuttedNotes result)
		{
			if (result.isSuccess())
			{
				Toast.makeText(getActivity(), "É¾³ý³É¹¦£¡", Toast.LENGTH_SHORT).show();
				adapter.content.remove(note2Selected);
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getActivity(), "É¾³ýÊ§°Ü£¡", Toast.LENGTH_SHORT).show();
		};
	}
}
