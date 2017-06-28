package co.mafiagame.bot.exception;

import co.mafiagame.engine.exception.MafiaException;

/**
 * @author Esa Hekmatizadeh
 */
public class NotTimeOfMafiaKillException extends MafiaException {
    @Override
    public String getMessageCode() {
        return "not.time.of.mafia.vote";
    }

    @Override
    public String[] getMessageArgs() {
        return new String[0];
    }
}
