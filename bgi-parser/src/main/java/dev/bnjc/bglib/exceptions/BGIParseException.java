package dev.bnjc.bglib.exceptions;

/**
 * This exception is raised if there is an issue that occurs while the BGI data is being parsed.
 *
 * @since 0.1.0
 * @author Jack Grzechowiak
 */
public class BGIParseException extends Exception {
  private final ErrorCode errorCode;

  public BGIParseException() {
    this(ErrorCode.DEFAULT);
  }

  public BGIParseException(ErrorCode code) {
    super();
    errorCode = code;
  }

  public BGIParseException(String message) {
    this(message, ErrorCode.DEFAULT);
  }

  public BGIParseException(String message, ErrorCode code) {
    super(message);
    errorCode = code;
  }

  public BGIParseException(String message, Throwable cause) {
    this(message, cause, ErrorCode.DEFAULT);
  }

  public BGIParseException(String message, Throwable cause, ErrorCode code) {
    super(message, cause);
    errorCode = code;
  }

  public BGIParseException(Throwable cause) {
    this(cause, ErrorCode.DEFAULT);
  }

  public BGIParseException(Throwable cause, ErrorCode code) {
    super(cause);
    errorCode = code;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    String msg = super.getMessage();
    if (msg == null || msg.isEmpty()) {
      return errorCode.getMessage();
    }

    return errorCode.getMessage() + ": " + msg;
  }
}
