package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
public class TInlineKeyboardButton {
    private String text;
    @JsonProperty("callback_data")
    private String callbackData;

    public String getText() {
        return text;
    }

    public TInlineKeyboardButton setText(String text) {
        this.text = text;
        return this;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public TInlineKeyboardButton setCallbackData(String callbackData) {
        this.callbackData = callbackData;
        return this;
    }
}
