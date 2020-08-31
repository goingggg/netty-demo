package com.xiaoyin.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class MyServevrHander extends SimpleChannelInboundHandler<HttpObject> {
    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //如果解析的类型是HttpRequest
        if (httpObject instanceof HttpRequest) {
            System.out.println("msg type:" + httpObject.getClass());
            System.out.println("address:"+ channelHandlerContext.channel().remoteAddress());
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello I am server", CharsetUtil.UTF_8);
            //构造一个http的response
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            //设置http头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());
            channelHandlerContext.writeAndFlush(response);

        }
    }
}
