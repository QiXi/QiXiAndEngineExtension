package ru.qixi.andengine.entity.sprite;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;


public class NinePatchRegion {
		
	protected final ITextureRegion mTopLeftTextureRegion;
	protected final ITextureRegion mTopCenterTextureRegion;
	protected final ITextureRegion mTopRightTextureRegion;
	protected final ITextureRegion mCenterLeftTextureRegion;
	protected final ITextureRegion mCenterTextureRegion;
	protected final ITextureRegion mCenterRightTextureRegion;
	protected final ITextureRegion mBottomLeftTextureRegion;
	protected final ITextureRegion mBottomCenterTextureRegion;
	protected final ITextureRegion mBottomRightTextureRegion;
	
	private final ITextureRegion mTextureRegion;
	
	
	public NinePatchRegion(final ITextureRegion pTextureRegion){
		mTextureRegion = pTextureRegion;

		final ITextureRegion region = new TextureRegion(pTextureRegion.getTexture(), 0, 0, 0, 0);
		
		mTopLeftTextureRegion = region.deepCopy();
		mTopCenterTextureRegion = region.deepCopy();
		mTopRightTextureRegion = region.deepCopy();
		mCenterLeftTextureRegion = region.deepCopy();
		mCenterTextureRegion = region.deepCopy();
		mCenterRightTextureRegion = region.deepCopy();
		mBottomLeftTextureRegion = region.deepCopy();
		mBottomCenterTextureRegion = region.deepCopy();
		mBottomRightTextureRegion = region.deepCopy();
	}
	
	
	public void updateTextureRegions(final NinePatchOptions pOptions) {		
		final float mInsetLeft = pOptions.mInsetLeft;
		final float mInsetTop = pOptions.mInsetTop;
		final float mInsetRight = pOptions.mInsetRight;
		final float mInsetBottom = pOptions.mInsetBottom;
		
		final float baseX = mTextureRegion.getTextureX();
		final float baseY = mTextureRegion.getTextureY();
		final float baseWidth = mTextureRegion.getWidth();
		final float baseHeight = mTextureRegion.getHeight();

		final float centerWidth = baseWidth - mInsetLeft - mInsetRight;
		final float centerHeight = baseHeight - mInsetTop - mInsetBottom;

		final float leftX = baseX;
		final float centerX = baseX + mInsetLeft;
		final float rightX = (baseX + baseWidth) - mInsetRight;

		final float topY = baseY;
		mTopLeftTextureRegion.set(leftX, topY, mInsetLeft, mInsetTop);
		mTopCenterTextureRegion.set(centerX, topY, centerWidth, mInsetTop);
		mTopRightTextureRegion.set(rightX, topY, mInsetRight, mInsetTop);

		final float centerY = baseY + mInsetTop;
		mCenterLeftTextureRegion.set(leftX, centerY, mInsetLeft, centerHeight);
		mCenterTextureRegion.set(centerX, centerY, centerWidth, centerHeight);
		mCenterRightTextureRegion.set(rightX, centerY, mInsetRight, centerHeight);

		final float bottomY = (baseY + baseHeight) - mInsetBottom;
		mBottomLeftTextureRegion.set(leftX, bottomY, mInsetLeft, mInsetBottom);
		mBottomCenterTextureRegion.set(centerX, bottomY, centerWidth, mInsetBottom);
		mBottomRightTextureRegion.set(rightX, bottomY, mInsetRight, mInsetBottom);
	}
	
	public ITextureRegion getTopLeftTextureRegion() {
		return mTopLeftTextureRegion;
	}

	public ITextureRegion getTopCenterTextureRegion() {
		return mTopCenterTextureRegion;
	}

	public ITextureRegion getTopRightTextureRegion() {
		return mTopRightTextureRegion;
	}

	public ITextureRegion getCenterLeftTextureRegion() {
		return mCenterLeftTextureRegion;
	}

	public ITextureRegion getCenterTextureRegion() {
		return mCenterTextureRegion;
	}

	public ITextureRegion getCenterRightTextureRegion() {
		return mCenterRightTextureRegion;
	}

	public ITextureRegion getBottomLeftTextureRegion() {
		return mBottomLeftTextureRegion;
	}

	public ITextureRegion getBottomCenterTextureRegion() {
		return mBottomCenterTextureRegion;
	}

	public ITextureRegion getBottomRightTextureRegion() {
		return mBottomRightTextureRegion;
	}

}
