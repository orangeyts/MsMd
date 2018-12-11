package com.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * https://www.cnblogs.com/rainmer/p/4605064.html
 * Created by Rainmer on 2015/6/28.
 */
public class FileUtils {
    public static void main(String[] args) {
        String oldPath = "E:\\xfqcodetemplate\\src";
        String newPath = "E:\\xfqcodetemplate\\target";
        File dirNew = new File(newPath);
        dirNew.mkdirs();//可以在不存在的目录中创建文件夹
        directory(oldPath, newPath);
        System.out.println("复制文件夹成功");
    }

    /**
     * 复制单个文件
     * @param oldPath 要复制的文件名
     * @param newPath 目标文件名
     */
    public static void copyfile(String oldPath, String newPath) {
        int hasRead = 0;
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(oldFile);//读入原文件
                FileOutputStream fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((hasRead = fis.read(buffer)) != -1) {//当文件没有读到结尾
                    fos.write(buffer, 0, hasRead);//写文件
                }
                fis.close();
            } catch (Exception e) {
                System.out.println("复制单个文件操作出错！");
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param oldPath 要复制的文件夹路径
     * @param newPath 目标文件夹路径
     */
    public static void directory(String oldPath, String newPath) {
        File f1 = new File(oldPath);
        File[] files = f1.listFiles();//listFiles能够获取当前文件夹下的所有文件和文件夹
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                File dirNew = new File(newPath + File.separator + files[i].getName());
                dirNew.mkdir();//在目标文件夹中创建文件夹
                //递归
                directory(oldPath + File.separator + files[i].getName(), newPath + File.separator + files[i].getName());
            } else {
                String filePath = newPath + File.separator + files[i].getName();
                copyfile(files[i].getAbsolutePath(), filePath);
            }

        }
    }
}
