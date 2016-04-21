import chatter.common.ChatMessage;
import chatter.common.ChatMessagePB;
import chatter.common.handler.MsgDecoder;
import chatter.common.handler.MsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by c0s on 16-4-20.
 */
public class DecodeTest {

    @Test
    public void testDecoder(){
        EmbeddedChannel channel = new EmbeddedChannel(new de());
        ByteBuf buf = Unpooled.buffer();
        for(int i = 0; i < 10; i++) {
            buf.writeInt(i);
        }
        assertTrue(channel.writeInbound(buf));
        assertTrue(channel.finish());
        for(int i = 0; i < 10; i++) {
            assertEquals(channel.readInbound(), i);
        }
    }

    static class de extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            if (in.readableBytes() >= 4) {
                int i = in.readInt();
                out.add(i);
                System.out.println("decoder retrieved " + i);
            }
        }
    }

    @Test
    public void testMsgDecoder() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new MsgDecoder());
        ChatMessage chatMessage = new ChatMessage(0, "abc", "def", "message from abc to def");
        byte[] bytes;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(chatMessage);
            outputStream.close();
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
            return;
        }

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        assertTrue(embeddedChannel.writeInbound(buf));
        assertTrue(embeddedChannel.finish());

        Object recv = embeddedChannel.readInbound();
        assertTrue(recv instanceof ChatMessage);
        assertTrue(recv.equals(chatMessage));
    }

    @Test
    public void msgDecoderWithMsgEncoder() {
        EmbeddedChannel decode = new EmbeddedChannel(new MsgDecoder());
        EmbeddedChannel encdoe = new EmbeddedChannel(new MsgEncoder());

        ChatMessage msg = new ChatMessage(1, "1234", "5678", "from 1234 to 5678");
        assertTrue(encdoe.writeOutbound(msg));
        assertTrue(encdoe.finish());

        Object outbound = encdoe.readOutbound();
        assertNotNull(outbound);

        assertTrue(decode.writeInbound(outbound));
        assertTrue(decode.finish());

        Object recv = decode.readInbound();
        assertNotNull(recv);
        assertTrue(recv instanceof ChatMessage);

        assertTrue(recv.equals(msg));
    }

    @Test
    public void testProtobuf() {
        EmbeddedChannel encode = new EmbeddedChannel(new ProtobufVarint32LengthFieldPrepender(), new ProtobufEncoder());
        EmbeddedChannel decode = new EmbeddedChannel(new ProtobufVarint32FrameDecoder(), new ProtobufDecoder(ChatMessagePB.ChatMessageProto.getDefaultInstance()));

        ChatMessage chatMessage = new ChatMessage(0, "abcde", "fghij", "from abcde to fghij");
        assertTrue(encode.writeOutbound(chatMessage.getChatMessageProto()));
        assertTrue(encode.finish());

        Object outbound = encode.readOutbound();
        assertNotNull(outbound);

        assertTrue(decode.writeInbound(outbound));
        assertTrue(decode.finish());

        Object recv = decode.readInbound();
        assertNotNull(recv);
        assertTrue(recv instanceof ChatMessagePB.ChatMessageProto);
        assertTrue(recv.equals(chatMessage.getChatMessageProto()));
    }
}
