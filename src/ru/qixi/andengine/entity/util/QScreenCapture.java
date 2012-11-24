package ru.qixi.andengine.entity.util;

import android.graphics.Bitmap;

import org.andengine.entity.util.ScreenCapture;



public class QScreenCapture extends ScreenCapture {
	
	
	private IBitmapHelper mBitmapHelper;

	
	@Override
	public void onScreenGrabbed(Bitmap pBitmap) {
		Bitmap bitmap = pBitmap;
		if (mBitmapHelper != null) {
			bitmap = mBitmapHelper.createBitmap(pBitmap);
		}
		super.onScreenGrabbed(bitmap);
	}
	
	
	public void capture(final int pCaptureX, final int pCaptureY, final int pCaptureWidth, final int pCaptureHeight, final IBitmapHelper pBitmapHelper, final String pFilePath, final IScreenCaptureCallback pScreenCaptureCallback) {
		mBitmapHelper = pBitmapHelper;
		capture(0, 0, pCaptureWidth, pCaptureHeight, pFilePath, pScreenCaptureCallback);
	}
	
}
