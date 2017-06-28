package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessage {
    @JsonProperty("chat_id")
    private Integer chatId;
    private String text;
    @JsonProperty("parse_mode")
    private String parseMode;
    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    public Integer getChatId() {
        return chatId;
    }

    public SendMessage setChatId(Integer chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getText() {
        return text;
    }

    public SendMessage setText(String text) {
        this.text = text;
        return this;
    }

    public String getParseMode() {
        return parseMode;
    }

    public SendMessage setParseMode(String parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public Integer getReplyToMessageId() {
        return replyToMessageId;
    }

    public SendMessage setReplyToMessageId(Integer replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
        return this;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
            "chatId=" + chatId +
            ", text='" + text + '\'' +
            ", parseMode='" + parseMode + '\'' +
            ", replyToMessageId=" + replyToMessageId +
            '}';
    }
}
