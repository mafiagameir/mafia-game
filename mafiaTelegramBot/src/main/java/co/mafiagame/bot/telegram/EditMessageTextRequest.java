package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditMessageTextRequest {
    @JsonProperty("chat_id")
    private Integer chatId;
    @JsonProperty("message_id")
    private Integer messageId;
    private String text;
    @JsonProperty("parse_mode")
    private String parseMode;
    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview;
    @JsonProperty("reply_markup")
    private TInlineKeyboardMarkup replyMarkup;

    public Integer getChatId() {
        return chatId;
    }

    public EditMessageTextRequest setChatId(Integer chatId) {
        this.chatId = chatId;
        return this;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public EditMessageTextRequest setMessageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getText() {
        return text;
    }

    public EditMessageTextRequest setText(String text) {
        this.text = text;
        return this;
    }

    public String getParseMode() {
        return parseMode;
    }

    public EditMessageTextRequest setParseMode(String parseMode) {
        this.parseMode = parseMode;
        return this;
    }

    public Boolean getDisableWebPagePreview() {
        return disableWebPagePreview;
    }

    public EditMessageTextRequest setDisableWebPagePreview(Boolean disableWebPagePreview) {
        this.disableWebPagePreview = disableWebPagePreview;
        return this;
    }

    public TInlineKeyboardMarkup getReplyMarkup() {
        return replyMarkup;
    }

    public EditMessageTextRequest setReplyMarkup(TInlineKeyboardMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }
}
