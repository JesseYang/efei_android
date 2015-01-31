package com.efei.android.module.settings.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.utils.TextUtils;

final class Mobile_PhoneCodeFragment extends Fragment
{
	private String mobile;
	private EditText editNewMobile;

	Mobile_PhoneCodeFragment(String mobile)
	{
		this.mobile = mobile;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		String sp = new String("下一步");
		MenuItem item = menu.add(sp);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				final CharSequence mobile = editNewMobile.getText();
				if (TextUtils.isBlank(mobile))
				{
					Toast.makeText(getActivity(), "号码不能为空！", Toast.LENGTH_SHORT).show();
					return true;
				}
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_ModifyMobile(mobile.toString()),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@Override
							public void onPostExecute(BaseRespBean result)
							{
								getActivity().getSupportFragmentManager()
										.beginTransaction()
										.replace(R.id.fl_fragment_container,
												new Mobile_VerifyCodeFragment(mobile.toString())).commit();
							}
						}));
				return true;
			}
		});
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_me_mobile_phone_num, null);
		TextView tvPrompt = (TextView) view.findViewById(R.id.tv_prompt);
		if (TextUtils.isBlank(mobile))
		{
			view.findViewById(R.id.iv_mobile).setVisibility(View.VISIBLE);
			tvPrompt.setText("请设置一个您用来登录的手机号码：");
		} else
		{
			view.findViewById(R.id.iv_mobile).setVisibility(View.GONE);

			tvPrompt.setText("您当前的手机号为：" + mobile);
		}
		editNewMobile = (EditText) view.findViewById(R.id.actv_mobile);
		return view;
	}
}