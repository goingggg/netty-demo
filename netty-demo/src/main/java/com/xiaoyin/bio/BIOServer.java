package com.xiaoyin.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO
 * 1.使用线程池实现简单通信
 * 使用telnet 127.0.0.1 [port]
 * 按下ctrl+] 键
 *     send [内容]
 *
 * 2.验证每次连接一个线程
 * 3.验证线程阻塞
 *
 * @author xiaoyin
 * @date 2020/8/9 9:55
 */
public class BIOServer {
    public static void main(String[] args) throws Exception {
        //创建线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ServerSocket socket = new ServerSocket(9090);
        System.out.println("连接创建");
        while (true) {
            //socket.accept()验证阻塞
            System.out.println("正在连接到一个客户端");
            final Socket socket1 = socket.accept();
            System.out.println("连接到 一个客户端");
            cachedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        handle(socket1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void handle(Socket socket) throws Exception {
        //验证每个连接一个线程
        System.out.println(Thread.currentThread().getId());
        byte[] bytes = new byte[1024];
        //可验证阻塞
        InputStream inputStream = socket.getInputStream();
        while (true) {
            int read = inputStream.read(bytes);
            if (read != -1) {
                System.out.println(new String(bytes, 0, read));
            } else {
                break;
            }
        }
        socket.close();
        System.out.println("socket关闭");
    }
}
