package ru.qixi.andengine.entity.sprite;


import org.andengine.entity.Entity;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;


public class NinePatchButton extends Entity{


	private final int mStateCount;
	private OnClickListener mOnClickListener;

	private boolean mEnabled = true;
	private State mState;	

	protected final ITiledTextureRegion mTextureRegion;
	
	protected SpriteBatch mSpriteBatchNormal;
	protected SpriteBatch mSpriteBatchPressed;
	protected SpriteBatch mSpriteBatchDisabled;
	
	protected NinePatchRegion mNinePatch;
	protected NinePatchOptions mOptions;

	
	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pOptions, pNormalTextureRegion, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance(), (OnClickListener) null);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram, final OnClickListener pOnClickListener) {
		this(pX, pY, pOptions, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion), pVertexBufferObjectManager, pShaderProgram, pOnClickListener);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pOptions, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance(), (OnClickListener) null);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram, final OnClickListener pOnClickListener) {
		this(pX, pY, pOptions, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion), pVertexBufferObjectManager, pShaderProgram, pOnClickListener);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pOptions, pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance(), (OnClickListener) null);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram, final OnClickListener pOnClickListener) {
		this(pX, pY, pOptions, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion), pVertexBufferObjectManager, pShaderProgram, pOnClickListener);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pOptions, pTiledTextureRegion, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance(), (OnClickListener) null);
	}

	
	public NinePatchButton(final float pX, final float pY, final NinePatchOptions pOptions, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram, final OnClickListener pOnClickListener) {
		super(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight());
		
		mOptions = pOptions;
		mTextureRegion = pTiledTextureRegion;
		mStateCount = pTiledTextureRegion.getTileCount();
		mOnClickListener = pOnClickListener;
		
		final ITexture texture = pTiledTextureRegion.getTexture();		
				
		switch(mStateCount) {
			case 3:
				mSpriteBatchDisabled = new SpriteBatch(texture, 9, pVertexBufferObjectManager, pShaderProgram);
				attachChild(mSpriteBatchDisabled);
			case 2:
				mSpriteBatchPressed = new SpriteBatch(texture, 9, pVertexBufferObjectManager, pShaderProgram);
				attachChild(mSpriteBatchPressed);
				Debug.w("No " + ITextureRegion.class.getSimpleName() + " supplied for " + State.class.getSimpleName() + "." + State.DISABLED + ".");
			case 1:
				mSpriteBatchNormal = new SpriteBatch(texture, 9, pVertexBufferObjectManager, pShaderProgram);
				attachChild(mSpriteBatchNormal);
				Debug.w("No " + ITextureRegion.class.getSimpleName() + " supplied for " + State.class.getSimpleName() + "." + State.PRESSED + ".");			
				break;
			default:
				throw new IllegalArgumentException("The supplied " + ITiledTextureRegion.class.getSimpleName() + " has an unexpected amount of states: '" + mStateCount + "'.");
		}

		mNinePatch = new NinePatchRegion(mTextureRegion);
		mNinePatch.updateTextureRegions(pOptions);
		updateVertices();
		
		changeState(State.NORMAL);
	}


	public boolean isEnabled() {
		return mEnabled;
	}

	
	public void setEnabled(final boolean pEnabled) {
		mEnabled = pEnabled;

		if(mEnabled && mState == State.DISABLED) {
			changeState(State.NORMAL);
		} else if(!mEnabled) {
			changeState(State.DISABLED);
		}
	}

	
	public boolean isPressed() {
		return mState == State.PRESSED;
	}

	
	public State getState() {
		return mState;
	}

	
	public void setOnClickListener(final OnClickListener pOnClickListener) {
		mOnClickListener = pOnClickListener;
	}



	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(!isEnabled()) {
			changeState(State.DISABLED);
		} else if(pSceneTouchEvent.isActionDown()) {
			changeState(State.PRESSED);
		} else if(pSceneTouchEvent.isActionCancel() || !contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
			changeState(State.NORMAL);
		} else if(pSceneTouchEvent.isActionUp() && mState == State.PRESSED) {
			changeState(State.NORMAL);
			if(mOnClickListener != null) {
				mOnClickListener.onClick(this, pTouchAreaLocalX, pTouchAreaLocalY);
			}
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


	private void changeState(final State pState) {
		if(pState == mState) {
			return;
		}

		mState = pState;

		final int stateTiledTextureRegionIndex = mState.getTiledTextureRegionIndex();
		if(stateTiledTextureRegionIndex >= mStateCount) {
			mTextureRegion.setCurrentTileIndex(0);
			Debug.w(getClass().getSimpleName() + " changed its " + State.class.getSimpleName() + " to " + pState.toString() + ", which doesn't have a " + ITextureRegion.class.getSimpleName() + " supplied. Applying default " + ITextureRegion.class.getSimpleName() + ".");
		} else {
			mTextureRegion.setCurrentTileIndex(stateTiledTextureRegionIndex);
		}		
		
		setVisibleBatch(false);
		
		final int index = mTextureRegion.getCurrentTileIndex();
		switch (index) {
		case 0:
			mSpriteBatchNormal.setVisible(true);
			break;
		case 1:
			mSpriteBatchPressed.setVisible(true);
			break;
		case 2:
			mSpriteBatchDisabled.setVisible(true);
			break;
		}
	}
	
	
	@Override
	public void setWidth(final float pWidth) {
		super.setWidth(pWidth);

		updateVertices();
	}

	
	@Override
	public void setHeight(final float pHeight) {
		super.setHeight(pHeight);

		updateVertices();
	}

	
	@Override
	public void setSize(final float pWidth, final float pHeight) {
		super.setSize(pWidth, pHeight);

		updateVertices();
	}

	
	@Override
	protected void onUpdateColor() {
		updateVertices();
	}

	
	@Override
	public void dispose() {
		super.dispose();

		mSpriteBatchNormal.dispose();
		mSpriteBatchPressed.dispose();
		mSpriteBatchDisabled.dispose();
	}

	
	private void setVisibleBatch(boolean pVisible){
		switch (mStateCount) {
		case 3:
			mSpriteBatchDisabled.setVisible(pVisible);
		case 2:
			mSpriteBatchPressed.setVisible(pVisible);
		case 1:
			mSpriteBatchNormal.setVisible(pVisible);
			break;
		}
	}
	



	
	private void updateVertices(){
		switch (mStateCount) {
		case 3:
			updateVertices(mSpriteBatchDisabled, mOptions);
		case 2:
			updateVertices(mSpriteBatchPressed, mOptions);
		case 1:
			updateVertices(mSpriteBatchNormal, mOptions);
			break;
		default:
			throw new IllegalArgumentException("The supplied " + ITiledTextureRegion.class.getSimpleName() + " has an unexpected amount of states: '" + mStateCount + "'.");
		}
	}
	
	private void updateVertices(final SpriteBatch pSpriteBatch, final NinePatchOptions pOptions) {
		pSpriteBatch.reset();
		
		final float mInsetLeft = pOptions.mInsetLeft;
		final float mInsetTop = pOptions.mInsetTop;
		final float mInsetRight = pOptions.mInsetRight;
		final float mInsetBottom = pOptions.mInsetBottom;

		final float color = mColor.getABGRPackedFloat();

		final float centerWidth = mWidth - mInsetLeft - mInsetRight;
		final float centerHeight = mHeight - mInsetTop - mInsetBottom;

		final float leftX = 0;
		final float centerX = mInsetLeft;
		final float rightX = mWidth - mInsetRight;

		final float topY = mHeight - mInsetTop;
		pSpriteBatch.draw(mNinePatch.mTopLeftTextureRegion, leftX, topY, mInsetLeft, mInsetTop, color);
		pSpriteBatch.draw(mNinePatch.mTopCenterTextureRegion, centerX, topY, centerWidth, mInsetTop, color);
		pSpriteBatch.draw(mNinePatch.mTopRightTextureRegion, rightX, topY, mInsetRight, mInsetTop, color);

		final float centerY = mInsetBottom;
		pSpriteBatch.draw(mNinePatch.mCenterLeftTextureRegion, leftX, centerY, mInsetLeft, centerHeight, color);
		pSpriteBatch.draw(mNinePatch.mCenterTextureRegion, centerX, centerY, centerWidth, centerHeight, color);
		pSpriteBatch.draw(mNinePatch.mCenterRightTextureRegion, rightX, centerY, mInsetRight, centerHeight, color);

		final float bottomY = 0;
		pSpriteBatch.draw(mNinePatch.mBottomLeftTextureRegion, leftX, bottomY, mInsetLeft, mInsetBottom, color);
		pSpriteBatch.draw(mNinePatch.mBottomCenterTextureRegion, centerX, bottomY, centerWidth, mInsetBottom, color);
		pSpriteBatch.draw(mNinePatch.mBottomRightTextureRegion, rightX, bottomY, mInsetRight, mInsetBottom, color);

		pSpriteBatch.submit();
	}
	

	
	public interface OnClickListener {
		public void onClick(final NinePatchButton pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	}

	
	public static enum State {
		NORMAL(0),
		PRESSED(1),
		DISABLED(2);

		private final int mTiledTextureRegionIndex;


		private State(final int pTiledTextureRegionIndex) {
			mTiledTextureRegionIndex = pTiledTextureRegionIndex;
		}

		public int getTiledTextureRegionIndex() {
			return mTiledTextureRegionIndex;
		}
	}
	
}
