package chatter.common.handler;

import chatter.common.ChatMessage;
import chatter.common.handler.serializers.JDKSerializer;
import chatter.common.handler.serializers.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;

/**
 * Created by c0s on 16-4-20.
 */
public class MsgDecoder extends ByteToMessageDecoder{
    Serializer serializer;

    public MsgDecoder(Serializer serializer) {
        this.serializer = serializer;
    }

    public MsgDecoder() {
        this(new JDKSerializer());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int bufLen = in.readableBytes();
        if (bufLen > 4) {
            in.markReaderIndex();
            //todo:this is not necessarily the start of a packet. it applies only no broken message is received.
            int msgLen = in.readInt();
            if (msgLen <= 0) {
                //todo:add checksum for every message
                //todo:how to recover?
                throw new DecoderException("invalid length");
            }
            if (msgLen + 4 <= bufLen) {
                ByteBuf body = in.readSlice(msgLen);
                ChatMessage chatMessage = serializer.deserialize(body);
                out.add(chatMessage);
            } else {
                in.resetReaderIndex();
            }
        }
    }
}
