package dev.bnjc.bglib;

/**
 * A possible field found within the parsed BGI data. Each field is associated with its expected type.
 *
 * @since 0.1.0
 */
public enum BGIField {
  ABILITY(BGIType.STREAM),

  // Additional Experience
  ADDITIONAL_EXPERIENCE_ALCHEMY(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_ARCHAEOLOGY(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_COOKING(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_FISHING(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_HERBALISM(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_HUNTING(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_LOGGING(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_MINING(BGIType.DOUBLE),
  ADDITIONAL_EXPERIENCE_RUNECARVING(BGIType.DOUBLE),

  ARROW_PARTICLES(BGIType.STREAM),
  ARROW_VELOCITY(BGIType.DOUBLE),
  ATTACK_DAMAGE(BGIType.DOUBLE),
  ATTACK_SPEED(BGIType.DOUBLE),
  BLOCK_COOLDOWN_REDUCTION(BGIType.DOUBLE),
  BLOCK_POWER(BGIType.DOUBLE),
  BLOCK_RATING(BGIType.DOUBLE),
  BLUNT_POWER(BGIType.DOUBLE),
  BLUNT_RATING(BGIType.DOUBLE),
  CAN_DECONSTRUCT(BGIType.BOOLEAN),
  COMMANDS(BGIType.STREAM),
//  COMPASS_TARGET(BGIType.STREAM),
  CONSUMABLE_BUFFS(BGIType.STREAM),
  COOLDOWN_REDUCTION(BGIType.DOUBLE),
  CRAFT_PERMISSION(BGIType.STRING),
  RECIPE_ID(BGIType.STRING),
  CRITICAL_FISHING_CHANCE(BGIType.DOUBLE),
  CRITICAL_STRIKE_CHANCE(BGIType.DOUBLE),
  CRITICAL_STRIKE_POWER(BGIType.DOUBLE),
  CUSTOM_NBT(BGIType.STRING_ARRAY),
  DAMAGE_REDUCTION(BGIType.DOUBLE),
  DEFENSE(BGIType.DOUBLE),

  // Disable Interaction
  DISABLE_ARROW_SHOOTING(BGIType.BOOLEAN),
  DISABLE_ATTACK_PASSIVE(BGIType.BOOLEAN),
  DISABLE_CRAFTING(BGIType.BOOLEAN),
  DISABLE_DURING_PVP(BGIType.BOOLEAN),
  DISABLE_ENCHANTING(BGIType.BOOLEAN),
  DISABLE_INTERACTION(BGIType.BOOLEAN),
  DISABLE_REPAIRING(BGIType.BOOLEAN),
  DISABLE_SMELTING(BGIType.BOOLEAN),
  DISABLE_SMITHING(BGIType.BOOLEAN),

  DISPLAYED_TYPE(BGIType.STRING),
  DURABILITY_RESILIENCE(BGIType.DOUBLE),
  DYNAMIC_LORE(BGIType.STRING_ARRAY),
  EFFECTS(BGIType.STREAM),
  ENCHANTS(BGIType.STRING_ARRAY),
  FALL_DAMAGE_REDUCTION(BGIType.DOUBLE),
  FOOD(BGIType.DOUBLE),
  GEM_COLOR(BGIType.STRING),
  GEM_SOCKETS(BGIType.STREAM),
  GEM_UPGRADE_SCALING(BGIType.STRING),
//  GRANTED_PERMISSIONS(BGIType.STRING_ARRAY),
  HANDWORN(BGIType.BOOLEAN),
  HEALING_RECEIVED(BGIType.DOUBLE),
  HEALTH_REGENERATION(BGIType.DOUBLE),
  HSTRY_ABILITY(BGIType.STREAM),

  // HStry Additional Experience
  HSTRY_ADDITIONAL_EXPERIENCE(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_ALCHEMY(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_ARCHAEOLOGY(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_COOKING(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_FISHING(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_HERBALISM(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_HUNTING(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_LOGGING(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_MINING(BGIType.STREAM),
  HSTRY_ADDITIONAL_EXPERIENCE_RUNECARVING(BGIType.STREAM),

  HSTRY_ATTACK_DAMAGE(BGIType.STREAM),
  HSTRY_BACKSTAB_DAMAGE(BGIType.STREAM),
  HSTRY_BLOCK_COOLDOWN_REDUCTION(BGIType.STREAM),
  HSTRY_BLOCK_POWER(BGIType.STREAM),
  HSTRY_BLOCK_RATING(BGIType.STREAM),
  HSTRY_BLUNT_POWER(BGIType.STREAM),
  HSTRY_BLUNT_RATING(BGIType.STREAM),
  HSTRY_COOLDOWN_REDUCTION(BGIType.STREAM),
  HSTRY_CRITICAL_STRIKE_CHANCE(BGIType.STREAM),
  HSTRY_CRITICAL_STRIKE_POWER(BGIType.STREAM),
  HSTRY_DAMAGE_REDUCTION(BGIType.STREAM),
  HSTRY_DEFENSE(BGIType.STREAM),
  HSTRY_ENCHANTS(BGIType.STREAM),
  HSTRY_FALL_DAMAGE_REDUCTION(BGIType.STREAM),
  HSTRY_HEALING_RECEIVED(BGIType.STREAM),
  HSTRY_HEALTH_REGENERATION(BGIType.STREAM),
  HSTRY_KNOCKBACK_RESISTANCE(BGIType.STREAM),
  HSTRY_LORE(BGIType.STREAM),
  HSTRY_MAGIC_DAMAGE(BGIType.STREAM),
  HSTRY_MAX_HEALTH(BGIType.STREAM),
  HSTRY_MOVEMENT_SPEED(BGIType.STREAM),
  HSTRY_NAME(BGIType.STREAM),
  HSTRY_PROJECTILE_DAMAGE(BGIType.STREAM),
  HSTRY_PVE_DAMAGE(BGIType.STREAM),
  HSTRY_PVE_DAMAGE_REDUCTION(BGIType.STREAM),
  HSTRY_PVP_DAMAGE(BGIType.STREAM),
  HSTRY_PVP_DAMAGE_REDUCTION(BGIType.STREAM),
  HSTRY_SKILL_ARCHAEOLOGY(BGIType.STREAM),
  HSTRY_SKILL_FISHING(BGIType.STREAM),
  HSTRY_SKILL_HERBALISM(BGIType.STREAM),
  HSTRY_SKILL_LOGGING(BGIType.STREAM),
  HSTRY_SKILL_MINING(BGIType.STREAM),
  HSTRY_THAUMATURGY_DAMAGE(BGIType.STREAM),
  HSTRY_WEAPON_DAMAGE(BGIType.STREAM),
  INEDIBLE(BGIType.BOOLEAN),
  ITEM_COOLDOWN(BGIType.DOUBLE),
  ITEM_ID(BGIType.STRING),
  ITEM_TYPE(BGIType.STRING),
  KNOCKBACK_RESISTANCE(BGIType.DOUBLE),
  LORE(BGIType.STRING_ARRAY),
  LORE_FORMAT(BGIType.STRING),
  MAGIC_DAMAGE(BGIType.DOUBLE),
  MATERIAL(BGIType.STRING),
  MAX_CONSUME(BGIType.DOUBLE),
  MAX_DURABILITY(BGIType.INTEGER),
  MAX_HEALTH(BGIType.DOUBLE),
  MOVEMENT_SPEED(BGIType.DOUBLE),
  NAME(BGIType.STRING),
  NAME_PRE(BGIType.STRING_ARRAY),
  NAME_SUF(BGIType.STRING_ARRAY),
  PERMISSION(BGIType.STRING),
  PERM_EFFECTS(BGIType.STREAM),
  PROJECTILE_DAMAGE(BGIType.DOUBLE),
  RANDOM_UNSOCKET(BGIType.DOUBLE),
  RARITY(BGIType.STRING),
  REPAIR_PERCENT(BGIType.DOUBLE),
  REPAIR_TYPE(BGIType.STRING),

  // Required Level
  REQUIRED_LEVEL(BGIType.INTEGER),
  REQUIRED_LEVEL_ALCHEMY(BGIType.DOUBLE),
  REQUIRED_LEVEL_ARCHAEOLOGY(BGIType.DOUBLE),
  REQUIRED_LEVEL_COOKING(BGIType.DOUBLE),
  REQUIRED_LEVEL_FISHING(BGIType.DOUBLE),
  REQUIRED_LEVEL_HERBALISM(BGIType.DOUBLE),
  REQUIRED_LEVEL_HUNTING(BGIType.DOUBLE),
  REQUIRED_LEVEL_LOGGING(BGIType.DOUBLE),
  REQUIRED_LEVEL_MINING(BGIType.DOUBLE),
  REQUIRED_LEVEL_RUNECARVING(BGIType.DOUBLE),

  RESTORE_FOOD(BGIType.DOUBLE),
  RESTORE_HEALTH(BGIType.DOUBLE),
  RESTORE_SATURATION(BGIType.DOUBLE),
  REVISION_ID(BGIType.INTEGER),
  SET(BGIType.STRING),
  SKILL_ARCHAEOLOGY(BGIType.DOUBLE),
  SKILL_FISHING(BGIType.DOUBLE),
  SKILL_HERBALISM(BGIType.DOUBLE),
  SKILL_LOGGING(BGIType.DOUBLE),
  SKILL_MINING(BGIType.DOUBLE),
  SOUND_ON_CONSUME(BGIType.STRING),
  SOUND_ON_CONSUME_PIT(BGIType.DOUBLE),
  SOUND_ON_CONSUME_VOL(BGIType.DOUBLE),
  STARTS_QUEST(BGIType.STRING),
  THAUMATURGY_DAMAGE(BGIType.DOUBLE),
  TIER(BGIType.STRING),
  TRIM_MATERIAL(BGIType.STRING),
  TRIM_PATTERN(BGIType.STRING),
  TWO_HANDED(BGIType.BOOLEAN),
  UNSTACKABLE(BGIType.BOOLEAN),
  UNSTACKABLE_UUID(BGIType.STRING),
  VANILLA_EATING(BGIType.BOOLEAN),
  WEAPON_DAMAGE(BGIType.DOUBLE),
  WILL_BREAK(BGIType.BOOLEAN);

  public final BGIType type;
  BGIField(BGIType type) {
    this.type = type;
  }

  public int key() {
    return this.name().hashCode();
  }
}
