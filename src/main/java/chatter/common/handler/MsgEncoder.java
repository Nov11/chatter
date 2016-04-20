package chatter.common.handler;

import chatter.common.ChatMessage;
import chatter.common.handler.serializers.JDKSerializer;
import chatter.common.handler.serializers.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * Created by c0s on 16-4-20.
 */
public class MsgEncoder extends MessageToByteEncoder<ChatMessage>{
    private Serializer serializer;

    public MsgEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    public MsgEncoder() {
        this(new JDKSerializer());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ChatMessage msg, ByteBuf out) throws Exception {
        byte[] bytes = serializer.serialize(msg);

        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
