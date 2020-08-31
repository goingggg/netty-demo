package com.xiaoyin.netty.tcp.solve;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new MessageToByteEncoder<MSG>() {
                                @Override
                                protected void encode(ChannelHandlerContext channelHandlerContext, MSG msg, ByteBuf byteBuf) throws Exception {
                                    System.out.println("MessageToByteEncoder被调用");
                                    byteBuf.writeInt(msg.getLen());
                                    byteBuf.writeBytes(msg.getContent());
                                }
                            });
                            pipeline.addLast(new SimpleChannelInboundHandler<MSG>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, MSG msg) throws Exception {

                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    for (int i = 0; i < 5; i++) {
                                        String s = "hello server";
                                        byte[] content = s.getBytes(CharsetUtil.UTF_8);
                                        int len = content.length;
                                        MSG msg = new MSG();
                                        msg.setLen(len);
                                        msg.setContent(content);
                                        ctx.writeAndFlush(msg);
                                    }
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                }
                            });

                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
