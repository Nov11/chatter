package chatter.common.handler.serializers;

import chatter.common.handler.serializers.Serializer;
import io.netty.buffer.ByteBuf;

import java.io.*;

/**
 * Created by c0s on 16-4-20.
 */

/**
 * note that here netty ByteBufAllocator is not utilized.
 */
public class JDKSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(obj);
        outputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] bytes, int offset, int length) throws Exception{
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes, offset, length));
        Object obj = inputStream.readObject();
        return (T)obj;
    }

    @Override
    public <T> T deserialize(ByteBuf byteBuf) throws Exception {
        byte[] bytes;
        int offset;
        int length = byteBuf.readableBytes();
        if (byteBuf.hasArray()) {
            bytes = byteBuf.array();
            offset = byteBuf.arrayOffset() + byteBuf.readerIndex();
        } else {
            bytes = new byte[length];
            offset = 0;
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
        }
        return deserialize(bytes, offset, length);
    }
}
