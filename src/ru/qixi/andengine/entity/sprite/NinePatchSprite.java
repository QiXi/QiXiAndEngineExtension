package ru.qixi.andengine.entity.sprite;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class NinePatchSprite extends SpriteBatch {
	
	private NinePatchRegion mRegion;
	private NinePatchOptions mOptions;
	
	
	public NinePatchSprite(float pX, float pY,  final ITextureRegion pTextureRegion, final NinePatchOptions pOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion.getTexture(), 9, pVertexBufferObjectManager);

		mOptions = pOptions;
		
		mRegion = new NinePatchRegion(pTextureRegion);
		mRegion.updateTextureRegions(mOptions);
		
		updateVertices();
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
	
	
	private void updateVertices() {
		//reset();
		
		final NinePatchOptions options = mOptions;
		final NinePatchRegion region = mRegion;
		
		final float mInsetLeft = options.mInsetLeft;
		final float mInsetTop = options.mInsetTop;
		final float mInsetRight = options.mInsetRight;
		final float mInsetBottom = options.mInsetBottom;

		final float color = mColor.getABGRPackedFloat();

		final float centerWidth = mWidth - mInsetLeft - mInsetRight;
		final float centerHeight = mHeight - mInsetTop - mInsetBottom;

		final float leftX = 0;
		final float centerX = mInsetLeft;
		final float rightX = mWidth - mInsetRight;

		final float topY = mHeight - mInsetTop;
		draw(region.mTopLeftTextureRegion, leftX, topY, mInsetLeft, mInsetTop, color);
		draw(region.mTopCenterTextureRegion, centerX, topY, centerWidth, mInsetTop, color);
		draw(region.mTopRightTextureRegion, rightX, topY, mInsetRight, mInsetTop, color);

		final float centerY = mInsetBottom;
		draw(region.mCenterLeftTextureRegion, leftX, centerY, mInsetLeft, centerHeight, color);
		draw(region.mCenterTextureRegion, centerX, centerY, centerWidth, centerHeight, color);
		draw(region.mCenterRightTextureRegion, rightX, centerY, mInsetRight, centerHeight, color);

		final float bottomY = 0;
		draw(region.mBottomLeftTextureRegion, leftX, bottomY, mInsetLeft, mInsetBottom, color);
		draw(region.mBottomCenterTextureRegion, centerX, bottomY, centerWidth, mInsetBottom, color);
		draw(region.mBottomRightTextureRegion, rightX, bottomY, mInsetRight, mInsetBottom, color);

		submit();
	}

}
