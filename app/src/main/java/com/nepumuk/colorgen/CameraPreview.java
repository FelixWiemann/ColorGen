package com.nepumuk.colorgen;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback
{
	private static final String TAG = "CAMERA_PREVIEW";
	private SurfaceHolder aHolder;
	private Camera aCamera;
	private int[][] aPixels;
	private Camera.Size aPreviewSize;
	TextView tv;

	public void flashOn(boolean on){
		Camera.Parameters p = aCamera.getParameters();
		if (on){
			p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);}
		else{
			p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
		aCamera.setParameters(p);
		}



	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CameraPreview(Context context) {
		super(context);
	}

	public CameraPreview(Context context, Camera camera) {
		super(context);
		aCamera = camera;
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		aHolder = getHolder();
		aHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		aHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	private void init(Camera camera) {
		aCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		aHolder = getHolder();
		aHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		aHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			aCamera.setPreviewDisplay(holder);
			aCamera.setPreviewCallback(this);
			aCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
		//aCamera.release();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (aHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			aCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		aCamera.getParameters().getPreviewSize();
		// reformatting changes here
		Canvas canvas = null;


		// start preview with new settings
		try {
			aCamera.setPreviewDisplay(aHolder);
			aCamera.setPreviewCallback(this);
			aPreviewSize = aCamera.getParameters().getPreviewSize();
			aCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	public void setCamera(Camera cameraInstance) {
		init(cameraInstance);
	}

	public void setTextView(TextView tv) {
		this.tv = tv;
	}

	void decodeYUV420SP( byte[] yuv420sp, int width, int height) {

		final int frameSize = width * height;
		int[][] rgb = new int[frameSize][4];

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0) r = 0;
				else if (r > 262143)
					r = 262143;
				if (g < 0) g = 0;
				else if (g > 262143)
					g = 262143;
				if (b < 0) b = 0;
				else if (b > 262143)
					b = 262143;

				//rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
				rgb[yp][0] = 0xff;
				rgb[yp][1] = (r << 6)&0xff;
				rgb[yp][2] = (g >> 2)&0xff;
				rgb[yp][3] = (b >> 10)&0xff;

			}
		}
		aPixels =rgb;
	}

	public void closeCam() {
		aCamera.release();
	}

	private void makeToast(String message)
	{
		Toast.makeText(this.getContext(),message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		//transforms NV21 pixel data into RGB aPixels
		decodeYUV420SP(data, aPreviewSize.width, aPreviewSize.height);
		int averageGreen=0;
		int averageBlue=0;
		int averageRed=0;

		for (int[] i: aPixels) {
			averageRed += i[1];
			averageGreen += i[2];
			averageBlue += i[3];
		}
		averageBlue /= aPixels.length;
		averageGreen /= aPixels.length;
		averageRed /= aPixels.length;

		tv.setText("r: "+Integer.toHexString(averageRed)+", g: "+Integer.toHexString(averageGreen)+", b: "+Integer.toHexString(averageBlue));

		//Outuput the value of the top left pixel in the preview to LogCat
		//Log.i("Pixels", "The top right pixel has the following RGB (hexadecimal) values:"
		//		+Integer.toHexString(aPixels[0]));

	}

}
