package com.xiaoyin.nio.group.chat;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class Client {
    private SocketChannel socketChannel;
    private Selector selector;
    private static final int PORT = 7000;
    private static final String IP = "127.0.0.1";
    private String username = "";

    public Client() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "初始化完成");
    }

    public void send(String msg) throws IOException {
        msg = username + "说：" + msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));

    }
    public void read() throws IOException {
        int count = selector.select(2000);
        if (count > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel)key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    sc.read(byteBuffer);
                    String str = new String(byteBuffer.array());
                    System.out.println(str.trim());
                }
                iterator.remove();
            }
        }else {
            //System.out.println("没有可用的通道");
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        new Thread(){
            public void run() {
                while (true) {
                    try {
                        client.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            client.send(s);
        }
    }
}
