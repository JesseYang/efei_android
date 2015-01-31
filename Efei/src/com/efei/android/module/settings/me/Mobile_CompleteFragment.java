package com.efei.android.module.settings.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.efei.android.R;

final class Mobile_CompleteFragment extends Fragment
{
	private String mobile;

	Mobile_CompleteFragment(String mobile)
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
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_me_mobile_complete, null);
		TextView tvPrompt = (TextView) view.findViewById(R.id.tv_prompt);
		tvPrompt.setText("您的手机号为：" + mobile);
		view.findViewById(R.id.tv_change_mobile).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.fl_fragment_container, new Mobile_PhoneCodeFragment(mobile)).commit();
			}
		});
		return view;
	}
}