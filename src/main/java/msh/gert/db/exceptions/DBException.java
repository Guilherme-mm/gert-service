package msh.gert.db.exceptions;

public class DBException extends Exception{
    public DBException(String message, Throwable cause) {
        super(message, cause);
	}

	private static final long serialVersionUID = 1L;
}