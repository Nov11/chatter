package chatter.common;

import java.io.Serializable;

/**
 * Created by c0s on 16-4-20.
 */
public class ChatMessage implements Serializable{
    int communicationType;
    String sender;
    String receiver;
    String message;

    public ChatMessage(int communicationType, String sender, String receiver, String message) {
        this.communicationType = communicationType;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public int getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(int communicationType) {
        this.communicationType = communicationType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "communicationType=" + communicationType +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (communicationType != that.communicationType) return false;
        if (sender != null ? !sender.equals(that.sender) : that.sender != null) return false;
        if (receiver != null ? !receiver.equals(that.receiver) : that.receiver != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = communicationType;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
