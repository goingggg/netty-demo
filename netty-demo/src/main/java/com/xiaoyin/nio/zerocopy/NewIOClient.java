package com.xiaoyin.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",7000));
        String fileName = "1.txt";
        long startTime = System.currentTimeMillis();
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //transferTo:0拷贝，win操作系统下限制8m大小
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("字节数："+ transferCount);
        System.out.println("花费时间：" + (System.currentTimeMillis() - startTime));
        fileChannel.close();
    }
}
