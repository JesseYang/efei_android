package com.efei.android.module.scan;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.MainActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.bean.persistance.QuestionOrNote;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.engine.ServiceFactory;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.repository.QuestionNoteRepo;
import com.efei.lib.android.utils.CollectionUtils;

public class ScanActivity extends Activity
{
	private Camera mCamera;
	private ScanView mPreview;

	private ImageScanner scanner;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static
	{
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_scan);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new ScanView(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);

		// TODO yunzhong:
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		mPreview.setLayoutParams(params);

		preview.addView(mPreview);

		View viewContinueMode = findViewById(R.id.tv_scan_continue_mode);
		viewContinueMode.setSelected(true);
		viewContinueMode.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean selected = v.isSelected();
				v.setSelected(!selected);
				findViewById(R.id.tv_scan_single_mode).setSelected(selected);
			}
		});

		View viewSingleMode = findViewById(R.id.tv_scan_single_mode);
		viewSingleMode.setSelected(false);
		viewSingleMode.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean selected = v.isSelected();
				v.setSelected(!selected);
				findViewById(R.id.tv_scan_continue_mode).setSelected(selected);
			}
		});

		View scanButton = findViewById(R.id.tv_scan_continue);
		scanButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (barcodeScanned)
				{
					barcodeScanned = false;
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);

					findViewById(R.id.question_scan_result_panel).setVisibility(View.GONE);
					findViewById(R.id.scan_bottom_panel).setVisibility(View.VISIBLE);
					QuestionOrNote queOrNote = (QuestionOrNote) findViewById(R.id.tv_question_scan_result).getTag();
					if (null == queOrNote)
						return;
					queOrNotes.add(queOrNote);

					TextView tvQueNum = (TextView) findViewById(R.id.tv_scanned_que_num);
					tvQueNum.setText("已扫描：" + queOrNotes.size() + "道题");
					tvQueNum.setVisibility(View.VISIBLE);
					findViewById(R.id.ll_bottom_bar).setVisibility(View.VISIBLE);
				}
			}
		});

		View viewCancel = findViewById(R.id.cancel);
		viewCancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (barcodeScanned)
				{
					barcodeScanned = false;
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);

					findViewById(R.id.question_scan_result_panel).setVisibility(View.GONE);
					findViewById(R.id.scan_bottom_panel).setVisibility(View.VISIBLE);
					TextView tvQueNum = (TextView) findViewById(R.id.tv_scanned_que_num);
					tvQueNum.setText("已扫描：" + queOrNotes.size() + "道题");
					tvQueNum.setVisibility(View.VISIBLE);
					findViewById(R.id.ll_bottom_bar).setVisibility(View.VISIBLE);
				}
			}
		});

		View finish = findViewById(R.id.tv_finish);
		finish.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{// TODO yunhun:save
				ILoginService service = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
				Account defaultUser = service.getDefaultUser();
				if (null == defaultUser)
					throw new EfeiException("undo");
				else
				{
					Executor.INSTANCE.execute(new JobAsyncTask<Boolean>(new IBusinessCallback<Boolean>()
					{
						@Override
						public Boolean onBusinessLogic(IJob job)
						{
							boolean result = false;
							QuestionNoteRepo.getInstance().createOrUpdate(queOrNotes);
							result = true;
							return result;
						}
					}, new IUICallback.Adapter<Boolean>()
					{
						public void onPostExecute(Boolean result)
						{
							if (result)
							{
								finish();
								startActivity(new Intent(ScanActivity.this, MainActivity.class));
							}
						};

						public void onError(Throwable e)
						{
							System.out.println(e);
						};
					}));
				}
			}
		});
	}

	private List<QuestionOrNote> queOrNotes = new ArrayList<QuestionOrNote>();

	public void onPause()
	{
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	private Camera getCameraInstance()
	{
		try
		{
			Camera camera = Camera.open();
			camera.getParameters().setZoom(0);
			camera.getParameters().setPreviewSize(900, 900);
			return camera;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private void releaseCamera()
	{
		if (mCamera != null)
		{
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private PreviewCallback previewCb = new PreviewCallback()
	{
		public void onPreviewFrame(byte[] data, Camera camera)
		{
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0)
			{
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				if (CollectionUtils.isEmpty(syms))
					return;
				queryQuestionInRepo(syms.iterator().next().getData());
				barcodeScanned = true;
			}
		}
	};

	private void queryQuestionInRepo(final String shortLink)
	{
		Executor.INSTANCE.execute(new JobAsyncTask<QuestionOrNote>(new QueFromShortLinkTask(shortLink), new QueFromShortLinkCallBack()));
	}

	// Mimic continuous auto-focusing
	private AutoFocusCallback autoFocusCB = new AutoFocusCallback()
	{
		private Handler autoFocusHandler = new Handler(Looper.getMainLooper());
		private Runnable doAutoFocus = new Runnable()
		{
			public void run()
			{
				if (previewing)
					mCamera.autoFocus(autoFocusCB);
			}
		};

		public void onAutoFocus(boolean success, Camera camera)
		{
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private class QueFromShortLinkCallBack extends IUICallback.Adapter<QuestionOrNote>
	{
		@Override
		public void onPostExecute(QuestionOrNote result)
		{
			findViewById(R.id.question_scan_result_panel).setVisibility(View.VISIBLE);
			findViewById(R.id.scan_bottom_panel).setVisibility(View.INVISIBLE);
			TextView tv = (TextView) findViewById(R.id.tv_question_scan_result);
			tv.setTag(result);
			tv.setText(result.getFormattedContent());
		}

		@Override
		public void onError(Throwable e)
		{// TODO yunzhong
			findViewById(R.id.question_scan_result_panel).setVisibility(View.VISIBLE);
			TextView tv = (TextView) findViewById(R.id.tv_question_scan_result);
			tv.setText(e.getMessage());
		}
	}

	private class QueFromShortLinkTask implements IBusinessCallback<QuestionOrNote>
	{
		private String shortLink;

		public QueFromShortLinkTask(String shortLink)
		{
			this.shortLink = shortLink;
		}

		@Override
		public QuestionOrNote onBusinessLogic(IJob job)
		{
			return QuestionNoteRepo.getInstance().queryByShortLink(shortLink);
		}
	}
}
