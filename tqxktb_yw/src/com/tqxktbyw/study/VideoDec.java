package com.tqxktbyw.study;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tqxktbyw.videoplayer.AES;

public class VideoDec {

	// private static final String DOWNLOAD_DIRECTOTY = "tqvideoczownloads";
	private final String videoFileName = "tqxktbyw.amt";
	private Context contxt;

	public VideoDec(Context context) {
		contxt = context;
	}

	private boolean fileIsExists(String OrigFile) {
		try {
			File f = new File(OrigFile);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressLint("WorldReadableFiles")
	public String DecryptVideo(String OrigFile) throws InvalidKeyException,
			UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, ShortBufferException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {

		if (!fileIsExists(OrigFile))
			return "";

		String strV = contxt.getFilesDir().getPath() + File.separator
				+ videoFileName;
		AES myAES = new AES();
		myAES.test(OrigFile, strV);// org源地址，str目的地址
		return strV;
	}
}
