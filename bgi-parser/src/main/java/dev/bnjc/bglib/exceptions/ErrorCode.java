package dev.bnjc.bglib.exceptions;

/**
 * Contains error codes for exceptions that can occur during BGI parsing.
 *
 * @since 0.1.0
 * @author Jack Grzechowiak
 */
public enum ErrorCode {
  DEFAULT(0, "An unexpected error occurred"),
  MISSING_TAG(1, "Missing custom BGI tag"),
  DATA_TOO_SHORT(2, "Data is too short to be valid"),
  UNKNOWN_DATA_TYPE(3, "Unknown data type"),
  GOBLINLESS(7, "Not goblin enough to be BGI data");

  // The actual error code and message
  private final int code;
  private final String message;

  // Constructor to set the error code and message
  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  // Getters for code and message
  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
