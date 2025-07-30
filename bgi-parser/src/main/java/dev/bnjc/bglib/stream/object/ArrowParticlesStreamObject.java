package dev.bnjc.bglib.stream.object;

import dev.bnjc.bglib.utils.ByteParser;

import java.nio.ByteBuffer;

public class ArrowParticlesStreamObject extends StreamObject {
  private boolean maybeHasParticles;
  private String particle;
  private int amount;
  private double offset;
  private boolean colored;
  private double speed;

  public ArrowParticlesStreamObject(int key, ByteBuffer buffer) {
    super(key, buffer);

    this.parseBuffer();
  }

  public boolean hasParticles() {
    return this.maybeHasParticles;
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

  private void parseBuffer() {
    this.maybeHasParticles = ByteParser.getBoolean(this.buffer);
    this.particle = ByteParser.getString(this.buffer);
    this.amount = ByteParser.getVarInt(this.buffer);
    this.offset = ByteParser.getDouble(this.buffer);
    this.colored = ByteParser.getBoolean(this.buffer);
    this.speed = ByteParser.getDouble(this.buffer);
  }

  @Override
  public String toString() {
    return "ArrowParticlesStreamObject{" +
        "hasParticles=" + maybeHasParticles +
        ", particle='" + particle + '\'' +
        ", amount=" + amount +
        ", offset=" + offset +
        ", colored=" + colored +
        ", speed=" + speed +
        '}';
  }
}
