package dev.bnjc.bglib;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BGIParseResult<T> {
  // Success case: holds the value of type T
  public static class BGIParseSuccess<T> extends BGIParseResult<T> {
    private final T value;

    public BGIParseSuccess(T value) {
      this.value = value;
    }

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

  // Failure case: holds an exception
  public static class BGIParseError<T> extends BGIParseResult<T> {
    private final BGIParseException error;

    public BGIParseError(BGIParseException error) {
      this.error = error;
    }

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

  // Utility methods
  public boolean isSuccess() {
    return this instanceof BGIParseSuccess;
  }

  public boolean isError() {
    return this instanceof BGIParseError;
  }

  public Optional<T> result() {
    if (isSuccess()) {
      return Optional.of(((BGIParseSuccess<T>) this).value);
    }
    return Optional.empty();
  }

  public Optional<BGIParseException> error() {
    if (isError()) {
      return Optional.of(((BGIParseError<T>) this).error);
    }
    return Optional.empty();
  }

  // Static factory methods for ease of use
  public static <T> BGIParseSuccess<T> success(T value) {
    return new BGIParseSuccess<>(value);
  }

  public static <T> BGIParseError<T> error(BGIParseException error) {
    return new BGIParseError<T>(error);
  }

  public static <T> BGIParseError<T> error(String error) {
    return new BGIParseError<T>(new BGIParseException(error));
  }

  public static <T> BGIParseError<T> error(String error, ErrorCode code) {
    return new BGIParseError<T>(new BGIParseException(error, code));
  }

  public static <T> BGIParseError<T> error(ErrorCode code) {
    return new BGIParseError<T>(new BGIParseException(code));
  }

  // Abstract methods
  public abstract BGIParseResult<T> ifSuccess(Consumer<T> consumer);
  public abstract BGIParseResult<T> ifError(Consumer<BGIParseException> consumer);
  public abstract <R> R mapOrElse(Function<? super T, ? extends R> successFunction, Function<? super BGIParseError<T>, ? extends R> errorFunction);
}

