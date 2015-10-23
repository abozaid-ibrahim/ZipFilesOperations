package com.timore.zipfilesoperations;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Abuzeid on 10/21/2015.
 */
public class ZipFile {
    private static final int BUFFER_SIZE = 1024;

    public static void zip(String[] files, String zipFile) throws IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } finally {
                    origin.close();
                }
            }
        } finally {
            out.close();
        }
    }

    public static void unzip(String zipFile, String unzipPath, ZipInputStream zin) throws IOException {
        try {


            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                System.err.println("ZE :" + ze.getName() + "  Size: " + ze.getSize());
                unzipThread(zin, ze, unzipPath);
            }

        } catch (Exception e) {
            Log.e("ZIP FILE", "Unzip exception", e);
        }
    }

    private static void unzipThread(final ZipInputStream zin, final ZipEntry ze, final String unzipPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("EXTRAXTING %%%%   WHILE ^^^  GET NEXT ENTRY " + System.currentTimeMillis());
                String path = unzipPath + ze.getName();

                if (ze.isDirectory()) {
                    File unzipFile = new File(path);
                    if (!unzipFile.isDirectory())
                        unzipFile.mkdirs();

                } else {
                    FileOutputStream fout = null;

                    try {
                        fout = new FileOutputStream(path, false);
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        zin.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }).start();

    }

    public static void zipImage() {

        Bitmap mBackground = null;
        try {
            FileInputStream fis = new FileInputStream("/sdcard/Image/Images.zip");
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().equals("Background.png")) {
                    mBackground = BitmapFactory.decodeStream(zis);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    void zi(String filename) {
//        ZipFile zipFile = new ZipFile();
//        for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements(); ) {
//            ZipEntry ze = e.nextElement();
//            Bitmap bm = BitmapFactory.decodeStream(zipFile.getInputStream(ze));
//
//        }
//    }
}
