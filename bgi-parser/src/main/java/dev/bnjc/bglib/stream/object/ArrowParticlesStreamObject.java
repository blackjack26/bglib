package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.exceptions.BGIParseException;
import dev.bnjc.bglib.exceptions.ErrorCode;
import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;

public class ArrowParticlesStreamObject extends VersionedStreamObject {
  private String particle;
  private int amount;
  private double offset;
  private boolean colored;
  private double speed;

  private ArrowParticlesStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);
  }

  public static ArrowParticlesStreamObject from(int key, ByteBuffer buffer) throws BGIParseException {
    var apso = new ArrowParticlesStreamObject(key, buffer);
    apso.parse();
    return apso;
  }

  public String getParticle() {
    return particle;
  }

  public int getAmount() {
    return amount;
  }

  public double getSpeed() {
    return speed;
  }

  public boolean isColored() {
    return colored;
  }

  public double getOffset() {
    return offset;
  }

  @Override
  protected void parseBuffer() throws BGIParseException {
    if (this.version > 1) {
      throw new BGIParseException("Unsupported ARROW_PARTICLES stream version [" + this.version + "]", ErrorCode.UNSUPPORTED_STREAM_VERSION);
    }

    this.particle = ByteParser.getString(this.buffer);
    this.amount = ByteParser.getVarInt(this.buffer);
    this.offset = ByteParser.getDouble(this.buffer);
    this.colored = ByteParser.getBoolean(this.buffer);
    this.speed = ByteParser.getDouble(this.buffer);
  }

  @Override
  public String toString() {
    return "ArrowParticlesStreamObject{" +
        "particle='" + particle + '\'' +
        ", amount=" + amount +
        ", offset=" + offset +
        ", colored=" + colored +
        ", speed=" + speed +
        ", version=" + version +
        '}';
  }
}
