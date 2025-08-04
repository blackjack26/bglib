package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CommandStreamObject extends VersionedStreamObject {
  private final List<Command> commands;

  private CommandStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.commands = new ArrayList<>();
  }

  public static CommandStreamObject from(int key, ByteBuffer buffer) throws BGIParseException {
    var cso = new CommandStreamObject(key, buffer);
    cso.parse();
    return cso;
  }

  public List<Command> getCommands() {
    return this.commands;
  }

  public boolean hasCommands() {
    return !this.commands.isEmpty();
  }

  @Override
  protected void parseBuffer() throws BGIParseException {
    if (this.version > 1) {
      throw new BGIParseException("Unsupported COMMANDS stream version [" + this.version + "]", ErrorCode.UNSUPPORTED_STREAM_VERSION);
    }

    int entryCount = ByteParser.getVarInt(this.buffer);
    for (var i = 0; i < entryCount; i++) {
      String commandText = ByteParser.getString(this.buffer);
      double delay = ByteParser.getDouble(this.buffer);
      boolean console = ByteParser.getBoolean(this.buffer);
      boolean op = ByteParser.getBoolean(this.buffer);

      this.commands.add(new Command(commandText, delay, console, op));
    }
  }

  @Override
  public String toString() {
    return "CommandStreamObject{" +
        "commands=" + commands +
        ", version=" + version +
        '}';
  }

  public record Command(String text, double delay, boolean console, boolean op) {}
}
