package com.xiaoyin.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class MyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    ChannelHandlerContext context;
    //返回的结果
   String result;
   //传入的参数
   String param;

   //启动时获取context(1)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }
    //设置传入的参数(2)
   void setParam(String param){
       this.param = param;
   }
    //读取(4)
    @Override
    public  synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        //唤醒等待的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }
    //调用call方法context.writeAndFlush(param)(3)
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        //唤醒后返回结果
        wait();
        return result;
    }
}
