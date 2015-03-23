package com.efei.android.module.settings.about;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespLatestVersion;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CommonUtils;
import com.efei.lib.android.utils.NetUtils;

public class AboutActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("关于易飞");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_about);

		final String curVersionName = CommonUtils.getCurrentApkVersion(this);

		final View versionBar = findViewById(R.id.ll_version);

		TextView tvCurVersion = (TextView) findViewById(R.id.tv_cur_version);
		tvCurVersion.setText("易飞网v" + curVersionName);

		EfeiApplication app = (EfeiApplication) getApplication();
		final RespLatestVersion latestVersion = app.removeTemporary(Constants.KEY_LATEST_VERSION);
		app.addTemporary(Constants.KEY_LATEST_VERSION, latestVersion);

		TextView tvLatestVersion = (TextView) findViewById(R.id.tv_latest_version);
		if (null == latestVersion || latestVersion.getAndroid().equals(curVersionName))
		{
			findViewById(R.id.iv_red_point).setVisibility(View.GONE);
			tvLatestVersion.setText("已经是最新版本");
		} else
		{
			findViewById(R.id.iv_red_point).setVisibility(View.VISIBLE);
			tvLatestVersion.setText("v" + latestVersion.getAndroid());
			versionBar.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Toast.makeText(AboutActivity.this, "开始下载新版本安装包……", Toast.LENGTH_SHORT).show();
					Executor.INSTANCE.execute(new JobAsyncTask<File>(new Biz_DownloadLatestVersionApk(latestVersion.getAndroid_url()),
							new IUICallback.Adapter<File>()
							{
								public void onPostExecute(File result)
								{
									Toast.makeText(AboutActivity.this, "安装包下载完成", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(Intent.ACTION_VIEW);
									intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
									startActivity(intent);
								};
							}));
				}
			});
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
			finish();
		return super.onOptionsItemSelected(item);
	}

	private static class Biz_DownloadLatestVersionApk implements IBusinessCallback<File>
	{
		private final String fileUrl;

		public Biz_DownloadLatestVersionApk(String fileUrl)
		{
			this.fileUrl = fileUrl;
		}

		@Override
		public File onBusinessLogic(IJob job) throws Exception
		{
			InputStream is = NetUtils.getAsStream(fileUrl, null);
			String download = Environment.getExternalStorageDirectory() + "/efei/download/";
			File file = new File(download, "newEfei.apk");
			OutputStream os = FileUtils.openOutputStream(file);
			int iLen = -1;
			byte[] buffer = new byte[1024];
			while (-1 != (iLen = is.read(buffer)))
				os.write(buffer, 0, iLen);
			is.close();
			os.close();
			return file;
		}
	}
}
