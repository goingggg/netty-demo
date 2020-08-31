package com.xiaoyin.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SelectorServiceDemo {
    public static void main(String[] args) throws  Exception {
        //得到ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置为非阻塞Channel
        serverSocketChannel.configureBlocking(false);
        //得到Selector
        Selector selector = Selector.open();
        //把Channel绑定到7000port
        serverSocketChannel.socket().bind(new InetSocketAddress(7000));
        //注册到Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待1秒无连接");
                continue;
            }
            //拿到selectedKeys集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                //如果有新连接
                if (key.isAcceptable()) {

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("连接成功socketChannel.hashCode:"+ socketChannel.hashCode() );
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from客户端:"+ new String(buffer.array()));
                }
                //防止多线程重复操作
                keyIterator.remove();
            }
        }
    }
}
