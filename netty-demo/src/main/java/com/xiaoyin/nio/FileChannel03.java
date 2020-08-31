package com.xiaoyin.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 使用transferFrom拷贝文件
 *
 * @author xiaoyin
 * @date 2020/8/9 15:51
 */
public class FileChannel03 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel sour = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("3.txt");
        FileChannel tager = fileOutputStream.getChannel();

        tager.transferFrom(sour, 0, sour.size());

        fileOutputStream.close();
        fileInputStream.close();


    }
}
