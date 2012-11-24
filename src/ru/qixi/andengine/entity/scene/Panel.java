package ru.qixi.andengine.entity.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.adt.list.SmartList;

import android.util.SparseArray;



public class Panel extends Entity implements IPanel{


	private static final int TOUCHAREAS_CAPACITY_DEFAULT = 4;

	protected SmartList<ITouchArea> mTouchAreas = new SmartList<ITouchArea>(Panel.TOUCHAREAS_CAPACITY_DEFAULT);
	private IOnAreaTouchListener mOnAreaTouchListener;
	private boolean mOnAreaTouchTraversalBackToFront = true;

	private boolean mTouchAreaBindingOnActionDownEnabled = false;
	private boolean mTouchAreaBindingOnActionMoveEnabled = false;
	private final SparseArray<ITouchArea> mTouchAreaBindings = new SparseArray<ITouchArea>();
	

	
	public Panel(){
		
	}
			

	public Panel(final float pX, final float pY) {
		super(pX, pY);
	}


	public void setOnAreaTouchListener(final IOnAreaTouchListener pOnAreaTouchListener) {
		mOnAreaTouchListener = pOnAreaTouchListener;
	}

	
	public IOnAreaTouchListener getOnAreaTouchListener() {
		return mOnAreaTouchListener;
	}

	
	public boolean hasOnAreaTouchListener() {
		return mOnAreaTouchListener != null;
	}



	public void setOnAreaTouchTraversalBackToFront() {
		mOnAreaTouchTraversalBackToFront = true;
	}

	
	public void setOnAreaTouchTraversalFrontToBack() {
		mOnAreaTouchTraversalBackToFront = false;
	}

	
	public boolean isTouchAreaBindingOnActionDownEnabled() {
		return mTouchAreaBindingOnActionDownEnabled;
	}

	
	public boolean isTouchAreaBindingOnActionMoveEnabled() {
		return mTouchAreaBindingOnActionMoveEnabled;
	}


	public void setTouchAreaBindingOnActionDownEnabled(final boolean pTouchAreaBindingOnActionDownEnabled) {
		if(mTouchAreaBindingOnActionDownEnabled && !pTouchAreaBindingOnActionDownEnabled) {
			mTouchAreaBindings.clear();
		}
		mTouchAreaBindingOnActionDownEnabled = pTouchAreaBindingOnActionDownEnabled;
	}


	public void setTouchAreaBindingOnActionMoveEnabled(final boolean pTouchAreaBindingOnActionMoveEnabled) {
		if(mTouchAreaBindingOnActionMoveEnabled && !pTouchAreaBindingOnActionMoveEnabled) {
			mTouchAreaBindings.clear();
		}
		mTouchAreaBindingOnActionMoveEnabled = pTouchAreaBindingOnActionMoveEnabled;
	}


	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		{
			pGLState.pushProjectionGLMatrix();

			onApplyMatrix(pGLState, pCamera);
			pGLState.loadModelViewGLMatrixIdentity();

			super.onManagedDraw(pGLState, pCamera);

			pGLState.popProjectionGLMatrix();
		}
	}

	
	protected void onApplyMatrix(final GLState pGLState, final Camera pCamera) {
		pCamera.onApplySceneMatrix(pGLState);
	}


	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		final int action = pSceneTouchEvent.getAction();
		final boolean isActionDown = pSceneTouchEvent.isActionDown();
		final boolean isActionMove = pSceneTouchEvent.isActionMove();

		if(!isActionDown) {
			if(mTouchAreaBindingOnActionDownEnabled) {
				final SparseArray<ITouchArea> touchAreaBindings = mTouchAreaBindings;
				final ITouchArea boundTouchArea = touchAreaBindings.get(pSceneTouchEvent.getPointerID());

				if(boundTouchArea != null) {
					final float sceneTouchEventX = pSceneTouchEvent.getX();
					final float sceneTouchEventY = pSceneTouchEvent.getY();

					switch(action) {
						case TouchEvent.ACTION_UP:
						case TouchEvent.ACTION_CANCEL:
							touchAreaBindings.remove(pSceneTouchEvent.getPointerID());
					}
					final Boolean handled = onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, boundTouchArea);
					if(handled != null && handled) {
						return true;
					}
				}
			}
		}

		final float sceneTouchEventX = pSceneTouchEvent.getX();
		final float sceneTouchEventY = pSceneTouchEvent.getY();

		final SmartList<ITouchArea> touchAreas = mTouchAreas;
		if(touchAreas != null) {
			final int touchAreaCount = touchAreas.size();
			if(touchAreaCount > 0) {
				if(mOnAreaTouchTraversalBackToFront) { 
					for(int i = 0; i < touchAreaCount; i++) {
						final ITouchArea touchArea = touchAreas.get(i);
						if(touchArea.contains(sceneTouchEventX, sceneTouchEventY)) {
							final Boolean handled = onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchArea);
							if(handled != null && handled) {
								if((mTouchAreaBindingOnActionDownEnabled && isActionDown) || (mTouchAreaBindingOnActionMoveEnabled && isActionMove)) {
									mTouchAreaBindings.put(pSceneTouchEvent.getPointerID(), touchArea);
								}
								return true;
							}
						}
					}
				} else { 
					for(int i = touchAreaCount - 1; i >= 0; i--) {
						final ITouchArea touchArea = touchAreas.get(i);
						if(touchArea.contains(sceneTouchEventX, sceneTouchEventY)) {
							final Boolean handled = onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchArea);
							if(handled != null && handled) {
								if((mTouchAreaBindingOnActionDownEnabled && isActionDown) || (mTouchAreaBindingOnActionMoveEnabled && isActionMove)) {
									mTouchAreaBindings.put(pSceneTouchEvent.getPointerID(), touchArea);
								}
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	
	private Boolean onAreaTouchEvent(final TouchEvent pSceneTouchEvent, final float sceneTouchEventX, final float sceneTouchEventY, final ITouchArea touchArea) {
		final float[] touchAreaLocalCoordinates = touchArea.convertSceneCoordinatesToLocalCoordinates(sceneTouchEventX, sceneTouchEventY);
		final float touchAreaLocalX = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_X];
		final float touchAreaLocalY = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_Y];

		final boolean handledSelf = touchArea.onAreaTouched(pSceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
		if(handledSelf) {
			return Boolean.TRUE;
		} else if(mOnAreaTouchListener != null) {
			return mOnAreaTouchListener.onAreaTouched(pSceneTouchEvent, touchArea, touchAreaLocalX, touchAreaLocalY);
		} else {
			return null;
		}
	}


	public void registerTouchArea(final ITouchArea pTouchArea) {
		mTouchAreas.add(pTouchArea);
	}

	
	public boolean unregisterTouchArea(final ITouchArea pTouchArea) {
		return mTouchAreas.remove(pTouchArea);
	}

	
	public boolean unregisterTouchAreas(final ITouchAreaMatcher pTouchAreaMatcher) {
		return mTouchAreas.removeAll(pTouchAreaMatcher);
	}

	
	public void clearTouchAreas() {
		mTouchAreas.clear();
	}

	
	public SmartList<ITouchArea> getTouchAreas() {
		return mTouchAreas;
	}


}
