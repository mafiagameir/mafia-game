package co.mafiagame.engine.exception;

/**
 * @author Esa Hekmatizadeh
 */
public class CitizenNoCouldNotBeZeroException extends MafiaException {
	@Override
	public String getMessageCode() {
		return "citizen.no.is.zero";
	}

	@Override
	public String[] getMessageArgs() {
		return new String[0];
	}
}
