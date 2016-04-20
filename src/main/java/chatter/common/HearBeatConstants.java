package chatter.common;

import io.netty.util.CharsetUtil;

/**
 * Created by c0s on 16-4-20.
 */
public interface HearBeatConstants {
    byte[] ping = "ping".getBytes(CharsetUtil.UTF_8);
    byte[] pong = "pong".getBytes(CharsetUtil.UTF_8);
}
