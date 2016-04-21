package chatter.common;

import java.io.Serializable;

/**
 * Created by c0s on 16-4-20.
 */
public class ChatMessage implements Serializable {
    ChatMessagePB.ChatMessageProto chatMessageProto;

    public ChatMessage(int communicationType, String sender, String receiver, String message) {
        chatMessageProto = ChatMessagePB.ChatMessageProto.newBuilder()
                .setCommunicationType(communicationType)
                .setSender(sender)
                .setReceviver(receiver)
                .setMessage(message)
                .build();
    }

    public ChatMessagePB.ChatMessageProto getChatMessageProto() {
        return chatMessageProto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        return chatMessageProto != null ? chatMessageProto.equals(that.chatMessageProto) : that.chatMessageProto == null;

    }

    @Override
    public int hashCode() {
        return chatMessageProto != null ? chatMessageProto.hashCode() : 0;
    }
}
