package ru.qixi.andengine.entity.sprite;


public class NinePatchOptions {

	protected float mInsetLeft;
	protected float mInsetTop;
	protected float mInsetRight;
	protected float mInsetBottom;


	public NinePatchOptions(final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom) {
		mInsetLeft = pInsetLeft;
		mInsetTop = pInsetTop;
		mInsetRight = pInsetRight;
		mInsetBottom = pInsetBottom;
	}
	
	
	public float getInsetLeft() {
		return mInsetLeft;
	}


	public void setInsetLeft(final float pInsetLeft) {
		mInsetLeft = pInsetLeft;
	}


	public float getInsetTop() {
		return mInsetTop;
	}


	public void setInsetTop(final float pInsetTop) {
		mInsetTop = pInsetTop;
	}


	public float getInsetRight() {
		return mInsetRight;
	}


	public void setInsetRight(final float pInsetRight) {
		mInsetRight = pInsetRight;
	}


	public float getInsetBottom() {
		return mInsetBottom;
	}


	public void setInsetBottom(final float pInsetBottom) {
		mInsetBottom = pInsetBottom;
	}

}
