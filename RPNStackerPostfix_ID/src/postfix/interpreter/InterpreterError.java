package postfix.interpreter;


public class InterpreterError extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public InterpreterError(String msg) {
		super(msg);
	}
}
