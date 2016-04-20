package chatter.common.handler.serializers;

import io.netty.buffer.ByteBuf;

/**
 * Created by c0s on 16-4-20.
 */
public interface Serializer {

    byte[] serialize(Object obj) throws Exception;

//    <T> T deserialize(byte[] bytes) throws Exception;

    <T> T deserialize(ByteBuf byteBuf) throws Exception;
}
