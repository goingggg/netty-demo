package com.xiaoyin.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent stateEvent = (IdleStateEvent) evt;
        String evenType = "";
        switch (stateEvent.state()) {
            case ALL_IDLE:
                evenType = "读写空闲";
                break;
            case READER_IDLE:
                evenType = "读空闲";
                break;
            case WRITER_IDLE:
                evenType = "写空闲";
        }
        System.out.println(ctx.channel().remoteAddress() + evenType);
    }
}
