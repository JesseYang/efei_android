package com.efei.android.module.scan;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.grammar.RichText;
import com.efei.lib.android.repository.QuestionNoteRepo;

public class ScanActivity extends Activity
{
	private Camera mCamera;
	private ScanView mPreview;
	private Handler autoFocusHandler;

	private TextView scanText;
	private Button scanButton;

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

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new ScanView(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);

		// TODO yunzhong:
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(600, 600, Gravity.CENTER);
		mPreview.setLayoutParams(params);

		preview.addView(mPreview);

		scanText = (TextView) findViewById(R.id.scanText);

		scanButton = (Button) findViewById(R.id.ScanButton);

		scanButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if (barcodeScanned)
				{
					barcodeScanned = false;
					scanText.setText("Scanning...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});
	}

	public void onPause()
	{
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance()
	{
		try
		{
			return Camera.open();
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

	private Runnable doAutoFocus = new Runnable()
	{
		public void run()
		{
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

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
				for (Symbol sym : syms)
				{
					scanText.setText("barcode result " + sym.getData());
					testGet(sym.getData());
					barcodeScanned = true;
				}
			}
		}
	};

	// TODO yunzhong: test tmp code
	private void testGet(final String shortLink)
	{
		Executor.INSTANCE.execute(new JobAsyncTask<RichText>(new IBusinessCallback<RichText>()
		{

			@Override
			public RichText onBusinessLogic(IJob job)
			{
				RespQueOrNote respQueOrNote = QuestionNoteRepo.getInstance().queryByShortLink(shortLink);
				return new RichText(respQueOrNote.getContent());
			}
		}, new IUICallback.Adapter<RichText>()
		{
			@Override
			public void onPostExecute(RichText result)
			{
				TextView tv = (TextView) findViewById(R.id.ivTest);
				tv.setText(result.getReformatText());
			}
		}));
	}

	// Mimic continuous auto-focusing
	private AutoFocusCallback autoFocusCB = new AutoFocusCallback()
	{
		public void onAutoFocus(boolean success, Camera camera)
		{
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

}
