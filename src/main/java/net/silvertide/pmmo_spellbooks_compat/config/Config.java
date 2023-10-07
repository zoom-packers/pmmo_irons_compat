package net.silvertide.pmmo_spellbooks_compat.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER;
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.ConfigValue<String> HEAL_SKILL;
    public static final ForgeConfigSpec.ConfigValue<Integer> HEAL_XP_REWARD;

    static {
        BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Heal XP Configs");

        BUILDER.comment("When a spell heals another entity this skill is rewarded to the caster. Default: endurance");
        HEAL_SKILL = BUILDER.define("Heal Skill", "endurance");
        BUILDER.comment("");
        BUILDER.comment("XP awarded to the caster of a spell that heals another player."
                , "Amount healed is the amount healed multiplied by this number."
                , "If you heal a player for 4 life and this is 10 then you would be rewarded with 10*4=40 xp."
                , "Set to 0 to award no experience for healing another player."
                , "This xp is not rewarded for healing yourself because PMMO already gives xp for that.");
        HEAL_XP_REWARD = BUILDER.defineInRange("Heal Xp Reward", 10, 0, Integer.MAX_VALUE);

        BUILDER.pop();

        COMMON_CONFIG = BUILDER.build();
    }
}
