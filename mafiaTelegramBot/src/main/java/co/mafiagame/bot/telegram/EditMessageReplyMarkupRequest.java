package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
public class EditMessageReplyMarkupRequest {
    @JsonProperty("chat_id")
    private Integer chatId;
    @JsonProperty("message_id")
    private Integer messageId;
    @JsonProperty("reply_markup")
    private TInlineKeyboardMarkup replyMarkup;

    public Integer getChatId() {
        return chatId;
    }

    public EditMessageReplyMarkupRequest setChatId(Integer chatId) {
        this.chatId = chatId;
        return this;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public EditMessageReplyMarkupRequest setMessageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public TInlineKeyboardMarkup getReplyMarkup() {
        return replyMarkup;
    }

    public EditMessageReplyMarkupRequest setReplyMarkup(TInlineKeyboardMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }
}
