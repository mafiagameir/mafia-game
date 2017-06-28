package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Esa Hekmatizadeh
 */
public class TInlineKeyboardMarkup {
    @JsonProperty("inline_keyboard")
    private List<List<TInlineKeyboardButton>> inlineKeyboard = new ArrayList<>();

    public TInlineKeyboardMarkup addOptions(List<TInlineKeyboardButton> options) {
        long skipSize = 0L;
        while (options.stream().skip(skipSize).count() > 0) {
            this.inlineKeyboard.add(
                options.stream().skip(skipSize)
                    .limit(3L)
                    .collect(Collectors.toList()));
            skipSize += 3L;
        }
        return this;
    }
    public List<List<TInlineKeyboardButton>> getInlineKeyboard() {
        return inlineKeyboard;
    }

    public TInlineKeyboardMarkup setInlineKeyboard(List<List<TInlineKeyboardButton>> inlineKeyboard) {
        this.inlineKeyboard = inlineKeyboard;
        return this;
    }
}
