package com.efei.android.module.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.edit.QuestiontEditActivity;
import com.efei.android.module.list.SelectorActivity.SelectorResult;
import com.efei.android.module.list.SelectorActivity.SelectorType;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.Subject;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespDeletedOrPuttedNotes;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.utils.CollectionUtils;
import com.efei.lib.android.utils.TextUtils;

public class QueListFragment extends Fragment
{
	static final int REQUEST_FOR_SELECTOR = 1;
	static final int REQUEST_FOR_QUE_SEARCH_KEYWORD = 2;

	private QueListAdapter adapter;

	private View viewContainer;

	private IJob currentJob;

	private SelectorResult selectorResult;

	private String key_word;

	private boolean selectMode;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		getActivity().findViewById(R.id.bar_action_quelist).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.actv_search).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivityForResult(new Intent(getActivity(), QueSearchActivity.class), REQUEST_FOR_QUE_SEARCH_KEYWORD);
			}
		});
		getActivity().findViewById(R.id.tv_select).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				v.setSelected(!v.isSelected());
				TextView tv = (TextView) v;
				String text = v.isSelected() ? "取消" : "选择";
				int drawable = v.isSelected() ? R.drawable.cancel : R.drawable.icon_select;
				tv.setText(text);
				tv.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);

				selectMode = v.isSelected();
				adapter.cancelSelectAll();
				viewContainer.findViewById(R.id.bar_select_all).findViewById(R.id.fl_select).findViewById(R.id.iv_check).setSelected(false);

				TextView tvLogo = (TextView) getActivity().findViewById(R.id.tv_logo);
				if (v.isSelected())
				{
					tvLogo.setText("导出");
					tvLogo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
					tvLogo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.export, 0, 0);
					tvLogo.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Map<String, QuestionOrNote2> selectedQues = adapter.getSelectedQues();
							Set<String> selectedIds = selectedQues.keySet();
							if (CollectionUtils.isEmpty(selectedIds))
							{
								Toast.makeText(getActivity(), "请选择导出的题目！", Toast.LENGTH_SHORT).show();
								return;
							}
							Intent intent = new Intent(getActivity(), ExportActivity.class);
							intent.putExtra(ExportActivity.KEY_EXPORT_QUE_IDS,
									new ArrayList<String>(selectedIds).toArray(new String[0]));
							selectedQues.clear();
							getActivity().findViewById(R.id.tv_select).performClick();
							startActivity(intent);
						}
					});

					viewContainer.findViewById(R.id.bar_category).setVisibility(View.GONE);
					viewContainer.findViewById(R.id.bar_select_all).setVisibility(View.VISIBLE);
				} else
				{
					adapter.getSelectedQues().clear();
					tvLogo.setText("易");
					tvLogo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
					tvLogo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					tvLogo.setOnClickListener(null);

					viewContainer.findViewById(R.id.bar_category).setVisibility(View.VISIBLE);
					viewContainer.findViewById(R.id.bar_select_all).setVisibility(View.GONE);
				}
			}
		});
		getActivity().findViewById(R.id.bar_action_settings).setVisibility(View.GONE);
		viewContainer = View.inflate(getActivity(), R.layout.fragment_que, null);
		setupSelectorBar(viewContainer);
		viewContainer.findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
		ListView lv = (ListView) viewContainer.findViewById(R.id.lv_que_or_note);
		lv.setVisibility(View.GONE);
		lv.setOnItemClickListener(itemLsn);
		adapter = new QueListAdapter(this);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);

		viewContainer.findViewById(R.id.bar_select_all).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				v = v.findViewById(R.id.fl_select);
				v.setSelected(!v.isSelected());
				v.findViewById(R.id.iv_check).setSelected(v.isSelected());
				if (v.isSelected())
					adapter.selectAll();
				else
					adapter.cancelSelectAll();
			}
		});

		return viewContainer;
	}

	private void setupSelectorBar(View viewContainer)
	{
		viewContainer.findViewById(R.id.ll_subject).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SelectorActivity.forwardForResult(QueListFragment.this, SelectorType.Subject);
			}
		});

		viewContainer.findViewById(R.id.ll_time).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SelectorActivity.forwardForResult(QueListFragment.this, SelectorType.Time);
			}
		});

		viewContainer.findViewById(R.id.ll_tag).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SelectorActivity.forwardForResult(QueListFragment.this, SelectorType.Tag);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		menu.add("删除").setOnMenuItemClickListener(new OnMenuItemClickListener()
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
		menu.add("导出").setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				ContextMenuInfo info = item.getMenuInfo();
				if (info instanceof AdapterContextMenuInfo)
				{
					Intent intent = new Intent(getActivity(), ExportActivity.class);
					QuestionOrNote2 note2 = adapter.content.get(((AdapterContextMenuInfo) info).position);
					intent.putExtra(ExportActivity.KEY_EXPORT_QUE_IDS, new String[]
					{ note2.metaData.get_id() });
					startActivity(intent);
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
		viewContainer.findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
		if (null != currentJob)
			return;
		currentJob = Executor.INSTANCE.execute(new JobAsyncTask<List<QuestionOrNote2>>(new BizRunner_QueList(), uiCallbackQueList));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (Activity.RESULT_OK != resultCode)
			return;
		if (REQUEST_FOR_SELECTOR == requestCode)
			selectorResult = (SelectorResult) data.getSerializableExtra(SelectorActivity.KEY_RESULT);
		else if (REQUEST_FOR_QUE_SEARCH_KEYWORD == requestCode)
			key_word = data.getStringExtra(QueSearchActivity.KEY_SEARCH_KEY_WORD);
		super.onActivityResult(requestCode, resultCode, data);
	}

	boolean isSelectMode()
	{
		return selectMode;
	}

	private IUICallback.Adapter<List<QuestionOrNote2>> uiCallbackQueList = new IUICallback.Adapter<List<QuestionOrNote2>>()
	{
		public void onPostExecute(List<QuestionOrNote2> result)
		{
			filterResultByTypeIfNeed(result);
			filterResultByKeyWordIfNeed(result);
			viewContainer.findViewById(R.id.pb_progress).setVisibility(View.GONE);
			viewContainer.findViewById(R.id.lv_que_or_note).setVisibility(View.VISIBLE);
			adapter.content.clear();
			adapter.content.addAll(result);
			adapter.notifyDataSetChanged();
			currentJob = null;
		};

		private void filterResultByKeyWordIfNeed(List<QuestionOrNote2> result)
		{
			if (null == getActivity())
				return;
			final EditText et = (EditText) getActivity().findViewById(R.id.actv_search);
			et.setText(key_word);
			if (TextUtils.isBlank(key_word))
				return;
			Iterator<QuestionOrNote2> iterator = result.iterator();
			while (iterator.hasNext())
			{
				QuestionOrNote2 queOrNote = iterator.next();
				String tmpContent = "";
				List<String> contents = queOrNote.metaData.getContent();
				if (!CollectionUtils.isEmpty(contents))
				{
					for (String content : contents)
						tmpContent += content;
					if (tmpContent.contains(key_word))
						continue;
				}

				tmpContent = "";
				List<String> items = queOrNote.metaData.getItems();
				if (!CollectionUtils.isEmpty(items))
				{
					for (String item : items)
						tmpContent += item;
					if (tmpContent.contains(key_word))
						continue;
				}

				String summary = queOrNote.metaData.getSummary();
				if (!TextUtils.isEmpty(summary))
				{
					if (summary.contains(key_word))
						continue;
				}

				String tag = queOrNote.metaData.getTag();
				if (!TextUtils.isEmpty(tag))
				{
					if (tag.contains(key_word))
						continue;
				}

				iterator.remove();
			}

			key_word = null;
		}

		private void filterResultByTypeIfNeed(List<QuestionOrNote2> result)
		{
			if (null == selectorResult)
				return;
			switch (selectorResult.type)
			{
			case Subject:
				filterSubject(Subject.getSubjectByName(SelectorType.Subject.content[selectorResult.index]), result);
				selectorResult = null;
				return;

			case Tag:
				filterTag(selectorResult.type.content[selectorResult.index], result);
				selectorResult = null;
				return;

			case Time:
				filterTime(selectorResult.type.content[selectorResult.index], result);
				selectorResult = null;
				return;

			default:
				throw new EfeiException("unknow filter result");
			}
		}

		private void filterTime(String time, List<QuestionOrNote2> result)
		{
			TextView tvSubject = (TextView) viewContainer.findViewById(R.id.tv_time);
			tvSubject.setText(time);
			// { "最近一周", "最近一个月", "最近三个月", "最近半年", "全部时间" }
			if ("全部时间".equals(time))
				return;

			final long duration;
			if ("最近一周".equals(time))
				duration = 7l * 24l * 60l * 60l * 1000l;
			else if ("最近一个月".equals(time))
				duration = 30l * 24l * 60l * 60l * 1000l;
			else if ("最近三个月".equals(time))
				duration = 90l * 24l * 60l * 60l * 1000l;
			else if ("最近半年".equals(time))
				duration = 180l * 24l * 60l * 60l * 1000l;
			else
				duration = 0;
			final Date dateDivider = new Date(new Date().getTime() - duration);
			Iterator<QuestionOrNote2> iterator = result.iterator();
			while (iterator.hasNext())
			{
				QuestionOrNote2 note2 = iterator.next();
				if (!new Date(note2.metaData.getLast_update_time() * 1000).after(dateDivider))
					iterator.remove();
			}
		}

		private void filterTag(String tag, List<QuestionOrNote2> result)
		{
			if ("全部标签".equals(tag))
				return;
			Iterator<QuestionOrNote2> iterator = result.iterator();
			while (iterator.hasNext())
			{
				QuestionOrNote2 note2 = iterator.next();
				if (!note2.metaData.getTag().contains(tag))
					iterator.remove();
			}
		}

		private void filterSubject(Subject subject, List<QuestionOrNote2> result)
		{
			Iterator<QuestionOrNote2> iterator = result.iterator();
			while (iterator.hasNext())
			{
				QuestionOrNote2 note2 = iterator.next();
				if (Subject.getSubjectByIndex(note2.metaData.getSubject()) != subject)
					iterator.remove();
			}

			TextView tvSubject = (TextView) viewContainer.findViewById(R.id.tv_subject);
			tvSubject.setText(subject.name);
		}

		public void onError(Throwable e)
		{
			viewContainer.findViewById(R.id.pb_progress).setVisibility(View.GONE);
			currentJob = null;
			super.onError(e);
		};

		public void onCancelled()
		{
			viewContainer.findViewById(R.id.pb_progress).setVisibility(View.GONE);
			currentJob = null;
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
				Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
				adapter.content.remove(note2Selected);
				adapter.notifyDataSetChanged();
			} else
				Toast.makeText(getActivity(), "删除失败！", Toast.LENGTH_SHORT).show();
		};
	}
}
