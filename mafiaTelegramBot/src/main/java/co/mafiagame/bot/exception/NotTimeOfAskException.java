package co.mafiagame.bot.exception;

import co.mafiagame.engine.exception.MafiaException;

/**
 * @author Esa Hekmatizadeh
 */
public class NotTimeOfAskException extends MafiaException {
    @Override
    public String getMessageCode() {
        return "you.cant.ask.now";
    }

    @Override
    public String[] getMessageArgs() {
        return new String[0];
    }
}
