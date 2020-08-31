package com.xiaoyin.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Hander 必须继承某个ChannelInboundHandlerAdapter
 * @author xiaoyin
 * @date 2020/8/13 21:32
 */
public class ServerHander extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ChannelHandlerContext:" + ctx);
        ByteBuf byteBuf = (ByteBuf)msg;
        System.out.println("client message:" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("clinet address:" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
