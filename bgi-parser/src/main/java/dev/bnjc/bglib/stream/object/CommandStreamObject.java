package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.utils.ByteParser;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CommandStreamObject extends StreamObject {
  private final List<Command> commands;

  public CommandStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.commands = new ArrayList<>();

    this.parseBuffer();
  }

  public List<Command> getCommands() {
    return this.commands;
  }

  public boolean hasCommands() {
    return !this.commands.isEmpty();
  }

  private void parseBuffer() {
    boolean hasCommands = ByteParser.getBoolean(this.buffer);

    if (hasCommands) {
      int entryCount = ByteParser.getVarInt(this.buffer);
      for (var i = 0; i < entryCount; i++) {
        String commandText = ByteParser.getString(this.buffer);
        double delay = ByteParser.getDouble(this.buffer);
        boolean console = ByteParser.getBoolean(this.buffer);
        boolean op = ByteParser.getBoolean(this.buffer);

        this.commands.add(new Command(commandText, delay, console, op));
      }
    }
  }

  @Override
  public String toString() {
    return "CommandStreamObject{" +
        "commands=" + commands +
        '}';
  }

  public record Command(String text, double delay, boolean console, boolean op) {}
}
