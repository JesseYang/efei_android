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

final class Email_CompleteFragment extends Fragment
{
	private String email;

	Email_CompleteFragment(String email)
	{
		this.email = email;
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
		View view = View.inflate(getActivity(), R.layout.fragment_me_email_complete, null);
		TextView tvPrompt = (TextView) view.findViewById(R.id.tv_prompt);
		tvPrompt.setText("您当前绑定的邮箱为：" + email);
		view.findViewById(R.id.tv_change_email).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, new Email_FillFragment(email))
						.commit();
			}
		});
		return view;
	}
}