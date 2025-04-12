package dev.bnjc.bglib;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A wrapper class used to hold either the successful result while parsing BGI data, or an exception thrown
 * during the process.
 *
 * @param <T> The data type expected on a successful result.
 *
 * @since 0.1.0
 * @author Jack Grzechowiak
 */
public abstract class BGIParseResult<T> {
  private BGIParseResult() {}

  /**
   * A wrapper class for a successful result. Holds a value matching the given type.
   *
   * @param <T> The data type of the result value
   *
   * @since 0.1.0
   * @author Jack Grzechowiak
   */
  public static class BGIParseSuccess<T> extends BGIParseResult<T> {
    private final T value;

    private BGIParseSuccess(T value) {
      this.value = value;
    }

    /**
     * Returns the wrapped success value of type {@link T}
     *
     * @return The success value
     * @since 0.1.0
     */
    public T getValue() {
      return value;
    }

    @Override
    public BGIParseResult<T> ifSuccess(Consumer<T> consumer) {
      consumer.accept(value);
      return this;
    }

    @Override
    public BGIParseResult<T> ifError(Consumer<BGIParseException> consumer) {
      // Nothing, this is a success
      return this;
    }

    @Override
    public <R> R mapOrElse(Function<? super T, ? extends R> successFunction, Function<? super BGIParseError<T>, ? extends R> errorFunction) {
      return successFunction.apply(value);
    }
  }

  /**
   * A wrapper class for a failed result. Holds an exception value.
   *
   * @param <T> The data type of the success result value
   *
   * @since 0.1.0
   * @author Jack Grzechowiak
   */
  public static class BGIParseError<T> extends BGIParseResult<T> {
    private final BGIParseException error;

    private BGIParseError(BGIParseException error) {
      this.error = error;
    }

    /**
     * Returns the wrapped {@link BGIParseException}
     *
     * @return The exception value
     * @since 0.1.0
     */
    public BGIParseException getError() {
      return error;
    }

    @Override
    public BGIParseResult<T> ifSuccess(Consumer<T> consumer) {
      // Nothing, this is an error
      return this;
    }

    @Override
    public BGIParseResult<T> ifError(Consumer<BGIParseException> consumer) {
      consumer.accept(error);
      return this;
    }

    @Override
    public <R> R mapOrElse(Function<? super T, ? extends R> successFunction, Function<? super BGIParseError<T>, ? extends R> errorFunction) {
      return errorFunction.apply(this);
    }
  }

  /**
   * Whether this result represents a success
   *
   * @return {@code true} if this result contains a success value
   * @since 0.1.0
   */
  public boolean isSuccess() {
    return this instanceof BGIParseSuccess;
  }

  /**
   * Whether this result represents an error
   *
   * @return {@code true} if this result contains an exception value
   * @since 0.1.0
   */
  public boolean isError() {
    return this instanceof BGIParseError;
  }

  /**
   * Returns the optional successful result (empty for an error)
   *
   * @return The successful result of type {@link T}
   * @since 0.1.0
   */
  public Optional<T> result() {
    if (isSuccess()) {
      return Optional.of(((BGIParseSuccess<T>) this).value);
    }
    return Optional.empty();
  }

  /**
   * Returns the optional error result (empty for a success)
   *
   * @return The exception from the error result
   * @since 0.1.0
   */
  public Optional<BGIParseException> error() {
    if (isError()) {
      return Optional.of(((BGIParseError<T>) this).error);
    }
    return Optional.empty();
  }

  /**
   * Wraps the given value in a successful result
   *
   * @param value The success value
   * @param <T> The type of the given value
   *
   * @return The constructed {@link BGIParseSuccess} object
   * @since 0.1.0
   */
  public static <T> BGIParseSuccess<T> success(T value) {
    return new BGIParseSuccess<>(value);
  }

  /**
   * Wraps the given exception in an error result
   *
   * @param error The exception value
   * @param <T> The type of the value if it were a success
   *
   * @return The constructed {@link BGIParseError} object
   */
  public static <T> BGIParseError<T> error(BGIParseException error) {
    return new BGIParseError<T>(error);
  }

  /**
   * Wraps the given string in an error result
   *
   * @param error The string value, used to create a new exception
   * @param <T> The type of the value if it were a success
   *
   * @return The constructed {@link BGIParseError} object
   */
  public static <T> BGIParseError<T> error(String error) {
    return new BGIParseError<T>(new BGIParseException(error));
  }

  /**
   * Wraps the given string and error code in an error result
   *
   * @param error The string value, used to create a new exception
   * @param code The error code used to create a new exception
   * @param <T> The type of the value if it were a success
   *
   * @return The constructed {@link BGIParseError} object
   */
  public static <T> BGIParseError<T> error(String error, ErrorCode code) {
    return new BGIParseError<T>(new BGIParseException(error, code));
  }

  /**
   * Wraps the given error code in an error result
   *
   * @param code The error code used to create a new exception
   * @param <T> The type of the value if it were a success
   *
   * @return The constructed {@link BGIParseError} object
   */
  public static <T> BGIParseError<T> error(ErrorCode code) {
    return new BGIParseError<T>(new BGIParseException(code));
  }

  /**
   * If the result is a success, then the given consumer will be called with the success value of type {@link T}
   *
   * @param consumer The function to call if this result is a success
   *
   * @return This instance to use for chaining
   * @since 0.1.0
   */
  public abstract BGIParseResult<T> ifSuccess(Consumer<T> consumer);

  /**
   * If the result is an error, then the given consumer will be called with the {@link BGIParseException}
   *
   * @param consumer The function to call if this result is an error
   *
   * @return This instance to use for chaining
   * @since 0.1.0
   */
  public abstract BGIParseResult<T> ifError(Consumer<BGIParseException> consumer);

  /**
   * Utility used to both run functions on either a success or an error to map to a value.
   *
   * @param successFunction The function to call if this result is a success with a value of type {@link T}
   * @param errorFunction The function to call if this result is an error with a {@link BGIParseException}
   * @param <R> The return value type
   *
   * @return The mapped result of either the given success or error function
   */
  public abstract <R> R mapOrElse(Function<? super T, ? extends R> successFunction, Function<? super BGIParseError<T>, ? extends R> errorFunction);
}

