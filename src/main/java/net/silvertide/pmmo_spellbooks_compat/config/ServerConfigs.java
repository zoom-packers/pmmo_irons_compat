package net.silvertide.pmmo_spellbooks_compat.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_CAST_REQ;
    static {
        BUILDER.comment("Other Configuration");
        BUILDER.push("Misc");

        BUILDER.comment("Whether or not imbued weapons require mana to be casted. Default: true");
        ENABLE_CAST_REQ = BUILDER.worldRestart().define("swordsConsumeMana", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
