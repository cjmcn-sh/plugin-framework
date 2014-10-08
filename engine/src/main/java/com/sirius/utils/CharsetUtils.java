/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : myctu-utils
 * user created : pippo
 * date created : 2012-9-27 - 下午12:34:44
 */
package com.sirius.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

/**
 * @since 2012-9-27
 * @author pippo
 */
public class CharsetUtils {

	//	private static final String DEFAULT_CHARSET = Charset.forName("ASCII").name();

	public static String getCharset(InputStream in) throws IOException {
		nsDetector det = new nsDetector(nsPSMDetector.ALL);
		CharsetObserver observer = new CharsetObserver();
		det.Init(observer);

		byte[] buf = new byte[1024];
		int len;
		boolean done = false;

		while ((len = in.read(buf, 0, buf.length)) != -1) {
			if (done) {
				break;
			}

			// Check if the stream is only ascii.
			boolean isAscii = det.isAscii(buf, len);
			// DoIt if non-ascii and not done yet.
			if (!isAscii) {
				done = det.DoIt(buf, len, false);
			}
		}
		det.DataEnd();
		//		System.out.println(isAscii);
		if (StringUtils.isNotBlank(observer.charset)) {
			return observer.charset;
		} else {
			return CharEncoding.UTF_8;
		}
	}

	public static String getCharset(byte[] in) throws IOException {
		return getCharset(new ByteArrayInputStream(in));
	}

	public static String encode2UTF8(byte[] _in) throws IOException {
		String charSet = getCharset(_in);
		return new String(_in, charSet);
	}

	public static String encode2UTF8(InputStream in) throws IOException {
		byte[] _in = IOUtils.toByteArray(in);
		String charSet = getCharset(_in);
		return new String(_in, charSet);
	}

	private static class CharsetObserver implements nsICharsetDetectionObserver {

		private String charset;

		public void Notify(String charset) {
			this.charset = charset;
		}

	}
}
