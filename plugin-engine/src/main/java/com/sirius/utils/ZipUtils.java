/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : antlogistics-core
 * user created : pippo
 * date created : 2008-9-9 - 上午11:48:30
 */
package com.sirius.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

/**
 * @since 2008-9-9
 * @author pippo
 */
public class ZipUtils {

    static final int BUFFER = 2048;

    public static void unzip(File zipFile, String destDir) throws IOException {
        FileUtils.forceMkdir(new File(destDir));

        BufferedOutputStream dest = null;
        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                (new File(destDir + File.separator + entry.getName())).mkdir();
                continue;
            }
            int count;
            byte[] data = new byte[BUFFER];
            FileOutputStream fos = new FileOutputStream(destDir + File.separator + entry.getName());
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
        zis.close();
    }

}
