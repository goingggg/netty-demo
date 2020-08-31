package com.xiaoyin.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *复制文件
 * @author xiaoyin
 * @date 2020/8/9 15:16
 */
public class FileChannel02 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel2 = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(8);
        while (true) {
            //清空缓冲区（重置标志位）
            buffer.clear();
            int read = channel1.read(buffer);
            if (read == -1) {
                break;
            }
            buffer.flip();
            channel2.write(buffer);
        }

        fileOutputStream.close();
        fileInputStream.close();
    }
}
