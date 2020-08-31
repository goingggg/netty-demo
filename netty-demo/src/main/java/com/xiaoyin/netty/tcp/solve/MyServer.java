package com.xiaoyin.netty.tcp.solve;

/**
 * 解决TCP粘包
 * @author xiaoyin
 * @date 2020/8/20 23:22
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Handler 调用机制,编码解码机制
 * 使用Long进行编码解码
 * @author xiaoyin
 * @date 2020/8/19 21:54
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ReplayingDecoder<Void>() {
                                @Override
                                protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
                                    System.out.println("ReplayingDecoder被调用");
                                    int len = byteBuf.readInt();
                                    byte[] con = new byte[len];
                                    byteBuf.readBytes(con);

                                    MSG msg = new MSG();
                                    msg.setLen(len);
                                    msg.setContent(con);
                                    list.add(msg);
                                }
                            });
                            pipeline.addLast(new SimpleChannelInboundHandler<MSG>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, MSG msg) throws Exception {
                                    int len = msg.getLen();
                                    byte[] content = msg.getContent();
                                    System.out.println("server get:" + new String(content, Charset.forName("utf-8"))+"len="+ len);

                                }
                            });
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
