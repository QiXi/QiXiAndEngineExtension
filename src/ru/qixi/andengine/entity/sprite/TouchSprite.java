package ru.qixi.andengine.entity.sprite;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class TouchSprite extends Sprite{

	public static final int STATE_NORMAL = 0;
	public static final int STATE_PRESSED = 1;
	
	
	private OnTouchListener mOnTouchListener;
	private int mState;
	

	public TouchSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTextureRegion, pVertexBufferObjectManager, (OnTouchListener) null);
	}

	
	public TouchSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnTouchListener pOnTouchListener) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mOnTouchListener = pOnTouchListener;
		changeState(STATE_NORMAL);
	}

	
	public void setOnClickListener(final OnTouchListener pOnTouchListener) {
		mOnTouchListener = pOnTouchListener;
	}
	
		
	public void setOnTouchListener(final OnTouchListener pOnTouchListener) {
		mOnTouchListener = pOnTouchListener;
	}


	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(pSceneTouchEvent.isActionDown()) {
			changeState(STATE_PRESSED);
			
		}else if(pSceneTouchEvent.isActionCancel() || !this.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
			changeState(STATE_NORMAL);
			
		} else if(pSceneTouchEvent.isActionUp() && mState == STATE_PRESSED) {
			changeState(STATE_NORMAL);
			
			if(mOnTouchListener != null) {
				mOnTouchListener.onClick(this, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}
		return true;
	}
	

	
	@Override
	public boolean contains(final float pX, final float pY) {
		if(!this.isVisible()) {
			return false;
		} else {
			return super.contains(pX, pY);
		}
	}
	
	
	public boolean isPressed() {
		return mState == STATE_PRESSED;
	}

	
	public int getState() {
		return mState;
	}
	
	
	private void changeState(final int pState) {
		if(pState == mState) {
			return;
		}
		mState = pState;
	}
	
	
	public interface OnTouchListener {		
		public void onClick(final TouchSprite pTouchSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	}
	
	
}
