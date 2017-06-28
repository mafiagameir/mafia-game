package co.mafiagame.bot.exception;

import co.mafiagame.engine.exception.MafiaException;

/**
 * @author Esa Hekmatizadeh
 */
public class GameNotStartedYetException extends MafiaException {
    @Override
    public String getMessageCode() {
        return "game.not.started.yet";
    }

    @Override
    public String[] getMessageArgs() {
        return new String[0];
    }
}
