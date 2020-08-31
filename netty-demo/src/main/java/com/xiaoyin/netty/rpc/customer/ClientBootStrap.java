package com.xiaoyin.netty.rpc.customer;

import com.xiaoyin.netty.rpc.netty.NettyClient;
import com.xiaoyin.netty.rpc.pubinterface.HelloServer;

public class ClientBootStrap {
    //定义通信的协议头
    public  static final  String provider = "#";

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        HelloServer helloServer = (HelloServer) nettyClient.getBean(HelloServer.class,provider);
        String str = helloServer.hello("hello xiaoyin");
        System.out.println("调用结果：" + str);
    }
}
