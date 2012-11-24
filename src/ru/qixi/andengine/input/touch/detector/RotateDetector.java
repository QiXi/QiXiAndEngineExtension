package ru.qixi.andengine.input.touch.detector;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.BaseDetector;
import org.andengine.util.math.MathUtils;

import android.view.MotionEvent;


public class RotateDetector extends BaseDetector {


	private static final float MINIMUM_DISTANCE_DEFAULT = 30;

	private final IRotateDetectorListener mDetectorListener;

	private float mInitialDistance;
	private float mCurrentDistance;
	private boolean mPinchRotating;
	private float mPreviousX;
	private float mPreviousY;
	private float mCurrentX;
	private float mCurrentY;
	

	public RotateDetector(final IRotateDetectorListener pPinchZoomDetectorListener) {
		mDetectorListener = pPinchZoomDetectorListener;
	}


	public boolean isRotating() {
		return mPinchRotating;
	}


	@Override
	public void reset() {
		if(mPinchRotating) {
			mDetectorListener.onRotateFinished(this, null, getRotationDegressDelta());
		}
		mPreviousX = 0;
		mPreviousY = 0;
		mCurrentX = 0;
		mCurrentY = 0;
		mInitialDistance = 0;
		mCurrentDistance = 0;
		mPinchRotating = false;
	}

	
	@Override
	public boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();

		final int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

		switch(action) {
			case MotionEvent.ACTION_POINTER_DOWN:
				if(!mPinchRotating && hasTwoOrMorePointers(motionEvent))  {
					mPreviousX = calculateX(motionEvent);
					mPreviousY = calculateY(motionEvent);
					mCurrentX = mPreviousX;
					mCurrentY = mPreviousY;
					
					mInitialDistance = RotateDetector.calculatePointerDistance(motionEvent);
					mCurrentDistance = mInitialDistance;
					if(mInitialDistance > RotateDetector.MINIMUM_DISTANCE_DEFAULT) {
						mPinchRotating = true;
						mDetectorListener.onRotateStarted(this, pSceneTouchEvent);
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if(mPinchRotating) {
					mPinchRotating = false;
					mDetectorListener.onRotateFinished(this, pSceneTouchEvent, getRotationDegressDelta());
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(mPinchRotating) {
					if (hasTwoOrMorePointers(motionEvent)) {
						mPreviousX = mCurrentX;
						mPreviousY = mCurrentY;
						mCurrentX = calculateX(motionEvent);
						mCurrentY = calculateY(motionEvent);
						mCurrentDistance = RotateDetector.calculatePointerDistance(motionEvent);
						if(mCurrentDistance > RotateDetector.MINIMUM_DISTANCE_DEFAULT) {
							mDetectorListener.onRotate(this, pSceneTouchEvent, getRotationDegressDelta());
						}
					} else {
						mPinchRotating = false;
						mDetectorListener.onRotateFinished(this, pSceneTouchEvent, getRotationDegressDelta());
					}
				}
				break;
		}
		return true;
	}
	

	private static float calculatePointerDistance(final MotionEvent pMotionEvent) {
		return MathUtils.distance(pMotionEvent.getX(0), pMotionEvent.getY(0), pMotionEvent.getX(1), pMotionEvent.getY(1));
	}
	
	
	private static float calculateX(final MotionEvent pMotionEvent) {
		return (pMotionEvent.getX(1)-pMotionEvent.getX(0));
	}
	
	
	private static float calculateY(final MotionEvent pMotionEvent) {
		return (pMotionEvent.getY(1)-pMotionEvent.getY(0));
	}

	
	private boolean hasTwoOrMorePointers(final MotionEvent pMotionEvent) {
		return pMotionEvent.getPointerCount() >= 2;
	}

	
	public float getRotationDegressDelta() {
		double diffRadians = Math.atan2(mPreviousY, mPreviousX) - Math.atan2(mCurrentY, mCurrentX);
		return (float) (diffRadians * 180 / Math.PI);
	}
	

	public static interface IRotateDetectorListener {
		public void onRotateStarted(final RotateDetector pDetector, final TouchEvent pSceneTouchEvent);
		public void onRotate(final RotateDetector pDetector, final TouchEvent pTouchEvent, final float pDegress);
		public void onRotateFinished(final RotateDetector pDetector, final TouchEvent pTouchEvent, final float pDegress);
	}
}
