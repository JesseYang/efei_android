package com.efei.android.module.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.biz_remote_interface.IAccountService;
import com.efei.lib.android.biz_remote_interface.IAccountService.RespVerifyPwd;
import com.efei.lib.android.utils.TextUtils;

@SuppressLint("HandlerLeak")
final class ForgetPwd_MobileVerfiyCodeFragment extends Fragment
{

	private String account;
	private EditText editVerifyCode;
	private Handler handlerTimer;

	ForgetPwd_MobileVerfiyCodeFragment(String account)
	{
		this.account = account;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_me_mobile_verfify_num, null);
		TextView tvNewPhoneNum = (TextView) view.findViewById(R.id.tv_new_phone_num);
		tvNewPhoneNum.setText(account);

		final TextView tvResend = (TextView) view.findViewById(R.id.tv_resend_verify_code);
		tvResend.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				handlerTimer.sendEmptyMessage(100);
				v.setEnabled(false);
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_SendAccountForPwd(account),
						new IUICallback.Adapter<BaseRespBean>()));
			}
		});

		editVerifyCode = (EditText) view.findViewById(R.id.actv_verify_code);

		handlerTimer = new Handler()
		{
			private int time = 60;

			@Override
			public void handleMessage(Message msg)
			{
				time--;
				if (time <= 0)
				{
					time = 60;
					tvResend.setEnabled(true);
					tvResend.setText("重发验证码");
					return;
				}
				tvResend.setText("重发验证码（" + time + "）");
				tvResend.setEnabled(false);
				sendEmptyMessageDelayed(100, 1000);
			}
		};

		handlerTimer.sendEmptyMessage(100);
		tvResend.setEnabled(false);
		return view;
	}

	@Override
	public void onPause()
	{
		super.onPause();
		handlerTimer.removeCallbacks(null);
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
				final CharSequence verifyCode = editVerifyCode.getText();
				if (TextUtils.isBlank(verifyCode))
				{
					Toast.makeText(getActivity(), "请输入验证码！", Toast.LENGTH_SHORT).show();
					return true;
				}
				Executor.INSTANCE.execute(new JobAsyncTask<RespVerifyPwd>(new BizRunner_VerifyCode(account, verifyCode.toString()),
						new IUICallback.Adapter<RespVerifyPwd>()
						{
							@Override
							public void onPostExecute(RespVerifyPwd result)
							{
								getActivity().getSupportFragmentManager()
										.beginTransaction()
										.replace(R.id.fl_fragment_container,
												new ForgetPwd_MobileResetPwdFragment(result
														.getReset_password_token())).commit();
							}
						}));
				return true;
			}
		});
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private static class BizRunner_VerifyCode implements IBusinessCallback<RespVerifyPwd>
	{

		private String mobile;
		private String verify_code;

		public BizRunner_VerifyCode(String mobile, String verify_code)
		{
			this.verify_code = verify_code;
			this.mobile = mobile;
		}

		@Override
		public RespVerifyPwd onBusinessLogic(IJob job) throws Exception
		{
			return IAccountService.Factory.getService().post0account$passwords$verify_code(mobile, verify_code);
		}
	}

}