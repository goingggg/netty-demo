package com.xiaoyin.nio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 简单使用FileChannel
 * 写入文件文件
 * @author xiaoyin
 * @date 2020/8/9 13:16
 */
public class FileChannel01 {
    public static void main(String[] args) throws Exception {
        File file = new File("d:\\FileChannel.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //把channel中的放到buffer
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
    }

    public void write() throws Exception{
        //放入文件的内容
        String s = "hello File Channel";
        //ByteBuffer.allocate(1024)创建ByteBuffer并给定大小
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //把string放入byteBuffer
        byteBuffer.put(s.getBytes());
        //读写反转
        byteBuffer.flip();

        FileOutputStream fileOutputStream = new FileOutputStream("d:\\FileChannel.txt");
        //把FileOutputStream包装成FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();
        //包buffer放到channel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
