package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
public class EditMessageReplyMarkupRequest {
    @JsonProperty("chat_id")
    private Long chatId;
    @JsonProperty("message_id")
    private Long messageId;
    @JsonProperty("reply_markup")
    private TInlineKeyboardMarkup replyMarkup;

    public Long getChatId() {
        return chatId;
    }

    public EditMessageReplyMarkupRequest setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public Long getMessageId() {
        return messageId;
    }

    public EditMessageReplyMarkupRequest setMessageId(Long messageId) {
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
