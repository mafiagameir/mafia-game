package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageWithReplyKeyboard extends SendMessage {

    @JsonProperty("reply_markup")
    private TReplyKeyboardMarkup replyMarkup;

    public TReplyKeyboardMarkup getReplyMarkup() {
        return replyMarkup;
    }

    public SendMessageWithReplyKeyboard setReplyMarkup(TReplyKeyboardMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }
}
