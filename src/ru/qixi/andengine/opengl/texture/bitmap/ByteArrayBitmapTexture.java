package ru.qixi.andengine.opengl.texture.bitmap;

import java.io.IOException;

import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.util.adt.io.in.ByteArrayInputStreamOpener;



public class ByteArrayBitmapTexture extends BitmapTexture {
	
	
	public ByteArrayBitmapTexture(final TextureManager pTextureManager, final byte[] pBytes) throws IOException {
		super(pTextureManager, new ByteArrayInputStreamOpener(pBytes));
	}

	
	public ByteArrayBitmapTexture(final TextureManager pTextureManager, final byte[] pBytes, final BitmapTextureFormat pBitmapTextureFormat) throws IOException {
		super(pTextureManager, new ByteArrayInputStreamOpener(pBytes), pBitmapTextureFormat);
	}

	
	public ByteArrayBitmapTexture(final TextureManager pTextureManager, final byte[] pBytes, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions) throws IOException {
		super(pTextureManager, new ByteArrayInputStreamOpener(pBytes), pBitmapTextureFormat, pTextureOptions);
	}
	

	public ByteArrayBitmapTexture(final TextureManager pTextureManager, final byte[] pBytes, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IOException {
		super(pTextureManager, new ByteArrayInputStreamOpener(pBytes), pBitmapTextureFormat, pTextureOptions, pTextureStateListener);
	}

	
	public ByteArrayBitmapTexture(final TextureManager pTextureManager, final byte[] pBytes, final TextureOptions pTextureOptions) throws IOException {
		super(pTextureManager, new ByteArrayInputStreamOpener(pBytes), pTextureOptions);
	}

}
