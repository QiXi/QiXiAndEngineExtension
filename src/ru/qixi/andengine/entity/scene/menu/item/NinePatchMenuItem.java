package ru.qixi.andengine.entity.scene.menu.item;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ru.qixi.andengine.entity.sprite.NinePatchOptions;
import ru.qixi.andengine.entity.sprite.NinePatchSprite;




public class NinePatchMenuItem extends Entity implements IMenuItem{

	//private final ITextureRegion mTextureRegion;
	private NinePatchSprite mNinePatchNormal;
	private NinePatchSprite mNinePatchPressed;
	private NinePatchOptions mOptions;	
	private Text text;	
	private final int menuID;
	
	
	public NinePatchMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final ITextureRegion pTextureRegion2, final NinePatchOptions pOptions, final Text pText, final VertexBufferObjectManager pVertexBufferObjectManager){
		this(pID, pWidth, pHeight, pTextureRegion, pTextureRegion2, pOptions, pText, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}
	
	
	public NinePatchMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final ITextureRegion pTextureRegion2, final NinePatchOptions pOptions, final Text pText, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram){
		super(0, 0, pWidth, pHeight);

		mOptions = pOptions;
		
		//setIgnoreUpdate(true);
		setChildrenIgnoreUpdate(true);
		
		text = pText;
		text.setZIndex(1);
		//mTextureRegion = pTextureRegion;

		mNinePatchNormal = new NinePatchSprite(pWidth/2, pHeight/2, pTextureRegion, mOptions, pVertexBufferObjectManager);
		mNinePatchNormal.setSize(pWidth, pHeight);
		
		mNinePatchPressed = new NinePatchSprite(pWidth/2, pHeight/2, pTextureRegion2, mOptions, pVertexBufferObjectManager);
		mNinePatchPressed.setSize(pWidth, pHeight);
		
		attachChild(mNinePatchNormal);
		attachChild(text);
		
		menuID = pID;
	}
	
	
	@Override
	public void setWidth(final float pWidth) {
		super.setWidth(pWidth);

		mNinePatchNormal.setWidth(pWidth);
		mNinePatchPressed.setWidth(pWidth);
	}

	
	@Override
	public void setHeight(final float pHeight) {
		super.setHeight(pHeight);

		mNinePatchNormal.setHeight(pHeight);
		mNinePatchPressed.setHeight(pHeight);
	}

	
	@Override
	public void setSize(final float pWidth, final float pHeight) {
		super.setSize(pWidth, pHeight);

		mNinePatchNormal.setSize(pWidth, pHeight);
		mNinePatchPressed.setSize(pWidth, pHeight);
	}

	
	@Override
	protected void onUpdateColor() {
		super.onUpdateColor();
		
		//
	}
	
	
	
	@Override
	public boolean isBlendingEnabled() {
		return false;
	}

	
	@Override
	public void setBlendingEnabled(boolean pBlendingEnabled) {

	}

	
	@Override
	public int getBlendFunctionSource() {
		return 0;
	}

	
	@Override
	public int getBlendFunctionDestination() {
		return 0;
	}

	
	@Override
	public void setBlendFunctionSource(int pBlendFunctionSource) {
	
	}

	
	@Override
	public void setBlendFunctionDestination(int pBlendFunctionDestination) {

	}

	
	@Override
	public void setBlendFunction(int pBlendFunctionSource, int pBlendFunctionDestination) {
		
	}

	
	@Override
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return null;
	}

	
	@Override
	public IVertexBufferObject getVertexBufferObject() {
		return null;
	}

	
	@Override
	public ShaderProgram getShaderProgram() {
		return null;
	}
	

	@Override
	public void setShaderProgram(ShaderProgram pShaderProgram) {

	}

	
	@Override
	public int getID() {
		return menuID;
	}


	@Override
	public void onSelected() {
		detachChildren();
		attachChild(mNinePatchPressed);
		attachChild(text);
	}

	
	@Override
	public void onUnselected() {
		detachChildren();
		attachChild(mNinePatchNormal);
		attachChild(text);
	}

}
