package ru.qixi.andengine.input.touch.detector;

import android.view.MotionEvent;

import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.BaseDetector;


public class SmartDetector extends BaseDetector{
	
	private static final float TRIGGER_SCROLL_MINIMUM_DISTANCE_DEFAULT = 10;
	private static final float TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT = 20;
	private static final float TRIGGER_ROTATE_MINIMUM_DISTANCE_DEFAULT = 30;
	private static final float TRIGGER_ROTATE_MINIMUM_DEGRESS_DEFAULT = 1;
	
	private static final float PINCHZOOM_DISTANCE_DETECT_DEFAULT = 20;
	private static final float ROTATE_DEGRESS_DETECT_DEFAULT = 3;
	
	private final ISmartDetectorListener mSmartDetectorListener;
	
	private final float mTriggerScrollMinimumDistance;
	private final float mTriggerPinchZoomMinimumDistance;
	private final float mTriggerRotateMinimumDistance;
	
	private final float mPinchZoomDistanceDetect;
	private final float mRotateDegressDetect;
	
	private State mState = State.NONE;

	//two points
	private float mInitialTwoDistance;
	private float mCurrentTwoDistance;	
	private float mPreviousTwoXDistance;
	private float mPreviousTwoYDistance;
	private float mCurrentTwoXDistance;
	private float mCurrentTwoYDistance;
	//one point	
	private float mPreviousX;
	private float mPreviousY;
	private float mCurrentXDistance;
	private float mCurrentYDistance;
	
	private boolean mTriggering;
	
	
	
	public SmartDetector(final ISmartDetectorListener pScrollDetectorListener) {
		this(pScrollDetectorListener, PINCHZOOM_DISTANCE_DETECT_DEFAULT, ROTATE_DEGRESS_DETECT_DEFAULT);
	}
	
	
	public SmartDetector(final ISmartDetectorListener pScrollDetectorListener, final float pPinchZoomDistanceDetect, final float pRotateDegressDetect) {
		this(pScrollDetectorListener, pPinchZoomDistanceDetect, pRotateDegressDetect, TRIGGER_SCROLL_MINIMUM_DISTANCE_DEFAULT, TRIGGER_PINCHZOOM_MINIMUM_DISTANCE_DEFAULT, TRIGGER_ROTATE_MINIMUM_DISTANCE_DEFAULT);
	}

	
	public SmartDetector(final ISmartDetectorListener pScrollDetectorListener, final float pPinchZoomDistanceDetect, final float pRotateDegressDetect, final float pTriggerScrollMinimumDistance, final float pTriggerPinchZoomMinimumDistance, final float pTriggerRotateMinimumDistance) {
		mSmartDetectorListener = pScrollDetectorListener;
		
		mPinchZoomDistanceDetect = pPinchZoomDistanceDetect;
		mRotateDegressDetect = pRotateDegressDetect;
		
		mTriggerScrollMinimumDistance = pTriggerScrollMinimumDistance;
		mTriggerPinchZoomMinimumDistance = pTriggerPinchZoomMinimumDistance;
		mTriggerRotateMinimumDistance = pTriggerRotateMinimumDistance;	
	}
	
	
	@Override
	public void reset() {
		finishedState(null);

		mCurrentTwoDistance = mInitialTwoDistance = 0;
		mCurrentTwoXDistance = mPreviousTwoXDistance = 0;
		mCurrentTwoYDistance = mPreviousTwoYDistance = 0;

		mPreviousX = mPreviousY = 0;
		mCurrentXDistance = mCurrentYDistance = 0;
		
		mTriggering = false;
	}

	
	
