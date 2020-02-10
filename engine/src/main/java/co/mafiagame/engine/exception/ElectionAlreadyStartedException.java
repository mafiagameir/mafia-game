package co.mafiagame.engine.exception;

/**
 * @author Esa Hekmatizadeh
 */
public class ElectionAlreadyStartedException extends MafiaException {
	@Override
	public String getMessageCode() {
		return "election.already.started";
	}

	@Override
	public String[] getMessageArgs() {
		return new String[0];
	}
}
