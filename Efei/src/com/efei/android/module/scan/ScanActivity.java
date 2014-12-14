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
import com.efei.lib.android.bean.persistance.QuestionOrNote;
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
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
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
			Camera camera = Camera.open();
			// Parameters param = camera.getParameters();
			// if (param.isSmoothZoomSupported())
			// param.setZoom(0);
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

			// YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), size.width, size.height, null);
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			// yuv.(new Rect(0, 0, size.width, size.height), 100, out);
			// byte[] bytes = out.toByteArray();
			// final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			// Bitmap bmpBig = Bitmap.createScaledBitmap(bmp, bmp.getWidth() * 3, bmp.getHeight() * 3, false);
			// bmp.recycle();
			// // calculate how many bytes our image consists of.
			// int bytesNew = bmpBig.getByteCount();
			// // or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
			// // int bytes = b.getWidth()*b.getHeight()*4;
			// ByteBuffer buffer = ByteBuffer.allocate(bytesNew); // Create a new buffer
			// bmpBig.copyPixelsToBuffer(buffer); // Move the byte data to the buffer
			// data = buffer.array(); // Get the underlying array containing the data.
			// bmpBig.recycle();
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
		Executor.INSTANCE.execute(new JobAsyncTask<QuestionOrNote>(new IBusinessCallback<QuestionOrNote>()
		{

			@Override
			public QuestionOrNote onBusinessLogic(IJob job)
			{
				return QuestionNoteRepo.getInstance().queryByShortLink(shortLink);
			}
		}, new IUICallback.Adapter<QuestionOrNote>()
		{
			@Override
			public void onPostExecute(QuestionOrNote result)
			{
				TextView tv = (TextView) findViewById(R.id.ivTest);
				tv.setText(result.getFormattedContent());
			}
			
			@Override
			public void onError(Throwable e)
			{
				TextView tv = (TextView) findViewById(R.id.ivTest);
				tv.setText(e.getMessage());
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
