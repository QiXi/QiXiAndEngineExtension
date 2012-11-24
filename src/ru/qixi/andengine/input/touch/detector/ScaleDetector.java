package ru.qixi.andengine.input.touch.detector;


import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.BaseDetector;
import org.andengine.util.math.MathUtils;

import android.util.FloatMath;
import android.view.MotionEvent;


public class ScaleDetector extends BaseDetector {

	private static final float TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT = 10;


	private final IScaleDetectorListener mPinchZoomDetectorListener;

	private float mInitialDistance;
	private float mCurrentDistance;
	private float mPreviousX;
	private float mPreviousY;
	private float mCurrentX;
	private float mCurrentY;
	
	private boolean mPinchZooming;

	
	public ScaleDetector(final IScaleDetectorListener pPinchZoomDetectorListener) {
		mPinchZoomDetectorListener = pPinchZoomDetectorListener;
	}

	
	public boolean isZooming() {
		return mPinchZooming;
	}


	@Override
	public void reset() {
		if(mPinchZooming) {
			mPinchZoomDetectorListener.onScaleFinished(this, null, getZoomFactorX(), getZoomFactorY());
		}
		mPreviousX = 0;
		mPreviousY = 0;
		mCurrentX = 0;
		mCurrentY = 0;
		mInitialDistance = 0;
		mCurrentDistance = 0;
		mPinchZooming = false;
	}

	
	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		final int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

		switch(action) {
			case MotionEvent.ACTION_POINTER_DOWN:
				if(!mPinchZooming && hasTwoOrMorePointers(motionEvent))  {
					mPreviousX = calculateX(motionEvent);
					mPreviousY = calculateY(motionEvent);
					mCurrentX = mPreviousX;
					mCurrentY = mPreviousY;					
					mInitialDistance = calculatePointerDistance(motionEvent);
					mCurrentDistance = mInitialDistance;
					if(mInitialDistance > ScaleDetector.TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT) {
						mPinchZooming = true;
						mPinchZoomDetectorListener.onScaleStarted(this, pSceneTouchEvent);
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if(mPinchZooming) {
					mPinchZooming = false;
					mPinchZoomDetectorListener.onScaleFinished(this, pSceneTouchEvent, getZoomFactorX(), getZoomFactorY());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(mPinchZooming) {
					if (hasTwoOrMorePointers(motionEvent)) {
						//mPreviousX = mCurrentX;
						//mPreviousY = mCurrentY;
						mCurrentX = calculateX(motionEvent);
						mCurrentY = calculateY(motionEvent);
						
						mInitialDistance = mCurrentDistance;
						mCurrentDistance = calculatePointerDistance(motionEvent);			
						//Log.e("", "=="+Math.abs(1-mCurrentDistance/mInitialDistance)+"   =="+(mCurrentDistance/mInitialDistance));
						if(mCurrentDistance > ScaleDetector.TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT && Math.abs(1-mCurrentDistance/mInitialDistance)>0.008f) {
							mPinchZoomDetectorListener.onScale(this, pSceneTouchEvent, getZoomFactorX(), getZoomFactorY());
						}
					} else {
						mPinchZooming = false;
						mPinchZoomDetectorListener.onScaleFinished(this, pSceneTouchEvent, getZoomFactorX(), getZoomFactorY());
					}
				}
				break;
		}
		return true;
	}
	
	
	public float getZoomFactorX() {
		return 0;
	}

	public float getZoomFactorY() {
		return 0;
	}
	
	public float getZoomFactorX2(float pD) {
		float rad = getRotationRad();
		float d = MathUtils.radToDeg(rad);
		rad = MathUtils.degToRad(d-pD);
		float cos = Math.abs((float)Math.cos(rad));
		
		float factor = mCurrentDistance / mInitialDistance;
		float result = factor;
		if (factor>1){
			result = 1+((factor-1)*cos);
		}else if (factor<1){
			result = 1-((1-factor)*cos);
		}		
		//Log.e("", "d="+d+" pD="+pD);
		return result;
	}

	
	public float getZoomFactorY2(float pD) {	
		
		float rad = getRotationRad();
		float d = MathUtils.radToDeg(rad);
		
		rad = MathUtils.degToRad(d-pD);
		float sin = Math.abs(FloatMath.sin(rad));
		
		float factor = mCurrentDistance / mInitialDistance;
		float result = factor;
		if (factor>1){
			result = 1+((factor-1)*sin);
		}else if (factor<1){
			result = 1-((1-factor)*sin);
		}
		//result = Math.abs(sin);
		//Log.e("", "result="+result+" d="+d+" pd="+pD);
		return result;
	}
	

	private static float calculatePointerDistance(final MotionEvent pMotionEvent) {
		return MathUtils.distance(pMotionEvent.getX(0), pMotionEvent.getY(0), pMotionEvent.getX(1), pMotionEvent.getY(1));
	}

	
	private static float calculateX(final MotionEvent pMotionEvent) {
		return Math.abs(pMotionEvent.getX(1)-pMotionEvent.getX(0));
	}
	
	
	private static float calculateY(final MotionEvent pMotionEvent) {
		return Math.abs(pMotionEvent.getY(1)-pMotionEvent.getY(0));
	}
	
	
	/*public float getRotationDegressDelta() {
		double diffRadians = Math.atan2(mPreviousY, mPreviousX) - Math.atan2(mCurrentY, mCurrentX);
		return (float) (diffRadians * 180 / Math.PI);
	}*/
	
	
	public float getRotationRad() {
		return (float)( Math.atan(mCurrentY/mCurrentX));
		//return (float)(Math.atan2(mCurrentY, mCurrentX) - Math.atan2(0, 0));
	}
	
	
	private boolean hasTwoOrMorePointers(final MotionEvent pMotionEvent) {
		return pMotionEvent.getPointerCount() >= 2;
	}


	public static interface IScaleDetectorListener {
		public void onScaleStarted(final ScaleDetector pScaleDetector, final TouchEvent pSceneTouchEvent);
		public void onScale(final ScaleDetector pScaleDetector, final TouchEvent pTouchEvent, final float pZoomXFactor, final float pZoomYFactor);
		public void onScaleFinished(final ScaleDetector pScaleDetector, final TouchEvent pTouchEvent, final float pZoomXFactor, final float pZoomYFactor);
	}
}
