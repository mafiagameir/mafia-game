package co.mafiagame.engine.exception;

import co.mafiagame.engine.GameMood;

/**
 * @author Esa Hekmatizadeh
 */
public class CommandNotAvailableInThisMoodException extends MafiaException {
	private final GameMood gameMood;
	private final String command;

	public CommandNotAvailableInThisMoodException(String command, GameMood gameMood) {
		this.command = command;
		this.gameMood = gameMood;
	}

	@Override
	public String getMessageCode() {
		return "command.is.not.available.in.current.mood";
	}

	@Override
	public String[] getMessageArgs() {
		return new String[] {command, gameMood.toString()};
	}
}