	@Override
	protected boolean onManagedTouchEvent(final TouchEvent pSceneTouchEvent) {
		final int action = pSceneTouchEvent.getAction();
		//final int pointerID = pSceneTouchEvent.getPointerID();
		
		final MotionEvent motionEvent = pSceneTouchEvent.getMotionEvent();
		final boolean hasTwoOrMorePointers = hasTwoOrMorePointers(motionEvent);

		switch(action) {
			case TouchEvent.ACTION_DOWN:
				//Debug.e("ACTION_DOWN "+pointerID);
				if (mState == State.NONE){
					if (hasTwoOrMorePointers){
						prepareTwoPointers(motionEvent);	
						mPreviousTwoXDistance = mCurrentTwoXDistance;
						mPreviousTwoYDistance = mCurrentTwoYDistance;						
						mInitialTwoDistance = mCurrentTwoDistance;
					}else {
						mPreviousX = pSceneTouchEvent.getX();
						mPreviousY = pSceneTouchEvent.getY();	
						mCurrentXDistance = mCurrentYDistance = 0;
					}
					mState = State.NONE;
					mTriggering = true;
				}				
				break;
				
			case TouchEvent.ACTION_MOVE:	
				//Debug.e("ACTION_MOVE "+pointerID);
				switch (mState) {

				case NONE:				//Detect
					if (mTriggering){
						if (hasTwoOrMorePointers){
							prepareTwoPointers(motionEvent);									
							
							if(Math.abs(mInitialTwoDistance-mCurrentTwoDistance) > mPinchZoomDistanceDetect) {							
								mSmartDetectorListener.onPinchZoomStarted(this, pSceneTouchEvent, getZoomFactor());
								mState = State.PINCHZOOM;
							}else{							
								final float degress = calculateRotationDegress();
								if(Math.abs(degress) > mRotateDegressDetect) {
									mSmartDetectorListener.onRotateStarted(this, pSceneTouchEvent, degress);
									mPreviousTwoXDistance = mCurrentTwoXDistance;
									mPreviousTwoYDistance = mCurrentTwoYDistance;
									mState = State.ROTATE;
								}
							}
						}else{
							prepareOnePointer(pSceneTouchEvent);
							if(Math.abs(mCurrentXDistance) > mTriggerScrollMinimumDistance || Math.abs(mCurrentYDistance) > mTriggerScrollMinimumDistance) {
								mSmartDetectorListener.onScrollStarted(this, pSceneTouchEvent, mCurrentXDistance, mCurrentYDistance);
								mPreviousX = pSceneTouchEvent.getX();
								mPreviousY = pSceneTouchEvent.getY();
								mState = State.SCROOL;
							}						
						}
					}
					break;
					
				case SCROOL:
					if (hasOnePointer(motionEvent)){		
						prepareOnePointer(pSceneTouchEvent);	
						if(Math.abs(mCurrentXDistance) > mTriggerScrollMinimumDistance || Math.abs(mCurrentYDistance) > mTriggerScrollMinimumDistance) {
							mSmartDetectorListener.onScroll(this, pSceneTouchEvent, mCurrentXDistance, mCurrentYDistance);				
							mPreviousX = pSceneTouchEvent.getX();
							mPreviousY = pSceneTouchEvent.getY();
						}
					}else{
						finishedState(pSceneTouchEvent);
					}
					break;
					
				case ROTATE:					
					if (hasTwoOrMorePointers){						
						prepareTwoPointers(motionEvent);
						final float degress = calculateRotationDegress();
						if(Math.abs(degress) > TRIGGER_ROTATE_MINIMUM_DEGRESS_DEFAULT && mCurrentTwoDistance > mTriggerRotateMinimumDistance) {
							mSmartDetectorListener.onRotate(this, pSceneTouchEvent, degress);
							mPreviousTwoXDistance = mCurrentTwoXDistance;
							mPreviousTwoYDistance = mCurrentTwoYDistance;
						}			
					}else{
						finishedState(pSceneTouchEvent);
					}					
					break;
					
				case PINCHZOOM:
					if (hasTwoOrMorePointers){
						prepareTwoPointers(motionEvent);
						if(mCurrentTwoDistance > mTriggerPinchZoomMinimumDistance) {
							mSmartDetectorListener.onPinchZoom(this, pSceneTouchEvent, getZoomFactor());
						}
					}else{
						finishedState(pSceneTouchEvent);
					}
					break;
				}
				break;
				
				case TouchEvent.ACTION_UP:
				case TouchEvent.ACTION_CANCEL:	
					//Debug.e("ACTION_UP ACTION_CANCEL "+pointerID);
					finishedState(pSceneTouchEvent);
					break;
		}
		
		return true;
	}
	

