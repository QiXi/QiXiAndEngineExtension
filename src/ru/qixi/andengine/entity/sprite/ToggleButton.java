package ru.qixi.andengine.entity.sprite;


import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class ToggleButton extends TiledSprite {
	
	public static final int STATE_NORMAL 	= 0;
	public static final int STATE_PRESSED 	= 1;
	public static final int STATE_DISABLED 	= 2;

	private final int mStateCount;
	private OnCheckedChangeListener mOnCheckedChangeListener;

	private boolean mEnabled = true;
	private int mState;
	private boolean mChecked;

	
	
	public ToggleButton(final float pX, final float pY, final ITextureRegion pNormal1TextureRegion, final ITextureRegion pNormal2TextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pNormal1TextureRegion, pNormal2TextureRegion, pVertexBufferObjectManager, (OnCheckedChangeListener) null);
	}

	
	public ToggleButton(final float pX, final float pY, final ITextureRegion pNormal1TextureRegion, final ITextureRegion pNormal2TextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnCheckedChangeListener pOnClickListener) {
		this(pX, pY, new TiledTextureRegion(pNormal1TextureRegion.getTexture(), pNormal1TextureRegion, pNormal2TextureRegion), pVertexBufferObjectManager, pOnClickListener);
	}

	
	public ToggleButton(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, (OnCheckedChangeListener) null);
	}

	
	public ToggleButton(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnCheckedChangeListener pOnClickListener) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

		mOnCheckedChangeListener = pOnClickListener;
		mStateCount = pTiledTextureRegion.getTileCount();

		switch(mStateCount) {
			case 1:
				//
			case 2:
				//
				break;
			default:
				throw new IllegalArgumentException("The supplied " + ITiledTextureRegion.class.getSimpleName() + " has an unexpected amount of states: '" + mStateCount + "'.");
		}
		changeState(STATE_NORMAL);
		setChecked(false);
	}


	public boolean isEnabled() {
		return mEnabled;
	}

	
	public void setEnabled(final boolean pEnabled) {
		mEnabled = pEnabled;

		if(mEnabled && mState == STATE_DISABLED) {
			changeState(STATE_NORMAL);
		} else if(!mEnabled) {
			changeState(STATE_DISABLED);
		}
	}

	
	public boolean isPressed() {
		return mState == STATE_PRESSED;
	}

	
	public int getState() {
		return mState;
	}

	
	public void setOnClickListener(final OnCheckedChangeListener pOnClickListener) {
		mOnCheckedChangeListener = pOnClickListener;
	}


	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(!isEnabled()) {
			changeState(STATE_DISABLED);
		} else if(pSceneTouchEvent.isActionDown()) {
			changeState(STATE_PRESSED);
		} else if(pSceneTouchEvent.isActionCancel() || !contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
			changeState(STATE_NORMAL);
		} else if(pSceneTouchEvent.isActionUp() && mState == STATE_PRESSED) {
			changeState(STATE_NORMAL);
			toggle();
		}
		return true;
	}
	

	@Override
	public boolean contains(final float pX, final float pY) {
		if(!isVisible()) {
			return false;
		} else {
			return super.contains(pX, pY);
		}
	}


	private void changeState(final int pState) {
		if(pState == mState) {
			return;
		}

		mState = pState;	
	}
	
	
	public void toggle() {
        setChecked(!mChecked);
    }

	
    public boolean isChecked() {
        return mChecked;
    }
    
	
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            
            final int stateTiledTextureRegionIndex = (mChecked)?1:0;
    		if(stateTiledTextureRegionIndex >= mStateCount) {
    			setCurrentTileIndex(0);
    		} else {
    			setCurrentTileIndex(stateTiledTextureRegionIndex);
    		}
            
            if(mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
			}           
        }
    }
    
    
	public interface OnCheckedChangeListener {
		public void onCheckedChanged(final ToggleButton pButtonSprite, final boolean isChecked);
	}

	
	
}
