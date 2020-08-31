package com.xiaoyin.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 使用多个buffer数组
 *
 * @author xiaoyin
 * @date 2020/8/9 17:04
 */
public class ManyBuffer {
    public static void main(String[] args) throws Exception {
        //创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        serverSocketChannel.bind(inetSocketAddress);

        //创建ByteBuffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();
        //两个数组的大小
        int messageLength = 8;
        while (true) {
            int read = 0;
            while (read < messageLength) {
                long l = socketChannel.read(byteBuffers);
                read += l;
                System.out.println("读取的字节数" + read);
                Arrays.asList(byteBuffers).stream().map(buffer -> "postion:" + buffer.position() + "  limit:" + buffer.limit()).forEach(System.out::println);
            }
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
            long write = 0;
            while (write <messageLength) {
                long l = socketChannel.write(byteBuffers);
                write += l;
            }
            Arrays.asList(byteBuffers).forEach(buffer -> {buffer.clear();});
            System.out.println("read:" + read + " write:" + write + " message:" + messageLength);
        }

    }
}
