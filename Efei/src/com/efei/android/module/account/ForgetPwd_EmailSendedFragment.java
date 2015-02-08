package com.efei.android.module.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.efei.android.R;

final class ForgetPwd_EmailSendedFragment extends Fragment
{
	private String account;

	ForgetPwd_EmailSendedFragment(String email)
	{
		this.account = email;
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
		View view = View.inflate(getActivity(), R.layout.fragment_me_email_final, null);
		TextView tvPrompt = (TextView) view.findViewById(R.id.tv_prompt);
		tvPrompt.setText(account);
		return view;
	}
}