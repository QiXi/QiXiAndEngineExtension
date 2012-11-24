package ru.qixi.andengine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

import org.andengine.util.FileUtils;

import android.content.Context;



public class QFileUtils extends FileUtils {
	
	
	public static boolean isFileExistingOnInternalStorage(final Context pContext, final String pFilePath) {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnInternalStorage(pContext, pFilePath);
		final File file = new File(absoluteFilePath);
		return file.exists() && file.isFile();
	}

	
	public static boolean isDirectoryExistingOnInternalStorage(final Context pContext, final String pDirectory) {
		final String absoluteFilePath = QFileUtils.getAbsolutePathOnInternalStorage(pContext, pDirectory);
		final File file = new File(absoluteFilePath);
		return file.exists() && file.isDirectory();
	}
	
	
	public static boolean ensureDirectoriesExistOnInternalStorage(final Context pContext, final String pDirectory) {
		if (QFileUtils.isDirectoryExistingOnInternalStorage(pContext, pDirectory)) {
			return true;
		}

		final String absoluteDirectoryPath = QFileUtils.getAbsolutePathOnInternalStorage(pContext, pDirectory);
		return new File(absoluteDirectoryPath).mkdirs();
	}

	
	public static InputStream openOnInternalStorage(final Context pContext, final String pFilePath) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnInternalStorage(pContext, pFilePath);
		return new FileInputStream(absoluteFilePath);
	}

	
	public static String[] getDirectoryListOnInternalStorage(final Context pContext, final String pFilePath) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnInternalStorage(pContext, pFilePath);
		return new File(absoluteFilePath).list();
	}

	
	public static String[] getDirectoryListOnInternalStorage(final Context pContext, final String pFilePath, final FilenameFilter pFilenameFilter) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnInternalStorage(pContext, pFilePath);
		return new File(absoluteFilePath).list(pFilenameFilter);
	}
	
	
	

	
}
