package com.efei.android.module.account;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.scan.ScanActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.utils.TextUtils;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends ActionBarActivity
{
	public static final String KEY_REGISTER_RESULT = "key_register_result";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private IJob jobRegister;

	// UI references.
	private AutoCompleteTextView mMobileView;
	private EditText mName;
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

		actionBar.setTitle("注册");
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_register);

		setupViews();
	}

	private void setupViews()
	{
		// Set up the login form.
		mMobileView = (AutoCompleteTextView) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		mName = (EditText) findViewById(R.id.name);

		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);

		Button mEmailSignInButton = (Button) findViewById(R.id.email_register_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				attemptRegister();
			}
		});

		findViewById(R.id.tv_scan_direct).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
				startActivity(new Intent(RegisterActivity.this, ScanActivity.class));
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email, missing fields, etc.), the errors
	 * are presented and no actual login attempt is made.
	 */
	private void attemptRegister()
	{
		if (jobRegister != null)
			return;

		// Reset errors.
		mMobileView.setError(null);
		mPasswordView.setError(null);
		mName.setError(null);

		// Store values at the time of the login attempt.
		String mobile = mMobileView.getText().toString();
		String password = mPasswordView.getText().toString();
		String name = mName.getText().toString();

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
		if (!isMobileValid(mobile))
		{
			Toast.makeText(this, "请填写合法的手机号！", Toast.LENGTH_SHORT).show();
			focusView = mMobileView;
			cancel = true;
		}

		if (TextUtils.isBlank(name))
		{
			Toast.makeText(this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
			focusView = mName;
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

			jobRegister = Executor.INSTANCE.execute(new JobAsyncTask<Account>(new BizRunner_Register(mobile, password, name),
					new RegisterUICallback()));
		}
	}

	private boolean isMobileValid(String mobile)
	{
		return TextUtils.isMobilePhoneNumber(mobile);
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

	private class RegisterUICallback extends Adapter<Account>
	{
		@Override
		public void onPostExecute(Account result)
		{
			jobRegister = null;
			showProgress(false);
			Intent resultIntent = new Intent();
			resultIntent.putExtra(KEY_REGISTER_RESULT, true);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}

		@Override
		public void onCancelled()
		{
			jobRegister = null;
			showProgress(false);
		}

		@Override
		public void onError(Throwable e)
		{
			jobRegister = null;
			showProgress(false);
			super.onError(e);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// String sp = new String("直接登录");
	// // sp.setSpan(new AbsoluteSizeSpan(30 , true), 0, sp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	// MenuItem item = menu.add(sp);
	// item.setOnMenuItemClickListener(new OnMenuItemClickListener()
	// {
	//
	// @Override
	// public boolean onMenuItemClick(MenuItem item)
	// {
	// finish();
	// EfeiApplication.switchToActivity(LoginActivity.class);
	// return true;
	// }
	// });
	// MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
	// return super.onCreateOptionsMenu(menu);
	// }
}
