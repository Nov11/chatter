package chatter.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * Created by c0s on 16-4-20.
 */
public interface HearBeatConstants {
    ByteBuf ping = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("ping".getBytes(CharsetUtil.UTF_8)));
    int pingLen = ping.readableBytes();
    ByteBuf pong = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("pong".getBytes(CharsetUtil.UTF_8)));
    int pongLen = pong.readableBytes();
}
