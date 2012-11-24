package ru.qixi.andengine.opengl.texture.atlas.bitmap.source;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;
import org.andengine.util.StreamUtils;
import org.andengine.util.debug.Debug;

import com.android.vending.expansion.zipfile.ZipResourceFile;



public class ZipFileBitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {

	private final ZipResourceFile mZipFile;
	private final String mZipPath;
	
	
	public static ZipFileBitmapTextureAtlasSource create(final ZipResourceFile pZipFile, final String pZipPath) {
		return ZipFileBitmapTextureAtlasSource.create(pZipFile, pZipPath, 0, 0);
	}

	
	public static ZipFileBitmapTextureAtlasSource create(final ZipResourceFile pZipFile, final String pZipPath, final int pTextureX, final int pTextureY) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inJustDecodeBounds = true;

		InputStream in = null;
		try {
			in = pZipFile.getInputStream(pZipPath);
			BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in " + ZipFileBitmapTextureAtlasSource.class.getSimpleName() + ". File: " + pZipFile, e);
		} finally {
			StreamUtils.close(in);
		}

		return new ZipFileBitmapTextureAtlasSource(pZipFile, pZipPath, pTextureX, pTextureY, decodeOptions.outWidth, decodeOptions.outHeight);
	}
	
	
	ZipFileBitmapTextureAtlasSource(final ZipResourceFile pZipFile, final String pZipPath, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		super(pTextureX, pTextureY, pTextureWidth, pTextureHeight);

		this.mZipFile = pZipFile;
		this.mZipPath = pZipPath;
	}

	
	@Override
	public ZipFileBitmapTextureAtlasSource deepCopy() {
		return new ZipFileBitmapTextureAtlasSource(this.mZipFile, this.mZipPath, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight);
	}


	@Override
	public Bitmap onLoadBitmap(final Config pBitmapConfig) {
		final BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = pBitmapConfig;

		InputStream in = null;
		try {
			in = mZipFile.getInputStream(mZipPath);
			return BitmapFactory.decodeStream(in, null, decodeOptions);
		} catch (final IOException e) {
			Debug.e("Failed loading Bitmap in " + this.getClass().getSimpleName() + ". File: " + this.mZipPath, e);
			return null;
		} finally {
			StreamUtils.close(in);
		}
	}

	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.mZipFile + ")";
	}

}