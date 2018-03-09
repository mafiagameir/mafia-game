package co.mafiagame.engine.exception;

/**
 * @author Esa Hekmatizadeh
 */
public class MafiaNoCouldNotBeZeroException extends MafiaException {
	@Override
	public String getMessageCode() {
		return "mafia.no.is.zero";
	}

	@Override
	public String[] getMessageArgs() {
		return new String[0];
	}
}
