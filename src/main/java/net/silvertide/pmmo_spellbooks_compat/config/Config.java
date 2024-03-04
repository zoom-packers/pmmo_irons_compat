package net.silvertide.pmmo_spellbooks_compat.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER;
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.ConfigValue<String> HEAL_SELF_SKILL;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEAL_SELF_XP_REWARD;
    public static final ForgeConfigSpec.ConfigValue<String> HEAL_OTHER_SKILL;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEAL_OTHER_XP_REWARD;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Heal XP Configs");

        BUILDER.comment("When a spell heals the caster this skill is rewarded. Default: magic");
        HEAL_SELF_SKILL = BUILDER.define("Heal Self Skill", "magic");
        BUILDER.comment("");

        BUILDER.comment("XP awarded to the caster of when a spell heals themselves."
                , "XP given is the amount healed multiplied by this number."
                , "If you heal a player for 4 life and this is 10 then you would be rewarded with 10*4=40 xp."
                , "Set to 0 to award no experience for healing yourself.");

        HEAL_SELF_XP_REWARD = BUILDER.defineInRange("Heal Self Xp Reward", 20, 0, Integer.MAX_VALUE);
        BUILDER.comment("");

        BUILDER.comment("When a spell heals another entity this skill is rewarded to the caster. Default: magic");
        HEAL_OTHER_SKILL = BUILDER.define("Heal Other Skill", "magic");
        BUILDER.comment("");

        BUILDER.comment("XP awarded to the caster of a spell that heals another player."
                , "XP given is the amount healed multiplied by this number."
                , "If you heal a player for 4 life and this is 10 then you would be rewarded with 10*4=40 xp."
                , "Set to 0 to award no experience for healing another player.");
        HEAL_OTHER_XP_REWARD = BUILDER.defineInRange("Heal Other Xp Reward", 20, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        COMMON_CONFIG = BUILDER.build();
    }
}
