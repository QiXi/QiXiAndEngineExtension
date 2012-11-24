package ru.qixi.andengine.entity.scene;

import org.andengine.entity.scene.ITouchArea;



public interface IPanel {
	
	void registerTouchArea(final ITouchArea pTouchArea);
	boolean unregisterTouchArea(final ITouchArea pTouchArea);
	
}
