package com.efei.android.module.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.account.LoginActivity;
import com.efei.android.module.settings.about.AboutActivity;
import com.efei.android.module.settings.feedback.FeedbackActivity;
import com.efei.android.module.settings.me.SettingsMeActivity;
import com.efei.android.module.settings.teacher.MyTeacherActivity;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespLatestVersion;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.utils.CommonUtils;

public class SettingsFragment extends Fragment
{
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
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		getActivity().findViewById(R.id.bar_action_quelist).setVisibility(View.GONE);
		getActivity().findViewById(R.id.bar_action_settings).setVisibility(View.VISIBLE);
		View view = View.inflate(getActivity(), R.layout.fragment_settings, null);
		setupViews(view);
		return view;
	}

	private void setupViews(View view)
	{
		view.findViewById(R.id.ll_settings_me).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EfeiApplication.switchToActivity(SettingsMeActivity.class);
			}
		});

		view.findViewById(R.id.ll_my_teacher).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				EfeiApplication.switchToActivity(MyTeacherActivity.class);
			}
		});

		view.findViewById(R.id.ll_about).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EfeiApplication.switchToActivity(AboutActivity.class);
			}
		});

		EfeiApplication app = (EfeiApplication) getActivity().getApplication();
		final RespLatestVersion version = app.removeTemporary(Constants.KEY_LATEST_VERSION);
		app.addTemporary(Constants.KEY_LATEST_VERSION, version);
		if (null != version)
		{
			String curVersion = CommonUtils.getCurrentApkVersion(getActivity());
			final int visibility = version.getAndroid().equals(curVersion) ? View.GONE : View.VISIBLE;
			view.findViewById(R.id.iv_red_point).setVisibility(visibility);
		} else
			view.findViewById(R.id.iv_red_point).setVisibility(View.GONE);

		view.findViewById(R.id.ll_feedback).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EfeiApplication.switchToActivity(FeedbackActivity.class);
			}
		});

		view.findViewById(R.id.fl_quit).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ILoginService.Factory.getService().logout();
				getActivity().finish();
				EfeiApplication.switchToActivity(LoginActivity.class);
			}
		});
	}
}
