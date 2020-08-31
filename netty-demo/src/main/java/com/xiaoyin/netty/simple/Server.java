package com.xiaoyin.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现简单TCP通信
 * @author xiaoyin
 * @date 2020/8/13 21:07
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {
        //新建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {

        //新建ServerBootstrap启动对象并配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                //使用什么channel
                .channel(NioServerSocketChannel.class)
                //设置线程队列得到的线程个数
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ServerHander());
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(7000).sync();
        System.out.println("服务器启动...");
        //对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
        }
    }
}
