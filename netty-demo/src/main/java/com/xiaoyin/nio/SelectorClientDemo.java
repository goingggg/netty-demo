package com.xiaoyin.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ServiceSelectorDemo类关联
 * @author xiaoyin
 * @date 2020/8/10 18:22
 */
public class SelectorClientDemo {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",7000);
        socketChannel.configureBlocking(false);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接超时，正在重试......");
            }
            String s = "hello service";
            ByteBuffer byteBuffer = ByteBuffer.wrap(s.getBytes());
            socketChannel.write(byteBuffer);
            System.in.read();
        }
    }
}
