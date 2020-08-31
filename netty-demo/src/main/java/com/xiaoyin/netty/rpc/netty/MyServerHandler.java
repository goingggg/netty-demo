package com.xiaoyin.netty.rpc.netty;

import com.xiaoyin.netty.rpc.provider.HelloServerImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("MyHandler msg:" + msg);
        //#为协议头
        if (msg.toString().startsWith("#")) {
            //截取协议头后面的内容
            String result = new HelloServerImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            //写入到channel
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
