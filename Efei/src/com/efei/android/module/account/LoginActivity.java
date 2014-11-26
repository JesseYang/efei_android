package com.efei.android.module.account;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.efei.android.R;
import com.efei.android.module.question.QueListActivity;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.bean.net.ReqLogin;
import com.efei.lib.android.bean.net.RespLogin;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.engine.ServiceFactory;
import com.efei.lib.android.utils.TextUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity
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
		setContentView(R.layout.activity_login);

		ILoginService loginService = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
		Account defaultUser = loginService.getDefaultUser();
		if (null == defaultUser)
			setupViews();
		else
		{
			finish();
			EfeiApplication.switchToActivity(QueListActivity.class);
		}
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
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email))
		{
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isEmailValid(email))
		{
			mEmailView.setError(getString(R.string.error_invalid_email));
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
			ReqLogin reqLogin = new ReqLogin();
			reqLogin.setEmail_mobile(email);
			reqLogin.setPassword(password);
			ILoginService loginService = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
			jobLogin = loginService.login(reqLogin, new LoginUICallback());
		}
	}

	private boolean isEmailValid(String email)
	{
		// TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password)
	{
		// TODO: Replace this with your own logic
		return password.length() > 4;
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

	private class LoginUICallback extends Adapter<RespLogin>
	{
		@Override
		public void onPostExecute(RespLogin result)
		{
			jobLogin = null;
			showProgress(false);

			if (result.isSuccess())
			{
				finish();
				EfeiApplication.switchToActivity(QueListActivity.class);
			} else
			{
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		public void onCancelled()
		{
			jobLogin = null;
			showProgress(false);
		}
	}
}