package chatter.common.handler;


import chatter.common.HearBeatConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by c0s on 16-4-20.
 */
public class HeartBeatHandler extends ChannelDuplexHandler{
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(HearBeatConstants.ping);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        if (buf.readableBytes() >= HearBeatConstants.pingLen) {
            ByteBuf incoming = buf.slice(0, HearBeatConstants.pingLen);
            if (ByteBufUtil.compare(incoming, HearBeatConstants.ping) == 0) {
                ctx.writeAndFlush(HearBeatConstants.pong);
                buf.readBytes(HearBeatConstants.pingLen);
                return;
            }
        }
        super.channelRead(ctx, msg);
    }
}
