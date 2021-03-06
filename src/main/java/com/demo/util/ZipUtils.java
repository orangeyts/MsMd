package com.demo.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 参考 https://www.cnblogs.com/zeng1994/p/7862288.html
 */
public class ZipUtils {

    /**s
     * 压缩文件
     * @param srcFilePath 压缩源路径
     * @param destFilePath 压缩目的路径
     */
    public static void compress(String srcFilePath, String destFilePath) {
        //
        File src = new File(srcFilePath);

        if (!src.exists()) {
            throw new RuntimeException(srcFilePath + "不存在");
        }
        File zipFile = new File(destFilePath);

        try {

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            String baseDir = "";
            compressbyType(src, zos, baseDir);
            zos.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
    /**
     * 按照原路径的类型就行压缩。文件路径直接把文件压缩，
     * @param src
     * @param zos
     * @param baseDir
     */
    private static void compressbyType(File src, ZipOutputStream zos,String baseDir) {
        if (!src.exists()){
            return;
        }
        System.out.println("压缩路径" + baseDir + src.getName());
        //判断文件是否是文件，如果是文件调用compressFile方法,如果是路径，则调用compressDir方法；
        if (src.isFile() && !src.getName().endsWith(".zip")) {
            //src是文件，调用此方法
            compressFile(src, zos, baseDir);

        } else if (src.isDirectory()) {
            //src是文件夹，调用此方法
            compressDir(src, zos, baseDir);

        }

    }

    /**
     * 压缩文件
     */
    private static void compressFile(File file, ZipOutputStream zos,String baseDir) {
        if (!file.exists()){
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zos.putNextEntry(entry);
            int count;
            byte[] buf = new byte[1024];
            while ((count = bis.read(buf)) != -1) {
                zos.write(buf, 0, count);
            }
            bis.close();

        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    /**
     * 压缩文件夹
     */
    private static void compressDir(File dir, ZipOutputStream zos,String baseDir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if(files.length == 0){
            try {
                zos.putNextEntry(new ZipEntry(baseDir + dir.getName()+File.separator));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File file : files) {
            compressbyType(file, zos, baseDir + dir.getName() + File.separator);
        }
    }

    /**
     * 压缩一个文件夹下，可选择的几个文件
     *
     * @param srcFilePath
     * @param destFilePath
     * @param firstLevelName    srcFilePath路径下的文件 或者 文件夹的名称
     */
    public static void compressMultiFolder(String srcFilePath, String destFilePath, String... firstLevelName) throws IOException {
        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        try {
            File zipFile = new File(destFilePath);
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            File src = null;
            for (String subFileOrDir : firstLevelName) {
                src = new File(srcFilePath + File.separator + subFileOrDir);

                if (!src.exists()) {
                    throw new RuntimeException(srcFilePath + "不存在");
                }

                String baseDir = "";
                compressbyType(src, zos, baseDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null){
                zos.close();
            }
            if (fos != null){
                fos.close();
            }
        }
    }
}