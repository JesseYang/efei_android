package com.efei.android.module.account;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.MainActivity;
import com.efei.android.module.edit.BizRunner_SaveQue;
import com.efei.android.module.scan.BizRunner_SaveQues;
import com.efei.android.module.scan.ScanActivity;
import com.efei.android.module.settings.teacher.ConfirmAddTeacherActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddBatchQues;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddSingleQue;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.utils.TextUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity
{
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private IJob jobLogin;

	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0xff4388ff));

		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setLogo(new ColorDrawable(0));

		actionBar.setTitle("登录");
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_login);

		Account defaultUser = ILoginService.Factory.getService().getDefaultUser();
		if (null == defaultUser)
			setupViews();
		else
		{
			finish();
			EfeiApplication.switchToActivity(MainActivity.class);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK != resultCode)
			return;
		boolean res = data.getBooleanExtra(RegisterActivity.KEY_REGISTER_RESULT, false);
		if (!res)
			return;
		finish();
		EfeiApplication.switchToActivity(MainActivity.class);
	}

	private void setupViews()
	{
		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);

		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				attemptLogin();
			}
		});

		findViewById(R.id.tv_scan_direct).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
				startActivity(new Intent(LoginActivity.this, ScanActivity.class));
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email, missing fields, etc.), the errors
	 * are presented and no actual login attempt is made.
	 */
	private void attemptLogin()
	{
		if (jobLogin != null)
			return;

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!isPasswordValid(password))
		{
			Toast.makeText(this, "请填写长度为6~16的密码！", Toast.LENGTH_SHORT).show();
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (!isAccountValid(email))
		{
			Toast.makeText(this, "请填写合法的手机号或邮箱号！", Toast.LENGTH_SHORT).show();
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel)
			// There was an error; don't attempt login and focus the
			// first form field with an error.
			focusView.requestFocus();
		else
		{
			// Show a progress spinner, and kick off a background
			// task to perform the user login attempt.
			showProgress(true);
			jobLogin = Executor.INSTANCE.execute(new JobAsyncTask<Account>(new BizRunner_Login(email, password), new LoginUICallback()));
		}
	}

	private boolean isAccountValid(String account)
	{
		return TextUtils.isMobilePhoneNumber(account) || TextUtils.isEmail(account);
	}

	private boolean isPasswordValid(String password)
	{
		return TextUtils.isValidatePassword(password);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show)
	{
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which
		// allow for very easy animations. If available, use these APIs
		// to fade-in the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});
		} else
		{
			// The ViewPropertyAnimator APIs are not available, so
			// simply show and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private class LoginUICallback extends Adapter<Account>
	{
		@SuppressWarnings("unchecked")
		@Override
		public void onPostExecute(Account result)
		{
			jobLogin = null;
			showProgress(false);

			Intent intent = getIntent();
			EfeiApplication app = (EfeiApplication) getApplication();

			boolean bForSaveList = intent.getBooleanExtra(Constants.LOGIN_FOR_SAVE_QUE_LIST, false);
			if (bForSaveList)
			{
				Executor.INSTANCE.execute(new JobAsyncTask<RespAddBatchQues>(new BizRunner_SaveQues((List<QuestionOrNote2>) app
						.removeTemporary(Constants.TMP_QUE_LIST)), new IUICallback.Adapter<RespAddBatchQues>()
				{
					public void onPostExecute(RespAddBatchQues result)
					{
						finish();
						EfeiApplication.switchToActivity(MainActivity.class);
					};
				}));
				return;
			}

			boolean bForSaveQue = intent.getBooleanExtra(Constants.LOGIN_FOR_SAVE_QUE, false);
			if (bForSaveQue)
			{
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_SaveQue(((QuestionOrNote2) app
						.removeTemporary(Constants.TMP_QUE)).metaData, true), new IUICallback.Adapter<BaseRespBean>()
				{
					public void onPostExecute(BaseRespBean result)
					{
						finish();
						RespAddSingleQue resp = (RespAddSingleQue) result;
						if (null == resp.getTeacher())
							EfeiApplication.switchToActivity(MainActivity.class);
						else
						{
							EfeiApplication app = (EfeiApplication) getApplication();
							app.addTemporary(Constants.TMP_TEACHER, resp.getTeacher());
							Intent intent = new Intent(LoginActivity.this, ConfirmAddTeacherActivity.class);
							startActivity(intent);
						}
					};
				}));
				return;
			}

			finish();
			EfeiApplication.switchToActivity(MainActivity.class);
		}

		@Override
		public void onCancelled()
		{
			jobLogin = null;
			showProgress(false);
		}

		@Override
		public void onError(Throwable e)
		{
			jobLogin = null;
			showProgress(false);
			super.onError(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		String sp = new String("免费注册");
		// sp.setSpan(new AbsoluteSizeSpan(30 , true), 0, sp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		MenuItem item = menu.add(sp);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{

			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(intent, 1);
				return true;
			}
		});
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
}
