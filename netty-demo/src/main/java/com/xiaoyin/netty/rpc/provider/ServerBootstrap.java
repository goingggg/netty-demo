package com.xiaoyin.netty.rpc.provider;

import com.xiaoyin.netty.rpc.netty.NettyServer;

/**
 * ServerBootstrap会启用一个服务提供者。就是nettyServer
 * 此程序利用Proxy.newProxyInstance实现远程调用
 * @author xiaoyin
 * @date 2020/8/24 22:31
 */
public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.serverStart("127.0.0.1",7000);
    }
}
