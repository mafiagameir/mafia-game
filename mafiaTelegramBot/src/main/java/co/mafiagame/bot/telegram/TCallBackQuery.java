package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TCallBackQuery {
    private String id;
    private TUser from;
    private TMessage message;
    @JsonProperty("inline_message_id")
    private String inlineMessageId;
    @JsonProperty("chat_instance")
    private String chatInstance;
    private String data;

    public String getId() {
        return id;
    }

    public TCallBackQuery setId(String id) {
        this.id = id;
        return this;
    }

    public TUser getFrom() {
        return from;
    }

    public TCallBackQuery setFrom(TUser from) {
        this.from = from;
        return this;
    }

    public TMessage getMessage() {
        return message;
    }

    public TCallBackQuery setMessage(TMessage message) {
        this.message = message;
        return this;
    }

    public String getInlineMessageId() {
        return inlineMessageId;
    }

    public TCallBackQuery setInlineMessageId(String inlineMessageId) {
        this.inlineMessageId = inlineMessageId;
        return this;
    }

    public String getChatInstance() {
        return chatInstance;
    }

    public TCallBackQuery setChatInstance(String chatInstance) {
        this.chatInstance = chatInstance;
        return this;
    }

    public String getData() {
        return data;
    }

    public TCallBackQuery setData(String data) {
        this.data = data;
        return this;
    }
}
