package com.xiaoyin.nio.group.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊系统服务端
 * @author xiaoyin
 * @date 2020/8/10 21:30
 */
public class Server {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private static final int PORT = 7000;

    //初始化server端
    public Server() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            //bind，PORT
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            //Blocking(false)
            serverSocketChannel.configureBlocking(false);
            //register到selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) {
            try {
                int count = selector.select(2000);
                if (count > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    if (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                       if (key.isAcceptable()) {
                           SocketChannel socketChannel = serverSocketChannel.accept();
                           socketChannel.configureBlocking(false);
                           socketChannel.register(selector,SelectionKey.OP_READ);
                           System.out.println(socketChannel.getRemoteAddress()+"上线");
                       }
                       //如果通道可读
                       if (key.isReadable()) {
                            read(key);
                       }
                       //防止重复处理
                       iterator.remove();
                    }
                }else {
                    //System.out.println("等待连接客户端......");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //读取客户端数据
    public void read(SelectionKey key) {
        SocketChannel socketChannel = null;
        socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("来自客户端信息："+ msg);
                //向其他客户端发信息
                sendMsgToOtherClient(socketChannel,msg);
            }

        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线");
                key.cancel();
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    public void sendMsgToOtherClient(SocketChannel self, String msg) {
        System.out.println("信息正在转发......");
        for (SelectionKey key : selector.keys()) {
            SelectableChannel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                try {
                    ((SocketChannel) targetChannel).write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }
}
