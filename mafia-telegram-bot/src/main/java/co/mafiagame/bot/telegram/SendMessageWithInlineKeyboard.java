package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageWithInlineKeyboard extends SendMessage {

    @JsonProperty("reply_markup")
    private TInlineKeyboardMarkup replyMarkup;

    public TInlineKeyboardMarkup getReplyMarkup() {
        return replyMarkup;
    }

    public SendMessageWithInlineKeyboard setReplyMarkup(TInlineKeyboardMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }
}
