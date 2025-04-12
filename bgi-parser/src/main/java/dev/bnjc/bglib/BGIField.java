package dev.bnjc.bglib;

/**
 * A possible field found within the parsed BGI data. Each field is associated with its expected type.
 *
 * @since 0.1.0
 */
public enum BGIField {
  ITEM_ID(BGIType.STRING),
  SET(BGIType.STRING),
  TIER(BGIType.STRING),
  REVISION_ID(BGIType.INTEGER),
  DYNAMIC_LORE(BGIType.STRING_ARRAY),
  LORE(BGIType.STRING_ARRAY),
  GEM_SOCKETS(BGIType.STREAM),
  ITEM_TYPE(BGIType.STRING),
  NAME_PRE(BGIType.STRING_ARRAY),
  MAX_DURABILITY(BGIType.INTEGER),
  NAME(BGIType.STRING),
  DISABLE_SMELTING(BGIType.BOOLEAN),
  DISABLE_ENCHANTING(BGIType.BOOLEAN),
  DISABLE_CRAFTING(BGIType.BOOLEAN),
  DISABLE_SMITHING(BGIType.BOOLEAN),
  REPAIR_TYPE(BGIType.STRING),
  DISPLAYED_TYPE(BGIType.STRING),
  DEFENSE(BGIType.DOUBLE),

  ENCHANTS(BGIType.STRING_ARRAY),
  HSTRY_ENCHANTS(BGIType.STRING),

  CRITICAL_STRIKE_POWER(BGIType.DOUBLE),
  HSTRY_CRITICAL_STRIKE_POWER(BGIType.STRING),

  PVE_DAMAGE(BGIType.DOUBLE),
  HSTRY_PVE_DAMAGE(BGIType.STRING),

  PROJECTILE_DAMAGE(BGIType.DOUBLE),
  HSTRY_PROJECTILE_DAMAGE(BGIType.STRING),

  MAGIC_DAMAGE(BGIType.DOUBLE),

  MOVEMENT_SPEED(BGIType.DOUBLE),
  HSTRY_MOVEMENT_SPEED(BGIType.STRING),

  THAUMATURGY_DAMAGE(BGIType.DOUBLE),
  HSTRY_THAUMATURGY_DAMAGE(BGIType.STRING),

  BLUNT_RATING(BGIType.DOUBLE),
  HSTRY_BLUNT_RATING(BGIType.STRING);


  final BGIType type;
  BGIField(BGIType type) {
    this.type = type;
  }
}