	private void finishedState(final TouchEvent pSceneTouchEvent) {
		switch (mState) {
		case SCROOL:
			mSmartDetectorListener.onScrollFinished(this, pSceneTouchEvent, mCurrentXDistance, mCurrentYDistance);
			mPreviousX = mPreviousY = 0;
			mCurrentXDistance = mCurrentYDistance = 0;
			break;
		case ROTATE:
			mSmartDetectorListener.onRotateFinished(this, pSceneTouchEvent, calculateRotationDegress());
			mPreviousTwoXDistance = mCurrentTwoXDistance = 0;
			mPreviousTwoYDistance = mCurrentTwoYDistance = 0;
			break;
		case PINCHZOOM:
			mSmartDetectorListener.onPinchZoomFinished(this, pSceneTouchEvent, getZoomFactor());
			mCurrentTwoDistance = 0;
			break;
		default:
			break;
		}
		mState = State.NONE;
		mTriggering = false;
	}

	
	private void prepareOnePointer(final TouchEvent pSceneTouchEvent){	
		mCurrentXDistance = pSceneTouchEvent.getX() - mPreviousX;
		mCurrentYDistance = pSceneTouchEvent.getY() - mPreviousY;
	}

	private void prepareTwoPointers(final MotionEvent pMotionEvent){		
		mCurrentTwoXDistance = calculateXDistance(pMotionEvent);
		mCurrentTwoYDistance = calculateYDistance(pMotionEvent);
		mCurrentTwoDistance = calculateDistance(mCurrentTwoXDistance, mCurrentTwoYDistance);
	}
	
	
	private boolean hasOnePointer(final MotionEvent pMotionEvent) {
		return pMotionEvent.getPointerCount() == 1;
	}
	
	
	private boolean hasTwoOrMorePointers(final MotionEvent pMotionEvent) {
		return pMotionEvent.getPointerCount() >= 2;
	}

	
	public final float calculateDistance(final float pDX, final float pDY){
		return (float)Math.sqrt((pDX * pDX) + (pDY * pDY));
	}
	
	
	private static float calculateXDistance(final MotionEvent pMotionEvent) {
		return (pMotionEvent.getX(1)-pMotionEvent.getX(0));
	}
	
	
	private static float calculateYDistance(final MotionEvent pMotionEvent) {
		return (pMotionEvent.getY(1)-pMotionEvent.getY(0));
	}
	
	
	private float calculateRotationDegress() {
		double diffRadians = Math.atan2(mPreviousTwoYDistance, mPreviousTwoXDistance) - Math.atan2(mCurrentTwoYDistance, mCurrentTwoXDistance);
		return (float) (diffRadians * 180 / Math.PI);
	}
	
	
	public float getRotationDegress() {
		return calculateRotationDegress();
	}
	
	
	public float getZoomFactor() {
		if (mCurrentTwoDistance == 0 || mInitialTwoDistance == 0)
			return 1;
		return mCurrentTwoDistance / mInitialTwoDistance;
	}
	
	
	
	private enum State {

		NONE, SCROOL, ROTATE, PINCHZOOM;

	}
	
		
	
	public static interface ISmartDetectorListener extends ISmartScrollDetectorListener, ISmartRotateDetectorListener, ISmartPinchZoomDetectorListener{

		
	}
	
	
	public static interface ISmartScrollDetectorListener {		
		public void onScrollStarted(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY);
		public void onScroll(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY);
		public void onScrollFinished(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY);
	}
	
	
	public static interface ISmartRotateDetectorListener {
		public void onRotateStarted(final SmartDetector pSmartDetector, final TouchEvent pSceneTouchEvent, final float pDistanceDegress);
		public void onRotate(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pDistanceDegress);
		public void onRotateFinished(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pDistanceDegress);
	}
	
	
	public static interface ISmartPinchZoomDetectorListener {
		public void onPinchZoomStarted(final SmartDetector pSmartDetector, final TouchEvent pSceneTouchEvent, final float pZoomFactor);
		public void onPinchZoom(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pZoomFactor);
		public void onPinchZoomFinished(final SmartDetector pSmartDetector, final TouchEvent pTouchEvent, final float pZoomFactor);
	}

}
