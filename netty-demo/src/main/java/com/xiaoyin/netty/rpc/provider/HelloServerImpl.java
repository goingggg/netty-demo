package com.xiaoyin.netty.rpc.provider;

import com.xiaoyin.netty.rpc.pubinterface.HelloServer;


public class HelloServerImpl implements HelloServer {
    public String hello(String msg) {
        if (msg != null || msg != ""){
            return "亲，你的消息为：" + msg;
        }
        return "亲，你发送的消息为空";
    }
}
