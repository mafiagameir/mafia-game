package co.mafiagame.engine.exception;

/**
 * @author Esa Hekmatizadeh
 */
public class RegisterAfterStartException extends MafiaException {
	@Override
	public String getMessageCode() {
		return "register.after.start";
	}

	@Override
	public String[] getMessageArgs() {
		return new String[0];
	}
}
