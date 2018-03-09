package co.mafiagame.bot.telegram;

/**
 * @author Esa Hekmatizadeh
 */
public class TKeyboardButton {
    private String text;

    public String getText() {
        return text;
    }

    public TKeyboardButton setText(String text) {
        this.text = text;
        return this;
    }
}
